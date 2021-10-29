package com.dosth.tool.repository;

import com.dosth.common.db.repository.BaseRepository;

import org.springframework.data.jpa.repository.Query;
import com.dosth.tool.entity.ExtraBoxNumSetting;

public interface ExtraBoxNumSettingRepository extends BaseRepository<ExtraBoxNumSetting, String> {
	
	@Query("select t.id from ExtraBoxNumSetting t where t.accountId = :accountId")
	public String findUserByAccountId(String accountId);
	
	/**
	 * 
	 * @description 暂存柜数量设置数集合
	 * @return
	 */
	@Query("select e.extraBoxNum from ExtraBoxNumSetting e where e.accountId =:accountId")
	public String getExtraBoxNumByAccountId(String accountId);
}
