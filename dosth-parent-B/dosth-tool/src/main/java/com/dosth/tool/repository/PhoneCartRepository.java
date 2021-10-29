package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.mobile.PhoneCart;

/**
 * 
 * @description App购物车持久化层
 * @author guozhidong
 *
 */
public interface PhoneCartRepository extends BaseRepository<PhoneCart, String> {

	/**
	 * @description 根据帐户Id和物料Id获取购物车列表
	 * @param accountId 帐户Id
	 * @param matId 物料Id
	 * @return
	 */
	@Query("from PhoneCart c where c.accountId = :accountId and c.matId = :matId")
	public List<PhoneCart> getShoppingList(String accountId, String matId);
	
	/**
	 * @description 根据帐户获取购物车信息
	 * @param accountId 帐户Id
	 * @return
	 */
	@Query("from PhoneCart c where c.accountId = :accountId")
	public List<PhoneCart> getShoppingList(String accountId);
}