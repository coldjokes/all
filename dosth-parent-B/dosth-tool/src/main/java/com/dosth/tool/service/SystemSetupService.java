package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.SystemSetup;

/**
 * 系统设置Service
 * 
 * @author Zhongyan.He
 *
 */
public interface SystemSetupService extends BaseService<SystemSetup> {
	
	/**
	 * 获取全部信息
	 * 
	 * @param
	 * */
	public List<SystemSetup> findAllSystemSetup(); 
	
	/**
	 * 根据key获取value
	 * 
	 * @param setupKey
	 * 
	 * return 
	 * */
	public String getValueByKey(String setupKey);
}
