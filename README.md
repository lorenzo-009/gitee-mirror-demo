# 2026Javaweb · 学生成绩管理系统

《JavaWeb 编程技术》期末测评作业 · 兰州理工大学计算机与人工智能学院 · 指导教师：任旭鹏

技术栈：Java 17 + Servlet 4.0 + JSP（JSTL/EL）+ JDBC（Druid 连接池）+ MySQL 8 + Tomcat 9 + Bootstrap 5（本地静态资源，无需联网）

> 注意：必须使用 **Tomcat 9**。Tomcat 10 及以上版本的包名已改为 `jakarta.servlet`，与本项目使用的 `javax.servlet` 不兼容。

## 一、启动前两步

### 1. 建库与初始化数据

在 MySQL 中依次执行：

```
sql/schema.sql        -- 建 sms 库 + 8 张表（含外键与唯一索引）
sql/init-data.sql     -- 初始化账号、教师、学生、课程、选课与成绩数据
```

导入后共 21 个账号、10 名教师、10 名学生、10 门课程、97 条选课记录、97 条成绩记录。

### 2. 修改数据库连接

`src/main/resources/db.properties`

```
url=jdbc:mysql://127.0.0.1:3306/sms?...
username=root
password=你的实际密码        ← 只需改这一行
```

## 二、运行

在 IDEA 中配置 Tomcat 9：

- Deployment 挂载 `2026Javaweb:war exploded`
- Application context 填 `/2026Javaweb`
- 若页面偶发中文乱码，在 Tomcat `conf/server.xml` 的 Connector 上补 `URIEncoding="UTF-8"`

访问：`http://localhost:8080/2026Javaweb/login`

## 三、演示账号（密码统一 `123456`）

| 角色 | 账号 | 说明 |
|---|---|---|
| 管理员 | `admin` | 系统概览、学生 / 教师 / 课程管理 |
| 教师 | `T2020002` | 韩雪松，讲授 JavaWeb 编程技术 |
| 学生 | `250162701050` | 刘开宇，软件工程 1 班 |

教师账号为 `T2020001` ~ `T2020010`，学生账号为 `250162701050` ~ `250162701059`（账号即工号 / 学号）。

## 四、目录说明

```
src/main/java/com/lut/grade/
  controller/            Servlet 控制层
    base/BaseServlet       按 method 参数反射分发，统一业务异常处理
    admin/                 管理员端：概览、学生、教师、课程
    teacher/               教师端：成绩录入、导入、导出、统计
    student/               学生端：选课、成绩、统计
  service/               业务层，事务边界在这一层
    impl/                  各业务实现
  dao/                   JDBC 数据访问层，只写 SQL
    BaseDao                结果集到实体的反射映射
  model/
    entity/                与数据表对应的实体
    vo/                    统计与展示用的视图对象
  filter/                EncodingFilter（编码） + LoginFilter（登录与 RBAC 鉴权）
  util/                  DBUtil / MD5Util / ExcelUtil / UploadUtil / Page

src/main/webapp/
  WEB-INF/views/         JSP 页面全部放在这里，浏览器无法直连（只能经 Servlet 转发）
  WEB-INF/web.xml        过滤器与错误页配置
  static/                Bootstrap 与自定义样式、默认头像

sql/                     建表脚本与初始化数据脚本
```

## 五、功能一览

- 管理员：系统概览、学生信息增删改查（分页 + 模糊检索）、教师信息维护、课程信息维护
- 教师：我的课程、按课程录入与修改成绩、Excel 批量导入（幂等 upsert + 整批事务）、导出成绩单、单科成绩统计（平均分 / 最高 / 最低 / 及格率 / 五档分布）
- 学生：选课与退选（校验重复选课与容量）、我的成绩（平均分 / 已修学分 / 平均绩点）、成绩统计
- 公共：登录、退出、修改密码、头像上传
- 权限：基于角色的访问控制，服务端过滤器按 URL 前缀校验权限码，菜单由数据库动态生成
