package com.lut.grade.service;

import com.lut.grade.model.entity.Course;

import java.util.List;

/** 选课业务接口。 */
public interface SelectionService {

    List<Course> listSelected(Long studentId);

    List<Course> listSelectable(Long studentId, String keyword);

    /** 选课：重复选课与超过容量均会抛出业务异常。 */
    void select(Long studentId, Long courseId);

    /** 退选。 */
    void drop(Long studentId, Long courseId);
}
