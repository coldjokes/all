package com.dosth.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AppKnifes", description = "刀具信息")
public class AppKnifes {

	// 主键
	@ApiModelProperty(name = "knifeId", value = "主键")
	private String knifeId;

	// 刀具编号
	@ApiModelProperty(name = "knifeNo", value = "刀具编号")
	private String knifeNo;
	
	// 刀具名称
	@ApiModelProperty(name = "knifeName", value = "刀具名称")
	private String knifeName;

	// 刀具价格
	@ApiModelProperty(name = "knifePrice", value = "刀具价格")
	private Float knifePrice;

	// 刀具位置
	@ApiModelProperty(name = "knifePosition", value = "刀具位置")
	private String knifePosition;

	// 刀具库存
	@ApiModelProperty(name = "knifeTotal", value = "刀具库存")
	private Integer knifeTotal;

	// 刀具图片
	@ApiModelProperty(name = "knifeImage", value = "刀具图片")
	private String knifeImage;
	
	// 刀具规格
	@ApiModelProperty(name = "knifeSpec", value = "刀具规格")
	private String knifeSpec;
	
	// 购物车数量
	@ApiModelProperty(name = "counter", value = "购物车数量")
	private Integer counter;

	public AppKnifes() {
	}

	public AppKnifes(String knifeId, String knifeNo, String knifeName, Float knifePrice, String knifePosition, int knifeTotal,
			String knifeImage, String knifeSpec) {
		this.knifeId = knifeId;
		this.knifeNo = knifeNo;
		this.knifeName = knifeName;
		this.knifePrice = knifePrice;
		this.knifePosition = knifePosition;
		this.knifeTotal = knifeTotal;
		this.knifeImage = knifeImage;
		this.knifeSpec = knifeSpec;
	}

	public String getKnifeId() {
		return knifeId;
	}

	public void setKnifeId(String knifeId) {
		this.knifeId = knifeId;
	}

	public String getKnifeNo() {
		return knifeNo;
	}

	public void setKnifeNo(String knifeNo) {
		this.knifeNo = knifeNo;
	}

	public String getKnifeName() {
		return knifeName;
	}

	public void setKnifeName(String knifeName) {
		this.knifeName = knifeName;
	}

	public Float getKnifePrice() {
		return knifePrice;
	}

	public void setKnifePrice(Float knifePrice) {
		this.knifePrice = knifePrice;
	}

	public String getKnifePosition() {
		return knifePosition;
	}

	public void setKnifePosition(String knifePosition) {
		this.knifePosition = knifePosition;
	}

	public Integer getKnifeTotal() {
		return this.knifeTotal;
	}

	public void setKnifeTotal(Integer knifeTotal) {
		this.knifeTotal = knifeTotal;
	}

	public String getKnifeImage() {
		return this.knifeImage;
	}

	public void setKnifeImage(String knifeImage) {
		this.knifeImage = knifeImage;
	}

	
	public String getKnifeSpec() {
		return knifeSpec;
	}

	public void setKnifeSpec(String knifeSpec) {
		this.knifeSpec = knifeSpec;
	}

	public Integer getCounter() {
		if (this.counter == null) {
			this.counter = 1;
		}
		return this.counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}
}