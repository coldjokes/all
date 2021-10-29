package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 
 * @description 购物车详情
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class CartInfo implements Serializable {
	/** 购物车Id */
	private String cartId;
	/** 刀具名称 */
	private String name;
	/** 刀具编号 */
	private String barCode;
	/** 规格型号 */
	private String spec;
	/** 包装数量 */
	private int packNum;
	/** 包装单位 */
	private String packUnit;
	/** 添加时间 */
	private String time;
	/** 借出数量 */
	private Integer num;
	/** 库存数量 */
	private Integer remainNum;
	/** 借出类型 */
	private String borrowType;
	/** 借出类型名称 */
	private String borrowTypeName;
	/** 领取类型 */
	private String receiveType;
	/** 领取类型明细 */
	private String receiveInfo;
	/** 图片 */
	private String icon;
	/** 物料Id */
	private String matId;

	public CartInfo() {
	}

	public CartInfo(String cartId) {
		this.cartId = cartId;
	}

	public CartInfo(Integer num, String borrowType) {
		this.num = num;
		this.borrowType = borrowType;
	}

	public CartInfo(Integer num, String borrowType, String receiveType, String receiveInfo) {
		this.num = num;
		this.borrowType = borrowType;
		this.receiveType = receiveType;
		this.receiveInfo = receiveInfo;
	}

	public CartInfo(Integer num, String borrowType, String receiveType, String receiveInfo, String matId) {
		this.num = num;
		this.borrowType = borrowType;
		this.receiveType = receiveType;
		this.receiveInfo = receiveInfo;
		this.matId = matId;
	}

	public CartInfo(String cartId, String name, String barCode, String spec, int packNum,
			String packUnit, String time, Integer num, String borrowType, String icon, String matId) {
		this.cartId = cartId;
		this.name = name;
		this.barCode = barCode;
		this.spec = spec;
		this.packNum = packNum;
		this.packUnit = packUnit;
		this.time = time;
		this.num = num;
		this.borrowType = borrowType;
		this.icon = icon;
		this.matId = matId;
	}

	public CartInfo(String cartId, String name, String barCode, String spec, int packNum,
			String packUnit, String time, Integer num, String borrowType, String borrowTypeName, String icon,
			String matId) {
		this.cartId = cartId;
		this.name = name;
		this.barCode = barCode;
		this.spec = spec;
		this.packNum = packNum;
		this.packUnit = packUnit;
		this.time = time;
		this.num = num;
		this.borrowType = borrowType;
		this.borrowTypeName = borrowTypeName;
		this.icon = icon;
		this.matId = matId;
	}

	public CartInfo(String cartId, String name, String barCode, String spec, int packNum,
			String packUnit, String time, Integer num, Integer remainNum, String borrowType, String borrowTypeName,
			String receiveType, String receiveInfo, String icon, String matId) {
		this.cartId = cartId;
		this.name = name;
		this.barCode = barCode;
		this.spec = spec;
		this.packNum = packNum;
		this.packUnit = packUnit;
		this.time = time;
		this.num = num;
		this.remainNum = remainNum;
		this.borrowType = borrowType;
		this.borrowTypeName = borrowTypeName;
		this.receiveType = receiveType;
		this.receiveInfo = receiveInfo;
		this.icon = icon;
		this.matId = matId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public int getPackNum() {
		return packNum;
	}

	public void setPackNum(int packNum) {
		this.packNum = packNum;
	}

	public String getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(Integer remainNum) {
		this.remainNum = remainNum;
	}

	public String getBorrowType() {
		return this.borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	public String getBorrowTypeName() {
		return borrowTypeName;
	}

	public void setBorrowTypeName(String borrowTypeName) {
		this.borrowTypeName = borrowTypeName;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getReceiveInfo() {
		return receiveInfo;
	}

	public void setReceiveInfo(String receiveInfo) {
		this.receiveInfo = receiveInfo;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMatId() {
		return matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}
}