package com.dosth.tool.external.entity;

/**
 * 物料信息 
 * 
 * @author chen
 */
public class ExternalMatInfo {

	private String matId; //物料id
	private String matName; //物料名称
	private String matSpec; //物料规格
	private String matBarCode; //物料编号
	private String borrowType; //借出类型
	private Integer packNum; //包装数量
	private String brand; //品牌
	private String icon; //物料图片
	private String manufacturerId; //供应商
	private float storePrice; //库存成本
	
	public String getMatId() {
		return matId;
	}
	public void setMatId(String matId) {
		this.matId = matId;
	}
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}
	public String getMatSpec() {
		return matSpec;
	}
	public void setMatSpec(String matSpec) {
		this.matSpec = matSpec;
	}
	public String getMatBarCode() {
		return matBarCode;
	}
	public void setMatBarCode(String matBarCode) {
		this.matBarCode = matBarCode;
	}
	public String getBorrowType() {
		return borrowType;
	}
	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}
	public Integer getPackNum() {
		return packNum;
	}
	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public float getStorePrice() {
		return storePrice;
	}
	public void setStorePrice(float storePrice) {
		this.storePrice = storePrice;
	}
}

