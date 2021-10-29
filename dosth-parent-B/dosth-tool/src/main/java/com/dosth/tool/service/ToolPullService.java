package com.dosth.tool.service;

import com.dosth.common.exception.DoSthException;
import com.dosth.dto.BorrowNotice;
import com.dosth.dto.TrolDrawerNotice;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.MatUseRecord;

/**
 * @description 工具提取Service
 * 
 * @author guozhidong
 *
 */
public interface ToolPullService {

	/**
	 * @description 工具提取
	 * 
	 * @param record 物料领用记录
	 * @param equDetailSta   设备详情状态
	 * @param borrowNum      借出数量
	 * @param accountId      操作人Id
	 * @param receiveType    接收类型
	 * @param receiveInfo    接收信息
	 * @param isSyncStockNum 是否同步库存量
	 * @throws DoSthException
	 */
	public void toolPullBill(MatUseRecord record, EquDetailSta equDetailSta, Integer borrowNum, String accountId,
			String receiveType, String receiveInfo, Boolean isSyncStockNum) throws DoSthException;

	/**
	 * @description 取料同步
	 * @param notice 同步信息
	 * @throws DoSthException
	 */
	public void notice(BorrowNotice notice) throws DoSthException;

    /**
	 * @description 可控抽屉柜数据通讯
	 * @param notice 可控抽屉柜数据
	 * @return
	 */
	public void noticeTrolDrawerNotice(TrolDrawerNotice notice);
}