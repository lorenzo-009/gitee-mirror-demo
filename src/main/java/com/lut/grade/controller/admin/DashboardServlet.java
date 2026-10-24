package com.lut.grade.controller.admin;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.model.LoginUser;
import com.lut.grade.service.CourseService;
import com.lut.grade.service.StudentService;
import com.lut.grade.service.TeacherService;
import com.lut.grade.service.impl.CourseServiceImpl;
import com.lut.grade.service.impl.StudentServiceImpl;
import com.lut.grade.service.impl.TeacherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 管理员端：系统概览。 */
@WebServlet("/admin/dashboard")
public class DashboardServlet extends BaseServlet {

    private final StudentService studentService = new StudentServiceImpl();
    private final TeacherService teacherService = new TeacherServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("studentCount", studentService.countAll());
        request.setAttribute("teacherCount", teacherService.countAll());
        request.setAttribute("courseCount", courseService.countAll());
        request.setAttribute("pageTitle", "系统概览");
        request.setAttribute("navActive", "admin:dashboard");
        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }
}
