package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.WarehouseFeed;

/**
 * 
 * @description 入库单补料
 * @author chenlei
 *
 */
public interface WarehouseFeedRepository extends BaseRepository<WarehouseFeed, String> {

	/**
	 * @description 获取补料单对应的入库单
	 * @param feedListId
	 * @return
	 */
	@Query("from WarehouseFeed w where w.feedingListId = :feedListId and w.status = :status")
	public List<WarehouseFeed> getWarehouseList(String feedListId, YesOrNo status);

	/**
	 * @description 入库单查询
	 * @param feedListId
	 * @return
	 */
	@Query("from WarehouseFeed w where w.feedNo = :feedNo and w.status = :status")
	public List<WarehouseFeed> getFeedInfo(String feedNo, YesOrNo status);

	/**
	 * @description 根据入库单ID查询
	 * @param feedListId
	 * @return
	 */
	@Query("update WarehouseFeed w set w.message = :message, w.status = :status where w.feedNo = :feedNo and w.status = 'NO'")
	public void updateFeedInfo(String message, YesOrNo status);
}