use dosth;

-- 菜单
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('101', 'system', NULL, '', '', '', '1', '系统管理', '1', NULL, NULL, '1', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('102', 'equsetting', NULL, '', '\0', '', '2', '柜体管理', '1', 'system', '[system],', '2', NULL, '/equsetting');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('201', 'basicInfo', NULL, '', '', '', '1', '基础信息', '1', NULL, NULL, '1', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('202', 'unit', NULL, '', '\0', '', '2', '单位管理', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/unit');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('203', 'manufacturer', NULL, '', '\0', '', '2', '厂商管理', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/manufacturer');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('204', 'matequinfo', NULL, '', '\0', '', '2', '物料录入', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/matequinfo');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('205', 'matCategory', NULL, '', '\0', '', '2', '物料关联', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/matCategory');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('206', 'deptMgr', NULL, '', '\0', '', '2', '部门设置', '1', 'basicInfo', '[basicInfo],', '1', NULL, '/mgrDept');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('207', 'mgrUser', NULL, '', '\0', '', '2', '用户设置', '1', 'basicInfo', '[basicInfo],', '1', NULL, '/mgrUser');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('208', 'borrowPopedom', NULL, '', '\0', '', '2', '权限设置', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/borrowPopedom');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('209', 'dailyLimit', NULL, '', '\0', '', '2', '限额设置', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/dailyLimit');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('210', 'restitutionType', NULL, '', '\0', '', '2', '归还设置', '1', 'basicInfo', '[basicInfo],', '2', NULL, '/restitutionType');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('301', 'tool', NULL, '', '', '', '1', '日常作业', '1', NULL, NULL, '2', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('302', 'equdetail', NULL, '', '\0', '', '2', '库位补料', '1', 'tool', '[tool],', '2', NULL, '/equdetail');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('303', 'matInOrOut', NULL, '', '\0', '', '2', '物料上下架', '1', 'tool', '[tool],', '2', NULL, '/matInOrOut');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('304', 'recycReview', NULL, '', '\0', '', '3', '回收审核', '1', 'tool', '[tool],', '2', NULL, '/recycReview');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('306', 'inventory', NULL, '', '\0', '', '2', '盘点管理', '1', 'tool', '[tool],', '2', NULL, '/inventory');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('401', 'query', NULL, '', '', '', '1', '报表记录', '1', NULL, NULL, '2', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('402', 'mainCabinetQuery', NULL, '', '\0', '', '2', '领用记录', '1', 'query', '[query],', '2', NULL, '/mainCabinetQuery');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('403', 'matReturnBackBill', NULL, '', '\0', '', '2', '归还记录', '1', 'query', '[query],', '2', NULL, '/matReturnBackBill');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('404', 'stock', NULL, '', '\0', '', '2', '库存明细', '1', 'query', '[query],', '2', NULL, '/stock');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('405', 'feedQuery', NULL, '', '\0', '', '2', '补料记录', '1', 'query', '[query],', '2', NULL, '/feedQuery');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('406', 'lowerFrameQuery', NULL, '', '\0', '', '2', '下架记录', '1', 'query', '[query],', '2', NULL, '/lowerFrameQuery');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('407', 'useRecordSummary', NULL, '', '\0', '', '2', '领用汇总', '1', 'query', '[query],', '2', NULL, '/useRecordSummary');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('408', 'feedRecordSummary', NULL, '', '\0', '', '2', '补料汇总', '1', 'query', '[query],', '2', NULL, '/feedRecordSummary');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('410', 'inventoryQuery', NULL, '', '\0', '', '2', '盘点记录', '1', 'query', '[query],', '2', NULL, '/inventory/query');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('501', 'systemSet', NULL, '', '', '', '1', '系统设置', '1', NULL, NULL, '2', NULL, '');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('502', 'clearData', NULL, '', '\0', '', '2', '清理数据', '1', 'systemSet', '[systemSet],', '2', NULL, '/clearData');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('503', 'noticeMgr', NULL, '', '\0', '', '2', '通知管理', '1', 'systemSet', '[systemSet],', '2', NULL, '/noticeMgr');
INSERT INTO `dosth`.`menu` (`id`, `code`, `icon`, `is_menu`, `is_open`, `is_use`, `levels`, `name`, `num`, `pcode`, `pcodes`, `system_info_id`, `tips`, `url`) VALUES ('504', 'timeTask', NULL, '', '\0', '', '2', '定时任务', '1', 'systemSet', '[systemSet],', '2', NULL, '/timeTask');

-- 角色权限
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('101', '101', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('102', '201', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('103', '301', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('104', '401', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('105', '501', '101');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('106', '201', '102');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('107', '301', '102');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('108', '401', '102');
INSERT INTO `dosth`.`relation` (`id`, `menu_id`, `role_id`) VALUES ('109', '501', '102');


SET FOREIGN_KEY_CHECKS=0;

-- 定时任务
-- ----------------------------
-- Table structure for time_task
-- ----------------------------
DROP TABLE IF EXISTS `time_task`;
CREATE TABLE `time_task` (
  `id` varchar(36) NOT NULL,
  `name` int(11) DEFAULT NULL,
  `status` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of time_task
-- ----------------------------
INSERT INTO `time_task` VALUES ('1', '0', 'OFF');
INSERT INTO `time_task` VALUES ('2', '1', 'OFF');
INSERT INTO `time_task` VALUES ('3', '2', 'OFF');
INSERT INTO `time_task` VALUES ('4', '3', 'OFF');
INSERT INTO `time_task` VALUES ('5', '4', 'OFF');
INSERT INTO `time_task` VALUES ('6', '5', 'OFF');
INSERT INTO `time_task` VALUES ('7', '6', 'OFF');
INSERT INTO `time_task` VALUES ('8', '7', 'OFF');

-- 定时任务详情
-- ----------------------------
-- Table structure for time_task_detail
-- ----------------------------
DROP TABLE IF EXISTS `time_task_detail`;
CREATE TABLE `time_task_detail` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  `cron_expression` varchar(36) DEFAULT NULL,
  `execution_time` varchar(36) DEFAULT NULL,
  `job_group` varchar(36) DEFAULT NULL,
  `job_id` varchar(36) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of time_task_detail
-- ----------------------------
INSERT INTO `time_task_detail` VALUES ('1', '1,', '0 0 8 * * ?', '8', 'REPORT', '1', 'admin,');

