<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${empty student.id ? '新增学生' : '编辑学生'}"/>
<c:set var="navActive" value="admin:student"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-4">${empty student.id ? '新增学生' : '编辑学生信息'}</h4>

<form method="post" action="${ctx}/admin/student" class="bg-white p-4 border rounded" style="max-width:640px">
    <input type="hidden" name="method" value="save">
    <input type="hidden" name="id" value="${student.id}">

    <div class="mb-3">
        <label class="form-label">学号</label>
        <input type="text" class="form-control" name="studentNo" value="${student.studentNo}"
               placeholder="如 20260101" required>
    </div>

    <div class="mb-3">
        <label class="form-label">姓名</label>
        <input type="text" class="form-control" name="name" value="${student.name}" required>
    </div>

    <div class="mb-3">
        <label class="form-label">性别</label>
        <select class="form-select" name="gender">
            <option value="0" ${student.gender == 0 ? 'selected' : ''}>未知</option>
            <option value="1" ${student.gender == 1 ? 'selected' : ''}>男</option>
            <option value="2" ${student.gender == 2 ? 'selected' : ''}>女</option>
        </select>
    </div>

    <div class="mb-3">
        <label class="form-label">联系电话</label>
        <input type="text" class="form-control" name="phone" value="${student.phone}">
    </div>

    <div class="mb-3">
        <label class="form-label">邮箱</label>
        <input type="text" class="form-control" name="email" value="${student.email}">
    </div>

    <div class="mb-3">
        <label class="form-label">入学日期</label>
        <input type="date" class="form-control" name="enrollDate" value="${student.enrollDate}">
    </div>

    <c:if test="${empty student.id}">
        <hr>
        <div class="alert alert-info py-2 small">
            新增时会同时创建同名登录账号，初始密码请在此设置（至少 6 位）。
        </div>
        <div class="mb-3">
            <label class="form-label">登录账号</label>
            <input type="text" class="form-control" name="username" placeholder="如 s011" required>
        </div>
        <div class="mb-3">
            <label class="form-label">初始密码</label>
            <input type="password" class="form-control" name="password" value="123456" required>
        </div>
    </c:if>

    <div class="mt-4">
        <button type="submit" class="btn btn-primary">保存</button>
        <a class="btn btn-outline-secondary ms-2" href="${ctx}/admin/student?method=list">返回列表</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
