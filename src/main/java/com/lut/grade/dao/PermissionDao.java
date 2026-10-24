package com.lut.grade.dao;

import com.lut.grade.model.entity.Permission;

import java.util.List;

/** 权限 / 菜单数据访问接口。 */
public interface PermissionDao {

    /** 查询某角色的菜单列表，按 sort_no 排序。 */
    List<Permission> findMenusByRole(Integer role);

    /** 查询某角色的权限码集合。 */
    List<String> findCodesByRole(Integer role);
}
