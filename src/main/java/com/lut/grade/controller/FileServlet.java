package com.lut.grade.controller;

import com.lut.grade.util.UploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 读取磁盘上的头像文件并回写给浏览器。
 * 头像是存在 webapp 之外目录的，只能通过本 Servlet 输出。
 */
@WebServlet("/file/avatar/*")
public class FileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String fileName = uri.substring(uri.lastIndexOf('/') + 1);
        Path file = UploadUtil.resolveAvatar(fileName);

        if (file == null || !Files.exists(file)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = Files.probeContentType(file);
        response.setContentType(contentType == null ? "image/png" : contentType);
        response.setContentLengthLong(Files.size(file));
        try (OutputStream out = response.getOutputStream()) {
            Files.copy(file, out);
        }
    }
}
