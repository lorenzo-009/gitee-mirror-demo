-- ============================================================
-- 学生成绩管理系统 · 数据库结构
-- MySQL 8.0 / utf8mb4
-- 先执行本文件建表，再执行 init-data.sql 灌入测试数据
-- ============================================================

CREATE DATABASE IF NOT EXISTS sms
  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE sms;

-- 1. 用户表（登录身份与角色）
CREATE TABLE IF NOT EXISTS user (
  id          BIGINT       PRIMARY KEY AUTO_INCREMENT,
  username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '登录账号',
  password    CHAR(32)     NOT NULL COMMENT 'MD5(明文+盐)',
  salt        CHAR(16)     NOT NULL COMMENT '随机盐值',
  role        TINYINT      NOT NULL COMMENT '1管理员 2教师 3学生',
  avatar      VARCHAR(200) DEFAULT NULL COMMENT '头像文件名',
  status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1启用 0停用',
  create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='用户表';

-- 2. 学生表
CREATE TABLE IF NOT EXISTS student (
  id          BIGINT      PRIMARY KEY AUTO_INCREMENT,
  user_id     BIGINT      NOT NULL UNIQUE COMMENT '关联 user.id',
  student_no  VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
  name        VARCHAR(50) NOT NULL,
  gender      TINYINT     NOT NULL DEFAULT 0 COMMENT '0未知 1男 2女',
  phone       VARCHAR(20),
  email       VARCHAR(100),
  enroll_date DATE,
  CONSTRAINT fk_stu_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='学生表';

-- 3. 教师表
CREATE TABLE IF NOT EXISTS teacher (
  id         BIGINT      PRIMARY KEY AUTO_INCREMENT,
  user_id    BIGINT      NOT NULL UNIQUE COMMENT '关联 user.id',
  teacher_no VARCHAR(20) NOT NULL UNIQUE COMMENT '工号',
  name       VARCHAR(50) NOT NULL,
  title      VARCHAR(30) COMMENT '职称',
  phone      VARCHAR(20),
  email      VARCHAR(100),
  CONSTRAINT fk_tea_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='教师表';

-- 4. 课程表
CREATE TABLE IF NOT EXISTS course (
  id          BIGINT       PRIMARY KEY AUTO_INCREMENT,
  course_code VARCHAR(20)  NOT NULL UNIQUE COMMENT '课程编号',
  course_name VARCHAR(100) NOT NULL,
  credit      DECIMAL(3,1) NOT NULL DEFAULT 2.0 COMMENT '学分',
  teacher_id  BIGINT       COMMENT '任课教师，对应 teacher.id',
  semester    VARCHAR(20)  COMMENT '如 2026-2027-1',
  capacity    INT          NOT NULL DEFAULT 60 COMMENT '选课容量上限',
  description VARCHAR(500),
  status      TINYINT      NOT NULL DEFAULT 1 COMMENT '1可选 0停开',
  CONSTRAINT fk_course_tea FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE SET NULL
) ENGINE=InnoDB COMMENT='课程表';

-- 5. 选课表
CREATE TABLE IF NOT EXISTS course_selection (
  id          BIGINT   PRIMARY KEY AUTO_INCREMENT,
  student_id  BIGINT   NOT NULL,
  course_id   BIGINT   NOT NULL,
  select_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  status      TINYINT  NOT NULL DEFAULT 1 COMMENT '1已选 0已退',
  UNIQUE KEY uk_stu_course (student_id, course_id),
  CONSTRAINT fk_sel_stu FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
  CONSTRAINT fk_sel_cou FOREIGN KEY (course_id)  REFERENCES course(id)  ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='选课表';

-- 6. 成绩表
CREATE TABLE IF NOT EXISTS grade (
  id          BIGINT       PRIMARY KEY AUTO_INCREMENT,
  student_id  BIGINT       NOT NULL,
  course_id   BIGINT       NOT NULL,
  score       DECIMAL(5,2) DEFAULT NULL COMMENT '百分制成绩，NULL 表示未录入',
  exam_type   VARCHAR(20)  NOT NULL DEFAULT '期末',
  remark      VARCHAR(200),
  update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_grade_sc (student_id, course_id),
  CONSTRAINT fk_grade_stu FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE,
  CONSTRAINT fk_grade_cou FOREIGN KEY (course_id)  REFERENCES course(id)  ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='成绩表';

-- 7. 权限 / 菜单表
CREATE TABLE IF NOT EXISTS permission (
  id        BIGINT      PRIMARY KEY AUTO_INCREMENT,
  perm_code VARCHAR(50) NOT NULL UNIQUE COMMENT '权限码，如 admin:student:list',
  perm_name VARCHAR(50) NOT NULL COMMENT '菜单显示名',
  url       VARCHAR(200) COMMENT '受保护 URL 前缀',
  parent_id BIGINT      NOT NULL DEFAULT 0,
  sort_no   INT         NOT NULL DEFAULT 0,
  icon      VARCHAR(50)
) ENGINE=InnoDB COMMENT='权限表兼菜单表';

-- 8. 角色权限关联表
CREATE TABLE IF NOT EXISTS role_permission (
  id      BIGINT  PRIMARY KEY AUTO_INCREMENT,
  role    TINYINT NOT NULL COMMENT '1管理员 2教师 3学生',
  perm_id BIGINT  NOT NULL,
  UNIQUE KEY uk_role_perm (role, perm_id),
  CONSTRAINT fk_rp_perm FOREIGN KEY (perm_id) REFERENCES permission(id) ON DELETE CASCADE
) ENGINE=InnoDB COMMENT='角色权限关联表';
