package com.dosth.tool.entity.mobile;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.vo.ViewUser;

/**
 * 
 * @description 手机预约
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class PhoneOrder extends BasePojo {

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "预约人员", isForeign = true)
	private ViewUser user;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "预约时间")
	@Column(columnDefinition = "datetime COMMENT '预约时间'")
	private Date orderCreatedTime;

	@Column(columnDefinition = "longtext COMMENT '订单二维码文本'")
	private String orderText;

	@Column(columnDefinition = "longtext COMMENT '预约二维码'")
	private String orderCode;

	public PhoneOrder() {
		setOrderCreatedTime(new Date());
	}

	public PhoneOrder(String orderText, String accountId, ViewUser user, Date orderCreatedTime) {
		this.orderText = orderText;
		this.accountId = accountId;
		this.user = user;
		this.orderCreatedTime = orderCreatedTime;
	}

	public PhoneOrder(String accountId) {
		this.accountId = accountId;
		setOrderCreatedTime(new Date());
	}

	public String getOrderText() {
		return this.orderText;
	}

	public void setOrderText(String orderText) {
		this.orderText = orderText;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ViewUser getUser() {
		return user;
	}

	public void setUser(ViewUser user) {
		this.user = user;
	}

	public Date getOrderCreatedTime() {
		return orderCreatedTime;
	}

	public void setOrderCreatedTime(Date orderCreatedTime) {
		this.orderCreatedTime = orderCreatedTime;
	}

	public String getOrderCode() {
		return this.orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
}