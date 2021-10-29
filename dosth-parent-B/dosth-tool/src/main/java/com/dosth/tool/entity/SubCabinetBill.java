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
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 副柜流水
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class SubCabinetBill extends BasePojo {

	@Column(name = "SUB_BOX_ID", columnDefinition = "varchar(36) COMMENT '暂存柜货位ID'")
	private String subBoxId;
	@ManyToOne
	@JoinColumn(name = "SUB_BOX_ID", insertable = false, updatable = false)
	private SubBox subBox;

	@PageTableTitle(value = "暂存柜名称")
	@Column(columnDefinition = "varchar(255) COMMENT '暂存柜名称'")
	private String subBoxName;

	@Transient
	@PageTableTitle(value = "货位")
	private String position;

	@PageTableTitle(value = "物料名称")
	@Column(columnDefinition = "varchar(255) COMMENT '物料名称'")
	private String matInfoName;

	@PageTableTitle(value = "物料编号")
	@Column(columnDefinition = "varchar(255) COMMENT '物料编号'")
	private String barCode;

	@PageTableTitle(value = "物料型号")
	@Column(columnDefinition = "varchar(255) COMMENT '物料型号'")
	private String spec;

	@PageTableTitle(value = "数量")
	@Column(columnDefinition = "int(11) COMMENT '数量'")
	private Integer num;

	@PageTableTitle(value = "单位")
	@Column(columnDefinition = "varchar(255) COMMENT '单位'")
	private String borrowType;

	@PageTableTitle(value = "单价（元）")
	@Column(columnDefinition = "float COMMENT '单价'")
	private Float price;

	@PageTableTitle(value = "金额（元）")
	@Column(columnDefinition = "float COMMENT '金额'")
	private Float money;

	@Transient
	@PageTableTitle(value = "金额（元）")
	@Column(columnDefinition = "float COMMENT '金额'")
	private Float tmpMoney;

	@Column(columnDefinition = "varchar(255) COMMENT '领用人员帐号ID'")
	private String accountId;

	@PageTableTitle(value = "领用人员")
	@Column(columnDefinition = "varchar(255) COMMENT '领用人员'")
	private String userName;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '取出/暂存'")
	@PageTableTitle(value = "取出/暂存", isForeign = true)
	private YesOrNo inOrOut;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "操作时间")
	@Column(columnDefinition = "datetime COMMENT '操作时间'")
	private Date opDate;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	private MatEquInfo matInfo;

	public SubCabinetBill() {
		super();
	}

	public SubCabinetBill(String subBoxId, String subBoxName, String matInfoName, String barCode, String spec,
			Integer num, String borrowType, Float price, Float money, String accountId, String userName,
			YesOrNo inOrOut, String matInfoId) {
		super();
		this.subBoxId = subBoxId;
		this.subBoxName = subBoxName;
		this.matInfoName = matInfoName;
		this.barCode = barCode;
		this.spec = spec;
		this.num = num;
		this.borrowType = borrowType;
		this.price = price;
		this.money = money;
		this.accountId = accountId;
		this.userName = userName;
		this.inOrOut = inOrOut;
		this.matInfoId = matInfoId;
		setOpDate(new Date());
	}

	public String getSubBoxName() {
		return subBoxName;
	}

	public void setSubBoxName(String subBoxName) {
		this.subBoxName = subBoxName;
	}

	public String getSubBoxId() {
		return subBoxId;
	}

	public void setSubBoxId(String subBoxId) {
		this.subBoxId = subBoxId;
	}

	public SubBox getSubBox() {
		return subBox;
	}

	public void setSubBox(SubBox subBox) {
		this.subBox = subBox;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMatInfoName() {
		return matInfoName;
	}

	public void setMatInfoName(String matInfoName) {
		this.matInfoName = matInfoName;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Float getTmpMoney() {
		return tmpMoney;
	}

	public void setTmpMoney(Float tmpMoney) {
		this.tmpMoney = tmpMoney;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public YesOrNo getInOrOut() {
		return inOrOut;
	}

	public void setInOrOut(YesOrNo inOrOut) {
		this.inOrOut = inOrOut;
	}

	public Date getOpDate() {
		return opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

	public String getMatInfoId() {
		return matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public MatEquInfo getMatInfo() {
		return matInfo;
	}

	public void setMatInfo(MatEquInfo matInfo) {
		this.matInfo = matInfo;
	}

}