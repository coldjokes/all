package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.BorrowType;

/**
 * 物料/设备定义
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class MatEquInfo extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '物料编号'")
	@PageTableTitle(value = "物料编号")
	private String barCode;

	@Column(columnDefinition = "varchar(50) COMMENT '物料名称'")
	@PageTableTitle(value = "物料名称")
	private String matEquName;

	@Column(columnDefinition = "varchar(50) COMMENT '物料型号'")
	@PageTableTitle(value = "物料型号")
	private String spec;

	@Column(name = "MANUFACTURER_ID", columnDefinition = "varchar(36) COMMENT '供应商ID'")
	private String manufacturerId;
	@ManyToOne
	@JoinColumn(name = "MANUFACTURER_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "供应商", isForeign = true)
	private Manufacturer manufacturer;

	@Transient
	@PageTableTitle(value = "供应商名称")
	private String manufacturerName;

	@Column(columnDefinition = "varchar(50) COMMENT '品牌'")
	@PageTableTitle(value = "品牌")
	private String brand;

	@Column(columnDefinition = "int(10) COMMENT '包装数量'")
	@PageTableTitle(value = "包装数量")
	private Integer num;

	@Column(columnDefinition = "varchar(50) COMMENT '包装单位'")
	@PageTableTitle(value = "包装单位")
	private String packUnit;

	@Column(name = "STORE_PRICE", precision = 8, scale = 2, columnDefinition = "float COMMENT '库存成本'")
	@PageTableTitle(value = "库存成本")
	private Float storePrice;

	@PageTableTitle("最低库存")
	@Column(columnDefinition = "int(11) COMMENT '最低库存'")
	private Integer lowerStockNum;

	@PageTableTitle("使用寿命")
	@Column(columnDefinition = "int(11) COMMENT '使用寿命'")
	private Integer useLife;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '借出类型'")
	@PageTableTitle(value = "借出类型", isEnum = true)
	private BorrowType borrowType;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '以旧换新'")
	@PageTableTitle(value = "以旧换新", isEnum = true, isVisible = false)
	private YesOrNo oldForNew;

	@Column(columnDefinition = "varchar(500) COMMENT '备注'")
	@PageTableTitle(value = "备注", isVisible = false)
	private String remark;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	@Column(columnDefinition = "varchar(50) COMMENT '图片'")
	private String icon;

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getMatEquName() {
		return matEquName;
	}

	public void setMatEquName(String matEquName) {
		this.matEquName = matEquName;
	}

	public Integer getNum() {
		if (this.num == null) {
			this.num = 1;
		}
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public Float getStorePrice() {
		if (this.storePrice == null) {
			this.storePrice = 0F;
		}
		return this.storePrice;
	}

	public void setStorePrice(Float storePrice) {
		this.storePrice = storePrice;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BorrowType getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(BorrowType borrowType) {
		this.borrowType = borrowType;
	}

	public YesOrNo getOldForNew() {
		return oldForNew;
	}

	public void setOldForNew(YesOrNo oldForNew) {
		this.oldForNew = oldForNew;
	}

	public Integer getLowerStockNum() {
		if (this.lowerStockNum == null) {
			this.lowerStockNum = 0;
		}
		return this.lowerStockNum;
	}

	public void setLowerStockNum(Integer lowerStockNum) {
		this.lowerStockNum = lowerStockNum;
	}

	public Integer getUseLife() {
		if (this.useLife == null) {
			this.useLife = 1;
		}
		return this.useLife;
	}

	public void setUseLife(Integer useLife) {
		this.useLife = useLife;
	}

	public UsingStatus getStatus() {
		return status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

}