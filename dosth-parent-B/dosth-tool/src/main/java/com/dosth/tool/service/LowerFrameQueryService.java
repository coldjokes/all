package com.dosth.tool.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.dto.CabinetName;
import com.dosth.tool.entity.LowerFrameQuery;
import com.dosth.util.OpTip;

public interface LowerFrameQueryService {

	/**
	 * @description 下架主柜
	 * 
	 * @param frameId   主柜弹簧Id
	 * @param accountId 当前操作账号Id
	 * @throws DoSthException
	 */
	public OpTip lowMainFrame(List<String> frameIds, String accountId, String cabinetId);

	/**
	 * @description 获取分页数据
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param name      账户名称
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @return
	 * @throws BusinessException
	 */
	public Page<LowerFrameQuery> getPage(int pageNo, int pageSize, String beginTime, String endTime, String name)
			throws DoSthException;

	/**
	 * @description 下架暂存柜
	 * @param frameId   暂存柜Id
	 * @param accountId 当前操作人员Id
	 * @throws DoSthException
	 */
	public OpTip lowTempFrame(List<String> frameIds, String accountId, String cabinetId);

	/**
	 * @description 下架导出
	 * @param response  ServletResponse
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String name);

	/**
	 * @description 获取设备名称列表
	 */
	public List<CabinetName> getCabinetNameList();

	/**
	 * 下架记录邮件发送
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param name
	 */
	public void sendLowerRecord(String beginTime, String endTime, String name);
}