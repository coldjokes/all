package com.cnbaosi.dto.tool;

import java.io.Serializable;

/**
 * @description 物料信息
 * @author guozhidong
 */
@SuppressWarnings("serial")
public class FeignMat implements Serializable {
	private String manufacturerName; // 供应商名称
	private String manufacturerNo; // 供应商编号
	private String matId; // 物料Id
	private String matName; // 物料名称
	private String barCode; // 物料编号
	private String spec; // 规格
	private Integer packNum; // 包装数量
	private Float stotePrice; // 单价
	private String brand; // 品牌
	private String icon; // 图片
	private String borrowType; // 领用方式

	public FeignMat() {
	}

	public String getMatId() {
		return matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getManufacturerName() {
		return this.manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getManufacturerNo() {
		return manufacturerNo;
	}

	public void setManufacturerNo(String manufacturerNo) {
		this.manufacturerNo = manufacturerNo;
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

	public Integer getPackNum() {
		return this.packNum;
	}

	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}

	public Float getStotePrice() {
		return this.stotePrice;
	}

	public void setStotePrice(Float stotePrice) {
		this.stotePrice = stotePrice;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getBorrowType() {
		return this.borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}
}