<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="我的成绩"/>
<c:set var="navActive" value="student:grade"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-3">我的成绩</h4>

<div class="row g-3 mb-4">
    <div class="col-md-4">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">平均分</div>
            <div class="fs-4 fw-semibold text-primary">${avg}</div>
        </div></div>
    </div>
    <div class="col-md-4">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">已修学分</div>
            <div class="fs-4 fw-semibold text-success">${credit}</div>
        </div></div>
    </div>
    <div class="col-md-4">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">平均绩点 GPA</div>
            <div class="fs-4 fw-semibold text-warning">${gpa}</div>
        </div></div>
    </div>
</div>

<table class="table table-bordered table-hover bg-white">
    <thead class="table-light">
    <tr>
        <th style="width:60px">序号</th>
        <th>课程编号</th>
        <th>课程名称</th>
        <th>学分</th>
        <th>成绩</th>
        <th>等级</th>
        <th>绩点</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="g" items="${list}" varStatus="st">
        <tr class="${g.failed ? 'table-danger' : ''}">
            <td>${st.count}</td>
            <td>${g.courseCode}</td>
            <td>${g.courseName}</td>
            <td>${g.credit}</td>
            <td>${g.score}</td>
            <td>${g.levelName}</td>
            <td>${g.gradePoint}</td>
        </tr>
    </c:forEach>
    <c:if test="${empty list}">
        <tr><td colspan="7" class="empty-tip">暂无成绩记录</td></tr>
    </c:if>
    </tbody>
</table>

<p class="text-secondary small">绩点换算：绩点 =（成绩 - 50）/ 10，低于 60 分记 0；GPA 为学分加权平均值。</p>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
