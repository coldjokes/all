 use tool;

DELETE FROM sub_box;
DELETE FROM sub_box_num_setting;
DELETE FROM sub_cabinet;
DELETE FROM feeding_detail;
DELETE FROM feeding_list;
DELETE FROM mat_use_bill;
DELETE FROM equ_detail_sta;
DELETE FROM equ_detail;
DELETE FROM mat_association;
DELETE FROM equ_setting;
DELETE FROM mat_equ_info;
DELETE FROM mat_equ_Type;
DELETE FROM manufacturer;
DELETE FROM plc_setting;
DELETE FROM product;
DELETE FROM proc_info;
DELETE FROM parts;
DELETE FROM custom;
DELETE FROM restitution_type;
DELETE FROM unit;


INSERT INTO unit(id,status,unit_name) VALUES('101', 'ENABLE', '台');
INSERT INTO unit(id,status,unit_name) VALUES('102', 'ENABLE', '箱');
INSERT INTO unit(id,status,unit_name) VALUES('103', 'ENABLE', '盒');
INSERT INTO unit(id,status,unit_name) VALUES('104', 'ENABLE', '支');
INSERT INTO unit(id,status,unit_name) VALUES('105', 'ENABLE', '把');
INSERT INTO unit(id,status,unit_name) VALUES('106', 'ENABLE', '件');
INSERT INTO unit(id,status,unit_name) VALUES('107', 'ENABLE', '片');

INSERT INTO restitution_type(id,remark,rest_name,return_back_type,status) VALUES('101','报废','报废','NORMAL','ENABLE');
INSERT INTO restitution_type(id,remark,rest_name,return_back_type,status) VALUES('102','修磨','修磨','NORMAL','ENABLE');
INSERT INTO restitution_type(id,remark,rest_name,return_back_type,status) VALUES('103','错领','错领','NORMAL','ENABLE');  
INSERT INTO restitution_type(id,remark,rest_name,return_back_type,status) VALUES('104','故障异常','故障异常','ABNORMAL','ENABLE');  
INSERT INTO restitution_type(id,remark,rest_name,return_back_type,status) VALUES('105','使用未达上限','使用未达上限','ABNORMAL','ENABLE');
 
INSERT INTO custom(id,custom_name,remark,status) VALUES('101','自定义取料A','','ENABLE');
INSERT INTO custom(id,custom_name,remark,status) VALUES('102','自定义取料B','','ENABLE');

INSERT INTO parts(id,parts_name,status) VALUES('101','零件A','ENABLE');
INSERT INTO parts(id,parts_name,status) VALUES('102','零件B','ENABLE');

INSERT INTO proc_info(id,account_id,proc_name,remark,op_date,status) VALUES('101','1','工序A','',getdate(),'ENABLE');
INSERT INTO proc_info(id,account_id,proc_name,remark,op_date,status) VALUES('102','1','工序B','',getdate(),'ENABLE');

INSERT INTO product(id,product_name,product_size,status) VALUES('101','产品A','','ENABLE');
INSERT INTO product(id,product_name,product_size,status) VALUES('102','产品B','','ENABLE');

INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1001', '4096', '10', 'F1', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1002', '6099', '20', 'F2', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1003', '6101', '30', 'F3', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1004', '6103', '40', 'F4', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1005', '6105', '50', 'F5', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1006', '6107', '60', 'F6', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1007', '6109', '70', 'F7', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1008', '6111', '80', 'F8', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1009', '6113', '90', 'F9', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1010', '6115', '100', 'F10', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1011', '6117', '35', '取料口位置', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1012', '6119', '30', '运行速度', '寄存器', 'ENABLE','YES');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1013', '6121', '35', '实时位置', '寄存器', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1014', '6123', '0', '偏移量', '寄存器', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1015', '4187', null, '目标层', '寄存器', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1016', '2349', null, '点动上升', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1017', '2350', null, '点动下降', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1018', '2351', null, '取料口开门', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1019', '2352', null, '取料口关门', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1020', '2353', null, '复位料斗', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1021', '2354', null, '已到达目标层', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1022', '2355', null, '取料口关门检测', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1023', '2356', null, '故障标志', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1024', '2357', null, '上限位报警', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1025', '2358', null, '下限位报警', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1026', '2359', null, '伺服驱动器报警', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1027', '2360', null, '开门失败', '线圈', 'ENABLE','NO');
INSERT INTO plc_setting(id,address,default_value,plc_name,remark,status,is_default) VALUES('1028', '2361', null, '关门失败', '线圈', 'ENABLE','NO');

INSERT INTO manufacturer(id, address, contact, manufacturer_name, phone, remark, status) values('001', '苏州工业园区科智路9号', '高勇', '阿诺', '13800000000', '', 'ENABLE' );

INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1001', 'STORAGE', '101', '', 'ENABLE', '101', '刀具柜');
INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1002', 'EQUIPMENT', '101', '', 'ENABLE', '101', '数控机床');
INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1003', 'MATERIAL', '103', '', 'ENABLE', '103', '刀片');
INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1004', 'MATERIAL', '103', '', 'ENABLE', '103', '丝锥');
INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1005', 'MATERIAL', '103', '', 'ENABLE', '103', '钻头');
INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1006', 'MATERIAL', '103', '', 'ENABLE', '103', '铣刀');
INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1007', 'MATERIAL', '103', '', 'ENABLE', '103', '镗刀');
INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1008', 'MATERIAL', '103', '', 'ENABLE', '103', '铰刀');
INSERT INTO mat_equ_Type(id, mat_equ, pack_unit_id, remark, status, store_unit_id, type_name) values('1009', 'MATERIAL', '103', '', 'ENABLE', '103', '车刀');

INSERT INTO mat_equ_info(id, bar_code, borrow_type, brand, icon, manufacturer_id, mat_equ_name, mat_equ_type_id, num, old_for_new, remark, spec, status, store_price) 
values('297e5a8667960b0701679698f7cb0012', '001', 'PACK', '阿诺', '7a1f325e-356c-467a-a385-fb411d45d52f.jpg', '001', '刀具柜001', '1001', '0', 'YES', '', '9*10', 'ENABLE', '0');

INSERT INTO equ_setting(id, account_id, asset_no, col_num, equ_info_id, ip, op_date, port, remark, row_num, status) 
	values('297e5a8667960b070167969f51620013', '1', '0001', '10', '297e5a8667960b0701679698f7cb0012', '192.168.2.97', getdate(), '502', '2018-12-25', '9', 'ENABLE');	

INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('1', '1101060244', 'WNMG080408 GT VA5525', 'b8a57ffd-c835-441a-80b4-08ba218c9513.jpg', '车刀片', '1009', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('2', '1101060292', 'SNMG120412-XMGC15', 'e9285b19-c0e3-4254-ad09-4c83ce1159b0.jpg', '车刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('3', '1101110015', 'CNMG120404-PF4015', '25036631-5c91-4859-821c-0b140ae739b8.jpg', '车刀片', '1009', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('4', '1101110057', 'CNMG160608-PM4025(PM4225)', 'd00e5b0a-face-4cff-a081-d5cdd31f4c5c.jpg', '车刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('5', '1101111071', 'CNMG120404-PM4015', 'df3ac947-260a-4cc5-88a8-384c464cb80b.jpg', '车刀片', '1009', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('6', '1101060320', 'CCMT120412-PM4325', '150befee-9bab-4a8f-805e-52000b718204.jpg', '车刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山特', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('7', '1101060322', '266LG-16MM01A200M 1125', '2a2ad9d4-846e-4998-a268-31fbe7c14483.jpg', '车刀片', '1009', '0', 'ENABLE', '', 'METER', 'YES', '10', '山特', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('8', '1101060325', 'WNMG080412-PR4235', 'd42237d8-4568-40cb-8b47-63b878a05dea.jpg', '车刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山特', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('9', '1101111296', '266LG-16MM01A 150M', 'c571fa10-6255-40e1-b109-186a7e59c2c5.jpg', '车丝刀片', '1009', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('10', '1101111297', '266RG-16MM01A 150M 1125', '19f19cb9-6614-42bc-81af-fa7544a11b6b.jpg', '车丝刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('11', '1101102048', '266RG-16MM01A 200M 1125', 'f5a7a279-6429-46f7-97e5-b113eb33ae9b.jpg', '刀片', '1003', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('12', '1101111095', 'XPNT160412 TN5515', '565e4c04-1860-4597-8700-80f316f10f46.jpg', '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('13', '1101111328', 'EDPT140408PDSRGD KC520M', 'ca1b2f51-4c02-42cb-a36f-0e4fe69ce72c.jpg', '刀片', '1003', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('14', '1101101830', 'DNMG150608-PF4215', '8d543faf-f79d-46ad-a759-e4d3faa0ee34.jpg', '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('15', '1101101896', 'DPGT11T310', '7e294b90-f633-45f8-8054-e1f53fdcd8c4.jpg', '刀片', '1003', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('16', '1101110700', 'CCMT09T304-F1 CP500', '7c713694-53d9-49f6-a765-907600232ade.jpg', '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('17', '1101110714', 'N9MT2204CT NC40', '6fe967c3-6924-4903-840b-2085a16b0733.jpg', '刀片', '1003', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('18', '1101110730', 'TCMT220408LF KC5010', '3e351f22-c17e-443a-bb2d-8607cb904ab1.jpg', '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('19', '1101110761', 'SPMX090304-75 F40M', '26dfb4be-e5c8-402e-8b19-438df283ce3f.jpg', '刀片', '1003', '0', 'ENABLE', '', 'METER', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('20', '1101111247', 'SNMG120408-PM4215', '8760bdc0-52a5-46f3-802c-f30c3abef23c.jpg', '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('21', '1101111320', 'ONMU090520ANTN-M14 F40M', 'fcb9a956-6b7a-4369-ab11-5f26a758eb26.jpg', '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('22', '1101102359', 'CCMT09T308-M3 TK2001', '19420b9e-2510-4462-9bb7-fe93397e16d3.jpg', '倒角刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山高', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('23', '1101080052', 'M14*1.5-H2', '512aa743-5bec-4731-bfbe-9874e3d25a66.jpg', '机用丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('24', '1101080061', 'M16*1.5-H2', '5efc45af-bd26-4c03-9711-70e8ed5e2ffe.jpg', '机用丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('25', '1101110128', 'CCMT060204-F2TP200(TP2000)', '8cf2ea7e-2fe0-4e2a-b5be-3169d1ebc718.jpg', '精镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('26', '1101101742', '12.04099.0012 D12.3', 'b8cae236-2b24-481f-9922-4ca71aa8bfb6.jpg', '立铣刀', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('27', '1101050421', 'GSX41000S-2D', '5cdf1afa-7dc4-45b4-bc9c-c7ed5af6111e.jpg', '立铣刀', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '住友', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('28', '1101101025', '6445 M16-6HX', '905ad672-f79f-4552-a3ad-a40d9935b1f9.jpg', '丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('29', '1101101745', '1394440-M16x1.5', 'b8a57ffd-c835-441a-80b4-08ba218c9513.jpg', '丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('30', '1101110143', 'CCMT09T308-PM4035', 'e9285b19-c0e3-4254-ad09-4c83ce1159b0.jpg', '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('31', '1101110156', 'TCMT090204-UF4025', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('32', '1101110282', 'WCGT050304-X15 WTP35', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('33', '1101110433', 'CCMT120408-PM4025', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('34', '1101102296', 'TCMT16T308-PR4325', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山特', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('35', '1101102360', 'TCMT16T308-PM4315', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山特', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('36', '1101070089', 'ONMU090520ANTN-M14 MP2500', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('37', '1101070090', 'XNEX080608TR-MD15 MP1500', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('38', '1101070066', 'XOMX120404TR-ME08 F40M', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('39', '1101070014', 'YT5 4XH16R', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '4.76', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('40', '1101050354', ' 12.3', null, '直柄键槽铣刀', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('41', '1101101702', 'D14.5*182*L135*SD16', null, '钻头', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('42', '1101060291', 'CNMG120408-PF4315', null, '车刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('43', '1101060315', 'WNMG080412-M5 TP2501', null, '车刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山高', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('44', '1101060324', 'CNMG120404-LC1525', null, '车刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山高', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('45', '1101110340', 'N9MT11T3CT-NC40', null, '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('46', '1101110567', 'CCGT09T304-26G6', null, '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('47', '1101111322', 'TCMT110304-PF4215', null, '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('48', '1101111334', 'SCMT120408 YBC251', null, '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('49', '1101100649', '831-M16*1.5', null, '丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('50', '1101080291', '1394439-M14*1.5', null, '丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '蓝帜', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('51', '1101110201', 'TCMT220408-KC850', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('52', '1101102361', 'TCMT110304-PF4315', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山特', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('53', '1101070114', 'ONMU090520ANTN-M13 MP1500', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('54', '1101070064', 'SNHQ110202TL4-M07 F40M', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('55', '1101070065', 'SNHQ110202TR4-M07 F40M', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('56', '1101110587', 'N9MT11T308-NC40', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('57', '1101070121', 'SNHQ110304TR4-M07 F40M', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山高', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('58', '1101070122', 'SNHQ110304TL4-M07 F40M', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山高', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('59', '1101070140', 'CCMT120408-M5 TK2001', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山高', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('60', '1101070143', 'S-328.2KEC.A1025', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '山特', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('61', '1101102213', 'P6002-D24.21R WXK25', null, '钻尖', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '瓦尔特', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('62', '1101100493', '05514-12.500', null, '钻头', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('63', '1101101491', '5514-22.25', null, '钻头', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('64', '1101110166', 'TCMT110204-UF4025', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('65', '1101111260', 'CNMG190612-YBC251', null, '车刀片', '1009', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('66', '1101110713', 'N9GX060204 NC2033', null, '刀片', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('67', '1111011004', 'GJ239-001', null, '复合绞刀', '1008', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('68', '1101140422', 'GJ251-001(左)', null, '花键滚刀', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '博特泰诺', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('69', '1101140423', 'GJ251-002(右)', null, '花键滚刀', '1003', '0', 'ENABLE', '', 'PACK', 'YES', '10', '博特泰诺', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('70', '1101080261', 'M26*1.5*150-H2', null, '加长机用丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '西南工具', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('71', '1101100528', 'MKD-JD22*45*100', null, '铰刀', '1008', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('72', '1101090187', '715-D10', null, '铰刀', '1008', '0', 'ENABLE', '', 'PACK', 'YES', '10', '钴领', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('73', '1101100879', '361-10', null, '丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('74', '1101080249', '1402483-M12', null, '丝锥', '1004', '0', 'ENABLE', '', 'PACK', 'YES', '10', '蓝帜', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('75', '1101110441', 'TNMG160408-YBC351', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('76', '1101110716', 'TNMG220408-KM3215', null, '镗刀片', '1007', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('77', '1101110325', 'N9MT11T308-NC10', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('78', '1101110331', 'OFMT070405TN-M15T250M', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('79', '1101111177', 'HNGJ0905 ANSNGD TN7535', null, '铣刀片', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('80', '1101050352', ' 10', null, '直柄键槽铣刀', '1006', '0', 'ENABLE', '', 'PACK', 'YES', '10', '非标', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('81', '1101020121', 'φ 24.5', null, '锥钻', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('82', '1101100026', '05515-8.500', null, '钻头', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('83', '1101100029', '05514-13000', null, '钻头', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('84', '1101100037', '05514-16000', null, '钻头', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('85', '1101100997', '5515-10.3', null, '钻头', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
INSERT INTO mat_equ_info(id, bar_code, spec, icon, mat_equ_name, mat_equ_type_id, store_price, status, remark, borrow_type, old_for_new, num, brand, manufacturer_id) values('86', '1101101282', '5514-9-1', null, '钻头', '1005', '0', 'ENABLE', '', 'PACK', 'YES', '10', '', '001');
	
INSERT INTO mat_association(id, num, mat_info_id, custom_id, status) values('001', '5', '1', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, custom_id, status) values('002', '5', '2', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, custom_id, status) values('003', '5', '3', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, custom_id, status) values('004', '5', '4', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, custom_id, status) values('005', '5', '5', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, custom_id, status) values('006', '5', '6', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, mat_equ_type_id, status) values('007', '5', '7', '1002', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, mat_equ_type_id, status) values('008', '5', '8', '1002', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, mat_equ_type_id, status) values('009', '5', '9', '1002', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, mat_equ_type_id, status) values('010', '5', '10', '1002', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, mat_equ_type_id, status) values('011', '5', '11', '1002', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, mat_equ_type_id, status) values('012', '5', '12', '1002', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, parts_id, status) values('013', '5', '13', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, parts_id, status) values('014', '5', '14', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, parts_id, status) values('015', '5', '15', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, parts_id, status) values('016', '5', '16', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, parts_id, status) values('017', '5', '17', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, parts_id, status) values('018', '5', '18', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, proc_info_id, status) values('019', '5', '19', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, proc_info_id, status) values('020', '5', '20', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, proc_info_id, status) values('021', '5', '21', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, proc_info_id, status) values('022', '5', '22', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, proc_info_id, status) values('023', '5', '23', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, proc_info_id, status) values('024', '5', '24', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, product_id, status) values('025', '5', '25', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, product_id, status) values('026', '5', '26', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, product_id, status) values('027', '5', '27', '101', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, product_id, status) values('028', '5', '28', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, product_id, status) values('029', '5', '29', '102', 'ENABLE');
INSERT INTO mat_association(id, num, mat_info_id, product_id, status) values('030', '5', '30', '102', 'ENABLE');	
	
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('1', '297e5a8667960b070167969f51620013', '192.168.1.101', '502', '1', 'ENABLE');
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('2', '297e5a8667960b070167969f51620013', '192.168.1.102', '502', '2', 'ENABLE');
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('3', '297e5a8667960b070167969f51620013', '192.168.1.103', '502', '3', 'ENABLE');
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('4', '297e5a8667960b070167969f51620013', '192.168.1.104', '502', '4', 'ENABLE');
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('5', '297e5a8667960b070167969f51620013', '192.168.1.105', '502', '5', 'ENABLE');
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('6', '297e5a8667960b070167969f51620013', '192.168.1.106', '502', '6', 'ENABLE');
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('7', '297e5a8667960b070167969f51620013', '192.168.1.107', '502', '7', 'ENABLE');
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('8', '297e5a8667960b070167969f51620013', '192.168.1.108', '502', '8', 'ENABLE');
INSERT INTO equ_detail(id, equ_setting_id, ip, port, row_no, status) values('9', '297e5a8667960b070167969f51620013', '192.168.1.109', '502', '9', 'ENABLE');

INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('1', '1', '17', '1', 'NONE', '1', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('2', '2', '17', '1', 'NONE', '2', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('3', '3', '17', '1', 'NONE', '3', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('4', '4', '17', '1', 'NONE', '4', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('5', '5', '17', '1', 'NONE', '5', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('6', '6', '17', '1', 'NONE', '6', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('7', '7', '17', '1', 'NONE', '7', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('8', '8', '17', '1', 'NONE', '8', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('9', '9', '17', '1', 'NONE', '9', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('10', '10', '17', '1', 'NONE', '10', '17', '3', 'ENABLE');

INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('11', '1', '17', '2', 'NONE', '11', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('12', '2', '17', '2', 'NONE', '12', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('13', '3', '17', '2', 'NONE', '13', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('14', '4', '17', '2', 'NONE', '14', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('15', '5', '17', '2', 'NONE', '15', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('16', '6', '17', '2', 'NONE', '16', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('17', '7', '17', '2', 'NONE', '17', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('18', '8', '17', '2', 'NONE', '18', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('19', '9', '17', '2', 'NONE', '19', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('20', '10', '17', '2', 'NONE', '20', '17', '3', 'ENABLE');

INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('21', '1', '17', '3', 'NONE', '21', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('22', '2', '17', '3', 'NONE', '22', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('23', '3', '17', '3', 'NONE', '23', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('24', '4', '17', '3', 'NONE', '24', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('25', '5', '17', '3', 'NONE', '25', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('26', '6', '17', '3', 'NONE', '26', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('27', '7', '17', '3', 'NONE', '27', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('28', '8', '17', '3', 'NONE', '28', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('29', '9', '17', '3', 'NONE', '29', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('30', '10', '17', '3', 'NONE', '30', '17', '3', 'ENABLE');

INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('31', '1', '17', '4', 'NONE', '1', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('32', '2', '17', '4', 'NONE', '2', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('33', '3', '17', '4', 'NONE', '3', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('34', '4', '17', '4', 'NONE', '4', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('35', '5', '17', '4', 'NONE', '5', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('36', '6', '17', '4', 'NONE', '6', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('37', '7', '17', '4', 'NONE', '7', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('38', '8', '17', '4', 'NONE', '8', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('39', '9', '17', '4', 'NONE', '9', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('40', '10', '17', '4', 'NONE', '10', '17', '3', 'ENABLE');

INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('41', '1', '17', '5', 'NONE', '11', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('42', '2', '17', '5', 'NONE', '12', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('43', '3', '17', '5', 'NONE', '13', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('44', '4', '17', '5', 'NONE', '14', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('45', '5', '17', '5', 'NONE', '15', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('46', '6', '17', '5', 'NONE', '16', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('47', '7', '17', '5', 'NONE', '17', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('48', '8', '17', '5', 'NONE', '18', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('49', '9', '17', '5', 'NONE', '19', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('50', '10', '17', '5', 'NONE', '20', '17', '3', 'ENABLE');

INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('51', '1', '17', '6', 'NONE', '21', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('52', '2', '17', '6', 'NONE', '22', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('53', '3', '17', '6', 'NONE', '23', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('54', '4', '17', '6', 'NONE', '24', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('55', '5', '17', '6', 'NONE', '25', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('56', '6', '17', '6', 'NONE', '26', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('57', '7', '17', '6', 'NONE', '27', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('58', '8', '17', '6', 'NONE', '28', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('59', '9', '17', '6', 'NONE', '29', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('60', '10', '17', '6', 'NONE', '30', '17', '3', 'ENABLE');

INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('61', '1', '17', '7', 'NONE', '1', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('62', '2', '17', '7', 'NONE', '2', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('63', '3', '17', '7', 'NONE', '3', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('64', '4', '17', '7', 'NONE', '4', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('65', '5', '17', '7', 'NONE', '5', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('66', '6', '17', '7', 'NONE', '6', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('67', '7', '17', '7', 'NONE', '7', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('68', '8', '17', '7', 'NONE', '8', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('69', '9', '17', '7', 'NONE', '9', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('70', '10', '17', '7', 'NONE', '10', '17', '3', 'ENABLE');

INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('71', '1', '17', '8', 'NONE', '11', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('72', '2', '17', '8', 'NONE', '12', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('73', '3', '17', '8', 'NONE', '13', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('74', '4', '17', '8', 'NONE', '14', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('75', '5', '17', '8', 'NONE', '15', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('76', '6', '17', '8', 'NONE', '16', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('77', '7', '17', '8', 'NONE', '17', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('78', '8', '17', '8', 'NONE', '18', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('79', '9', '17', '8', 'NONE', '19', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('80', '10', '17', '8', 'NONE', '20', '17', '3', 'ENABLE');
	
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('81', '1', '17', '9', 'NONE', '21', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('82', '2', '17', '9', 'NONE', '22', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('83', '3', '17', '9', 'NONE', '23', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('84', '4', '17', '9', 'NONE', '24', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('85', '5', '17', '9', 'NONE', '25', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('86', '6', '17', '9', 'NONE', '26', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('87', '7', '17', '9', 'NONE', '27', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('88', '8', '17', '9', 'NONE', '28', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('89', '9', '17', '9', 'NONE', '29', '17', '3', 'ENABLE');
INSERT INTO equ_detail_sta(id, col_no, cur_num, equ_detail_id, equ_sta, mat_info_id, max_reserve, warn_val, status) values('90', '10', '17', '9', 'NONE', '30', '17', '3', 'ENABLE');

INSERT INTO sub_cabinet(id, account_id, asset_no, cabinet_name, col_num, comm, ip, op_date, remark, row_num, status) 
	values('402882ba67e3cfa20167e3d7d7a50000', '1', '001', '副柜001', '5', '502', '192.168.2.97', getdate(), '2018-12-25', '20', 'ENABLE');	
	
INSERT INTO sub_box_num_setting(id, max_num) values('1', '3');


INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('1', '1', 'NORMAL', '1', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('2', '2', 'NORMAL', '2', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('3', '3', 'NORMAL', '3', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('4', '4', 'NORMAL', '4', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('5', '5', 'NORMAL', '5', '1', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('6', '6', 'NORMAL', '1', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('7', '7', 'NORMAL', '2', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('8', '8', 'NORMAL', '3', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('9', '9', 'NORMAL', '4', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('10', '10', 'NORMAL', '5', '2', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('11', '11', 'NORMAL', '1', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('12', '12', 'NORMAL', '2', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('13', '13', 'NORMAL', '3', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('14', '14', 'NORMAL', '4', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('15', '15', 'NORMAL', '5', '3', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('16', '16', 'NORMAL', '1', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('17', '17', 'NORMAL', '2', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('18', '18', 'NORMAL', '3', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('19', '19', 'NORMAL', '4', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('20', '20', 'NORMAL', '5', '4', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('21', '21', 'NORMAL', '1', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('22', '22', 'NORMAL', '2', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('23', '23', 'NORMAL', '3', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('24', '24', 'NORMAL', '4', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('25', '25', 'NORMAL', '5', '5', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('26', '26', 'NORMAL', '1', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('27', '27', 'NORMAL', '2', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('28', '28', 'NORMAL', '3', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('29', '29', 'NORMAL', '4', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('30', '30', 'NORMAL', '5', '6', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('31', '31', 'NORMAL', '1', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('32', '32', 'NORMAL', '2', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('33', '33', 'NORMAL', '3', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('34', '34', 'NORMAL', '4', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('35', '35', 'NORMAL', '5', '7', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('36', '36', 'NORMAL', '1', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('37', '37', 'NORMAL', '2', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('38', '38', 'NORMAL', '3', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('39', '39', 'NORMAL', '4', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('40', '40', 'NORMAL', '5', '8', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('41', '41', 'NORMAL', '1', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('42', '42', 'NORMAL', '2', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('43', '43', 'NORMAL', '3', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('44', '44', 'NORMAL', '4', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('45', '45', 'NORMAL', '5', '9', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('46', '46', 'NORMAL', '1', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('47', '47', 'NORMAL', '2', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('48', '48', 'NORMAL', '3', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('49', '49', 'NORMAL', '4', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('50', '50', 'NORMAL', '5', '10', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('51', '51', 'NORMAL', '1', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('52', '52', 'NORMAL', '2', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('53', '53', 'NORMAL', '3', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('54', '54', 'NORMAL', '4', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('55', '55', 'NORMAL', '5', '11', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('56', '56', 'NORMAL', '1', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('57', '57', 'NORMAL', '2', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('58', '58', 'NORMAL', '3', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('59', '59', 'NORMAL', '4', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('60', '60', 'NORMAL', '5', '12', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('61', '61', 'NORMAL', '1', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('62', '62', 'NORMAL', '2', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('63', '63', 'NORMAL', '3', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('64', '64', 'NORMAL', '4', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('65', '65', 'NORMAL', '5', '13', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('66', '66', 'NORMAL', '1', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('67', '67', 'NORMAL', '2', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('68', '68', 'NORMAL', '3', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('69', '69', 'NORMAL', '4', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('70', '70', 'NORMAL', '5', '14', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('71', '71', 'NORMAL', '1', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('72', '72', 'NORMAL', '2', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('73', '73', 'NORMAL', '3', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('74', '74', 'NORMAL', '4', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('75', '75', 'NORMAL', '5', '15', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('76', '76', 'NORMAL', '1', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('77', '77', 'NORMAL', '2', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('78', '78', 'NORMAL', '3', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('79', '79', 'NORMAL', '4', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('80', '80', 'NORMAL', '5', '16', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('81', '81', 'NORMAL', '1', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('82', '82', 'NORMAL', '2', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('83', '83', 'NORMAL', '3', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('84', '84', 'NORMAL', '4', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('85', '85', 'NORMAL', '5', '17', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('86', '86', 'NORMAL', '1', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('87', '87', 'NORMAL', '2', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('88', '88', 'NORMAL', '3', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('89', '89', 'NORMAL', '4', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('90', '90', 'NORMAL', '5', '18', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('91', '91', 'NORMAL', '1', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('92', '92', 'NORMAL', '2', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('93', '93', 'NORMAL', '3', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('94', '94', 'NORMAL', '4', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('95', '95', 'NORMAL', '5', '19', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	

INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('96', '96', 'NORMAL', '1', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('97', '97', 'NORMAL', '2', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('98', '98', 'NORMAL', '3', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('99', '99', 'NORMAL', '4', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');	
INSERT INTO sub_box(id, box_index, cabinet_sta, col_no, row_no, status, sub_cabinet_id) values('100', '100', 'NORMAL', '5', '20', 'ENABLE', '402882ba67e3cfa20167e3d7d7a50000');










