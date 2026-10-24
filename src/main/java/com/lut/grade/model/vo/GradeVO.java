package com.lut.grade.model.vo;

import java.math.BigDecimal;

/**
 * 成绩列表视图对象：学生 + 课程 + 成绩三表联查的结果。
 * 附带派生属性（等级、绩点），供页面直接展示。
 */
public class GradeVO {

    private Long studentId;
    private String studentNo;
    private String studentName;
    private Long courseId;
    private String courseName;
    private String courseCode;
    private BigDecimal credit;
    private BigDecimal score;
    private String examType;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    /** 百分制转五档等级。 */
    public String getLevelName() {
        if (score == null) {
            return "未录入";
        }
        double v = score.doubleValue();
        if (v >= 90) {
            return "优";
        }
        if (v >= 80) {
            return "良";
        }
        if (v >= 70) {
            return "中";
        }
        if (v >= 60) {
            return "及格";
        }
        return "不及格";
    }

    /** 绩点 =(成绩-50)/10，低于 60 记 0，保留两位小数。 */
    public BigDecimal getGradePoint() {
        if (score == null || score.doubleValue() < 60) {
            return BigDecimal.ZERO;
        }
        double point = (score.doubleValue() - 50) / 10;
        return BigDecimal.valueOf(Math.max(point, 0)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /** 是否及格，用于页面给整行标红。 */
    public boolean isFailed() {
        return score != null && score.doubleValue() < 60;
    }
}
