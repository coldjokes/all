-- ----------------------------
-- Table structure for bs_setting
-- ----------------------------
DROP TABLE IF EXISTS `bs_setting`;
CREATE TABLE `bs_setting` (
  `name` varchar(256) DEFAULT NULL COMMENT '健',
  `value` varchar(256) DEFAULT NULL COMMENT '值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统参数设置';

-- ----------------------------
-- Records of bs_setting
-- ----------------------------
INSERT INTO `bs_setting` VALUES ('faceStatus', 'OFF');
INSERT INTO `bs_setting` VALUES ('materialCount', 'ON');
INSERT INTO `bs_setting` VALUES ('multiMaterialOneCell', 'OFF');
