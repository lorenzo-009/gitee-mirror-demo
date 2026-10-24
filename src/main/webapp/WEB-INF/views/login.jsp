<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>学生成绩管理系统 · 登录</title>
    <link rel="stylesheet" href="${ctx}/static/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/app.css">
</head>
<body class="login-body">

<div class="login-box">
    <div class="text-center mb-4">
        <h3 class="text-primary mb-1">学生成绩管理系统</h3>
        <div class="text-secondary small">Student Grade Management System</div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger py-2 small">${error}</div>
    </c:if>

    <form action="${ctx}/login" method="post">
        <div class="mb-3">
            <label for="username" class="form-label">用户名</label>
            <input type="text" class="form-control" id="username" name="username"
                   value="${username}" placeholder="请输入用户名" autocomplete="off" required>
        </div>
        <div class="mb-4">
            <label for="password" class="form-label">密码</label>
            <input type="password" class="form-control" id="password" name="password"
                   placeholder="请输入密码" required>
        </div>
        <button type="submit" class="btn btn-primary w-100">登 录</button>
    </form>

    <div class="login-tip mt-4">
        <div>演示账号（密码统一 <code>123456</code>）</div>
        <div>管理员 <code>admin</code> ｜ 教师 <code>T2020002</code>（韩雪松）</div>
        <div>学生 <code>250162701050</code>（刘开宇）</div>
    </div>
</div>

</body>
</html>
