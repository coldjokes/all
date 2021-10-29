package com.dosth.tool.repository;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.Unit;

public interface UnitRepository extends BaseRepository<Unit, String> {

	/**
	 *根据名称查询单位信息 
	 * @param unitName
	 * @return
	 */
	@Query("from Unit u where u.unitName = :unitName and u.status = 'ENABLE'")
	public Unit findByName(String unitName);
	
}