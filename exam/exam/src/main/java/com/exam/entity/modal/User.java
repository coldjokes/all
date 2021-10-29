package com.exam.entity.modal;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 *  用户表
 * 
 * @author Yifeng Wang  
 */
public class User extends BaseModel<User>{

	private static final long serialVersionUID = 1L;

	private String username; //用户名
	private String password; //密码
	private String email; //邮箱
	private String fullname; //姓名
	private Integer status; //账户状态 1:启用 2:禁用
	
	private Date updateTime; //更新时间
	private Date deleteTime; //删除时间

	@TableField(exist = false)
	private String oldPassword; //旧密码

	public User() {
		super();
	}
	public User(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}

