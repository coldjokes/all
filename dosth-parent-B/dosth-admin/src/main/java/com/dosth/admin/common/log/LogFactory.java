package com.dosth.admin.common.log;

import java.util.Date;

import com.dosth.admin.constant.state.LogType;
import com.dosth.admin.entity.LoginLog;
import com.dosth.admin.entity.OperationLog;
import com.dosth.common.constant.Succeed;

/**
 * 日志对象创建工厂
 * 
 * @author guozhidong
 *
 */
public class LogFactory {
	/**
	 * 创建操作日志
	 * 
	 * @param logType
	 *            日志类型
	 * @param accountId
	 *            账户Id
	 * @param businessName
	 *            业务名称
	 * @param clazzName
	 *            类名
	 * @param methodName
	 *            方法名称
	 * @param msg
	 *            日志消息
	 * @param succeed
	 *            是否成功
	 * @return
	 */
	public static OperationLog createOperationLog(LogType logType, String accountId, String businessName, String clazzName,
			String methodName, String msg, Succeed succeed) {
		OperationLog log = new OperationLog();
		log.setLogType(logType);
		log.setLogName(businessName);
		log.setAccountId(accountId);
		log.setClassName(clazzName);
		log.setMethod(methodName);
		log.setSucceed(succeed);
		log.setMessage(msg);
		log.setCreateTime(new Date());
		return log;
	}

	/**
	 * 创建登录日志
	 * 
	 * @param logType
	 *            日志类型
	 * @param accountId
	 *            账户Id
	 * @param msg
	 *            日志信息
	 * @param ip
	 *            客户端Ip
	 * @return
	 */
	public static LoginLog createLoginLog(LogType logType, String accountId, String msg, String ip) {
		LoginLog log = new LoginLog();
		if(accountId != null && !"".equals(accountId)) {
			log.setSucceed(Succeed.SUCCESS);
		}else {
			log.setSucceed(Succeed.FAIL);
		}
		log.setLogName(logType.getMessage());
		log.setAccountId(accountId);
		log.setIp(ip);
		log.setMessage(msg);
		log.setCreateTime(new Date());
		
		return log;
	}
}