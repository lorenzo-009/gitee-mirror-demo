package com.lut.grade.dao;

import com.lut.grade.model.vo.GradeVO;

import java.math.BigDecimal;
import java.util.List;

/** 成绩表数据访问接口。 */
public interface GradeDao {

    /**
     * 查询某课程的成绩单（含已选课但尚未录入成绩的学生，成绩为 NULL）。
     */
    List<GradeVO> listByCourse(Long courseId, String keyword);

    /** 查询某学生的全部课程成绩。 */
    List<GradeVO> listByStudent(Long studentId);

    GradeVO findByStudentAndCourse(Long studentId, Long courseId);

    /**
     * 新增或更新一条成绩，依赖 (student_id, course_id) 唯一索引做 UPSERT。
     */
    int upsert(Long studentId, Long courseId, BigDecimal score, String examType);

    int deleteByStudentAndCourse(Long studentId, Long courseId);

    /** 选课人数。 */
    int countSelected(Long courseId);

    /** 已录入成绩的人数。 */
    int countScored(Long courseId);

    BigDecimal avgByCourse(Long courseId);

    BigDecimal maxByCourse(Long courseId);

    BigDecimal minByCourse(Long courseId);

    /** 及格人数（成绩 >= 60）。 */
    int countPass(Long courseId);

    /** 落在该分数区间内的人数，区间为 [low, high)。 */
    int countInSegment(Long courseId, double low, double high);
}
