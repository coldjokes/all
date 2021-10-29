-- ----------------------------
-- 默认账户
-- ----------------------------
INSERT INTO `bs_user` VALUES (replace(uuid(),"-",""), 'superadmin', '96e79218965eb72c92a549dd5a330112', '1', '0405200482',null, '超级管理员', '1', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00', null);
INSERT INTO `bs_user` VALUES (replace(uuid(),"-",""), 'admin', '96e79218965eb72c92a549dd5a330112', '2', '' ,null, '管理员', '1', '1', '2019-01-01 00:00:00', '2019-01-01 00:00:00', null);

-- ----------------------------
-- 物料类别添加默认全部的分类
-- ----------------------------
INSERT INTO `bs_material_category` (`id`, `text`, `p_id`, `create_time`, `update_time`, `delete_time`) VALUES ('-1', '全部', NULL, '2019-01-01 00:00:00', NULL, NULL);
