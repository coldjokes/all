update equ_detail_sta s set s.last_feed_time = DATE_FORMAT(NOW(),'%Y-%m-%d %H:%m:%s') where s.last_feed_time is null;

update users u set u.ic_card = (select c.ic_str from iccard c where u.account_id = c.account_id);

update lower_frame_query q set q.equ_name = (SELECT e.equ_setting_name from equ_detail_sta s, equ_detail d, equ_setting e where s.equ_detail_id = d.id and d.equ_setting_id = e.id and q.equ_detail_sta_id = s.id) WHERE q.equ_detail_sta_id is not null;
update lower_frame_query q set q.equ_name = (SELECT c.cabinet_name from sub_box b, sub_cabinet c where b.sub_cabinet_id = c.id and q.sub_box_id = b.id) WHERE q.sub_box_id is not null;
update lower_frame_query q set q.mat_info_name = (SELECT m.mat_equ_name from mat_equ_info m where m.id = q.mat_info_id);
update lower_frame_query q set q.bar_code = (SELECT m.bar_code from mat_equ_info m where m.id = q.mat_info_id);
update lower_frame_query q set q.spec = (SELECT m.spec from mat_equ_info m where m.id = q.mat_info_id);
update lower_frame_query q set q.user_account_id = q.account_id;
update lower_frame_query q set q.user_name = (SELECT u.user_name from view_user u where u.account_id = q.user_account_id);
update lower_frame_query q set q.owner_account_id = q.owner_id;
update lower_frame_query q set q.owner_name = (SELECT u.user_name from view_user u where u.account_id = q.owner_id);
update lower_frame_query q set q.unit = (SELECT CASE m.borrow_type WHEN 'METER' THEN '支' ELSE '盒' END from mat_equ_info m where q.mat_info_id = m.id);

update mat_use_record r set r.mat_info_name = (SELECT m.mat_equ_name from mat_equ_info m where m.id = r.mat_info_id);
update mat_use_record r set r.bar_code = (SELECT m.bar_code from mat_equ_info m where m.id = r.mat_info_id);
update mat_use_record r set r.spec = (SELECT m.spec from mat_equ_info m where m.id = r.mat_info_id);
update mat_use_record r set r.brand = (SELECT m.brand from mat_equ_info m where m.id = r.mat_info_id);
update mat_use_record r set r.user_name = (SELECT u.user_name from view_user u where u.account_id = r.account_id);
update mat_use_record r set r.pack_num = (SELECT m.num from mat_equ_info m where m.id = r.mat_info_id);
update mat_use_record r set r.pack_unit = (SELECT u.unit_name from mat_equ_info m,mat_equ_type t,unit u where m.id = r.mat_info_id and t.id = m.mat_equ_type_id and u.id = t.pack_unit_id);
update mat_use_record r set r.price = (SELECT m.store_price from mat_equ_info m where m.id = r.mat_info_id);
update mat_use_record r set r.money = r.price * r.borrow_num;
update mat_use_record r set r.borrow_type = (SELECT CASE m.borrow_type WHEN 'METER' THEN '支' ELSE '盒' END from mat_equ_info m where r.mat_info_id = m.id);


UPDATE sub_cabinet_bill b SET b.sub_box_name = (SELECT c.cabinet_name from sub_box s,sub_cabinet c WHERE b.sub_box_id = s.id and s.sub_cabinet_id = c.id);
UPDATE sub_cabinet_bill b SET b.mat_info_name = (SELECT m.mat_equ_name from mat_equ_info m WHERE m.id = b.mat_info_id);
UPDATE sub_cabinet_bill b SET b.bar_code = (SELECT m.bar_code from mat_equ_info m WHERE m.id = b.mat_info_id);
UPDATE sub_cabinet_bill b SET b.spec = (SELECT m.spec from mat_equ_info m WHERE m.id = b.mat_info_id);
UPDATE sub_cabinet_bill b SET b.user_name = (SELECT u.user_name from view_user u where u.account_id = b.account_id);
update sub_cabinet_bill b set b.borrow_type = (SELECT CASE m.borrow_type WHEN 'METER' THEN '支' ELSE '盒' END from mat_equ_info m where b.mat_info_id = m.id);
update sub_cabinet_bill b set b.price = (SELECT m.store_price from mat_equ_info m where b.mat_info_id = m.id);
update sub_cabinet_bill b set b.money = (SELECT m.store_price from mat_equ_info m where b.mat_info_id = m.id) * b.num;


