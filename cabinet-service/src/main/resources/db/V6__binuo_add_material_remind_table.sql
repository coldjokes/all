DROP TABLE IF EXISTS `bs_material_remind`;
CREATE TABLE `bs_material_remind` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `material_id` varchar(32) DEFAULT NULL COMMENT '物料id',
  `name` varchar(1024) DEFAULT NULL COMMENT '物料名称',
  `no` varchar(1024) DEFAULT NULL COMMENT '物料编号',
  `spec` varchar(1024) DEFAULT NULL COMMENT '物料规格',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `created_user_fullname` varchar(255) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='库存提醒表';
