package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.DailyLimit;

public interface DailyLimitRepository extends BaseRepository<DailyLimit, String> {

	/**
	 * @description 根据账户Id查询物料权限
	 * @param accountId
	 * @return
	 */
	@Query("from DailyLimit d where d.accountId = :accountId")
	public List<DailyLimit> findAllByAccountId(String accountId);

	/**
	 * @description 根据帐号id删除每日限额
	 * @param accountId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("delete from DailyLimit d where d.accountId = :accountId")
	public void delDailyLimit(String accountId);
	
	/**
	 * @description 根据帐号id删除每日限额
	 * @param accountId
	 * @return
	 */
	@Modifying
	@Transactional
	@Query("delete from DailyLimit d where d.accountId = :accountId and d.matInfoId = :matInfoId")
	public void delDailyLimitByMatId(String accountId, String matInfoId);
	
	/**
	 * @description 根据帐号id和物料id获取限额信息
	 * @param accountId
	 * @param matInfoId
	 * @return
	 */
	@Query("from DailyLimit d where d.accountId = :accountId and d.matInfoId = :matInfoId")
	public List<DailyLimit> findByMatId(String accountId, String matInfoId);
	
	/**
	 * @description 根据账户Id查询物料
	 * @param accountId
	 * @return
	 */
	@Query("select d.matInfoId from DailyLimit d where d.accountId = :accountId")
	public List<String> findMatIdByAccountId(String accountId);
}