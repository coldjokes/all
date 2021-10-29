package com.dosth.tool.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.enums.CabinetType;
import com.dosth.tool.entity.enums.EnumDoor;
import com.dosth.tool.vo.ViewUser;

/**
 * 柜体管理
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class EquSetting extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '名称'")
	@PageTableTitle(value = "名称")
	private String equSettingName;

	@Column(columnDefinition = "varchar(36) COMMENT '序列号'")
	@PageTableTitle(value = "序列号")
	private String serialNo;

	@PageTableTitle(value = "层数")
	@Column(columnDefinition = "int(11) COMMENT '层数'")
	private Integer rowNum;

	@PageTableTitle(value = "列数")
	@Column(columnDefinition = "int(11) COMMENT '列数'")
	private Integer colNum;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(36) COMMENT '类型'")
	@PageTableTitle(value = "类型", isEnum = true)
	private CabinetType cabinetType;

	@Column(name = "EQU_SETTING_PARENT_ID", columnDefinition = "varchar(36) COMMENT '上级刀具柜ID'")
	private String equSettingParentId;
	@ManyToOne
	@JoinColumn(name = "EQU_SETTING_PARENT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "附属柜子", isForeign = true)
	private EquSetting equSettingParent;

	@Transient
	private String equSettingParentName;

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '管理员帐号ID'")
	private String accountId;
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "管理员", isForeign = true)
	private ViewUser user;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '门位置'")
	private EnumDoor door;

//	private Integer boardNo; // 主控板栈号

//	private Integer doorHeight; // 门高度

	// 仓库别名，当前刀具柜与第三方系统对应
	@Column(columnDefinition = "varchar(20) COMMENT '刀具柜别名'")
	private String wareHouseAlias;

	// 暂存柜共享开关
	@Transient
	private String shareSwitch;

	@Transient
	private Map<Integer, Map<Integer, EquDetailSta>> equInfoMap;

	public EquSetting() {
	}

	public EquSetting(String equSettingName, String serialNo, Integer rowNum, Integer colNum, CabinetType cabinetType,
			String equSettingParentId, String accountId, UsingStatus status, EnumDoor door) {
		this.equSettingName = equSettingName;
		this.serialNo = serialNo;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.cabinetType = cabinetType;
		this.equSettingParentId = equSettingParentId;
		this.accountId = accountId;
		this.status = status;
		this.door = door;
	}

	public String getEquSettingName() {
		return this.equSettingName;
	}

	public void setEquSettingName(String equSettingName) {
		this.equSettingName = equSettingName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}

	public CabinetType getCabinetType() {
		return cabinetType;
	}

	public void setCabinetType(CabinetType cabinetType) {
		this.cabinetType = cabinetType;
	}

	public String getEquSettingParentId() {
		return equSettingParentId;
	}

	public void setEquSettingParentId(String equSettingParentId) {
		this.equSettingParentId = equSettingParentId;
	}

	public EquSetting getEquSettingParent() {
		return equSettingParent;
	}

	public void setEquSettingParent(EquSetting equSettingParent) {
		this.equSettingParent = equSettingParent;
	}

	public String getEquSettingParentName() {
		return equSettingParentName;
	}

	public void setEquSettingParentName(String equSettingParentName) {
		this.equSettingParentName = equSettingParentName;
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

	public UsingStatus getStatus() {
		if (this.status == null) {
			this.status = UsingStatus.ENABLE;
		}
		return this.status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	public EnumDoor getDoor() {
		if (this.door == null) {
			this.door = EnumDoor.LEFT;
		}
		return this.door;
	}

	public void setDoor(EnumDoor door) {
		this.door = door;
	}

//	public Integer getBoardNo() {
//		return this.boardNo;
//	}
//
//	public void setBoardNo(Integer boardNo) {
//		this.boardNo = boardNo;
//	}

//	public Integer getDoorHeight() {
//		return this.doorHeight;
//	}
//
//	public void setDoorHeight(Integer doorHeight) {
//		this.doorHeight = doorHeight;
//	}

	public Map<Integer, Map<Integer, EquDetailSta>> getEquInfoMap() {
		if (this.equInfoMap == null) {
			this.equInfoMap = new HashMap<>();
		}
		return this.equInfoMap;
	}

	public void setEquInfoMap(Map<Integer, Map<Integer, EquDetailSta>> equInfoMap) {
		this.equInfoMap = equInfoMap;
	}

	public String getWareHouseAlias() {
		return this.wareHouseAlias;
	}

	public void setWareHouseAlias(String wareHouseAlias) {
		this.wareHouseAlias = wareHouseAlias;
	}

	public String getShareSwitch() {
		return this.shareSwitch;
	}

	public void setShareSwitch(String shareSwitch) {
		this.shareSwitch = shareSwitch;
	}
}