package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.IsReturnBack;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.AuditStatus;
import com.dosth.tool.common.state.BackWay;
import com.dosth.tool.common.state.VerifyMode;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 物料归还
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class MatReturnBack extends BasePojo {

	@PageTableTitle(value = "刀具柜名称")
	@Column(columnDefinition = "varchar(36) COMMENT '刀具柜名称'")
	private String equSettingName;

	@PageTableTitle(value = "归还流水号")
	@Column(columnDefinition = "int(7) COMMENT '归还流水号'")
	private Integer returnBackNo;

	@Column(name = "MAT_USE_BILL_ID", columnDefinition = "varchar(36) COMMENT '领用ID'")
	private String matUseBillId;
	@OneToOne
	@PageTableTitle(value = "物料名称", isForeign = true)
	@JoinColumn(name = "MAT_USE_BILL_ID", insertable = false, updatable = false)
	private MatUseBill matUseBill;

	@Transient
	@PageTableTitle(value = "编号")
	private String knifeId; // 刀具编号
	
	@Transient
	@PageTableTitle(value = "归还人员")
	private String userName;

	@Transient
	@PageTableTitle(value = "型号", isVisible = false)
	private String knifeSpec;

	@PageTableTitle(value = "归还详情")
	@Column(columnDefinition = "varchar(255) COMMENT '归还详情D'")
	private String returnDetailInfo;

	@Transient
	@PageTableTitle(value = "领用类型", isVisible = false)
	private String receiveType;

	@Transient
	@PageTableTitle("领用途径")
	private String receiveInfo;

	@Transient
	@PageTableTitle("使用寿命")
	private Integer matLife;

	@PageTableTitle("制造产量")
	@Column(columnDefinition = "int(11) COMMENT '制造产量'")
	private Integer realLife;

	@Transient
	@PageTableTitle("剩余寿命")
	private Integer surplusLife;

	@PageTableTitle("产品流水号")
	@Column(columnDefinition = "varchar(50) COMMENT '产品流水号'")
	private String serialNum;

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(50) COMMENT '归还人帐号ID'")
	private String accountId;
	@ManyToOne
	@PageTableTitle(value = "归还人员", isForeign = true)
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	private ViewUser user;

	@PageTableTitle(value = "归还时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '归还时间'")
	private Date opDate;

	@PageTableTitle(value = "审核人员")
	@Column(columnDefinition = "varchar(255) COMMENT '审核人员间'")
	private String confirmUser;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '审核方式'")
	@PageTableTitle(value = "审核方式", isForeign = true)
	private VerifyMode confirmMode;

	@PageTableTitle(value = "审核时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '审核时间'")
	private Date confirmDate;

	@Transient
	@PageTableTitle(value = "应还数量")
	private Integer packageNum;

	@PageTableTitle(value = "实际数量")
	@Column(columnDefinition = "int(11) COMMENT '实际数量'")
	private Integer num;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '审核状态'")
	@PageTableTitle(value = "审核状态")
	private AuditStatus auditStatus;

	@PageTableTitle(value = "备注")
	@Column(columnDefinition = "varchar(255) COMMENT '备注'")
	private String remark;

	@PageTableTitle(value = "归还码", isVisible = false)
	@Column(columnDefinition = "varchar(255) COMMENT '归还码'")
	private String barCode;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '归还方式'")
	@PageTableTitle(value = "归还方式", isVisible = false)
	private BackWay backWay;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '是否归还'")
	@PageTableTitle(value = "是否归还", isVisible = false)
	private IsReturnBack isReturnBack;

	@PageTableTitle(value = "以旧换新", isVisible = false)
	@Column(columnDefinition = "varchar(255) COMMENT '以旧换新'")
	private String isGetNewOne;

	@PageTableTitle(value = "归还类型", isVisible = false)
	@Column(columnDefinition = "varchar(255) COMMENT '归还类型'")
	private String returnBackType;

	public MatReturnBack() {
		setOpDate(new Date());
	}

	public MatReturnBack(int returnBackNo, String equSettingName, String barCode, String matUseBillId, String accountId,
			AuditStatus auditStatus, IsReturnBack isReturnBack, BackWay backWay, int num, String isGetNewOne) {
		this.returnBackNo = returnBackNo;
		this.equSettingName = equSettingName;
		this.barCode = barCode;
		this.matUseBillId = matUseBillId;
		this.accountId = accountId;
		this.auditStatus = auditStatus;
		this.isReturnBack = isReturnBack;
		this.backWay = backWay;
		this.num = num;
		this.isGetNewOne = isGetNewOne;
		setOpDate(new Date());
	}

	public String getEquSettingName() {
		return equSettingName;
	}

	public void setEquSettingName(String equSettingName) {
		this.equSettingName = equSettingName;
	}

	public Integer getReturnBackNo() {
		return returnBackNo;
	}

	public void setReturnBackNo(Integer returnBackNo) {
		this.returnBackNo = returnBackNo;
	}

	public String getMatUseBillId() {
		return this.matUseBillId;
	}

	public void setMatUseBillId(String matUseBillId) {
		this.matUseBillId = matUseBillId;
	}

	public MatUseBill getMatUseBill() {
		return this.matUseBill;
	}

	public void setMatUseBill(MatUseBill matUseBill) {
		this.matUseBill = matUseBill;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public ViewUser getUser() {
		return this.user;
	}

	public void setUser(ViewUser user) {
		this.user = user;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

	public String getKnifeId() {
		return knifeId;
	}

	public void setKnifeId(String knifeId) {
		this.knifeId = knifeId;
	}

	public String getKnifeSpec() {
		return knifeSpec;
	}

	public void setKnifeSpec(String knifeSpec) {
		this.knifeSpec = knifeSpec;
	}

	public String getReturnDetailInfo() {
		return returnDetailInfo;
	}

	public void setReturnDetailInfo(String returnDetailInfo) {
		this.returnDetailInfo = returnDetailInfo;
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

	public Integer getMatLife() {
		return this.matLife;
	}

	public void setMatLife(Integer matLife) {
		this.matLife = matLife;
	}

	public Integer getRealLife() {
		return this.realLife;
	}

	public void setRealLife(Integer realLife) {
		this.realLife = realLife;
	}

	public Integer getSurplusLife() {
		return this.surplusLife;
	}

	public void setSurplusLife(Integer surplusLife) {
		this.surplusLife = surplusLife;
	}

	public String getSerialNum() {
		return this.serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public VerifyMode getConfirmMode() {
		return confirmMode;
	}

	public void setConfirmMode(VerifyMode confirmMode) {
		this.confirmMode = confirmMode;
	}

	public String getConfirmUser() {
		return confirmUser;
	}

	public void setConfirmUser(String confirmUser) {
		this.confirmUser = confirmUser;
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Integer getPackageNum() {
		return packageNum;
	}

	public void setPackageNum(Integer packageNum) {
		this.packageNum = packageNum;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public AuditStatus getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(AuditStatus auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public IsReturnBack getIsReturnBack() {
		return isReturnBack;
	}

	public void setIsReturnBack(IsReturnBack isReturnBack) {
		this.isReturnBack = isReturnBack;
	}

	public String getIsGetNewOne() {
		return isGetNewOne;
	}

	public void setIsGetNewOne(String isGetNewOne) {
		this.isGetNewOne = isGetNewOne;
	}

	public String getReturnBackType() {
		return returnBackType;
	}

	public void setReturnBackType(String returnBackType) {
		this.returnBackType = returnBackType;
	}

	public BackWay getBackWay() {
		if (backWay == null) {
			backWay = BackWay.SINGLE;
		}
		return backWay;
	}

	public void setBackWay(BackWay backWay) {
		this.backWay = backWay;
	}
}