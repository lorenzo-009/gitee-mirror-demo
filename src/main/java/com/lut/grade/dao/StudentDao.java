package com.lut.grade.dao;

import com.lut.grade.model.entity.Student;

import java.util.List;

/** 学生表数据访问接口。 */
public interface StudentDao {

    /** 分页 + 关键字模糊查询（学号 / 姓名）。 */
    List<Student> pageList(String keyword, int offset, int pageSize);

    int countByKeyword(String keyword);

    Student findById(Long id);

    List<Student> findAll();

    Long save(Student student);

    int update(Student student);

    int delete(Long id);

    /** 统计：学生总数。 */
    int countAll();
}
