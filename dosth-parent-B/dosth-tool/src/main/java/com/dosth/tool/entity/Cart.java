package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.db.entity.BasePojo;

/**
 * 
 * @description 购物车
 * @author liweifeng
 *
 */
@Entity
@SuppressWarnings("serial")
public class Cart extends BasePojo {

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;

	@Column(name = "CABINET_ID", columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String cabinetId;

	@Column(name = "MAT_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matId;
	@ManyToOne
	@JoinColumn(name = "MAT_ID", insertable = false, updatable = false)
	private MatEquInfo matInfo;

	@Column(name = "RECEIVE_TYPE", columnDefinition = "varchar(36) COMMENT '领取类型'")
	private String receiveType;

	@Column(name = "RECEIVE_INFO", columnDefinition = "varchar(36) COMMENT '领取类型'")
	private String receiveInfo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ADD_TIME", columnDefinition = "datetime COMMENT '添加时间'")
	private Date addTime;

	@Column(name = "NUM", columnDefinition = "int(11) COMMENT '数量'")
	private Integer num;

	public Cart() {
		setAddTime(new Date());
	}

	public Cart(String accountId, String matId, Integer num) {
		setAddTime(new Date());
		this.accountId = accountId;
		this.matId = matId;
		this.num = num;
	}

	public Cart(String accountId, String matId, String receiveType, String receiveInfo, Integer num, String cabinetId) {
		this.accountId = accountId;
		this.matId = matId;
		this.receiveType = receiveType;
		this.receiveInfo = receiveInfo;
		this.num = num;
		this.cabinetId = cabinetId;
		setAddTime(new Date());
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public MatEquInfo getMatInfo() {
		return matInfo;
	}

	public void setMatInfo(MatEquInfo matInfo) {
		this.matInfo = matInfo;
	}

	public String getMatId() {
		return matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getReceiveInfo() {
		return receiveInfo;
	}

	public void setReceiveInfo(String receiveInfo) {
		this.receiveInfo = receiveInfo;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}