use dosth;

DELETE FROM account_role;
DELETE FROM users;
DELETE FROM relation;
DELETE FROM roles;
DELETE FROM dept;
DELETE FROM account;
DELETE FROM menu;
DELETE FROM system_info;
DELETE FROM dict;
DELETE FROM notice;
DELETE FROM operation_log;
DELETE FROM login_log;

INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('1', null, '', '总公司', '总公司', '总公司', 1, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('2', '1', '[1],', '开发部', '开发部', '开发部', 2, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('3', '1', '[1],', '运营部', '运营部', '运营部', 3, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('4', '1', '[1],', '财务部', '财务部', '财务部', 4, 'OK');

INSERT INTO system_info(id, system_name, url) values(1, '系统管理', 'http://127.0.0.1:8081');

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('101', '1', 'system', null, null, '系统管理', 1, 1, '', 'fa-user', 1, 1, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('102', '1', 'deptMgr', 'system', '[system],', '部门管理', 1, 2, '/mgrDept', '', 1, 0, 1, null );
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('103', '1', 'userMgr', 'system', '[system],', '用户管理', 1, 2, '', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('104', '1', 'mgrAccount', 'userMgr', '[system],[userMgr],', '账户信息', 1, 3, '/mgrAccount', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('105', '1', 'mgrAccountPage', 'mgrAccount', '[system],[userMgr],[mgrAccount],', '账户查询', 1, 4, '/mgrAccount/list', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('106', '1', 'mgrAccountAdd', 'mgrAccount', '[system],[userMgr],[mgrAccount],', '添加账户', 1, 4, '/mgrAccount/add', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('107', '1', 'mgrAccountMod', 'mgrAccount', '[system],[userMgr],[mgrAccount],', '修改账户', 1, 4, '/mgrAccount/edit', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('108', '1', 'mgrAccountDel', 'mgrAccount', '[system],[userMgr],[mgrAccount],', '删除账户', 1, 4, '/mgrAccount/delete', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('109', '1', 'mgrAccountReset', 'mgrAccount', '[system],[userMgr],[mgrAccount],', '重置账户密码', 1, 4, '/mgrAccount/reset', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('110', '1', 'mgrAccountFreeze', 'mgrAccount', '[system],[userMgr],[mgrAccount],', '冻结账户', 1, 4, '/mgrAccount/freeze', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('111', '1', 'mgrAccountUnfreeze', 'mgrAccount', '[system],[userMgr],[mgrAccount],', '解冻账户', 1, 4, '/mgrAccount/unfreeze', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('112', '1', 'mgrUser', 'userMgr', '[system],[userMgr],', '用户信息', 1, 3, '/mgrUser', 'fa-train', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('113', '1', 'mgrUserPage', 'mgrUser', '[system],[userMgr],[mgrUser],', '用户查询', 1, 4, '/mgrUser/list', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('114', '1', 'mgrUserAdd', 'mgrUser', '[system],[userMgr],[mgrUser],', '添加用户', 1, 4, '/mgrUser/add', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('115', '1', 'mgrUserMod', 'mgrUser', '[system],[userMgr],[mgrUser],', '修改用户', 1, 4, '/mgrUser/edit', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('116', '1', 'mgrUserDel', 'mgrUser', '[system],[userMgr],[mgrUser],', '删除用户', 1, 4, '/mgrUser/delete', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('117', '1', 'mgrUserBindIC', 'userMgr', '[system],[userMgr],', '用户IC卡信息', 1, 3, '/mgrUserBindIC', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('118', '1', 'popedomMgr', 'system', '[system],', '权限管理', 1, 2, '', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('119', '1', 'mgrRole', 'popedomMgr', '[system],[popedomMgr],', '角色信息', 1, 3, '/mgrRole', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('120', '1', 'mgrRoleAdd', 'mgrRole', '[system],[popedomMgr],[mgrRole],', '添加角色', 1, 4, '/mgrRole/add', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('121', '1', 'mgrRoleMod', 'mgrRole', '[system],[popedomMgr],[mgrRole],', '修改角色', 1, 4, '/mgrRole/edit', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('122', '1', 'mgrRoleDel', 'mgrRole', '[system],[popedomMgr],[mgrRole],', '删除角色', 1, 4, '/mgrRole/delete', 'fa-train', 0, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('123', '1', 'mgrAcRole', 'popedomMgr', '[system],[popedomMgr],', '角色分配', 1, 3, '/mgrAcRole', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('124', '1', 'logMgr', 'system', '[system],', '日志管理', 1, 2, '/logMgr', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('125', '1', 'loginLog', 'logMgr', '[system],[logMgr],', '登录日志', 1, 3, '/loginLog', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('126', '1', 'operationLog', 'logMgr', '[system],[logMgr],', '操作日志', 1, 3, '/operationLog', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('127', '1', 'systemInfo', 'system', '[system],', '系统维护', 1, 2, '/systemInfo', '', 1, 0, 1, null);

INSERT INTO roles(id, p_id, dept_id, name, num, tips, version) values('101', '101', '1', '超级管理员', 1, 'administrator', '1');
INSERT INTO roles(id, p_id, dept_id, name, num, tips, version) values('102', '102', '1', '管理员', 2, 'admin', '1');

INSERT INTO relation (id, menu_id, role_id) values ('101', '101', '101');	

INSERT INTO account (id, login_name, password, face_pwd, salt, status, version) values ('1', 'admin', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', 'sDiaLyeh+RwocMiweDJcGA==', '3b9j6', 'OK', null);
INSERT INTO account (id, login_name, password, face_pwd, salt, status, version) values ('2', 'ZS', '6a50849eba127f36b63eb7597fc2cb24', 'cCZMc7dhRL6D1N6LGmhEWQ==', '0yed6', 'OK', null);
INSERT INTO account (id, login_name, password, face_pwd, salt, status, version) values ('3', 'LS', '35de46f291b424c3d1f10c7f733afaeb', 'Ku8mX20b61k6Zkw8doSrVQ==', 'xjepm', 'OK', null);

INSERT INTO account_role(id, account_id, role_id) values ('1', '1', '101');
INSERT INTO account_role(id, account_id, role_id) values ('2', '2', '101');
INSERT INTO account_role(id, account_id, role_id) values ('3', '3', '101');

INSERT INTO users (id, account_id, name, sex, phone, email, dept_id, birthday, avatar, createtime) values('1', '1', 'admin', 'MALE', '13888888888', 'cc@qq.com', 1, '1980-01-01', 'boy.gif', getdate());
INSERT INTO users (id, account_id, name, sex, phone, email, dept_id, birthday, avatar, createtime) values('2', '2', '张三', 'FEMALE', '13888888889', '12334@qq.com', 1, '1980-01-01', 'girl.gif', getdate());
INSERT INTO users (id, account_id, name, sex, phone, email, dept_id, birthday, avatar, createtime) values('3', '3', '李四', 'MALE', '13888888890', '12233@qq.com', 1, '1980-01-01', 'boy.gif', getdate());