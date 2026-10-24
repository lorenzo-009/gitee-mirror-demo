package com.lut.grade.service;

import com.lut.grade.model.vo.GradeVO;

import java.util.List;

/** 统计业务接口：区分于按角色划分的功能接口，这里统一放查询统计类能力。 */
public interface StatService {

    /** 学生个人成绩明细。 */
    List<GradeVO> listStudentGrades(Long studentId);

    /** 学生平均绩点（学分加权）。 */
    java.math.BigDecimal gpaOf(Long studentId);

    /** 学生已修学分合计（仅统计及格的课程）。 */
    java.math.BigDecimal creditOf(Long studentId);

    /** 学生平均分。 */
    java.math.BigDecimal avgOf(Long studentId);
}
