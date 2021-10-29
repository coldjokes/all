@echo off
rem mysql安装路径(MYSQL路径根据本地安装实际路径修改)
set MYSQL_HOME=D:\mysql-5.7.22-winx64
rem 启动数据库清库脚本
echo start database clean
%MYSQL_HOME%\bin\mysql -uroot -p123456<clean.sql
rem 数据库清库成功
echo database clean success
pause