<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${empty pageTitle ? '成绩录入' : pageTitle}"/>
<c:set var="navActive" value="${empty navActive ? 'teacher:grade' : navActive}"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-3">${pageTitle}</h4>
<p class="text-secondary small">请先选择一门课程，再进入该课程的成绩单进行录入或修改。</p>

<div class="row g-3">
    <c:forEach var="c" items="${courses}">
        <div class="col-md-6">
            <div class="card shadow-sm h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <h6 class="mb-1">${c.courseName}</h6>
                            <div class="text-secondary small">
                                ${c.courseCode} ｜ ${c.credit} 学分 ｜ ${c.semester}
                            </div>
                            <div class="text-secondary small mt-1">已选人数：${c.selectedCount} / ${c.capacity}</div>
                        </div>
                        <span class="badge bg-primary">${c.status == 1 ? '进行中' : '已停开'}</span>
                    </div>
                    <div class="mt-3">
                        <a class="btn btn-sm btn-primary" href="${ctx}/teacher/grade?method=list&courseId=${c.id}">录入成绩</a>
                        <a class="btn btn-sm btn-outline-secondary ms-1" href="${ctx}/teacher/grade?method=stat&courseId=${c.id}">成绩统计</a>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
    <c:if test="${empty courses}">
        <div class="empty-tip">您当前没有授课安排，请联系管理员指派课程</div>
    </c:if>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
