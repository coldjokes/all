package com.cnbaosi.cabinet.entity.criteria;

/**
 * 柜体相关搜索条件
 * 
 * @author Yifeng Wang  
 */

public class CabinetCriteria {
	
	private String computerId; //主机id
	private String name; //柜体名称
	
	public String getComputerId() {
		return computerId;
	}
	public void setComputerId(String computerId) {
		this.computerId = computerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

