package com.lut.grade.dao.impl;

import com.lut.grade.dao.BaseDao;
import com.lut.grade.dao.UserDao;
import com.lut.grade.model.entity.User;

import java.util.HashMap;
import java.util.Map;

/** UserDao 的 JDBC 实现。 */
public class UserDaoImpl extends BaseDao implements UserDao {

    private static final Map<Integer, String> PROFILE_TABLE = new HashMap<>();

    static {
        PROFILE_TABLE.put(User.ROLE_TEACHER, "teacher");
        PROFILE_TABLE.put(User.ROLE_STUDENT, "student");
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, salt, role, avatar, status, "
                + "create_time, update_time FROM user WHERE username = ?";
        return queryOne(User.class, sql, username);
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT id, username, password, salt, role, avatar, status, "
                + "create_time, update_time FROM user WHERE id = ?";
        return queryOne(User.class, sql, id);
    }

    @Override
    public Long create(User user) {
        String sql = "INSERT INTO user (username, password, salt, role, avatar, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        return insertReturnKey(sql, user.getUsername(), user.getPassword(), user.getSalt(),
                user.getRole(), user.getAvatar(),
                user.getStatus() == null ? 1 : user.getStatus());
    }

    @Override
    public int updatePassword(Long userId, String password, String salt) {
        String sql = "UPDATE user SET password = ?, salt = ? WHERE id = ?";
        return update(sql, password, salt, userId);
    }

    @Override
    public int updateAvatar(Long userId, String avatar) {
        String sql = "UPDATE user SET avatar = ? WHERE id = ?";
        return update(sql, avatar, userId);
    }

    @Override
    public Long findProfileId(Long userId, Integer role) {
        String table = PROFILE_TABLE.get(role);
        if (table == null) {
            return null;
        }
        return queryScalar(Long.class, "SELECT id FROM " + table + " WHERE user_id = ?", userId);
    }

    @Override
    public String findRealName(Long userId, Integer role) {
        String table = PROFILE_TABLE.get(role);
        if (table == null) {
            return null;
        }
        return queryScalar(String.class, "SELECT name FROM " + table + " WHERE user_id = ?", userId);
    }
}
