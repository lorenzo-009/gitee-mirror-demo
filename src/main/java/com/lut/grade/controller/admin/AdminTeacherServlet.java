package com.lut.grade.controller.admin;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.entity.Teacher;
import com.lut.grade.service.TeacherService;
import com.lut.grade.service.impl.TeacherServiceImpl;
import com.lut.grade.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 管理员端：教师信息增删改查。 */
@WebServlet("/admin/teacher")
public class AdminTeacherServlet extends BaseServlet {

    private final TeacherService teacherService = new TeacherServiceImpl();

    /** 默认入口：直接进入列表页（菜单链接未带 method 参数时走这里）。 */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        list(request, response);
    }


    public void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        consumeFlash(request);
        String keyword = request.getParameter("keyword");
        int pageNo = toInt(request.getParameter("pageNo"), 1);
        Page<Teacher> page = teacherService.page(pageNo, keyword);
        request.setAttribute("page", page);
        request.setAttribute("keyword", keyword);
        request.setAttribute("pageTitle", "教师管理");
        request.setAttribute("navActive", "admin:teacher");
        forward(request, response, "/WEB-INF/views/admin/teacher_list.jsp");
    }

    public void toAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "新增教师");
        request.setAttribute("navActive", "admin:teacher");
        forward(request, response, "/WEB-INF/views/admin/teacher_form.jsp");
    }

    public void toEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("teacher", teacherService.findById(Long.valueOf(request.getParameter("id"))));
        request.setAttribute("pageTitle", "编辑教师");
        request.setAttribute("navActive", "admin:teacher");
        forward(request, response, "/WEB-INF/views/admin/teacher_form.jsp");
    }

    public void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Teacher teacher = new Teacher();
        teacher.setTeacherNo(request.getParameter("teacherNo"));
        teacher.setName(request.getParameter("name"));
        teacher.setTitle(request.getParameter("title"));
        teacher.setPhone(request.getParameter("phone"));
        teacher.setEmail(request.getParameter("email"));

        try {
            if (id == null || id.isEmpty()) {
                teacherService.create(teacher, request.getParameter("username"),
                        request.getParameter("password"));
                setFlash(request, "教师新增成功，已同步创建登录账号");
            } else {
                teacher.setId(Long.valueOf(id));
                teacherService.update(teacher);
                setFlash(request, "教师信息修改成功");
            }
            response.sendRedirect(request.getContextPath() + "/admin/teacher?method=list");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("teacher", teacher);
            request.setAttribute("pageTitle", id == null ? "新增教师" : "编辑教师");
            request.setAttribute("navActive", "admin:teacher");
            forward(request, response, "/WEB-INF/views/admin/teacher_form.jsp");
        }
    }

    public void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        teacherService.delete(Long.valueOf(request.getParameter("id")));
        setFlash(request, "教师删除成功");
        response.sendRedirect(request.getContextPath() + "/admin/teacher?method=list");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }

    private int toInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
