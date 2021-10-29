package com.cnbaosi.cabinet.entity.criteria;

/**
 * 物料查询条件
 * 
 * @author Yifeng Wang  
 */
public class MaterialRemindCriteria {

	private String text; //物料名称、编号或规格
	private String materialId; //物料id
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
}

