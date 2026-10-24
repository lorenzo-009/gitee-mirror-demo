package com.lut.grade.dao;

import com.lut.grade.util.DBUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC 通用基类。
 * 子类只负责写 SQL 与传参，反射封装结果集在这里统一完成。
 *
 * 结果映射规则：列名下划线转驼峰后匹配实体字段（student_no -> studentNo）；
 * 实体中不存在的列（如关联查询出来的冗余字段）自动忽略。
 */
public abstract class BaseDao {

    protected <T> List<T> queryList(Class<T> clazz, String sql, Object... params) {
        List<T> result = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DBUtil.getConnection();
            st = conn.prepareStatement(sql);
            setParams(st, params);
            rs = st.executeQuery();
            while (rs.next()) {
                result.add(mapRow(clazz, rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("查询失败：" + e.getMessage(), e);
        } finally {
            DBUtil.close(rs, st);
        }
        return result;
    }

    protected <T> T queryOne(Class<T> clazz, String sql, Object... params) {
        List<T> list = queryList(clazz, sql, params);
        return list.isEmpty() ? null : list.get(0);
    }

    protected int update(String sql, Object... params) {
        PreparedStatement st = null;
        try {
            Connection conn = DBUtil.getConnection();
            st = conn.prepareStatement(sql);
            setParams(st, params);
            return st.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("更新失败：" + e.getMessage(), e);
        } finally {
            DBUtil.close(st);
        }
    }

    /** 插入并返回自增主键。 */
    protected Long insertReturnKey(String sql, Object... params) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DBUtil.getConnection();
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParams(st, params);
            st.executeUpdate();
            rs = st.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("插入失败：" + e.getMessage(), e);
        } finally {
            DBUtil.close(rs, st);
        }
    }

    /**
     * 查询单个标量值，如 COUNT/MAX/AVG 或单个字段。
     * 支持 Long / Integer / String / BigDecimal 等简单类型，无结果时返回 null。
     */
    protected <T> T queryScalar(Class<T> clazz, String sql, Object... params) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DBUtil.getConnection();
            st = conn.prepareStatement(sql);
            setParams(st, params);
            rs = st.executeQuery();
            if (rs.next()) {
                return clazz.cast(convert(rs.getObject(1), clazz));
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("查询失败：" + e.getMessage(), e);
        } finally {
            DBUtil.close(rs, st);
        }
    }

    /** 查询单列标量列表，如权限码、姓名列表等。 */
    protected <T> List<T> queryScalarList(Class<T> clazz, String sql, Object... params) {
        List<T> result = new ArrayList<>();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DBUtil.getConnection();
            st = conn.prepareStatement(sql);
            setParams(st, params);
            rs = st.executeQuery();
            while (rs.next()) {
                result.add(clazz.cast(convert(rs.getObject(1), clazz)));
            }
        } catch (Exception e) {
            throw new RuntimeException("查询失败：" + e.getMessage(), e);
        } finally {
            DBUtil.close(rs, st);
        }
        return result;
    }

    protected int count(String sql, Object... params) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            Connection conn = DBUtil.getConnection();
            st = conn.prepareStatement(sql);
            setParams(st, params);
            rs = st.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException("统计失败：" + e.getMessage(), e);
        } finally {
            DBUtil.close(rs, st);
        }
    }

    private void setParams(PreparedStatement st, Object... params) throws Exception {
        if (params == null) {
            return;
        }
        for (int i = 0; i < params.length; i++) {
            st.setObject(i + 1, params[i]);
        }
    }

    private <T> T mapRow(Class<T> clazz, ResultSet rs) throws Exception {
        T obj = clazz.getDeclaredConstructor().newInstance();
        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String columnLabel = meta.getColumnLabel(i);
            Field field = findField(clazz, columnLabel);
            if (field == null) {
                continue;
            }
            Object value = convert(rs.getObject(i), field.getType());
            field.setAccessible(true);
            field.set(obj, value);
        }
        return obj;
    }

    private Field findField(Class<?> clazz, String columnLabel) {
        String camel = toCamel(columnLabel);
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(camel);
            } catch (NoSuchFieldException ignored) {
                try {
                    return current.getDeclaredField(columnLabel);
                } catch (NoSuchFieldException e2) {
                    current = current.getSuperclass();
                }
            }
        }
        return null;
    }

    private Object convert(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }
        if (targetType == LocalDate.class) {
            if (value instanceof java.sql.Date) {
                return ((java.sql.Date) value).toLocalDate();
            }
            if (value instanceof java.sql.Timestamp) {
                return ((java.sql.Timestamp) value).toLocalDateTime().toLocalDate();
            }
        }
        if (targetType == LocalDateTime.class && value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime();
        }
        if (targetType == String.class) {
            return value.toString();
        }
        if (Number.class.isAssignableFrom(targetType)) {
            if (value instanceof Number) {
                Number num = (Number) value;
                if (targetType == Integer.class) {
                    return num.intValue();
                }
                if (targetType == Long.class) {
                    return num.longValue();
                }
                if (targetType == Double.class) {
                    return num.doubleValue();
                }
                if (targetType == BigDecimal.class) {
                    return new BigDecimal(num.toString());
                }
            }
        }
        return value;
    }

    private String toCamel(String name) {
        StringBuilder sb = new StringBuilder();
        boolean upperNext = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '_') {
                upperNext = true;
            } else if (upperNext) {
                sb.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
