package com.cnbaosi.cabinet.entity.criteria;

import java.util.List;

/**
 * 物料类别相关条件
* 
* @author Yifeng Wang
*/
public class MaterialCategoryMapCriteria {
	
	private String categoryId; //物料类别id
	private String materialId; //物料id
	private List<String> materialIdList; //物料id list
	private List<String> categoryIdList; //物料分类id list
	
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
	public List<String> getMaterialIdList() {
		return materialIdList;
	}
	public void setMaterialIdList(List<String> materialIdList) {
		this.materialIdList = materialIdList;
	}
	public List<String> getCategoryIdList() {
		return categoryIdList;
	}
	public void setCategoryIdList(List<String> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}
}
