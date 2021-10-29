
DELETE FROM dosth.relation where menu_id = 103;
DELETE FROM dosth.menu WHERE id = 103;
DELETE FROM dosth.relation where menu_id = 104;
DELETE FROM dosth.menu WHERE id = 104;
DELETE FROM dosth.relation where menu_id = 105;
DELETE FROM dosth.menu WHERE id = 105;
DELETE FROM dosth.relation where menu_id = 108;
DELETE FROM dosth.menu WHERE id = 108;
DELETE FROM dosth.relation where menu_id = 109;
DELETE FROM dosth.menu WHERE id = 109;
DELETE FROM dosth.relation where menu_id = 110;
DELETE FROM dosth.menu WHERE id = 110;
DELETE FROM dosth.relation where menu_id = 118;
DELETE FROM dosth.menu WHERE id = 118;
DELETE FROM dosth.relation where menu_id = 121;
DELETE FROM dosth.menu WHERE id = 121;
DELETE FROM dosth.relation where menu_id = 122;
DELETE FROM dosth.menu WHERE id = 122;
DELETE FROM dosth.relation where menu_id = 123;
DELETE FROM dosth.menu WHERE id = 123;

DELETE FROM dosth.relation where menu_id = 102;
DELETE FROM dosth.menu WHERE id = 102;

##################################################

DELETE FROM dosth.relation where menu_id = 111;
DELETE FROM dosth.menu WHERE id = 111;
DELETE FROM dosth.relation where menu_id = 112;
DELETE FROM dosth.menu WHERE id = 112;
DELETE FROM dosth.relation where menu_id = 113;
DELETE FROM dosth.menu WHERE id = 113;
DELETE FROM dosth.relation where menu_id = 119;
DELETE FROM dosth.menu WHERE id = 119;
DELETE FROM dosth.relation where menu_id = 120;
DELETE FROM dosth.menu WHERE id = 120;
DELETE FROM dosth.relation where menu_id = 124;
DELETE FROM dosth.menu WHERE id = 124;

DELETE FROM dosth.relation where menu_id = 106;
DELETE FROM dosth.menu WHERE id = 106;

##################################################

DELETE FROM dosth.relation where menu_id = 114;
DELETE FROM dosth.menu WHERE id = 114;
DELETE FROM dosth.relation where menu_id = 115;
DELETE FROM dosth.menu WHERE id = 115;
DELETE FROM dosth.relation where menu_id = 116;
DELETE FROM dosth.menu WHERE id = 116;
DELETE FROM dosth.relation where menu_id = 117;
DELETE FROM dosth.menu WHERE id = 117;
DELETE FROM dosth.relation where menu_id = 107;
DELETE FROM dosth.menu WHERE id = 107;

#################################################

DELETE FROM dosth.relation where menu_id = 101;
DELETE FROM dosth.menu WHERE id = 101;

INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (101, 2, 'tool', null, null, '工具管理', 1, 1, '', 'fa-user', 1, 1, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (101, 101, 1);

INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (102, 2, 'toolBasic', 'tool', '[tool],', '基础信息', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (102, 102, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (106, 2, 'preferences', 'tool', '[tool],', '参数设置', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (106, 106, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (107, 2, 'matequ', 'tool', '[tool],', '物料/设备', 1, 2, '', 'fa-user', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (107, 107, 1);

INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (103, 2, 'manufacturer', 'toolBasic', '[tool],[toolBasic],', '供货商管理', 1, 3, '/manufacturer', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (103, 103, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (104, 2, 'unit', 'toolBasic', '[tool],[toolBasic],', '单位管理', 1, 3, '/unit', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (104, 104, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (105, 2, 'warehouse', 'toolBasic', '[tool],[toolBasic],', '仓库管理', 1, 3, '/warehouse', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (105, 105, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (108, 2, 'matequtype', 'toolBasic', '[tool],[toolBasic],', '物料/设备类型', 1, 3, '/matequtype', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (108, 108, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (109, 2, 'procinfo', 'toolBasic', '[tool],[toolBasic],', '工序信息', 1, 3, '/procinfo', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (109, 109, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (110, 2, 'matequinfo', 'toolBasic', '[tool],[toolBasic],', '物料/设备信息', 1, 3, '/matequinfo', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (110, 110, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (118, 2, 'restitutionType', 'toolBasic', '[tool],[toolBasic],', '归还类型定义', 1, 3, '/restitutionType', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (118, 118, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (121, 2, 'product', 'toolBasic', '[tool],[toolBasic],', '产品定义', 1, 3, '/product', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (121, 121, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (122, 2, 'custom', 'toolBasic', '[tool],[toolBasic],', '自定义借出类型', 1, 3, '/custom', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (122, 122, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (123, 2, 'parts', 'toolBasic', '[tool],[toolBasic],', '零件定义', 1, 3, '/parts', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (123, 123, 1);

INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (111, 2, 'equsetting', 'preferences', '[tool],[preferences],', '设备设置', 1, 3, '/equsetting', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (111, 111, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (112, 2, 'equmatref', 'preferences', '[tool],[preferences],', '设备-物料', 1, 3, '/equmatref', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (112, 112, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (113, 2, 'procmatref', 'preferences', '[tool],[preferences],', '工序-物料', 1, 3, '/procmatref', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (113, 113, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (119, 2, 'plcSetting', 'preferences', '[tool],[preferences],', 'PLC设置', 1, 3, '/plcSetting', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (119, 119, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (120, 2, 'smartCabinet', 'preferences', '[tool],[preferences],', '智能柜设置', 1, 3, '/smartCabinet', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (120, 120, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (124, 2, 'matAssociation', 'preferences', '[tool],[preferences],', '物料关联设置', 1, 3, '/matAssociation', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (124, 124, 1);


INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (114, 2, 'warehousebill', 'matequ', '[tool],[matequ],', '库存', 1, 3, '/warehousebill', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (114, 114, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (115, 2, 'equdetail', 'matequ', '[tool],[matequ],', '存储', 1, 3, '/equdetail', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (115, 115, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (116, 2, 'matborrow', 'matequ', '[tool],[matequ],', '领取', 1, 3, '/matborrow', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (116, 116, 1);
INSERT INTO dosth.menu (id, system_info_id, code, pcode, pcodes, name, num, levels, url, icon, is_menu, is_open, is_use, tips) values (117, 2, 'returnbackbill', 'matequ', '[tool],[matequ],', '归还', 1, 3, '/returnbackbill', '', 1, 0, 1, null);
INSERT INTO dosth.relation (id, menu_id, role_id) values (117, 117, 1);


