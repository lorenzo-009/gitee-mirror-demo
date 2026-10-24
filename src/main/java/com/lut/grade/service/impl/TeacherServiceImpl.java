package com.lut.grade.service.impl;

import com.lut.grade.dao.TeacherDao;
import com.lut.grade.dao.UserDao;
import com.lut.grade.dao.impl.TeacherDaoImpl;
import com.lut.grade.dao.impl.UserDaoImpl;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.entity.Teacher;
import com.lut.grade.model.entity.User;
import com.lut.grade.service.TeacherService;
import com.lut.grade.util.DBUtil;
import com.lut.grade.util.MD5Util;
import com.lut.grade.util.Page;

import java.util.List;

/** TeacherService 实现。 */
public class TeacherServiceImpl implements TeacherService {

    private final TeacherDao teacherDao = new TeacherDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    @Override
    public void create(Teacher teacher, String username, String rawPassword) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new ServiceException("登录账号不能为空");
            }
            if (userDao.findByUsername(username.trim()) != null) {
                throw new ServiceException("登录账号 " + username + " 已存在");
            }
            if (rawPassword == null || rawPassword.length() < 6) {
                throw new ServiceException("初始密码不能少于 6 位");
            }
            DBUtil.beginTransaction();

            String salt = MD5Util.randomSalt();
            User user = new User();
            user.setUsername(username.trim());
            user.setPassword(MD5Util.encrypt(rawPassword, salt));
            user.setSalt(salt);
            user.setRole(User.ROLE_TEACHER);
            user.setStatus(1);
            teacher.setUserId(userDao.create(user));
            teacherDao.save(teacher);

            DBUtil.commit();
        } catch (ServiceException e) {
            DBUtil.rollback();
            throw e;
        } catch (Exception e) {
            DBUtil.rollback();
            throw new RuntimeException("新增教师失败：" + e.getMessage(), e);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void update(Teacher teacher) {
        try {
            teacherDao.update(teacher);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            teacherDao.delete(id);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public Teacher findById(Long id) {
        try {
            return teacherDao.findById(id);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public Page<Teacher> page(int pageNo, String keyword) {
        try {
            Page<Teacher> page = new Page<>(pageNo, Page.DEFAULT_PAGE_SIZE);
            page.setTotalCount(teacherDao.countByKeyword(keyword));
            page.setList(teacherDao.pageList(keyword, page.getOffset(), page.getPageSize()));
            return page;
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public List<Teacher> findAll() {
        try {
            return teacherDao.findAll();
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public int countAll() {
        try {
            return teacherDao.countAll();
        } finally {
            DBUtil.closeConnection();
        }
    }
}
