package com.dosth.common.shiro;

import java.io.Serializable;
import java.util.List;

/**
 * 封装Authentication对象,使得Shiro Subject除了携带用户登录信息以外的更多信息
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ShiroAccount implements Serializable {

	private String id; // 主键Id
	private String loginName; // 账号
	private String name; // 姓名
	private String deptId; // 部门Id
	private List<String> roleList; // 角色集合
	private String deptName; // 部门名称
	private List<String> roleNames; // 角色名称集合
	
	private String token;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public List<String> getRoleList() {
		return this.roleList;
	}

	public void setRoleList(List<String> roleList) {
		this.roleList = roleList;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<String> getRoleNames() {
		return this.roleNames;
	}

	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ShiroAccount other = (ShiroAccount) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}