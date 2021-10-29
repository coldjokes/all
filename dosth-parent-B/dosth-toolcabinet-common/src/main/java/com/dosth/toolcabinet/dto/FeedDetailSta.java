package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * @description 前台补料界面封装
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeedDetailSta implements Serializable {
	// 柜子弹簧Id
	private String staId;
	// 物料Id
	private String matId;
	// 位置
	private String position;
	// 图片
	private String icon;
	// 品名
	private String matName;
	// 编号
	private String barCode;
	// 规格型号
	private String spec;
	// 包装数量
	private int packNum;
	// 包装单位
	private String packUnit;
	// 现有库存
	private int curNum;
	// 待补料数量
	private int waitNum;
	// 最大库存
	private int maxNum;

	public FeedDetailSta() {
	}

	public FeedDetailSta(String staId, String matId, String position, String icon, String matName, String barCode,
			String spec, int packNum, String packUnit, int curNum, int waitNum, int maxNum) {
		this.staId = staId;
		this.matId = matId;
		this.position = position;
		this.icon = icon;
		this.matName = matName;
		this.barCode = barCode;
		this.spec = spec;
		this.packNum = packNum;
		this.packUnit = packUnit;
		this.curNum = curNum;
		this.waitNum = waitNum;
		this.maxNum = maxNum;
	}

	public String getStaId() {
		return this.staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public String getMatId() {
		return this.matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getMatName() {
		return this.matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public int getPackNum() {
		return this.packNum;
	}

	public void setPackNum(int packNum) {
		this.packNum = packNum;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public int getCurNum() {
		return this.curNum;
	}

	public void setCurNum(int curNum) {
		this.curNum = curNum;
	}

	public int getWaitNum() {
		return this.waitNum;
	}

	public void setWaitNum(int waitNum) {
		this.waitNum = waitNum;
	}

	public int getMaxNum() {
		return this.maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
}