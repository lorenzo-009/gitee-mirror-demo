package com.lut.grade.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一编码过滤器。
 * 请求进来先 setCharacterEncoding，响应统一 UTF-8，
 * 一次性根治中文乱码（三大乱码来源之一，另两个见 Tomcat URIEncoding 与 JDBC URL 参数）。
 */
public class EncodingFilter implements Filter {

    private String encoding = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String cfg = filterConfig.getInitParameter("encoding");
        if (cfg != null && !cfg.trim().isEmpty()) {
            this.encoding = cfg.trim();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        req.setCharacterEncoding(encoding);
        resp.setCharacterEncoding(encoding);

        // 静态资源与下载文件由容器按扩展名决定 MIME 类型，
        // 若在此统一设置 text/html，浏览器会拒绝把 CSS/JS 当作样式表或脚本执行。
        if (!isStaticResource(req)) {
            resp.setContentType("text/html;charset=" + encoding);
        }

        chain.doFilter(req, resp);
    }

    /** 判断是否为静态资源或文件请求，这类响应不应被改写为 text/html。 */
    private boolean isStaticResource(HttpServletRequest req) {
        String uri = req.getRequestURI();
        return uri.contains("/static/") || uri.contains("/file/");
    }

    @Override
    public void destroy() {
    }
}
