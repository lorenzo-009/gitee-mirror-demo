package com.lut.grade.dao.impl;

import com.lut.grade.dao.BaseDao;
import com.lut.grade.dao.CourseDao;
import com.lut.grade.model.entity.Course;

import java.util.ArrayList;
import java.util.List;

/** CourseDao 的 JDBC 实现。 */
public class CourseDaoImpl extends BaseDao implements CourseDao {

    private static final String COLUMNS = "c.id, c.course_code, c.course_name, c.credit, c.teacher_id, "
            + "c.semester, c.capacity, c.description, c.status, t.name AS teacher_name";

    private static final String SELECTED_COUNT =
            "(SELECT COUNT(*) FROM course_selection cs WHERE cs.course_id = c.id AND cs.status = 1) "
                    + "AS selected_count";

    private static final String FROM = " FROM course c LEFT JOIN teacher t ON t.id = c.teacher_id ";

    @Override
    public List<Course> pageList(Long teacherId, String keyword, int offset, int pageSize) {
        StringBuilder sql = new StringBuilder("SELECT ").append(COLUMNS).append(", ").append(SELECTED_COUNT)
                .append(FROM);
        List<Object> params = new ArrayList<>();
        List<String> where = new ArrayList<>();
        if (teacherId != null) {
            where.add("c.teacher_id = ?");
            params.add(teacherId);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            where.add("(c.course_code LIKE ? OR c.course_name LIKE ?)");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        appendWhere(sql, where);
        sql.append("ORDER BY c.course_code LIMIT ?, ?");
        params.add(offset);
        params.add(pageSize);
        return queryList(Course.class, sql.toString(), params.toArray());
    }

    @Override
    public int countByCondition(Long teacherId, String keyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) ").append(FROM);
        List<Object> params = new ArrayList<>();
        List<String> where = new ArrayList<>();
        if (teacherId != null) {
            where.add("c.teacher_id = ?");
            params.add(teacherId);
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            where.add("(c.course_code LIKE ? OR c.course_name LIKE ?)");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        appendWhere(sql, where);
        return count(sql.toString(), params.toArray());
    }

    @Override
    public Course findById(Long id) {
        String sql = "SELECT " + COLUMNS + FROM + "WHERE c.id = ?";
        return queryOne(Course.class, sql, id);
    }

    @Override
    public List<Course> findByTeacher(Long teacherId) {
        String sql = "SELECT " + COLUMNS + ", " + SELECTED_COUNT + FROM
                + "WHERE c.teacher_id = ? ORDER BY c.course_code";
        return queryList(Course.class, sql, teacherId);
    }

    @Override
    public List<Course> findAll() {
        return queryList(Course.class, "SELECT " + COLUMNS + FROM + "ORDER BY c.course_code");
    }

    @Override
    public Long save(Course course) {
        String sql = "INSERT INTO course (course_code, course_name, credit, teacher_id, semester, "
                + "capacity, description, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        return insertReturnKey(sql, course.getCourseCode(), course.getCourseName(), course.getCredit(),
                course.getTeacherId(), course.getSemester(), course.getCapacity(),
                course.getDescription(), course.getStatus());
    }

    @Override
    public int update(Course course) {
        String sql = "UPDATE course SET course_code = ?, course_name = ?, credit = ?, teacher_id = ?, "
                + "semester = ?, capacity = ?, description = ?, status = ? WHERE id = ?";
        return update(sql, course.getCourseCode(), course.getCourseName(), course.getCredit(),
                course.getTeacherId(), course.getSemester(), course.getCapacity(),
                course.getDescription(), course.getStatus(), course.getId());
    }

    @Override
    public int delete(Long id) {
        // 选课与成绩表外键均为 ON DELETE CASCADE
        return update("DELETE FROM course WHERE id = ?", id);
    }

    @Override
    public int countAll() {
        return count("SELECT COUNT(*) FROM course");
    }

    private void appendWhere(StringBuilder sql, List<String> where) {
        if (where.isEmpty()) {
            return;
        }
        sql.append("WHERE ").append(String.join(" AND ", where)).append(" ");
    }
}
