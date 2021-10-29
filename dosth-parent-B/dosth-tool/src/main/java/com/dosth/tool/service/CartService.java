package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.Cart;
import com.dosth.toolcabinet.dto.CartInfo;

/**
 * 
 * @description 购物车Service
 * @author liweifeng
 *
 */
public interface CartService extends BaseService<Cart>{

	/**
	 * @description 增加购物车
	 * @param 
	 * @return
	 */
	public void addCart(String matId, String type, int num, String accountId, String receiveType, String receiveInfo, String cabinetId);

	/**
	 * @description 购物车详情
	 * @param accountId 账户Id
	 * @return
	 */
	public List<CartInfo> selectCart(String accountId, String cabinetId);
	
	/**
	 * @description 保存到购物车
	 * @param cart
	 * @return
	 */
	public Cart saveCart(Cart cart);

	/**
	 * @description 修改购物车
	 * @param cartId 购物车Id
	 * @return
	 */
	public void updateCart(String cartId, int num);

	/**
	 * @description 删除购物车
	 * @param cartId 购物车Id
	 * @return
	 */
	public void delCart(String cartId);

}
