package com.cnbaosi.modbus.enums;

/**
 * @description 地址类型
 * 
 * @author guozhidong
 *
 */
public enum AddrType {
	F1("F1", 6097, AddrDataType.HOLDING_REGISTER, "F1"),
	F2("F2", 6099, AddrDataType.HOLDING_REGISTER, "F2"),
	F3("F3", 6101, AddrDataType.HOLDING_REGISTER, "F3"),
	F4("F4", 6103, AddrDataType.HOLDING_REGISTER, "F4"),
	F5("F5", 6105, AddrDataType.HOLDING_REGISTER, "F5"),
	F6("F6", 6107, AddrDataType.HOLDING_REGISTER, "F6"),
	F7("F7", 6109, AddrDataType.HOLDING_REGISTER, "F7"),
	F8("F8", 6111, AddrDataType.HOLDING_REGISTER, "F8"),
	F9("F9", 6113, AddrDataType.HOLDING_REGISTER, "F9"),
	F10("F10", 6115, AddrDataType.HOLDING_REGISTER, "F10"),

	DoorCoordinate("DoorCoordinate", 6117, AddrDataType.HOLDING_REGISTER, "取料口位置"),
	DoorCoordinateHigh("DoorCoordinateLower", 6118, AddrDataType.HOLDING_REGISTER, "取料口位置高位"),
	Speed("Speed", 6119, AddrDataType.HOLDING_REGISTER, "运行速度"),
	CurPosition("CurPosition", 6121, AddrDataType.HOLDING_REGISTER, "实时位置"),
	CurPositionHigh("CurPositionLower", 6122, AddrDataType.HOLDING_REGISTER, "实时位置高位"),
	IP1ST("IP1st", 6125, AddrDataType.HOLDING_REGISTER, "IP第一段"),
	IP2ND("IP2nd", 6126, AddrDataType.HOLDING_REGISTER, "IP第二段"),
	IP3RD("IP3rd", 6127, AddrDataType.HOLDING_REGISTER, "IP第三段"),
	IP4TH("IP4th", 6128, AddrDataType.HOLDING_REGISTER, "IP第四段"),
	
	PositionCoefficient("PositionCoefficient", 6167, AddrDataType.HOLDING_REGISTER, "位置系数"),
	
	TargetLvl("TargetLevel", 4187, AddrDataType.HOLDING_REGISTER, "目标层"),
	PointRising("PointRising", 2349, AddrDataType.COIL_STATUS, "点动上升", 0),
	PointDrop("PointDrop", 2350, AddrDataType.COIL_STATUS, "点动下降", 0),
	OpenLeftDoor("OpenLeftDoor", 2351, AddrDataType.COIL_STATUS, "左侧取料口开门", 0),
	CloseLeftDoor("CloseLeftDoor", 2352, AddrDataType.COIL_STATUS, "左侧取料口关门", 0),
	ResetHopper("ResetHopper", 2353, AddrDataType.COIL_STATUS, "料斗复位", 0),
	IsArrive("IsArrive", 2354, AddrDataType.COIL_STATUS, "已到达目标层", 1),
	LeftDoorCheck("LeftDoorCheck", 1045, AddrDataType.INPUT_STATUS, "左侧取料口状态", 0),
	ErrFlag("ErrFlag", 2356, AddrDataType.INPUT_STATUS, "故障标识", 1),
	
	ErrReset("ErrReset", 2363, AddrDataType.COIL_STATUS, "伺服驱动器故障复位", 0),
	RecovReset("RecovReset", 2365, AddrDataType.COIL_STATUS, "回收口复位", 0),
	

	OpenRightDoor("OpenRightDoor", 2371, AddrDataType.COIL_STATUS, "右侧取料口开门", 0),
	CloseRightDoor("CloseRightDoor", 2372, AddrDataType.COIL_STATUS, "右侧取料口关门", 0),
	
	/**
	 * @description 回收口 1 复位 2 左翻 3 右翻 
	 */
	ScanResult("ScanResult", 4188, AddrDataType.HOLDING_REGISTER, "扫码结果", 0),
	
	RightDoorCheck("RightDoorCheck", 2373, AddrDataType.INPUT_STATUS, "右侧取料口状态", 0),
	
	/**
	 * @description 左门信号
	 */
	DoorSignal("M005", 1031, AddrDataType.INPUT_STATUS, "门未关紧", 1),
	/**
	 * @description 左门信号
	 */
	LeftDoorSignal("M005", 1041, AddrDataType.INPUT_STATUS, "左门未关紧", 1),
	/**
	 * @description 右门信号
	 */
	RightDoorSignal("M006", 1042, AddrDataType.INPUT_STATUS, "右门未关紧", 1),
	/**
	 * @description 取料口开门故障清除
	 */
	DoorOpenFailClear("M001", 1548, AddrDataType.COIL_STATUS, "取料口开门故障清除", 0),
	/**
	 * @description 取料口关门故障清除
	 */
	DoorCloseFailClear("M002", 1549, AddrDataType.COIL_STATUS, "取料口关门故障清除", 0),
	/**
	 * @description 右侧取料口开门故障清除
	 */
	RightDoorOpenFailClear("M003", 1550, AddrDataType.COIL_STATUS, "右侧取料口开门故障清除", 0),
	/**
	 * @description 右侧取料口关门故障清除
	 */
	RightDoorCloseFailClear("M004", 1551, AddrDataType.COIL_STATUS, "右侧取料口关门故障清除", 0),
	/**
	 * @description 上限位报警
	 */
	TopBoundAlarm("X001", 2357, AddrDataType.INPUT_STATUS, "上限位报警", 1),
	/**
	 * @description 下限位报警
	 */
	BottomBoundAlarm("X002", 2358, AddrDataType.INPUT_STATUS, "下限位报警", 1),
	/**
	 * @description 伺服驱动器报警
	 */
	ServoAlarm("S001", 2359, AddrDataType.INPUT_STATUS, "伺服驱动器报警", 1),
	/**
	 * @description 左侧取料口开门失败
	 */
	LeftDoorOpenFail("M001", 2360, AddrDataType.INPUT_STATUS, "左侧取料口开门失败", 1),
	/**
	 * @description 
	 */
	LeftDoorCloseFail("M002", 2361, AddrDataType.INPUT_STATUS, "左侧取料口关门失败", 1),
	/**
	 * @description 右侧取料口开门失败
	 */
	RightDoorOpenFail("M003", 2374, AddrDataType.INPUT_STATUS, "右侧取料口开门失败", 1),
	/**
	 * @description 右侧取料口关门失败
	 */
	RightDoorCloseFail("M004", 2375, AddrDataType.INPUT_STATUS, "右侧取料口关门失败", 1),
	/**
	 * @description 取料口未关紧
	 */
	DoorCheck("M001", 2355, AddrDataType.INPUT_STATUS, "取料口未关紧", 0);

	/**代码*/
	private String code;
	/** 编码 */
	private int address;
	/** modbus数据类型 */
	private AddrDataType dataType;
	/** 描述 */
	private String desc;
	/** 正常值 */
	private int regularValue;

	private AddrType(String code, int address, AddrDataType dataType, String desc) {
		this.code = code;
		this.address = address;
		this.dataType = dataType;
		this.desc = desc;
	}

	private AddrType(String code, int address, AddrDataType dataType, String desc, int regularValue) {
		this.code = code;
		this.address = address;
		this.dataType = dataType;
		this.desc = desc;
		this.regularValue = regularValue;
	}

	public String getCode() {
		return this.code;
	}

	public int getAddress() {
		return this.address - 1;
	}

	public AddrDataType getDataType() {
		return this.dataType;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setRegularValue(int regularValue) {
		this.regularValue = regularValue;
	}

	public int getRegularValue() {
		return this.regularValue;
	}

	@Override
	public String toString() {
		return new StringBuilder("addrType {address=").append(this.address).append(", desc=").append(this.desc)
				.append("}").toString();
	}
}