<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="成绩统计"/>
<c:set var="navActive" value="student:stat"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-3">成绩统计</h4>

<div class="row g-3 mb-4">
    <div class="col-md-4">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">平均分</div>
            <div class="display-6 text-primary">${avg}</div>
        </div></div>
    </div>
    <div class="col-md-4">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">已修学分</div>
            <div class="display-6 text-success">${credit}</div>
        </div></div>
    </div>
    <div class="col-md-4">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">平均绩点 GPA</div>
            <div class="display-6 text-warning">${gpa}</div>
        </div></div>
    </div>
</div>

<div class="card shadow-sm">
    <div class="card-header bg-white fw-semibold">各课程明细</div>
    <div class="card-body p-0">
        <table class="table mb-0">
            <thead class="table-light">
            <tr>
                <th>课程名称</th>
                <th style="width:90px">学分</th>
                <th style="width:90px">成绩</th>
                <th style="width:90px">等级</th>
                <th style="width:90px">绩点</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="g" items="${list}">
                <tr class="${g.failed ? 'table-danger' : ''}">
                    <td>${g.courseName}</td>
                    <td>${g.credit}</td>
                    <td>${g.score}</td>
                    <td>${g.levelName}</td>
                    <td>${g.gradePoint}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty list}">
                <tr><td colspan="5" class="empty-tip">暂无成绩记录</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<p class="text-secondary small mt-3">
    说明：已修学分只统计成绩及格（≥ 60）的课程；GPA 按学分加权，用于奖学金与评优参考。
</p>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
