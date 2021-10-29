package com.dosth.tool.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.mobile.PhoneOrder;

/**
 * 
 * @description 手机预约持久化层
 * @author guozhidong
 *
 */
public interface PhoneOrderRepository extends BaseRepository<PhoneOrder, String> {
	/**
	 * @description 根据用户获取所有订单
	 * @param accountId 用户Id
	 * @param pageable 分页对象
	 * @return
	 */
	@Query(value="select o from PhoneOrder o where o.accountId = :accountId order by orderCreatedTime desc",
			countQuery="select count(*) from PhoneOrder o where o.accountId = :accountId")
	public Page<PhoneOrder> getPageAll(String accountId, Pageable pageable);
}