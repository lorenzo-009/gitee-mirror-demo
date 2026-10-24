package com.lut.grade.dao.impl;

import com.lut.grade.dao.BaseDao;
import com.lut.grade.dao.TeacherDao;
import com.lut.grade.model.entity.Teacher;

import java.util.ArrayList;
import java.util.List;

/** TeacherDao 的 JDBC 实现。 */
public class TeacherDaoImpl extends BaseDao implements TeacherDao {

    private static final String COLUMNS = "t.id, t.user_id, t.teacher_no, t.name, t.title, "
            + "t.phone, t.email, u.username AS username";

    private static final String FROM = " FROM teacher t LEFT JOIN user u ON u.id = t.user_id ";

    @Override
    public List<Teacher> pageList(String keyword, int offset, int pageSize) {
        StringBuilder sql = new StringBuilder("SELECT ").append(COLUMNS).append(FROM);
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("WHERE t.teacher_no LIKE ? OR t.name LIKE ? ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        sql.append("ORDER BY t.teacher_no LIMIT ?, ?");
        params.add(offset);
        params.add(pageSize);
        return queryList(Teacher.class, sql.toString(), params.toArray());
    }

    @Override
    public int countByKeyword(String keyword) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) ").append(FROM);
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("WHERE t.teacher_no LIKE ? OR t.name LIKE ? ");
            String like = "%" + keyword.trim() + "%";
            params.add(like);
            params.add(like);
        }
        return count(sql.toString(), params.toArray());
    }

    @Override
    public Teacher findById(Long id) {
        return queryOne(Teacher.class, "SELECT " + COLUMNS + FROM + "WHERE t.id = ?", id);
    }

    @Override
    public List<Teacher> findAll() {
        return queryList(Teacher.class, "SELECT " + COLUMNS + FROM + "ORDER BY t.teacher_no");
    }

    @Override
    public Long save(Teacher teacher) {
        String sql = "INSERT INTO teacher (user_id, teacher_no, name, title, phone, email) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        return insertReturnKey(sql, teacher.getUserId(), teacher.getTeacherNo(), teacher.getName(),
                teacher.getTitle(), teacher.getPhone(), teacher.getEmail());
    }

    @Override
    public int update(Teacher teacher) {
        String sql = "UPDATE teacher SET teacher_no = ?, name = ?, title = ?, phone = ?, email = ? "
                + "WHERE id = ?";
        return update(sql, teacher.getTeacherNo(), teacher.getName(), teacher.getTitle(),
                teacher.getPhone(), teacher.getEmail(), teacher.getId());
    }

    @Override
    public int delete(Long id) {
        return update("DELETE u FROM user u JOIN teacher t ON t.user_id = u.id WHERE t.id = ?", id);
    }

    @Override
    public int countAll() {
        return count("SELECT COUNT(*) FROM teacher");
    }
}
