package com.dosth.tool.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.SubCabinetBill;

/**
 * 
 * @description 副柜流水Service
 * @author guozhidong
 *
 */
public interface SubCabinetBillService extends BaseService<SubCabinetBill> {

	/**
	 * @description 获取分页数据
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @param info      搜索条件
	 * @return
	 * @throws DoSthException
	 */
	public Page<SubCabinetBill> getPage(int pageNo, int pageSize, String beginTime, String endTime, String info, String subBoxName,String inOrOut)
			throws DoSthException;

	/**
	 * @description  获取指定暂存柜存入时间降序排列
	 * @param subBoxId 暂存柜Id 
	 * @param matInfoId 物料信息Id
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<SubCabinetBill> getLastInTime(String subBoxId, String matInfoId, String accountId);
	
	/**
	 * @description  暂存柜领用记录导出
	 * @param beginTime 起始时间
	 * @param endTime 截止时间
	 * @param info 刀具信息
	 * @return
	 */
	public String infoExport(HttpServletRequest request, HttpServletResponse response, 
			String beginTime, String endTime, String info, String subBoxName,String inOrOut) throws IOException;
}