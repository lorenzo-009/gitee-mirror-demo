package com.lut.grade.exception;

/**
 * 业务异常：Service 层发现业务规则冲突时抛出，
 * 由 Servlet 捕获后转成 request 域中的错误提示，回显到 JSP。
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
