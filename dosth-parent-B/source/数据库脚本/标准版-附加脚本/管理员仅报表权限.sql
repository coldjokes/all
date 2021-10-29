INSERT INTO relation (id, menu_id, role_id) values (UUID(), '201', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '301', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '401', '102');

INSERT INTO account (id, face_pwd, login_name, password, salt, status) VALUES ('101', 'sDiaLyeh+RwocMiweDJcGA==', 'admin1', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', '3b9j6', 'OK');

INSERT INTO users (id, account_id, name, avatar, createtime, dept_id, start_time, end_time, limit_sum_num, not_return_limit_num, ic_card, face_feature) 
						VALUES (UUID(), '101', 'admin1', 'boy.gif', NOW(), '1', '00:00:00', '23:59:59', '0', '0', NULL, NULL);

INSERT INTO account_role (id, account_id, role_id) VALUES (UUID(), '101', '102');


