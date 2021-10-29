package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.HardwareInfoType;

/**
 * @description 硬件日志
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class HardwareLog extends BasePojo {

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '硬件信息类型'")
	private HardwareInfoType infoType;

	@Column(columnDefinition = "varchar(50) COMMENT '位置'")
	private String position;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(5) COMMENT '执行结果'")
	private YesOrNo isSucc;

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '操作人帐号ID'")
	private String accountId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '操作时间'")
	private Date opDate;

	public HardwareLog() {
	}

	public HardwareLog(HardwareInfoType infoType, String position, String accountId) {
		setOpDate(new Date());
		this.infoType = infoType;
		this.position = position;
		this.accountId = accountId;
	}

	public HardwareInfoType getInfoType() {
		return this.infoType;
	}

	public void setInfoType(HardwareInfoType infoType) {
		this.infoType = infoType;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public YesOrNo getIsSucc() {
		return this.isSucc;
	}

	public void setIsSucc(YesOrNo isSucc) {
		this.isSucc = isSucc;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
}