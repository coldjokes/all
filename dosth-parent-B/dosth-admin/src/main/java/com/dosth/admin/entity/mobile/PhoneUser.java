package com.dosth.admin.entity.mobile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 电话用户信息
 * @author liweifeng
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "phoneUser")
public class PhoneUser extends BasePojo {

	@PageTableTitle(value = "用户姓名")
	@Column(columnDefinition = "varchar(100) COMMENT '用户姓名'")
	private String userName;

	@PageTableTitle(value = "员工号")
	@Column(columnDefinition = "varchar(20) COMMENT '员工号'")
	private String empNo;

	@PageTableTitle(value = "部门名称")
	@Column(columnDefinition = "varchar(50) COMMENT '部门名称码'")
	private String deptName;

	@PageTableTitle(value = "电话号码")
	@Column(columnDefinition = "varchar(30) COMMENT '电话号码'")
	private String phoneNo;

	@PageTableTitle(value = "注册密码")
	@Column(columnDefinition = "varchar(200) COMMENT '注册密码'")
	private String appPwd;

	public PhoneUser() {
	}

	public PhoneUser(String userName, String empNo, String deptName, String phoneNo, String appPwd) {
		this.userName = userName;
		this.empNo = empNo;
		this.deptName = deptName;
		this.phoneNo = phoneNo;
		this.appPwd = appPwd;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmpNo() {
		return this.empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPhoneNo() {
		return this.phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getAppPwd() {
		return this.appPwd;
	}

	public void setAppPwd(String appPwd) {
		this.appPwd = appPwd;
	}
}