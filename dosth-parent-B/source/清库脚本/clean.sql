use tool;

-- 删除暂存柜人员关联数据
DELETE FROM sub_box_account_ref;
-- 删除暂存柜流水
DELETE FROM sub_cabinet_bill;
-- 删除暂存柜储存详情
DELETE FROM sub_cabinet_detail;

-- 删除归还明细
DELETE FROM mat_return_detail;
-- 删除归还记录
DELETE FROM mat_return_back;
-- 删除领用流水
DELETE FROM mat_use_bill;
-- 删除领用记录
DELETE FROM mat_use_record;

-- 删除补料明细
DELETE FROM feeding_detail;
-- 删除补料清单
DELETE FROM feeding_list;

-- 删除硬件日志
DELETE FROM hardware_log;
-- 删除下架记录
DELETE FROM lower_frame_query;
-- 清空物料库存
UPDATE equ_detail_sta SET cur_num = 0 WHERE mat_info_id IS NOT NULL;

