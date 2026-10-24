<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="个人中心"/>
<c:set var="navActive" value="profile"/>
<%@ include file="/WEB-INF/views/common/header.jspf" %>

<h4 class="mb-4">个人中心</h4>

<div class="row">
    <div class="col-md-4">
        <div class="card shadow-sm">
            <div class="card-header bg-white fw-semibold">我的头像</div>
            <div class="card-body text-center">
                <img src="${ctx}/${loginUser.avatarPath}" alt="头像"
                     class="rounded-circle mb-3 border"
                     style="width:120px;height:120px;object-fit:cover">
                <form method="post" action="${ctx}/profile" enctype="multipart/form-data">
                    <input type="hidden" name="method" value="uploadAvatar">
                    <input type="file" class="form-control form-control-sm mb-2" name="avatar"
                           accept="image/*" required>
                    <button type="submit" class="btn btn-primary btn-sm w-100">上传头像</button>
                </form>
                <div class="text-secondary small mt-2">支持 jpg / png / gif，大小不超过 2MB</div>
            </div>
        </div>
    </div>

    <div class="col-md-8">
        <div class="card shadow-sm">
            <div class="card-header bg-white fw-semibold">基本信息</div>
            <div class="card-body">
                <div class="row mb-2">
                    <div class="col-4 text-secondary">登录账号</div>
                    <div class="col-8">${loginUser.username}</div>
                </div>
                <div class="row mb-2">
                    <div class="col-4 text-secondary">真实姓名</div>
                    <div class="col-8">${empty loginUser.realName ? '—' : loginUser.realName}</div>
                </div>
                <div class="row mb-2">
                    <div class="col-4 text-secondary">所属角色</div>
                    <div class="col-8">${loginUser.roleName}</div>
                </div>
            </div>
        </div>

        <div class="card shadow-sm mt-3">
            <div class="card-header bg-white fw-semibold">修改密码</div>
            <div class="card-body">
                <form method="post" action="${ctx}/profile">
                    <input type="hidden" name="method" value="changePassword">
                    <div class="mb-3">
                        <label class="form-label">原密码</label>
                        <input type="password" class="form-control" name="oldPassword" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">新密码</label>
                        <input type="password" class="form-control" name="newPassword"
                               placeholder="至少 6 位" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">确认新密码</label>
                        <input type="password" class="form-control" name="confirmPassword" required>
                    </div>
                    <button type="submit" class="btn btn-primary">确认修改</button>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/common/footer.jspf" %>
