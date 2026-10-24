package com.lut.grade.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 数据库连接工具（Druid 连接池 + ThreadLocal 单连接）。
 *
 * 约定：
 * 1. 一个请求线程只持有一条 Connection，多次 getConnection() 拿到的是同一个对象；
 * 2. DAO 方法内部只关闭 ResultSet / Statement，不关 Connection；
 * 3. Service 方法负责事务边界：try { ... } catch { rollback } finally { closeConnection() }。
 */
public final class DBUtil {

    private static final DruidDataSource DATA_SOURCE;
    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<>();

    static {
        Properties props = new Properties();
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("未找到 classpath 下的 db.properties");
            }
            props.load(in);
            DATA_SOURCE = (DruidDataSource) DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("数据库连接池初始化失败：" + e.getMessage());
        }
    }

    private DBUtil() {
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }

    /** 获取当前线程绑定的连接（不存在则从池中取出并绑定）。 */
    public static Connection getConnection() throws SQLException {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null || conn.isClosed()) {
            conn = DATA_SOURCE.getConnection();
            CONNECTION_HOLDER.set(conn);
        }
        return conn;
    }

    /** 开启事务：关闭自动提交。 */
    public static void beginTransaction() throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            conn.commit();
        }
    }

    public static void rollback() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ignored) {
            }
        }
    }

    /** 归还连接在 Service 的 finally 中统一调用。 */
    public static void closeConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn != null) {
            try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ignored) {
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    public static void close(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public static void close(ResultSet rs, Statement st) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }
        }
        close(st);
    }
}
