package com.lut.grade.controller.admin;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.entity.Student;
import com.lut.grade.service.StudentService;
import com.lut.grade.service.impl.StudentServiceImpl;
import com.lut.grade.util.Page;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

/** 管理员端：学生信息增删改查。 */
@WebServlet("/admin/student")
public class AdminStudentServlet extends BaseServlet {

    private final StudentService studentService = new StudentServiceImpl();

    /**
     * 默认入口：直接进入列表页。
     * 菜单中只配置了 /admin/student 而没有 method 参数，
     * BaseServlet 会默认分发到 execute，若不定义本方法将返回 400。
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        list(request, response);
    }

    /** 列表 + 分页 + 关键字查询。 */
    public void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        } catch (NumberFormatException ignored) {
        }
        consumeFlash(request);
        Page<Student> page = studentService.page(pageNo, keyword);
        request.setAttribute("page", page);
        request.setAttribute("keyword", keyword);
        request.setAttribute("pageTitle", "学生管理");
        request.setAttribute("navActive", "admin:student");
        forward(request, response, "/WEB-INF/views/admin/student_list.jsp");
    }

    /** 跳转新增表单。 */
    public void toAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "新增学生");
        request.setAttribute("navActive", "admin:student");
        forward(request, response, "/WEB-INF/views/admin/student_form.jsp");
    }

    /** 跳转编辑表单。 */
    public void toEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Long.valueOf(request.getParameter("id"));
        request.setAttribute("student", studentService.findById(id));
        request.setAttribute("pageTitle", "编辑学生");
        request.setAttribute("navActive", "admin:student");
        forward(request, response, "/WEB-INF/views/admin/student_form.jsp");
    }

    /** 保存：新增或更新。 */
    public void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Student student = new Student();
        student.setStudentNo(request.getParameter("studentNo"));
        student.setName(request.getParameter("name"));
        student.setGender(toInt(request.getParameter("gender"), 0));
        student.setPhone(request.getParameter("phone"));
        student.setEmail(request.getParameter("email"));
        String enrollDate = request.getParameter("enrollDate");
        if (enrollDate != null && !enrollDate.isEmpty()) {
            student.setEnrollDate(LocalDate.parse(enrollDate));
        }

        try {
            if (id == null || id.isEmpty()) {
                studentService.create(student, request.getParameter("username"),
                        request.getParameter("password"));
                setFlash(request, "学生新增成功，已同步创建登录账号");
            } else {
                student.setId(Long.valueOf(id));
                studentService.update(student);
                setFlash(request, "学生信息修改成功");
            }
            response.sendRedirect(request.getContextPath() + "/admin/student?method=list");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("student", student);
            request.setAttribute("pageTitle", id == null ? "新增学生" : "编辑学生");
            request.setAttribute("navActive", "admin:student");
            forward(request, response, "/WEB-INF/views/admin/student_form.jsp");
        }
    }

    /** 删除学生（级联删除其登录账号，成绩与选课由外键级联清理）。 */
    public void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        studentService.delete(Long.valueOf(request.getParameter("id")));
        setFlash(request, "学生删除成功");
        response.sendRedirect(request.getContextPath() + "/admin/student?method=list");
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
