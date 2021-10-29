package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description 购物车
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "AppCart", description = "购物车")
public class AppCart implements Serializable {
	@ApiModelProperty(name = "shoppingId", value = "购物车主键")
	private String shoppingId; // 购物车主键
	@ApiModelProperty(name = "knifeId", value = "物料主键")
	private String knifeId; // 物料主键
	@ApiModelProperty(name = "knifeName", value = "刀具名称")
	private String knifeName; // 刀具名称
	@ApiModelProperty(name = "knifePrice", value = "刀具价格")
	private Float knifePrice; // 刀具价格
	@ApiModelProperty(name = "knifePosition", value = "刀具位置")
	private String knifePosition; // 刀具位置
	@ApiModelProperty(name = "knifeTotal", value = "刀具库存")
	private int knifeTotal; // 刀具库存
	@ApiModelProperty(name = "shoppingTotal", value = "购买数量")
	private int shoppingTotal; // 购买数量
	@ApiModelProperty(name = "knifeImage", value = "刀具图片")
	private String knifeImage; // 刀具图片
	@ApiModelProperty(name = "knifeSpec", value = "刀具规格")
	private String knifeSpec; // 刀具规格

	public AppCart() {
	}

	public AppCart(String shoppingId, String knifeId, String knifeName, Float knifePrice, String knifePosition,
			int knifeTotal, int shoppingTotal, String knifeImage, String knifeSpec) {
		this.shoppingId = shoppingId;
		this.knifeId = knifeId;
		this.knifeName = knifeName;
		this.knifePrice = knifePrice;
		this.knifePosition = knifePosition;
		this.knifeTotal = knifeTotal;
		this.shoppingTotal = shoppingTotal;
		this.knifeImage = knifeImage;
		this.knifeSpec = knifeSpec;
	}

	public String getShoppingId() {
		return this.shoppingId;
	}

	public void setShoppingId(String shoppingId) {
		this.shoppingId = shoppingId;
	}

	public String getKnifeId() {
		return this.knifeId;
	}

	public void setKnifeId(String knifeId) {
		this.knifeId = knifeId;
	}

	public String getKnifeName() {
		return this.knifeName;
	}

	public void setKnifeName(String knifeName) {
		this.knifeName = knifeName;
	}

	public Float getKnifePrice() {
		return this.knifePrice;
	}

	public void setKnifePrice(Float knifePrice) {
		this.knifePrice = knifePrice;
	}

	public String getKnifePosition() {
		return this.knifePosition;
	}

	public void setKnifePosition(String knifePosition) {
		this.knifePosition = knifePosition;
	}

	public int getKnifeTotal() {
		return this.knifeTotal;
	}

	public void setKnifeTotal(int knifeTotal) {
		this.knifeTotal = knifeTotal;
	}

	public int getShoppingTotal() {
		return this.shoppingTotal;
	}

	public void setShoppingTotal(int shoppingTotal) {
		this.shoppingTotal = shoppingTotal;
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
}