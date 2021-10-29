package com.dosth.tool.service;

import java.util.List;

import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.tool.FeignFeedingList;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.entity.FeedingList;
import com.dosth.toolcabinet.dto.ExtractionMethod;

/**
 * 
 * @description 补料清单Service
 * @author guozhidong
 *
 */
public interface FeedingListService extends BaseService<FeedingList> {
	/**
	 * @description 获取待完成的补料清单
	 * @param cabinetId        柜子Id
	 * @param feedingAccountId 补料人Id
	 * @return
	 */
	public List<ExtractionMethod> getWaitFinishFeedingList(String cabinetId, String feedingAccountId);

	/**
	 * 待确认补料清单
	 * 
	 * @return
	 */
	public List<FeedingList> getWaitFeedingNum(String cabinetId);

	/**
	 * @description 补料单完成补料
	 * @param feedingList 补料单
	 * @return
	 */
	public FeedingList finishFeedingList(FeedingList feedingList);

	/**
	 * @description 手动补料
	 * @param feed      补料信息
	 * @param cabinetId 当前柜子Id
	 * @param accountId 账户Id
	 * @return
	 */
	public void feed(String feed, String cabinetId, String accountId, FeedType feedType);

	/**
	 * @description 补料清单
	 * @param supplyList 物料补料单
	 * @return
	 */
	public OpTip feedingList(List<FeignFeedingList> feedingList);

	/**
	 * @description 获取补料清单详情(用于推送到第三方)
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	public FeignFeedingList getFeeding(String feedingListId);
}