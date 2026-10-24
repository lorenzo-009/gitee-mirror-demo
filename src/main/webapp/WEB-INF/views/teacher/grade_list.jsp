<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="成绩录入"/>
<c:set var="navActive" value="teacher:grade"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<div class="d-flex justify-content-between align-items-center mb-3">
    <h4 class="mb-0">
        ${course.courseName}
        <span class="text-secondary fs-6">（${course.courseCode} ｜ ${course.credit} 学分）</span>
    </h4>
    <a class="btn btn-outline-secondary btn-sm" href="${ctx}/teacher/grade">返回课程列表</a>
</div>

<form class="row g-2 mb-3" method="get" action="${ctx}/teacher/grade">
    <input type="hidden" name="method" value="list">
    <input type="hidden" name="courseId" value="${course.id}">
    <div class="col-auto">
        <input type="text" class="form-control" name="keyword" value="${keyword}" placeholder="学号或姓名">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-outline-primary">查询</button>
    </div>
    <div class="col-auto align-self-center">
        <a class="btn btn-outline-secondary" href="${ctx}/teacher/grade?method=list&courseId=${course.id}">重置</a>
    </div>
    <div class="col-auto ms-auto align-self-center text-secondary">共 ${list.size()} 名学生</div>
</form>

<table class="table table-bordered table-hover bg-white">
    <thead class="table-light">
    <tr>
        <th style="width:60px">序号</th>
        <th>学号</th>
        <th>姓名</th>
        <th style="width:120px">成绩</th>
        <th style="width:120px">考试类型</th>
        <th>等级</th>
        <th>绩点</th>
        <th style="width:150px">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="g" items="${list}" varStatus="st">
        <tr class="${g.failed ? 'table-danger' : ''}">
            <td>${st.count}</td>
            <td>${g.studentNo}</td>
            <td>${g.studentName}</td>
            <td>
                <c:choose>
                    <c:when test="${g.score != null}">${g.score}</c:when>
                    <c:otherwise><span class="text-secondary">未录入</span></c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${g.examType != null}">${g.examType}</c:when>
                    <c:otherwise>-</c:otherwise>
                </c:choose>
            </td>
            <td>${g.levelName}</td>
            <td>${g.score != null ? g.gradePoint : '-'}</td>
            <td>
                <form method="post" action="${ctx}/teacher/grade" class="d-flex gap-1">
                    <input type="hidden" name="method" value="save">
                    <input type="hidden" name="courseId" value="${course.id}">
                    <input type="hidden" name="studentId" value="${g.studentId}">
                    <input type="text" class="form-control form-control-sm" name="score"
                           style="width:66px" value="${g.score}" placeholder="分数">
                    <button type="submit" class="btn btn-sm btn-primary">保存</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty list}">
        <tr><td colspan="8" class="empty-tip">该课程暂无选课学生</td></tr>
    </c:if>
    </tbody>
</table>

<p class="text-secondary small">提示：留空保存会提示错误；成绩支持 0 ~ 100，重复保存会覆盖原成绩。</p>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
