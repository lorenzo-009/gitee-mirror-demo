package com.lut.grade.service;

import com.lut.grade.model.LoginUser;

/** 用户认证与个人信息业务接口。 */
public interface UserService {

    /**
     * 登录校验。
     * @return 登录成功返回身份对象；失败抛出 ServiceException
     */
    LoginUser login(String username, String password);

    /** 修改密码，返回 true 表示成功。 */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /** 更新头像文件名。 */
    void updateAvatar(Long userId, String fileName);
}
