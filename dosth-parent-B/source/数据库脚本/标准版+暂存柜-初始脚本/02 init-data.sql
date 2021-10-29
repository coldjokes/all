use dosth;
SET FOREIGN_KEY_CHECKS = 0;
-- 账户角色
DELETE FROM dosth.account_role;
-- 角色菜单
DELETE FROM dosth.relation;
-- 角色
DELETE FROM dosth.roles;
-- 菜单
DELETE FROM dosth.menu;
-- 系统信息
DELETE FROM dosth.system_info;
-- 账户
DELETE FROM dosth.account;
-- 用户
DELETE FROM dosth.users;
-- 部门
DELETE FROM dosth.dept;
SET FOREIGN_KEY_CHECKS = 1;

-- 菜单维护
INSERT INTO `dosth`.`system_info` (`id`, `status`, `system_name`, `url`) VALUES ('1', NULL, '部门用户设置', '8081');
INSERT INTO `dosth`.`system_info` (`id`, `status`, `system_name`, `url`) VALUES ('2', NULL, '基础信息和报表', '8082');

-- 菜单
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('101', 'system', NULL, 1, 1, 1, '1', '系统管理', '1', NULL, NULL, '1', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('102', 'equsetting', NULL, 1, 0, 1, '2', '柜体管理', '1', 'system', '[system],', '2', NULL, '/equsetting');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('103', 'clearData', NULL, 1, 0, 1, '2', '清理数据', '1', 'system', '[system],', '2', NULL, '/clearData');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('201', 'basicInfo', NULL, 1, 1, 1, '1', '基础信息', '1', NULL, NULL, '1', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('203', 'manufacturer', NULL, 1, 0, 1, '2', '厂商管理', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/manufacturer');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('204', 'matequinfo', NULL, 1, 0, 1, '2', '物料录入', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/matequinfo');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('205', 'matCategory', NULL, 1, 0, 1, '2', '物料关联', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/matCategory');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('206', 'deptMgr', NULL, 1, 0, 1, '2', '部门设置', '1', 'basicInfo', '[basicInfo],', '1', NULL, '/mgrDept');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('207', 'mgrUser', NULL, 1, 0, 1, '2', '用户设置', '1', 'basicInfo', '[basicInfo],', '1', NULL, '/mgrUser');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('208', 'borrowPopedom', NULL, 1, 0, 1, '2', '权限设置', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/borrowPopedom');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('209', 'dailyLimit', NULL, 1, 0, 1, '2', '限额设置', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/dailyLimit');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('210', 'restitutionType', NULL, 1, 0, 1, '2', '归还设置', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/restitutionType');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('211', 'noticeMgr', NULL, 1, 0, 1, '2', '通知管理', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/noticeMgr');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('301', 'tool', NULL, 1, 1, 1, '1', '日常作业', '1', NULL, NULL, '2', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('302', 'equdetail', NULL, 1, 0, 1, '2', '库位补料', '1', 'tool', '[tool],', '2', NULL, '/equdetail');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('303', 'matInOrOut', NULL, 1, 0, 1, '2', '物料上下架', '1', 'tool', '[tool],', '2', NULL, '/matInOrOut');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('304', 'recycReview', NULL, 1, 0, 1, '3', '回收审核', '1', 'tool', '[tool],', '2', NULL, '/recycReview');
-- INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('305', 'statement', NULL, 1, 0, 1, '2', '核对管理', '1', 'tool', '[tool],', '2', NULL, '/statement');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('306', 'inventory', NULL, 1, 0, 1, '2', '盘点管理', '1', 'tool', '[tool],', '2', NULL, '/inventory');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('401', 'query', NULL, 1, 1, 1, '1', '报表记录', '1', NULL, NULL, '2', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('402', 'mainCabinetQuery', NULL, 1, 0, 1, '2', '领用记录', '1', 'query', '[query],', '2', NULL, '/mainCabinetQuery');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('403', 'matReturnBackBill', NULL, 1, 0, 1, '2', '归还记录', '1', 'query', '[query],', '2', NULL, '/matReturnBackBill');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('404', 'stock', NULL, 1, 0, 1, '2', '库存明细', '1', 'query', '[query],', '2', NULL, '/stock');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('405', 'feedQuery', NULL, 1, 0, 1, '2', '补料记录', '1', 'query', '[query],', '2', NULL, '/feedQuery');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('406', 'lowerFrameQuery', NULL, 1, 0, 1, '2', '下架记录', '1', 'query', '[query],', '2', NULL, '/lowerFrameQuery');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('407', 'viceCabinetQuery', NULL, 1, 0, 1, '2', '暂存柜领用记录', '1', 'query', '[query],', '2', NULL, '/viceCabinetQuery');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('408', 'inventoryQuery', NULL, 1, 0, 1, '2', '盘点记录', '1', 'query', '[query],', '2', NULL, '/inventory/query');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('409', 'useRecordSummary', NULL, 1, 0, 1, '2', '领用汇总', '1', 'query', '[query],', '2', NULL, '/useRecordSummary');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('410', 'feedRecordSummary', NULL, 1, 0, 1, '2', '补料汇总', '1', 'query', '[query],', '2', NULL, '/feedRecordSummary');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('411', 'categorySummary', NULL, '', '\0', '', '2', '领用途径汇总', '1', 'query', '[query],', '2', NULL, '/useRecordSummary/category');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('412', 'deptSummary', NULL, '', '\0', '', '2', '领用部门汇总', '1', 'query', '[query],', '2', NULL, '/useRecordSummary/dept');

-- 部门
INSERT INTO `dosth`.`dept` (`id`, `dept_name`, `p_id`, `status`, `full_ids`) VALUES ('1', '总公司', NULL, 'OK', NULL);
INSERT INTO `dosth`.`dept` (`id`, `dept_name`, `p_id`, `status`, `full_ids`) VALUES ('402881ba6dd83f93016dd844ec4c0004', '人事部', '1', 'OK', NULL);
INSERT INTO `dosth`.`dept` (`id`, `dept_name`, `p_id`, `status`, `full_ids`) VALUES ('402881ba6e3b3f37016e3b408c5b0004', '市场部', '1', 'OK', NULL);

-- 角色
INSERT INTO `dosth`.`roles` (`id`, `dept_id`, `name`, `num`, `p_id`, `tips`, `version`) VALUES ('101', '1', '超级管理员', '1', NULL, 'administrator', '1');
INSERT INTO `dosth`.`roles` (`id`, `dept_id`, `name`, `num`, `p_id`, `tips`, `version`) VALUES ('102', '1', '管理员', '2', '101', 'admin', NULL);

-- 角色权限
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('101', '101', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('102', '201', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('103', '301', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('104', '401', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('105', '201', '102');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('106', '301', '102');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('107', '401', '102');

-- 管理员帐号信息-- 
INSERT INTO `dosth`.`account` (`id`, `face_pwd`, `login_name`, `password`, `salt`, `status`, `version`) VALUES ('1', 'qyJX0sLCwtLIRakoRRMHpQ==', 'admin', 'cd97df6a2429251d7c3b09f0f496cc3a', 'wiwvb', 'OK', NULL);
INSERT INTO `dosth`.`account` (`id`, `face_pwd`, `login_name`, `password`, `salt`, `status`, `version`) VALUES ('101', 'TiZK2Xa7HhsYFyxIGaksTQ==', 'administrator', 'c0d23d1480f93a4b7fb32ec0f0073797', 'wiwvb', 'OK', NULL);

-- 管理员用户信息
INSERT INTO `dosth`.`users` (`id`, `account_id`, `avatar`, `createtime`, `dept_id`, `end_time`, `name`, `start_time`, `limit_sum_num`, `not_return_limit_num`, `face_feature`, `ic_card`, `email`) VALUES ('1', '1', 'admin.jpg', '2020-01-01 00:00:00', '1', '23:59:59', 'admin', '00:00:00', '0', '0', NULL, NULL, NULL);
INSERT INTO `dosth`.`users` (`id`, `account_id`, `avatar`, `createtime`, `dept_id`, `end_time`, `name`, `start_time`, `limit_sum_num`, `not_return_limit_num`, `face_feature`, `ic_card`, `email`) VALUES ('101', '101', 'admin.jpg', '2020-01-01 00:00:00', '1', '23:59:59', 'administrator', '00:00:00', '0', '0', NULL, NULL, NULL);

-- 管理员权限
INSERT INTO `dosth`.`account_role` (`id`, `account_id`, `role_id`) VALUES ('101', '101', '101');
INSERT INTO `dosth`.`account_role` (`id`, `account_id`, `role_id`) VALUES ('102', '1', '102');

DELETE FROM tool.plc_setting;

-- PLC参数
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1001', '4096', '10', 'YES', 'F1', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1002', '6099', '20', 'YES', 'F2', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1003', '6101', '30', 'YES', 'F3', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1004', '6103', '40', 'YES', 'F4', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1005', '6105', '50', 'YES', 'F5', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1006', '6107', '60', 'YES', 'F6', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1007', '6109', '70', 'YES', 'F7', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1008', '6111', '80', 'YES', 'F8', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1009', '6113', '90', 'YES', 'F9', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1010', '6115', '100', 'YES', 'F10', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1011', '6117', '35', 'YES', '取料口位置', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1012', '6119', '30', 'YES', '运行速度', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1013', '6121', '35', 'NO', '实时位置', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1014', '6123', '0', 'NO', '偏移量', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1015', '4187', NULL, 'NO', '目标层', '寄存器', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1016', '2349', NULL, 'NO', '点动上升', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1017', '2350', NULL, 'NO', '点动下降', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1018', '2351', NULL, 'NO', '取料口开门', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1019', '2352', NULL, 'NO', '取料口关门', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1020', '2353', NULL, 'NO', '复位料斗', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1021', '2354', NULL, 'NO', '已到达目标层', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1022', '2355', NULL, 'NO', '取料口关门检测', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1023', '2356', NULL, 'NO', '故障标志', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1024', '2357', NULL, 'NO', '上限位报警', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1025', '2358', NULL, 'NO', '下限位报警', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1026', '2359', NULL, 'NO', '伺服驱动器报警', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1027', '2360', NULL, 'NO', '开门失败', '线圈', 'ENABLE');
INSERT INTO `tool`.`plc_setting` (`id`, `address`, `default_value`, `is_default`, `plc_name`, `remark`, `status`) VALUES ('1028', '2361', NULL, 'NO', '关门失败', '线圈', 'ENABLE');

DELETE FROM `tool`.unit;
-- 单位
INSERT INTO `tool`.`unit` (`id`, `status`, `unit_name`) VALUES ('101', 'ENABLE', '支');
INSERT INTO `tool`.`unit` (`id`, `status`, `unit_name`) VALUES ('102', 'ENABLE', '盒');
INSERT INTO `tool`.`unit` (`id`, `status`, `unit_name`) VALUES ('103', 'ENABLE', '片');
INSERT INTO `tool`.`unit` (`id`, `status`, `unit_name`) VALUES ('104', 'ENABLE', '把');
INSERT INTO `tool`.`unit` (`id`, `status`, `unit_name`) VALUES ('105', 'ENABLE', '件');
INSERT INTO `tool`.`unit` (`id`, `status`, `unit_name`) VALUES ('106', 'ENABLE', '支/盒');
INSERT INTO `tool`.`unit` (`id`, `status`, `unit_name`) VALUES ('107', 'ENABLE', '片/盒');
INSERT INTO `tool`.`unit` (`id`, `status`, `unit_name`) VALUES ('999', 'ENABLE', '台');

DELETE FROM `tool`.manufacturer;
-- 供应商
INSERT INTO `tool`.`manufacturer` (`id`, `address`, `contact`, `manufacturer_name`, `phone`, `remark`, `status`) VALUES ('001', '苏州', '阿诺', '阿诺', '13800000000', '', 'ENABLE');

DELETE FROM `tool`.mat_category_tree;
-- 权限树主分支

INSERT INTO `tool`.mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('1', NOW(), 'MATTYPE', '0', '类型', '1', 'ENABLE', '1', '类型');
INSERT INTO `tool`.mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('2', NOW(), 'REQREF', '0', '设备', '2', 'ENABLE', '2', '设备');
INSERT INTO `tool`.mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('3', NOW(), 'PROCREF', '0', '工序', '3', 'ENABLE', '3', '工序');
INSERT INTO `tool`.mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('4', NOW(), 'PARTS', '0', '零件', '4', 'ENABLE', '4', '零件');
INSERT INTO `tool`.mat_category_tree(id, create_date, equ_type, `level`, `name`, p_id, `status`, f_id, f_name) VALUES ('5', NOW(), 'CUSTOM', '0', '自定义', '5', 'ENABLE', '5', '自定义');

DELETE FROM `tool`.restitution_type;
-- 物料归还定义
INSERT INTO `tool`.`restitution_type` (`id`, `remark`, `rest_name`, `return_back_type`, `status`) VALUES ('101', '续用', '续用', 'NORMAL', 'ENABLE');
INSERT INTO `tool`.`restitution_type` (`id`, `remark`, `rest_name`, `return_back_type`, `status`) VALUES ('102', '修磨', '修磨', 'NORMAL', 'ENABLE');
INSERT INTO `tool`.`restitution_type` (`id`, `remark`, `rest_name`, `return_back_type`, `status`) VALUES ('103', '错领', '错领', 'NORMAL', 'ENABLE');
INSERT INTO `tool`.`restitution_type` (`id`, `remark`, `rest_name`, `return_back_type`, `status`) VALUES ('104', '报废', '报废', 'ABNORMAL', 'ENABLE');
INSERT INTO `tool`.`restitution_type` (`id`, `remark`, `rest_name`, `return_back_type`, `status`) VALUES ('105', '遗失', '遗失', 'ABNORMAL', 'ENABLE');

DELETE FROM `tool`.time_task;
-- 定时任务
INSERT INTO `tool`.`time_task` (`id`, `name`, `status`) VALUES ('1', '0', 'OFF');
INSERT INTO `tool`.`time_task` (`id`, `name`, `status`) VALUES ('2', '1', 'OFF');
INSERT INTO `tool`.`time_task` (`id`, `name`, `status`) VALUES ('3', '2', 'OFF');
INSERT INTO `tool`.`time_task` (`id`, `name`, `status`) VALUES ('4', '3', 'OFF');
INSERT INTO `tool`.`time_task` (`id`, `name`, `status`) VALUES ('5', '4', 'OFF');
INSERT INTO `tool`.`time_task` (`id`, `name`, `status`) VALUES ('6', '5', 'OFF');
INSERT INTO `tool`.`time_task` (`id`, `name`, `status`) VALUES ('7', '6', 'OFF');
INSERT INTO `tool`.`time_task` (`id`, `name`, `status`) VALUES ('8', '7', 'OFF');

DELETE FROM `tool`.time_task_detail;
-- 定时任务详情
INSERT INTO `tool`.`time_task_detail` (`id`, `account_id`, `cron_expression`, `user_name`, `execution_time`, `job_id`, `job_group`) VALUES ('1', '1,', '0 0 8 * * ?', 'admin,', '8', '1', 'REPORT');
