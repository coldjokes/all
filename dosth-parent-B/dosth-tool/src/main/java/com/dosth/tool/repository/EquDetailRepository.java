package com.dosth.tool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.EquDetail;

public interface EquDetailRepository extends BaseRepository<EquDetail, String> {

	/**
	 * 根据设备设置Id获取设备详情列表
	 * 
	 * @param equSettingId
	 * @return
	 */
	@Query("from EquDetail i where i.equSettingId = :equSettingId")
	public List<EquDetail> getEquDetailListBySettingId(String equSettingId);
	
	@Query("from EquDetail d where d.ip = :ip")
	public List<EquDetail> getDoor(String ip);

	/**
	 * @description 根据柜体Id删除托盘行
	 * @param equSettingId 柜体Id
	 */
	@Modifying
	@Transactional
	@Query("delete from EquDetail d where d.equSettingId = :equSettingId")
	public void deleteEquDetailByCabinetId(String equSettingId);
	
	@Query("from EquDetail i where i.id = :id")
	public List<EquDetail> getEquDetailListById(String id);
}