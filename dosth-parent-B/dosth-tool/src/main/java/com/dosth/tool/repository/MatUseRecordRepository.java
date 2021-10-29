package com.dosth.tool.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.MatUseRecord;

/**
 * @description 物料领用记录持久化层
 * @author guozhidong
 *
 */
public interface MatUseRecordRepository extends BaseRepository<MatUseRecord, String> {

	/**
	 * @description 查询物料领用区间
	 * @param startDate 起始时间
	 * @param endDate   截止时间
	 * @return
	 */
	@Query("from MatUseRecord r where r.opDate >= :startDate and r.opDate <= :endDate")
	public List<MatUseRecord> getMatUseRecordListBetween(Date startDate, Date endDate);

	/**
	 * @description 根据供应商区间内领取的未结算物料列表
	 * @param manufacturerId 供应商Id
	 * @param beginTime      起始时间
	 * @param endTime        截止时间
	 */
	@Query("from MatUseRecord r where not exists (select 1 from StatementDetail d where r.id = d.matUseRecordId and d.statement.isHD = 'YES') and r.matInfo.manufacturerId = :manufacturerId and r.opDate >= :beginTime and r.opDate <= :endTime")
	public List<MatUseRecord> getUnStatementList(String manufacturerId, Date beginTime, Date endTime);
	
	/**
	 * @description 获取领用记录
	 * @param ids 供应商Id
	 */
	@Query("from MatUseRecord r where r.id in ( :ids)")
	public List<MatUseRecord> getRecordList(List<String> ids);

	/**
	 * @description 按部门统计领取数量分组
	 * @return
	 */
	@Query("select r.deptId, sum(r.realNum) from MatUseRecord r group by r.deptId")
	public List<Object[]> getBorrowNumGroupByDept();
}