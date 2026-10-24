package com.lut.grade.dao;

import com.lut.grade.model.entity.User;

/** 用户表数据访问接口。 */
public interface UserDao {

    User findByUsername(String username);

    User findById(Long id);

    /** 新建登录账号并返回自增主键，供 Service 在同一事务中创建用户 + 档案。 */
    Long create(User user);

    int updatePassword(Long userId, String password, String salt);

    int updateAvatar(Long userId, String avatar);

    /** 按人档案表反查主键：学生返回 student.id，教师返回 teacher.id。 */
    Long findProfileId(Long userId, Integer role);

    /** 反查档案表中的真实姓名。 */
    String findRealName(Long userId, Integer role);
}
