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

/** 学生端：个人成绩统计（平均分 / 已修学分 / 平均绩点）。 */
@WebServlet("/student/stat")
public class StudentStatServlet extends BaseServlet {

    private final StatService statService = new StatServiceImpl();

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        Long studentId = loginUser.getProfileId();
        request.setAttribute("list", statService.listStudentGrades(studentId));
        request.setAttribute("gpa", statService.gpaOf(studentId));
        request.setAttribute("credit", statService.creditOf(studentId));
        request.setAttribute("avg", statService.avgOf(studentId));
        request.setAttribute("pageTitle", "成绩统计");
        request.setAttribute("navActive", "student:stat");
        request.getRequestDispatcher("/WEB-INF/views/student/my_stat.jsp").forward(request, response);
    }
}
