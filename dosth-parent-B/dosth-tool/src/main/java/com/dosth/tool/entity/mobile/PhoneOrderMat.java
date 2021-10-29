package com.dosth.tool.entity.mobile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.entity.MatEquInfo;

/**
 * 
 * @description 手机订单物料信息
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class PhoneOrderMat extends BasePojo {

	@Column(name = "PHONE_ORDER_ID", columnDefinition = "varchar(36) COMMENT '订单ID'")
	private String phoneOrderId;
	@ManyToOne
	@JoinColumn(name = "PHONE_ORDER_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "订单编号", isForeign = true)
	private PhoneOrder phoneOrder;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "物料名称", isForeign = true)
	private MatEquInfo matInfo;

	@PageTableTitle(value = "预约数量")
	@Column(columnDefinition = "int(11) COMMENT '预约数量'")
	private int num;

	@Column(columnDefinition = "longtext COMMENT '订单二维码文本'")
	private String orderMatText;

	@Column(columnDefinition = "longtext COMMENT '预约二维码'")
	private String orderMatCode;

	public PhoneOrderMat() {
		super();
	}

	public PhoneOrderMat(String phoneOrderId, String matInfoId, int num) {
		this.phoneOrderId = phoneOrderId;
		this.matInfoId = matInfoId;
		this.num = num;
	}

	public String getPhoneOrderId() {
		return this.phoneOrderId;
	}

	public void setPhoneOrderId(String phoneOrderId) {
		this.phoneOrderId = phoneOrderId;
	}

	public PhoneOrder getPhoneOrder() {
		return this.phoneOrder;
	}

	public void setPhoneOrder(PhoneOrder phoneOrder) {
		this.phoneOrder = phoneOrder;
	}

	public String getMatInfoId() {
		return this.matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public MatEquInfo getMatInfo() {
		return this.matInfo;
	}

	public void setMatInfo(MatEquInfo matInfo) {
		this.matInfo = matInfo;
	}

	public int getNum() {
		return this.num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getOrderMatText() {
		return this.orderMatText;
	}

	public void setOrderMatText(String orderMatText) {
		this.orderMatText = orderMatText;
	}

	public String getOrderMatCode() {
		return this.orderMatCode;
	}

	public void setOrderMatCode(String orderMatCode) {
		this.orderMatCode = orderMatCode;
	}
}