/*
Navicat MySQL Data Transfer

Source Server         : 192.168.2.58
Source Server Version : 50562
Source Host           : 192.168.2.58:3306
Source Database       : tool

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2019-09-11 15:41:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for allocation_mgr
-- ----------------------------
DROP TABLE IF EXISTS `allocation_mgr`;
CREATE TABLE `allocation_mgr` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `disc_posit_id` varchar(36) DEFAULT NULL,
  `feed_posit_id` varchar(36) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3613g7nsq1qa1w0bxgjmbg5pv` (`disc_posit_id`),
  KEY `FK54jphhsaoljptso4dhcdb3aw0` (`feed_posit_id`),
  KEY `FKl9mi7lgdb7fyhihkjg2j0scvt` (`mat_info_id`),
  CONSTRAINT `FK3613g7nsq1qa1w0bxgjmbg5pv` FOREIGN KEY (`disc_posit_id`) REFERENCES `equ_detail_sta` (`id`),
  CONSTRAINT `FK54jphhsaoljptso4dhcdb3aw0` FOREIGN KEY (`feed_posit_id`) REFERENCES `equ_detail_sta` (`id`),
  CONSTRAINT `FKl9mi7lgdb7fyhihkjg2j0scvt` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of allocation_mgr
-- ----------------------------

-- ----------------------------
-- Table structure for borrow_info
-- ----------------------------
DROP TABLE IF EXISTS `borrow_info`;
CREATE TABLE `borrow_info` (
  `id` varchar(36) NOT NULL,
  `back_num` varchar(255) DEFAULT NULL,
  `broken_num` varchar(255) DEFAULT NULL,
  `continued_use_num` varchar(255) DEFAULT NULL,
  `grinding_num` varchar(255) DEFAULT NULL,
  `knife_id` varchar(255) DEFAULT NULL,
  `knife_image` varchar(255) DEFAULT NULL,
  `knife_name` varchar(255) DEFAULT NULL,
  `lose_num` varchar(255) DEFAULT NULL,
  `num` varchar(255) DEFAULT NULL,
  `time` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `wrongcollar_num` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of borrow_info
-- ----------------------------

-- ----------------------------
-- Table structure for borrow_popedom
-- ----------------------------
DROP TABLE IF EXISTS `borrow_popedom`;
CREATE TABLE `borrow_popedom` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `borrow_popedom` varchar(10) DEFAULT NULL,
  `popedoms` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of borrow_popedom
-- ----------------------------

-- ----------------------------
-- Table structure for cabinet_plc_setting
-- ----------------------------
DROP TABLE IF EXISTS `cabinet_plc_setting`;
CREATE TABLE `cabinet_plc_setting` (
  `id` varchar(36) NOT NULL,
  `cabinet_id` varchar(36) DEFAULT NULL,
  `plc_setting_id` varchar(36) DEFAULT NULL,
  `setting_val` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpu1jdm66qwjp5g4a06q1hjqfv` (`plc_setting_id`),
  CONSTRAINT `FKpu1jdm66qwjp5g4a06q1hjqfv` FOREIGN KEY (`plc_setting_id`) REFERENCES `plc_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cabinet_plc_setting
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
-- Table structure for custom
-- ----------------------------
DROP TABLE IF EXISTS `custom`;
CREATE TABLE `custom` (
  `id` varchar(36) NOT NULL,
  `custom_name` varchar(200) DEFAULT NULL,
  `one_menu` varchar(36) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `two_menu` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of custom
-- ----------------------------
INSERT INTO `custom` VALUES ('402881ba6ae22de2016ae3689dcd0004', '数控01-工序A', '数控机床01', '', 'ENABLE', '工序A');

-- ----------------------------
-- Table structure for daily_limit
-- ----------------------------
DROP TABLE IF EXISTS `daily_limit`;
CREATE TABLE `daily_limit` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `limit_num` int(11) NOT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `cur_num` int(11) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `not_return_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK863lo7fsnuvce9tkcitrqkf30` (`mat_info_id`),
  CONSTRAINT `FK863lo7fsnuvce9tkcitrqkf30` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of daily_limit
-- ----------------------------

-- ----------------------------
-- Table structure for equ_detail
-- ----------------------------
DROP TABLE IF EXISTS `equ_detail`;
CREATE TABLE `equ_detail` (
  `id` varchar(36) NOT NULL,
  `equ_setting_id` varchar(36) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `port` varchar(10) DEFAULT NULL,
  `row_no` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6b33i7tm2ilp0cr364t03aeay` (`equ_setting_id`),
  CONSTRAINT `FK6b33i7tm2ilp0cr364t03aeay` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equ_detail
-- ----------------------------
INSERT INTO `equ_detail` VALUES ('1', '297e5a8667960b070167969f51620013', '192.168.6.101', '502', '1', 'ENABLE');
INSERT INTO `equ_detail` VALUES ('2', '297e5a8667960b070167969f51620013', '192.168.6.102', '502', '2', 'ENABLE');
INSERT INTO `equ_detail` VALUES ('3', '297e5a8667960b070167969f51620013', '192.168.6.103', '502', '3', 'ENABLE');
INSERT INTO `equ_detail` VALUES ('4', '297e5a8667960b070167969f51620013', '192.168.6.104', '502', '4', 'ENABLE');
INSERT INTO `equ_detail` VALUES ('5', '297e5a8667960b070167969f51620013', '192.168.6.105', '502', '5', 'ENABLE');
INSERT INTO `equ_detail` VALUES ('6', '297e5a8667960b070167969f51620013', '192.168.6.106', '502', '6', 'ENABLE');
INSERT INTO `equ_detail` VALUES ('7', '297e5a8667960b070167969f51620013', '192.168.6.107', '502', '7', 'ENABLE');
INSERT INTO `equ_detail` VALUES ('8', '297e5a8667960b070167969f51620013', '192.168.6.108', '502', '8', 'ENABLE');
INSERT INTO `equ_detail` VALUES ('9', '297e5a8667960b070167969f51620013', '192.168.6.109', '502', '9', 'ENABLE');

-- ----------------------------
-- Table structure for equ_detail_bill
-- ----------------------------
DROP TABLE IF EXISTS `equ_detail_bill`;
CREATE TABLE `equ_detail_bill` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `equ_detail_sta_id` varchar(36) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdijprmnha3sieceafesgw761a` (`equ_detail_sta_id`),
  KEY `FK19rxj1l6kisp2gmfqkwktq0wg` (`mat_info_id`),
  CONSTRAINT `FK19rxj1l6kisp2gmfqkwktq0wg` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKdijprmnha3sieceafesgw761a` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equ_detail_bill
-- ----------------------------

-- ----------------------------
-- Table structure for equ_detail_sta
-- ----------------------------
DROP TABLE IF EXISTS `equ_detail_sta`;
CREATE TABLE `equ_detail_sta` (
  `id` varchar(36) NOT NULL,
  `col_no` int(11) DEFAULT NULL,
  `cur_num` int(11) DEFAULT NULL,
  `equ_detail_id` varchar(36) DEFAULT NULL,
  `equ_sta` varchar(15) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `max_reserve` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `warn_val` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKte66q10nvy5v7inqe0pehjq78` (`equ_detail_id`),
  KEY `FKwpfkg6meu3gyxd9jquxb4aew` (`mat_info_id`),
  CONSTRAINT `FKte66q10nvy5v7inqe0pehjq78` FOREIGN KEY (`equ_detail_id`) REFERENCES `equ_detail` (`id`),
  CONSTRAINT `FKwpfkg6meu3gyxd9jquxb4aew` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equ_detail_sta
-- ----------------------------
INSERT INTO `equ_detail_sta` VALUES ('1', '1', '0', '1', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('10', '10', '0', '1', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('11', '1', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('12', '2', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('13', '3', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('14', '4', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('15', '5', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('16', '6', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('17', '7', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('18', '8', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('19', '9', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('2', '2', '0', '1', 'NONE', null, '19', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('20', '10', '0', '2', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('21', '1', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('22', '2', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('23', '3', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('24', '4', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('25', '5', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('26', '6', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('27', '7', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('28', '8', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('29', '9', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('3', '3', '0', '1', 'NONE', null, '17', 'ENABLE', '5');
INSERT INTO `equ_detail_sta` VALUES ('30', '10', '0', '3', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('31', '1', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('32', '2', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('33', '3', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('34', '4', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('35', '5', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('36', '6', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('37', '7', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('38', '8', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('39', '9', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('4', '4', '0', '1', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('40', '10', '0', '4', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('41', '1', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('42', '2', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('43', '3', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('44', '4', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('45', '5', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('46', '6', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('47', '7', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('48', '8', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('49', '9', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('5', '5', '0', '1', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('50', '10', '0', '5', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('51', '1', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('52', '2', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('53', '3', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('54', '4', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('55', '5', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('56', '6', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('57', '7', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('58', '8', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('59', '9', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('6', '6', '0', '1', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('60', '10', '0', '6', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('61', '1', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('62', '2', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('63', '3', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('64', '4', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('65', '5', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('66', '6', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('67', '7', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('68', '8', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('69', '9', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('7', '7', '0', '1', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('70', '10', '0', '7', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('71', '1', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('72', '2', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('73', '3', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('74', '4', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('75', '5', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('76', '6', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('77', '7', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('78', '8', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('79', '9', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('8', '8', '0', '1', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('80', '10', '0', '8', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('81', '1', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('82', '2', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('83', '3', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('84', '4', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('85', '5', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('86', '6', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('87', '7', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('88', '8', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('89', '9', '0', '9', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('9', '9', '0', '1', 'NONE', null, '17', 'ENABLE', '3');
INSERT INTO `equ_detail_sta` VALUES ('90', '10', '0', '9', 'NONE', null, '17', 'ENABLE', '3');

-- ----------------------------
-- Table structure for equ_mat_ref
-- ----------------------------
DROP TABLE IF EXISTS `equ_mat_ref`;
CREATE TABLE `equ_mat_ref` (
  `id` varchar(36) NOT NULL,
  `equ_info_id` varchar(36) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `mat_num` int(11) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfqv243kawxlgp2m71opplfn5l` (`equ_info_id`),
  KEY `FKqnsaf5l0v634qatj16ujux56y` (`mat_info_id`),
  CONSTRAINT `FKfqv243kawxlgp2m71opplfn5l` FOREIGN KEY (`equ_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKqnsaf5l0v634qatj16ujux56y` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equ_mat_ref
-- ----------------------------

-- ----------------------------
-- Table structure for equ_setting
-- ----------------------------
DROP TABLE IF EXISTS `equ_setting`;
CREATE TABLE `equ_setting` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `asset_no` varchar(30) DEFAULT NULL,
  `col_num` int(11) DEFAULT NULL,
  `equ_info_id` varchar(36) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `op_date` date DEFAULT NULL,
  `port` varchar(10) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `row_num` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `ver` varchar(50) DEFAULT NULL,
  `equ_setting_parent_id` varchar(36) DEFAULT NULL,
  `equ_setting_name` varchar(50) DEFAULT NULL,
  `door` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK71okty24cip4uxu96ajbfewsi` (`equ_info_id`),
  KEY `FKr4kfvxe2estix3qe4bmw3t2rc` (`equ_setting_parent_id`),
  CONSTRAINT `FK71okty24cip4uxu96ajbfewsi` FOREIGN KEY (`equ_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKr4kfvxe2estix3qe4bmw3t2rc` FOREIGN KEY (`equ_setting_parent_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equ_setting
-- ----------------------------
INSERT INTO `equ_setting` VALUES ('297e5a8667960b070167969f51620013', '1', '0001', '10', '297e5a8667960b0701679698f7cb0012', '192.168.2.58', '2019-04-02', '502', '2018-12-25', '9', 'ENABLE', null, null, '刀具柜001', null);

-- ----------------------------
-- Table structure for extra_box_num_setting
-- ----------------------------
DROP TABLE IF EXISTS `extra_box_num_setting`;
CREATE TABLE `extra_box_num_setting` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  `extra_box_num` varchar(100) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of extra_box_num_setting
-- ----------------------------

-- ----------------------------
-- Table structure for feeding_detail
-- ----------------------------
DROP TABLE IF EXISTS `feeding_detail`;
CREATE TABLE `feeding_detail` (
  `id` varchar(36) NOT NULL,
  `equ_detail_sta_id` varchar(36) DEFAULT NULL,
  `feeding_list_id` varchar(36) DEFAULT NULL,
  `feeding_num` int(11) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7489qpq24cethdyog3xpn0cys` (`equ_detail_sta_id`),
  KEY `FK66xl8ahpwctl9krn10sct2eb9` (`feeding_list_id`),
  KEY `FKjuhkcq1g2c519ba8u3a82coel` (`mat_info_id`),
  CONSTRAINT `FK66xl8ahpwctl9krn10sct2eb9` FOREIGN KEY (`feeding_list_id`) REFERENCES `feeding_list` (`id`),
  CONSTRAINT `FK7489qpq24cethdyog3xpn0cys` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`),
  CONSTRAINT `FKjuhkcq1g2c519ba8u3a82coel` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of feeding_detail
-- ----------------------------

-- ----------------------------
-- Table structure for feeding_list
-- ----------------------------
DROP TABLE IF EXISTS `feeding_list`;
CREATE TABLE `feeding_list` (
  `id` varchar(36) NOT NULL,
  `cabinet_id` varchar(36) DEFAULT NULL,
  `create_account_id` varchar(36) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `feeding_account_id` varchar(36) DEFAULT NULL,
  `feeding_name` varchar(50) DEFAULT NULL,
  `feeding_time` datetime DEFAULT NULL,
  `is_finish` varchar(10) DEFAULT NULL,
  `feed_type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1xq56i6qr15lfmy7qr68js0h8` (`cabinet_id`),
  CONSTRAINT `FK1xq56i6qr15lfmy7qr68js0h8` FOREIGN KEY (`cabinet_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of feeding_list
-- ----------------------------

-- ----------------------------
-- Table structure for inventory
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `equ_detail_sta_id` varchar(36) DEFAULT NULL,
  `inventory_num` int(11) DEFAULT NULL,
  `mat_info_id` varchar(255) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `owner_id` varchar(36) DEFAULT NULL,
  `storage_num` int(11) DEFAULT NULL,
  `sub_box_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1p9pojq9rrtchhjdic6ofdmw0` (`equ_detail_sta_id`),
  KEY `FKjupl1npotc7qjvjmvs834lkpw` (`mat_info_id`),
  KEY `FKsvwl1l9wmtlc40ju30pa10ia2` (`sub_box_id`),
  CONSTRAINT `FK1p9pojq9rrtchhjdic6ofdmw0` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`),
  CONSTRAINT `FKjupl1npotc7qjvjmvs834lkpw` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKsvwl1l9wmtlc40ju30pa10ia2` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of inventory
-- ----------------------------

-- ----------------------------
-- Table structure for lower_frame_query
-- ----------------------------
DROP TABLE IF EXISTS `lower_frame_query`;
CREATE TABLE `lower_frame_query` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `equ_detail_sta_id` varchar(36) DEFAULT NULL,
  `low_frame_num` int(11) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `owner_id` varchar(36) DEFAULT NULL,
  `sub_box_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfyhpdgd0x8uy2g6ld0eq2l5t2` (`equ_detail_sta_id`),
  KEY `FKenrfn54iitxoxtfv7mqbxcm9i` (`mat_info_id`),
  KEY `FK9f6er4k1jbgftrpxl5xohsv8k` (`sub_box_id`),
  CONSTRAINT `FK9f6er4k1jbgftrpxl5xohsv8k` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`),
  CONSTRAINT `FKenrfn54iitxoxtfv7mqbxcm9i` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKfyhpdgd0x8uy2g6ld0eq2l5t2` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lower_frame_query
-- ----------------------------

-- ----------------------------
-- Table structure for manufacturer
-- ----------------------------
DROP TABLE IF EXISTS `manufacturer`;
CREATE TABLE `manufacturer` (
  `id` varchar(36) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `contact` varchar(30) DEFAULT NULL,
  `manufacturer_name` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manufacturer
-- ----------------------------
INSERT INTO `manufacturer` VALUES ('001', '苏州', '陈磊', '阿诺', '13800000000', '', 'ENABLE');

-- ----------------------------
-- Table structure for manufacturer_custom
-- ----------------------------
DROP TABLE IF EXISTS `manufacturer_custom`;
CREATE TABLE `manufacturer_custom` (
  `id` varchar(36) NOT NULL,
  `contact_name` varchar(20) DEFAULT NULL,
  `contact_phone` varchar(30) DEFAULT NULL,
  `mail_address` varchar(200) DEFAULT NULL,
  `main_user` varchar(10) DEFAULT NULL,
  `manufacturer_id` varchar(36) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK77auw9hjdsriuo5cboejry1py` (`manufacturer_id`),
  CONSTRAINT `FK77auw9hjdsriuo5cboejry1py` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manufacturer_custom
-- ----------------------------

-- ----------------------------
-- Table structure for mat_association
-- ----------------------------
DROP TABLE IF EXISTS `mat_association`;
CREATE TABLE `mat_association` (
  `id` varchar(36) NOT NULL,
  `custom_id` varchar(36) DEFAULT NULL,
  `mat_equ_type_id` varchar(36) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `parts_id` varchar(36) DEFAULT NULL,
  `proc_info_id` varchar(36) DEFAULT NULL,
  `product_id` varchar(36) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4st6383s6tavdgvghyipyun7l` (`custom_id`),
  KEY `FKc7epugflvnypj8yfxcd52g4i1` (`mat_info_id`),
  KEY `FK4cthp9h6gfst5o9hlw0j2mt2` (`mat_equ_type_id`),
  KEY `FK34v3o3fa3tc4fol6g5aixru7v` (`parts_id`),
  KEY `FKr9e2mx4crdhctf8wjx7veabxq` (`proc_info_id`),
  KEY `FKcfghsayw6ob8ya2ijabaxxn1q` (`product_id`),
  CONSTRAINT `FK34v3o3fa3tc4fol6g5aixru7v` FOREIGN KEY (`parts_id`) REFERENCES `parts` (`id`),
  CONSTRAINT `FK4cthp9h6gfst5o9hlw0j2mt2` FOREIGN KEY (`mat_equ_type_id`) REFERENCES `mat_equ_type` (`id`),
  CONSTRAINT `FK4st6383s6tavdgvghyipyun7l` FOREIGN KEY (`custom_id`) REFERENCES `custom` (`id`),
  CONSTRAINT `FKc7epugflvnypj8yfxcd52g4i1` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKcfghsayw6ob8ya2ijabaxxn1q` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKr9e2mx4crdhctf8wjx7veabxq` FOREIGN KEY (`proc_info_id`) REFERENCES `proc_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_association
-- ----------------------------

-- ----------------------------
-- Table structure for mat_equ_info
-- ----------------------------
DROP TABLE IF EXISTS `mat_equ_info`;
CREATE TABLE `mat_equ_info` (
  `id` varchar(36) NOT NULL,
  `bar_code` varchar(30) DEFAULT NULL,
  `borrow_type` varchar(10) DEFAULT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `manufacturer_id` varchar(36) DEFAULT NULL,
  `mat_equ_name` varchar(50) DEFAULT NULL,
  `mat_equ_type_id` varchar(36) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `old_for_new` varchar(10) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `spec` varchar(50) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `store_price` float DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdrnv5fyunokp94tuon2ewhhig` (`manufacturer_id`),
  KEY `FKj5mju96lmdxmvpl6oanbgifet` (`mat_equ_type_id`),
  CONSTRAINT `FKdrnv5fyunokp94tuon2ewhhig` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`id`),
  CONSTRAINT `FKj5mju96lmdxmvpl6oanbgifet` FOREIGN KEY (`mat_equ_type_id`) REFERENCES `mat_equ_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_equ_info
-- ----------------------------
INSERT INTO `mat_equ_info` VALUES ('297e5a8667960b0701679698f7cb0012', '001', 'PACK', '阿诺', '7a1f325e-356c-467a-a385-fb411d45d52f.jpg', '001', '刀具柜001', '1001', '0', 'YES', '', '9*10', 'ENABLE', '0');

-- ----------------------------
-- Table structure for mat_equ_type
-- ----------------------------
DROP TABLE IF EXISTS `mat_equ_type`;
CREATE TABLE `mat_equ_type` (
  `id` varchar(36) NOT NULL,
  `mat_equ` varchar(10) DEFAULT NULL,
  `pack_unit_id` varchar(36) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `store_unit_id` varchar(36) DEFAULT NULL,
  `type_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmn3tejk3oauc4ggvqi38bxomt` (`pack_unit_id`),
  KEY `FKdpkqlmw91rlqtthcwlq2hi2c1` (`store_unit_id`),
  CONSTRAINT `FKdpkqlmw91rlqtthcwlq2hi2c1` FOREIGN KEY (`store_unit_id`) REFERENCES `unit` (`id`),
  CONSTRAINT `FKmn3tejk3oauc4ggvqi38bxomt` FOREIGN KEY (`pack_unit_id`) REFERENCES `unit` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_equ_type
-- ----------------------------
INSERT INTO `mat_equ_type` VALUES ('1001', 'STORAGE', '101', '', 'ENABLE', '101', '刀具柜');
INSERT INTO `mat_equ_type` VALUES ('1002', 'EQUIPMENT', '101', '', 'ENABLE', '101', '数控机床01');
INSERT INTO `mat_equ_type` VALUES ('1003', 'MATERIAL', '4028811e69e1b1bf0169e1ba0e7d0002', '', 'ENABLE', '103', '刀片');
INSERT INTO `mat_equ_type` VALUES ('1004', 'MATERIAL', '4028811e69e1b1bf0169e1ba0e7d0002', '', 'ENABLE', '103', '丝锥');
INSERT INTO `mat_equ_type` VALUES ('1005', 'MATERIAL', '4028811e69e1b1bf0169e1ba0e7d0002', '', 'ENABLE', '103', '钻头');
INSERT INTO `mat_equ_type` VALUES ('1006', 'MATERIAL', '4028811e69e1b1bf0169e1ba0e7d0002', '', 'ENABLE', '103', '铣刀');
INSERT INTO `mat_equ_type` VALUES ('1007', 'MATERIAL', '4028811e69e1b1bf0169e1ba0e7d0002', '', 'ENABLE', '103', '镗刀');
INSERT INTO `mat_equ_type` VALUES ('1008', 'MATERIAL', '4028811e69e1b1bf0169e1ba0e7d0002', '', 'ENABLE', '103', '铰刀');
INSERT INTO `mat_equ_type` VALUES ('1009', 'MATERIAL', '4028811e69e1b1bf0169e1ba0e7d0002', '', 'ENABLE', '103', '车刀');
INSERT INTO `mat_equ_type` VALUES ('1010', 'STORAGE', '101', '', 'ENABLE', '101', '暂存柜');

-- ----------------------------
-- Table structure for mat_return_back
-- ----------------------------
DROP TABLE IF EXISTS `mat_return_back`;
CREATE TABLE `mat_return_back` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `back_num` int(11) DEFAULT NULL,
  `broken_num` int(11) DEFAULT NULL,
  `continued_use_num` int(11) DEFAULT NULL,
  `grinding_num` int(11) DEFAULT NULL,
  `lose_num` int(11) DEFAULT NULL,
  `mat_use_bill_id` varchar(36) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `restitution_type_id` varchar(36) DEFAULT NULL,
  `wrongcollar_num` int(11) DEFAULT NULL,
  `bar_code` varchar(255) DEFAULT NULL,
  `audit_status` varchar(10) DEFAULT NULL,
  `confirm_date` datetime DEFAULT NULL,
  `confirm_mode` varchar(10) DEFAULT NULL,
  `confirm_user` varchar(255) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `is_return_back` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKi2xlwgrl6iax3hcptkiqjc5r9` (`mat_use_bill_id`),
  KEY `FK1i67g1i5xrsmy3vf8pf3a9in8` (`restitution_type_id`),
  CONSTRAINT `FK1i67g1i5xrsmy3vf8pf3a9in8` FOREIGN KEY (`restitution_type_id`) REFERENCES `restitution_type` (`id`),
  CONSTRAINT `FKi2xlwgrl6iax3hcptkiqjc5r9` FOREIGN KEY (`mat_use_bill_id`) REFERENCES `mat_use_bill` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_return_back
-- ----------------------------

-- ----------------------------
-- Table structure for mat_return_back_verify
-- ----------------------------
DROP TABLE IF EXISTS `mat_return_back_verify`;
CREATE TABLE `mat_return_back_verify` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `mat_returnback_id` varchar(36) DEFAULT NULL,
  `mode` varchar(10) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `num` int(11) NOT NULL,
  `audit_status` varchar(10) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKp60b7j4ttgm1q8ofydfjjkcfl` (`mat_returnback_id`),
  CONSTRAINT `FKp60b7j4ttgm1q8ofydfjjkcfl` FOREIGN KEY (`mat_returnback_id`) REFERENCES `mat_return_back` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_return_back_verify
-- ----------------------------

-- ----------------------------
-- Table structure for mat_use_bill
-- ----------------------------
DROP TABLE IF EXISTS `mat_use_bill`;
CREATE TABLE `mat_use_bill` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `borrow_num` int(11) DEFAULT NULL,
  `equ_detail_sta_id` varchar(36) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `store_price` float DEFAULT NULL,
  `receive_type` varchar(36) DEFAULT NULL,
  `receive_info` varchar(36) DEFAULT NULL,
  `spec` varchar(255) DEFAULT NULL,
  `bar_code` varchar(30) DEFAULT NULL,
  `mat_use_record_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7h7pw3110exn2ntpvuoodvql8` (`equ_detail_sta_id`),
  KEY `FK1ff0ftaawpgj5peyorse7w0dy` (`mat_info_id`),
  KEY `FKct9tw6m13cs21cgj4bnff3b1v` (`mat_use_record_id`),
  CONSTRAINT `FK1ff0ftaawpgj5peyorse7w0dy` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FK7h7pw3110exn2ntpvuoodvql8` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`),
  CONSTRAINT `FKct9tw6m13cs21cgj4bnff3b1v` FOREIGN KEY (`mat_use_record_id`) REFERENCES `mat_use_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_use_bill
-- ----------------------------

-- ----------------------------
-- Table structure for mat_use_record
-- ----------------------------
DROP TABLE IF EXISTS `mat_use_record`;
CREATE TABLE `mat_use_record` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `borrow_num` int(11) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `receive_info` varchar(36) DEFAULT NULL,
  `receive_type` varchar(255) DEFAULT NULL,
  `store_price` float DEFAULT NULL,
  `borrow_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8lhxypjsni8dmlp23223on4l5` (`mat_info_id`),
  CONSTRAINT `FK8lhxypjsni8dmlp23223on4l5` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_use_record
-- ----------------------------

-- ----------------------------
-- Table structure for parts
-- ----------------------------
DROP TABLE IF EXISTS `parts`;
CREATE TABLE `parts` (
  `id` varchar(36) NOT NULL,
  `parts_name` varchar(200) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of parts
-- ----------------------------
INSERT INTO `parts` VALUES ('101', '零件A', 'ENABLE');

-- ----------------------------
-- Table structure for phone_cart
-- ----------------------------
DROP TABLE IF EXISTS `phone_cart`;
CREATE TABLE `phone_cart` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `mat_id` varchar(36) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc5tu3a93hn4b9o3rs1dot3ram` (`mat_id`),
  CONSTRAINT `FKc5tu3a93hn4b9o3rs1dot3ram` FOREIGN KEY (`mat_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_cart
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order
-- ----------------------------
DROP TABLE IF EXISTS `phone_order`;
CREATE TABLE `phone_order` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `order_code` varchar(255) DEFAULT NULL,
  `order_created_time` datetime DEFAULT NULL,
  `order_id` varchar(36) DEFAULT NULL,
  `order_text` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `phone_order_detail`;
CREATE TABLE `phone_order_detail` (
  `id` varchar(36) NOT NULL,
  `equ_setting_id` varchar(36) DEFAULT NULL,
  `col_no` int(11) NOT NULL,
  `equ_detail_sta_id` varchar(255) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `num` int(11) NOT NULL,
  `phone_order_id` varchar(36) DEFAULT NULL,
  `row_no` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbll4oiev7sjkfa7387oxsuvki` (`equ_setting_id`),
  KEY `FKv734vdumispicl17ikbl1oa` (`mat_info_id`),
  KEY `FKtrrjcs8kdf2si3xhqupsl3rce` (`phone_order_id`),
  CONSTRAINT `FKbll4oiev7sjkfa7387oxsuvki` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`),
  CONSTRAINT `FKtrrjcs8kdf2si3xhqupsl3rce` FOREIGN KEY (`phone_order_id`) REFERENCES `phone_order` (`id`),
  CONSTRAINT `FKv734vdumispicl17ikbl1oa` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order_mat
-- ----------------------------
DROP TABLE IF EXISTS `phone_order_mat`;
CREATE TABLE `phone_order_mat` (
  `id` varchar(36) NOT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `num` int(11) NOT NULL,
  `order_mat_code` longtext,
  `order_mat_text` longtext,
  `phone_order_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK48vk6sjwc85k85y42gnwminyq` (`mat_info_id`),
  KEY `FK3tpbo4m7relunum3llgaeokls` (`phone_order_id`),
  CONSTRAINT `FK3tpbo4m7relunum3llgaeokls` FOREIGN KEY (`phone_order_id`) REFERENCES `phone_order` (`id`),
  CONSTRAINT `FK48vk6sjwc85k85y42gnwminyq` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order_mat
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order_mat_detail
-- ----------------------------
DROP TABLE IF EXISTS `phone_order_mat_detail`;
CREATE TABLE `phone_order_mat_detail` (
  `id` varchar(36) NOT NULL,
  `equ_setting_id` varchar(36) DEFAULT NULL,
  `col_no` int(11) NOT NULL,
  `equ_detail_sta_id` varchar(255) DEFAULT NULL,
  `num` int(11) NOT NULL,
  `phone_order_mat_id` varchar(36) DEFAULT NULL,
  `row_no` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq9supgrqfgeq0eod2qqmxcwj7` (`equ_setting_id`),
  KEY `FKbj2grsha7nh8tjq13cl439a3f` (`phone_order_mat_id`),
  KEY `FKk46yod7wsuuntw8ychxbahdhk` (`equ_detail_sta_id`),
  CONSTRAINT `FKbj2grsha7nh8tjq13cl439a3f` FOREIGN KEY (`phone_order_mat_id`) REFERENCES `phone_order_mat` (`id`),
  CONSTRAINT `FKk46yod7wsuuntw8ychxbahdhk` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`),
  CONSTRAINT `FKq9supgrqfgeq0eod2qqmxcwj7` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order_mat_detail
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order_sta
-- ----------------------------
DROP TABLE IF EXISTS `phone_order_sta`;
CREATE TABLE `phone_order_sta` (
  `id` varchar(36) NOT NULL,
  `op_date` datetime DEFAULT NULL,
  `phone_order_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKso5uj1enxwh2jjqjqk94ya4w4` (`phone_order_id`),
  CONSTRAINT `FKso5uj1enxwh2jjqjqk94ya4w4` FOREIGN KEY (`phone_order_id`) REFERENCES `phone_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order_sta
-- ----------------------------

-- ----------------------------
-- Table structure for plc_setting
-- ----------------------------
DROP TABLE IF EXISTS `plc_setting`;
CREATE TABLE `plc_setting` (
  `id` varchar(36) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `default_value` varchar(100) DEFAULT NULL,
  `is_default` varchar(10) DEFAULT NULL,
  `plc_name` varchar(200) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plc_setting
-- ----------------------------
INSERT INTO `plc_setting` VALUES ('1001', '4096', '10', 'YES', 'F1', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1002', '6099', '20', 'YES', 'F2', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1003', '6101', '30', 'YES', 'F3', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1004', '6103', '40', 'YES', 'F4', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1005', '6105', '50', 'YES', 'F5', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1006', '6107', '60', 'YES', 'F6', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1007', '6109', '70', 'YES', 'F7', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1008', '6111', '80', 'YES', 'F8', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1009', '6113', '90', 'YES', 'F9', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1010', '6115', '100', 'YES', 'F10', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1011', '6117', '35', 'YES', '取料口位置', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1012', '6119', '30', 'YES', '运行速度', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1013', '6121', '35', 'NO', '实时位置', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1014', '6123', '0', 'NO', '偏移量', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1015', '4187', null, 'NO', '目标层', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1016', '2349', null, 'NO', '点动上升', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1017', '2350', null, 'NO', '点动下降', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1018', '2351', null, 'NO', '取料口开门', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1019', '2352', null, 'NO', '取料口关门', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1020', '2353', null, 'NO', '复位料斗', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1021', '2354', null, 'NO', '已到达目标层', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1022', '2355', null, 'NO', '取料口关门检测', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1023', '2356', null, 'NO', '故障标志', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1024', '2357', null, 'NO', '上限位报警', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1025', '2358', null, 'NO', '下限位报警', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1026', '2359', null, 'NO', '伺服驱动器报警', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1027', '2360', null, 'NO', '开门失败', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1028', '2361', null, 'NO', '关门失败', '线圈', 'ENABLE');

-- ----------------------------
-- Table structure for proc_info
-- ----------------------------
DROP TABLE IF EXISTS `proc_info`;
CREATE TABLE `proc_info` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  `op_date` date DEFAULT NULL,
  `proc_name` varchar(30) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of proc_info
-- ----------------------------
INSERT INTO `proc_info` VALUES ('101', '1', '2019-04-02', '工序A', '', 'ENABLE');

-- ----------------------------
-- Table structure for proc_mat_ref
-- ----------------------------
DROP TABLE IF EXISTS `proc_mat_ref`;
CREATE TABLE `proc_mat_ref` (
  `id` varchar(36) NOT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `mat_num` int(11) DEFAULT NULL,
  `proc_info_id` varchar(36) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKf6jso4qklseth2300o1gkj3yp` (`mat_info_id`),
  KEY `FKevjwgkjly74gjf8hc1p7kod9q` (`proc_info_id`),
  CONSTRAINT `FKevjwgkjly74gjf8hc1p7kod9q` FOREIGN KEY (`proc_info_id`) REFERENCES `proc_info` (`id`),
  CONSTRAINT `FKf6jso4qklseth2300o1gkj3yp` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of proc_mat_ref
-- ----------------------------

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` varchar(36) NOT NULL,
  `product_name` varchar(200) DEFAULT NULL,
  `product_size` varchar(200) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('101', '产品A', '', 'ENABLE');
INSERT INTO `product` VALUES ('102', '产品B', '', 'ENABLE');

-- ----------------------------
-- Table structure for restitution_type
-- ----------------------------
DROP TABLE IF EXISTS `restitution_type`;
CREATE TABLE `restitution_type` (
  `id` varchar(36) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `rest_name` varchar(30) DEFAULT NULL,
  `return_back_type` varchar(10) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of restitution_type
-- ----------------------------

-- ----------------------------
-- Table structure for shift
-- ----------------------------
DROP TABLE IF EXISTS `shift`;
CREATE TABLE `shift` (
  `id` varchar(36) NOT NULL,
  `end_time` varchar(255) DEFAULT NULL,
  `shift_name` varchar(36) DEFAULT NULL,
  `start_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shift
-- ----------------------------

-- ----------------------------
-- Table structure for staff_quota
-- ----------------------------
DROP TABLE IF EXISTS `staff_quota`;
CREATE TABLE `staff_quota` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `quota_num` int(11) NOT NULL,
  `staff_shift_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKggt5ifd9wey2yww9bebrjkhum` (`mat_info_id`),
  KEY `FKl0tl6x093qofmnyi5vibis40d` (`staff_shift_id`),
  CONSTRAINT `FKggt5ifd9wey2yww9bebrjkhum` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKl0tl6x093qofmnyi5vibis40d` FOREIGN KEY (`staff_shift_id`) REFERENCES `staff_shift` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of staff_quota
-- ----------------------------

-- ----------------------------
-- Table structure for staff_shift
-- ----------------------------
DROP TABLE IF EXISTS `staff_shift`;
CREATE TABLE `staff_shift` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `shift_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK27us0macgugi6in0q06j0eaht` (`shift_id`),
  CONSTRAINT `FK27us0macgugi6in0q06j0eaht` FOREIGN KEY (`shift_id`) REFERENCES `shift` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of staff_shift
-- ----------------------------

-- ----------------------------
-- Table structure for statement
-- ----------------------------
DROP TABLE IF EXISTS `statement`;
CREATE TABLE `statement` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `balance` int(11) DEFAULT NULL,
  `cost` float DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `outer_num` int(11) DEFAULT NULL,
  `price` float DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `temp_num` int(11) DEFAULT NULL,
  `inventory_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfbd4rymtj8ubwfkx0idrt66pu` (`mat_info_id`),
  CONSTRAINT `FKfbd4rymtj8ubwfkx0idrt66pu` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of statement
-- ----------------------------

-- ----------------------------
-- Table structure for sub_box
-- ----------------------------
DROP TABLE IF EXISTS `sub_box`;
CREATE TABLE `sub_box` (
  `id` varchar(36) NOT NULL,
  `box_index` int(11) NOT NULL,
  `cabinet_sta` varchar(10) DEFAULT NULL,
  `col_no` int(11) NOT NULL,
  `row_no` int(11) NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  `sub_cabinet_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4nldkopii8rn0okl186b8vfc4` (`sub_cabinet_id`),
  CONSTRAINT `FK4nldkopii8rn0okl186b8vfc4` FOREIGN KEY (`sub_cabinet_id`) REFERENCES `sub_cabinet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_box
-- ----------------------------
INSERT INTO `sub_box` VALUES ('1', '1', 'NORMAL', '1', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('10', '10', 'NORMAL', '5', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('100', '100', 'NORMAL', '5', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('11', '11', 'NORMAL', '1', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('12', '12', 'NORMAL', '2', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('13', '13', 'NORMAL', '3', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('14', '14', 'NORMAL', '4', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('15', '15', 'NORMAL', '5', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('16', '16', 'NORMAL', '1', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('17', '17', 'NORMAL', '2', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('18', '18', 'NORMAL', '3', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('19', '19', 'NORMAL', '4', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('2', '2', 'NORMAL', '2', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('20', '20', 'NORMAL', '5', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('21', '21', 'NORMAL', '1', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('22', '22', 'NORMAL', '2', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('23', '23', 'NORMAL', '3', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('24', '24', 'NORMAL', '4', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('25', '25', 'NORMAL', '5', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('26', '26', 'NORMAL', '1', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('27', '27', 'NORMAL', '2', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('28', '28', 'NORMAL', '3', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('29', '29', 'NORMAL', '4', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('3', '3', 'NORMAL', '3', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('30', '30', 'NORMAL', '5', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('31', '31', 'NORMAL', '1', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('32', '32', 'NORMAL', '2', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('33', '33', 'NORMAL', '3', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('34', '34', 'NORMAL', '4', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('35', '35', 'NORMAL', '5', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('36', '36', 'NORMAL', '1', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('37', '37', 'NORMAL', '2', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('38', '38', 'NORMAL', '3', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('39', '39', 'NORMAL', '4', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('4', '4', 'NORMAL', '4', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('40', '40', 'NORMAL', '5', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('41', '41', 'NORMAL', '1', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('42', '42', 'NORMAL', '2', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('43', '43', 'NORMAL', '3', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('44', '44', 'NORMAL', '4', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('45', '45', 'NORMAL', '5', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('46', '46', 'NORMAL', '1', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('47', '47', 'NORMAL', '2', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('48', '48', 'NORMAL', '3', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('49', '49', 'NORMAL', '4', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('5', '5', 'NORMAL', '5', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('50', '50', 'NORMAL', '5', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('51', '51', 'NORMAL', '1', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('52', '52', 'NORMAL', '2', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('53', '53', 'NORMAL', '3', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('54', '54', 'NORMAL', '4', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('55', '55', 'NORMAL', '5', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('56', '56', 'NORMAL', '1', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('57', '57', 'NORMAL', '2', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('58', '58', 'NORMAL', '3', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('59', '59', 'NORMAL', '4', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('6', '6', 'NORMAL', '1', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('60', '60', 'NORMAL', '5', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('61', '61', 'NORMAL', '1', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('62', '62', 'NORMAL', '2', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('63', '63', 'NORMAL', '3', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('64', '64', 'NORMAL', '4', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('65', '65', 'NORMAL', '5', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('66', '66', 'NORMAL', '1', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('67', '67', 'NORMAL', '2', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('68', '68', 'NORMAL', '3', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('69', '69', 'NORMAL', '4', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('7', '7', 'NORMAL', '2', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('70', '70', 'NORMAL', '5', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('71', '71', 'NORMAL', '1', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('72', '72', 'NORMAL', '2', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('73', '73', 'NORMAL', '3', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('74', '74', 'NORMAL', '4', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('75', '75', 'NORMAL', '5', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('76', '76', 'NORMAL', '1', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('77', '77', 'NORMAL', '2', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('78', '78', 'NORMAL', '3', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('79', '79', 'NORMAL', '4', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('8', '8', 'NORMAL', '3', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('80', '80', 'NORMAL', '5', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('81', '81', 'NORMAL', '1', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('82', '82', 'NORMAL', '2', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('83', '83', 'NORMAL', '3', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('84', '84', 'NORMAL', '4', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('85', '85', 'NORMAL', '5', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('86', '86', 'NORMAL', '1', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('87', '87', 'NORMAL', '2', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('88', '88', 'NORMAL', '3', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('89', '89', 'NORMAL', '4', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('9', '9', 'NORMAL', '4', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('90', '90', 'NORMAL', '5', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('91', '91', 'NORMAL', '1', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('92', '92', 'NORMAL', '2', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('93', '93', 'NORMAL', '3', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('94', '94', 'NORMAL', '4', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('95', '95', 'NORMAL', '5', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('96', '96', 'NORMAL', '1', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('97', '97', 'NORMAL', '2', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('98', '98', 'NORMAL', '3', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');
INSERT INTO `sub_box` VALUES ('99', '99', 'NORMAL', '4', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');

-- ----------------------------
-- Table structure for sub_box_account_ref
-- ----------------------------
DROP TABLE IF EXISTS `sub_box_account_ref`;
CREATE TABLE `sub_box_account_ref` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `sub_box_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK99c4motn1ccsx5rxkcak0kx5a` (`sub_box_id`),
  CONSTRAINT `FK99c4motn1ccsx5rxkcak0kx5a` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_box_account_ref
-- ----------------------------

-- ----------------------------
-- Table structure for sub_box_num_setting
-- ----------------------------
DROP TABLE IF EXISTS `sub_box_num_setting`;
CREATE TABLE `sub_box_num_setting` (
  `id` varchar(36) NOT NULL,
  `max_num` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_box_num_setting
-- ----------------------------
INSERT INTO `sub_box_num_setting` VALUES ('1', '3');

-- ----------------------------
-- Table structure for sub_cabinet
-- ----------------------------
DROP TABLE IF EXISTS `sub_cabinet`;
CREATE TABLE `sub_cabinet` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `asset_no` varchar(30) DEFAULT NULL,
  `cabinet_name` varchar(50) DEFAULT NULL,
  `col_num` int(11) DEFAULT NULL,
  `comm` varchar(50) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `op_date` date DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `row_num` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_cabinet
-- ----------------------------
INSERT INTO `sub_cabinet` VALUES ('402882ba67e3cfa20167e3d7d7a50000', '1', '001', '暂存柜', '5', '502', '192.168.2.58', '2019-04-02', '2018-12-25', '20', 'ENABLE');

-- ----------------------------
-- Table structure for sub_cabinet_bill
-- ----------------------------
DROP TABLE IF EXISTS `sub_cabinet_bill`;
CREATE TABLE `sub_cabinet_bill` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) DEFAULT NULL,
  `in_or_out` varchar(10) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `sub_box_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnw7myb4ggdhpyooj1hokytbjo` (`mat_info_id`),
  KEY `FKgkhxwp3erkf2d3m910a4eok3i` (`sub_box_id`),
  CONSTRAINT `FKgkhxwp3erkf2d3m910a4eok3i` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`),
  CONSTRAINT `FKnw7myb4ggdhpyooj1hokytbjo` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_cabinet_bill
-- ----------------------------

-- ----------------------------
-- Table structure for sub_cabinet_detail
-- ----------------------------
DROP TABLE IF EXISTS `sub_cabinet_detail`;
CREATE TABLE `sub_cabinet_detail` (
  `id` varchar(36) NOT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `sub_box_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6tkgmbaa1qbido486097p6y23` (`mat_info_id`),
  KEY `FK81btjqt5vegupyi91e2ae95qp` (`sub_box_id`),
  CONSTRAINT `FK6tkgmbaa1qbido486097p6y23` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FK81btjqt5vegupyi91e2ae95qp` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_cabinet_detail
-- ----------------------------

-- ----------------------------
-- Table structure for unit
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit` (
  `id` varchar(36) NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  `unit_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of unit
-- ----------------------------
INSERT INTO `unit` VALUES ('101', 'ENABLE', '台');
INSERT INTO `unit` VALUES ('102', 'ENABLE', '箱');
INSERT INTO `unit` VALUES ('103', 'ENABLE', '盒');
INSERT INTO `unit` VALUES ('104', 'ENABLE', '支');
INSERT INTO `unit` VALUES ('105', 'ENABLE', '把');
INSERT INTO `unit` VALUES ('106', 'ENABLE', '件');
INSERT INTO `unit` VALUES ('107', 'ENABLE', '片');
INSERT INTO `unit` VALUES ('4028811e69e1b1bf0169e1ba0e7d0002', 'ENABLE', '支/盒');
INSERT INTO `unit` VALUES ('402881ba6bde7a18016bde8128d40000', 'ENABLE', '片/盒');

-- ----------------------------
-- Table structure for warehouse
-- ----------------------------
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `id` varchar(36) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `warehouse_name` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse
-- ----------------------------

-- ----------------------------
-- Table structure for warehouse_bill
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_bill`;
CREATE TABLE `warehouse_bill` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(255) DEFAULT NULL,
  `in_out_sta` varchar(10) DEFAULT NULL,
  `mat_info_id` varchar(36) DEFAULT NULL,
  `op_date` datetime DEFAULT NULL,
  `warehouse_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgu64nip0dppy39l34iheqdoub` (`mat_info_id`),
  KEY `FKmtmqe03nif79bpcxsw0kb3pts` (`warehouse_id`),
  CONSTRAINT `FKgu64nip0dppy39l34iheqdoub` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKmtmqe03nif79bpcxsw0kb3pts` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse_bill
-- ----------------------------

-- ----------------------------
-- View structure for view_dept
-- ----------------------------
DROP VIEW IF EXISTS `view_dept`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `view_dept` AS select `d`.`id` AS `DEPT_ID`,`d`.`p_id` AS `DEPT_P_ID`,`d`.`dept_name` AS `DEPT_NAME`,`d`.`status` AS `DEPT_STATUS` from `dosth`.`dept` `d` ;

-- ----------------------------
-- View structure for view_user
-- ----------------------------
DROP VIEW IF EXISTS `view_user`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`%` SQL SECURITY DEFINER VIEW `view_user` AS select `a`.`id` AS `ACCOUNT_ID`,`u`.`dept_id` AS `DEPT_ID`,`u`.`name` AS `USER_NAME`,`a`.`login_name` AS `LOGIN_NAME`,'' AS `USER_NO`,`u`.`start_time` AS `START_TIME`,`u`.`end_time` AS `END_TIME`,`u`.`limit_sum_num` AS `LIMIT_SUM_NUM`,`u`.`not_return_limit_num` AS `NOT_RETURN_LIMIT_NUM` from (`dosth`.`account` `a` left join `dosth`.`users` `u` on((`a`.`id` = `u`.`account_id`))) where (`a`.`status` = 'OK') ;
