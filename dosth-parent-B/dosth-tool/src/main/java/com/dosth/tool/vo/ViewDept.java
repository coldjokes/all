package com.dosth.tool.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

/**
 * 部门视图
 * 
 * @author guozhidong
 *
 */
@Entity
@Immutable
@Subselect("SELECT * FROM VIEW_DEPT")
@SuppressWarnings("serial")
public class ViewDept implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "DEPT_ID")
	private String deptId;
	
	@Column(name = "DEPT_NO")
	private String deptNo;

	@Column(name = "DEPT_P_ID")
	private String deptPId;

	@Column(name = "DEPT_NAME")
	private String deptName;

	@Column(name = "DEPT_STATUS")
	private String deptStatus;

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

	public String getDeptPId() {
		return this.deptPId;
	}

	public void setDeptPId(String deptPId) {
		this.deptPId = deptPId;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptStatus() {
		return deptStatus;
	}

	public void setDeptStatus(String deptStatus) {
		this.deptStatus = deptStatus;
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
		ViewDept other = (ViewDept) obj;
		if (deptId == null) {
			if (other.deptId != null)
				return false;
		} else if (!deptId.equals(other.deptId))
			return false;
		return true;
	}
}