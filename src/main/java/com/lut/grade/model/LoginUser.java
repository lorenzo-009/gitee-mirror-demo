package com.lut.grade.model;

import java.io.Serializable;
import java.util.List;

import com.lut.grade.model.entity.Permission;
import com.lut.grade.model.entity.User;

/**
 * 登录成功后放入 Session 的身份对象（session key = LOGIN_USER）。
 * 除了基本身份，还携带该角色的菜单列表与权限码集合，供 Filter 拦截与动态菜单渲染。
 */
public class LoginUser implements Serializable {

    public static final String SESSION_KEY = "LOGIN_USER";

    private Long userId;
    private String username;
    private String realName;
    private Integer role;
    private String avatar;

    /** 关联的业务档案主键：学生为 student.id，教师为 teacher.id。 */
    private Long profileId;

    private List<Permission> menus;
    private List<String> permCodes;

    public LoginUser() {
    }

    public static LoginUser from(User user, Long profileId) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setRole(user.getRole());
        loginUser.setAvatar(user.getAvatar());
        loginUser.setProfileId(profileId);
        return loginUser;
    }

    public boolean isAdmin() {
        return Integer.valueOf(User.ROLE_ADMIN).equals(role);
    }

    public boolean isTeacher() {
        return Integer.valueOf(User.ROLE_TEACHER).equals(role);
    }

    public boolean isStudent() {
        return Integer.valueOf(User.ROLE_STUDENT).equals(role);
    }

    /** 头像为空时的占位路径。 */
    public String getAvatarPath() {
        if (avatar == null || avatar.trim().isEmpty()) {
            return "static/images/default-avatar.png";
        }
        return "file/avatar/" + avatar;
    }

    public String getRoleName() {
        if (role == null) {
            return "未知";
        }
        switch (role) {
            case User.ROLE_ADMIN:
                return "管理员";
            case User.ROLE_TEACHER:
                return "教师";
            case User.ROLE_STUDENT:
                return "学生";
            default:
                return "未知";
        }
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public List<Permission> getMenus() {
        return menus;
    }

    public void setMenus(List<Permission> menus) {
        this.menus = menus;
    }

    public List<String> getPermCodes() {
        return permCodes;
    }

    public void setPermCodes(List<String> permCodes) {
        this.permCodes = permCodes;
    }
}
