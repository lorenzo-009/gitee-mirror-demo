package com.lut.grade.dao.impl;

import com.lut.grade.dao.BaseDao;
import com.lut.grade.dao.SelectionDao;
import com.lut.grade.model.entity.Course;

import java.util.ArrayList;
import java.util.List;

/** SelectionDao 的 JDBC 实现。 */
public class SelectionDaoImpl extends BaseDao implements SelectionDao {

    private static final String COLUMNS = "c.id, c.course_code, c.course_name, c.credit, c.teacher_id, "
            + "c.semester, c.capacity, c.description, c.status, t.name AS teacher_name";

    private static final String SELECTED_COUNT =
            "(SELECT COUNT(*) FROM course_selection cs WHERE cs.course_id = c.id AND cs.status = 1) "
                    + "AS selected_count";

    @Override
    public List<Course> listSelected(Long studentId) {
        String sql = "SELECT " + COLUMNS + ", " + SELECTED_COUNT + " "
                + "FROM course_selection cs "
                + "JOIN course c ON c.id = cs.course_id "
                + "LEFT JOIN teacher t ON t.id = c.teacher_id "
                + "WHERE cs.student_id = ? AND cs.status = 1 ORDER BY c.course_code";
        return queryList(Course.class, sql, studentId);
    }

    @Override
    public List<Course> listSelectable(Long studentId, String keyword) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(COLUMNS).append(", ").append(SELECTED_COUNT).append(" ")
                .append("FROM course c ")
                .append("LEFT JOIN teacher t ON t.id = c.teacher_id ")
                .append("WHERE c.status = 1 ")
                .append("AND c.id NOT IN (SELECT course_id FROM course_selection ")
                .append("                  WHERE student_id = ? AND status = 1) ");
        List<Object> params = new ArrayList<>();
        params.add(studentId);
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (c.course_code LIKE ? OR c.course_name LIKE ?) ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        sql.append("ORDER BY c.course_code");
        return queryList(Course.class, sql.toString(), params.toArray());
    }

    @Override
    public boolean exists(Long studentId, Long courseId) {
        Integer count = queryScalar(Integer.class,
                "SELECT COUNT(*) FROM course_selection WHERE student_id = ? AND course_id = ? AND status = 1",
                studentId, courseId);
        return count != null && count > 0;
    }

    @Override
    public int countSelected(Long courseId) {
        Integer count = queryScalar(Integer.class,
                "SELECT COUNT(*) FROM course_selection WHERE course_id = ? AND status = 1", courseId);
        return count == null ? 0 : count;
    }

    @Override
    public int capacityOf(Long courseId) {
        Integer cap = queryScalar(Integer.class, "SELECT capacity FROM course WHERE id = ?", courseId);
        return cap == null ? 0 : cap;
    }

    @Override
    public Long save(Long studentId, Long courseId) {
        String sql = "INSERT INTO course_selection (student_id, course_id, status) VALUES (?, ?, 1) "
                + "ON DUPLICATE KEY UPDATE status = 1";
        insertReturnKey(sql, studentId, courseId);
        return courseId;
    }

    @Override
    public int delete(Long studentId, Long courseId) {
        return update("DELETE FROM course_selection WHERE student_id = ? AND course_id = ?",
                studentId, courseId);
    }
}
