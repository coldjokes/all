-- 修改领取方式名称为领取方式Id
UPDATE mat_use_record r, mat_category_tree t SET receive_info = t.id
WHERE r.receive_info = t.`name`;

-- 修改领用记录部门Id为当前用户部门Id
UPDATE mat_use_record r, view_user u, view_dept d SET r.dept_id = d.DEPT_ID
WHERE r.account_id = u.ACCOUNT_ID AND u.DEPT_ID = d.DEPT_ID;

-- 修改领用流水相关信息
UPDATE mat_use_bill b, mat_equ_info i, manufacturer m
SET b.mat_equ_name = i.mat_equ_name,
	  b.spec = i.spec,
    b.manufacturer = m.manufacturer_name,
    b.brand = i.brand,
    b.bar_code = i.bar_code,
    b.store_price = i.store_price,	
    b.borrow_num = CASE WHEN i.borrow_type = 'PACK' THEN 1 ELSE i.num END	
WHERE b.mat_info_id = i.id
	AND i.manufacturer_id = m.id;
	
-- 更新领取记录全部类型为空	
UPDATE mat_use_record SET receive_info = NULL WHERE receive_info = '全部';
	