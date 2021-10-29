/*
Navicat MySQL Data Transfer

Source Server         : 192.168.2.58
Source Server Version : 50562
Source Host           : 192.168.2.58:3306
Source Database       : dosth

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2019-11-28 09:23:21
*/

SET FOREIGN_KEY_CHECKS=0;

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
INSERT INTO `menu` VALUES ('201', 'tool', '', '', '', '', '1', '基础信息和报表', '1', null, null, '2', null, '');
INSERT INTO `menu` VALUES ('202', 'toolBasic', '', '', '\0', '', '2', '基础信息', '1', 'tool', '[tool],', '2', null, '');
INSERT INTO `menu` VALUES ('203', 'unit', '', '', '\0', '', '3', '单位管理', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/unit');
INSERT INTO `menu` VALUES ('204', 'manufacturer', '', '', '\0', '', '3', '供货商管理', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/manufacturer');
INSERT INTO `menu` VALUES ('205', 'manufacturerCustom', '', '', '\0', '', '3', '供货商详情', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/manufacturerCustom');
INSERT INTO `menu` VALUES ('206', 'matequtype', '', '', '\0', '', '3', '物料类型设置', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/matequtype');
INSERT INTO `menu` VALUES ('207', 'matequinfo', '', '', '\0', '', '3', '物料清单导入', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/matequinfo');
INSERT INTO `menu` VALUES ('208', 'extraBoxNumSetting', '', '', '\0', '', '3', '暂存柜数量设定', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/extraBoxNumSetting');
INSERT INTO `menu` VALUES ('209', 'dailyLimit', '', '', '\0', '', '3', '每日限额', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/dailyLimit');
INSERT INTO `menu` VALUES ('210', 'matAssociation', '', '', '\0', '', '3', '物料关联设置', '1', 'toolBasic', '[tool],[toolBasic],', '2', null, '/matAssociation');
INSERT INTO `menu` VALUES ('215', 'preferences', '', '', '\0', '', '2', '日常作业', '1', 'tool', '[tool],', '2', null, '');
INSERT INTO `menu` VALUES ('216', 'equdetail', '', '', '\0', '', '3', '柜体补料/库位设置', '1', 'preferences', '[tool],[preferences],', '2', null, '/equdetail');
INSERT INTO `menu` VALUES ('217', 'lowerFrameMgr', '', '', '\0', '', '3', '物料上下架', '1', 'preferences', '[tool],[preferences],', '2', null, '/lowerFrameMgr');
INSERT INTO `menu` VALUES ('218', 'matReturnBackBill', '', '', '\0', '', '3', '归还审核', '1', 'preferences', '[tool],[preferences],', '2', null, '/matReturnBackBill');
INSERT INTO `menu` VALUES ('219', 'query', '', '', '\0', '', '2', '报表记录', '1', 'tool', '[tool],', '2', null, '');
INSERT INTO `menu` VALUES ('220', 'mainCabinetQuery', '', '', '\0', '', '3', '主柜领用记录', '1', 'query', '[tool],[query],', '2', null, '/mainCabinetQuery');
INSERT INTO `menu` VALUES ('221', 'viceCabinetQuery', '', '', '\0', '', '3', '暂存柜领用记录', '1', 'query', '[tool],[query],', '2', null, '/viceCabinetQuery');
INSERT INTO `menu` VALUES ('222', 'viceCabinetDetail', '', '', '\0', '', '3', '暂存柜库存明细', '1', 'query', '[tool],[query],', '2', null, '/viceCabinetDetail');
INSERT INTO `menu` VALUES ('223', 'feedQuery', '', '', '\0', '', '3', '补料记录', '1', 'query', '[tool],[query],', '2', null, '/feedQuery');
INSERT INTO `menu` VALUES ('224', 'lowerFrameQuery', '', '', '\0', '', '3', '下架记录', '1', 'query', '[tool],[query],', '2', null, '/lowerFrameQuery');
