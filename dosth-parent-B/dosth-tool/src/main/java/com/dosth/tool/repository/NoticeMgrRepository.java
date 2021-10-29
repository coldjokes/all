package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.common.state.NoticeType;
import com.dosth.tool.entity.NoticeMgr;

public interface NoticeMgrRepository extends BaseRepository<NoticeMgr, String> {

	/**
	 * 根据刀具柜id查询通知管理
	 * 
	 * @param equSettingId
	 * @return
	 */
	@Query("from NoticeMgr n where n.equSettingId = :cabinetId")
	public List<NoticeMgr> getNoticeMgrByCabinetId(String cabinetId);
	
	/**
	 * 获取通知管理状态
	 * 
	 * @param equSettingId
	 * @param noticeType
	 * @return
	 */
	@Query("from NoticeMgr n where n.equSettingId = :cabinetId and n.noticeType = :noticeType")
	public List<NoticeMgr> getNoticeMgrStatus(String cabinetId, NoticeType noticeType);

}
