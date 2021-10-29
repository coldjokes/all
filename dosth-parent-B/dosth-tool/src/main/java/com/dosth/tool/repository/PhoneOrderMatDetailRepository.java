package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.mobile.PhoneOrderMatDetail;

/**
 * 
 * @description 手机预约物料明细持久化层
 * @author guozhidong
 *
 */
public interface PhoneOrderMatDetailRepository extends BaseRepository<PhoneOrderMatDetail, String> {

	/**
	 * @description 根据手机订单物料Id获取物料明细列表
	 * @param orderMatId 手机订单物料Id
	 * @return
	 */
	@Query("from PhoneOrderMatDetail m where m.phoneOrderMatId = :orderMatId")
	public List<PhoneOrderMatDetail> findDetailListByOrderMatId(String orderMatId);

	/**
	 * @description 根据订单Id获取订单明细列表
	 * @param orderId 订单Id
	 * @return
	 */
	@Query("from PhoneOrderMatDetail m where m.phoneOrderMat.phoneOrderId = :orderId")
	public List<PhoneOrderMatDetail> findPhoneOrderMatDetailsByPhoneOrderId(String orderId);
}