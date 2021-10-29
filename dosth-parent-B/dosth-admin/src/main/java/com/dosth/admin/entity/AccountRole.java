package com.dosth.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dosth.common.db.entity.BasePojo;

/**
 * 账户角色关系
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class AccountRole extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;

	@Column(columnDefinition = "varchar(36) COMMENT '角色ID'")
	private String roleId;

	public AccountRole() {
	}

	public AccountRole(String accountId, String roleId) {
		this.accountId = accountId;
		this.roleId = roleId;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
}