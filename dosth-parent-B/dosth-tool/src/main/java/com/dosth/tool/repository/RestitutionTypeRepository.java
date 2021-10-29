package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.toolcabinet.enums.ReturnBackType;

public interface RestitutionTypeRepository extends BaseRepository<RestitutionType, String> {

	/**
	 * 
	 * @description 归还集合
	 * @return
	 */
	@Query("from RestitutionType r where r.returnBackType = :type and r.status = :status")
	public List<RestitutionType> getNormalList(ReturnBackType type, UsingStatus status);
	
	/**
	 * 
	 * @description 归还种别获取
	 * @return
	 */
	@Query("from RestitutionType r where r.status = 'ENABLE'")
	public List<RestitutionType> getReturnBackList();
	
}