package com.dosth.tool.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.Statement;

/**
 * @description 核对持久化层
 * @author guozhidong
 *
 */
public interface StatementRepository extends BaseRepository<Statement, String> {

	/**
	 * @description 获取指定物料最后一次统计暂存柜结余
	 * @param matInfoId 物料Id
	 * @return
	 */
	@Query("from Statement s where s.matInfoId = :matInfoId order by opDate desc")
	public List<Statement> getLastStatementList(String matInfoId);

	/**
	 * @description 获取区间已核对的记录
	 * @param beginTime 起始时间
	 * @param endTime 截止时间
	 * @return
	 */
	@Query("from Statement s where s.opDate >= :beginTime or s.opDate <= :endTime")
	public List<Statement> getStatementListBetween(Date beginTime, Date endTime);

	/**
	 * @description 获取指定供应商区间内已核对的记录
	 * @param manufacturerId 供应商Id
	 * @param beginTime 起始时间
	 * @param endTime 截止时间
	 * @return
	 */
	@Query("from Statement s where s.matInfo.manufacturerId = :manufacturerId and s.isHD = 'YES' and s.opDate >= :beginTime and s.opDate <= :endTime")
	public List<Statement> getStatementListAlready(String manufacturerId, Date beginTime, Date endTime);

	/**
	 * @description 获取未审核的结算
	 */
	@Query("from Statement s where s.isHD is null or s.isHD = 'NO'")
	public List<Statement> getUnStatementList();
}