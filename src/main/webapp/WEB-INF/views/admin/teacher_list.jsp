<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="教师管理"/>
<c:set var="navActive" value="admin:teacher"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0">教师管理</h4>
    <a class="btn btn-primary btn-sm" href="${ctx}/admin/teacher?method=toAdd">新增教师</a>
</div>

<form class="row g-2 mb-3" method="get" action="${ctx}/admin/teacher">
    <input type="hidden" name="method" value="list">
    <div class="col-auto">
        <input type="text" class="form-control" name="keyword" value="${keyword}" placeholder="工号或姓名">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-outline-primary">查询</button>
    </div>
    <div class="col-auto ms-auto align-self-center text-secondary">共 ${page.totalCount} 条记录</div>
</form>

<table class="table table-bordered table-hover bg-white">
    <thead class="table-light">
    <tr>
        <th style="width:60px">序号</th>
        <th>工号</th>
        <th>姓名</th>
        <th>职称</th>
        <th>联系电话</th>
        <th>邮箱</th>
        <th>登录账号</th>
        <th style="width:130px">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="t" items="${page.list}" varStatus="st">
        <tr>
            <td>${(page.pageNo - 1) * page.pageSize + st.count}</td>
            <td>${t.teacherNo}</td>
            <td>${t.name}</td>
            <td>${t.title}</td>
            <td>${t.phone}</td>
            <td>${t.email}</td>
            <td><code>${t.username}</code></td>
            <td>
                <a class="btn btn-sm btn-outline-primary"
                   href="${ctx}/admin/teacher?method=toEdit&id=${t.id}">编辑</a>
                <a class="btn btn-sm btn-outline-danger"
                   href="${ctx}/admin/teacher?method=delete&id=${t.id}"
                   onclick="return confirm('删除教师会同时清除其登录账号，其名下课程将变为无任课教师，确定继续？')">删除</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty page.list}">
        <tr><td colspan="8" class="empty-tip">暂无教师数据</td></tr>
    </c:if>
    </tbody>
</table>

<c:if test="${page.totalPage > 1}">
    <nav>
        <ul class="pagination pagination-sm justify-content-end">
            <li class="page-item ${page.pageNo <= 1 ? 'disabled' : ''}">
                <a class="page-link" href="${ctx}/admin/teacher?method=list&pageNo=${page.prevPage}&keyword=${keyword}">上一页</a>
            </li>
            <c:forEach begin="1" end="${page.totalPage}" var="p">
                <li class="page-item ${p == page.pageNo ? 'active' : ''}">
                    <a class="page-link" href="${ctx}/admin/teacher?method=list&pageNo=${p}&keyword=${keyword}">${p}</a>
                </li>
            </c:forEach>
            <li class="page-item ${page.pageNo >= page.totalPage ? 'disabled' : ''}">
                <a class="page-link" href="${ctx}/admin/teacher?method=list&pageNo=${page.nextPage}&keyword=${keyword}">下一页</a>
            </li>
        </ul>
    </nav>
</c:if>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
