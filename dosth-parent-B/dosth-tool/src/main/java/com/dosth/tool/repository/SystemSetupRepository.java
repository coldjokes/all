package com.dosth.tool.repository;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.SystemSetup;

/**
 * @description 系统设置Repository
 * 
 * @author Zhongyan.He
 * 
 * 
 * **/
public interface SystemSetupRepository extends BaseRepository<SystemSetup,String>{
	/**
	 * @description 查询补料
	 * @param key       
	 * @return
	 */
	@Query("select setupValue from SystemSetup s where s.setupKey =:setupKey")
	public String getValueByKey(String setupKey);
}
