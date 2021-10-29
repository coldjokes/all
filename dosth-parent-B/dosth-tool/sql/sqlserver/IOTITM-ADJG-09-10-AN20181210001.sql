use tool;

INSERT INTO unit(id, status, unit_name) values('1001', 'ENABLE', '台');

INSERT INTO manufacturer(id, address, contact, manufacturer_name, phone, remark, status) 
	values('297e5a8667960b070167968ebf5b0010', '苏州工业园区科智路9号', '高勇', '阿诺', '', '', 'ENABLE' );

INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) 
	values('297e5a8667960b0701679690c5100011', 'STORAGE', '1001', '', 'ENABLE', '1001', '刀具柜');

INSERT INTO mat_equ_info(id, bar_code, borrow_type, brand, icon, manufacturer_id, mat_equ_name, mat_equ_type_id, num, old_for_new, remark, spec, status, store_price) 
	values('297e5a8667960b0701679698f7cb0012', '01', 'PACK', '阿诺', '7a1f325e-356c-467a-a385-fb411d45d52f.jpg', '297e5a8667960b070167968ebf5b0010', '主柜A', '297e5a8667960b0701679690c5100011', '1', 'YES', '', '9*10', 'ENABLE', '1');
	
INSERT INTO equ_setting(id, account_id, asset_no, col_num, equ_info_id, ip, op_date, port, remark, row_num, status) 
	values('297e5a8667960b070167969f51620013', '1', '001', '10', '297e5a8667960b0701679698f7cb0012', '192.168.2.10', getdate(), '2010', '', '9', 'ENABLE');	
