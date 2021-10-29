package com.dosth.tool.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.Inventory;
import com.dosth.tool.vo.InventoryInfo;

/**
 * @description 盘点Service
 * @author guozhidong
 *
 */
public interface InventoryService extends BaseService<Inventory> {

	/**
	 * @description 盘点分页查询
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param beginTime 起始时间
	 * @param endTime   截止时间
	 * @return
	 */
	public Page<Inventory> getPage(int pageNo, int pageSize, String beginTime, String endTime, String matInfo,String equName);

	/**
	 * @description 盘点设备树
	 */
	public List<ZTreeNode> treeEqu();

	/**
	 * @description 库存明细
	 * @param equInfoId 设备Id
	 * @return
	 */
	public List<InventoryInfo> getEquStockView(String equInfoId);

	/**
	 * @description 盘点
	 * @param equInfoId     设备Id
	 * @param inventoryVals 盘点信息
	 * @param accountId     盘点人员Id
	 */
	public void inventory(String equInfoId, String inventoryVals, String accountId);

	/**
	 * @description 导出补料详情
	 * @param request
	 * @param response
	 * @param beginTime 起始时间
	 * @param endTime   截止时间
	 */
	public void exportInventory(HttpServletRequest request, HttpServletResponse response, String beginTime,
			String endTime);

	/**
	 * 盘点记录邮件发送
	 * 
	 * @param beginTime
	 * @param endTime
	 */
	public void sendInventoryRecord(String beginTime, String endTime);
}