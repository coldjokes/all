package com.dosth.dto;

import java.io.Serializable;

/**
 * @description 物料信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class MatInfo implements Serializable {
	/** 物料Id */
	private String matId;
	/** 编号 */
	private String barCode;
	/** 类型编码 */
	private String typeCode;
	/**包装单位*/
	private String packUnit;
	/** 物料名称 */
	private String name;
	/** 品牌 */
	private String brand;
	/** 规格 */
	private String spec;
	/** 借出类型 */
	private String borrowType;
	/** 借出类型名称 */
	private String borrowTypeName;
	/** 图片 */
	private String icon;
	/** 库存单位 */
	private String storeUnit;
	/** 包装数量 */
	private Integer packNum;
	/** 供应商 */
	private String manufacturerName;
	/** 备注 */
	private String remark;

	public MatInfo() {
	}

	public MatInfo(String matId) {
		this.matId = matId;
	}

	public String getMatId() {
		return this.matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getName() {
		return this.name;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getBorrowType() {
		return this.borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	public String getBorrowTypeName() {
		return this.borrowTypeName;
	}

	public void setBorrowTypeName(String borrowTypeName) {
		this.borrowTypeName = borrowTypeName;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getStoreUnit() {
		return this.storeUnit;
	}

	public void setStoreUnit(String storeUnit) {
		this.storeUnit = storeUnit;
	}

	public Integer getPackNum() {
		return this.packNum;
	}

	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}

	public String getManufacturerName() {
		return this.manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}