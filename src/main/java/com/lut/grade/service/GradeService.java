package com.lut.grade.service;

import com.lut.grade.model.vo.CourseStatVO;
import com.lut.grade.model.vo.GradeVO;

import java.math.BigDecimal;
import java.util.List;

/** 成绩业务接口。 */
public interface GradeService {

    /** 某课程的成绩单（含未录入成绩的学生）。 */
    List<GradeVO> listByCourse(Long courseId, String keyword);

    /** 录入或修改单个学生的某门课成绩。 */
    void save(Long courseId, Long studentId, BigDecimal score, String examType);

    /** 清除某学生的某门课成绩。 */
    void remove(Long courseId, Long studentId);

    /** Excel 批量导入成绩：整批校验通过才写入，任一失败则全部回滚。 */
    int batchImport(Long courseId, List<com.lut.grade.util.ExcelUtil.GradeRow> rows);

    /** 单科成绩统计：平均分、最高、最低、及格率、分数段分布。 */
    CourseStatVO statByCourse(Long courseId);
}
