package com.dosth.tool.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.cnbaosi.dto.tool.ExternalWarehouseFeed;
import com.cnbaosi.dto.tool.FeignWarehouseFeed;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.state.FeedType;
import com.dosth.util.OpTip;

/**
 * @description 入库单补料Service
 * 
 * @author chenlei
 *
 */
public interface WarehouseFeedService {
	/**
	 * @description 设置物料存储设置
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws DoSthException
	 */
	public Page<ExternalWarehouseFeed> getFeedList(int pageNo, int pageSize, String cabinetId) throws DoSthException;
	
	/**
	 * 入库单补料清单
	 * @param arrs
	 * @return
	 * @throws IOException
	 */
	public OpTip warehouseFeedList(String arrs, String equSettingId, String accountId, FeedType feedType) throws IOException;
	
	/**
	 * 入库单补料清单
	 * @param feedListId
	 * @return
	 * @throws IOException
	 */
	public List<FeignWarehouseFeed> getWarehouseFeedList(String feedListId) throws IOException;
	
	/**
	 * 入库单确认结果保存
	 * @param feedNo
	 * @param message
	 * @param result
	 * @return
	 * @throws IOException
	 */
	public void warehouseSave(String feedNo, String message, Boolean result) throws IOException;

}