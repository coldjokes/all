package com.dosth.tool.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.dto.FeedRecordSummary;

/**
 * @description 补料记录汇总Service
 * @author chenlei
 *
 */
public interface FeedRecordSummaryService {

	/**
	 * @description 获取分页数据
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @param matInfo   搜索物料信息
	 * @return
	 * @throws DoSthException
	 */
	public Page<FeedRecordSummary> getPage(int pageNo, int pageSize, String beginTime, String endTime, String matInfo)
			throws DoSthException;

	/**
	 * @description 补料汇总导出
	 * @param response  ServletResponse
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @param matInfo   搜索物料信息
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String matInfo);

	/**
	 * 补料汇总邮件发送
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param matInfo
	 */
	public void sendFeedSummary(String beginTime, String endTime, String matInfo);
}