package com.lut.grade.dao.impl;

import com.lut.grade.dao.BaseDao;
import com.lut.grade.dao.GradeDao;
import com.lut.grade.model.vo.GradeVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** GradeDao 的 JDBC 实现。 */
public class GradeDaoImpl extends BaseDao implements GradeDao {

    @Override
    public List<GradeVO> listByCourse(Long courseId, String keyword) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT st.id AS student_id, st.student_no, st.name AS student_name, ")
                .append("       c.id AS course_id, c.course_name, c.course_code, c.credit, ")
                .append("       g.score, g.exam_type ")
                .append("FROM course_selection cs ")
                .append("JOIN student st ON st.id = cs.student_id ")
                .append("JOIN course c ON c.id = cs.course_id ")
                .append("LEFT JOIN grade g ON g.student_id = cs.student_id AND g.course_id = cs.course_id ")
                .append("WHERE cs.status = 1 AND cs.course_id = ? ");
        List<Object> params = new ArrayList<>();
        params.add(courseId);
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (st.student_no LIKE ? OR st.name LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        sql.append("ORDER BY st.student_no");
        return queryList(GradeVO.class, sql.toString(), params.toArray());
    }

    @Override
    public List<GradeVO> listByStudent(Long studentId) {
        String sql = "SELECT st.id AS student_id, st.student_no, st.name AS student_name, "
                + "       c.id AS course_id, c.course_name, c.course_code, c.credit, "
                + "       g.score, g.exam_type "
                + "FROM grade g "
                + "JOIN student st ON st.id = g.student_id "
                + "JOIN course c ON c.id = g.course_id "
                + "WHERE g.student_id = ? ORDER BY c.course_code";
        return queryList(GradeVO.class, sql, studentId);
    }

    @Override
    public GradeVO findByStudentAndCourse(Long studentId, Long courseId) {
        String sql = "SELECT st.id AS student_id, st.student_no, st.name AS student_name, "
                + "       c.id AS course_id, c.course_name, c.course_code, c.credit, "
                + "       g.score, g.exam_type "
                + "FROM grade g "
                + "JOIN student st ON st.id = g.student_id "
                + "JOIN course c ON c.id = g.course_id "
                + "WHERE g.student_id = ? AND g.course_id = ?";
        return queryOne(GradeVO.class, sql, studentId, courseId);
    }

    @Override
    public int upsert(Long studentId, Long courseId, BigDecimal score, String examType) {
        String sql = "INSERT INTO grade (student_id, course_id, score, exam_type) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE score = VALUES(score), exam_type = VALUES(exam_type)";
        return update(sql, studentId, courseId, score, examType);
    }

    @Override
    public int deleteByStudentAndCourse(Long studentId, Long courseId) {
        return update("DELETE FROM grade WHERE student_id = ? AND course_id = ?", studentId, courseId);
    }

    @Override
    public int countSelected(Long courseId) {
        return count("SELECT COUNT(*) FROM course_selection WHERE course_id = ? AND status = 1", courseId);
    }

    @Override
    public int countScored(Long courseId) {
        return count("SELECT COUNT(*) FROM grade WHERE course_id = ? AND score IS NOT NULL", courseId);
    }

    @Override
    public BigDecimal avgByCourse(Long courseId) {
        return queryScalar(BigDecimal.class,
                "SELECT ROUND(AVG(score), 2) FROM grade WHERE course_id = ? AND score IS NOT NULL", courseId);
    }

    @Override
    public BigDecimal maxByCourse(Long courseId) {
        return queryScalar(BigDecimal.class, "SELECT MAX(score) FROM grade WHERE course_id = ?", courseId);
    }

    @Override
    public BigDecimal minByCourse(Long courseId) {
        return queryScalar(BigDecimal.class, "SELECT MIN(score) FROM grade WHERE course_id = ?", courseId);
    }

    @Override
    public int countPass(Long courseId) {
        return count("SELECT COUNT(*) FROM grade WHERE course_id = ? AND score >= 60", courseId);
    }

    @Override
    public int countInSegment(Long courseId, double low, double high) {
        return count("SELECT COUNT(*) FROM grade WHERE course_id = ? AND score >= ? AND score < ?",
                courseId, low, high);
    }
}
