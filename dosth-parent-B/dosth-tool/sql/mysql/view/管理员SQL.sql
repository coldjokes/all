INSERT INTO relation (id, menu_id, role_id) values (UUID(), '301', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '302', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '303', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '304', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '305', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '306', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '307', '102');
INSERT INTO relation (id, menu_id, role_id) values (UUID(), '308', '102');

INSERT INTO account (id, face_pwd, login_name, password, salt, status) VALUES ('101', 'sDiaLyeh+RwocMiweDJcGA==', 'admin1', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', '3b9j6', 'OK');
INSERT INTO account (id, face_pwd, login_name, password, salt, status) VALUES ('102', 'sDiaLyeh+RwocMiweDJcGA==', 'admin2', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', '3b9j6', 'OK');
INSERT INTO account (id, face_pwd, login_name, password, salt, status) VALUES ('103', 'sDiaLyeh+RwocMiweDJcGA==', 'admin3', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', '3b9j6', 'OK');
INSERT INTO account (id, face_pwd, login_name, password, salt, status) VALUES ('104', 'sDiaLyeh+RwocMiweDJcGA==', 'admin4', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', '3b9j6', 'OK');
INSERT INTO account (id, face_pwd, login_name, password, salt, status) VALUES ('105', 'sDiaLyeh+RwocMiweDJcGA==', 'admin5', 'bbc5b1d5ecbacde6b2075aa8344a1b8e', '3b9j6', 'OK');

INSERT INTO users (id, account_id, name, avatar, createtime, dept_id, start_time, end_time, limit_sum_num, not_return_limit_num, ic_card, face_feature) 
						VALUES (UUID(), '101', 'admin1', 'boy.gif', NOW(), '1', '00:00:00', '23:59:59', '0', '0', NULL, NULL);
INSERT INTO users (id, account_id, name, avatar, createtime, dept_id, start_time, end_time, limit_sum_num, not_return_limit_num, ic_card, face_feature) 
						VALUES (UUID(), '102', 'admin2', 'boy.gif', NOW(), '1', '00:00:00', '23:59:59', '0', '0', NULL, NULL);
INSERT INTO users (id, account_id, name, avatar, createtime, dept_id, start_time, end_time, limit_sum_num, not_return_limit_num, ic_card, face_feature) 
						VALUES (UUID(), '103', 'admin3', 'boy.gif', NOW(), '1', '00:00:00', '23:59:59', '0', '0', NULL, NULL);
INSERT INTO users (id, account_id, name, avatar, createtime, dept_id, start_time, end_time, limit_sum_num, not_return_limit_num, ic_card, face_feature) 
						VALUES (UUID(), '104', 'admin4', 'boy.gif', NOW(), '1', '00:00:00', '23:59:59', '0', '0', NULL, NULL);
INSERT INTO users (id, account_id, name, avatar, createtime, dept_id, start_time, end_time, limit_sum_num, not_return_limit_num, ic_card, face_feature) 
						VALUES (UUID(), '105', 'admin5', 'boy.gif', NOW(), '1', '00:00:00', '23:59:59', '0', '0', NULL, NULL);

INSERT INTO account_role (id, account_id, role_id) VALUES (UUID(), '101', '102');
INSERT INTO account_role (id, account_id, role_id) VALUES (UUID(), '102', '102');
INSERT INTO account_role (id, account_id, role_id) VALUES (UUID(), '103', '102');
INSERT INTO account_role (id, account_id, role_id) VALUES (UUID(), '104', '102');
INSERT INTO account_role (id, account_id, role_id) VALUES (UUID(), '105', '102');

