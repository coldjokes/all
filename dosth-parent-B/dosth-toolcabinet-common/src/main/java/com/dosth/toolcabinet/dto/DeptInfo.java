package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 
 * @description 部门详情
 * @author chen
 *
 */
@SuppressWarnings("serial")
public class DeptInfo implements Serializable {
	/** 部门Id */
	private String deptId;
	/** 部门名称 */
	private String deptName;

	public DeptInfo() {
		super();
	}

	public DeptInfo(String deptId, String deptName) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

}
