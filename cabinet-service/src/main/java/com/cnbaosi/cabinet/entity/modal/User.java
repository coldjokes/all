package com.cnbaosi.cabinet.entity.modal;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldStrategy;

/**
 *  用户表
 * 
 * @author Yifeng Wang  
 */
public class User extends BaseModel<User>{

	private static final long serialVersionUID = 1L;

	private String username; //用户名
	private String password; //密码
	private Integer role; //角色
	private String icCard; //ic卡
	private String email; //邮箱
	private String fullname; //姓名
	private Integer source; //数据来源
	private Integer status; //账户状态 1:启用 2:禁用
	
	@TableField(strategy=FieldStrategy.IGNORED)
	private byte[] faceFeature; //人脸特征
	
	private Date updateTime; //更新时间
	private Date deleteTime; //删除时间

	@TableField(exist = false)
	private String oldPassword; //旧密码

//	private String deptId; // 部门ID

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

//	public String getDeptId() {
//		return deptId;
//	}
//
//	public void setDeptId(String deptId) {
//		this.deptId = deptId;
//	}

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
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public String getIcCard() {
		return icCard;
	}
	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
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
	public byte[] getFaceFeature() {
		return faceFeature;
	}
	public void setFaceFeature(byte[] faceFeature) {
		this.faceFeature = faceFeature;
	}
}

