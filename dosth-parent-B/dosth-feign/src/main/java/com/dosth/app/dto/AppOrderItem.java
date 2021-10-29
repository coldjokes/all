package com.dosth.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AppOrderItem", description = "订单详情信息")
public class AppOrderItem {

	// 采购id
	@ApiModelProperty(name = "purchaseId", value = "采购id")
	private String purchaseId;

	// 刀具id
	@ApiModelProperty(name = "knifeId", value = "刀具id")
	private String knifeId;

	// 刀具名称
	@ApiModelProperty(name = "knifeName", value = "刀具名称")
	private String knifeName;

	// 机柜名称
	@ApiModelProperty(name = "cupboardName", value = "机柜名称")
	private String cupboardName;

	// 剩余时长:分钟
	@ApiModelProperty(name = "purchaseEndMinute", value = "剩余时长:分钟")
	private Integer purchaseEndMinute;

	// 二维码地址
	@ApiModelProperty(name = "purchaseCodeUrl", value = "二维码地址")
	private String purchaseCodeUrl;

	// 价格
	@ApiModelProperty(name = "purchasePrice", value = "价格")
	private Float purchasePrice;

	// 采购状态 0 待使用， 1 已使用， -1 已失效
	@ApiModelProperty(name = "purchaseType", value = "采购状态 0 待使用， 1 已使用， -1 已失效")
	private String purchaseType;

	// 数量
	@ApiModelProperty(name = "purchaseTotal", value = "数量")
	private Integer purchaseTotal;

	// 数量
	@ApiModelProperty(name = "knifeImage", value = "图片")
	private String knifeImage;

	public AppOrderItem() {
	}

	public AppOrderItem(String purchaseId, String knifeId, String knifeName, String cupboardName,
			Integer purchaseEndMinute, String purchaseCodeUrl, Float purchasePrice, String purchaseType,
			Integer purchaseTotal, String knifeImage) {
		this.purchaseId = purchaseId;
		this.knifeId = knifeId;
		this.knifeName = knifeName;
		this.cupboardName = cupboardName;
		this.purchaseEndMinute = purchaseEndMinute;
		this.purchaseCodeUrl = purchaseCodeUrl;
		this.purchasePrice = purchasePrice;
		this.purchaseType = purchaseType;
		this.purchaseTotal = purchaseTotal;
		this.knifeImage = knifeImage;
	}

	public String getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(String purchaseId) {
		this.purchaseId = purchaseId;
	}

	public String getKnifeId() {
		return knifeId;
	}

	public void setKnifeId(String knifeId) {
		this.knifeId = knifeId;
	}

	public String getKnifeName() {
		return knifeName;
	}

	public void setKnifeName(String knifeName) {
		this.knifeName = knifeName;
	}

	public String getCupboardName() {
		return cupboardName;
	}

	public void setCupboardName(String cupboardName) {
		this.cupboardName = cupboardName;
	}

	public Integer getPurchaseEndMinute() {
		return purchaseEndMinute;
	}

	public void setPurchaseEndMinute(Integer purchaseEndMinute) {
		this.purchaseEndMinute = purchaseEndMinute;
	}

	public String getPurchaseCodeUrl() {
		return purchaseCodeUrl;
	}

	public void setPurchaseCodeUrl(String purchaseCodeUrl) {
		this.purchaseCodeUrl = purchaseCodeUrl;
	}

	public Float getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Float purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public Integer getPurchaseTotal() {
		return purchaseTotal;
	}

	public void setPurchaseTotal(Integer purchaseTotal) {
		this.purchaseTotal = purchaseTotal;
	}

	public String getKnifeImage() {
		return this.knifeImage;
	}

	public void setKnifeImage(String knifeImage) {
		this.knifeImage = knifeImage;
	}
}