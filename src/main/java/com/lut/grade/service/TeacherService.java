package com.lut.grade.service;

import com.lut.grade.model.entity.Teacher;
import com.lut.grade.util.Page;

import java.util.List;

/** 教师信息维护业务接口。 */
public interface TeacherService {

    /** 新增教师：同时创建登录账号与教师档案，在同一事务内完成。 */
    void create(Teacher teacher, String username, String rawPassword);

    void update(Teacher teacher);

    void delete(Long id);

    Teacher findById(Long id);

    Page<Teacher> page(int pageNo, String keyword);

    List<Teacher> findAll();

    int countAll();
}
