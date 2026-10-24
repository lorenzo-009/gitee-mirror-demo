package com.lut.grade.dao;

import com.lut.grade.model.entity.Course;

import java.util.List;

/** 课程表数据访问接口。 */
public interface CourseDao {

    List<Course> pageList(Long teacherId, String keyword, int offset, int pageSize);

    int countByCondition(Long teacherId, String keyword);

    Course findById(Long id);

    List<Course> findByTeacher(Long teacherId);

    List<Course> findAll();

    Long save(Course course);

    int update(Course course);

    int delete(Long id);

    int countAll();
}
