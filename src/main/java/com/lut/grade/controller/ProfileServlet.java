package com.lut.grade.controller;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.LoginUser;
import com.lut.grade.service.UserService;
import com.lut.grade.service.impl.UserServiceImpl;
import com.lut.grade.util.UploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/** 个人中心：修改密码、上传头像（三种角色通用）。 */
@WebServlet("/profile")
@MultipartConfig(maxFileSize = UploadUtil.MAX_SIZE)
public class ProfileServlet extends BaseServlet {

    private final UserService userService = new UserServiceImpl();

    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = currentUser(request);
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("loginUser", loginUser);
        request.setAttribute("pageTitle", "个人中心");
        request.setAttribute("navActive", "profile");
        forward(request, response, "/WEB-INF/views/profile.jsp");
    }

    /** 修改密码。 */
    public void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = currentUser(request);
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirm = request.getParameter("confirmPassword");

        try {
            if (newPassword == null || !newPassword.equals(confirm)) {
                throw new ServiceException("两次输入的新密码不一致");
            }
            userService.changePassword(loginUser.getUserId(), oldPassword, newPassword);
            setFlash(request, "密码修改成功，请使用新密码重新登录");
            response.sendRedirect(request.getContextPath() + "/profile");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("loginUser", loginUser);
            request.setAttribute("pageTitle", "个人中心");
            request.setAttribute("navActive", "profile");
            forward(request, response, "/WEB-INF/views/profile.jsp");
        }
    }

    /** 上传头像。 */
    public void uploadAvatar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = currentUser(request);
        Part part = request.getPart("avatar");
        if (part == null || part.getSize() == 0) {
            setFlash(request, "请先选择要上传的图片");
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }
        try {
            String fileName = UploadUtil.saveAvatar(part);
            userService.updateAvatar(loginUser.getUserId(), fileName);
            loginUser.setAvatar(fileName);
            request.getSession().setAttribute(LoginUser.SESSION_KEY, loginUser);
            setFlash(request, "头像上传成功");
        } catch (Exception e) {
            setFlash(request, "头像上传失败：" + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private LoginUser currentUser(HttpServletRequest request) {
        return (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}
