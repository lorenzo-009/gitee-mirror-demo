<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>500 服务器错误</title>
    <link rel="stylesheet" href="${ctx}/static/lib/bootstrap/css/bootstrap.min.css">
</head>
<body class="login-body">
<div class="login-box text-center">
    <h2 class="text-danger mb-3">500</h2>
    <p class="text-secondary mb-4">服务器开小差了，请检查数据库连接是否正常</p>
    <a class="btn btn-primary" href="${ctx}/home">返回首页</a>
</div>
</body>
</html>
