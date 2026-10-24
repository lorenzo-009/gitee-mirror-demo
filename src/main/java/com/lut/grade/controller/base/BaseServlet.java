package com.lut.grade.controller.base;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Servlet 基类：按请求参数 method 反射分发到具体处理方法。
 *
 * 用法：子类继承本类，写 public void list(HttpServletRequest, HttpServletResponse)，
 * 前端请求 xxx?method=list 即自动调用，避免一个功能一个 Servlet 类导致类爆炸。
 */
public abstract class BaseServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String methodName = request.getParameter("method");
        if (methodName == null || methodName.trim().isEmpty()) {
            methodName = "execute";
        }
        try {
            Method method = this.getClass().getMethod(methodName,
                    HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, request, response);
        } catch (NoSuchMethodException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "不支持的操作：" + methodName);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            if (cause instanceof com.lut.grade.exception.ServiceException) {
                request.setAttribute("error", cause.getMessage());
                forwardToDefault(request, response);
            } else {
                throw new ServletException("处理请求失败", cause == null ? e : cause);
            }
        }
    }

    /** 默认的转发目标，子类可覆盖为该类对应的列表页。 */
    protected void forwardToDefault(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/common/error500.jsp")
                .forward(request, response);
    }

    /** 存入一次性提示（重定向后仍然可见）。 */
    protected void setFlash(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flash", message);
    }

    /** 取出一次性提示并写入 request 域，同时从 Session 中清除。 */
    protected void consumeFlash(HttpServletRequest request) {
        Object flash = request.getSession().getAttribute("flash");
        if (flash != null) {
            request.setAttribute("message", flash);
            request.getSession().removeAttribute("flash");
        }
    }
}
