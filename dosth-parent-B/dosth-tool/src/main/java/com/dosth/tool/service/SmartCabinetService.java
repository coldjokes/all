package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;

/**
 * @Desc 智能刀具柜设置Service
 * @author guozhidong
 *
 */
public interface SmartCabinetService {

	/**
	 * @description 获取设备详情信息
	 * @param equDetailId
	 * @return
	 * @throws DoSthException
	 */
	public EquDetail getEquDetail(String equDetailId) throws DoSthException;

	/**
	 * @description 获取设备详情状态列表
	 * @param equDetailId
	 * @return
	 * @throws DoSthException
	 */
	public List<EquDetailSta> getStaList(String equDetailId) throws DoSthException;

}