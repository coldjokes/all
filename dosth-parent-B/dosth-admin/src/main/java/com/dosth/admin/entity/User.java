package com.dosth.admin.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 用户表
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class User extends BasePojo {

	@Column(name = "accountId", columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;
	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "accountId", insertable = false, updatable = false)
	@PageTableTitle(value = "账户", isForeign = true)
	private Account account;

	@Column(columnDefinition = "varchar(50) COMMENT '姓名'")
	@PageTableTitle("姓名")
	private String name;

	@Transient
	private String loginName;

	@Transient
	private String password;

	@Lob
	@Column(columnDefinition = "longblob COMMENT '人臉特征'")
	private byte[] faceFeature;

	@Transient
	@PageTableTitle(value = "部门名称")
	private String deptName;

	@Column(columnDefinition = "varchar(50) COMMENT '部门ID'")
	private String deptId;
	@ManyToOne
	@JoinColumn(name = "deptId", insertable = false, updatable = false)
	@PageTableTitle(value = "部门", isForeign = true)
	private Dept dept;

	@Column(columnDefinition = "varchar(50) COMMENT 'IC卡号'")
	@PageTableTitle("IC卡号")
	private String icCard;

	@Column(columnDefinition = "varchar(50) COMMENT '邮箱'")
	@PageTableTitle(value = "邮箱")
	private String email;

	@Column(columnDefinition = "varchar(255) COMMENT '头像'")
	@PageTableTitle(value = "头像", isVisible = false)
	private String avatar;

	@Transient
	@PageTableTitle(value = "暂存柜数量")
	private String extraBoxNum;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle("创建时间")
	@Column(columnDefinition = "datetime COMMENT '创建时间'")
	private Date createtime = new Date();

	@Column(columnDefinition = "int(36) COMMENT '限额总数'")
	private Integer limitSumNum;

	@Column(columnDefinition = "int(36) COMMENT '未归还限额'")
	private Integer notReturnLimitNum;

	@Column(columnDefinition = "varchar(50) COMMENT '限额开始时间'")
	private String startTime;

	@Column(columnDefinition = "varchar(50) COMMENT '限额结束时间'")
	private String endTime;

	@Transient
	private Boolean isAdminRole;

	public User() {
		super();
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginName() {
		return loginName;
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

	public byte[] getFaceFeature() {
		return faceFeature;
	}

	public void setFaceFeature(byte[] faceFeature) {
		this.faceFeature = faceFeature;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Dept getDept() {
		return this.dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public String getIcCard() {
		return icCard;
	}

	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getExtraBoxNum() {
		return extraBoxNum;
	}

	public void setExtraBoxNum(String extraBoxNum) {
		this.extraBoxNum = extraBoxNum;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getLimitSumNum() {
		return limitSumNum;
	}

	public void setLimitSumNum(Integer limitSumNum) {
		this.limitSumNum = limitSumNum;
	}

	public Integer getNotReturnLimitNum() {
		return notReturnLimitNum;
	}

	public void setNotReturnLimitNum(Integer notReturnLimitNum) {
		this.notReturnLimitNum = notReturnLimitNum;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Boolean getIsAdminRole() {
		if (this.isAdminRole == null) {
			this.isAdminRole = false;
		}
		return this.isAdminRole;
	}

	public void setIsAdminRole(Boolean isAdminRole) {
		this.isAdminRole = isAdminRole;
	}
}