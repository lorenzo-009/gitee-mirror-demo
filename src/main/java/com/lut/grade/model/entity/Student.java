package com.lut.grade.model.entity;

import java.time.LocalDate;

/** 学生表 student：业务档案。 */
public class Student {

    private Long id;
    private Long userId;
    private String studentNo;
    private String name;
    private Integer gender;
    private String phone;
    private String email;
    private LocalDate enrollDate;

    /** 非持久化字段：关联查询出来的登录账号。 */
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(LocalDate enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGenderName() {
        if (gender == null) {
            return "未知";
        }
        if (gender == 1) {
            return "男";
        }
        if (gender == 2) {
            return "女";
        }
        return "未知";
    }
}
