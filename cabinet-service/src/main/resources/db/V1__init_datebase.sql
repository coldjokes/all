/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50562
Source Host           : localhost:3306
Source Database       : bs_cabinet_ahno

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2020-09-09 08:40:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for bs_cabinet
-- ----------------------------
DROP TABLE IF EXISTS `bs_cabinet`;
CREATE TABLE `bs_cabinet` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `name` varchar(64) DEFAULT NULL COMMENT '柜体名称',
  `computer_id` varchar(32) DEFAULT NULL COMMENT '主机id',
  `computer_name` varchar(64) DEFAULT NULL COMMENT '主机名称',
  `computer_port` varchar(64) DEFAULT NULL COMMENT '柜体类型 1：主柜 2 副柜',
  `computer_baud_rate` int(5) DEFAULT NULL COMMENT '锁控板栈号',
  `sort` int(3) DEFAULT NULL COMMENT '序号',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `delete_time` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='柜体';

-- ----------------------------
-- Table structure for bs_cabinet_cell
-- ----------------------------
DROP TABLE IF EXISTS `bs_cabinet_cell`;
CREATE TABLE `bs_cabinet_cell` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `cabinet_id` varchar(32) DEFAULT NULL COMMENT '柜体id',
  `row_id` varchar(32) DEFAULT NULL COMMENT '列id',
  `stack` int(3) DEFAULT NULL COMMENT '栈号',
  `name` varchar(64) DEFAULT NULL COMMENT '显示名称',
  `pin` int(5) DEFAULT NULL COMMENT '针脚地址',
  `sort` int(3) DEFAULT NULL COMMENT '序号',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='格口';

-- ----------------------------
-- Table structure for bs_cabinet_row
-- ----------------------------
DROP TABLE IF EXISTS `bs_cabinet_row`;
CREATE TABLE `bs_cabinet_row` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `cabinet_id` varchar(32) DEFAULT NULL COMMENT '柜子id',
  `name` varchar(64) DEFAULT NULL COMMENT '行名称',
  `sort` int(3) DEFAULT NULL COMMENT '序号',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='行';

-- ----------------------------
-- Table structure for bs_computer
-- ----------------------------
DROP TABLE IF EXISTS `bs_computer`;
CREATE TABLE `bs_computer` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `name` varchar(64) DEFAULT NULL COMMENT '主机名称',
  `identify_code` varchar(64) DEFAULT NULL COMMENT '识别码',
  `cell_prefix` varchar(64) DEFAULT NULL COMMENT '格口前缀',
  `sort` int(3) DEFAULT NULL COMMENT '序号',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `delete_time` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='主机';

-- ----------------------------
-- Table structure for bs_log
-- ----------------------------
DROP TABLE IF EXISTS `bs_log`;
CREATE TABLE `bs_log` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `log_type` int(1) DEFAULT NULL COMMENT '用户具体操作类型',
  `business` varchar(64) DEFAULT NULL COMMENT '业务名称',
  `params` varchar(2000) DEFAULT NULL COMMENT '相关参数',
  `remark` varchar(2000) DEFAULT NULL COMMENT '备注信息',
  `ip_address` varchar(64) DEFAULT NULL COMMENT 'ip地址',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日志表';

-- ----------------------------
-- Table structure for bs_material
-- ----------------------------
DROP TABLE IF EXISTS `bs_material`;
CREATE TABLE `bs_material` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `name` varchar(1024) DEFAULT NULL COMMENT '物料名称',
  `no` varchar(1024) DEFAULT NULL COMMENT '物料编号',
  `spec` varchar(1024) DEFAULT NULL COMMENT '物料规格',
  `warn_val` int(2) DEFAULT NULL COMMENT '预警值',
  `source` int(1) DEFAULT NULL COMMENT '数据来源',
  `picture` varchar(2000) DEFAULT NULL COMMENT '图片',
  `blueprint` varchar(2000) DEFAULT NULL COMMENT '图纸',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `delete_time` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='物料基础信息表';

-- ----------------------------
-- Table structure for bs_material_bill
-- ----------------------------
DROP TABLE IF EXISTS `bs_material_bill`;
CREATE TABLE `bs_material_bill` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `material_id` varchar(32) DEFAULT NULL COMMENT '物料id',
  `material_name` varchar(1024) DEFAULT NULL COMMENT '物料名称',
  `material_no` varchar(1024) DEFAULT NULL COMMENT '物料编号',
  `material_spec` varchar(1024) DEFAULT NULL COMMENT '物料规格',
  `material_picture` varchar(2000) DEFAULT NULL COMMENT '物料图片',
  `material_remark` varchar(1024) DEFAULT NULL COMMENT '物料备注',
  `amount_start` int(5) DEFAULT NULL COMMENT '起始数量',
  `amount_diff` int(5) DEFAULT NULL COMMENT '数量差',
  `amount_end` int(5) DEFAULT NULL COMMENT '结束数量',
  `computer_id` varchar(32) DEFAULT NULL COMMENT '主机id',
  `computer_name` varchar(64) DEFAULT NULL COMMENT '主机名称',
  `cabinet_id` varchar(32) DEFAULT NULL COMMENT '柜子id',
  `cabinet_name` varchar(64) DEFAULT NULL COMMENT '设备名称',
  `cell_id` varchar(32) DEFAULT NULL COMMENT '格口id',
  `cell_name` varchar(64) DEFAULT NULL COMMENT '格口名称',
  `operate_type` int(11) DEFAULT NULL COMMENT '操作类型  1:存入 2:领取',
  `operate_name` varchar(64) DEFAULT NULL COMMENT '操作名',
  `user_id` varchar(32) DEFAULT NULL COMMENT '操作人id',
  `user_fullname` varchar(64) DEFAULT NULL COMMENT '用户姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='物料流水表';

-- ----------------------------
-- Table structure for bs_material_category
-- ----------------------------
DROP TABLE IF EXISTS `bs_material_category`;
CREATE TABLE `bs_material_category` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `text` varchar(512) DEFAULT NULL COMMENT '名称',
  `p_id` varchar(32) DEFAULT NULL COMMENT '父id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `delete_time` timestamp NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='物料类别表';

-- ----------------------------
-- Table structure for bs_material_category_map
-- ----------------------------
DROP TABLE IF EXISTS `bs_material_category_map`;
CREATE TABLE `bs_material_category_map` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `category_id` varchar(32) DEFAULT NULL COMMENT '物料类别id',
  `material_id` varchar(32) DEFAULT NULL COMMENT '物料id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='类别类别映射表';

-- ----------------------------
-- Table structure for bs_stock
-- ----------------------------
DROP TABLE IF EXISTS `bs_stock`;
CREATE TABLE `bs_stock` (
  `id` varchar(32) DEFAULT NULL COMMENT 'id',
  `computer_id` varchar(32) DEFAULT NULL COMMENT '主机id',
  `cabinet_id` varchar(32) DEFAULT NULL COMMENT '设备id',
  `cabinet_cell_id` varchar(32) DEFAULT NULL COMMENT '设备方格id',
  `material_id` varchar(32) DEFAULT NULL COMMENT '物料',
  `amount` int(8) DEFAULT NULL COMMENT '数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='物料库存表';;

-- ----------------------------
-- Table structure for bs_user
-- ----------------------------
DROP TABLE IF EXISTS `bs_user`;
CREATE TABLE `bs_user` (
  `id` varchar(32) DEFAULT NULL COMMENT 'id',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL,
  `role` int(1) DEFAULT NULL COMMENT '角色',
  `ic_card` varchar(64) DEFAULT NULL COMMENT 'ic卡',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `fullname` varchar(64) DEFAULT NULL COMMENT '姓名',
  `source` int(1) DEFAULT NULL COMMENT '数据来源',
  `status` int(1) DEFAULT NULL COMMENT '账户状态 1:启用 2:禁用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `delete_time` timestamp NULL DEFAULT NULL COMMENT '删除时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户基础信息表';

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;