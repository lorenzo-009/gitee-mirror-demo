package com.lut.grade.service;

import com.lut.grade.model.entity.Course;
import com.lut.grade.util.Page;

import java.util.List;

/** 课程业务接口。 */
public interface CourseService {

    /**
     * 分页查询课程。
     * @param teacherId 非空时只查该教师的授课，为空查全部（管理员视角）
     */
    Page<Course> page(int pageNo, String keyword, Long teacherId);

    Course findById(Long id);

    List<Course> findByTeacher(Long teacherId);

    List<Course> findAll();

    void save(Course course);

    void update(Course course);

    void delete(Long id);

    int countAll();
}
