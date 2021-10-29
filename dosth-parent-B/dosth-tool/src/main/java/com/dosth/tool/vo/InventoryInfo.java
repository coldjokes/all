package com.dosth.tool.vo;

import java.io.Serializable;

/**
 * @description 盘点库存辅助
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class InventoryInfo implements Serializable {
	// 货位Id
	private String detailBoxId;
	// 描述
	private String detailBoxDesc;
	// 物料信息
	private MatInfo matInfo;
	// 使用人员Id
	private String accountId;
	// 使用人员名称
	private String userName;
	// 库存数量
	private Integer storageNum;

	public InventoryInfo() {
	}

	public InventoryInfo(String detailBoxId, String detailBoxDesc, MatInfo matInfo) {
		this.detailBoxId = detailBoxId;
		this.detailBoxDesc = detailBoxDesc;
		this.matInfo = matInfo;
	}

	public InventoryInfo(String detailBoxId, String detailBoxDesc, MatInfo matInfo, String accountId, String userName) {
		this.detailBoxId = detailBoxId;
		this.detailBoxDesc = detailBoxDesc;
		this.matInfo = matInfo;
		this.accountId = accountId;
		this.userName = userName;
	}

	public String getDetailBoxId() {
		return this.detailBoxId;
	}

	public void setDetailBoxId(String detailBoxId) {
		this.detailBoxId = detailBoxId;
	}

	public String getDetailBoxDesc() {
		return this.detailBoxDesc;
	}

	public void setDetailBoxDesc(String detailBoxDesc) {
		this.detailBoxDesc = detailBoxDesc;
	}

	public MatInfo getMatInfo() {
		return this.matInfo;
	}

	public void setMatInfo(MatInfo matInfo) {
		this.matInfo = matInfo;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getStorageNum() {
		return this.storageNum;
	}

	public void setStorageNum(Integer storageNum) {
		this.storageNum = storageNum;
	}
}