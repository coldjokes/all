package com.dosth.tool.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.SubCabinetBill;

/**
 * 
 * @description 副柜流水持久化层
 * @author guozhidong
 *
 */
public interface SubCabinetBillRepository extends BaseRepository<SubCabinetBill, String> {
	/**
	 * @description 获取指定暂存柜存入时间降序排列
	 * @param subBoxId 暂存柜Id 
	 * @param matInfoId 物料信息Id
	 * @param accountId 帐户Id
	 * @return
	 */
	@Query("from SubCabinetBill b where b.subBoxId = :subBoxId and b.matInfoId = :matInfoId and b.accountId = :accountId order by b.opDate desc")
	public List<SubCabinetBill> getLastInTime(String subBoxId, String matInfoId, String accountId);

	/**
	 * @description 指定物料Id区间内暂存柜流水
	 * @param matId 物料Id
	 * @param beginTime 起始时间
	 * @param endTime 截止时间
	 * @return
	 */
	@Query("from SubCabinetBill b where b.matInfoId = :matId and b.opDate >= :beginTime and b.opDate <= :endTime")
	public List<SubCabinetBill> getBillList(String matId, Date beginTime, Date endTime);
}