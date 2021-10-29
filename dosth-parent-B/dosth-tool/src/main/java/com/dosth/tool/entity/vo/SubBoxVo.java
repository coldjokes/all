package com.dosth.tool.entity.vo;

public class SubBoxVo {
	// 格子id
	private String id;
	// 暂存柜名称
	private String cabinetName;
	// 索引号
	private int boxIndex;
	// 行号
	private int rowNo;
	// 列号
	private int colNo;
	// 柜子状态
	private String cabinetSta;
	// 当前用户
	private String userName;
	// 物料名称
	private String matInfoName;
	// 编号
	private String barCode;
	// 型号
	private String spec;
	// 剩余数量
	private Integer num;

	public SubBoxVo() {
		super();
	}

	public SubBoxVo(String id, String cabinetName, int boxIndex, int rowNo, int colNo, String cabinetSta,
			String userName, String matInfoName, String barCode, String spec, Integer num) {
		super();
		this.id = id;
		this.cabinetName = cabinetName;
		this.boxIndex = boxIndex;
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.cabinetSta = cabinetSta;
		this.userName = userName;
		this.matInfoName = matInfoName;
		this.barCode = barCode;
		this.spec = spec;
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCabinetName() {
		return cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

	public int getBoxIndex() {
		return boxIndex;
	}

	public void setBoxIndex(int boxIndex) {
		this.boxIndex = boxIndex;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getColNo() {
		return colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	public String getCabinetSta() {
		return cabinetSta;
	}

	public void setCabinetSta(String cabinetSta) {
		this.cabinetSta = cabinetSta;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

}
