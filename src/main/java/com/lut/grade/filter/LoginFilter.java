package com.lut.grade.filter;

import com.lut.grade.model.LoginUser;
import com.lut.grade.model.entity.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登录校验 + RBAC 权限过滤器。
 *
 * 放行规则：
 * 1. 白名单资源（登录页、静态资源、头像读取）直接放行；
 * 2. 未登录 -> 重定向登录页；
 * 3. 已登录但当前 URL 所需权限码不在其角色权限集合中 -> 转发至 403 页面。
 */
public class LoginFilter implements Filter {

    private static final Set<String> WHITE_LIST = new HashSet<>(Arrays.asList(
            "/login", "/logout", "/views/login.jsp", "/file/", "/static/"));

    /** 受保护 URL 前缀 -> 所需权限码。 */
    private static final String[][] PROTECTED = {
            {"/admin/dashboard", "admin:dashboard"},
            {"/admin/student", "admin:student"},
            {"/admin/teacher", "admin:teacher"},
            {"/admin/course", "admin:course"},
            {"/teacher/course", "teacher:course"},
            {"/teacher/grade", "teacher:grade"},
            {"/teacher/stat", "teacher:stat"},
            {"/student/selection", "student:selection"},
            {"/student/grade", "student:grade"},
            {"/student/stat", "student:stat"},
            {"/profile", "profile"},
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI().substring(contextPath.length());

        if (isWhiteListed(uri)) {
            chain.doFilter(request, response);
            return;
        }

        LoginUser loginUser = (LoginUser) req.getSession().getAttribute(LoginUser.SESSION_KEY);
        if (loginUser == null) {
            resp.sendRedirect(contextPath + "/login");
            return;
        }

        String required = matchPermCode(uri);
        List<String> permCodes = loginUser.getPermCodes();
        if (required != null && (permCodes == null || !permCodes.contains(required))) {
            req.setAttribute("error", "抱歉，您没有访问该功能的权限");
            req.getRequestDispatcher("/WEB-INF/views/common/403.jsp").forward(req, resp);
            return;
        }

        // 管理员访问 /teacher/* 或 /student/* 等特殊路由不做拦截，交给 Servlet 自行处理
        chain.doFilter(request, response);
    }

    private boolean isWhiteListed(String uri) {
        for (String prefix : WHITE_LIST) {
            if (uri.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    /** 管理员放行所有已定义路由，避免管理员 Demo 时被挡。 */
    private String matchPermCode(String uri) {
        for (String[] pair : PROTECTED) {
            if (uri.startsWith(pair[0])) {
                return pair[1];
            }
        }
        return null;
    }

    @Override
    public void destroy() {
    }
}
