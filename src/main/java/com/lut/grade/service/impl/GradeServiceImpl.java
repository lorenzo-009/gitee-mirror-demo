package com.lut.grade.service.impl;

import com.lut.grade.dao.CourseDao;
import com.lut.grade.dao.GradeDao;
import com.lut.grade.dao.impl.CourseDaoImpl;
import com.lut.grade.dao.impl.GradeDaoImpl;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.entity.Course;
import com.lut.grade.model.vo.CourseStatVO;
import com.lut.grade.model.vo.GradeVO;
import com.lut.grade.model.vo.ScoreSegmentVO;
import com.lut.grade.service.GradeService;
import com.lut.grade.util.DBUtil;
import com.lut.grade.util.ExcelUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** GradeService 实现。 */
public class GradeServiceImpl implements GradeService {

    private final GradeDao gradeDao = new GradeDaoImpl();
    private final CourseDao courseDao = new CourseDaoImpl();

    @Override
    public List<GradeVO> listByCourse(Long courseId, String keyword) {
        try {
            return gradeDao.listByCourse(courseId, keyword);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void save(Long courseId, Long studentId, BigDecimal score, String examType) {
        try {
            if (score == null) {
                throw new ServiceException("成绩不能为空");
            }
            double value = score.doubleValue();
            if (value < 0 || value > 100) {
                throw new ServiceException("成绩必须在 0 ~ 100 之间");
            }
            gradeDao.upsert(studentId, courseId, score, examType == null ? "期末" : examType);
        } catch (ServiceException e) {
            throw e;
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void remove(Long courseId, Long studentId) {
        try {
            gradeDao.deleteByStudentAndCourse(studentId, courseId);
        } finally {
            DBUtil.closeConnection();
        }
    }

    /**
     * 批量导入：先按课程花名册逐行校验（学号是否存在于该课 / 分数是否 0~100），
     * 全部通过才开事务写入，避免"导入一半留残数据"。
     *
     * @return 成功导入的条数
     */
    @Override
    public int batchImport(Long courseId, List<ExcelUtil.GradeRow> rows) {
        try {
            if (rows == null || rows.isEmpty()) {
                throw new ServiceException("没有读取到任何成绩数据，请检查文件内容");
            }
            if (rows.size() > ExcelUtil.MAX_ROWS) {
                throw new ServiceException("单次导入不能超过 " + ExcelUtil.MAX_ROWS + " 行");
            }

            // 该课程花名册：学号 -> 学生ID
            Map<String, Long> roster = new HashMap<>();
            for (GradeVO vo : gradeDao.listByCourse(courseId, null)) {
                roster.put(vo.getStudentNo(), vo.getStudentId());
            }

            List<String> errors = new ArrayList<>();
            int line = 0;
            for (ExcelUtil.GradeRow row : rows) {
                line++;
                if (row.getStudentNo() == null || row.getStudentNo().isEmpty()) {
                    errors.add("第 " + row.getRowNum() + " 行：学号为空");
                    continue;
                }
                if (!roster.containsKey(row.getStudentNo())) {
                    errors.add("第 " + row.getRowNum() + " 行：学号 " + row.getStudentNo()
                            + " 不在该课程花名册中");
                    continue;
                }
                if (row.getScore() == null) {
                    errors.add("第 " + row.getRowNum() + " 行：成绩为空");
                    continue;
                }
                double value = row.getScore().doubleValue();
                if (value < 0 || value > 100) {
                    errors.add("第 " + row.getRowNum() + " 行：成绩 " + value + " 超出 0~100 范围");
                }
            }
            if (!errors.isEmpty()) {
                throw new ServiceException("导入失败，共 " + errors.size() + " 处错误：<br>"
                        + String.join("<br>", errors));
            }

            DBUtil.beginTransaction();
            int saved = 0;
            for (ExcelUtil.GradeRow row : rows) {
                gradeDao.upsert(roster.get(row.getStudentNo()), courseId, row.getScore(),
                        row.getExamType() == null || row.getExamType().isEmpty()
                                ? "期末" : row.getExamType());
                saved++;
            }
            DBUtil.commit();
            return saved;
        } catch (ServiceException e) {
            DBUtil.rollback();
            throw e;
        } catch (Exception e) {
            DBUtil.rollback();
            throw new RuntimeException("批量导入失败：" + e.getMessage(), e);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public CourseStatVO statByCourse(Long courseId) {
        try {
            Course course = courseDao.findById(courseId);
            CourseStatVO stat = new CourseStatVO();
            if (course == null) {
                return stat;
            }
            stat.setCourseId(courseId);
            stat.setCourseName(course.getCourseName());
            stat.setCourseCode(course.getCourseCode());
            stat.setTeacherId(course.getTeacherId());
            stat.setTeacherName(course.getTeacherName());

            int selected = gradeDao.countSelected(courseId);
            int scored = gradeDao.countScored(courseId);
            stat.setTotal(selected);
            stat.setScoredCount(scored);
            stat.setAvg(gradeDao.avgByCourse(courseId));
            stat.setMax(gradeDao.maxByCourse(courseId));
            stat.setMin(gradeDao.minByCourse(courseId));

            if (scored > 0) {
                double rate = gradeDao.countPass(courseId) * 100.0 / scored;
                stat.setPassRate(BigDecimal.valueOf(rate).setScale(2, RoundingMode.HALF_UP));
            } else {
                stat.setPassRate(BigDecimal.ZERO);
            }
            stat.setSegments(buildSegments(courseId, scored));
            return stat;
        } finally {
            DBUtil.closeConnection();
        }
    }

    /** 五档分数段：优 [90,101)、良 [80,90)、中 [70,80)、及格 [60,70)、不及格 [0,60)。 */
    private List<ScoreSegmentVO> buildSegments(Long courseId, int scored) {
        String[] names = {"优", "良", "中", "及格", "不及格"};
        double[][] ranges = {{90, 101}, {80, 90}, {70, 80}, {60, 70}, {0, 60}};
        String[] colors = {"success", "primary", "info", "warning", "danger"};

        List<ScoreSegmentVO> segments = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            int count = gradeDao.countInSegment(courseId, ranges[i][0], ranges[i][1]);
            ScoreSegmentVO seg = new ScoreSegmentVO(names[i], count, colors[i]);
            seg.setRatio(scored == 0 ? 0 : count * 1.0 / scored);
            segments.add(seg);
        }
        return segments;
    }
}
