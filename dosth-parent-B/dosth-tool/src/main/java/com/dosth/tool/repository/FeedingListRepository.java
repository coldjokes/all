package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.FeedingList;

/**
 * 
 * @description 补料清单持久化层
 * @author guozhidong
 *
 */
public interface FeedingListRepository extends BaseRepository<FeedingList, String> {

	/**
	 * @description 查询补料
	 * @param cabinetId        柜子Id
	 * @param feedingAccountId 补料人
	 * @param isFinish         是否完成
	 * @return
	 */
	@Query("from FeedingList l where (l.cabinetId = :cabinetId or ( l.equSetting.equSettingParentId is null and l.equSetting.cabinetType = 'VIRTUAL_WAREHOUSE') or l.equSetting.equSettingParentId = :cabinetId) and (l.feedingAccountId = :feedingAccountId or l.feedingAccountId is null) and l.isFinish = :isFinish")
	public List<FeedingList> getFeedingList(String cabinetId, String feedingAccountId, YesOrNo isFinish);

	/**
	 * 待确认补料清单
	 * 
	 * @param equSettingId
	 * @param isFinish
	 * @return
	 */
	@Query("from FeedingList f where f.cabinetId = :cabinetId and f.isFinish = :isFinish")
	public List<FeedingList> getWaitFeedingNum(String cabinetId, YesOrNo isFinish);
}