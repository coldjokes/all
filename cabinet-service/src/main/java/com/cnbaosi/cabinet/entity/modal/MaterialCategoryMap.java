package com.cnbaosi.cabinet.entity.modal;

/**
 * 类别类别映射
 * 
 * @author Yifeng Wang  
 */

public class MaterialCategoryMap extends BaseModel<MaterialCategoryMap> {
	
	private static final long serialVersionUID = 1L;
	
	private String categoryId; //物料类别id
	private String materialId; //物料id
	
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
}

