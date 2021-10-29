package com.dosth.admin.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.OperationLog;
import com.dosth.common.db.repository.BaseRepository;

public interface OperationLogRepository extends BaseRepository<OperationLog, Long> {
	
	/**
	 * 通过账户Id获取登录日志
	 * 
	 * @param accountId
	 * @return
	 */
	@Query("select o from OperationLog o where o.accountId = ?1")
	public OperationLog findOperationLogByAccountId(Long accountId);

	/**
	 * @description 清除操作日志
	 */
	@Modifying
	@Transactional
	@Query("delete from OperationLog where accountId <> '1' and accountId <> '101'")
	public void clearByUser();
}