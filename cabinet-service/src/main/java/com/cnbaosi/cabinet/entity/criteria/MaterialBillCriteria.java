package com.cnbaosi.cabinet.entity.criteria;

/**
 * 物料流水查询条件
 * 
 * @author Weifeng Li
 */
public class MaterialBillCriteria {

	private String text; // 物料名称、编号、规格
	private String fullname; // 领用人、归还人姓名
	private String cabinetId; //设备id
	private Integer operateType; // 操作类型 1:领出 2:存入

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

}
