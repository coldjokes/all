SET FOREIGN_KEY_CHECKS=0;

Delete from mat_use_record;

INSERT INTO mat_use_record(id, account_id, borrow_num, mat_info_id, op_date, receive_info, receive_type, store_price) 
SELECT id, account_id, borrow_num, mat_info_id, op_date, receive_info, receive_type, store_price from mat_use_bill;

update mat_use_bill set mat_use_record_id = id;

update feeding_list set feed_type = 'LIST';

DROP VIEW IF EXISTS VIEW_USER;
CREATE VIEW VIEW_USER
AS
SELECT A.ID ACCOUNT_ID, U.DEPT_ID, U.NAME USER_NAME, A.LOGIN_NAME, '' USER_NO, U.START_TIME, U.END_TIME, U.LIMIT_SUM_NUM, U.NOT_RETURN_LIMIT_NUM
FROM dosth.ACCOUNT A
LEFT JOIN dosth.USERS U
ON A.ID = U.ACCOUNT_ID
WHERE A.`status` = 'OK';

DROP VIEW IF EXISTS VIEW_DEPT;
CREATE VIEW VIEW_DEPT
AS
SELECT D.ID DEPT_ID, D.P_ID DEPT_P_ID, D.DEPT_NAME FROM dosth.DEPT D;

update mat_return_back set audit_status = (select audit_status from mat_return_back_verify where mat_return_back.id = mat_return_back_verify.mat_returnback_id);
update mat_return_back set audit_Status = 'NO_CHECK' where audit_Status is null;
update mat_return_back set confirm_date = (select op_date from mat_return_back_verify where mat_return_back.id = mat_return_back_verify.mat_returnback_id);
update mat_return_back set confirm_mode = (select mode from mat_return_back_verify where mat_return_back.id = mat_return_back_verify.mat_returnback_id);
update mat_return_back set confirm_user = (select account_id from mat_return_back_verify where mat_return_back.id = mat_return_back_verify.mat_returnback_id);
update mat_return_back set confirm_user = (select USER_NAME from view_user where mat_return_back.confirm_user = view_user.ACCOUNT_ID);
update mat_return_back set num = (select num from mat_return_back_verify where mat_return_back.id = mat_return_back_verify.mat_returnback_id);
update mat_return_back set remark = (select remark from mat_return_back_verify where mat_return_back.id = mat_return_back_verify.mat_returnback_id);

SET FOREIGN_KEY_CHECKS=1;