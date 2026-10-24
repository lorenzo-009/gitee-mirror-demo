package com.lut.grade.controller.admin;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.LoginUser;
import com.lut.grade.model.entity.Course;
import com.lut.grade.model.entity.Teacher;
import com.lut.grade.service.CourseService;
import com.lut.grade.service.TeacherService;
import com.lut.grade.service.impl.CourseServiceImpl;
import com.lut.grade.service.impl.TeacherServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/** 管理员端：课程信息维护。 */
@WebServlet("/admin/course")
public class AdminCourseServlet extends BaseServlet {

    private final CourseService courseService = new CourseServiceImpl();

    /** 默认入口：直接进入列表页（菜单链接未带 method 参数时走这里）。 */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        list(request, response);
    }

    private final TeacherService teacherService = new TeacherServiceImpl();

    public void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        consumeFlash(request);
        String keyword = request.getParameter("keyword");
        int pageNo = toInt(request.getParameter("pageNo"), 1);
        request.setAttribute("page", courseService.page(pageNo, keyword, null));
        request.setAttribute("keyword", keyword);
        request.setAttribute("teachers", teacherList());
        request.setAttribute("pageTitle", "课程管理");
        request.setAttribute("navActive", "admin:course");
        forward(request, response, "/WEB-INF/views/admin/course_list.jsp");
    }

    public void toAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("teachers", teacherList());
        request.setAttribute("pageTitle", "新增课程");
        request.setAttribute("navActive", "admin:course");
        forward(request, response, "/WEB-INF/views/admin/course_form.jsp");
    }

    public void toEdit(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("course", courseService.findById(Long.valueOf(request.getParameter("id"))));
        request.setAttribute("teachers", teacherList());
        request.setAttribute("pageTitle", "编辑课程");
        request.setAttribute("navActive", "admin:course");
        forward(request, response, "/WEB-INF/views/admin/course_form.jsp");
    }

    public void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        Course course = new Course();
        course.setCourseCode(request.getParameter("courseCode"));
        course.setCourseName(request.getParameter("courseName"));
        course.setCredit(toBigDecimal(request.getParameter("credit")));
        course.setTeacherId(toLongOrNull(request.getParameter("teacherId")));
        course.setSemester(request.getParameter("semester"));
        course.setCapacity(toInt(request.getParameter("capacity"), 60));
        course.setDescription(request.getParameter("description"));
        course.setStatus(toInt(request.getParameter("status"), 1));

        try {
            if (id == null || id.isEmpty()) {
                courseService.save(course);
                setFlash(request, "课程新增成功");
            } else {
                course.setId(Long.valueOf(id));
                courseService.update(course);
                setFlash(request, "课程修改成功");
            }
            response.sendRedirect(request.getContextPath() + "/admin/course?method=list");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("course", course);
            request.setAttribute("teachers", teacherList());
            forward(request, response, "/WEB-INF/views/admin/course_form.jsp");
        }
    }

    public void delete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        courseService.delete(Long.valueOf(request.getParameter("id")));
        setFlash(request, "课程删除成功，关联选课与成绩已一并清除");
        response.sendRedirect(request.getContextPath() + "/admin/course?method=list");
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

    private Long toLongOrNull(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal toBigDecimal(String value) {
        if (value == null || value.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    /** 教师下拉框数据源。 */
    private List<Teacher> teacherList() {
        return teacherService.findAll();
    }
}
