package com.dosth.admin.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.LoginLog;
import com.dosth.common.db.repository.BaseRepository;

public interface LoginLogRepository extends BaseRepository<LoginLog, Long> {

	/**
	 * 通过账户Id获取登录日志
	 * 
	 * @param accountId
	 * @return
	 */
	@Query("select l from LoginLog l where l.accountId = ?1")
	public LoginLog findLoginLogByAccountId(Long accountId);

	/**
	 * @description 清除登录日志
	 */
	@Modifying
	@Transactional
	@Query("delete from LoginLog where accountId <> '1' and accountId <> '101'")
	public void clearByUser();
}