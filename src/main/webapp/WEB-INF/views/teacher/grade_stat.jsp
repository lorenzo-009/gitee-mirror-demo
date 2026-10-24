<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="成绩统计"/>
<c:set var="navActive" value="teacher:stat"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-1">${stat.courseName} <span class="text-secondary fs-6">（${stat.courseCode}）</span></h4>
<p class="text-secondary small mb-4">任课教师：${stat.teacherName}</p>

<div class="row g-3 mb-4">
    <div class="col-md-3">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">选课人数</div>
            <div class="fs-4 fw-semibold">${stat.total}</div>
        </div></div>
    </div>
    <div class="col-md-3">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">平均分</div>
            <div class="fs-4 fw-semibold text-primary">${stat.avg}</div>
        </div></div>
    </div>
    <div class="col-md-3">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">最高分 / 最低分</div>
            <div class="fs-5 fw-semibold">${stat.max} / ${stat.min}</div>
        </div></div>
    </div>
    <div class="col-md-3">
        <div class="card text-center shadow-sm"><div class="card-body">
            <div class="text-secondary small">及格率</div>
            <div class="fs-4 fw-semibold text-success">${stat.passRate}%</div>
        </div></div>
    </div>
</div>

<div class="card shadow-sm">
    <div class="card-header bg-white fw-semibold">分数段分布</div>
    <div class="card-body">
        <c:forEach var="seg" items="${stat.segments}">
            <div class="mb-3">
                <div class="d-flex justify-content-between small mb-1">
                    <span>${seg.name}（${seg.range} 分）</span>
                    <span>${seg.count} 人 ｜ ${seg.percent}%</span>
                </div>
                <div class="progress" style="height:16px">
                    <div class="progress-bar bg-${seg.color}" role="progressbar"
                         style="width:${seg.percent}%" aria-valuenow="${seg.percent}" aria-valuemin="0" aria-valuemax="100">
                    </div>
                </div>
            </div>
        </c:forEach>

        <table class="table table-bordered mt-4 mb-0">
            <thead class="table-light">
            <tr><th>等级</th><th>人数</th><th>占比</th></tr>
            </thead>
            <tbody>
            <c:forEach var="seg" items="${stat.segments}">
                <tr>
                    <td>${seg.name}</td>
                    <td>${seg.count}</td>
                    <td>${seg.percent}%</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="mt-3">
    <a class="btn btn-outline-secondary" href="${ctx}/teacher/grade?method=list&courseId=${stat.courseId}">返回成绩单</a>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
