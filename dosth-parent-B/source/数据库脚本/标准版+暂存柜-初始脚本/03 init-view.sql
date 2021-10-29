USE tool;
-- ----------------------------
-- View structure for view_dept
-- ----------------------------
DROP VIEW IF EXISTS `view_dept`;
CREATE VIEW `view_dept` AS select `d`.`id` AS `DEPT_ID`,`d`.`p_id` AS `DEPT_P_ID`,`d`.`dept_no` AS `DEPT_NO`,`d`.`dept_name` AS `DEPT_NAME`,`d`.`status` AS `DEPT_STATUS` from `dosth`.`dept` `d` ;

-- ----------------------------
-- View structure for view_user
-- ----------------------------
DROP VIEW IF EXISTS `view_user`;
CREATE VIEW `view_user` AS select `a`.`id` AS `ACCOUNT_ID`,`u`.`dept_id` AS `DEPT_ID`,`u`.`name` AS `USER_NAME`,`a`.`login_name` AS `LOGIN_NAME`,`U`.`ic_card` AS `IC_CARD`,`u`.`start_time` AS `START_TIME`,`u`.`end_time` AS `END_TIME`,`u`.`limit_sum_num` AS `LIMIT_SUM_NUM`,`u`.`not_return_limit_num` AS `NOT_RETURN_LIMIT_NUM`,`u`.`email` AS `EMAIL` from (`dosth`.`account` `a` left join `dosth`.`users` `u` on((`a`.`id` = `u`.`account_id`))) where (`a`.`status` = 'OK') ;
