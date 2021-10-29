USE dosth;

DELETE FROM relation where exists (select 1 from menu where system_info_id = '2' and id = menu_id);
DELETE FROM menu where system_info_id = '2';

DELETE FROM system_info where id = '2';
INSERT INTO system_info(id, system_name, url) values('2', '工具管理', 'http://127.0.0.1:8082');

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('201', '2', 'tool', null, null, '工具管理', 1, 1, '', 'fa-user', 1, 1, 1, null);

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('202', '2', 'toolBasic', 'tool', '[tool],', '基础信息', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('203', '2', 'matequtype', 'toolBasic', '[tool],[toolBasic],', '物料/设备管理', 1, 3, '/matequtype', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('204', '2', 'equsetting', 'toolBasic', '[tool],[toolBasic],', '柜体管理', 1, 3, '/equsetting', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('205', '2', 'subcabinet', 'toolBasic', '[tool],[toolBasic],', '副柜管理', 1, 3, '/subcabinet', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('206', '2', 'unit', 'toolBasic', '[tool],[toolBasic],', '单位管理', 1, 3, '/unit', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('207', '2', 'manufacturer', 'toolBasic', '[tool],[toolBasic],', '供应商管理', 1, 3, '/manufacturer', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('208', '2', 'manufacturerCustom', 'toolBasic', '[tool],[toolBasic],', '供应商维护', 1, 3, '/manufacturerCustom', '', 1, 0, 1, null);

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('209', '2', 'paramDefinit', 'tool', '[tool],', '参数定义', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('210', '2', 'matequinfo', 'paramDefinit', '[tool],[paramDefinit],', '物料/设备定义', 1, 3, '/matequinfo', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('211', '2', 'restitutionType', 'paramDefinit', '[tool],[paramDefinit],', '归还类型定义', 1, 3, '/restitutionType', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('212', '2', 'custom', 'paramDefinit', '[tool],[paramDefinit],', '物料自定义', 1, 3, '/custom', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('213', '2', 'product', 'paramDefinit', '[tool],[paramDefinit],', '产品定义', 1, 3, '/product', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('214', '2', 'procinfo', 'paramDefinit', '[tool],[paramDefinit],', '工序定义', 1, 3, '/procinfo', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('215', '2', 'parts', 'paramDefinit', '[tool],[paramDefinit],', '零件定义', 1, 3, '/parts', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('216', '2', 'plcSetting', 'paramDefinit', '[tool],[paramDefinit],', 'PLC定义', 1, 3, '/plcSetting', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('232', '2', 'extraBoxNumSetting', 'paramDefinit', '[tool],[paramDefinit],', '暂存柜数量设定', 1, 3, '/extraBoxNumSetting', '', 1, 0, 1, null);

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('217', '2', 'preferences', 'tool', '[tool],', '参数设置', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('218', '2', 'smartcabinet', 'preferences', '[tool],[preferences],', '柜体设置', 1, 3, '/smartcabinet', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('219', '2', 'subcabinetsetting', 'preferences', '[tool],[preferences],', '副柜参数设置', 1, 3, '/subcabinetsetting', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('220', '2', 'matAssociation', 'preferences', '[tool],[preferences],', '物料关联设置', 1, 3, '/matAssociation', '', 1, 0, 1, null);

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('221', '2', 'equStockMgr', 'tool', '[tool],', '设备库存管理', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('222', '2', 'equdetail', 'equStockMgr', '[tool],[equStockMgr],', '柜体补料', 1, 3, '/equdetail', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('223', '2', 'allocationMgr', 'equStockMgr', '[tool],[equStockMgr],', '调拨管理', 1, 3, '/allocationMgr', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('224', '2', 'lowerFrameMgr', 'equStockMgr', '[tool],[equStockMgr],', '下架管理', 1, 3, '/lowerFrameMgr', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('225', '2', 'matReturnBackBill', 'equStockMgr', '[tool],[equStockMgr],', '归还管理', 1, 3, '/matReturnBackBill', '', 1, 0, 1, null);

INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('226', '2', 'query', 'tool', '[tool],', '查询统计', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('227', '2', 'mainCabinetQuery', 'query', '[tool],[query],', '主柜领用查询', 1, 3, '/mainCabinetQuery', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('228', '2', 'viceCabinetQuery', 'query', '[tool],[query],', '副柜领用查询', 1, 3, '/viceCabinetQuery', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('229', '2', 'viceCabinetDetail', 'query', '[tool],[query],', '副柜明细查询', 1, 3, '/viceCabinetDetail', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('230', '2', 'feedQuery', 'query', '[tool],[query],', '补料查询', 1, 3, '/feedQuery', '', 1, 0, 1, null);
INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('231', '2', 'lowerFrameQuery', 'query', '[tool],[query],', '下架查询', 1, 3, '/lowerFrameQuery', '', 1, 0, 1, null);


INSERT INTO relation (id, menu_id, role_id) values ('201', '201', '101');
INSERT INTO relation (id, menu_id, role_id) values ('202', '232', '101');



--INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('227', '2', 'matborrow', 'equStockMgr', '[tool],[equStockMgr],', '领取', 1, 3, '/matborrow', '', 1, 0, 1, null);
--INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('222', '2', 'stock', 'equStockMgr', '[tool],[equStockMgr],', '柜体库存状态查询', 1, 3, '/stock', '', 1, 0, 1, null);
--INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('234', '2', 'warehousingDetailQuery', 'query', '[tool],[query],', '入库明细查询', 1, 3, '/warehousingDetailQuery', '', 1, 0, 1, null);
--INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('235', '2', 'storehouseQuery', 'query', '[tool],[query],', '出库明细查询', 1, 3, '/storehouseQuery', '', 1, 0, 1, null);
--INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('232', '2', 'restitutionQuery', 'query', '[tool],[query],', '归还查询', 1, 3, '/restitutionQuery', '', 1, 0, 1, null);
--INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('233', '2', 'useDetailCostQuery', 'query', '[tool],[query],', '领用明细成本查询', 1, 3, '/useDetailCostQuery', '', 1, 0, 1, null);
--INSERT INTO menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values ('206', '2', 'warehouse', 'toolBasic', '[tool],[toolBasic],', '仓库管理', 1, 3, '/warehouse', '', 1, 0, 1, null);





