USE dosth;
if exists(select 1 from sysobjects where name='users')
DROP TABLE users;
if exists(select 1 from sysobjects where name='account_role')
DROP TABLE account_role;
if exists(select 1 from sysobjects where name='account')
DROP TABLE account;
if exists(select 1 from sysobjects where name='relation')
DROP TABLE relation;
if exists(select 1 from sysobjects where name='menu')
DROP TABLE  menu;
if exists(select 1 from sysobjects where name='system_info')
DROP TABLE system_info;
if exists(select 1 from sysobjects where name='roles')
DROP TABLE  roles;
if exists(select 1 from sysobjects where name='dept')
DROP TABLE  dept;
if exists(select 1 from sysobjects where name='dict')
DROP TABLE dict;
if exists(select 1 from sysobjects where name='notice')
DROP TABLE notice;
if exists(select 1 from sysobjects where name='operation_log')
DROP TABLE operation_log;
if exists(select 1 from sysobjects where name='login_log')
DROP TABLE login_log;
