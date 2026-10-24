package com.lut.grade.dao;

import com.lut.grade.model.entity.Course;

import java.util.List;

/** 选课表数据访问接口。 */
public interface SelectionDao {

    /** 该学生已选课程。 */
    List<Course> listSelected(Long studentId);

    /** 该学生可选（未选且未满员）的课程。 */
    List<Course> listSelectable(Long studentId, String keyword);

    boolean exists(Long studentId, Long courseId);

    /** 课程的已选人数。 */
    int countSelected(Long courseId);

    /** 课程的容量上限。 */
    int capacityOf(Long courseId);

    Long save(Long studentId, Long courseId);

    /** 退选：物理删除选课记录。 */
    int delete(Long studentId, Long courseId);
}
