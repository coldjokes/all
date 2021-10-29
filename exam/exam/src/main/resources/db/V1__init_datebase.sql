
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for exam_log
-- ----------------------------
DROP TABLE IF EXISTS `exam_log`;
CREATE TABLE `exam_log` (
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
-- Table structure for exam_setting
-- ----------------------------
DROP TABLE IF EXISTS `exam_setting`;
CREATE TABLE `exam_setting` (
  `name` varchar(256) DEFAULT NULL COMMENT '健',
  `value` varchar(256) DEFAULT NULL COMMENT '值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数设置';

-- ----------------------------
-- Records of exam_setting
-- ----------------------------
INSERT INTO `exam_setting` VALUES ('faceStatus', 'OFF');
INSERT INTO `exam_setting` VALUES ('materialCount', 'OFF');
INSERT INTO `exam_setting` VALUES ('multiMaterialOneCell', 'OFF');

-- ----------------------------
-- Table structure for exam_user
-- ----------------------------
DROP TABLE IF EXISTS `exam_user`;
CREATE TABLE `exam_user` (
  `id` varchar(32) DEFAULT NULL COMMENT 'id',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL,
  `role` int(1) DEFAULT NULL COMMENT '角色',
  `ic_card` varchar(64) DEFAULT NULL COMMENT 'ic卡',
  `face_feature` longblob COMMENT '人脸特征',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `fullname` varchar(64) DEFAULT NULL COMMENT '姓名',
  `source` int(1) DEFAULT NULL COMMENT '数据来源',
  `status` int(1) DEFAULT NULL COMMENT '账户状态 1:启用 2:禁用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `delete_time` timestamp NULL DEFAULT NULL COMMENT '删除时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户基础信息表';

-- ----------------------------
-- Records of exam_user
-- ----------------------------
INSERT INTO `exam_user` VALUES ('3287fcc4388611eca80f58a02305fa4a', 'superadmin', '96e79218965eb72c92a549dd5a330112', '1', '0405200482', null, 'dduuuuuu77mmmmyy@163.com', '超级管理员', '1', '1', '2019-01-01 00:00:00', '2021-10-29 18:16:37', null);
INSERT INTO `exam_user` VALUES ('32882591388611eca80f58a02305fa4a', 'admin', '96e79218965eb72c92a549dd5a330112', '2', '', null, null, '管理员', '1', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00', null);
