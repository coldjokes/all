package com.dosth.tool.external.service;

import java.util.List;

import com.dosth.tool.external.entity.ExternalMatInfo;
import com.dosth.tool.external.entity.ExternalSupplierInfo;

/**
 *  
 * 
 * @author Yifeng Wang  
 */
public interface ExternalService {

	/**
	 * 物料信息
	 * @return
	 */
	public List<ExternalMatInfo> getMatInfo();

	/**
	 * 补料
	 * @param accountId 补料人id
	 * @param equId 设备id
	 * @param arrs 补料详情 数量-单元格id,多个用英文逗号分割   1-82,2-83
	 * @return
	 */
	public String feeding(String accountId, String equId, String arrs);
	
	/**
	 * 供应商信息
	 * @return
	 */
	public List<ExternalSupplierInfo> getSupplierInfo();
	
	/**
	 * 领取推送
	 * @return
	 */
	public void borrowPost(String recordId);

}

