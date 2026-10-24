package com.lut.grade.model.vo;

import java.math.BigDecimal;
import java.util.List;

/** 单科成绩统计视图对象。 */
public class CourseStatVO {

    private Long courseId;
    private String courseName;
    private String courseCode;
    private Long teacherId;
    private String teacherName;

    private int total;          // 选课人数
    private int scoredCount;    // 已录入成绩人数
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal passRate;

    /** 五档分数段人数。 */
    private List<ScoreSegmentVO> segments;

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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getScoredCount() {
        return scoredCount;
    }

    public void setScoredCount(int scoredCount) {
        this.scoredCount = scoredCount;
    }

    public BigDecimal getAvg() {
        return avg;
    }

    public void setAvg(BigDecimal avg) {
        this.avg = avg;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getPassRate() {
        return passRate;
    }

    public void setPassRate(BigDecimal passRate) {
        this.passRate = passRate;
    }

    public List<ScoreSegmentVO> getSegments() {
        return segments;
    }

    public void setSegments(List<ScoreSegmentVO> segments) {
        this.segments = segments;
    }
}
