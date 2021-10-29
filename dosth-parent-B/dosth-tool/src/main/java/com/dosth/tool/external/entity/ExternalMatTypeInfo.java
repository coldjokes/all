package com.dosth.tool.external.entity;

/**
 * 物料类型信息 
 * 
 * @author chen
 */
public class ExternalMatTypeInfo {

	private String matTypeId; //物料类型id
	private String matTypeName; //物料类型名称
	
	public ExternalMatTypeInfo(String matTypeId, String matTypeName) {
		this.matTypeId = matTypeId;
		this.matTypeName = matTypeName;
	}

	public String getMatTypeId() {
		return matTypeId;
	}

	public void setMatTypeId(String matTypeId) {
		this.matTypeId = matTypeId;
	}

	public String getMatTypeName() {
		return matTypeName;
	}

	public void setMatTypeName(String matTypeName) {
		this.matTypeName = matTypeName;
	}
	
}

