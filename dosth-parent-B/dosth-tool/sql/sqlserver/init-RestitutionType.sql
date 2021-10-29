--﻿﻿/**
-- * @description 初始化归还类型
-- * @author guozhidong
-- * @date 2018-11-03
-- */
 use tool;
 -- 删除归还确认信息
  delete from tool.dbo.mat_return_back_verify;
  -- 删除归还信息
  delete from tool.dbo.mat_return_back;
  -- 删除归还类型
 delete from tool.dbo.restitution_type;
  
 INSERT INTO tool.dbo.restitution_type(id,remark,rest_name,return_back_type,status) VALUES('101','报废','报废','NORMAL','ENABLE');
 INSERT INTO tool.dbo.restitution_type(id,remark,rest_name,return_back_type,status) VALUES('102','修磨','修磨','NORMAL','ENABLE');
 INSERT INTO tool.dbo.restitution_type(id,remark,rest_name,return_back_type,status) VALUES('103','错领','错领','NORMAL','ENABLE');  
 INSERT INTO tool.dbo.restitution_type(id,remark,rest_name,return_back_type,status) VALUES('104','故障异常','故障异常','ABNORMAL','ENABLE');  
 INSERT INTO tool.dbo.restitution_type(id,remark,rest_name,return_back_type,status) VALUES('105','使用未达上限','使用未达上限','ABNORMAL','ENABLE');
  
  