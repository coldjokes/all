use dosth;

DELETE FROM users;
DELETE FROM iccard;
DELETE FROM login_log;
DELETE FROM account_role;
DELETE FROM account;
DELETE FROM relation;
DELETE FROM roles;
DELETE FROM menu;
DELETE FROM system_info;
DELETE FROM dept;


INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('1', null, '', '总公司', '总公司', '总公司', 1, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('2', '1', '[1],', '开发部', '开发部', '开发部', 2, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('3', '1', '[1],', '运营部', '运营部', '运营部', 3, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('4', '1', '[1],', '财务部', '财务部', '财务部', 4, 'OK');

INSERT INTO system_info(id, system_name, url) values(1, '系统管理', 'http://192.168.2.97:8081');
INSERT INTO system_info(id, system_name, url) values('2', '工具管理', 'http://192.168.2.97:8082');

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
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('201', '2', 'tool', null, null, '工具管理', 1, 1, '', 'fa-user', 1, 1, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('202', '2', 'toolBasic', 'tool', '[tool],', '基础信息', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('203', '2', 'matequtype', 'toolBasic', '[tool],[toolBasic],', '物料/设备管理', 1, 3, '/matequtype', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('204', '2', 'equsetting', 'toolBasic', '[tool],[toolBasic],', '柜体管理', 1, 3, '/equsetting', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('205', '2', 'subcabinet', 'toolBasic', '[tool],[toolBasic],', '副柜管理', 1, 3, '/subcabinet', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('206', '2', 'unit', 'toolBasic', '[tool],[toolBasic],', '单位管理', 1, 3, '/unit', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('207', '2', 'manufacturer', 'toolBasic', '[tool],[toolBasic],', '供应商管理', 1, 3, '/manufacturer', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('208', '2', 'manufacturerCustom', 'toolBasic', '[tool],[toolBasic],', '供应商维护', 1, 3, '/manufacturerCustom', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('209', '2', 'paramDefinit', 'tool', '[tool],', '参数定义', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('210', '2', 'matequinfo', 'paramDefinit', '[tool],[paramDefinit],', '物料/设备定义', 1, 3, '/matequinfo', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('211', '2', 'restitutionType', 'paramDefinit', '[tool],[paramDefinit],', '归还类型定义', 1, 3, '/restitutionType', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('212', '2', 'custom', 'paramDefinit', '[tool],[paramDefinit],', '物料自定义', 1, 3, '/custom', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('213', '2', 'product', 'paramDefinit', '[tool],[paramDefinit],', '产品定义', 1, 3, '/product', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('214', '2', 'procinfo', 'paramDefinit', '[tool],[paramDefinit],', '工序定义', 1, 3, '/procinfo', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('215', '2', 'parts', 'paramDefinit', '[tool],[paramDefinit],', '零件定义', 1, 3, '/parts', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('216', '2', 'plcSetting', 'paramDefinit', '[tool],[paramDefinit],', 'PLC定义', 1, 3, '/plcSetting', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('217', '2', 'preferences', 'tool', '[tool],', '参数设置', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('218', '2', 'smartcabinet', 'preferences', '[tool],[preferences],', '柜体设置', 1, 3, '/smartcabinet', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('219', '2', 'subcabinetsetting', 'preferences', '[tool],[preferences],', '副柜参数设置', 1, 3, '/subcabinetsetting', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('220', '2', 'matAssociation', 'preferences', '[tool],[preferences],', '物料关联设置', 1, 3, '/matAssociation', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('221', '2', 'equStockMgr', 'tool', '[tool],', '设备库存管理', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('222', '2', 'equdetail', 'equStockMgr', '[tool],[equStockMgr],', '柜体补料', 1, 3, '/equdetail', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('223', '2', 'allocationMgr', 'equStockMgr', '[tool],[equStockMgr],', '调拨管理', 1, 3, '/allocationMgr', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('224', '2', 'lowerFrameMgr', 'equStockMgr', '[tool],[equStockMgr],', '下架管理', 1, 3, '/lowerFrameMgr', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('225', '2', 'matReturnBackBill', 'equStockMgr', '[tool],[equStockMgr],', '归还管理', 1, 3, '/matReturnBackBill', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('226', '2', 'query', 'tool', '[tool],', '查询统计', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('227', '2', 'mainCabinetQuery', 'query', '[tool],[query],', '主柜领用查询', 1, 3, '/mainCabinetQuery', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('228', '2', 'viceCabinetQuery', 'query', '[tool],[query],', '副柜领用查询', 1, 3, '/viceCabinetQuery', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('229', '2', 'viceCabinetDetail', 'query', '[tool],[query],', '副柜明细查询', 1, 3, '/viceCabinetDetail', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('230', '2', 'feedQuery', 'query', '[tool],[query],', '补料查询', 1, 3, '/feedQuery', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('231', '2', 'lowerFrameQuery', 'query', '[tool],[query],', '下架查询', 1, 3, '/lowerFrameQuery', '', 1, 0, 1, null);

INSERT INTO roles(id, p_id, dept_id, name, num, tips, version) values('101', '101', '1', '超级管理员', 1, 'administrator', '1');
INSERT INTO roles(id, p_id, dept_id, name, num, tips, version) values('102', '102', '1', '管理员', 2, 'admin', '1');

INSERT INTO relation (id, menu_id, role_id) values ('101', '101', '101');
INSERT INTO relation (id, menu_id, role_id) values ('201', '201', '101');	

INSERT INTO account (id, login_name, password, face_pwd, salt, status, version) values ('1', 'admin', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', 'sDiaLyeh+RwocMiweDJcGA==', '3b9j6', 'OK', null);

INSERT INTO account_role(id, account_id, role_id) values ('1', '1', '101');

INSERT INTO users(id, account_id, name, sex, phone, email, dept_id, birthday, avatar, createtime) values('1', '1', 'admin', 'MALE', '13888888888', 'cc@qq.com', 1, '1980-01-01', 'boy.gif', getdate());




