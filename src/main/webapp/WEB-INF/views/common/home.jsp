<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>系统首页 · 学生成绩管理系统</title>
    <link rel="stylesheet" href="${ctx}/static/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${ctx}/static/css/app.css">
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
        <a class="navbar-brand" href="${ctx}/home">学生成绩管理系统</a>
        <div class="d-flex align-items-center text-white">
            <span class="me-3">${loginUser.realName}（${loginUser.roleName}）</span>
            <a class="btn btn-outline-light btn-sm" href="${ctx}/logout">退出登录</a>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar pt-3">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link active" href="${ctx}/home">系统首页</a>
                </li>
                <c:forEach var="menu" items="${loginUser.menus}">
                    <li class="nav-item">
                        <a class="nav-link" href="${ctx}${menu.url}">${menu.permName}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>

        <main class="col-md-10 ms-sm-auto px-md-4 pt-4">
            <h4 class="mb-4">欢迎使用学生成绩管理系统</h4>

            <div class="row g-3 mb-4">
                <div class="col-md-3">
                    <div class="card text-center shadow-sm">
                        <div class="card-body">
                            <div class="text-secondary small mb-1">登录账号</div>
                            <div class="fs-5 fw-semibold text-primary">${loginUser.username}</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center shadow-sm">
                        <div class="card-body">
                            <div class="text-secondary small mb-1">姓名</div>
                            <div class="fs-5 fw-semibold">${loginUser.realName}</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center shadow-sm">
                        <div class="card-body">
                            <div class="text-secondary small mb-1">角色</div>
                            <div class="fs-5 fw-semibold">${loginUser.roleName}</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center shadow-sm">
                        <div class="card-body">
                            <div class="text-secondary small mb-1">可用功能</div>
                            <div class="fs-5 fw-semibold text-success">${loginUser.menus.size()}</div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="card shadow-sm">
                <div class="card-header bg-white fw-semibold">系统说明</div>
                <div class="card-body">
                    <p class="mb-2">本系统基于 <strong>Servlet + JSP + JDBC</strong> 原生技术栈开发，采用 MVC 分层架构，
                        支持管理员、教师、学生三种角色的分级权限控制。</p>
                    <p class="mb-0 text-secondary small">
                        左侧菜单根据当前登录用户的角色动态生成，由数据库的权限表驱动。
                    </p>
                </div>
            </div>
        </main>
    </div>
</div>

<script src="${ctx}/static/lib/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
