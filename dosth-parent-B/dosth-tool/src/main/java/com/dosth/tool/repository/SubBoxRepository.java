package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.SubBox;

/**
 * @description 副柜盒子持久层
 * 
 * @author guozhidong
 *
 */
public interface SubBoxRepository extends BaseRepository<SubBox, String> {

	/**
	 * @description 根据副柜Id获取副柜格子集合
	 * 
	 * @param cabinetId 副柜Id
	 * @return
	 */
	@Query("from SubBox b where b.equSettingId = ?1 order by b.rowNo, b.colNo, b.boxIndex")
	public List<SubBox> getSubBoxListByCabinetId(String equSettingId);

	/**
	 * @description 查询未使用的副柜格子列表
	 * @return
	 */
	@Query("from SubBox b where not exists (select 1 from SubBoxAccountRef r where b.id = r.subBoxId)")
	public List<SubBox> getUnUsedSubBoxList();

	/**
	 * @description 获取暂存柜总数量
	 * @return
	 */
	@Query("select count(*) from SubBox")
	public Integer getSubBoxNum();
	
}