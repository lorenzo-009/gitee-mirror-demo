package com.lut.grade.model.entity;

import java.time.LocalDateTime;

/** 用户表 user：登录身份，与业务档案（学生/教师）通过 user_id 关联。 */
public class User {

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_TEACHER = 2;
    public static final int ROLE_STUDENT = 3;

    private Long id;
    private String username;
    private String password;
    private String salt;
    private Integer role;
    private String avatar;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /** 角色中文名，页面直接 ${user.roleName} 取用。 */
    public String getRoleName() {
        if (role == null) {
            return "未知";
        }
        switch (role) {
            case ROLE_ADMIN:
                return "管理员";
            case ROLE_TEACHER:
                return "教师";
            case ROLE_STUDENT:
                return "学生";
            default:
                return "未知";
        }
    }
}
