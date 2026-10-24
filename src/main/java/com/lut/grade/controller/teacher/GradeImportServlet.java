package com.lut.grade.controller.teacher;

import com.lut.grade.controller.base.BaseServlet;
import com.lut.grade.exception.ServiceException;
import com.lut.grade.model.LoginUser;
import com.lut.grade.service.CourseService;
import com.lut.grade.service.GradeService;
import com.lut.grade.service.impl.CourseServiceImpl;
import com.lut.grade.service.impl.GradeServiceImpl;
import com.lut.grade.util.ExcelUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/** 教师端：Excel 批量导入成绩。 */
@WebServlet("/teacher/grade/import")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024L)
public class GradeImportServlet extends BaseServlet {

    private final GradeService gradeService = new GradeServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();

    /** 导入页面。 */
    public void execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginUser loginUser = currentUser(request);
        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        consumeFlash(request);
        request.setAttribute("courses", courseService.findByTeacher(loginUser.getProfileId()));
        request.setAttribute("pageTitle", "批量导入");
        request.setAttribute("navActive", "teacher:import");
        forward(request, response, "/WEB-INF/views/teacher/grade_import.jsp");
    }

    /** 接收上传的 Excel 并导入。 */
    public void upload(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long courseId = Long.valueOf(request.getParameter("courseId"));
        Part part = request.getPart("file");
        if (part == null || part.getSize() == 0) {
            setFlash(request, "请先选择要导入的 Excel 文件");
            response.sendRedirect(request.getContextPath() + "/teacher/grade/import");
            return;
        }
        try (InputStream in = part.getInputStream()) {
            List<ExcelUtil.GradeRow> rows = ExcelUtil.readGradeRows(in);
            int count = gradeService.batchImport(courseId, rows);
            setFlash(request, "导入成功，共写入 " + count + " 条成绩");
        } catch (ServiceException e) {
            setFlash(request, e.getMessage());
        } catch (Exception e) {
            setFlash(request, "文件格式无法识别，请使用 xls / xlsx 成绩模板");
        }
        response.sendRedirect(request.getContextPath() + "/teacher/grade/import");
    }

    private LoginUser currentUser(HttpServletRequest request) {
        return (LoginUser) request.getSession().getAttribute(LoginUser.SESSION_KEY);
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
        request.getRequestDispatcher(path).forward(request, response);
    }
}
