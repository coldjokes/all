package com.dosth.admin.service;

import org.springframework.data.domain.Page;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.SystemInfo;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;

/**
 * 系统信息Service
 * 
 * @author liweifen
 *
 */
public interface SystemInfoService extends BaseService<SystemInfo>{
	/**
	 * 根据账户Id获取用户信息
	 * 
	 * @param Id Id
	 * @return
	 * @throws DoSthException
	 */
	public SystemInfo findSystemInfoById(String id) throws DoSthException;

	/**
	 * 获取分页数据
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param name      姓名
	 * @return
	 * @throws BusinessException
	 */
	public Page<SystemInfo> getPage(int pageNo, int pageSize, String name)
			throws BusinessException;
}
