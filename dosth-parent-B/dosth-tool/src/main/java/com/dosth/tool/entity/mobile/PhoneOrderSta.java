package com.dosth.tool.entity.mobile;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 
 * @description
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "phoneOrderSta")
public class PhoneOrderSta extends BasePojo {

	@Column(name = "PHONE_ORDER_ID", columnDefinition = "varchar(36) COMMENT '订单ID'")
	private String phoneOrderId;
	@ManyToOne
	@JoinColumn(name = "PHONE_ORDER_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "订单", isForeign = true)
	private PhoneOrder phoneOrder;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "取料时间")
	@Column(columnDefinition = "datetime COMMENT '取料时间'")
	private Date opDate;

	public PhoneOrderSta() {
		setOpDate(new Date());
	}

	public PhoneOrderSta(String phoneOrderId) {
		setOpDate(new Date());
		this.phoneOrderId = phoneOrderId;
	}

	public PhoneOrderSta(String phoneOrderId, PhoneOrder phoneOrder, Date opDate) {
		super();
		this.phoneOrderId = phoneOrderId;
		this.phoneOrder = phoneOrder;
		this.opDate = opDate;
	}

	public String getPhoneOrderId() {
		return phoneOrderId;
	}

	public void setPhoneOrderId(String phoneOrderId) {
		this.phoneOrderId = phoneOrderId;
	}

	public PhoneOrder getPhoneOrder() {
		return phoneOrder;
	}

	public void setPhoneOrder(PhoneOrder phoneOrder) {
		this.phoneOrder = phoneOrder;
	}

	public Date getOpDate() {
		return opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

}