-- ============================================================
-- 初始化数据（在 schema.sql 之后执行）
-- 所有账号初始密码均为 123456，已按 MD5(md5明文 + 盐) 加密
-- 登录账号规则：学生 = 学号，教师 = 工号（T 开头），管理员 = admin
-- ============================================================

USE sms;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE role_permission;
TRUNCATE TABLE grade;
TRUNCATE TABLE course_selection;
TRUNCATE TABLE permission;
TRUNCATE TABLE course;
TRUNCATE TABLE student;
TRUNCATE TABLE teacher;
TRUNCATE TABLE user;
SET FOREIGN_KEY_CHECKS = 1;

-- ---------- 1. 用户（账号 / 加盐MD5密文 / 盐 / 角色） ----------
INSERT INTO user (username, password, salt, role, status) VALUES
  ('admin', '1235b03f75ebf58af69366145102a2e2', 'b6b54539c654bb70', 1, 1),
  ('T2020001', '06021c8783ce174ff9dfb067d3501a66', '064c0bf8e758b879', 2, 1),
  ('T2020002', 'd1f7e8d413f51b80f2e4a57bec78e253', '98cc781bf3d5f3bf', 2, 1),
  ('T2020003', '4cc0f41fed803f7677d23447fe5d9117', 'e2a71ee771d7e268', 2, 1),
  ('T2020004', '376588d077c0814d2bb520e6b49401c1', '2d95e1a8717c3fd7', 2, 1),
  ('T2020005', '928ee07c057653e8e3f3c0494d8d4197', '80afcdd7a407c212', 2, 1),
  ('T2020006', '1f6c6bd03f0cbdc84241ecc1d0ef98fd', '7e7d30fbaa4af810', 2, 1),
  ('T2020007', '8793caafcdd3aa1bfae932873c8f7ffa', '33d4d8f297d0d55a', 2, 1),
  ('T2020008', 'd4d23fb5b2b9ebbe2c4485ee545841d4', '2effca0152dd5e78', 2, 1),
  ('T2020009', '75a238916f0c724e3bac66e93ee5e898', '8f1283219bd2e6fa', 2, 1),
  ('T2020010', 'eae8dc8de79dd122fe7cd0682f7ce720', '90b134b4c364f325', 2, 1),
  ('250162701050', '225cfcc10f4de05dad5ed9a970ee8edc', 'ce989bb801c8e0f3', 3, 1),
  ('250162701051', '0ce04fc0ad48e35c50da4f09041e4d6b', 'b996c82e3513a38d', 3, 1),
  ('250162701052', 'cf0dea2900f999238f27cc5c19e90fc9', 'd4547d46c3ee42bb', 3, 1),
  ('250162701053', '1559e7b454783b02b2a67b5277c795b6', '0c881d6902134cc3', 3, 1),
  ('250162701054', 'bdb1072ff0b0fbbe2a71273514064d72', 'a1b9ac604783537d', 3, 1),
  ('250162701055', '80843cdd9effae646b967f7928903123', 'a7bee1d85df5e8c0', 3, 1),
  ('250162701056', 'aabbf12e205497a1770af2789c26332e', '01aa61c0b8c8ebd8', 3, 1),
  ('250162701057', '286089d608435b0e5716ea939ce72f5e', 'aff32c48917bffc1', 3, 1),
  ('250162701058', '55d174e41ab86a387ad87647544cdf19', 'a7e153c34114c3d6', 3, 1),
  ('250162701059', '9d00a1a2c0485429cae769ab7d45e241', 'e2b35342072678d7', 3, 1);

-- ---------- 2. 教师档案 ----------
INSERT INTO teacher (user_id, teacher_no, name, title, phone, email) VALUES
  ((SELECT id FROM user WHERE username='T2020001'), 'T2020001', '沈国栋', '教授',   '13800000001', 'shengd@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020002'), 'T2020002', '韩雪松', '副教授', '13800000002', 'hanxs@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020003'), 'T2020003', '罗文君', '讲师',   '13800000003', 'luowj@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020004'), 'T2020004', '徐立新', '教授',   '13800000004', 'xulx@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020005'), 'T2020005', '崔雅琴', '副教授', '13800000005', 'cuiyq@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020006'), 'T2020006', '傅振宇', '讲师',   '13800000006', 'fuzy@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020007'), 'T2020007', '邵天佑', '副教授', '13800000007', 'shaoty@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020008'), 'T2020008', '何静娴', '讲师',   '13800000008', 'hejx@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020009'), 'T2020009', '秦立群', '教授',   '13800000009', 'qinlq@lut.edu.cn'),
  ((SELECT id FROM user WHERE username='T2020010'), 'T2020010', '顾晓岚', '讲师',   '13800000010', 'guxl@lut.edu.cn');

-- ---------- 3. 学生档案（250162701050 为刘开宇本人） ----------
INSERT INTO student (user_id, student_no, name, gender, phone, email, enroll_date) VALUES
  ((SELECT id FROM user WHERE username='250162701050'), '250162701050', '刘开宇', 1, '18384997830', 'jjb@126.com', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701051'), '250162701051', '王浩然', 1, '13900000002', '250162701051@stu.lut.edu.cn', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701052'), '250162701052', '陈静怡', 2, '13900000003', '250162701052@stu.lut.edu.cn', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701053'), '250162701053', '赵宇航', 1, '13900000004', '250162701053@stu.lut.edu.cn', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701054'), '250162701054', '孙雨萱', 2, '13900000005', '250162701054@stu.lut.edu.cn', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701055'), '250162701055', '周子谦', 1, '13900000006', '250162701055@stu.lut.edu.cn', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701056'), '250162701056', '吴梦琪', 2, '13900000007', '250162701056@stu.lut.edu.cn', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701057'), '250162701057', '郑嘉禾', 1, '13900000008', '250162701057@stu.lut.edu.cn', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701058'), '250162701058', '马文轩', 1, '13900000009', '250162701058@stu.lut.edu.cn', '2025-09-01'),
  ((SELECT id FROM user WHERE username='250162701059'), '250162701059', '黄诗涵', 2, '13900000010', '250162701059@stu.lut.edu.cn', '2025-09-01');

-- ---------- 4. 课程（10 门，学期 2025-2026-1） ----------
INSERT INTO course (course_code, course_name, credit, teacher_id, semester, capacity, description, status) VALUES
  ('CS1001', 'JavaWeb编程技术', 3.0, (SELECT id FROM teacher WHERE teacher_no='T2020002'), '2025-2026-1', 50, 'Servlet、JSP、JDBC 与 MVC 分层实践', 1),
  ('CS1002', '数据库原理',      2.5, (SELECT id FROM teacher WHERE teacher_no='T2020001'), '2025-2026-1', 50, '关系模型、SQL 与数据库设计范式', 1),
  ('CS1003', '数据结构',        3.5, (SELECT id FROM teacher WHERE teacher_no='T2020003'), '2025-2026-1', 60, '线性表、树、图与常用排序查找算法', 1),
  ('CS1004', '操作系统',        3.5, (SELECT id FROM teacher WHERE teacher_no='T2020004'), '2025-2026-1', 60, '进程调度、内存管理与文件系统', 1),
  ('CS1005', '计算机网络',      3.0, (SELECT id FROM teacher WHERE teacher_no='T2020005'), '2025-2026-1', 60, 'TCP/IP 协议栈与网络编程基础', 1),
  ('CS1006', '软件工程',        3.0, (SELECT id FROM teacher WHERE teacher_no='T2020006'), '2025-2026-1', 60, '需求分析、概要设计与软件测试', 1),
  ('CS1007', '人工智能导论',    2.5, (SELECT id FROM teacher WHERE teacher_no='T2020007'), '2025-2026-1', 80, '机器学习与神经网络入门', 1),
  ('CS1008', '大数据技术',      3.0, (SELECT id FROM teacher WHERE teacher_no='T2020008'), '2025-2026-1', 60, 'Hadoop、Spark 与分布式计算', 1),
  ('CS1009', '高等数学',        4.0, (SELECT id FROM teacher WHERE teacher_no='T2020009'), '2025-2026-1', 120, '极限、微积分与级数', 1),
  ('CS1010', '大学英语',        3.0, (SELECT id FROM teacher WHERE teacher_no='T2020010'), '2025-2026-1', 120, '综合英语与学术写作', 1);

-- ---------- 5. 选课 ----------
-- 刘开宇：已选 7 门（计算机网络 / 人工智能导论 / 高等数学 未选，用于演示选课）
INSERT INTO course_selection (student_id, course_id, status)
SELECT (SELECT id FROM student WHERE student_no='250162701050'), c.id, 1
FROM course c
WHERE c.course_code IN ('CS1001','CS1002','CS1003','CS1004','CS1006','CS1008','CS1010');

-- 其余 9 名同学：选修全部 10 门，便于统计报表有数据
INSERT INTO course_selection (student_id, course_id, status)
SELECT st.id, c.id, 1
FROM student st CROSS JOIN course c
WHERE st.student_no <> '250162701050';

-- ---------- 6. 成绩 ----------
INSERT INTO grade (student_id, course_id, score, exam_type)
SELECT (SELECT id FROM student WHERE student_no='250162701050'), c.id,
       CASE c.course_code
         WHEN 'CS1001' THEN 88.0
         WHEN 'CS1002' THEN 91.5
         WHEN 'CS1003' THEN 76.0
         WHEN 'CS1004' THEN 84.0
         WHEN 'CS1006' THEN 93.0
         WHEN 'CS1008' THEN 79.5
         WHEN 'CS1010' THEN 86.0
       END,
       '期末'
FROM course c
WHERE c.course_code IN ('CS1001','CS1002','CS1003','CS1004','CS1006','CS1008','CS1010');

-- 其他同学：按公式生成 55~99 分，覆盖各分数段；个别留空用于演示"未录入"
INSERT INTO grade (student_id, course_id, score, exam_type)
SELECT st.id, c.id,
       CASE WHEN MOD(st.id + c.id, 17) = 0 THEN NULL
            ELSE 55 + MOD(st.id * 7 + c.id * 13, 45) END,
       '期末'
FROM student st CROSS JOIN course c
WHERE st.student_no <> '250162701050';

-- ---------- 7. 权限菜单 ----------
INSERT INTO permission (perm_code, perm_name, url, parent_id, sort_no, icon) VALUES
  ('admin:dashboard', '系统概览',   '/admin/dashboard', 0, 1, 'speedometer'),
  ('admin:student',   '学生管理',   '/admin/student',   0, 2, 'people'),
  ('admin:teacher',   '教师管理',   '/admin/teacher',   0, 3, 'person-badge'),
  ('admin:course',    '课程管理',   '/admin/course',    0, 4, 'book'),
  ('teacher:course',  '我的课程',   '/teacher/course',  0, 1, 'collection'),
  ('teacher:grade',   '成绩录入',   '/teacher/grade',   0, 2, 'pencil-square'),
  ('teacher:import',  '批量导入',   '/teacher/grade/import', 0, 3, 'upload'),
  ('teacher:stat',    '成绩统计',   '/teacher/stat',    0, 4, 'bar-chart'),
  ('student:selection','我的选课',  '/student/selection', 0, 1, 'cart'),
  ('student:grade',   '我的成绩',   '/student/grade',   0, 2, 'card-list'),
  ('student:stat',    '成绩统计',   '/student/stat',    0, 3, 'graph-up'),
  ('profile',         '个人中心',   '/profile',         0, 9, 'person-circle');

INSERT INTO role_permission (role, perm_id)
SELECT 1, id FROM permission WHERE perm_code IN
  ('admin:dashboard','admin:student','admin:teacher','admin:course','profile');

INSERT INTO role_permission (role, perm_id)
SELECT 2, id FROM permission WHERE perm_code IN
  ('teacher:course','teacher:grade','teacher:import','teacher:stat','profile');

INSERT INTO role_permission (role, perm_id)
SELECT 3, id FROM permission WHERE perm_code IN
  ('student:selection','student:grade','student:stat','profile');

-- 完成提示
SELECT CONCAT('初始化完成：用户 ', (SELECT COUNT(*) FROM user),
              ' / 教师 ', (SELECT COUNT(*) FROM teacher),
              ' / 学生 ', (SELECT COUNT(*) FROM student),
              ' / 课程 ', (SELECT COUNT(*) FROM course),
              ' / 选课 ', (SELECT COUNT(*) FROM course_selection),
              ' / 成绩 ', (SELECT COUNT(*) FROM grade)) AS result;
