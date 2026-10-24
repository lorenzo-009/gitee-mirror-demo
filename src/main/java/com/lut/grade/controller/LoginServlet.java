package com.lut.grade.controller;

import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.LoginUser;
import com.lut.grade.service.UserService;
import com.lut.grade.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/** 用户登录 / 退出。 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
        if (loginUser != null) {
            response.sendRedirect(request.getContextPath() + homeOf(loginUser.getRole()));
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            LoginUser loginUser = userService.login(username, password);
            HttpSession session = request.getSession();
            session.setAttribute(LoginUser.SESSION_KEY, loginUser);
            session.setAttribute("realName", loginUser.getRealName());
            response.sendRedirect(request.getContextPath() + homeOf(loginUser.getRole()));
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    /**
     * 各角色登录后的落地页。
     * 现阶段统一进入 /home 面板，待各角色功能页完成后可直接换成对应主页。
     */
    private String homeOf(Integer role) {
        return "/home";
    }
}
