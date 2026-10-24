package com.lut.grade.controller.teacher;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.model.LoginUser;
import com.lut.grade.model.vo.GradeVO;
import com.lut.grade.service.CourseService;
import com.lut.grade.service.GradeService;
import com.lut.grade.service.impl.CourseServiceImpl;
import com.lut.grade.service.impl.GradeServiceImpl;
import com.lut.grade.util.ExcelUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** 教师端：将某门课成绩导出为 Excel。 */
@WebServlet("/teacher/grade/export")
public class GradeExportServlet extends BaseServlet {

    private final GradeService gradeService = new GradeServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Long courseId = Long.valueOf(request.getParameter("courseId"));
        var course = courseService.findById(courseId);
        List<GradeVO> list = gradeService.listByCourse(courseId, null);

        String[] headers = {"学号", "姓名", "课程名称", "课程编号", "学分", "成绩", "等级", "绩点"};
        List<String[]> rows = new ArrayList<>();
        for (GradeVO g : list) {
            rows.add(new String[]{
                    g.getStudentNo(),
                    g.getStudentName(),
                    g.getCourseName(),
                    g.getCourseCode(),
                    g.getCredit() == null ? "" : g.getCredit().toPlainString(),
                    g.getScore() == null ? "" : g.getScore().toPlainString(),
                    g.getLevelName(),
                    g.getScore() == null ? "" : g.getGradePoint().toPlainString()
            });
        }

        String fileName = course.getCourseName() + "-成绩单.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
                "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        try (var out = response.getOutputStream()) {
            ExcelUtil.writeGradeSheet(out, headers, rows);
        }
    }
}
