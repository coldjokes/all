update mat_use_record r set return_back_num = (select sum(bill.borrow_num) from mat_return_back back,mat_use_bill bill where bill.id = back.mat_use_bill_id and bill.mat_use_record_id = r.id and back.is_return_back = 'ISBACK' group BY r.id);
update mat_use_record r set return_back_num = 0 where return_back_num is null;
update mat_use_record r set is_return_back =case when borrow_num > return_back_num then '未归还' ELSE '已归还' end;
update mat_use_record r set is_return_back = 'NOBACK' where return_back_num is null;

