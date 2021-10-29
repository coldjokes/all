package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.BorrowType;
import com.dosth.tool.entity.Cart;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.repository.CartRepository;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.service.CartService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.toolcabinet.dto.CartInfo;

/**
 * 
 * @description 购物车Service实现
 * @author liweifeng
 *
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;

	@Override
	public void save(Cart cart) throws DoSthException {
		this.cartRepository.save(cart);
	}

	@Override
	public Cart saveCart(Cart cart) {
		return this.cartRepository.save(cart);
	}

	@Override
	public Cart get(Serializable id) throws DoSthException {
		Cart cart = this.cartRepository.findOne(id);
		return cart;
	}

	@Override
	public Cart update(Cart cart) throws DoSthException {
		return this.cartRepository.saveAndFlush(cart);
	}

	@Override
	public void delete(Cart cart) throws DoSthException {
		this.cartRepository.delete(cart);
	}

	@Override
	public void addCart(String matId, String type, int num, String accountId , String receiveType, 
			String receiveInfo, String cabinetId) {
		Cart cart = null;
		MatEquInfo info = this.matEquInfoService.get(matId);
		List<Cart> cartList = this.cartRepository.selectCartByAccountAndMatId(accountId, matId, receiveInfo);
		int borrowNum = num;
		if (type != null && BorrowType.PACK.name().equals(type)) {
			if (BorrowType.METER.equals(info.getBorrowType())) {
				borrowNum = info.getNum() * num;
			}
		}
		if (cartList != null && cartList.size() > 0) {
			cart = cartList.get(0);
			cart.setNum(borrowNum + cart.getNum());
			update(cart);
		} else {
			cart = new Cart(accountId, matId, receiveType, receiveInfo, borrowNum, cabinetId);
			cart = saveCart(cart);
		}
	}

	@Override
	public List<CartInfo> selectCart(String accountId, String cabinetId) {
		List<CartInfo> cartInfoList = new ArrayList<>();
		List<Cart> cartList = this.cartRepository.selectCartByAccountAndCabinet(accountId, cabinetId);
		List<EquDetailSta> equDetailStaList = new ArrayList<>();
		for (Cart cart : cartList) {
			int remainNum= 0;
			equDetailStaList = this.equDetailStaRepository.getStaByCart(cabinetId, cart.getMatId());
			if(equDetailStaList != null) {
				for(EquDetailSta equDetailSta : equDetailStaList) {
					if(equDetailSta.getMatInfoId() != null && equDetailSta.getMatInfoId().equals(cart.getMatId())) {
						remainNum += equDetailSta.getCurNum() == null ? 0 : equDetailSta.getCurNum();
					}
				}
			}
			cartInfoList.add(new CartInfo(cart.getId(), cart.getMatInfo().getMatEquName(), cart.getMatInfo().getBarCode(),
					cart.getMatInfo().getSpec(), cart.getMatInfo().getNum(),
					cart.getMatInfo().getPackUnit(),
					String.valueOf(cart.getAddTime()).substring(0, 10), cart.getNum(), remainNum,
					cart.getMatInfo().getBorrowType().getMessage(), cart.getMatInfo().getBorrowType().name(),
					cart.getReceiveType(), cart.getReceiveInfo(),
					this.toolProperties.getImgUrlPath() + cart.getMatInfo().getIcon(), cart.getMatId()));
		}
		return cartInfoList;
	}

	@Override
	public void updateCart(String cartId, int num) {
		Cart cart = this.cartRepository.findOne(cartId);
		cart.setNum(num);
		if(cart.getNum() == 0) {
			delete(cart);
		}else {
			update(cart);
		}
	}

	@Override
	public void delCart(String cartId) {
		delete(this.cartRepository.getOne(cartId));
	}
}