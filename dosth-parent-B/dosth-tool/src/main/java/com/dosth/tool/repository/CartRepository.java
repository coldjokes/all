package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.Cart;
import com.dosth.tool.entity.SubCabinetDetail;


/**
 * 
 * @description 购物车
 * @author liweifeng
 *
 */
public interface CartRepository extends BaseRepository<Cart, String> {
	
	/**
	 * 根据账户查询购物车
	 * @param accountId 账户Id
	 * @return
	 */
	@Query("from Cart c where c.accountId =:accountId ")
	public List<Cart> selectCartByAccount(String accountId);
	
	/**
	 * 根据账户和柜子查询购物车
	 * @param accountId 账户Id
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@Query("from Cart c where c.accountId =:accountId and c.cabinetId =:cabinetId")
	public List<Cart> selectCartByAccountAndCabinet(String accountId, String cabinetId);


	/**
	 * 根据账户和物料查询购物车
	 * @param accountId 账户Id
	 * @param matId 物料Id
	 * @return
	 */
	@Query("from Cart c where c.accountId =:accountId and c.matId =:matId and c.receiveInfo =:receiveInfo")
	public List<Cart> selectCartByAccountAndMatId(String accountId, String matId, String receiveInfo);
	
	/**
	 * 根据账户和物料查询暂存柜
	 * @param accountId 账户Id
	 * @param matId 物料Id
	 * @return
	 */
	@Query("from SubCabinetDetail d where d.matInfoId =:matInfoId and subBoxId in (select subBoxId from SubBoxAccountRef r where r.accountId =:accountId)")
	public SubCabinetDetail selectSubCabinetByAccountAndMatId(String accountId, String matInfoId);
	
	/**
	 * 根据购物车Id查询
	 * @param cartIds 购车车Id
	 * @return
	 */
	@Query("from Cart c where c.id in (:cartIds)")
	public List<Cart> getCartInfo(List<String> cartIds);
}