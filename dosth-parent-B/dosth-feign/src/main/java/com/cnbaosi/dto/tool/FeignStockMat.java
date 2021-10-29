package com.cnbaosi.dto.tool;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FeignStockMat implements Serializable {
	private String matName; // 物料名称
	private String barCode; // 物料编号
	private String spec; // 规格
	private Integer packNum; // 包装数量
	private String brand; // 品牌

	public FeignStockMat() {
	}

	public FeignStockMat(String barCode, String matName, String spec, Integer packNum, String brand) {
		this.barCode = barCode;
		this.matName = matName;
		this.spec = spec;
		this.packNum = packNum;
		this.brand = brand;
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

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
}