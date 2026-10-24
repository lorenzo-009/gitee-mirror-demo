package com.lut.grade.dao.impl;

import com.lut.grade.dao.BaseDao;
import com.lut.grade.dao.PermissionDao;
import com.lut.grade.model.entity.Permission;

import java.util.List;

/** PermissionDao 的 JDBC 实现。 */
public class PermissionDaoImpl extends BaseDao implements PermissionDao {

    @Override
    public List<Permission> findMenusByRole(Integer role) {
        String sql = "SELECT p.id, p.perm_code, p.perm_name, p.url, p.parent_id, p.sort_no, p.icon "
                + "FROM permission p JOIN role_permission rp ON rp.perm_id = p.id "
                + "WHERE rp.role = ? AND p.parent_id = 0 ORDER BY p.sort_no";
        return queryList(Permission.class, sql, role);
    }

    @Override
    public List<String> findCodesByRole(Integer role) {
        String sql = "SELECT p.perm_code FROM permission p "
                + "JOIN role_permission rp ON rp.perm_id = p.id WHERE rp.role = ?";
        return queryScalarList(String.class, sql, role);
    }
}
