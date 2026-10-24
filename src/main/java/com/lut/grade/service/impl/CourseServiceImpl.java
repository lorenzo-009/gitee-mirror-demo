package com.lut.grade.service.impl;

import com.lut.grade.dao.CourseDao;
import com.lut.grade.dao.impl.CourseDaoImpl;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.entity.Course;
import com.lut.grade.service.CourseService;
import com.lut.grade.util.DBUtil;
import com.lut.grade.util.Page;

import java.util.List;
import java.util.Objects;

/** CourseService 实现。 */
public class CourseServiceImpl implements CourseService {

    private final CourseDao courseDao = new CourseDaoImpl();

    @Override
    public Page<Course> page(int pageNo, String keyword, Long teacherId) {
        try {
            Page<Course> page = new Page<>(pageNo, Page.DEFAULT_PAGE_SIZE);
            page.setTotalCount(courseDao.countByCondition(teacherId, keyword));
            page.setList(courseDao.pageList(teacherId, keyword, page.getOffset(), page.getPageSize()));
            return page;
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public Course findById(Long id) {
        try {
            return courseDao.findById(id);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public List<Course> findByTeacher(Long teacherId) {
        try {
            return courseDao.findByTeacher(teacherId);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public List<Course> findAll() {
        try {
            return courseDao.findAll();
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void save(Course course) {
        try {
            if (course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
                throw new ServiceException("课程编号不能为空");
            }
            if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
                throw new ServiceException("课程名称不能为空");
            }
            courseDao.save(course);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void update(Course course) {
        try {
            courseDao.update(course);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void delete(Long id) {
        try {
            // 选课与成绩记录由外键级联删除
            courseDao.delete(id);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public int countAll() {
        try {
            return courseDao.countAll();
        } finally {
            DBUtil.closeConnection();
        }
    }
}
