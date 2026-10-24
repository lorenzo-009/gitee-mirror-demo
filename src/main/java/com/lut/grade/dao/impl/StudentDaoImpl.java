package com.lut.grade.dao.impl;

import com.lut.grade.dao.BaseDao;
import com.lut.grade.dao.StudentDao;
import com.lut.grade.model.entity.Student;

import java.util.ArrayList;
import java.util.List;

/** StudentDao 的 JDBC 实现。 */
public class StudentDaoImpl extends BaseDao implements StudentDao {

    private static final String COLUMNS = "s.id, s.user_id, s.student_no, s.name, s.gender, "
            + "s.phone, s.email, s.enroll_date, u.username AS username";

    private static final String FROM = " FROM student s LEFT JOIN user u ON u.id = s.user_id ";

    @Override
    public List<Student> pageList(String keyword, int offset, int pageSize) {
        StringBuilder sql = new StringBuilder("SELECT ").append(COLUMNS).append(FROM);
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("WHERE s.student_no LIKE ? OR s.name LIKE ? ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        sql.append("ORDER BY s.student_no LIMIT ?, ?");
        params.add(offset);
        params.add(pageSize);
        return queryList(Student.class, sql.toString(), params.toArray());
    }

    @Override
    public int countByKeyword(String keyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) ").append(FROM);
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("WHERE s.student_no LIKE ? OR s.name LIKE ? ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        return count(sql.toString(), params.toArray());
    }

    @Override
    public Student findById(Long id) {
        String sql = "SELECT " + COLUMNS + FROM + "WHERE s.id = ?";
        return queryOne(Student.class, sql, id);
    }

    @Override
    public List<Student> findAll() {
        return queryList(Student.class, "SELECT " + COLUMNS + FROM + "ORDER BY s.student_no");
    }

    @Override
    public Long save(Student student) {
        String sql = "INSERT INTO student (user_id, student_no, name, gender, phone, email, enroll_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        return insertReturnKey(sql, student.getUserId(), student.getStudentNo(), student.getName(),
                student.getGender(), student.getPhone(), student.getEmail(), student.getEnrollDate());
    }

    @Override
    public int update(Student student) {
        String sql = "UPDATE student SET student_no = ?, name = ?, gender = ?, phone = ?, "
                + "email = ?, enroll_date = ? WHERE id = ?";
        return update(sql, student.getStudentNo(), student.getName(), student.getGender(),
                student.getPhone(), student.getEmail(), student.getEnrollDate(), student.getId());
    }

    @Override
    public int delete(Long id) {
        // user 表外键配置了 ON DELETE CASCADE，删除 user 记录会级联删除学生档案
        String sql = "DELETE u FROM user u JOIN student s ON s.user_id = u.id WHERE s.id = ?";
        return update(sql, id);
    }

    @Override
    public int countAll() {
        return count("SELECT COUNT(*) FROM student");
    }
}
