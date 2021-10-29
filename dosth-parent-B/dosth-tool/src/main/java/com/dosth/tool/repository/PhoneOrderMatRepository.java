package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.mobile.PhoneOrderMat;

/**
 * @description 物料订单详情持久化层
 * @author guozhidong
 *
 */
public interface PhoneOrderMatRepository extends BaseRepository<PhoneOrderMat, String> {

	/**
	 * 根据刀具Id查询预约信息
	 * 
	 * @param matInfoId 刀具Id
	 * @return
	 */
	@Query("from PhoneOrderMat m where m.matInfoId = :matInfoId")
	public List<PhoneOrderMat> getPhoneOrderMatList(String matInfoId);

	/**
	 * 根据预约Id查询预约信息
	 * 
	 * @param phoneOrderId 订单号
	 * @return List of PhoneOrderMat
	 */
	@Query("from PhoneOrderMat where phoneOrderId = :phoneOrderId")
	public List<PhoneOrderMat> findPhoneOrderMatsByPhoneOrderId(String phoneOrderId);
}