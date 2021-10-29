package com.dosth.admin.service;

import org.springframework.data.domain.Page;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.LoginLog;
import com.dosth.common.exception.DoSthException;

/**
 * 登录日志Service
 * 
 * @author liweifeng
 *
 */
public interface LoginLogService {

	/**
	 * 根据账户Id获取日志信息
	 * 
	 * @param accountId
	 *            账户Id
	 * @return
	 * @throws DoSthException
	 */
	public LoginLog findLoginLogByAccountId(Long accountId) throws DoSthException;

	/**
	 * 获取分页数据
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param name
	 *            账户名称
	 * @param beginTime
	 *            起始日期
	 * @param endTime
	 *            截止日期
	 * @return
	 * @throws BusinessException
	 */
	public Page<LoginLog> getPage(int pageNo, int pageSize, String name, String logName, String beginTime, String endTime)
			throws BusinessException;
}
