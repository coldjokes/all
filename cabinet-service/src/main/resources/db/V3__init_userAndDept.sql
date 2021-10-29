-- -- ----------------------------
-- Table structure for bs_dept`
-- ----------------------------
DROP TABLE IF EXISTS `bs_dept`;
CREATE TABLE `bs_dept` (
  `id` varchar(255) NOT NULL,
  `text` varchar(255) DEFAULT NULL COMMENT '部门名称',
  `p_id` varchar(255) DEFAULT NULL COMMENT '上级ID',
  `type` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '最近修改时间',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- 部门添加默认全部的部门
-- ----------------------------
INSERT INTO `bs_dept` (`id`, `text`, `p_id`,`type`, `create_time`, `update_time`, `delete_time`) VALUES ('-1', '全部', NULL,NULL, '2019-01-01 00:00:00', NULL, NULL);

-- -- ----------------------------
-- Table structure for `bs_dept_map`
-- ----------------------------
DROP TABLE IF EXISTS `bs_dept_map`;
CREATE TABLE `bs_dept_map` (
  `id` varchar(255) NOT NULL,
  `dept_id` varchar(255) DEFAULT NULL COMMENT '部门ID',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 用户表添加部门
-- ALTER TABLE bs_user ADD COLUMN dept_id varchar(255) default null comment '部门ID' after fullname;

-- -- ----------------------------
-- Table structure for `bs_user_material_map`
-- ----------------------------
DROP TABLE IF EXISTS `bs_user_material_map`;
CREATE TABLE `bs_user_material_map` (
  `id` varchar(32) NOT NULL,
  `select_id` varchar(255) DEFAULT NULL COMMENT 'userId/deptId',
  `value` varchar(255) DEFAULT NULL COMMENT '物料ID',
  `type` enum('0','1') DEFAULT '0' COMMENT 'key值为user/dept:''0'':dept,''1'':user',
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 人员添加人脸特征
ALTER TABLE bs_user ADD COLUMN face_feature longblob COMMENT '人脸特征' AFTER ic_card;
