package com.dosth.tool.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.dto.CategorySummary;
import com.dosth.tool.common.dto.UseRecordSummary;

/**
 * @description 物料领用汇总记录Service
 * @author chenlei
 *
 */
public interface UseRecordSummaryService {

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
	public Page<UseRecordSummary> getPage(int pageNo, int pageSize, String beginTime, String endTime, String matInfo)
			throws DoSthException;

	/**
	 * @description 领用汇总导出
	 * @param response  ServletResponse
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @param matInfo   搜索物料信息
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String matInfo);

	/**
	 * @description 领取类型统计分页
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param beginTime 起始日期
	 * @param endTime   截至日期
	 * @param matInfo   编号/名称/规格/供应商/品牌
	 * @return
	 */
	public Page<CategorySummary> getCategoryPage(int pageNo, int pageSize, String beginTime, String endTime);

	/**
	 * @description 导出领取方式统计
	 * @param request
	 * @param response
	 * @param beginTime
	 * @param beginTime 起始日期
	 * @param endTime   截至日期
	 * @param matInfo   编号/名称/规格/供应商/品牌
	 */
	public void exportCategory(HttpServletRequest request, HttpServletResponse response, String beginTime,
			String endTime);

	/**
	 * 领用汇总邮件发送
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param matInfo
	 */
	public void sendUseSummary(String beginTime, String endTime, String matInfo);

	/**
	 * @description 获取部门统计分页
	 * @param pageNo
	 * @param pageSize
	 * @param beginTime
	 * @param endTime
	 * @param matInfo
	 * @return
	 */
	public Page<CategorySummary> getDeptPage(int pageNo, int pageSize, String beginTime, String endTime);

	/**
	 * @description 导出部门统计
	 * @param request
	 * @param response
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public void exportDept(HttpServletRequest request, HttpServletResponse response, String string, String string2);
}