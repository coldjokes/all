package com.dosth.admin.common.log;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.admin.constant.state.LogType;
import com.dosth.admin.entity.LoginLog;
import com.dosth.admin.entity.OperationLog;
import com.dosth.admin.repository.LoginLogRepository;
import com.dosth.admin.repository.OperationLogRepository;
import com.dosth.common.constant.Succeed;
import com.dosth.common.util.SpringContextHolder;
import com.dosth.common.util.ToolUtil;

/**
 * 日志操作任务创建工厂
 * 
 * @author guozhidong
 *
 */
public class LogTaskFactory {
	private static final Logger logger = LoggerFactory.getLogger(LogTaskFactory.class);
	private static LoginLogRepository loginLogRepository = SpringContextHolder.getBean(LoginLogRepository.class);
	private static OperationLogRepository operationLogRepository = SpringContextHolder.getBean(OperationLogRepository.class);

	/**
	 * 根据用户Id创建登录日志
	 * 
	 * @param accountId
	 * @param ip
	 * @return
	 */
	public static TimerTask loginLog(final String accountId, final String ip) {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					LoginLog loginLog = LogFactory.createLoginLog(LogType.LOGIN, accountId, null, ip);
					loginLogRepository.save(loginLog);
				} catch (Exception e) {
					logger.error("创建登录日志异常!", e);
				}
			}
		};
	}

	/**
	 * 根据用户名创建登录日志
	 * 
	 * @param loginName
	 * @param msg
	 * @param ip
	 * @return
	 */
	public static TimerTask loginLog(String loginName, String msg, String ip) {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					LoginLog log = LogFactory.createLoginLog(LogType.LOGIN, null, "账号:" + loginName + "," + msg, ip);
					loginLogRepository.save(log);
				} catch (Exception e) {
					logger.error("创建登录日志异常!", e);
				}
			}
		};
	}

	/**
	 * 创建退出日志
	 * 
	 * @param accountId
	 * @param ip
	 * @return
	 */
	public static TimerTask exitLog(String accountId, String ip) {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					LoginLog log = LogFactory.createLoginLog(LogType.EXIT, accountId, null, ip);
					loginLogRepository.save(log);
				} catch (Exception e) {
					logger.error("创建退出日志异常!", e);
				}
			}
		};
	}

	/**
	 * 创建业务日志
	 * @param accountId
	 * @param businessName
	 * @param clazzName
	 * @param methodName
	 * @param msg
	 * @return
	 */
	public static TimerTask businessLog(String accountId, String businessName, String clazzName, String methodName,
			String msg) {
		return new TimerTask() {
			@Override
			public void run() {
				try {
					OperationLog log = LogFactory.createOperationLog(LogType.BUSSINESS, accountId, businessName, clazzName,
							methodName, msg, Succeed.SUCCESS);
					operationLogRepository.save(log);
				} catch (Exception e) {
					logger.error("创建业务日志异常!", e);
				}
			}
		};
	}

	/**
	 * 创建异常日志
	 * 
	 * @param accountId
	 * @param exception
	 * @return
	 */
	public static TimerTask exceptionLog(final String accountId, final Exception exception) {
		return new TimerTask() {
			@Override
			public void run() {
				String msg = ToolUtil.getExceptionMsg(exception);
				OperationLog operationLog = LogFactory.createOperationLog(LogType.EXCEPTION, accountId, "", null, null,
						msg, Succeed.FAIL);
				try {
					operationLogRepository.save(operationLog);
				} catch (Exception e) {
					logger.error("创建异常日志异常!", e);
				}
			}
		};
	}
}