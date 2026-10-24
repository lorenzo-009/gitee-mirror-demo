package com.lut.grade.service;

import com.lut.grade.model.entity.Student;
import com.lut.grade.util.Page;

/** 学生信息维护业务接口。 */
public interface StudentService {

    /**
     * 新增学生：同时创建登录账号与学生档案，两步在同一事务内完成。
     */
    void create(Student student, String username, String rawPassword);

    void update(Student student);

    void delete(Long id);

    Student findById(Long id);

    Page<Student> page(int pageNo, String keyword);

    int countAll();
}
