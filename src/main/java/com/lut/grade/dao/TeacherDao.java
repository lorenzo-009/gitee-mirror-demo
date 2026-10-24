package com.lut.grade.dao;

import com.lut.grade.model.entity.Teacher;

import java.util.List;

/** 教师表数据访问接口。 */
public interface TeacherDao {

    List<Teacher> pageList(String keyword, int offset, int pageSize);

    int countByKeyword(String keyword);

    Teacher findById(Long id);

    List<Teacher> findAll();

    Long save(Teacher teacher);

    int update(Teacher teacher);

    int delete(Long id);

    int countAll();
}
