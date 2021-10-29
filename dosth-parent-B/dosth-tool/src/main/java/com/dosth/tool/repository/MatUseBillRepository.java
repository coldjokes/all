package com.dosth.tool.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.entity.MatUseRecord;

/**
 * 
 * @description 物料领用流水持久化层
 * @author guozhidong
 *
 */
public interface MatUseBillRepository extends BaseRepository<MatUseBill, String> {
	/**
	 * @description 根据帐户查询待归还的列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@Query("select b from MatReturnBack r right join r.matUseBill b where b.accountId = :accountId and (r.id is null or r.isReturnBack = 'NOTBACK') order by b.opDate desc")
	public List<MatUseBill> getUnReturnback(String accountId);
	
	/**
	 * @description 根据领用记录查询流水
	 * @param recordId 
	 * @return
	 */
	@Query("from MatUseBill b where b.matUseRecordId = :recordId")
	public List<MatUseBill> getInfoByRecordId(String recordId);
	
	/**
	 * @description 获取需要同步的领用记录
	 * @param cabinetName
	 * @param endTime 
	 * @param beginTime
	 * @return
	 */
	@Query("select distinct(b.matUseRecord) from MatUseBill b where b.matUseRecord.opDate >:beginTime and b.matUseRecord.opDate <=:endTime and b.equDetailSta.equDetail.equSetting.wareHouseAlias =:cabinetName")
	public List<MatUseRecord> getSyncRecord(String cabinetName, Date beginTime, Date endTime);
	
	/**
	 * @description 获取待归还的物料类型
	 * @param accountId
	 * @param serialNo 
	 * @return
	 */
	@Query("select distinct b.matUseRecord.tree from MatReturnBack r right join r.matUseBill b where b.accountId = :accountId and (b.equDetailSta.equDetail.equSettingId =:cabinetId or b.equDetailSta.equDetail.equSetting.equSettingParentId =:cabinetId) and (r.id is null or r.isReturnBack = 'NOTBACK')")
	public List<MatCategoryTree> getUnReturnTypeList(String accountId, String cabinetId);
	
	/**
	 * @description 获取批量归还的领用流水
	 * @param accountId 帐户Id
	 * @return
	 */
	@Query(value = "select b from MatReturnBack r right join r.matUseBill b where b.accountId = :accountId and b.matInfoId =:matInfoId and (b.equDetailSta.equDetail.equSettingId =:cabinetId or b.equDetailSta.equDetail.equSetting.equSettingParentId =:cabinetId) and (r.id is null or r.isReturnBack = 'NOTBACK') order by b.opDate asc")
	public List<MatUseBill> getBatchReturnBillList(String accountId, String matInfoId, String cabinetId);
}