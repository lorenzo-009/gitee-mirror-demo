<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${empty teacher.id ? '新增教师' : '编辑教师'}"/>
<c:set var="navActive" value="admin:teacher"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-4">${empty teacher.id ? '新增教师' : '编辑教师信息'}</h4>

<form method="post" action="${ctx}/admin/teacher" class="bg-white p-4 border rounded" style="max-width:640px">
    <input type="hidden" name="method" value="save">
    <input type="hidden" name="id" value="${teacher.id}">

    <div class="mb-3">
        <label class="form-label">工号</label>
        <input type="text" class="form-control" name="teacherNo" value="${teacher.teacherNo}"
               placeholder="如 T2020001" required>
    </div>

    <div class="mb-3">
        <label class="form-label">姓名</label>
        <input type="text" class="form-control" name="name" value="${teacher.name}" required>
    </div>

    <div class="mb-3">
        <label class="form-label">职称</label>
        <input type="text" class="form-control" name="title" value="${teacher.title}" placeholder="如 副教授">
    </div>

    <div class="mb-3">
        <label class="form-label">联系电话</label>
        <input type="text" class="form-control" name="phone" value="${teacher.phone}">
    </div>

    <div class="mb-3">
        <label class="form-label">邮箱</label>
        <input type="text" class="form-control" name="email" value="${teacher.email}">
    </div>

    <c:if test="${empty teacher.id}">
        <hr>
        <div class="alert alert-info py-2 small">新增时会同时创建同名登录账号，请设置初始密码（至少 6 位）。</div>
        <div class="mb-3">
            <label class="form-label">登录账号</label>
            <input type="text" class="form-control" name="username" placeholder="如 t003" required>
        </div>
        <div class="mb-3">
            <label class="form-label">初始密码</label>
            <input type="password" class="form-control" name="password" value="123456" required>
        </div>
    </c:if>

    <div class="mt-4">
        <button type="submit" class="btn btn-primary">保存</button>
        <a class="btn btn-outline-secondary ms-2" href="${ctx}/admin/teacher?method=list">返回列表</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
