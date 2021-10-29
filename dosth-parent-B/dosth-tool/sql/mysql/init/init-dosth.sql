/*
Navicat MySQL Data Transfer

Source Server         : 192.168.2.58
Source Server Version : 50562
Source Host           : 192.168.2.58:3306
Source Database       : dosth

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2019-09-11 15:25:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` varchar(36) NOT NULL,
  `face_pwd` varchar(200) DEFAULT NULL,
  `login_name` varchar(50) DEFAULT NULL,
  `password` varchar(200) DEFAULT NULL,
  `salt` varchar(10) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', 'sDiaLyeh+RwocMiweDJcGA==', 'admin', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', '3b9j6', 'OK', null);

-- ----------------------------
-- Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `role_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account_role
-- ----------------------------
INSERT INTO `account_role` VALUES ('1', '1', '101');

-- ----------------------------
-- Table structure for advice
-- ----------------------------
DROP TABLE IF EXISTS `advice`;
CREATE TABLE `advice` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `advice_contact` varchar(200) DEFAULT NULL,
  `advice_content` longtext,
  `advice_images` varchar(200) DEFAULT NULL,
  `advice_mail` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf9ogbkugxkjxi909nvw25xt77` (`account_id`),
  CONSTRAINT `FKf9ogbkugxkjxi909nvw25xt77` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of advice
-- ----------------------------

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `mat_id` varchar(36) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `receive_type` varchar(36) DEFAULT NULL,
  `receive_info` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKob1hwkxgjcjruji7ek6f2hcbe` (`mat_id`),
  CONSTRAINT `FKob1hwkxgjcjruji7ek6f2hcbe` FOREIGN KEY (`mat_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` varchar(36) NOT NULL,
  `dept_name` varchar(50) DEFAULT NULL,
  `full_ids` varchar(200) DEFAULT NULL,
  `full_name` varchar(200) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `p_id` varchar(36) DEFAULT NULL,
  `simple_name` varchar(100) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgoxdhh3jhyqumj2q5yvnwu49i` (`p_id`),
  CONSTRAINT `FKgoxdhh3jhyqumj2q5yvnwu49i` FOREIGN KEY (`p_id`) REFERENCES `dept` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES ('1', '总公司', '', '总公司', '1', null, '总公司', 'OK');
INSERT INTO `dept` VALUES ('2', '开发部', '[1],', '开发部', '2', '1', '开发部', 'OK');

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `id` varchar(36) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `p_id` varchar(255) DEFAULT NULL,
  `tips` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict
-- ----------------------------

-- ----------------------------
-- Table structure for helper
-- ----------------------------
DROP TABLE IF EXISTS `helper`;
CREATE TABLE `helper` (
  `id` varchar(36) NOT NULL,
  `answer` varchar(200) DEFAULT NULL,
  `question_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of helper
-- ----------------------------

-- ----------------------------
-- Table structure for iccard
-- ----------------------------
DROP TABLE IF EXISTS `iccard`;
CREATE TABLE `iccard` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `ic_str` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK46n4ffy5tg6mbsaqcntua9qvp` (`account_id`),
  CONSTRAINT `FK46n4ffy5tg6mbsaqcntua9qvp` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of iccard
-- ----------------------------

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `log_name` varchar(30) DEFAULT NULL,
  `message` varchar(200) DEFAULT NULL,
  `succeed` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfyivueaqqoo22w5vrlaqsai8l` (`account_id`),
  CONSTRAINT `FKfyivueaqqoo22w5vrlaqsai8l` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_log
-- ----------------------------

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` varchar(36) NOT NULL,
  `code` varchar(50) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `is_menu` bit(1) DEFAULT NULL,
  `is_open` bit(1) DEFAULT NULL,
  `is_use` bit(1) DEFAULT NULL,
  `levels` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `pcode` varchar(50) DEFAULT NULL,
  `pcodes` varchar(200) DEFAULT NULL,
  `system_info_id` varchar(36) DEFAULT NULL,
  `tips` varchar(255) DEFAULT NULL,
  `url` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b7as01rf085qmbog39pictwrw` (`code`),
  KEY `FK39u6l2dpbrg70x0qbdivm63v7` (`pcode`),
  KEY `FKnieqt5k39twlpao5tuulrdcus` (`system_info_id`),
  CONSTRAINT `FK39u6l2dpbrg70x0qbdivm63v7` FOREIGN KEY (`pcode`) REFERENCES `menu` (`code`),
  CONSTRAINT `FKnieqt5k39twlpao5tuulrdcus` FOREIGN KEY (`system_info_id`) REFERENCES `system_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('101', 'system', '', '', '', '', '1', '部门用户设置', '1', null, null, '1', null, '');
INSERT INTO `menu` VALUES ('102', 'deptMgr', '', '', '\0', '', '2', '部门设置', '1', 'system', '[system],', '1', null, '/mgrDept');
INSERT INTO `menu` VALUES ('103', 'mgrUser', '', '', '\0', '', '2', '用户设置', '1', 'system', '[system],', '1', null, '/mgrUser');
INSERT INTO `menu` VALUES ('104', 'mgrAccount', '', '', '\0', '', '2', '账户设置', '1', 'system', '[system],', '1', null, '/mgrAccount');
INSERT INTO `menu` VALUES ('201', 'tool', '', '', '', '', '1', '基础信息和报表', '1', null, null, '2', null, '');
INSERT INTO `menu` VALUES ('202', 'toolBasic', '', '', '\0', '', '2', '基础信息', '1', 'tool', '[tool],', '2', null, '');
INSERT INTO `menu` VALUES ('203', 'unit', '', '', '\0', '', '3', '单位管理', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/unit');
INSERT INTO `menu` VALUES ('204', 'manufacturer', '', '', '\0', '', '3', '供货商管理', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/manufacturer');
INSERT INTO `menu` VALUES ('205', 'manufacturerCustom', '', '', '\0', '', '3', '供货商详情', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/manufacturerCustom');
INSERT INTO `menu` VALUES ('206', 'matequtype', '', '', '\0', '', '3', '物料类型设置', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/matequtype');
INSERT INTO `menu` VALUES ('207', 'matequinfo', '', '', '\0', '', '3', '物料清单导入', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/matequinfo');
INSERT INTO `menu` VALUES ('208', 'extraBoxNumSetting', '', '', '\0', '', '3', '暂存柜数量设定', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/extraBoxNumSetting');
INSERT INTO `menu` VALUES ('209', 'dailyLimit', '', '', '\0', '', '3', '每日限额', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/dailyLimit');
INSERT INTO `menu` VALUES ('210', 'paramDefinit', '', '', '\0', '', '2', '关联设置', '1', 'tool', '[tool],', '2', null, '');
INSERT INTO `menu` VALUES ('211', 'custom', '', '', '\0', '', '3', '自定义领取类型', '1', 'paramDefinit', '[tool],[paramDefinit],', '2', null, '/custom');
INSERT INTO `menu` VALUES ('212', 'procinfo', '', '', '\0', '', '3', '工序类型设置', '1', 'paramDefinit', '[tool],[paramDefinit],', '2', null, '/procinfo');
INSERT INTO `menu` VALUES ('213', 'parts', '', '', '\0', '', '3', '零件分类设置', '1', 'paramDefinit', '[tool],[paramDefinit],', '2', null, '/parts');
INSERT INTO `menu` VALUES ('214', 'matAssociation', '', '', '\0', '', '3', '物料关联设置', '1', 'paramDefinit', '[tool],[paramDefinit],', '2', null, '/matAssociation');
INSERT INTO `menu` VALUES ('215', 'preferences', '', '', '\0', '', '2', '日常作业', '1', 'tool', '[tool],', '2', null, '');
INSERT INTO `menu` VALUES ('216', 'equdetail', '', '', '\0', '', '3', '柜体补料/库位设置', '1', 'preferences', '[tool],[preferences],', '2', null, '/equdetail');
INSERT INTO `menu` VALUES ('217', 'lowerFrameMgr', '', '', '\0', '', '3', '下架管理', '1', 'preferences', '[tool],[preferences],', '2', null, '/lowerFrameMgr');
INSERT INTO `menu` VALUES ('218', 'matReturnBackBill', '', '', '\0', '', '3', '归还审核', '1', 'preferences', '[tool],[preferences],', '2', null, '/matReturnBackBill');
INSERT INTO `menu` VALUES ('219', 'query', '', '', '\0', '', '2', '报表记录', '1', 'tool', '[tool],', '2', null, '');
INSERT INTO `menu` VALUES ('220', 'mainCabinetQuery', '', '', '\0', '', '3', '主柜领用记录', '1', 'query', '[tool],[query],', '2', null, '/mainCabinetQuery');
INSERT INTO `menu` VALUES ('221', 'viceCabinetQuery', '', '', '\0', '', '3', '暂存柜领用记录', '1', 'query', '[tool],[query],', '2', null, '/viceCabinetQuery');
INSERT INTO `menu` VALUES ('222', 'viceCabinetDetail', '', '', '\0', '', '3', '暂存柜库存明细', '1', 'query', '[tool],[query],', '2', null, '/viceCabinetDetail');
INSERT INTO `menu` VALUES ('223', 'feedQuery', '', '', '\0', '', '3', '补料记录', '1', 'query', '[tool],[query],', '2', null, '/feedQuery');
INSERT INTO `menu` VALUES ('224', 'lowerFrameQuery', '', '', '\0', '', '3', '下架记录', '1', 'query', '[tool],[query],', '2', null, '/lowerFrameQuery');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` varchar(36) NOT NULL,
  `content` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creater` bigint(20) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notice
-- ----------------------------

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  `class_name` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `log_name` varchar(50) DEFAULT NULL,
  `log_type` varchar(10) DEFAULT NULL,
  `message` longtext,
  `method` varchar(30) DEFAULT NULL,
  `succeed` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for phone_user
-- ----------------------------
DROP TABLE IF EXISTS `phone_user`;
CREATE TABLE `phone_user` (
  `id` varchar(36) NOT NULL,
  `app_pwd` varchar(200) DEFAULT NULL,
  `dept_name` varchar(50) DEFAULT NULL,
  `emp_no` varchar(20) DEFAULT NULL,
  `phone_no` varchar(30) DEFAULT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_user
-- ----------------------------

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `id` varchar(36) NOT NULL,
  `menu_id` varchar(36) DEFAULT NULL,
  `role_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8cbvofhycq4uutsfxlwx81778` (`menu_id`),
  KEY `FKe5c8lq05rwogey5ojq5btium1` (`role_id`),
  CONSTRAINT `FK8cbvofhycq4uutsfxlwx81778` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`),
  CONSTRAINT `FKe5c8lq05rwogey5ojq5btium1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of relation
-- ----------------------------
INSERT INTO `relation` VALUES ('101', '101', '101');
INSERT INTO `relation` VALUES ('102', '201', '101');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` varchar(36) NOT NULL,
  `dept_id` varchar(36) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `p_id` varchar(36) DEFAULT NULL,
  `tips` varchar(50) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8qr34liiiam0dccv9tio1n307` (`dept_id`),
  KEY `FKl9f18m8yo0ogxubacp61t0xnb` (`p_id`),
  CONSTRAINT `FK8qr34liiiam0dccv9tio1n307` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`),
  CONSTRAINT `FKl9f18m8yo0ogxubacp61t0xnb` FOREIGN KEY (`p_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('101', '1', '超级管理员', '1', '101', 'administrator', '1');
INSERT INTO `roles` VALUES ('102', '1', '管理员', '2', '101', 'admin', '1');

-- ----------------------------
-- Table structure for system_info
-- ----------------------------
DROP TABLE IF EXISTS `system_info`;
CREATE TABLE `system_info` (
  `id` varchar(36) NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  `system_name` varchar(50) DEFAULT NULL,
  `url` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_info
-- ----------------------------
INSERT INTO `system_info` VALUES ('1', null, '部门用户设置', 'http://192.168.2.58:8081');
INSERT INTO `system_info` VALUES ('2', null, '基础信息和报表', 'http://192.168.2.58:8082');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `avatar` varchar(200) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `dept_id` varchar(36) DEFAULT NULL,
  `end_time` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `start_time` varchar(50) DEFAULT NULL,
  `limit_sum_num` int(11) DEFAULT NULL,
  `not_return_limit_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3pwaj86pwopu3ot96qlrfo2up` (`account_id`),
  KEY `FKb5g26hfoj5g0fim8tth33aiax` (`dept_id`),
  CONSTRAINT `FK3pwaj86pwopu3ot96qlrfo2up` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`),
  CONSTRAINT `FKb5g26hfoj5g0fim8tth33aiax` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '1', 'boy.gif', '2019-08-05 11:08:33', '2', '22:59:59', 'admin', '00:00:00', '0', '0');
