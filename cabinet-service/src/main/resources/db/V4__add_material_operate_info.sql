-- 添加物料表相关字段
ALTER TABLE bs_material ADD COLUMN last_stock_operate_user_id VARCHAR(32) COMMENT '最后库存操作人id' AFTER remark;
ALTER TABLE bs_material ADD COLUMN last_stock_operate_user_fullname VARCHAR(64) COMMENT '最后库存操作人姓名' AFTER last_stock_operate_user_id;
ALTER TABLE bs_material ADD COLUMN last_stock_operate_time timestamp NULL COMMENT '最后库存操作时间' AFTER last_stock_operate_user_fullname;