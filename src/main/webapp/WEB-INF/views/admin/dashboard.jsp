<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="系统概览"/>
<c:set var="navActive" value="admin:dashboard"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-4">系统概览</h4>

<div class="row g-3 mb-4">
    <div class="col-md-4">
        <div class="card text-center shadow-sm">
            <div class="card-body">
                <div class="text-secondary small mb-1">学生总数</div>
                <div class="display-6 text-primary">${studentCount}</div>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-center shadow-sm">
            <div class="card-body">
                <div class="text-secondary small mb-1">教师总数</div>
                <div class="display-6 text-success">${teacherCount}</div>
            </div>
        </div>
    </div>
    <div class="col-md-4">
        <div class="card text-center shadow-sm">
            <div class="card-body">
                <div class="text-secondary small mb-1">课程总数</div>
                <div class="display-6 text-warning">${courseCount}</div>
            </div>
        </div>
    </div>
</div>

<div class="card shadow-sm">
    <div class="card-header bg-white fw-semibold">快捷操作</div>
    <div class="card-body">
        <a class="btn btn-outline-primary me-2" href="${ctx}/admin/student?method=list">学生管理</a>
        <a class="btn btn-outline-primary me-2" href="${ctx}/admin/teacher?method=list">教师管理</a>
        <a class="btn btn-outline-primary" href="${ctx}/admin/course?method=list">课程管理</a>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
