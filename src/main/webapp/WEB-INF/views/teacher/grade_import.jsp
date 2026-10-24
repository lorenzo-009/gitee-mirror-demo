<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="批量导入"/>
<c:set var="navActive" value="teacher:import"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-3">批量导入成绩</h4>

<div class="card shadow-sm mb-4" style="max-width:720px">
    <div class="card-header bg-white fw-semibold">导入说明</div>
    <div class="card-body">
        <ol class="mb-0 small">
            <li>先导出该课程成绩单作为模板，Excel 首行必须是表头。</li>
            <li>列顺序固定为：<code>学号 | 姓名 | 成绩 | 考试类型</code>（姓名与考试类型可留空）。</li>
            <li>学号必须在<b>该课程的花名册</b>中存在，否则整批导入会失败。</li>
            <li>成绩须为 0 ~ 100 的数字；单次最多 <code>500</code> 行。</li>
            <li>导入是事务性的：任何一行出错，整批都不会写入。</li>
        </ol>
    </div>
</div>

<form method="post" action="${ctx}/teacher/grade/import" enctype="multipart/form-data"
      class="bg-white p-4 border rounded" style="max-width:720px">
    <input type="hidden" name="method" value="upload">

    <div class="mb-3">
        <label class="form-label">所属课程</label>
        <select class="form-select" name="courseId" required>
            <c:forEach var="c" items="${courses}">
                <option value="${c.id}">${c.courseName}（${c.courseCode}）</option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label class="form-label">选择 Excel 文件</label>
        <input type="file" class="form-control" name="file" accept=".xls,.xlsx" required>
    </div>

    <button type="submit" class="btn btn-primary">开始导入</button>
    <a class="btn btn-outline-secondary ms-2"
       href="${ctx}/teacher/grade/import?method=execute">重置</a>
</form>

<div class="mt-3">
    <a class="btn btn-sm btn-outline-secondary" href="${ctx}/teacher/grade">返回成绩录入</a>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
