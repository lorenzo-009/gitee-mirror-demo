package com.lut.grade.service.impl;

import com.lut.grade.dao.SelectionDao;
import com.lut.grade.dao.impl.SelectionDaoImpl;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.entity.Course;
import com.lut.grade.service.SelectionService;
import com.lut.grade.util.DBUtil;

import java.util.List;

/** SelectionService 实现。 */
public class SelectionServiceImpl implements SelectionService {

    private final SelectionDao selectionDao = new SelectionDaoImpl();

    @Override
    public List<Course> listSelected(Long studentId) {
        try {
            return selectionDao.listSelected(studentId);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public List<Course> listSelectable(Long studentId, String keyword) {
        try {
            return selectionDao.listSelectable(studentId, keyword);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void select(Long studentId, Long courseId) {
        try {
            if (selectionDao.exists(studentId, courseId)) {
                throw new ServiceException("您已经选过这门课程了");
            }
            int capacity = selectionDao.capacityOf(courseId);
            int selected = selectionDao.countSelected(courseId);
            if (selected >= capacity) {
                throw new ServiceException("该课程已达到选课容量上限，无法再选");
            }
            selectionDao.save(studentId, courseId);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public void drop(Long studentId, Long courseId) {
        try {
            selectionDao.delete(studentId, courseId);
        } finally {
            DBUtil.closeConnection();
        }
    }
}
