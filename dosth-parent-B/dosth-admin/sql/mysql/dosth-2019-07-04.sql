use dosth;

INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('1', null, '', '总公司', '总公司', '总公司', 1, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('2', '1', '[1],', '开发部', '开发部', '开发部', 2, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('3', '1', '[1],', '运营部', '运营部', '运营部', 3, 'OK');
INSERT INTO dept (id, p_id, full_ids, dept_name, simple_name, full_name, num, status) values ('4', '1', '[1],', '财务部', '财务部', '财务部', 4, 'OK');

INSERT INTO system_info(id, system_name, url) values(1, '部门用户设置', 'http://127.0.0.1:8081');
INSERT INTO system_info(id, system_name, url) values(2, '基础信息和报表', 'http://127.0.0.1:8082');

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('101', '1', 'system', null, null, '部门用户设置', 1, 1, '', 'fa-user', 1, 1, 1, null);

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('102', '1', 'deptMgr', 'system', '[system],', '部门设置', 1, 2, '/mgrDept', '', 1, 0, 1, null );
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('103', '1', 'mgrUser', 'system', '[system],', '用户设置', 1, 2, '/mgrUser', 'fa-train', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('104', '1', 'mgrAccount', 'system', '[system],', '账户设置', 1, 2, '/mgrAccount', 'fa-train', 1, 0, 1, null);


INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (201, 2, 'tool', null, null, '基础信息和报表', 1, 1, '', '', 1, 1, 1, null);

INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (202, 2, 'toolBasic', 'tool', '[tool],', '基础信息', 1, 2, '', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (203, 2, 'unit', 'toolBasic', '[tool],[toolBasic],', '单位管理', 1, 3, '/unit', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (204, 2, 'manufacturer', 'toolBasic', '[tool],[toolBasic],', '供货商管理', 1, 3, '/manufacturer', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (205, 2, 'manufacturerCustom', 'toolBasic', '[tool],[toolBasic],', '供货商详情', 1, 3, '/manufacturerCustom', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (206, 2, 'matequtype', 'toolBasic', '[tool],[toolBasic],', '物料类型设置', 1, 3, '/matequtype', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (207, 2, 'matequinfo', 'toolBasic', '[tool],[toolBasic],', '物料清单导入', 1, 3, '/matequinfo', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (208, 2, 'extraBoxNumSetting', 'toolBasic', '[tool],[toolBasic],', '暂存柜数量设定', 1, 3, '/extraBoxNumSetting', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (209, 2, 'dailyLimit', 'toolBasic', '[tool],[toolBasic],', '每日限额', 1, 3, '/dailyLimit', '', 1, 0, 1, null);

INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (210, 2, 'paramDefinit', 'tool', '[tool],', '关联设置', 1, 2, '', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (211, 2, 'custom', 'paramDefinit', '[tool],[paramDefinit],', '自定义领取类型', 1, 3, '/custom', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (212, 2, 'procinfo', 'paramDefinit', '[tool],[paramDefinit],', '工序类型设置', 1, 3, '/procinfo', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (213, 2, 'parts', 'paramDefinit', '[tool],[paramDefinit],', '零件分类设置', 1, 3, '/parts', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (214, 2, 'matAssociation', 'paramDefinit', '[tool],[paramDefinit],', '物料关联设置', 1, 3, '/matAssociation', '', 1, 0, 1, null);

INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (215, 2, 'preferences', 'tool', '[tool],', '日常作业', 1, 2, '', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (216, 2, 'equdetail', 'preferences', '[tool],[preferences],', '柜体补料/库位设置', 1, 3, '/equdetail', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (217, 2, 'lowerFrameMgr', 'preferences', '[tool],[preferences],', '下架管理', 1, 3, '/lowerFrameMgr', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (218, 2, 'matReturnBackBill', 'preferences', '[tool],[preferences],', '归还审核', 1, 3, '/matReturnBackBill', '', 1, 0, 1, null);

INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (219, 2, 'query', 'tool', '[tool],', '报表记录', 1, 2, '', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (220, 2, 'mainCabinetQuery', 'query', '[tool],[query],', '主柜领用记录', 1, 3, '/mainCabinetQuery', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (221, 2, 'viceCabinetQuery', 'query', '[tool],[query],', '暂存柜领用记录', 1, 3, '/viceCabinetQuery', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (222, 2, 'viceCabinetDetail', 'query', '[tool],[query],', '暂存柜库存明细', 1, 3, '/viceCabinetDetail', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (223, 2, 'feedQuery', 'query', '[tool],[query],', '补料记录', 1, 3, '/feedQuery', '', 1, 0, 1, null);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (224, 2, 'lowerFrameQuery', 'query', '[tool],[query],', '下架记录', 1, 3, '/lowerFrameQuery', '', 1, 0, 1, null);

INSERT INTO roles(id, p_id, dept_id, name, num, tips, version) values('101', '101', '1', '超级管理员', 1, 'administrator', '1');
INSERT INTO roles(id, p_id, dept_id, name, num, tips, version) values('102', '101', '1', '管理员', 2, 'admin', '1');

INSERT INTO relation (id, menu_id, role_id) values ('101', '101', '101');	
INSERT INTO relation (id, menu_id, role_id) values ('102', '201', '101');	

INSERT INTO account (id, login_name, password, face_pwd, salt, status, version) values ('1', 'admin', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', 'sDiaLyeh+RwocMiweDJcGA==', '3b9j6', 'OK', null);
INSERT INTO account (id, login_name, password, face_pwd, salt, status, version) values ('2', 'ZS', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', 'sDiaLyeh+RwocMiweDJcGA==', '3b9j6', 'OK', null);
INSERT INTO account (id, login_name, password, face_pwd, salt, status, version) values ('3', 'LS', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', 'sDiaLyeh+RwocMiweDJcGA==', '3b9j6', 'OK', null);

INSERT INTO account_role(id, account_id, role_id) values ('1', '1', '101');
INSERT INTO account_role(id, account_id, role_id) values ('2', '2', '101');
INSERT INTO account_role(id, account_id, role_id) values ('3', '3', '101');

INSERT INTO users (id, account_id, name, dept_id, avatar, createtime, start_time, end_time) values('1', '1', 'admin', 1, 'boy.gif', NOW(), '00:00:00', '23:59:59');
INSERT INTO users (id, account_id, name, dept_id, avatar, createtime, start_time, end_time) values('2', '2', '张三', 1, 'boy.gif', NOW(), '00:00:00', '23:59:59');
INSERT INTO users (id, account_id, name, dept_id, avatar, createtime, start_time, end_time) values('3', '3', '李四', 1, 'boy.gif', NOW(), '00:00:00', '23:59:59');