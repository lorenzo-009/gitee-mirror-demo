package com.lut.grade.controller.student;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.model.LoginUser;
import com.lut.grade.service.StatService;
import com.lut.grade.service.impl.StatServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 学生端：查询个人成绩。 */
@WebServlet("/student/grade")
public class StudentGradeServlet extends BaseServlet {

    private final StatService statService = new StatServiceImpl();

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("list", statService.listStudentGrades(loginUser.getProfileId()));
        request.setAttribute("gpa", statService.gpaOf(loginUser.getProfileId()));
        request.setAttribute("credit", statService.creditOf(loginUser.getProfileId()));
        request.setAttribute("avg", statService.avgOf(loginUser.getProfileId()));
        request.setAttribute("pageTitle", "我的成绩");
        request.setAttribute("navActive", "student:grade");
        request.getRequestDispatcher("/WEB-INF/views/student/my_grade.jsp").forward(request, response);
    }
}
