package com.dosth.tool.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.FeedingDetail;

/**
 * 
 * @description 补料清单明细
 * @author guozhidong
 *
 */
public interface FeedingDetailRepository extends BaseRepository<FeedingDetail, String> {

	/**
	 * @description 根据补料清单获取补料明细
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@Query("select d from FeedingDetail d where d.feedingListId = :feedingListId")
	public List<FeedingDetail> getFeedingDetailListByFeedingListId(String feedingListId);

	/**
	 * @description 获取补料汇总
	 * @param beginTime
	 * @param endTime
	 * @param matInfo
	 * @return
	 */
	@Query("select d from FeedingDetail d where d.feedingList.feedingTime >= :beginTime and d.feedingList.feedingTime <= :endTime and d.feedingList.isFinish = 'YES'")
	public List<FeedingDetail> getFeedSummary(Date beginTime, Date endTime);

	/**
	 * @description 根据柜子Id获取待补料明细列表
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@Query("from FeedingDetail d where d.feedingList.isFinish = 'NO' and d.feedingList.cabinetId = :cabinetId or d.feedingList.equSetting.equSettingParentId = :cabinetId")
	public List<FeedingDetail> getWaitFeedingDetailList(String cabinetId);

	/**
	 * @description 根据弹簧Id获取没有完成补料单
	 * @param equDetailStaId 弹簧Id
	 * @return
	 */
	@Query("from FeedingDetail d where d.feedingList.isFinish = 'NO' and d.equDetailStaId = :equDetailStaId")
	public List<FeedingDetail> getNoFinishFeedingDetailListByEquDetialStaId(String equDetailStaId);

	/**
	 * @description 获取没有完成补料单
	 * @param barCode
	 * @return
	 */
	@Query("from FeedingDetail d where d.feedingList.isFinish = 'NO' and d.matInfo.barCode = :barCode")
	public List<FeedingDetail> getNoFinishFeedingDetailListByBarCode(String barCode);

	/**
	 * @description 获取没有完成补料单
	 * @return
	 */
	@Query("from FeedingDetail d where d.feedingList.isFinish = 'NO'")
	public List<FeedingDetail> getNoFinishFeedingDetailList();
}