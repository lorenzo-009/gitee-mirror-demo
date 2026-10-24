<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="学生管理"/>
<c:set var="navActive" value="admin:student"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0">学生管理</h4>
    <a class="btn btn-primary btn-sm" href="${ctx}/admin/student?method=toAdd">新增学生</a>
</div>

<form class="row g-2 mb-3" method="get" action="${ctx}/admin/student">
    <input type="hidden" name="method" value="list">
    <div class="col-auto">
        <input type="text" class="form-control" name="keyword" value="${keyword}"
               placeholder="学号或姓名">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-outline-primary">查询</button>
    </div>
    <div class="col-auto">
        <a class="btn btn-outline-secondary" href="${ctx}/admin/student?method=list">重置</a>
    </div>
    <div class="col-auto ms-auto align-self-center text-secondary">
        共 ${page.totalCount} 条记录
    </div>
</form>

<table class="table table-bordered table-hover bg-white">
    <thead class="table-light">
    <tr>
        <th style="width:60px">序号</th>
        <th>学号</th>
        <th>姓名</th>
        <th>性别</th>
        <th>联系电话</th>
        <th>邮箱</th>
        <th>入学日期</th>
        <th>登录账号</th>
        <th style="width:130px">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="s" items="${page.list}" varStatus="st">
        <tr>
            <td>${(page.pageNo - 1) * page.pageSize + st.count}</td>
            <td>${s.studentNo}</td>
            <td>${s.name}</td>
            <td>${s.genderName}</td>
            <td>${s.phone}</td>
            <td>${s.email}</td>
            <td>${s.enrollDate}</td>
            <td><code>${s.username}</code></td>
            <td>
                <a class="btn btn-sm btn-outline-primary"
                   href="${ctx}/admin/student?method=toEdit&id=${s.id}">编辑</a>
                <a class="btn btn-sm btn-outline-danger"
                   href="${ctx}/admin/student?method=delete&id=${s.id}"
                   onclick="return confirm('删除后该学生的成绩与选课记录会一并清除，确定继续？')">删除</a>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty page.list}">
        <tr>
            <td colspan="9" class="empty-tip">暂无学生数据</td>
        </tr>
    </c:if>
    </tbody>
</table>

<c:if test="${page.totalPage > 1}">
    <nav>
        <ul class="pagination pagination-sm justify-content-end">
            <li class="page-item ${page.pageNo <= 1 ? 'disabled' : ''}">
                <a class="page-link" href="${ctx}/admin/student?method=list&pageNo=${page.prevPage}&keyword=${keyword}">上一页</a>
            </li>
            <c:forEach begin="1" end="${page.totalPage}" var="p">
                <li class="page-item ${p == page.pageNo ? 'active' : ''}">
                    <a class="page-link" href="${ctx}/admin/student?method=list&pageNo=${p}&keyword=${keyword}">${p}</a>
                </li>
            </c:forEach>
            <li class="page-item ${page.pageNo >= page.totalPage ? 'disabled' : ''}">
                <a class="page-link" href="${ctx}/admin/student?method=list&pageNo=${page.nextPage}&keyword=${keyword}">下一页</a>
            </li>
        </ul>
    </nav>
</c:if>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
