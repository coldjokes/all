package com.cnbaosi.cabinet.entity.modal;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 物料出入库流水
 * 
 * @author Yifeng Wang
 */

public class MaterialBill extends BaseModel<MaterialBill> {

	private static final long serialVersionUID = 1L;

	private String materialId; // 物料id
	private String materialName; // 物料名称
	private String materialNo; // 物料编号
	private String materialSpec; // 物料规格
	private String materialPicture; // 物料图片
	private String materialRemark; //物料备注
	
	@TableField(exist = false)
	private String stockId; //库存库位id
	private Integer amountStart; //起始数量
	private Integer amountDiff; // 数量差
	private Integer amountEnd; //结束数量
	
	private String computerId; // 主机id
	private String computerName; //主机名称
	
	private String cabinetId; // 柜子id
	private String cabinetName; //柜子名称
	
	private String cellId; // 格口id
	private String cellName; //格口名称
	
	private Integer operateType; // 操作类型 1:领出 2:存入
	private String operateName; //操作名称
	
	private String userId; // 操作人id
	private String userFullname; //操作人姓名
	
	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public Integer getAmountDiff() {
		return amountDiff;
	}

	public void setAmountDiff(Integer amountDiff) {
		this.amountDiff = amountDiff;
	}

	public String getComputerId() {
		return computerId;
	}

	public void setComputerId(String computerId) {
		this.computerId = computerId;
	}

	public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComputerName() {
		return computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public String getCabinetName() {
		return cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getUserFullname() {
		return userFullname;
	}

	public void setUserFullname(String userFullname) {
		this.userFullname = userFullname;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public String getMaterialNo() {
		return materialNo;
	}

	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}

	public String getMaterialSpec() {
		return materialSpec;
	}

	public void setMaterialSpec(String materialSpec) {
		this.materialSpec = materialSpec;
	}

	public String getMaterialPicture() {
		return materialPicture;
	}

	public void setMaterialPicture(String materialPicture) {
		this.materialPicture = materialPicture;
	}

	public String getMaterialRemark() {
		return materialRemark;
	}

	public void setMaterialRemark(String materialRemark) {
		this.materialRemark = materialRemark;
	}

	public Integer getAmountStart() {
		return amountStart;
	}

	public void setAmountStart(Integer amountStart) {
		this.amountStart = amountStart;
	}

	public Integer getAmountEnd() {
		return amountEnd;
	}

	public void setAmountEnd(Integer amountEnd) {
		this.amountEnd = amountEnd;
	}

}
