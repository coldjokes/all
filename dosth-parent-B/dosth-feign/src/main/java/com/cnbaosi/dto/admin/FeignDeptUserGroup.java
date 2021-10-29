package com.cnbaosi.dto.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 部门人员分组
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignDeptUserGroup implements Serializable {
	private FeignDept dept; // 部门信息
	private List<FeignUser> userList; // 人员列表

	public FeignDeptUserGroup() {
	}

	public FeignDeptUserGroup(FeignDept dept) {
		this.dept = dept;
	}

	public FeignDept getDept() {
		return this.dept;
	}

	public void setDept(FeignDept dept) {
		this.dept = dept;
	}

	public List<FeignUser> getUserList() {
		if (this.userList == null) {
			this.userList = new ArrayList<>();
		}
		return this.userList;
	}

	public void setUserList(List<FeignUser> userList) {
		this.userList = userList;
	}
}