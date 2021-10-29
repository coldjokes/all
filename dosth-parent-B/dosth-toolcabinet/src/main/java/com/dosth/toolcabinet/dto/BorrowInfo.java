package com.dosth.toolcabinet.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @description 领取信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class BorrowInfo implements Serializable {
	private String cabinetId; // 柜子Id
	private List<CartInfo> cartList; // 取料信息
	private String accountId; // 帐户Id

	public BorrowInfo() {
	}

	public BorrowInfo(String cabinetId, List<CartInfo> cartList, String accountId) {
		this.cabinetId = cabinetId;
		this.cartList = cartList;
		this.accountId = accountId;
	}

	public String getCabinetId() {
		return this.cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public List<CartInfo> getCartList() {
		return this.cartList;
	}

	public void setCartList(List<CartInfo> cartList) {
		this.cartList = cartList;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String toString() {
		return "BorrowInfo [cabinetId=" + cabinetId + ", cartList=" + cartList + ", accountId=" + accountId + "]";
	}
}