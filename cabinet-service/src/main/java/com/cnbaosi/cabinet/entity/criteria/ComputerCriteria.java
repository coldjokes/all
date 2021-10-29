package com.cnbaosi.cabinet.entity.criteria;

/**
 * 主机搜索条件
 * 
 * @author Yifeng Wang  
 */

public class ComputerCriteria {

	private String computerId; //主机id
	private String name; //主机名称

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComputerId() {
		return computerId;
	}

	public void setComputerId(String computerId) {
		this.computerId = computerId;
	}
}

