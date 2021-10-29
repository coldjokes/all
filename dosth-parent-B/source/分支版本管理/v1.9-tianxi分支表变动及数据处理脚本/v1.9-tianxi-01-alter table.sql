-- 修改领取记录表 
-- 领取类型名称为领取类型Id
ALTER TABLE mat_use_record MODIFY COLUMN receive_info VARCHAR(36);
-- 领取记录表添加部门Id
ALTER TABLE mat_use_record ADD COLUMN dept_id VARCHAR(36);

-- 领取流水
-- 添加物料名称
ALTER TABLE mat_use_bill ADD COLUMN mat_equ_name VARCHAR(50);
-- 添加物料型号
ALTER TABLE mat_use_bill ADD COLUMN spec VARCHAR(50);
-- 添加供应商
ALTER TABLE mat_use_bill ADD COLUMN manufacturer VARCHAR(50);
-- 品牌
ALTER TABLE mat_use_bill ADD COLUMN brand VARCHAR(50);
-- 添加物料编号
ALTER TABLE mat_use_bill ADD COLUMN bar_code VARCHAR(30);

-- 移除领取方式
ALTER TABLE mat_use_bill DROP COLUMN receive_info;
-- 移除领取类型
ALTER TABLE mat_use_bill DROP COLUMN receive_type;
-- 移除领取类型path
ALTER TABLE mat_category_tree DROP COLUMN `path`;
-- 添加领取类型fullId
ALTER TABLE mat_category_tree ADD COLUMN f_id VARCHAR(200);
-- 添加领取类型fullName
ALTER TABLE mat_category_tree ADD COLUMN f_name VARCHAR(100);