<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>403 无权限</title>
    <link rel="stylesheet" href="${ctx}/static/lib/bootstrap/css/bootstrap.min.css">
</head>
<body class="login-body">
<div class="login-box text-center">
    <h2 class="text-danger mb-3">403</h2>
    <p class="text-secondary mb-4">${error}</p>
    <a class="btn btn-primary" href="${ctx}/home">返回首页</a>
</div>
</body>
</html>
