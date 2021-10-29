--﻿/**
-- * @description plc参数配置项
-- * @author guozhidong
-- */

use tool;

delete from cabinet_plc_setting;
delete from plc_setting;

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

select * from plc_setting;


