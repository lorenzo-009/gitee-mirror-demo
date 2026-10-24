package com.lut.grade.service.impl;

import com.lut.grade.dao.PermissionDao;
import com.lut.grade.dao.UserDao;
import com.lut.grade.dao.impl.PermissionDaoImpl;
import com.lut.grade.dao.impl.UserDaoImpl;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.LoginUser;
import com.lut.grade.model.entity.User;
import com.lut.grade.service.UserService;
import com.lut.grade.util.DBUtil;
import com.lut.grade.util.MD5Util;

/** UserService 实现：登录认证、改密、更新头像。 */
public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoImpl();
    private final PermissionDao permissionDao = new PermissionDaoImpl();

    @Override
    public LoginUser login(String username, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                throw new ServiceException("用户名不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new ServiceException("密码不能为空");
            }
            User user = userDao.findByUsername(username.trim());
            if (user == null) {
                throw new ServiceException("账号不存在");
            }
            if (!MD5Util.matches(password, user.getSalt(), user.getPassword())) {
                throw new ServiceException("密码错误");
            }
            if (user.getStatus() != null && user.getStatus() == 0) {
                throw new ServiceException("该账号已被停用，请联系管理员");
            }

            Long profileId = userDao.findProfileId(user.getId(), user.getRole());
            LoginUser loginUser = LoginUser.from(user, profileId);
            loginUser.setRealName(userDao.findRealName(user.getId(), user.getRole()));
            loginUser.setMenus(permissionDao.findMenusByRole(user.getRole()));
            loginUser.setPermCodes(permissionDao.findCodesByRole(user.getRole()));
            return loginUser;
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        try {
            User user = userDao.findById(userId);
            if (user == null) {
                throw new ServiceException("用户不存在");
            }
            if (!MD5Util.matches(oldPassword, user.getSalt(), user.getPassword())) {
                throw new ServiceException("原密码不正确");
            }
            if (newPassword == null || newPassword.length() < 6) {
                throw new ServiceException("新密码长度不能少于 6 位");
            }
            String salt = MD5Util.randomSalt();
            String encoded = MD5Util.encrypt(newPassword, salt);
            return userDao.updatePassword(userId, encoded, salt) > 0;
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void updateAvatar(Long userId, String fileName) {
        try {
            userDao.updateAvatar(userId, fileName);
        } finally {
            DBUtil.closeConnection();
        }
    }
}
