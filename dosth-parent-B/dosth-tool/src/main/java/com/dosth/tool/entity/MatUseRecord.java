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
import com.dosth.common.db.entity.BasePojo;
import com.dosth.common.util.DateUtil;
import com.dosth.tool.common.state.ReceiveType;

/**
 * @description 物料领用记录
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class MatUseRecord extends BasePojo {

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;

	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	private MatEquInfo matInfo;

	@PageTableTitle(value = "物料名称")
	@Column(columnDefinition = "varchar(255) COMMENT '物料名称'")
	private String matInfoName;

	@PageTableTitle(value = "物料编号")
	@Column(columnDefinition = "varchar(255) COMMENT '物料编号'")
	private String barCode;

	@PageTableTitle("物料型号")
	@Column(columnDefinition = "varchar(255) COMMENT '物料型号'")
	private String spec;

	@PageTableTitle("品牌（供应商）")
	@Column(columnDefinition = "varchar(255) COMMENT '品牌（供应商）'")
	private String brand;

	@PageTableTitle(value = "领取来源")
	@Column(columnDefinition = "varchar(255) COMMENT '领取来源'")
	private String borrowOrigin;

	@PageTableTitle("领取数量")
	@Column(columnDefinition = "int(11) COMMENT '领取数量'")
	private Integer borrowNum;

	@PageTableTitle("实领数量")
	@Column(columnDefinition = "int(11) COMMENT '实领数量'")
	private Integer realNum;

	@PageTableTitle("领取单位")
	@Column(columnDefinition = "varchar(255) COMMENT '领取单位'")
	private String borrowType;

	@PageTableTitle("包装数量")
	@Column(columnDefinition = "int(11) COMMENT '包装数量'")
	private Integer packNum;

	@PageTableTitle("包装单位")
	@Column(columnDefinition = "varchar(255) COMMENT '包装单位'")
	private String packUnit;

	@Column(name = "RECEIVE_INFO", columnDefinition = "varchar(36) COMMENT '领取分类ID'")
	private String receiveInfo;

	@ManyToOne
	@JoinColumn(name = "RECEIVE_INFO", insertable = false, updatable = false)
	private MatCategoryTree tree;

	@Enumerated(EnumType.STRING)
	@PageTableTitle(value = "类型", isVisible = false)
	@Column(columnDefinition = "varchar(255) COMMENT '类型'")
	private ReceiveType receiveType;

	@Transient
	@PageTableTitle(value = "领用类型1")
	private String receiveInfo1;
	@Transient
	@PageTableTitle(value = "领用类型2")
	private String receiveInfo2;
	@Transient
	@PageTableTitle(value = "领用类型3")
	private String receiveInfo3;

	@PageTableTitle(value = "单价（元）")
	@Column(columnDefinition = "float COMMENT '单价'")
	private Float price;

	@PageTableTitle(value = "金额（元）")
	@Column(columnDefinition = "float COMMENT '金额'")
	private Float money;

	@PageTableTitle("使用寿命")
	@Column(columnDefinition = "int(11) COMMENT '使用寿命'")
	private Integer useLife;

	// 领用人员帐号id
	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '领用人员帐号ID'")
	private String accountId;

	// 部门名称
	@PageTableTitle(value = "部门")
	@Column(columnDefinition = "varchar(255) COMMENT '部门名称'")
	private String deptName;

	@Column(name = "DEPT_ID", columnDefinition = "varchar(36) COMMENT '部门ID'")
	private String deptId;

	@PageTableTitle("领用人员")
	@Column(columnDefinition = "varchar(255) COMMENT '领用人员'")
	private String userName;

	@PageTableTitle("已归还数量")
	@Column(columnDefinition = "int(11) COMMENT '已归还数量'")
	private Integer returnBackNum;

	@PageTableTitle("是否归还")
	@Column(columnDefinition = "varchar(255) COMMENT '是否归还'")
	private String isReturnBack;

	@PageTableTitle("制造产量")
	@Column(columnDefinition = "int(11) COMMENT '制造产量'")
	private Integer realLife;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle("领用时间")
	@Column(columnDefinition = "datetime COMMENT '领用时间'")
	private Date opDate;

	public MatUseRecord() {
	}

	public MatUseRecord(String matInfoId, String borrowOrigin, String matInfoName, String barCode, String spec,
			String brand, Integer borrowNum, String borrowType, Integer packNum, String packUnit, String receiveInfo,
			ReceiveType receiveType, Float price, Float money, String accountId, String userName, String deptName,
			Integer returnBackNum, String isReturnBack) {
		this.matInfoId = matInfoId;
		this.borrowOrigin = borrowOrigin;
		this.matInfoName = matInfoName;
		this.barCode = barCode;
		this.spec = spec;
		this.brand = brand;
		this.borrowNum = borrowNum;
		this.borrowType = borrowType;
		this.packNum = packNum;
		this.packUnit = packUnit;
		this.receiveInfo = receiveInfo;
		this.receiveType = receiveType;
		this.price = price;
		this.money = money;
		this.accountId = accountId;
		this.userName = userName;
		this.deptName = deptName;
		this.returnBackNum = returnBackNum;
		this.isReturnBack = isReturnBack;
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

	public String getMatInfoName() {
		if (this.matInfoName == null) {
			this.matInfoName = "";
		}
		return this.matInfoName;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public void setMatInfoName(String matInfoName) {
		this.matInfoName = matInfoName;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBorrowOrigin() {
		return borrowOrigin;
	}

	public void setBorrowOrigin(String borrowOrigin) {
		this.borrowOrigin = borrowOrigin;
	}

	public Integer getBorrowNum() {
		return this.borrowNum;
	}

	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
	}

	public String getBorrowType() {
		return this.borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	public Integer getPackNum() {
		return this.packNum;
	}

	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}

	public String getPackUnit() {
		return this.packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public String getReceiveInfo() {
		return this.receiveInfo;
	}

	public void setReceiveInfo(String receiveInfo) {
		this.receiveInfo = receiveInfo;
	}

	public MatCategoryTree getTree() {
		return this.tree;
	}

	public void setTree(MatCategoryTree tree) {
		this.tree = tree;
	}

	public String getReceiveInfo1() {
		return this.receiveInfo1;
	}

	public void setReceiveInfo1(String receiveInfo1) {
		this.receiveInfo1 = receiveInfo1;
	}

	public String getReceiveInfo2() {
		return this.receiveInfo2;
	}

	public void setReceiveInfo2(String receiveInfo2) {
		this.receiveInfo2 = receiveInfo2;
	}

	public String getReceiveInfo3() {
		return this.receiveInfo3;
	}

	public void setReceiveInfo3(String receiveInfo3) {
		this.receiveInfo3 = receiveInfo3;
	}

	public ReceiveType getReceiveType() {
		return this.receiveType;
	}

	public void setReceiveType(ReceiveType receiveType) {
		this.receiveType = receiveType;
	}

	public Float getPrice() {
		return this.price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getMoney() {
		if (this.money == null) {
			this.money = 0F;
		}
		return this.money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public Integer getUseLife() {
		if (this.useLife == null) {
			this.useLife = 0;
		}
		return this.useLife;
	}

	public void setUseLife(Integer useLife) {
		this.useLife = useLife;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getRealNum() {
		if (this.realNum == null) {
			this.realNum = 0;
		}
		return this.realNum;
	}

	public void setRealNum(Integer realNum) {
		this.realNum = realNum;
	}

	public Integer getReturnBackNum() {
		return this.returnBackNum;
	}

	public void setReturnBackNum(Integer returnBackNum) {
		this.returnBackNum = returnBackNum;
	}

	public String getIsReturnBack() {
		return this.isReturnBack;
	}

	public void setIsReturnBack(String isReturnBack) {
		this.isReturnBack = isReturnBack;
	}

	public Integer getRealLife() {
		if (this.realLife == null) {
			this.realLife = 0;
		}
		return this.realLife;
	}

	public void setRealLife(Integer realLife) {
		this.realLife = realLife;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

	public String getSimpleDay() {
		return DateUtil.getDay(this.opDate);
	}

	public String getSimpleYear() {
		return DateUtil.getYear(this.opDate);
	}

	public String getSimpleMonth() {
		return DateUtil.formatDate(this.opDate, "yyyy-MM");
	}
}