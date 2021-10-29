package com.dosth.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 账号表
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class Account extends BasePojo {
	/**
	 * 用户信息
	 */
	@OneToOne(mappedBy = "account")
	@JoinColumn(name = "userId")
	@PageTableTitle(value = "用户", isForeign = true)
	private User user;

	@PageTableTitle(value = "账号")
	@Column(columnDefinition = "varchar(50) COMMENT '帐号'")
	private String loginName;

	@Column(columnDefinition = "varchar(255) COMMENT '密码'")
	private String password;

	@Column(columnDefinition = "varchar(255) COMMENT '人脸密码'")
	private String facePwd;

	@Column(columnDefinition = "varchar(10) COMMENT '密码盐'")
	private String salt;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '账户状态：1.启用， 2.冻结 ，3.删除'")
	@PageTableTitle(value = "账户状态", isEnum = true)
	private ManagerStatus status = ManagerStatus.OK;

	@Column(columnDefinition = "varchar(100) COMMENT '保留字段'")
	private Integer version;

	/**
	 * 用户名
	 */
	@Transient
	private String userName;

	/**
	 * 部门Id
	 */
	@Transient
	private String deptId;

	public Account() {
		super();
	}

	public Account(String id, String deptId, String loginName, String userName) {
		this.id = id;
		this.deptId = deptId;
		this.loginName = loginName;
		this.userName = userName;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFacePwd() {
		return this.facePwd;
	}

	public void setFacePwd(String facePwd) {
		this.facePwd = facePwd;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public ManagerStatus getStatus() {
		return this.status;
	}

	public void setStatus(ManagerStatus status) {
		this.status = status;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Override
	public String toString() {
		return "Account{" + "id=" + getId() + ", loginName=" + loginName + ", password=" + password + ", salt=" + salt
				+ ", status=" + status + ", version=" + version + "}";
	}
}