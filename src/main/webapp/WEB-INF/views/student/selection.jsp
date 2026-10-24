<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="我的选课"/>
<c:set var="navActive" value="student:selection"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-3">我的选课</h4>

<h6 class="mt-4 mb-2">已选课程</h6>
<table class="table table-bordered table-hover bg-white">
    <thead class="table-light">
    <tr>
        <th style="width:60px">序号</th>
        <th>课程编号</th>
        <th>课程名称</th>
        <th>学分</th>
        <th>任课教师</th>
        <th>学期</th>
        <th style="width:100px">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${selected}" varStatus="st">
        <tr>
            <td>${st.count}</td>
            <td>${c.courseCode}</td>
            <td>${c.courseName}</td>
            <td>${c.credit}</td>
            <td>${c.teacherName}</td>
            <td>${c.semester}</td>
            <td>
                <a class="btn btn-sm btn-outline-danger"
                   href="${ctx}/student/selection?method=drop&courseId=${c.id}"
                   onclick="return confirm('退选后该课程的成绩记录会一并清除，确定退选？')">退选</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty selected}">
        <tr><td colspan="7" class="empty-tip">当前没有已选课程</td></tr>
    </c:if>
    </tbody>
</table>

<h6 class="mt-4 mb-2">可选课程</h6>
<form class="row g-2 mb-3" method="get" action="${ctx}/student/selection">
    <div class="col-auto">
        <input type="text" class="form-control" name="keyword" value="${keyword}" placeholder="课程编号或名称">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-outline-primary">查询</button>
    </div>
</form>

<table class="table table-bordered table-hover bg-white">
    <thead class="table-light">
    <tr>
        <th style="width:60px">序号</th>
        <th>课程编号</th>
        <th>课程名称</th>
        <th>学分</th>
        <th>任课教师</th>
        <th>已选/容量</th>
        <th style="width:100px">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${selectable}" varStatus="st">
        <tr>
            <td>${st.count}</td>
            <td>${c.courseCode}</td>
            <td>${c.courseName}</td>
            <td>${c.credit}</td>
            <td>${c.teacherName}</td>
            <td>${c.selectedCount} / ${c.capacity}</td>
            <td>
                <a class="btn btn-sm btn-primary"
                   href="${ctx}/student/selection?method=select&courseId=${c.id}">选课</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty selectable}">
        <tr><td colspan="7" class="empty-tip">暂无可选课程</td></tr>
    </c:if>
    </tbody>
</table>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
