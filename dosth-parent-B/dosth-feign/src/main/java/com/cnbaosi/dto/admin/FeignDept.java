package com.cnbaosi.dto.admin;

import java.io.Serializable;

/**
 * @description 部门
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignDept implements Serializable {
	private String deptId; // 部门Id
	private String deptNo; // 部门编号
	private String deptName; // 部门名称
	private String parentDeptNo; // 上级部门编号
	private String parentDeptName; // 上级部门名称

	public FeignDept() {
	}

	public FeignDept(String deptId) {
		this.deptId = deptId;
	}

	public FeignDept(String deptId, String deptName) {
		this.deptId = deptId;
		this.deptName = deptName;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getParentDeptNo() {
		return parentDeptNo;
	}

	public void setParentDeptNo(String parentDeptNo) {
		this.parentDeptNo = parentDeptNo;
	}

	public String getParentDeptName() {
		return this.parentDeptName;
	}

	public void setParentDeptName(String parentDeptName) {
		this.parentDeptName = parentDeptName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeignDept other = (FeignDept) obj;
		if (deptId == null) {
			if (other.deptId != null)
				return false;
		} else if (!deptId.equals(other.deptId))
			return false;
		return true;
	}
}