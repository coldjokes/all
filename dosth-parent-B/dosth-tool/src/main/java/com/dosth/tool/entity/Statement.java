package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 对账单
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class Statement extends BasePojo {

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "物料信息", isForeign = true)
	private MatEquInfo matInfo;

	@Temporal(TemporalType.DATE)
	@PageTableTitle(value = "起始日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(columnDefinition = "datetime COMMENT '起始日期'")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@PageTableTitle(value = "截止日期")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(columnDefinition = "datetime COMMENT '截止日期'")
	private Date endDate;

	@PageTableTitle(value = "上期结余")
	@Column(columnDefinition = "int(11) COMMENT '上期结余'")
	private Integer balance;

	@PageTableTitle(value = "领料数量")
	@Column(columnDefinition = "int(11) COMMENT '领料数量'")
	private Integer outerNum;

	@PageTableTitle(value = "暂存柜结余")
	@Column(columnDefinition = "int(11) COMMENT '暂存柜结余'")
	private Integer tempNum;

	@PageTableTitle(value = "核对数量")
	@Column(columnDefinition = "int(11) COMMENT '核对数量'")
	private Integer inventoryNum;

	@PageTableTitle(value = "单价")
	@Column(columnDefinition = "float COMMENT '单价'")
	private Float price;

	@PageTableTitle(value = "金额")
	@Column(columnDefinition = "float COMMENT '金额'")
	private Float cost;

	@PageTableTitle(value = "是否核对", isEnum = true)
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '是否核对'")
	private YesOrNo isHD;

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "核对人", isForeign = true)
	private ViewUser account;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "核对日期")
	@Column(columnDefinition = "datetime COMMENT '核对日期'")
	private Date opDate;

	public Statement() {
	}

	public Statement(String matInfoId, Date startDate, Date endDate, Integer balance, Integer outerNum, Integer tempNum,
			Integer inventoryNum, Float price, Float cost) {
		this.matInfoId = matInfoId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.balance = balance;
		this.outerNum = outerNum;
		this.tempNum = tempNum;
		this.inventoryNum = inventoryNum;
		this.price = price;
		this.cost = cost;
		setOpDate(new Date());
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

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getBalance() {
		return this.balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getOuterNum() {
		return this.outerNum;
	}

	public void setOuterNum(Integer outerNum) {
		this.outerNum = outerNum;
	}

	public Integer getTempNum() {
		if (this.tempNum == null) {
			this.tempNum = 0;
		}
		return this.tempNum;
	}

	public void setTempNum(Integer tempNum) {
		this.tempNum = tempNum;
	}

	public Integer getInventoryNum() {
		return this.inventoryNum;
	}

	public void setInventoryNum(Integer inventoryNum) {
		this.inventoryNum = inventoryNum;
	}

	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getCost() {
		return this.cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public YesOrNo getIsHD() {
		if (this.isHD == null) {
			this.isHD = YesOrNo.NO;
		}
		return this.isHD;
	}

	public void setIsHD(YesOrNo isHD) {
		this.isHD = isHD;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ViewUser getAccount() {
		return this.account;
	}

	public void setAccount(ViewUser account) {
		this.account = account;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
}