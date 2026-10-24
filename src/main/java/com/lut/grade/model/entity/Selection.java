package com.lut.grade.model.entity;

import java.time.LocalDateTime;

/** 选课表 course_selection。 */
public class Selection {

    public static final int STATUS_SELECTED = 1;
    public static final int STATUS_DROPPED = 0;

    private Long id;
    private Long studentId;
    private Long courseId;
    private LocalDateTime selectTime;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(LocalDateTime selectTime) {
        this.selectTime = selectTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
