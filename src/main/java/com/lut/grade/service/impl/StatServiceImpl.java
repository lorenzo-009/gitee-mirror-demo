package com.lut.grade.service.impl;

import com.lut.grade.dao.GradeDao;
import com.lut.grade.dao.impl.GradeDaoImpl;
import com.lut.grade.model.vo.GradeVO;
import com.lut.grade.service.StatService;
import com.lut.grade.util.DBUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** StatService 实现：绩点与学分计算规则集中在此，便于后续调整口径。 */
public class StatServiceImpl implements StatService {

    private final GradeDao gradeDao = new GradeDaoImpl();

    @Override
    public List<GradeVO> listStudentGrades(Long studentId) {
        try {
            return gradeDao.listByStudent(studentId);
        } finally {
            DBUtil.closeConnection();
        }
    }

    @Override
    public BigDecimal gpaOf(Long studentId) {
        List<GradeVO> list = listStudentGrades(studentId);
        BigDecimal pointSum = BigDecimal.ZERO;
        BigDecimal creditSum = BigDecimal.ZERO;
        for (GradeVO g : list) {
            if (g.getScore() == null || g.getCredit() == null) {
                continue;
            }
            pointSum = pointSum.add(g.getGradePoint().multiply(g.getCredit()));
            creditSum = creditSum.add(g.getCredit());
        }
        if (creditSum.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return pointSum.divide(creditSum, 2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal creditOf(Long studentId) {
        List<GradeVO> list = listStudentGrades(studentId);
        BigDecimal credit = BigDecimal.ZERO;
        for (GradeVO g : list) {
            if (g.getScore() != null && g.getScore().doubleValue() >= 60 && g.getCredit() != null) {
                credit = credit.add(g.getCredit());
            }
        }
        return credit;
    }

    @Override
    public BigDecimal avgOf(Long studentId) {
        List<GradeVO> list = listStudentGrades(studentId);
        BigDecimal sum = BigDecimal.ZERO;
        int count = 0;
        for (GradeVO g : list) {
            if (g.getScore() != null) {
                sum = sum.add(g.getScore());
                count++;
            }
        }
        if (count == 0) {
            return BigDecimal.ZERO;
        }
        return sum.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }
}
