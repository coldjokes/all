use tool;

--仓库流水
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'warehouse_bill')
DROP TABLE warehouse_bill;
--设备详情领用流水
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'mat_use_bill')
DROP TABLE mat_use_bill;
--设备详情设置流水
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'equ_detail_bill')
DROP TABLE equ_detail_bill;
--设备详情状态
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'equ_detail_sta')
DROP TABLE equ_detail_sta;
--设备设置详情
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'equ_detail')
DROP TABLE equ_detail;
--设备设置
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'equ_setting')
DROP TABLE equ_setting;
--设备物料关系
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'equ_mat_ref')
DROP TABLE equ_mat_ref;
--工序物料关系
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'proc_mat_ref')
DROP TABLE proc_mat_ref;
--工序信息
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'proc_info')
DROP TABLE proc_info;
--PLC设置
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'plc_setting')
DROP TABLE plc_setting;
--PLC设置
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'restitution_type')
DROP TABLE restitution_type;
--smart_cabinet
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'smart_cabinet')
DROP TABLE smart_cabinet;
--物料/设备信息
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'mat_equ_info')
DROP TABLE mat_equ_info;
--物料/设备类型
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'mat_equ_type')
DROP TABLE mat_equ_type;
--生产商
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'manufacturer')
DROP TABLE manufacturer;
--单位
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'unit')
DROP TABLE unit;
--仓库
IF EXISTS (SELECT 1 FROM SYSOBJECTS WHERE NAME = 'warehouse')
DROP TABLE warehouse;
