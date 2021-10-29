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
import com.dosth.tool.common.state.InventoryStatus;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 库存盘点
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class Inventory extends BasePojo {

	@Transient
	@PageTableTitle(value = "刀具柜名称")
	private String equName;
	
	@Transient
	@PageTableTitle(value = "物料名称")
	private String matEquName;
	
	@Transient
	@PageTableTitle(value = "暂存柜名称")
	private String equSettingName;
	
	@Column(name = "EQU_DETAIL_STA_ID", columnDefinition = "varchar(36) COMMENT '货位ID'")
	private String equDetailStaId;
	@ManyToOne
	@JoinColumn(name = "EQU_DETAIL_STA_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "主柜信息", isForeign = true)
	private EquDetailSta equDetailSta;

	@Column(name = "SUB_BOX_ID", columnDefinition = "varchar(36) COMMENT '暂存柜ID'")
	private String subBoxId;
	@ManyToOne
	@JoinColumn(name = "SUB_BOX_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "暂存柜信息", isForeign = true)
	private SubBox subBox;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "物料信息", isForeign = true)
	private MatEquInfo matInfo;

	@PageTableTitle(value = "库存数量")
	@Column(columnDefinition = "int(11) COMMENT '库存数量'")
	private Integer storageNum;

	@PageTableTitle(value = "实盘数量")
	@Column(columnDefinition = "int(11) COMMENT '实盘数量'")
	private Integer inventoryNum;

	@Transient
	@Enumerated(EnumType.STRING)
	@PageTableTitle(value = "盘点状态", isEnum = true)
	private InventoryStatus inventoryStatus;

	@Column(name = "OWNER_ID", columnDefinition = "varchar(36) COMMENT '使用人员'")
	private String ownerId;
	@ManyToOne
	@JoinColumn(name = "OWNER_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "使用人员", isForeign = true)
	private ViewUser owner;

	@Transient
	@PageTableTitle(value = "使用人员")
	private String ownerName;

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "操作人员", isForeign = true)
	private ViewUser user;

	@Transient
	@PageTableTitle(value = "操作人员")
	private String userName;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "操作时间")
	@Column(columnDefinition = "datetime COMMENT '操作时间'")
	private Date opDate;

	public Inventory() {
	}

	public Inventory(String equDetailStaId, String subBoxId, String matInfoId, Integer storageNum, Integer inventoryNum,
			String ownerId, String accountId) {
		this.equDetailStaId = equDetailStaId;
		this.subBoxId = subBoxId;
		this.matInfoId = matInfoId;
		this.storageNum = storageNum;
		this.inventoryNum = inventoryNum;
		this.ownerId = ownerId;
		this.accountId = accountId;
		setOpDate(new Date());
	}

	public String getEquName() {
		return equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	public String getMatEquName() {
		return matEquName;
	}
	
	public void setMatEquName(String matEquName) {
		this.matEquName=matEquName;
	}
	public String getEquSettingName() {
		return equSettingName;
	}
	
	public void setEquSettingName(String equSettingName) {
		this.equSettingName=equSettingName;
	}
	
	public String getEquDetailStaId() {
		return this.equDetailStaId;
	}

	public void setEquDetailStaId(String equDetailStaId) {
		this.equDetailStaId = equDetailStaId;
	}

	public EquDetailSta getEquDetailSta() {
		return this.equDetailSta;
	}

	public void setEquDetailSta(EquDetailSta equDetailSta) {
		this.equDetailSta = equDetailSta;
	}

	public String getSubBoxId() {
		return this.subBoxId;
	}

	public void setSubBoxId(String subBoxId) {
		this.subBoxId = subBoxId;
	}

	public SubBox getSubBox() {
		return this.subBox;
	}

	public void setSubBox(SubBox subBox) {
		this.subBox = subBox;
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

	public Integer getStorageNum() {
		return this.storageNum;
	}

	public void setStorageNum(Integer storageNum) {
		this.storageNum = storageNum;
	}

	public Integer getInventoryNum() {
		return this.inventoryNum;
	}

	public void setInventoryNum(Integer inventoryNum) {
		this.inventoryNum = inventoryNum;
	}

	public InventoryStatus getInventoryStatus() {
		if (this.inventoryStatus == null) {
			this.inventoryStatus = InventoryStatus.PING;
		}
		return this.inventoryStatus;
	}

	public void setInventoryStatus(InventoryStatus inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public String getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public ViewUser getOwner() {
		return this.owner;
	}

	public void setOwner(ViewUser owner) {
		this.owner = owner;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}