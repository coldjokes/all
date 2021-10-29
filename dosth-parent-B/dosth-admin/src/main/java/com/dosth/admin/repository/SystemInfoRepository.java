package com.dosth.admin.repository;

import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.SystemInfo;
import com.dosth.common.db.repository.BaseRepository;

public interface SystemInfoRepository extends BaseRepository<SystemInfo, String> {
	/**
	 * 通过账户Id获取用户信息
	 * 
	 * @param Id
	 * @return
	 */
	@Query("select s from SystemInfo s where s.id = ?1")
	public SystemInfo findSystemInfoById(String id);

}