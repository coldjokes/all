package com.dosth.tool.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.mobile.PhoneOrder;
import com.dosth.tool.entity.mobile.PhoneOrderSta;

/**
 * 
 * @description 手机预约状态持久化
 * @author guozhidong
 *
 */
public interface PhoneOrderStaRepository extends BaseRepository<PhoneOrderSta, String> {
	
	/**
	 * @description 根据订单编号获取订单状态
	 * @param orderId 订单编号
	 * @return
	 */
	@Query("select s.phoneOrder from PhoneOrderSta s where s.phoneOrderId = :orderId")
	public List<PhoneOrder> getFinishedOrderListByOrderId(String orderId);
	
	/**
	 * @description 查询待使用的订单
	 * @param userId 用户Id
	 * @param date 起始时间
	 * @param pageable 分页对象
	 * @return
	 */
	@Query(value="select o from PhoneOrderSta s right join s.phoneOrder o where s.id is null and o.accountId = :userId and o.orderCreatedTime >= :date", 
			countQuery="select count(o) from PhoneOrderSta s right join s.phoneOrder o where s.id is null and o.accountId = :userId and o.orderCreatedTime >= :date")
	public Page<PhoneOrder> getWait(String userId, Date date, Pageable pageable);

	/**
	 * @description 查询已使用的订单
	 * @param userId 用户Id
	 * @param pageable 分页对象
	 * @return
	 */
	@Query(value="select o from PhoneOrderSta s right join s.phoneOrder o where s.id is not null and o.accountId = :userId", 
			countQuery="select count(o) from PhoneOrderSta s right join s.phoneOrder o where s.id is not null and o.accountId = :userId")
	public Page<PhoneOrder> getFinished(String userId, Pageable pageable);
	
	/**
	 * @description 查询已失效的订单
	 * @param userId 用户Id
	 * @param date 起始时间
	 * @param pageable 分页对象
	 * @return
	 */
	@Query(value="select o from PhoneOrderSta s right join s.phoneOrder o where s.id is null and o.accountId = :userId and o.orderCreatedTime < :date", 
			countQuery="select count(o) from PhoneOrderSta s right join s.phoneOrder o where s.id is null and o.accountId = :userId and o.orderCreatedTime < :date")
	public Page<PhoneOrder> getUnUsed(String userId, Date date, Pageable pageable);
}