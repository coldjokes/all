package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.entity.StatementDetail;

/**
 * 核对明细持久化层
 * 
 * @author guozhidong
 *
 */
public interface StatementDetailRepository extends BaseRepository<StatementDetail, String> {
	/**
	 * 根据核对Id获取核对清单
	 * 
	 * @param statementId 核对Id
	 * @return
	 */
	@Query("from StatementDetail d where d.statementId = :statementId")
	public List<StatementDetail> getStatementDetailByStatementId(String statementId);

	/**
	 * 根据核对Id获取领用清单
	 * 
	 * @param statementId 核对Id
	 * @return
	 */
	@Query("select d.matUseRecord from StatementDetail d where d.statementId = :statementId")
	public List<MatUseRecord> getMatUseRecordViewByStatementId(String statementId);
}