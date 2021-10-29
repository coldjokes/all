package com.dosth.tool.external.entity;

/**
 *  
 * 
 * @author Yifeng Wang  
 */
public class ExternalMat {

	private String matId; //物料id
	private String matName; //物料名称
	private String matSpec; //物料规格
	private String matBarCode; //物料编号
	private String supplyID; //供应商ID
	private String supplyFullName; //供应商全称
	private Integer packNum; //包装数量
	
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
	public Integer getPackNum() {
		return packNum;
	}
	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}
	public String getSupplyID() {
		return supplyID;
	}
	public void setSupplyID(String supplyID) {
		this.supplyID = supplyID;
	}
	public String getSupplyFullName() {
		return supplyFullName;
	}
	public void setSupplyFullName(String supplyFullName) {
		this.supplyFullName = supplyFullName;
	}
	
}

