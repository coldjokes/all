/*
Navicat MySQL Data Transfer

Source Server         : 192.168.2.46_3306
Source Server Version : 80016
Source Host           : 192.168.2.46:3306
Source Database       : dosth

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2019-11-27 10:42:59
*/

--Use dosth
INSERT INTO `menu` VALUES ('226', 'feedRecordSummary', '', '', '\0', '', '3', '补料记录汇总', '1', 'query', '[tool],[query],', '2', null, '/feedRecordSummary');
INSERT INTO `menu` VALUES ('227', 'useRecordSummary', '', '', '\0', '', '3', '领用记录汇总', '1', 'query', '[tool],[query],', '2', null, '/useRecordSummary');
--Use tool
update mat_use_record set brand = CONCAT(brand,'-');