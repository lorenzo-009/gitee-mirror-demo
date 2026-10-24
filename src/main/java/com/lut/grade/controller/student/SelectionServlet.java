package com.lut.grade.controller.student;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.LoginUser;
import com.lut.grade.service.SelectionService;
import com.lut.grade.service.impl.SelectionServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 学生端：选课与退选。 */
@WebServlet("/student/selection")
public class SelectionServlet extends BaseServlet {

    private final SelectionService selectionService = new SelectionServiceImpl();

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = currentUser(request);
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String keyword = request.getParameter("keyword");
        request.setAttribute("selected", selectionService.listSelected(loginUser.getProfileId()));
        request.setAttribute("selectable", selectionService.listSelectable(loginUser.getProfileId(), keyword));
        request.setAttribute("keyword", keyword);
        request.setAttribute("pageTitle", "我的选课");
        request.setAttribute("navActive", "student:selection");
        forward(request, response, "/WEB-INF/views/student/selection.jsp");
    }

    public void select(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = currentUser(request);
        Long courseId = Long.valueOf(request.getParameter("courseId"));
        try {
            selectionService.select(loginUser.getProfileId(), courseId);
            setFlash(request, "选课成功");
        } catch (ServiceException e) {
            setFlash(request, e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/student/selection");
    }

    public void drop(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = currentUser(request);
        Long courseId = Long.valueOf(request.getParameter("courseId"));
        selectionService.drop(loginUser.getProfileId(), courseId);
        setFlash(request, "已退选该课程");
        response.sendRedirect(request.getContextPath() + "/student/selection");
    }

    private LoginUser currentUser(HttpServletRequest request) {
        return (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}
