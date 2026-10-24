<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="课程管理"/>
<c:set var="navActive" value="admin:course"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0">课程管理</h4>
    <a class="btn btn-primary btn-sm" href="${ctx}/admin/course?method=toAdd">新增课程</a>
</div>

<form class="row g-2 mb-3" method="get" action="${ctx}/admin/course">
    <input type="hidden" name="method" value="list">
    <div class="col-auto">
        <input type="text" class="form-control" name="keyword" value="${keyword}" placeholder="课程编号或名称">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-outline-primary">查询</button>
    </div>
    <div class="col-auto ms-auto align-self-center text-secondary">共 ${page.totalCount} 门课程</div>
</form>

<table class="table table-bordered table-hover bg-white">
    <thead class="table-light">
    <tr>
        <th style="width:60px">序号</th>
        <th>课程编号</th>
        <th>课程名称</th>
        <th>学分</th>
        <th>任课教师</th>
        <th>学期</th>
        <th>选课/容量</th>
        <th>状态</th>
        <th style="width:130px">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="c" items="${page.list}" varStatus="st">
        <tr>
            <td>${(page.pageNo - 1) * page.pageSize + st.count}</td>
            <td>${c.courseCode}</td>
            <td>${c.courseName}</td>
            <td>${c.credit}</td>
            <td>${c.teacherName}</td>
            <td>${c.semester}</td>
            <td>${c.selectedCount} / ${c.capacity}</td>
            <td>
                <span class="badge ${c.status == 1 ? 'bg-success' : 'bg-secondary'}">
                    ${c.status == 1 ? '可选' : '停开'}
                </span>
            </td>
            <td>
                <a class="btn btn-sm btn-outline-primary"
                   href="${ctx}/admin/course?method=toEdit&id=${c.id}">编辑</a>
                <a class="btn btn-sm btn-outline-danger"
                   href="${ctx}/admin/course?method=delete&id=${c.id}"
                   onclick="return confirm('删除课程会同时清除其选课与成绩记录，确定继续？')">删除</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty page.list}">
        <tr><td colspan="9" class="empty-tip">暂无课程数据</td></tr>
    </c:if>
    </tbody>
</table>

<c:if test="${page.totalPage > 1}">
    <nav>
        <ul class="pagination pagination-sm justify-content-end">
            <li class="page-item ${page.pageNo <= 1 ? 'disabled' : ''}">
                <a class="page-link" href="${ctx}/admin/course?method=list&pageNo=${page.prevPage}&keyword=${keyword}">上一页</a>
            </li>
            <c:forEach begin="1" end="${page.totalPage}" var="p">
                <li class="page-item ${p == page.pageNo ? 'active' : ''}">
                    <a class="page-link" href="${ctx}/admin/course?method=list&pageNo=${p}&keyword=${keyword}">${p}</a>
                </li>
            </c:forEach>
            <li class="page-item ${page.pageNo >= page.totalPage ? 'disabled' : ''}">
                <a class="page-link" href="${ctx}/admin/course?method=list&pageNo=${page.nextPage}&keyword=${keyword}">下一页</a>
            </li>
        </ul>
    </nav>
</c:if>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
