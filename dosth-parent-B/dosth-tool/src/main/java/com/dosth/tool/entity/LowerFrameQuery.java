package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 下架查询
 * 
 * @author liweifeng
 *
 */
@Entity
@SuppressWarnings("serial")
public class LowerFrameQuery extends BasePojo {

	@PageTableTitle(value = "刀具柜名称")
	@Column(columnDefinition = "varchar(255) COMMENT '刀具柜名称'")
	private String equName;

	@Transient
	@PageTableTitle(value = "下架货位")
	private String position;

	@Column(name = "EQU_DETAIL_STA_ID", columnDefinition = "varchar(36) COMMENT '刀具柜货位ID'")
	private String equDetailStaId;
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn(name = "EQU_DETAIL_STA_ID", referencedColumnName = "ID", insertable = false, updatable = false)
	private EquDetailSta equDetailSta;

	@Column(name = "SUB_BOX_ID", columnDefinition = "varchar(36) COMMENT '暂存柜货位ID'")
	private String subBoxId;
	@ManyToOne
	@JoinColumn(name = "SUB_BOX_ID", insertable = false, updatable = false)
	private SubBox subBox;

	@PageTableTitle(value = "物料名称")
	@Column(columnDefinition = "varchar(255) COMMENT '物料名称'")
	private String matInfoName;

	@PageTableTitle(value = "物料编号")
	@Column(columnDefinition = "varchar(255) COMMENT '物料编号'")
	private String barCode;

	@PageTableTitle(value = "物料型号")
	@Column(columnDefinition = "varchar(255) COMMENT '物料型号'")
	private String spec;

	@PageTableTitle(value = "下架数量")
	@Column(columnDefinition = "int(11) COMMENT '下架数量'")
	private Integer lowFrameNum;

	@PageTableTitle(value = "单位")
	@Column(columnDefinition = "varchar(255) COMMENT '单位'")
	private String unit;

	@Column(columnDefinition = "varchar(255) COMMENT '使用人员帐号ID'")
	private String ownerAccountId;

	@PageTableTitle(value = "使用人员")
	@Column(columnDefinition = "varchar(255) COMMENT '使用人员'")
	private String ownerName;

	@Column(columnDefinition = "varchar(255) COMMENT '操作人员帐号ID'")
	private String userAccountId;

	@PageTableTitle(value = "操作人员")
	@Column(columnDefinition = "varchar(255) COMMENT '操作人员'")
	private String userName;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "操作时间")
	@Column(columnDefinition = "datetime COMMENT '操作时间'")
	private Date opDate;

	public LowerFrameQuery() {

	}

	public LowerFrameQuery(String equName, String equDetailStaId, String subBoxId, String matInfoName, String barCode,
			String spec, Integer lowFrameNum, String unit, String ownerAccountId, String ownerName,
			String userAccountId, String userName) {
		this.equName = equName;
		this.equDetailStaId = equDetailStaId;
		this.subBoxId = subBoxId;
		this.matInfoName = matInfoName;
		this.barCode = barCode;
		this.spec = spec;
		this.lowFrameNum = lowFrameNum;
		this.unit = unit;
		this.ownerAccountId = ownerAccountId;
		this.ownerName = ownerName;
		this.userAccountId = userAccountId;
		this.userName = userName;
		setOpDate(new Date());
	}

	public String getEquName() {
		return equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getEquDetailStaId() {
		return equDetailStaId;
	}

	public void setEquDetailStaId(String equDetailStaId) {
		this.equDetailStaId = equDetailStaId;
	}

	public EquDetailSta getEquDetailSta() {
		return equDetailSta;
	}

	public void setEquDetailSta(EquDetailSta equDetailSta) {
		this.equDetailSta = equDetailSta;
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

	public Integer getLowFrameNum() {
		return lowFrameNum;
	}

	public void setLowFrameNum(Integer lowFrameNum) {
		this.lowFrameNum = lowFrameNum;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getOwnerAccountId() {
		return ownerAccountId;
	}

	public void setOwnerAccountId(String ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getOpDate() {
		return opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

}
