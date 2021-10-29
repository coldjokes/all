CREATE DATABASE IF NOT EXISTS dosth DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
CREATE DATABASE IF NOT EXISTS tool DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50562
Source Host           : localhost:3306
Source Database       : dosth

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2021-05-11 16:11:40
*/

SET FOREIGN_KEY_CHECKS=0;

USE `dosth`;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `face_pwd` varchar(255) DEFAULT NULL COMMENT '人脸密码',
  `login_name` varchar(50) DEFAULT NULL COMMENT '帐号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `salt` varchar(10) DEFAULT NULL COMMENT '密码盐',
  `status` varchar(10) DEFAULT NULL COMMENT '账户状态：1.启用， 2.冻结 ，3.删除',
  `version` varchar(100) DEFAULT NULL COMMENT '保留字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('1', 'qyJX0sLCwtLIRakoRRMHpQ==', 'admin', 'cd97df6a2429251d7c3b09f0f496cc3a', 'wiwvb', 'OK', null);
INSERT INTO `account` VALUES ('101', 'TiZK2Xa7HhsYFyxIGaksTQ==', 'administrator', 'c0d23d1480f93a4b7fb32ec0f0073797', 'wiwvb', 'OK', null);

-- ----------------------------
-- Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `role_id` varchar(36) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account_role
-- ----------------------------
INSERT INTO `account_role` VALUES ('101', '101', '101');
INSERT INTO `account_role` VALUES ('102', '1', '102');

-- ----------------------------
-- Table structure for advice
-- ----------------------------
DROP TABLE IF EXISTS `advice`;
CREATE TABLE `advice` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `advice_contact` varchar(200) DEFAULT NULL COMMENT '其他联系方式',
  `advice_content` longtext COMMENT '内容',
  `advice_images` varchar(200) DEFAULT NULL COMMENT '头像',
  `advice_mail` varchar(200) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`),
  KEY `FKf9ogbkugxkjxi909nvw25xt77` (`account_id`),
  CONSTRAINT `FKf9ogbkugxkjxi909nvw25xt77` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of advice
-- ----------------------------

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `dept_name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `dept_no` varchar(36) DEFAULT NULL COMMENT '部门编号',
  `full_ids` varchar(255) DEFAULT NULL COMMENT '上级部门ids',
  `p_id` varchar(36) DEFAULT NULL COMMENT '上级部门ID',
  `status` varchar(10) DEFAULT NULL COMMENT '部门状态：1.启用 ，2.冻结， 3.删除',
  PRIMARY KEY (`id`),
  KEY `FKgoxdhh3jhyqumj2q5yvnwu49i` (`p_id`),
  CONSTRAINT `FKgoxdhh3jhyqumj2q5yvnwu49i` FOREIGN KEY (`p_id`) REFERENCES `dept` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES ('1', '总公司', null, null, null, 'OK');
INSERT INTO `dept` VALUES ('402881ba6dd83f93016dd844ec4c0004', '人事部', null, null, '1', 'OK');
INSERT INTO `dept` VALUES ('402881ba6e3b3f37016e3b408c5b0004', '市场部', null, null, '1', 'OK');

-- ----------------------------
-- Table structure for dict
-- ----------------------------
DROP TABLE IF EXISTS `dict`;
CREATE TABLE `dict` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `num` int(11) DEFAULT NULL COMMENT '排序',
  `p_id` varchar(255) DEFAULT NULL COMMENT '父级ID',
  `tips` varchar(50) DEFAULT NULL COMMENT '提示',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dict
-- ----------------------------

-- ----------------------------
-- Table structure for helper
-- ----------------------------
DROP TABLE IF EXISTS `helper`;
CREATE TABLE `helper` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `answer` varchar(200) DEFAULT NULL COMMENT '答案',
  `question_name` varchar(50) DEFAULT NULL COMMENT '问题名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of helper
-- ----------------------------

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '操作帐号',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `ip` varchar(20) DEFAULT NULL COMMENT '登录IP',
  `log_name` varchar(30) DEFAULT NULL COMMENT '日志名称',
  `message` varchar(255) DEFAULT NULL COMMENT '具体消息',
  `succeed` varchar(10) DEFAULT NULL COMMENT '是否成功',
  PRIMARY KEY (`id`),
  KEY `FKfyivueaqqoo22w5vrlaqsai8l` (`account_id`),
  CONSTRAINT `FKfyivueaqqoo22w5vrlaqsai8l` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of login_log
-- ----------------------------
INSERT INTO `login_log` VALUES ('402888fb795a73af01795a7b155f0000', '101', '2021-05-11 16:11:26', '0:0:0:0:0:0:0:1', '登录日志', null, 'SUCCESS');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `code` varchar(50) DEFAULT NULL COMMENT '菜单编号',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `is_menu` bit(1) DEFAULT NULL COMMENT '是否是菜单',
  `is_open` bit(1) DEFAULT NULL COMMENT '是否打开: 1.打开， 0.不打开',
  `is_use` bit(1) DEFAULT NULL COMMENT '菜单状态 : 1.启用， 0.不启用',
  `levels` int(11) DEFAULT NULL COMMENT '菜单层级',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `num` int(11) DEFAULT NULL COMMENT '排序',
  `pcode` varchar(50) DEFAULT NULL COMMENT '菜单父编号',
  `pcodes` varchar(255) DEFAULT NULL COMMENT '当前菜单的所有父菜单编号',
  `system_info_id` varchar(36) DEFAULT NULL COMMENT '系统编号',
  `tips` varchar(255) DEFAULT NULL COMMENT '备注',
  `url` varchar(150) DEFAULT NULL COMMENT 'URL地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b7as01rf085qmbog39pictwrw` (`code`),
  KEY `FK39u6l2dpbrg70x0qbdivm63v7` (`pcode`),
  KEY `FKnieqt5k39twlpao5tuulrdcus` (`system_info_id`),
  CONSTRAINT `FKnieqt5k39twlpao5tuulrdcus` FOREIGN KEY (`system_info_id`) REFERENCES `system_info` (`id`),
  CONSTRAINT `FK39u6l2dpbrg70x0qbdivm63v7` FOREIGN KEY (`pcode`) REFERENCES `menu` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('101', 'system', 'fa-server', '', '', '', '1', '系统管理', '1', null, null, '1', null, '');
INSERT INTO `menu` VALUES ('102', 'equsetting', null, '', '\0', '', '2', '柜体管理', '1', 'system', '[system],', '2', null, '/equsetting');
INSERT INTO `menu` VALUES ('201', 'userSetup', 'fa-user', '', '', '', '1', '人员设置', '1', null, null, '1', null, '');
INSERT INTO `menu` VALUES ('202', 'deptMgr', null, '', '\0', '', '2', '部门设置', '1', 'userSetup', '[userSetup],', '1', null, '/mgrDept');
INSERT INTO `menu` VALUES ('203', 'mgrUser', null, '', '\0', '', '2', '用户设置', '1', 'userSetup', '[userSetup],', '1', null, '/mgrUser');
INSERT INTO `menu` VALUES ('204', 'borrowPopedom', null, '', '\0', '', '2', '权限设置', '1', 'userSetup', '[userSetup],', '2', null, '/borrowPopedom');
INSERT INTO `menu` VALUES ('205', 'dailyLimit', null, '', '\0', '', '2', '限额设置', '1', 'userSetup', '[userSetup],', '2', null, '/dailyLimit');
INSERT INTO `menu` VALUES ('206', 'restitutionType', null, '', '\0', '', '2', '归还设置', '1', 'userSetup', '[userSetup],', '2', null, '/restitutionType');
INSERT INTO `menu` VALUES ('301', 'material', 'fa-reorder', '', '', '', '1', '物料管理', '1', null, null, '2', null, '');
INSERT INTO `menu` VALUES ('302', 'unit', null, '', '\0', '', '2', '单位管理', '1', 'material', '[material],', '2', null, '/unit');
INSERT INTO `menu` VALUES ('303', 'manufacturer', null, '', '\0', '', '2', '供应商管理', '1', 'material', '[material],', '2', null, '/manufacturer');
INSERT INTO `menu` VALUES ('304', 'matequinfo', null, '', '\0', '', '2', '物料录入', '1', 'material', '[material],', '2', null, '/matequinfo');
INSERT INTO `menu` VALUES ('305', 'matCategory', null, '', '\0', '', '2', '物料关联', '1', 'material', '[material],', '2', null, '/matCategory');
INSERT INTO `menu` VALUES ('401', 'tool', 'fa-folder-open-o', '', '', '', '1', '日常作业', '1', null, null, '2', null, '');
INSERT INTO `menu` VALUES ('402', 'matInOrOut', null, '', '\0', '', '2', '物料上下架', '1', 'tool', '[tool],', '2', null, '/matInOrOut');
INSERT INTO `menu` VALUES ('403', 'equdetail', null, '', '\0', '', '2', '库位补料', '1', 'tool', '[tool],', '2', null, '/equdetail');
INSERT INTO `menu` VALUES ('404', 'recycReview', null, '', '\0', '', '3', '回收审核', '1', 'tool', '[tool],', '2', null, '/recycReview');
INSERT INTO `menu` VALUES ('405', 'inventory', null, '', '\0', '', '2', '盘点管理', '1', 'tool', '[tool],', '2', null, '/inventory');
INSERT INTO `menu` VALUES ('501', 'query', 'fa-file-text-o', '', '', '', '1', '报表记录', '1', null, null, '2', null, '');
INSERT INTO `menu` VALUES ('502', 'mainCabinetQuery', null, '', '\0', '', '2', '领用记录', '1', 'query', '[query],', '2', null, '/mainCabinetQuery');
INSERT INTO `menu` VALUES ('503', 'matReturnBackBill', null, '', '\0', '', '2', '归还记录审核', '1', 'query', '[query],', '2', null, '/matReturnBackBill');
INSERT INTO `menu` VALUES ('504', 'stock', null, '', '\0', '', '2', '库存明细', '1', 'query', '[query],', '2', null, '/stock');
INSERT INTO `menu` VALUES ('505', 'feedQuery', null, '', '\0', '', '2', '补料记录', '1', 'query', '[query],', '2', null, '/feedQuery');
INSERT INTO `menu` VALUES ('506', 'lowerFrameQuery', null, '', '\0', '', '2', '下架记录', '1', 'query', '[query],', '2', null, '/lowerFrameQuery');
INSERT INTO `menu` VALUES ('507', 'useRecordSummary', null, '', '\0', '', '2', '领用汇总', '1', 'query', '[query],', '2', null, '/useRecordSummary');
INSERT INTO `menu` VALUES ('508', 'feedRecordSummary', null, '', '\0', '', '2', '补料汇总', '1', 'query', '[query],', '2', null, '/feedRecordSummary');
INSERT INTO `menu` VALUES ('509', 'categorySummary', null, '', '\0', '', '2', '领用途径汇总', '1', 'query', '[query],', '2', null, '/useRecordSummary/category');
INSERT INTO `menu` VALUES ('510', 'deptSummary', null, '', '\0', '', '2', '领用部门汇总', '1', 'query', '[query],', '2', null, '/useRecordSummary/dept');
INSERT INTO `menu` VALUES ('511', 'viceCabinetQuery', null, '', '\0', '', '2', '暂存柜领用记录', '1', 'query', '[query],', '2', null, '/viceCabinetQuery');
INSERT INTO `menu` VALUES ('512', 'inventoryQuery', null, '', '\0', '', '2', '盘点记录', '1', 'query', '[query],', '2', null, '/inventory/query');
INSERT INTO `menu` VALUES ('601', 'systemSet', 'fa-cog', '', '', '', '1', '系统设置', '1', null, null, '2', null, '');
INSERT INTO `menu` VALUES ('602', 'clearData', null, '', '\0', '', '2', '清理数据', '1', 'systemSet', '[systemSet],', '2', null, '/clearData');
INSERT INTO `menu` VALUES ('603', 'noticeMgr', null, '', '\0', '', '2', '通知管理', '1', 'systemSet', '[systemSet],', '2', null, '/noticeMgr');
INSERT INTO `menu` VALUES ('604', 'timeTask', null, '', '\0', '', '2', '定时任务', '1', 'systemSet', '[systemSet],', '2', null, '/timeTask');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` bigint(20) DEFAULT NULL COMMENT '创建人',
  `title` varchar(50) DEFAULT NULL COMMENT '标题',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notice
-- ----------------------------

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '账户ID',
  `class_name` varchar(200) DEFAULT NULL COMMENT '类名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `log_name` varchar(50) DEFAULT NULL COMMENT '日志名称',
  `log_type` varchar(10) DEFAULT NULL COMMENT '日志类型',
  `message` longtext COMMENT '备注',
  `method` varchar(36) DEFAULT NULL COMMENT '方法名称',
  `succeed` varchar(10) DEFAULT NULL COMMENT '是否成功',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of operation_log
-- ----------------------------
INSERT INTO `operation_log` VALUES ('402888fb795a73af01795a7b19f40001', '101', null, '2021-05-11 16:11:27', '', 'EXCEPTION', 'java.lang.RuntimeException: com.netflix.client.ClientException: Load balancer does not have available server for client: service-tool\r\n	at org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute(LoadBalancerFeignClient.java:71)\r\n	at feign.SynchronousMethodHandler.executeAndDecode(SynchronousMethodHandler.java:97)\r\n	at feign.SynchronousMethodHandler.invoke(SynchronousMethodHandler.java:76)\r\n	at feign.ReflectiveFeignTFeignInvocationHandler.invoke(ReflectiveFeign.java:103)\r\n	at com.sun.proxy.TProxy156.getThrYearPriceSumGroupByMonth(Unknown Source)\r\n	at com.dosth.admin.controller.BlackboardController.getCompThrYear(BlackboardController.java:62)\r\n	at com.dosth.admin.controller.BlackboardControllerTTFastClassBySpringCGLIBTTf7ad2e37.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)\r\n	at org.springframework.aop.framework.CglibAopProxyTCglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:738)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:85)\r\n	at com.dosth.common.intercept.SessionTimeoutInterceptor.sessionTimeoutValidate(SessionTimeoutInterceptor.java:56)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:629)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:618)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:85)\r\n	at com.dosth.common.intercept.SessionInterceptor.sessionKit(SessionInterceptor.java:30)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:629)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:618)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:92)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:673)\r\n	at com.dosth.admin.controller.BlackboardControllerTTEnhancerBySpringCGLIBTTe51caa17.getCompThrYear(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)\r\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:635)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:742)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:61)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.executeChain(AbstractShiroFilter.java:449)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilterT1.call(AbstractShiroFilter.java:365)\r\n	at org.apache.shiro.subject.support.SubjectCallable.doCall(SubjectCallable.java:90)\r\n	at org.apache.shiro.subject.support.SubjectCallable.call(SubjectCallable.java:83)\r\n	at org.apache.shiro.subject.support.DelegatingSubject.execute(DelegatingSubject.java:387)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:362)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.dosth.common.xss.XssFilter.doFilter(XssFilter.java:46)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:123)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:108)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:478)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:803)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\r\n	at org.apache.coyote.AbstractProtocolTConnectionHandler.process(AbstractProtocol.java:868)\r\n	at org.apache.tomcat.util.net.NioEndpointTSocketProcessor.doRun(NioEndpoint.java:1459)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\r\n	at java.util.concurrent.ThreadPoolExecutorTWorker.run(ThreadPoolExecutor.java:624)\r\n	at org.apache.tomcat.util.threads.TaskThreadTWrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:748)\r\nCaused by: com.netflix.client.ClientException: Load balancer does not have available server for client: service-tool\r\n	at com.netflix.loadbalancer.LoadBalancerContext.getServerFromLoadBalancer(LoadBalancerContext.java:483)\r\n	at com.netflix.loadbalancer.reactive.LoadBalancerCommandT1.call(LoadBalancerCommand.java:184)\r\n	at com.netflix.loadbalancer.reactive.LoadBalancerCommandT1.call(LoadBalancerCommand.java:180)\r\n	at rx.Observable.unsafeSubscribe(Observable.java:10211)\r\n	at rx.internal.operators.OnSubscribeConcatMap.call(OnSubscribeConcatMap.java:94)\r\n	at rx.internal.operators.OnSubscribeConcatMap.call(OnSubscribeConcatMap.java:42)\r\n	at rx.Observable.unsafeSubscribe(Observable.java:10211)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriberT1.call(OperatorRetryWithPredicate.java:127)\r\n	at rx.internal.schedulers.TrampolineSchedulerTInnerCurrentThreadScheduler.enqueue(TrampolineScheduler.java:73)\r\n	at rx.internal.schedulers.TrampolineSchedulerTInnerCurrentThreadScheduler.schedule(TrampolineScheduler.java:52)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriber.onNext(OperatorRetryWithPredicate.java:79)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriber.onNext(OperatorRetryWithPredicate.java:45)\r\n	at rx.internal.util.ScalarSynchronousObservableTWeakSingleProducer.request(ScalarSynchronousObservable.java:276)\r\n	at rx.Subscriber.setProducer(Subscriber.java:209)\r\n	at rx.internal.util.ScalarSynchronousObservableTJustOnSubscribe.call(ScalarSynchronousObservable.java:138)\r\n	at rx.internal.util.ScalarSynchronousObservableTJustOnSubscribe.call(ScalarSynchronousObservable.java:129)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.Observable.subscribe(Observable.java:10307)\r\n	at rx.Observable.subscribe(Observable.java:10274)\r\n	at rx.observables.BlockingObservable.blockForSingle(BlockingObservable.java:445)\r\n	at rx.observables.BlockingObservable.single(BlockingObservable.java:342)\r\n	at com.netflix.client.AbstractLoadBalancerAwareClient.executeWithLoadBalancer(AbstractLoadBalancerAwareClient.java:117)\r\n	at org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute(LoadBalancerFeignClient.java:63)\r\n	... 107 more\r\n', null, 'FAIL');
INSERT INTO `operation_log` VALUES ('402888fb795a73af01795a7b19f90002', '101', null, '2021-05-11 16:11:27', '', 'EXCEPTION', 'java.lang.RuntimeException: com.netflix.client.ClientException: Load balancer does not have available server for client: service-tool\r\n	at org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute(LoadBalancerFeignClient.java:71)\r\n	at feign.SynchronousMethodHandler.executeAndDecode(SynchronousMethodHandler.java:97)\r\n	at feign.SynchronousMethodHandler.invoke(SynchronousMethodHandler.java:76)\r\n	at feign.ReflectiveFeignTFeignInvocationHandler.invoke(ReflectiveFeign.java:103)\r\n	at com.sun.proxy.TProxy156.getThrYCntGroupByMatType(Unknown Source)\r\n	at com.dosth.admin.controller.BlackboardController.getThrYCntGroupByMatType(BlackboardController.java:100)\r\n	at com.dosth.admin.controller.BlackboardControllerTTFastClassBySpringCGLIBTTf7ad2e37.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)\r\n	at org.springframework.aop.framework.CglibAopProxyTCglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:738)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:85)\r\n	at com.dosth.common.intercept.SessionTimeoutInterceptor.sessionTimeoutValidate(SessionTimeoutInterceptor.java:56)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:629)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:618)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:85)\r\n	at com.dosth.common.intercept.SessionInterceptor.sessionKit(SessionInterceptor.java:30)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:629)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:618)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:92)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:673)\r\n	at com.dosth.admin.controller.BlackboardControllerTTEnhancerBySpringCGLIBTTe51caa17.getThrYCntGroupByMatType(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)\r\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:635)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:742)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:61)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.executeChain(AbstractShiroFilter.java:449)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilterT1.call(AbstractShiroFilter.java:365)\r\n	at org.apache.shiro.subject.support.SubjectCallable.doCall(SubjectCallable.java:90)\r\n	at org.apache.shiro.subject.support.SubjectCallable.call(SubjectCallable.java:83)\r\n	at org.apache.shiro.subject.support.DelegatingSubject.execute(DelegatingSubject.java:387)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:362)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.dosth.common.xss.XssFilter.doFilter(XssFilter.java:46)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:123)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:108)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:478)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:803)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\r\n	at org.apache.coyote.AbstractProtocolTConnectionHandler.process(AbstractProtocol.java:868)\r\n	at org.apache.tomcat.util.net.NioEndpointTSocketProcessor.doRun(NioEndpoint.java:1459)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\r\n	at java.util.concurrent.ThreadPoolExecutorTWorker.run(ThreadPoolExecutor.java:624)\r\n	at org.apache.tomcat.util.threads.TaskThreadTWrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:748)\r\nCaused by: com.netflix.client.ClientException: Load balancer does not have available server for client: service-tool\r\n	at com.netflix.loadbalancer.LoadBalancerContext.getServerFromLoadBalancer(LoadBalancerContext.java:483)\r\n	at com.netflix.loadbalancer.reactive.LoadBalancerCommandT1.call(LoadBalancerCommand.java:184)\r\n	at com.netflix.loadbalancer.reactive.LoadBalancerCommandT1.call(LoadBalancerCommand.java:180)\r\n	at rx.Observable.unsafeSubscribe(Observable.java:10211)\r\n	at rx.internal.operators.OnSubscribeConcatMap.call(OnSubscribeConcatMap.java:94)\r\n	at rx.internal.operators.OnSubscribeConcatMap.call(OnSubscribeConcatMap.java:42)\r\n	at rx.Observable.unsafeSubscribe(Observable.java:10211)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriberT1.call(OperatorRetryWithPredicate.java:127)\r\n	at rx.internal.schedulers.TrampolineSchedulerTInnerCurrentThreadScheduler.enqueue(TrampolineScheduler.java:73)\r\n	at rx.internal.schedulers.TrampolineSchedulerTInnerCurrentThreadScheduler.schedule(TrampolineScheduler.java:52)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriber.onNext(OperatorRetryWithPredicate.java:79)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriber.onNext(OperatorRetryWithPredicate.java:45)\r\n	at rx.internal.util.ScalarSynchronousObservableTWeakSingleProducer.request(ScalarSynchronousObservable.java:276)\r\n	at rx.Subscriber.setProducer(Subscriber.java:209)\r\n	at rx.internal.util.ScalarSynchronousObservableTJustOnSubscribe.call(ScalarSynchronousObservable.java:138)\r\n	at rx.internal.util.ScalarSynchronousObservableTJustOnSubscribe.call(ScalarSynchronousObservable.java:129)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.Observable.subscribe(Observable.java:10307)\r\n	at rx.Observable.subscribe(Observable.java:10274)\r\n	at rx.observables.BlockingObservable.blockForSingle(BlockingObservable.java:445)\r\n	at rx.observables.BlockingObservable.single(BlockingObservable.java:342)\r\n	at com.netflix.client.AbstractLoadBalancerAwareClient.executeWithLoadBalancer(AbstractLoadBalancerAwareClient.java:117)\r\n	at org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute(LoadBalancerFeignClient.java:63)\r\n	... 107 more\r\n', null, 'FAIL');
INSERT INTO `operation_log` VALUES ('402888fb795a73af01795a7b19fa0003', '101', null, '2021-05-11 16:11:27', '', 'EXCEPTION', 'java.lang.RuntimeException: com.netflix.client.ClientException: Load balancer does not have available server for client: service-tool\r\n	at org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute(LoadBalancerFeignClient.java:71)\r\n	at feign.SynchronousMethodHandler.executeAndDecode(SynchronousMethodHandler.java:97)\r\n	at feign.SynchronousMethodHandler.invoke(SynchronousMethodHandler.java:76)\r\n	at feign.ReflectiveFeignTFeignInvocationHandler.invoke(ReflectiveFeign.java:103)\r\n	at com.sun.proxy.TProxy156.getBorrowNumGroupByDept(Unknown Source)\r\n	at com.dosth.admin.controller.BlackboardController.getBorrowNumGroupByDept(BlackboardController.java:110)\r\n	at com.dosth.admin.controller.BlackboardControllerTTFastClassBySpringCGLIBTTf7ad2e37.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)\r\n	at org.springframework.aop.framework.CglibAopProxyTCglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:738)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:85)\r\n	at com.dosth.common.intercept.SessionTimeoutInterceptor.sessionTimeoutValidate(SessionTimeoutInterceptor.java:56)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:629)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:618)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:85)\r\n	at com.dosth.common.intercept.SessionInterceptor.sessionKit(SessionInterceptor.java:30)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:629)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:618)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:92)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:673)\r\n	at com.dosth.admin.controller.BlackboardControllerTTEnhancerBySpringCGLIBTTe51caa17.getBorrowNumGroupByDept(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)\r\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:635)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:742)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:61)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.executeChain(AbstractShiroFilter.java:449)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilterT1.call(AbstractShiroFilter.java:365)\r\n	at org.apache.shiro.subject.support.SubjectCallable.doCall(SubjectCallable.java:90)\r\n	at org.apache.shiro.subject.support.SubjectCallable.call(SubjectCallable.java:83)\r\n	at org.apache.shiro.subject.support.DelegatingSubject.execute(DelegatingSubject.java:387)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:362)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.dosth.common.xss.XssFilter.doFilter(XssFilter.java:46)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:123)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:108)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:478)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:803)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\r\n	at org.apache.coyote.AbstractProtocolTConnectionHandler.process(AbstractProtocol.java:868)\r\n	at org.apache.tomcat.util.net.NioEndpointTSocketProcessor.doRun(NioEndpoint.java:1459)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\r\n	at java.util.concurrent.ThreadPoolExecutorTWorker.run(ThreadPoolExecutor.java:624)\r\n	at org.apache.tomcat.util.threads.TaskThreadTWrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:748)\r\nCaused by: com.netflix.client.ClientException: Load balancer does not have available server for client: service-tool\r\n	at com.netflix.loadbalancer.LoadBalancerContext.getServerFromLoadBalancer(LoadBalancerContext.java:483)\r\n	at com.netflix.loadbalancer.reactive.LoadBalancerCommandT1.call(LoadBalancerCommand.java:184)\r\n	at com.netflix.loadbalancer.reactive.LoadBalancerCommandT1.call(LoadBalancerCommand.java:180)\r\n	at rx.Observable.unsafeSubscribe(Observable.java:10211)\r\n	at rx.internal.operators.OnSubscribeConcatMap.call(OnSubscribeConcatMap.java:94)\r\n	at rx.internal.operators.OnSubscribeConcatMap.call(OnSubscribeConcatMap.java:42)\r\n	at rx.Observable.unsafeSubscribe(Observable.java:10211)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriberT1.call(OperatorRetryWithPredicate.java:127)\r\n	at rx.internal.schedulers.TrampolineSchedulerTInnerCurrentThreadScheduler.enqueue(TrampolineScheduler.java:73)\r\n	at rx.internal.schedulers.TrampolineSchedulerTInnerCurrentThreadScheduler.schedule(TrampolineScheduler.java:52)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriber.onNext(OperatorRetryWithPredicate.java:79)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriber.onNext(OperatorRetryWithPredicate.java:45)\r\n	at rx.internal.util.ScalarSynchronousObservableTWeakSingleProducer.request(ScalarSynchronousObservable.java:276)\r\n	at rx.Subscriber.setProducer(Subscriber.java:209)\r\n	at rx.internal.util.ScalarSynchronousObservableTJustOnSubscribe.call(ScalarSynchronousObservable.java:138)\r\n	at rx.internal.util.ScalarSynchronousObservableTJustOnSubscribe.call(ScalarSynchronousObservable.java:129)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.Observable.subscribe(Observable.java:10307)\r\n	at rx.Observable.subscribe(Observable.java:10274)\r\n	at rx.observables.BlockingObservable.blockForSingle(BlockingObservable.java:445)\r\n	at rx.observables.BlockingObservable.single(BlockingObservable.java:342)\r\n	at com.netflix.client.AbstractLoadBalancerAwareClient.executeWithLoadBalancer(AbstractLoadBalancerAwareClient.java:117)\r\n	at org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute(LoadBalancerFeignClient.java:63)\r\n	... 107 more\r\n', null, 'FAIL');
INSERT INTO `operation_log` VALUES ('402888fb795a73af01795a7b19fb0004', '101', null, '2021-05-11 16:11:27', '', 'EXCEPTION', 'java.lang.RuntimeException: com.netflix.client.ClientException: Load balancer does not have available server for client: service-tool\r\n	at org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute(LoadBalancerFeignClient.java:71)\r\n	at feign.SynchronousMethodHandler.executeAndDecode(SynchronousMethodHandler.java:97)\r\n	at feign.SynchronousMethodHandler.invoke(SynchronousMethodHandler.java:76)\r\n	at feign.ReflectiveFeignTFeignInvocationHandler.invoke(ReflectiveFeign.java:103)\r\n	at com.sun.proxy.TProxy156.getCurMonthGroupByMat(Unknown Source)\r\n	at com.dosth.admin.controller.BlackboardController.getCurMonthPie(BlackboardController.java:89)\r\n	at com.dosth.admin.controller.BlackboardControllerTTFastClassBySpringCGLIBTTf7ad2e37.invoke(<generated>)\r\n	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:204)\r\n	at org.springframework.aop.framework.CglibAopProxyTCglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:738)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:157)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:85)\r\n	at com.dosth.common.intercept.SessionTimeoutInterceptor.sessionTimeoutValidate(SessionTimeoutInterceptor.java:56)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:629)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:618)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint.proceed(MethodInvocationProceedingJoinPoint.java:85)\r\n	at com.dosth.common.intercept.SessionInterceptor.sessionKit(SessionInterceptor.java:30)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethodWithGivenArgs(AbstractAspectJAdvice.java:629)\r\n	at org.springframework.aop.aspectj.AbstractAspectJAdvice.invokeAdviceMethod(AbstractAspectJAdvice.java:618)\r\n	at org.springframework.aop.aspectj.AspectJAroundAdvice.invoke(AspectJAroundAdvice.java:70)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:92)\r\n	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)\r\n	at org.springframework.aop.framework.CglibAopProxyTDynamicAdvisedInterceptor.intercept(CglibAopProxy.java:673)\r\n	at com.dosth.admin.controller.BlackboardControllerTTEnhancerBySpringCGLIBTTe51caa17.getCurMonthPie(<generated>)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)\r\n	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)\r\n	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\r\n	at java.lang.reflect.Method.invoke(Method.java:498)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205)\r\n	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133)\r\n	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827)\r\n	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738)\r\n	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85)\r\n	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967)\r\n	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901)\r\n	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970)\r\n	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:635)\r\n	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846)\r\n	at javax.servlet.http.HttpServlet.service(HttpServlet.java:742)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:61)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.executeChain(AdviceFilter.java:108)\r\n	at org.apache.shiro.web.servlet.AdviceFilter.doFilterInternal(AdviceFilter.java:137)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)\r\n	at org.apache.shiro.web.servlet.ProxiedFilterChain.doFilter(ProxiedFilterChain.java:66)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.executeChain(AbstractShiroFilter.java:449)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilterT1.call(AbstractShiroFilter.java:365)\r\n	at org.apache.shiro.subject.support.SubjectCallable.doCall(SubjectCallable.java:90)\r\n	at org.apache.shiro.subject.support.SubjectCallable.call(SubjectCallable.java:83)\r\n	at org.apache.shiro.subject.support.DelegatingSubject.execute(DelegatingSubject.java:387)\r\n	at org.apache.shiro.web.servlet.AbstractShiroFilter.doFilterInternal(AbstractShiroFilter.java:362)\r\n	at org.apache.shiro.web.servlet.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:125)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.dosth.common.xss.XssFilter.doFilter(XssFilter.java:46)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at com.alibaba.druid.support.http.WebStatFilter.doFilter(WebStatFilter.java:123)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:108)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197)\r\n	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107)\r\n	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193)\r\n	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166)\r\n	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199)\r\n	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96)\r\n	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:478)\r\n	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140)\r\n	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81)\r\n	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87)\r\n	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342)\r\n	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:803)\r\n	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66)\r\n	at org.apache.coyote.AbstractProtocolTConnectionHandler.process(AbstractProtocol.java:868)\r\n	at org.apache.tomcat.util.net.NioEndpointTSocketProcessor.doRun(NioEndpoint.java:1459)\r\n	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)\r\n	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\r\n	at java.util.concurrent.ThreadPoolExecutorTWorker.run(ThreadPoolExecutor.java:624)\r\n	at org.apache.tomcat.util.threads.TaskThreadTWrappingRunnable.run(TaskThread.java:61)\r\n	at java.lang.Thread.run(Thread.java:748)\r\nCaused by: com.netflix.client.ClientException: Load balancer does not have available server for client: service-tool\r\n	at com.netflix.loadbalancer.LoadBalancerContext.getServerFromLoadBalancer(LoadBalancerContext.java:483)\r\n	at com.netflix.loadbalancer.reactive.LoadBalancerCommandT1.call(LoadBalancerCommand.java:184)\r\n	at com.netflix.loadbalancer.reactive.LoadBalancerCommandT1.call(LoadBalancerCommand.java:180)\r\n	at rx.Observable.unsafeSubscribe(Observable.java:10211)\r\n	at rx.internal.operators.OnSubscribeConcatMap.call(OnSubscribeConcatMap.java:94)\r\n	at rx.internal.operators.OnSubscribeConcatMap.call(OnSubscribeConcatMap.java:42)\r\n	at rx.Observable.unsafeSubscribe(Observable.java:10211)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriberT1.call(OperatorRetryWithPredicate.java:127)\r\n	at rx.internal.schedulers.TrampolineSchedulerTInnerCurrentThreadScheduler.enqueue(TrampolineScheduler.java:73)\r\n	at rx.internal.schedulers.TrampolineSchedulerTInnerCurrentThreadScheduler.schedule(TrampolineScheduler.java:52)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriber.onNext(OperatorRetryWithPredicate.java:79)\r\n	at rx.internal.operators.OperatorRetryWithPredicateTSourceSubscriber.onNext(OperatorRetryWithPredicate.java:45)\r\n	at rx.internal.util.ScalarSynchronousObservableTWeakSingleProducer.request(ScalarSynchronousObservable.java:276)\r\n	at rx.Subscriber.setProducer(Subscriber.java:209)\r\n	at rx.internal.util.ScalarSynchronousObservableTJustOnSubscribe.call(ScalarSynchronousObservable.java:138)\r\n	at rx.internal.util.ScalarSynchronousObservableTJustOnSubscribe.call(ScalarSynchronousObservable.java:129)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:48)\r\n	at rx.internal.operators.OnSubscribeLift.call(OnSubscribeLift.java:30)\r\n	at rx.Observable.subscribe(Observable.java:10307)\r\n	at rx.Observable.subscribe(Observable.java:10274)\r\n	at rx.observables.BlockingObservable.blockForSingle(BlockingObservable.java:445)\r\n	at rx.observables.BlockingObservable.single(BlockingObservable.java:342)\r\n	at com.netflix.client.AbstractLoadBalancerAwareClient.executeWithLoadBalancer(AbstractLoadBalancerAwareClient.java:117)\r\n	at org.springframework.cloud.netflix.feign.ribbon.LoadBalancerFeignClient.execute(LoadBalancerFeignClient.java:63)\r\n	... 107 more\r\n', null, 'FAIL');

-- ----------------------------
-- Table structure for phone_user
-- ----------------------------
DROP TABLE IF EXISTS `phone_user`;
CREATE TABLE `phone_user` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `app_pwd` varchar(200) DEFAULT NULL COMMENT '注册密码',
  `dept_name` varchar(50) DEFAULT NULL COMMENT '部门名称码',
  `emp_no` varchar(20) DEFAULT NULL COMMENT '员工号',
  `phone_no` varchar(30) DEFAULT NULL COMMENT '电话号码',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_user
-- ----------------------------

-- ----------------------------
-- Table structure for relation
-- ----------------------------
DROP TABLE IF EXISTS `relation`;
CREATE TABLE `relation` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `menu_id` varchar(36) DEFAULT NULL COMMENT '菜单ID',
  `role_id` varchar(36) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  KEY `FK8cbvofhycq4uutsfxlwx81778` (`menu_id`),
  KEY `FKe5c8lq05rwogey5ojq5btium1` (`role_id`),
  CONSTRAINT `FKe5c8lq05rwogey5ojq5btium1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK8cbvofhycq4uutsfxlwx81778` FOREIGN KEY (`menu_id`) REFERENCES `menu` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of relation
-- ----------------------------
INSERT INTO `relation` VALUES ('101', '101', '101');
INSERT INTO `relation` VALUES ('102', '201', '101');
INSERT INTO `relation` VALUES ('103', '301', '101');
INSERT INTO `relation` VALUES ('104', '401', '101');
INSERT INTO `relation` VALUES ('105', '501', '101');
INSERT INTO `relation` VALUES ('106', '601', '101');
INSERT INTO `relation` VALUES ('107', '201', '102');
INSERT INTO `relation` VALUES ('108', '301', '102');
INSERT INTO `relation` VALUES ('109', '401', '102');
INSERT INTO `relation` VALUES ('110', '501', '102');
INSERT INTO `relation` VALUES ('111', '601', '102');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `dept_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `name` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `num` int(11) DEFAULT NULL COMMENT '序号',
  `p_id` varchar(36) DEFAULT NULL COMMENT '父角色ID',
  `tips` varchar(50) DEFAULT NULL COMMENT '提示',
  `version` bigint(20) DEFAULT NULL COMMENT '保留字段',
  PRIMARY KEY (`id`),
  KEY `FK8qr34liiiam0dccv9tio1n307` (`dept_id`),
  KEY `FKl9f18m8yo0ogxubacp61t0xnb` (`p_id`),
  CONSTRAINT `FKl9f18m8yo0ogxubacp61t0xnb` FOREIGN KEY (`p_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FK8qr34liiiam0dccv9tio1n307` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('101', '1', '超级管理员', '1', null, 'administrator', '1');
INSERT INTO `roles` VALUES ('102', '1', '管理员', '2', '101', 'admin', null);

-- ----------------------------
-- Table structure for system_info
-- ----------------------------
DROP TABLE IF EXISTS `system_info`;
CREATE TABLE `system_info` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `system_name` varchar(50) DEFAULT NULL COMMENT '系统名称',
  `url` varchar(50) DEFAULT NULL COMMENT '系统URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system_info
-- ----------------------------
INSERT INTO `system_info` VALUES ('1', null, '部门用户设置', '8081');
INSERT INTO `system_info` VALUES ('2', null, '基础信息和报表', '8082');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `dept_id` varchar(50) DEFAULT NULL COMMENT '部门ID',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `end_time` varchar(50) DEFAULT NULL COMMENT '限额结束时间',
  `face_feature` longblob COMMENT '人臉特征',
  `ic_card` varchar(50) DEFAULT NULL COMMENT 'IC卡号',
  `limit_sum_num` int(36) DEFAULT NULL COMMENT '限额总数',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `not_return_limit_num` int(36) DEFAULT NULL COMMENT '未归还限额',
  `start_time` varchar(50) DEFAULT NULL COMMENT '限额开始时间',
  PRIMARY KEY (`id`),
  KEY `FK3pwaj86pwopu3ot96qlrfo2up` (`account_id`),
  KEY `FKb5g26hfoj5g0fim8tth33aiax` (`dept_id`),
  CONSTRAINT `FKb5g26hfoj5g0fim8tth33aiax` FOREIGN KEY (`dept_id`) REFERENCES `dept` (`id`),
  CONSTRAINT `FK3pwaj86pwopu3ot96qlrfo2up` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '1', 'admin.jpg', '2020-01-01 00:00:00', '1', null, '23:59:59', null, null, '0', 'admin', '0', '00:00:00');
INSERT INTO `users` VALUES ('101', '101', 'admin.jpg', '2020-01-01 00:00:00', '1', null, '23:59:59', null, null, '0', 'administrator', '0', '00:00:00');

/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50562
Source Host           : localhost:3306
Source Database       : tool

Target Server Type    : MYSQL
Target Server Version : 50562
File Encoding         : 65001

Date: 2021-05-11 16:11:49
*/

SET FOREIGN_KEY_CHECKS=0;

USE `tool`;

-- ----------------------------
-- Table structure for borrow_info
-- ----------------------------
DROP TABLE IF EXISTS `borrow_info`;
CREATE TABLE `borrow_info` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `knife_id` varchar(255) DEFAULT NULL COMMENT '物料编号',
  `knife_image` varchar(255) DEFAULT NULL COMMENT '图片',
  `knife_name` varchar(255) DEFAULT NULL COMMENT '物料名称',
  `num` varchar(255) DEFAULT NULL COMMENT '借出数量',
  `return_info` varchar(255) DEFAULT NULL COMMENT '归还详情',
  `time` varchar(255) DEFAULT NULL COMMENT '归还时间',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(255) DEFAULT NULL COMMENT '归还人员',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of borrow_info
-- ----------------------------

-- ----------------------------
-- Table structure for borrow_popedom
-- ----------------------------
DROP TABLE IF EXISTS `borrow_popedom`;
CREATE TABLE `borrow_popedom` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐户ID',
  `borrow_popedom` varchar(10) DEFAULT NULL COMMENT '物料借出权限',
  `popedoms` longtext COMMENT '权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of borrow_popedom
-- ----------------------------

-- ----------------------------
-- Table structure for cabinet_plc_setting
-- ----------------------------
DROP TABLE IF EXISTS `cabinet_plc_setting`;
CREATE TABLE `cabinet_plc_setting` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `cabinet_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `plc_setting_id` varchar(36) DEFAULT NULL COMMENT 'plc参数ID',
  `setting_val` varchar(50) DEFAULT NULL COMMENT '参数值',
  PRIMARY KEY (`id`),
  KEY `FKpu1jdm66qwjp5g4a06q1hjqfv` (`plc_setting_id`),
  CONSTRAINT `FKpu1jdm66qwjp5g4a06q1hjqfv` FOREIGN KEY (`plc_setting_id`) REFERENCES `plc_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cabinet_plc_setting
-- ----------------------------

-- ----------------------------
-- Table structure for cabinet_setup
-- ----------------------------
DROP TABLE IF EXISTS `cabinet_setup`;
CREATE TABLE `cabinet_setup` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `equ_setting_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `setup_key` varchar(36) DEFAULT NULL COMMENT '参数名',
  `setup_value` varchar(36) DEFAULT NULL COMMENT '参数值',
  PRIMARY KEY (`id`),
  KEY `FK4f1es6f7tnk9m1v1qbfie6e04` (`equ_setting_id`),
  CONSTRAINT `FK4f1es6f7tnk9m1v1qbfie6e04` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cabinet_setup
-- ----------------------------

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `add_time` datetime DEFAULT NULL COMMENT '添加时间',
  `cabinet_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `mat_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `num` int(11) DEFAULT NULL COMMENT '数量',
  `receive_info` varchar(36) DEFAULT NULL COMMENT '领取类型',
  `receive_type` varchar(36) DEFAULT NULL COMMENT '领取类型',
  PRIMARY KEY (`id`),
  KEY `FKob1hwkxgjcjruji7ek6f2hcbe` (`mat_id`),
  CONSTRAINT `FKob1hwkxgjcjruji7ek6f2hcbe` FOREIGN KEY (`mat_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for daily_limit
-- ----------------------------
DROP TABLE IF EXISTS `daily_limit`;
CREATE TABLE `daily_limit` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `cur_num` int(11) DEFAULT NULL COMMENT '当前领取数量',
  `limit_num` int(11) DEFAULT NULL COMMENT '限额数量',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `not_return_num` int(11) DEFAULT NULL COMMENT '未归还限额',
  `op_date` datetime DEFAULT NULL COMMENT '领用时间',
  PRIMARY KEY (`id`),
  KEY `FK863lo7fsnuvce9tkcitrqkf30` (`mat_info_id`),
  CONSTRAINT `FK863lo7fsnuvce9tkcitrqkf30` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of daily_limit
-- ----------------------------

-- ----------------------------
-- Table structure for data_sync_state
-- ----------------------------
DROP TABLE IF EXISTS `data_sync_state`;
CREATE TABLE `data_sync_state` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `cabinet_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `data_sync_type` varchar(10) DEFAULT NULL COMMENT '同步类型',
  `sync_time` datetime DEFAULT NULL COMMENT '同步时间',
  PRIMARY KEY (`id`),
  KEY `FKldujxpj4noyb2l2hjm3t1j867` (`cabinet_id`),
  CONSTRAINT `FKldujxpj4noyb2l2hjm3t1j867` FOREIGN KEY (`cabinet_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of data_sync_state
-- ----------------------------

-- ----------------------------
-- Table structure for dept_borrow_popedom
-- ----------------------------
DROP TABLE IF EXISTS `dept_borrow_popedom`;
CREATE TABLE `dept_borrow_popedom` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `borrow_popedom` varchar(10) DEFAULT NULL COMMENT '物料借出权限',
  `dept_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `popedoms` longtext COMMENT '权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dept_borrow_popedom
-- ----------------------------

-- ----------------------------
-- Table structure for equ_detail
-- ----------------------------
DROP TABLE IF EXISTS `equ_detail`;
CREATE TABLE `equ_detail` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `equ_setting_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP',
  `level_ht` int(11) DEFAULT NULL COMMENT '层高',
  `port` varchar(10) DEFAULT NULL COMMENT '端口',
  `row_no` int(11) DEFAULT NULL COMMENT '行号',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`),
  KEY `FK6b33i7tm2ilp0cr364t03aeay` (`equ_setting_id`),
  CONSTRAINT `FK6b33i7tm2ilp0cr364t03aeay` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equ_detail
-- ----------------------------

-- ----------------------------
-- Table structure for equ_detail_sta
-- ----------------------------
DROP TABLE IF EXISTS `equ_detail_sta`;
CREATE TABLE `equ_detail_sta` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `board_no` int(11) DEFAULT NULL COMMENT '栈号',
  `box_index` int(11) DEFAULT NULL COMMENT '索引号',
  `col_no` int(11) DEFAULT NULL COMMENT '列号',
  `comm` varchar(10) DEFAULT NULL COMMENT '串口号',
  `cur_num` int(11) DEFAULT NULL COMMENT '当前数量',
  `equ_detail_id` varchar(36) DEFAULT NULL COMMENT '托盘ID',
  `equ_sta` varchar(10) DEFAULT NULL COMMENT '设备状态',
  `interval_val` int(11) DEFAULT NULL COMMENT '间隔',
  `last_feed_time` datetime DEFAULT NULL COMMENT '最后上架时间称',
  `lock_index` int(11) DEFAULT NULL COMMENT '电磁锁索引号',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `max_reserve` int(11) DEFAULT NULL COMMENT '最大存储',
  `sta_name` varchar(50) DEFAULT NULL COMMENT '货位名称',
  `status` varchar(10) DEFAULT NULL COMMENT '启用状态',
  `warn_val` int(11) DEFAULT NULL COMMENT '警告阀值',
  PRIMARY KEY (`id`),
  KEY `FKte66q10nvy5v7inqe0pehjq78` (`equ_detail_id`),
  KEY `FKwpfkg6meu3gyxd9jquxb4aew` (`mat_info_id`),
  CONSTRAINT `FKwpfkg6meu3gyxd9jquxb4aew` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FKte66q10nvy5v7inqe0pehjq78` FOREIGN KEY (`equ_detail_id`) REFERENCES `equ_detail` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equ_detail_sta
-- ----------------------------

-- ----------------------------
-- Table structure for equ_setting
-- ----------------------------
DROP TABLE IF EXISTS `equ_setting`;
CREATE TABLE `equ_setting` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '管理员帐号ID',
  `cabinet_type` varchar(36) DEFAULT NULL COMMENT '类型',
  `col_num` int(11) DEFAULT NULL COMMENT '列数',
  `door` varchar(10) DEFAULT NULL COMMENT '门位置',
  `equ_setting_name` varchar(36) DEFAULT NULL COMMENT '名称',
  `equ_setting_parent_id` varchar(36) DEFAULT NULL COMMENT '上级刀具柜ID',
  `row_num` int(11) DEFAULT NULL COMMENT '层数',
  `serial_no` varchar(36) DEFAULT NULL COMMENT '序列号',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `ware_house_alias` varchar(20) DEFAULT NULL COMMENT '刀具柜别名',
  PRIMARY KEY (`id`),
  KEY `FKr4kfvxe2estix3qe4bmw3t2rc` (`equ_setting_parent_id`),
  CONSTRAINT `FKr4kfvxe2estix3qe4bmw3t2rc` FOREIGN KEY (`equ_setting_parent_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equ_setting
-- ----------------------------

-- ----------------------------
-- Table structure for external_setting
-- ----------------------------
DROP TABLE IF EXISTS `external_setting`;
CREATE TABLE `external_setting` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `back_method` varchar(36) DEFAULT NULL COMMENT '归还方法名',
  `borrow_method` varchar(36) DEFAULT NULL COMMENT '借出方法名',
  `feed_method` varchar(36) DEFAULT NULL COMMENT '补料方法名',
  `method1` varchar(36) DEFAULT NULL COMMENT '其它方法名1',
  `method2` varchar(36) DEFAULT NULL COMMENT '其它方法名2',
  `token_method` varchar(36) DEFAULT NULL COMMENT '获取Token',
  `url` varchar(36) DEFAULT NULL COMMENT '外部访问地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of external_setting
-- ----------------------------

-- ----------------------------
-- Table structure for extra_box_num_setting
-- ----------------------------
DROP TABLE IF EXISTS `extra_box_num_setting`;
CREATE TABLE `extra_box_num_setting` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(255) DEFAULT NULL COMMENT '帐号ID',
  `extra_box_num` varchar(100) DEFAULT NULL COMMENT '暂存柜数量',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of extra_box_num_setting
-- ----------------------------

-- ----------------------------
-- Table structure for feeding_detail
-- ----------------------------
DROP TABLE IF EXISTS `feeding_detail`;
CREATE TABLE `feeding_detail` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `equ_detail_sta_id` varchar(36) DEFAULT NULL COMMENT '刀具柜货位ID',
  `feeding_list_id` varchar(36) DEFAULT NULL COMMENT '补料清单ID',
  `feeding_num` int(11) DEFAULT NULL COMMENT '补充数量',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  PRIMARY KEY (`id`),
  KEY `FK7489qpq24cethdyog3xpn0cys` (`equ_detail_sta_id`),
  KEY `FK66xl8ahpwctl9krn10sct2eb9` (`feeding_list_id`),
  KEY `FKjuhkcq1g2c519ba8u3a82coel` (`mat_info_id`),
  CONSTRAINT `FKjuhkcq1g2c519ba8u3a82coel` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FK66xl8ahpwctl9krn10sct2eb9` FOREIGN KEY (`feeding_list_id`) REFERENCES `feeding_list` (`id`),
  CONSTRAINT `FK7489qpq24cethdyog3xpn0cys` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of feeding_detail
-- ----------------------------

-- ----------------------------
-- Table structure for feeding_list
-- ----------------------------
DROP TABLE IF EXISTS `feeding_list`;
CREATE TABLE `feeding_list` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `cabinet_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `create_account_id` varchar(36) DEFAULT NULL COMMENT '补料创建人',
  `create_time` datetime DEFAULT NULL COMMENT '补料创建时间',
  `feed_type` varchar(10) DEFAULT NULL COMMENT '补料类型',
  `feeding_account_id` varchar(36) DEFAULT NULL COMMENT '补料人',
  `feeding_name` varchar(50) DEFAULT NULL COMMENT '补料清单',
  `feeding_time` datetime DEFAULT NULL COMMENT '补料时间',
  `is_finish` varchar(10) DEFAULT NULL COMMENT '是否完成',
  PRIMARY KEY (`id`),
  KEY `FK1xq56i6qr15lfmy7qr68js0h8` (`cabinet_id`),
  CONSTRAINT `FK1xq56i6qr15lfmy7qr68js0h8` FOREIGN KEY (`cabinet_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of feeding_list
-- ----------------------------

-- ----------------------------
-- Table structure for hardware_log
-- ----------------------------
DROP TABLE IF EXISTS `hardware_log`;
CREATE TABLE `hardware_log` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '操作人帐号ID',
  `info_type` varchar(10) DEFAULT NULL COMMENT '硬件信息类型',
  `is_succ` varchar(5) DEFAULT NULL COMMENT '执行结果',
  `op_date` datetime DEFAULT NULL COMMENT '操作时间',
  `position` varchar(50) DEFAULT NULL COMMENT '位置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hardware_log
-- ----------------------------

-- ----------------------------
-- Table structure for inventory
-- ----------------------------
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE `inventory` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `equ_detail_sta_id` varchar(36) DEFAULT NULL COMMENT '货位ID',
  `inventory_num` int(11) DEFAULT NULL COMMENT '实盘数量',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `op_date` datetime DEFAULT NULL COMMENT '操作时间',
  `owner_id` varchar(36) DEFAULT NULL COMMENT '使用人员',
  `storage_num` int(11) DEFAULT NULL COMMENT '库存数量',
  `sub_box_id` varchar(36) DEFAULT NULL COMMENT '暂存柜ID',
  PRIMARY KEY (`id`),
  KEY `FK1p9pojq9rrtchhjdic6ofdmw0` (`equ_detail_sta_id`),
  KEY `FKjupl1npotc7qjvjmvs834lkpw` (`mat_info_id`),
  KEY `FKsvwl1l9wmtlc40ju30pa10ia2` (`sub_box_id`),
  CONSTRAINT `FKsvwl1l9wmtlc40ju30pa10ia2` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`),
  CONSTRAINT `FK1p9pojq9rrtchhjdic6ofdmw0` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`),
  CONSTRAINT `FKjupl1npotc7qjvjmvs834lkpw` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of inventory
-- ----------------------------

-- ----------------------------
-- Table structure for lock_param
-- ----------------------------
DROP TABLE IF EXISTS `lock_param`;
CREATE TABLE `lock_param` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `board_no` int(11) DEFAULT NULL COMMENT '栈号',
  `cabinet_type` varchar(36) DEFAULT NULL COMMENT '类型',
  `col_no` int(11) DEFAULT NULL COMMENT '列号',
  `equ_setting_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `row_no` int(11) DEFAULT NULL COMMENT '行号',
  PRIMARY KEY (`id`),
  KEY `FKf2hhmvh8ckmlj15e73t4fbr9g` (`equ_setting_id`),
  CONSTRAINT `FKf2hhmvh8ckmlj15e73t4fbr9g` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lock_param
-- ----------------------------

-- ----------------------------
-- Table structure for lower_frame_query
-- ----------------------------
DROP TABLE IF EXISTS `lower_frame_query`;
CREATE TABLE `lower_frame_query` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `bar_code` varchar(255) DEFAULT NULL COMMENT '物料编号',
  `equ_detail_sta_id` varchar(36) DEFAULT NULL COMMENT '刀具柜货位ID',
  `equ_name` varchar(255) DEFAULT NULL COMMENT '刀具柜名称',
  `low_frame_num` int(11) DEFAULT NULL COMMENT '下架数量',
  `mat_info_name` varchar(255) DEFAULT NULL COMMENT '物料名称',
  `op_date` datetime DEFAULT NULL COMMENT '操作时间',
  `owner_account_id` varchar(255) DEFAULT NULL COMMENT '使用人员帐号ID',
  `owner_name` varchar(255) DEFAULT NULL COMMENT '使用人员',
  `spec` varchar(255) DEFAULT NULL COMMENT '物料型号',
  `sub_box_id` varchar(36) DEFAULT NULL COMMENT '暂存柜货位ID',
  `unit` varchar(255) DEFAULT NULL COMMENT '单位',
  `user_account_id` varchar(255) DEFAULT NULL COMMENT '操作人员帐号ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '操作人员',
  PRIMARY KEY (`id`),
  KEY `FKfyhpdgd0x8uy2g6ld0eq2l5t2` (`equ_detail_sta_id`),
  KEY `FK9f6er4k1jbgftrpxl5xohsv8k` (`sub_box_id`),
  CONSTRAINT `FK9f6er4k1jbgftrpxl5xohsv8k` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`),
  CONSTRAINT `FKfyhpdgd0x8uy2g6ld0eq2l5t2` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lower_frame_query
-- ----------------------------

-- ----------------------------
-- Table structure for manufacturer
-- ----------------------------
DROP TABLE IF EXISTS `manufacturer`;
CREATE TABLE `manufacturer` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `contact` varchar(30) DEFAULT NULL COMMENT '负责人',
  `manufacturer_name` varchar(50) DEFAULT NULL COMMENT '供应商名称',
  `manufacturer_no` varchar(30) DEFAULT NULL COMMENT '供应商编号',
  `phone` varchar(30) DEFAULT NULL COMMENT '联系电话',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manufacturer
-- ----------------------------
INSERT INTO `manufacturer` VALUES ('001', '苏州', '阿诺', '阿诺', null, '13800000000', '', 'ENABLE');

-- ----------------------------
-- Table structure for manufacturer_custom
-- ----------------------------
DROP TABLE IF EXISTS `manufacturer_custom`;
CREATE TABLE `manufacturer_custom` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `contact_name` varchar(20) DEFAULT NULL COMMENT '姓名',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `mail_address` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `manufacturer_id` varchar(36) DEFAULT NULL COMMENT '供应商ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`),
  KEY `FK77auw9hjdsriuo5cboejry1py` (`manufacturer_id`),
  CONSTRAINT `FK77auw9hjdsriuo5cboejry1py` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of manufacturer_custom
-- ----------------------------

-- ----------------------------
-- Table structure for mat_category
-- ----------------------------
DROP TABLE IF EXISTS `mat_category`;
CREATE TABLE `mat_category` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `category_tree_id` varchar(36) DEFAULT NULL COMMENT '物料分类树ID',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`),
  KEY `FK7nos8ebgw16x8ki63o4n85rwb` (`category_tree_id`),
  KEY `FK4s5v6erax6jaok3gwosoaelup` (`mat_info_id`),
  CONSTRAINT `FK4s5v6erax6jaok3gwosoaelup` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FK7nos8ebgw16x8ki63o4n85rwb` FOREIGN KEY (`category_tree_id`) REFERENCES `mat_category_tree` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_category
-- ----------------------------

-- ----------------------------
-- Table structure for mat_category_tree
-- ----------------------------
DROP TABLE IF EXISTS `mat_category_tree`;
CREATE TABLE `mat_category_tree` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `equ_type` varchar(10) DEFAULT NULL COMMENT '物料类型',
  `f_id` varchar(255) DEFAULT NULL COMMENT '节点路径',
  `f_name` varchar(100) DEFAULT NULL COMMENT '父节点名',
  `level` varchar(255) DEFAULT NULL COMMENT '层级',
  `name` varchar(255) DEFAULT NULL COMMENT '节点名',
  `p_id` varchar(255) DEFAULT NULL COMMENT '父节点ID',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_category_tree
-- ----------------------------
INSERT INTO `mat_category_tree` VALUES ('1', '2021-05-11 16:07:22', 'MATTYPE', '1', '类型', '0', '类型', '1', 'ENABLE');
INSERT INTO `mat_category_tree` VALUES ('2', '2021-05-11 16:07:22', 'REQREF', '2', '设备', '0', '设备', '2', 'ENABLE');
INSERT INTO `mat_category_tree` VALUES ('3', '2021-05-11 16:07:22', 'PROCREF', '3', '工序', '0', '工序', '3', 'ENABLE');
INSERT INTO `mat_category_tree` VALUES ('4', '2021-05-11 16:07:22', 'PARTS', '4', '零件', '0', '零件', '4', 'ENABLE');
INSERT INTO `mat_category_tree` VALUES ('5', '2021-05-11 16:07:22', 'CUSTOM', '5', '自定义', '0', '自定义', '5', 'ENABLE');

-- ----------------------------
-- Table structure for mat_equ_info
-- ----------------------------
DROP TABLE IF EXISTS `mat_equ_info`;
CREATE TABLE `mat_equ_info` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `bar_code` varchar(36) DEFAULT NULL COMMENT '物料编号',
  `borrow_type` varchar(10) DEFAULT NULL COMMENT '借出类型',
  `brand` varchar(50) DEFAULT NULL COMMENT '品牌',
  `icon` varchar(50) DEFAULT NULL COMMENT '图片',
  `lower_stock_num` int(11) DEFAULT NULL COMMENT '最低库存',
  `manufacturer_id` varchar(36) DEFAULT NULL COMMENT '供应商ID',
  `mat_equ_name` varchar(50) DEFAULT NULL COMMENT '物料名称',
  `num` int(10) DEFAULT NULL COMMENT '包装数量',
  `old_for_new` varchar(10) DEFAULT NULL COMMENT '以旧换新',
  `pack_unit` varchar(50) DEFAULT NULL COMMENT '包装单位',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `spec` varchar(50) DEFAULT NULL COMMENT '物料型号',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `store_price` float DEFAULT NULL COMMENT '库存成本',
  `use_life` int(11) DEFAULT NULL COMMENT '使用寿命',
  PRIMARY KEY (`id`),
  KEY `FKdrnv5fyunokp94tuon2ewhhig` (`manufacturer_id`),
  CONSTRAINT `FKdrnv5fyunokp94tuon2ewhhig` FOREIGN KEY (`manufacturer_id`) REFERENCES `manufacturer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_equ_info
-- ----------------------------

-- ----------------------------
-- Table structure for mat_return_back
-- ----------------------------
DROP TABLE IF EXISTS `mat_return_back`;
CREATE TABLE `mat_return_back` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(50) DEFAULT NULL COMMENT '归还人帐号ID',
  `audit_status` varchar(10) DEFAULT NULL COMMENT '审核状态',
  `back_way` varchar(10) DEFAULT NULL COMMENT '归还方式',
  `bar_code` varchar(255) DEFAULT NULL COMMENT '归还码',
  `confirm_date` datetime DEFAULT NULL COMMENT '审核时间',
  `confirm_mode` varchar(10) DEFAULT NULL COMMENT '审核方式',
  `confirm_user` varchar(255) DEFAULT NULL COMMENT '审核人员间',
  `equ_setting_name` varchar(36) DEFAULT NULL COMMENT '刀具柜名称',
  `is_get_new_one` varchar(255) DEFAULT NULL COMMENT '以旧换新',
  `is_return_back` varchar(10) DEFAULT NULL COMMENT '是否归还',
  `mat_use_bill_id` varchar(36) DEFAULT NULL COMMENT '领用ID',
  `num` int(11) DEFAULT NULL COMMENT '实际数量',
  `op_date` datetime DEFAULT NULL COMMENT '归还时间',
  `real_life` int(11) DEFAULT NULL COMMENT '制造产量',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `return_back_no` int(7) DEFAULT NULL COMMENT '归还流水号',
  `return_back_type` varchar(255) DEFAULT NULL COMMENT '归还类型',
  `return_detail_info` varchar(255) DEFAULT NULL COMMENT '归还详情D',
  `serial_num` varchar(50) DEFAULT NULL COMMENT '产品流水号',
  PRIMARY KEY (`id`),
  KEY `FKi2xlwgrl6iax3hcptkiqjc5r9` (`mat_use_bill_id`),
  CONSTRAINT `FKi2xlwgrl6iax3hcptkiqjc5r9` FOREIGN KEY (`mat_use_bill_id`) REFERENCES `mat_use_bill` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_return_back
-- ----------------------------

-- ----------------------------
-- Table structure for mat_return_detail
-- ----------------------------
DROP TABLE IF EXISTS `mat_return_detail`;
CREATE TABLE `mat_return_detail` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `mat_return_back_id` varchar(36) DEFAULT NULL COMMENT '归还ID',
  `num` int(36) DEFAULT NULL COMMENT '归还数量',
  `op_date` datetime DEFAULT NULL COMMENT '归还时间',
  `restitution_type_id` varchar(36) DEFAULT NULL COMMENT '归还ID',
  PRIMARY KEY (`id`),
  KEY `FK5ohbpdcg14ja8px2nspnod8ie` (`mat_return_back_id`),
  KEY `FKhn85bffav58d3fty70x9pc35w` (`restitution_type_id`),
  CONSTRAINT `FKhn85bffav58d3fty70x9pc35w` FOREIGN KEY (`restitution_type_id`) REFERENCES `restitution_type` (`id`),
  CONSTRAINT `FK5ohbpdcg14ja8px2nspnod8ie` FOREIGN KEY (`mat_return_back_id`) REFERENCES `mat_return_back` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_return_detail
-- ----------------------------

-- ----------------------------
-- Table structure for mat_use_bill
-- ----------------------------
DROP TABLE IF EXISTS `mat_use_bill`;
CREATE TABLE `mat_use_bill` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `bar_code` varchar(30) DEFAULT NULL COMMENT '物料编号',
  `borrow_num` int(11) DEFAULT NULL COMMENT '领用数量',
  `brand` varchar(50) DEFAULT NULL COMMENT '品牌',
  `equ_detail_sta_id` varchar(36) DEFAULT NULL COMMENT '托盘ID',
  `manufacturer` varchar(50) DEFAULT NULL COMMENT '供应商',
  `mat_equ_name` varchar(50) DEFAULT NULL COMMENT '物料名称',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `mat_use_record_id` varchar(36) DEFAULT NULL COMMENT '领用记录ID',
  `op_date` datetime DEFAULT NULL COMMENT '领用时间',
  `spec` varchar(50) DEFAULT NULL COMMENT '物料型号',
  `store_price` float DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`id`),
  KEY `FK7h7pw3110exn2ntpvuoodvql8` (`equ_detail_sta_id`),
  KEY `FKct9tw6m13cs21cgj4bnff3b1v` (`mat_use_record_id`),
  CONSTRAINT `FKct9tw6m13cs21cgj4bnff3b1v` FOREIGN KEY (`mat_use_record_id`) REFERENCES `mat_use_record` (`id`),
  CONSTRAINT `FK7h7pw3110exn2ntpvuoodvql8` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_use_bill
-- ----------------------------

-- ----------------------------
-- Table structure for mat_use_record
-- ----------------------------
DROP TABLE IF EXISTS `mat_use_record`;
CREATE TABLE `mat_use_record` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '领用人员帐号ID',
  `bar_code` varchar(255) DEFAULT NULL COMMENT '物料编号',
  `borrow_num` int(11) DEFAULT NULL COMMENT '领取数量',
  `borrow_origin` varchar(255) DEFAULT NULL COMMENT '领取来源',
  `borrow_type` varchar(255) DEFAULT NULL COMMENT '领取单位',
  `brand` varchar(255) DEFAULT NULL COMMENT '品牌（供应商）',
  `dept_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `dept_name` varchar(255) DEFAULT NULL COMMENT '部门名称',
  `is_return_back` varchar(255) DEFAULT NULL COMMENT '是否归还',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `mat_info_name` varchar(255) DEFAULT NULL COMMENT '物料名称',
  `money` float DEFAULT NULL COMMENT '金额',
  `op_date` datetime DEFAULT NULL COMMENT '领用时间',
  `pack_num` int(11) DEFAULT NULL COMMENT '包装数量',
  `pack_unit` varchar(255) DEFAULT NULL COMMENT '包装单位',
  `price` float DEFAULT NULL COMMENT '单价',
  `real_life` int(11) DEFAULT NULL COMMENT '制造产量',
  `real_num` int(11) DEFAULT NULL COMMENT '实领数量',
  `receive_info` varchar(36) DEFAULT NULL COMMENT '领取分类ID',
  `receive_type` varchar(255) DEFAULT NULL COMMENT '类型',
  `return_back_num` int(11) DEFAULT NULL COMMENT '已归还数量',
  `spec` varchar(255) DEFAULT NULL COMMENT '物料型号',
  `use_life` int(11) DEFAULT NULL COMMENT '使用寿命',
  `user_name` varchar(255) DEFAULT NULL COMMENT '领用人员',
  PRIMARY KEY (`id`),
  KEY `FK8lhxypjsni8dmlp23223on4l5` (`mat_info_id`),
  KEY `FK5t0ke07hly9mas6u5vuqj7clj` (`receive_info`),
  CONSTRAINT `FK5t0ke07hly9mas6u5vuqj7clj` FOREIGN KEY (`receive_info`) REFERENCES `mat_category_tree` (`id`),
  CONSTRAINT `FK8lhxypjsni8dmlp23223on4l5` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mat_use_record
-- ----------------------------

-- ----------------------------
-- Table structure for notice_mgr
-- ----------------------------
DROP TABLE IF EXISTS `notice_mgr`;
CREATE TABLE `notice_mgr` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(255) DEFAULT NULL COMMENT '帐号ID',
  `count` int(11) DEFAULT NULL COMMENT '计数',
  `equ_setting_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `equ_setting_name` varchar(50) DEFAULT NULL COMMENT '刀具柜名称',
  `notice_type` varchar(36) DEFAULT NULL COMMENT '类型',
  `num` int(11) DEFAULT NULL COMMENT '数量',
  `status` varchar(36) DEFAULT NULL COMMENT '启用状态',
  `user_name` varchar(255) DEFAULT NULL COMMENT '收件人',
  `warn_value` int(11) DEFAULT NULL COMMENT '预警值',
  PRIMARY KEY (`id`),
  KEY `FKilj97cq6lts5o63lrdno8cq08` (`equ_setting_id`),
  CONSTRAINT `FKilj97cq6lts5o63lrdno8cq08` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of notice_mgr
-- ----------------------------

-- ----------------------------
-- Table structure for phone_cart
-- ----------------------------
DROP TABLE IF EXISTS `phone_cart`;
CREATE TABLE `phone_cart` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `mat_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `num` int(11) DEFAULT NULL COMMENT '选择数量',
  `op_date` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `FKc5tu3a93hn4b9o3rs1dot3ram` (`mat_id`),
  CONSTRAINT `FKc5tu3a93hn4b9o3rs1dot3ram` FOREIGN KEY (`mat_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_cart
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order
-- ----------------------------
DROP TABLE IF EXISTS `phone_order`;
CREATE TABLE `phone_order` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `order_code` longtext COMMENT '预约二维码',
  `order_created_time` datetime DEFAULT NULL COMMENT '预约时间',
  `order_text` longtext COMMENT '订单二维码文本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order_mat
-- ----------------------------
DROP TABLE IF EXISTS `phone_order_mat`;
CREATE TABLE `phone_order_mat` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `num` int(11) DEFAULT NULL COMMENT '预约数量',
  `order_mat_code` longtext COMMENT '预约二维码',
  `order_mat_text` longtext COMMENT '订单二维码文本',
  `phone_order_id` varchar(36) DEFAULT NULL COMMENT '订单ID',
  PRIMARY KEY (`id`),
  KEY `FK48vk6sjwc85k85y42gnwminyq` (`mat_info_id`),
  KEY `FK3tpbo4m7relunum3llgaeokls` (`phone_order_id`),
  CONSTRAINT `FK3tpbo4m7relunum3llgaeokls` FOREIGN KEY (`phone_order_id`) REFERENCES `phone_order` (`id`),
  CONSTRAINT `FK48vk6sjwc85k85y42gnwminyq` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order_mat
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order_mat_detail
-- ----------------------------
DROP TABLE IF EXISTS `phone_order_mat_detail`;
CREATE TABLE `phone_order_mat_detail` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `equ_setting_id` varchar(36) DEFAULT NULL COMMENT '刀具柜ID',
  `col_no` int(11) DEFAULT NULL COMMENT '列数',
  `equ_detail_sta_id` varchar(36) DEFAULT NULL COMMENT '刀具柜货位ID',
  `num` int(11) DEFAULT NULL COMMENT '数量',
  `phone_order_mat_id` varchar(36) DEFAULT NULL COMMENT '订单物料ID',
  `row_no` int(11) DEFAULT NULL COMMENT '层数',
  PRIMARY KEY (`id`),
  KEY `FKk46yod7wsuuntw8ychxbahdhk` (`equ_detail_sta_id`),
  KEY `FKq9supgrqfgeq0eod2qqmxcwj7` (`equ_setting_id`),
  KEY `FKbj2grsha7nh8tjq13cl439a3f` (`phone_order_mat_id`),
  CONSTRAINT `FKbj2grsha7nh8tjq13cl439a3f` FOREIGN KEY (`phone_order_mat_id`) REFERENCES `phone_order_mat` (`id`),
  CONSTRAINT `FKk46yod7wsuuntw8ychxbahdhk` FOREIGN KEY (`equ_detail_sta_id`) REFERENCES `equ_detail_sta` (`id`),
  CONSTRAINT `FKq9supgrqfgeq0eod2qqmxcwj7` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order_mat_detail
-- ----------------------------

-- ----------------------------
-- Table structure for phone_order_sta
-- ----------------------------
DROP TABLE IF EXISTS `phone_order_sta`;
CREATE TABLE `phone_order_sta` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `op_date` datetime DEFAULT NULL COMMENT '取料时间',
  `phone_order_id` varchar(36) DEFAULT NULL COMMENT '订单ID',
  PRIMARY KEY (`id`),
  KEY `FKso5uj1enxwh2jjqjqk94ya4w4` (`phone_order_id`),
  CONSTRAINT `FKso5uj1enxwh2jjqjqk94ya4w4` FOREIGN KEY (`phone_order_id`) REFERENCES `phone_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_order_sta
-- ----------------------------

-- ----------------------------
-- Table structure for plc_setting
-- ----------------------------
DROP TABLE IF EXISTS `plc_setting`;
CREATE TABLE `plc_setting` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `address` varchar(200) DEFAULT NULL COMMENT 'PLC地址',
  `default_value` varchar(100) DEFAULT NULL COMMENT '默认值',
  `is_default` varchar(10) DEFAULT NULL COMMENT '默认项',
  `plc_name` varchar(200) DEFAULT NULL COMMENT 'PLC名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of plc_setting
-- ----------------------------
INSERT INTO `plc_setting` VALUES ('1001', '4096', '10', 'YES', 'F1', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1002', '6099', '20', 'YES', 'F2', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1003', '6101', '30', 'YES', 'F3', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1004', '6103', '40', 'YES', 'F4', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1005', '6105', '50', 'YES', 'F5', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1006', '6107', '60', 'YES', 'F6', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1007', '6109', '70', 'YES', 'F7', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1008', '6111', '80', 'YES', 'F8', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1009', '6113', '90', 'YES', 'F9', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1010', '6115', '100', 'YES', 'F10', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1011', '6117', '35', 'YES', '取料口位置', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1012', '6119', '30', 'YES', '运行速度', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1013', '6121', '35', 'NO', '实时位置', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1014', '6123', '0', 'NO', '偏移量', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1015', '4187', null, 'NO', '目标层', '寄存器', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1016', '2349', null, 'NO', '点动上升', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1017', '2350', null, 'NO', '点动下降', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1018', '2351', null, 'NO', '取料口开门', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1019', '2352', null, 'NO', '取料口关门', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1020', '2353', null, 'NO', '复位料斗', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1021', '2354', null, 'NO', '已到达目标层', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1022', '2355', null, 'NO', '取料口关门检测', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1023', '2356', null, 'NO', '故障标志', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1024', '2357', null, 'NO', '上限位报警', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1025', '2358', null, 'NO', '下限位报警', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1026', '2359', null, 'NO', '伺服驱动器报警', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1027', '2360', null, 'NO', '开门失败', '线圈', 'ENABLE');
INSERT INTO `plc_setting` VALUES ('1028', '2361', null, 'NO', '关门失败', '线圈', 'ENABLE');

-- ----------------------------
-- Table structure for post_status
-- ----------------------------
DROP TABLE IF EXISTS `post_status`;
CREATE TABLE `post_status` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `borrow_flg` bit(1) DEFAULT NULL COMMENT '是否领用',
  `feed_flg` bit(1) DEFAULT NULL COMMENT '是否补料',
  `message` varchar(100) DEFAULT NULL COMMENT '返回信息',
  `op_date` datetime DEFAULT NULL COMMENT '推送时间',
  `params` longtext COMMENT '访问参数',
  `return_back_flg` bit(1) DEFAULT NULL COMMENT '是否归还',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `url` varchar(100) DEFAULT NULL COMMENT '访问URL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post_status
-- ----------------------------

-- ----------------------------
-- Table structure for restitution_type
-- ----------------------------
DROP TABLE IF EXISTS `restitution_type`;
CREATE TABLE `restitution_type` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `rest_name` varchar(36) DEFAULT NULL COMMENT '归还名称',
  `return_back_type` varchar(10) DEFAULT NULL COMMENT '归还类型',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of restitution_type
-- ----------------------------
INSERT INTO `restitution_type` VALUES ('101', '续用', '续用', 'NORMAL', 'ENABLE');
INSERT INTO `restitution_type` VALUES ('102', '修磨', '修磨', 'NORMAL', 'ENABLE');
INSERT INTO `restitution_type` VALUES ('103', '错领', '错领', 'NORMAL', 'ENABLE');
INSERT INTO `restitution_type` VALUES ('104', '报废', '报废', 'ABNORMAL', 'ENABLE');
INSERT INTO `restitution_type` VALUES ('105', '遗失', '遗失', 'ABNORMAL', 'ENABLE');

-- ----------------------------
-- Table structure for statement
-- ----------------------------
DROP TABLE IF EXISTS `statement`;
CREATE TABLE `statement` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐号ID',
  `balance` int(11) DEFAULT NULL COMMENT '上期结余',
  `cost` float DEFAULT NULL COMMENT '金额',
  `end_date` datetime DEFAULT NULL COMMENT '截止日期',
  `inventory_num` int(11) DEFAULT NULL COMMENT '核对数量',
  `ishd` varchar(10) DEFAULT NULL COMMENT '是否核对',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `op_date` datetime DEFAULT NULL COMMENT '核对日期',
  `outer_num` int(11) DEFAULT NULL COMMENT '领料数量',
  `price` float DEFAULT NULL COMMENT '单价',
  `start_date` datetime DEFAULT NULL COMMENT '起始日期',
  `temp_num` int(11) DEFAULT NULL COMMENT '暂存柜结余',
  PRIMARY KEY (`id`),
  KEY `FKfbd4rymtj8ubwfkx0idrt66pu` (`mat_info_id`),
  CONSTRAINT `FKfbd4rymtj8ubwfkx0idrt66pu` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of statement
-- ----------------------------

-- ----------------------------
-- Table structure for statement_detail
-- ----------------------------
DROP TABLE IF EXISTS `statement_detail`;
CREATE TABLE `statement_detail` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `mat_use_record_id` varchar(36) DEFAULT NULL COMMENT '领料记录ID',
  `statement_id` varchar(36) DEFAULT NULL COMMENT '核对主业务ID',
  PRIMARY KEY (`id`),
  KEY `FKecoj0l3m90y65sb3rsy1i8473` (`mat_use_record_id`),
  KEY `FKg1ulti2rcek10fdcc9ukwr2pn` (`statement_id`),
  CONSTRAINT `FKg1ulti2rcek10fdcc9ukwr2pn` FOREIGN KEY (`statement_id`) REFERENCES `statement` (`id`),
  CONSTRAINT `FKecoj0l3m90y65sb3rsy1i8473` FOREIGN KEY (`mat_use_record_id`) REFERENCES `mat_use_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of statement_detail
-- ----------------------------

-- ----------------------------
-- Table structure for sub_box
-- ----------------------------
DROP TABLE IF EXISTS `sub_box`;
CREATE TABLE `sub_box` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `board_no` int(11) DEFAULT NULL COMMENT '栈号',
  `box_index` int(11) DEFAULT NULL COMMENT '索引号',
  `cabinet_sta` varchar(10) DEFAULT NULL COMMENT '状态',
  `col_no` int(11) DEFAULT NULL COMMENT '列号',
  `equ_setting_id` varchar(36) DEFAULT NULL COMMENT '暂存柜ID',
  `extra_num` int(11) DEFAULT NULL COMMENT '可存库位',
  `lock_index` int(11) DEFAULT NULL COMMENT '针脚号',
  `row_no` int(11) DEFAULT NULL COMMENT '行号',
  PRIMARY KEY (`id`),
  KEY `FKthluq84rk1v4a36b0e2oduhbs` (`equ_setting_id`),
  CONSTRAINT `FKthluq84rk1v4a36b0e2oduhbs` FOREIGN KEY (`equ_setting_id`) REFERENCES `equ_setting` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_box
-- ----------------------------

-- ----------------------------
-- Table structure for sub_box_account_ref
-- ----------------------------
DROP TABLE IF EXISTS `sub_box_account_ref`;
CREATE TABLE `sub_box_account_ref` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '帐户ID',
  `sub_box_id` varchar(36) DEFAULT NULL COMMENT '暂存柜货位ID',
  PRIMARY KEY (`id`),
  KEY `FK99c4motn1ccsx5rxkcak0kx5a` (`sub_box_id`),
  CONSTRAINT `FK99c4motn1ccsx5rxkcak0kx5a` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_box_account_ref
-- ----------------------------

-- ----------------------------
-- Table structure for sub_cabinet_bill
-- ----------------------------
DROP TABLE IF EXISTS `sub_cabinet_bill`;
CREATE TABLE `sub_cabinet_bill` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(255) DEFAULT NULL COMMENT '领用人员帐号ID',
  `bar_code` varchar(255) DEFAULT NULL COMMENT '物料编号',
  `borrow_type` varchar(255) DEFAULT NULL COMMENT '单位',
  `in_or_out` varchar(10) DEFAULT NULL COMMENT '取出/暂存',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `mat_info_name` varchar(255) DEFAULT NULL COMMENT '物料名称',
  `money` float DEFAULT NULL COMMENT '金额',
  `num` int(11) DEFAULT NULL COMMENT '数量',
  `op_date` datetime DEFAULT NULL COMMENT '操作时间',
  `price` float DEFAULT NULL COMMENT '单价',
  `spec` varchar(255) DEFAULT NULL COMMENT '物料型号',
  `sub_box_id` varchar(36) DEFAULT NULL COMMENT '暂存柜货位ID',
  `sub_box_name` varchar(255) DEFAULT NULL COMMENT '暂存柜名称',
  `user_name` varchar(255) DEFAULT NULL COMMENT '领用人员',
  PRIMARY KEY (`id`),
  KEY `FKnw7myb4ggdhpyooj1hokytbjo` (`mat_info_id`),
  KEY `FKgkhxwp3erkf2d3m910a4eok3i` (`sub_box_id`),
  CONSTRAINT `FKgkhxwp3erkf2d3m910a4eok3i` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`),
  CONSTRAINT `FKnw7myb4ggdhpyooj1hokytbjo` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_cabinet_bill
-- ----------------------------

-- ----------------------------
-- Table structure for sub_cabinet_detail
-- ----------------------------
DROP TABLE IF EXISTS `sub_cabinet_detail`;
CREATE TABLE `sub_cabinet_detail` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `num` int(11) DEFAULT NULL COMMENT '数量',
  `sub_box_id` varchar(36) DEFAULT NULL COMMENT '暂存柜货位ID',
  PRIMARY KEY (`id`),
  KEY `FK6tkgmbaa1qbido486097p6y23` (`mat_info_id`),
  KEY `FK81btjqt5vegupyi91e2ae95qp` (`sub_box_id`),
  CONSTRAINT `FK81btjqt5vegupyi91e2ae95qp` FOREIGN KEY (`sub_box_id`) REFERENCES `sub_box` (`id`),
  CONSTRAINT `FK6tkgmbaa1qbido486097p6y23` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sub_cabinet_detail
-- ----------------------------

-- ----------------------------
-- Table structure for time_task
-- ----------------------------
DROP TABLE IF EXISTS `time_task`;
CREATE TABLE `time_task` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `name` varchar(36) DEFAULT NULL COMMENT '任务名称',
  `status` varchar(36) DEFAULT NULL COMMENT '任务状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of time_task
-- ----------------------------
INSERT INTO `time_task` VALUES ('1', '0', 'OFF');
INSERT INTO `time_task` VALUES ('2', '1', 'OFF');
INSERT INTO `time_task` VALUES ('3', '2', 'OFF');
INSERT INTO `time_task` VALUES ('4', '3', 'OFF');
INSERT INTO `time_task` VALUES ('5', '4', 'OFF');
INSERT INTO `time_task` VALUES ('6', '5', 'OFF');
INSERT INTO `time_task` VALUES ('7', '6', 'OFF');
INSERT INTO `time_task` VALUES ('8', '7', 'OFF');

-- ----------------------------
-- Table structure for time_task_detail
-- ----------------------------
DROP TABLE IF EXISTS `time_task_detail`;
CREATE TABLE `time_task_detail` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `account_id` varchar(255) DEFAULT NULL COMMENT '帐号ID',
  `cron_expression` varchar(36) DEFAULT NULL COMMENT '执行表达式',
  `execution_time` varchar(36) DEFAULT NULL COMMENT '执行时间',
  `job_group` varchar(36) DEFAULT NULL COMMENT '任务分组',
  `job_id` varchar(36) DEFAULT NULL COMMENT '任务ID',
  `user_name` varchar(255) DEFAULT NULL COMMENT '收件人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of time_task_detail
-- ----------------------------
INSERT INTO `time_task_detail` VALUES ('1', '1,', '0 0 8 * * ?', '8', 'REPORT', '1', 'admin,');

-- ----------------------------
-- Table structure for unit
-- ----------------------------
DROP TABLE IF EXISTS `unit`;
CREATE TABLE `unit` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `status` varchar(10) DEFAULT NULL COMMENT '状态',
  `unit_name` varchar(36) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of unit
-- ----------------------------
INSERT INTO `unit` VALUES ('101', 'ENABLE', '支');
INSERT INTO `unit` VALUES ('102', 'ENABLE', '盒');
INSERT INTO `unit` VALUES ('103', 'ENABLE', '片');
INSERT INTO `unit` VALUES ('104', 'ENABLE', '把');
INSERT INTO `unit` VALUES ('105', 'ENABLE', '件');
INSERT INTO `unit` VALUES ('106', 'ENABLE', '支/盒');
INSERT INTO `unit` VALUES ('107', 'ENABLE', '片/盒');
INSERT INTO `unit` VALUES ('999', 'ENABLE', '台');

-- ----------------------------
-- Table structure for warehouse_feed
-- ----------------------------
DROP TABLE IF EXISTS `warehouse_feed`;
CREATE TABLE `warehouse_feed` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `feed_no` varchar(255) DEFAULT NULL COMMENT '入库单编号',
  `feed_num` int(11) DEFAULT NULL COMMENT '补料数量',
  `feed_time` datetime DEFAULT NULL COMMENT '上架时间',
  `feeding_list_id` varchar(36) DEFAULT NULL COMMENT '补料清单',
  `mat_info_id` varchar(36) DEFAULT NULL COMMENT '物料ID',
  `message` varchar(255) DEFAULT NULL COMMENT '返回信息',
  `status` varchar(10) DEFAULT NULL COMMENT '是否确认',
  `stock_no` varchar(255) DEFAULT NULL COMMENT '库房号',
  PRIMARY KEY (`id`),
  KEY `FK3r0m8l8rpqohr9ynw95y9uihm` (`feeding_list_id`),
  KEY `FKpucnchtg54hnw4rnqo6fxakf7` (`mat_info_id`),
  CONSTRAINT `FKpucnchtg54hnw4rnqo6fxakf7` FOREIGN KEY (`mat_info_id`) REFERENCES `mat_equ_info` (`id`),
  CONSTRAINT `FK3r0m8l8rpqohr9ynw95y9uihm` FOREIGN KEY (`feeding_list_id`) REFERENCES `feeding_list` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of warehouse_feed
-- ----------------------------

-- ----------------------------
-- View structure for view_dept
-- ----------------------------
DROP VIEW IF EXISTS `view_dept`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_dept` AS select `d`.`id` AS `DEPT_ID`,`d`.`p_id` AS `DEPT_P_ID`,`d`.`dept_no` AS `DEPT_NO`,`d`.`dept_name` AS `DEPT_NAME`,`d`.`status` AS `DEPT_STATUS` from `dosth`.`dept` `d` ;

-- ----------------------------
-- View structure for view_user
-- ----------------------------
DROP VIEW IF EXISTS `view_user`;
CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_user` AS select `a`.`id` AS `ACCOUNT_ID`,`u`.`dept_id` AS `DEPT_ID`,`u`.`name` AS `USER_NAME`,`a`.`login_name` AS `LOGIN_NAME`,`u`.`ic_card` AS `IC_CARD`,`u`.`start_time` AS `START_TIME`,`u`.`end_time` AS `END_TIME`,`u`.`limit_sum_num` AS `LIMIT_SUM_NUM`,`u`.`not_return_limit_num` AS `NOT_RETURN_LIMIT_NUM`,`u`.`email` AS `EMAIL` from (`dosth`.`account` `a` left join `dosth`.`users` `u` on((`a`.`id` = `u`.`account_id`))) where (`a`.`status` = 'OK') ;
