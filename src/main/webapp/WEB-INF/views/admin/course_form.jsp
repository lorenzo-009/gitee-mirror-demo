<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${empty course.id ? '新增课程' : '编辑课程'}"/>
<c:set var="navActive" value="admin:course"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-4">${empty course.id ? '新增课程' : '编辑课程信息'}</h4>

<form method="post" action="${ctx}/admin/course" class="bg-white p-4 border rounded" style="max-width:640px">
    <input type="hidden" name="method" value="save">
    <input type="hidden" name="id" value="${course.id}">

    <div class="mb-3">
        <label class="form-label">课程编号</label>
        <input type="text" class="form-control" name="courseCode" value="${course.courseCode}"
               placeholder="如 CS1001" required>
    </div>

    <div class="mb-3">
        <label class="form-label">课程名称</label>
        <input type="text" class="form-control" name="courseName" value="${course.courseName}" required>
    </div>

    <div class="mb-3">
        <label class="form-label">学分</label>
        <input type="text" class="form-control" name="credit" value="${course.credit}" placeholder="如 3.0">
    </div>

    <div class="mb-3">
        <label class="form-label">任课教师</label>
        <select class="form-select" name="teacherId">
            <option value="">暂不指派</option>
            <c:forEach var="t" items="${teachers}">
                <option value="${t.id}" ${course.teacherId == t.id ? 'selected' : ''}>${t.name}（${t.teacherNo}）</option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label class="form-label">开课学期</label>
        <input type="text" class="form-control" name="semester" value="${course.semester}"
               placeholder="如 2026-2027-1">
    </div>

    <div class="mb-3">
        <label class="form-label">选课容量</label>
        <input type="number" class="form-control" name="capacity" value="${course.capacity}">
    </div>

    <div class="mb-3">
        <label class="form-label">课程简介</label>
        <textarea class="form-control" name="description" rows="3">${course.description}</textarea>
    </div>

    <div class="mb-3">
        <label class="form-label">状态</label>
        <select class="form-select" name="status">
            <option value="1" ${course.status == 1 ? 'selected' : ''}>可选</option>
            <option value="0" ${course.status == 0 ? 'selected' : ''}>停开</option>
        </select>
    </div>

    <div class="mt-4">
        <button type="submit" class="btn btn-primary">保存</button>
        <a class="btn btn-outline-secondary ms-2" href="${ctx}/admin/course?method=list">返回列表</a>
    </div>
</form>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
