package com.lut.grade.controller.teacher;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.LoginUser;
import com.lut.grade.model.vo.CourseStatVO;
import com.lut.grade.model.vo.GradeVO;
import com.lut.grade.service.CourseService;
import com.lut.grade.service.GradeService;
import com.lut.grade.service.impl.CourseServiceImpl;
import com.lut.grade.service.impl.GradeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/** 教师端：成绩录入与修改。 */
@WebServlet({"/teacher/grade", "/teacher/course", "/teacher/stat"})
public class GradeServlet extends BaseServlet {

    private final GradeService gradeService = new GradeServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();

    /**
     * 默认入口：列出本人所授课程，教师选择课程后再进入成绩单或统计页。
     * 菜单中的“我的课程”与“成绩统计”分别落在 /teacher/course 与 /teacher/stat，
     * 未带 courseId 时同样展示课程列表，避免出现 404。
     */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String uri = request.getRequestURI();
        String navActive = "teacher:grade";
        String pageTitle = "成绩录入";
        if (uri.endsWith("/teacher/course")) {
            navActive = "teacher:course";
            pageTitle = "我的课程";
        } else if (uri.endsWith("/teacher/stat")) {
            navActive = "teacher:stat";
            pageTitle = "成绩统计";
        }
        request.setAttribute("courses", courseService.findByTeacher(loginUser.getProfileId()));
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("navActive", navActive);
        forward(request, response, "/WEB-INF/views/teacher/grade_course_list.jsp");
    }

    /** 某门课的成绩单，支持按学号/姓名过滤。 */
    public void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long courseId = Long.valueOf(request.getParameter("courseId"));
        String keyword = request.getParameter("keyword");
        List<GradeVO> list = gradeService.listByCourse(courseId, keyword);

        request.setAttribute("course", courseService.findById(courseId));
        request.setAttribute("list", list);
        request.setAttribute("keyword", keyword);
        request.setAttribute("pageTitle", "成绩录入");
        request.setAttribute("navActive", "teacher:grade");
        forward(request, response, "/WEB-INF/views/teacher/grade_list.jsp");
    }

    /** 保存单条成绩。 */
    public void save(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long courseId = Long.valueOf(request.getParameter("courseId"));
        Long studentId = Long.valueOf(request.getParameter("studentId"));
        String scoreStr = request.getParameter("score");
        String examType = request.getParameter("examType");

        try {
            if (scoreStr == null || scoreStr.trim().isEmpty()) {
                throw new ServiceException("成绩不能为空");
            }
            gradeService.save(courseId, studentId, new BigDecimal(scoreStr.trim()), examType);
            setFlash(request, "成绩已保存");
        } catch (ServiceException e) {
            setFlash(request, e.getMessage());
        } catch (NumberFormatException e) {
            setFlash(request, "成绩必须是数字");
        }
        response.sendRedirect(request.getContextPath()
                + "/teacher/grade?method=list&courseId=" + courseId);
    }

    /** 清除某学生的成绩。 */
    public void remove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long courseId = Long.valueOf(request.getParameter("courseId"));
        Long studentId = Long.valueOf(request.getParameter("studentId"));
        gradeService.remove(courseId, studentId);
        setFlash(request, "成绩已清除");
        response.sendRedirect(request.getContextPath()
                + "/teacher/grade?method=list&courseId=" + courseId);
    }

    /** 单科成绩统计。 */
    public void stat(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long courseId = Long.valueOf(request.getParameter("courseId"));
        CourseStatVO stat = gradeService.statByCourse(courseId);
        request.setAttribute("stat", stat);
        request.setAttribute("pageTitle", "成绩统计");
        request.setAttribute("navActive", "teacher:stat");
        forward(request, response, "/WEB-INF/views/teacher/grade_stat.jsp");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}
