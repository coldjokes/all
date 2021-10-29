package com.dosth.tool.service;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.PlcSetting;


/**
 * PLC设置Service
 * 
 * @author liweifeng
 *
 */
public interface PlcSettingService extends BaseService<PlcSetting> {
	/**
	 * 分页方法
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param equInfoId
	 *            PLC Id
	 * @param status
	 *            是否启用状态
	 * @return
	 * @throws DoSthException
	 */
	public Page<PlcSetting> getPage(int pageNo, int pageSize, String plcName, String status) throws DoSthException;

}
