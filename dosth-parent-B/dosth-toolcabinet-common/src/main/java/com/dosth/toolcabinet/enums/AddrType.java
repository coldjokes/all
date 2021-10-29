package com.dosth.toolcabinet.enums;

/**
 * 地址类型
 * 
 * @author guozhidong
 *
 */
public enum AddrType {
	
	/**
	 * 1、料斗目标层
	 */
	OutTo(4096, AddrDataType.HOLDING_REGISTER, "料斗目标层", 0),
	/**
	 * 2、是否到达 4097 0
	 */
	IsArr(4097, AddrDataType.HOLDING_REGISTER, "是否到达", 0),
	/**
	 * 3、设置速度 6556 30
	 */
	Speed(6556, AddrDataType.HOLDING_REGISTER, "设置速度", 30),
	/**
	 * 4、料斗实时位置(毫米) 6590 705
	 */
	HopperNow(6590, AddrDataType.HOLDING_REGISTER, "料斗实时位置", 705),
	/**
	 * 5、当前层 6592 0
	 */
	LvlCur(6592, AddrDataType.HOLDING_REGISTER, "当前层", 0),
	/**
	 * 6、取料口位置(毫米) 4608 705
	 */
	OuterPos(4608, AddrDataType.HOLDING_REGISTER, "取料口位置", 705),
	/**
	 * 7、降低(毫米) 4610 16
	 */
	Reduce(4610, AddrDataType.HOLDING_REGISTER, "降低(毫米)", 16),
	/**
	 * 8、F1 位置 6912 284
	 */
	F1(6912, AddrDataType.HOLDING_REGISTER, "F1位置", 284),
	/**
	 * 9、F2位置 6914 450
	 */
	F2(6914, AddrDataType.HOLDING_REGISTER, "F2位置", 450),
	/**
	 * 10、F3位置 6916 627
	 */
	F3(6916, AddrDataType.HOLDING_REGISTER, "F3位置", 627),
	/**
	 * 11、F4位置 6918 808
	 */
	F4(6918, AddrDataType.HOLDING_REGISTER, "F4位置", 808),
	/**
	 * 12、F5位置 6920 980
	 */
	F5(6920, AddrDataType.HOLDING_REGISTER, "F5位置", 980),
	/**
	 * 13、F6位置 6922 1158
	 */
	F6(6922, AddrDataType.HOLDING_REGISTER, "F6位置", 1158),
	/**
	 * 14、F7位置 6924 1420
	 */
	F7(6924, AddrDataType.HOLDING_REGISTER, "F7位置", 1420),
	/**
	 * 15、F8位置 6926 1483
	 */
	F8(6926, AddrDataType.HOLDING_REGISTER, "F8位置", 1483),
	/**
	 * 16、F9位置 6928 1666
	 */
	F9(6928, AddrDataType.HOLDING_REGISTER, "F9位置", 1666),
	/**
	 * 17、运行 2080 0
	 */
	Run(2080, AddrDataType.COIL_STATUS, "运行", 0),
	/**
	 * 18、停止 2081 0
	 */
	Stop(2081, AddrDataType.COIL_STATUS, "停止", 0),
	/**
	 * 19、取料口关门 2116 0
	 */
	OuterClose(2116, AddrDataType.COIL_STATUS, "取料口关门", 0),
	/**
	 * 20、取料口开门 2110 0
	 */
	OuterOpen(2110, AddrDataType.COIL_STATUS, "取料口开门", 0),
	/**
	 * 21、回收口开门 2109 0
	 */
	BackOpen(2109, AddrDataType.COIL_STATUS, "回收口开门", 0),
	/**
	 * 22、是否在取料口 2119 1
	 */
	Outed(2119, AddrDataType.COIL_STATUS, "是否在取料口", 1),
	/**
	 * 23、故障告警 2120 0
	 */
	ProblemWarn(2120, AddrDataType.COIL_STATUS, "故障告警", 0),
	/**
	 * 24、出料完成 2083 0
	 */
	OutFinished(2083, AddrDataType.COIL_STATUS, "出料完成", 0),
	/**
	 * 25、复位 2085 0
	 */
	Reset(2085, AddrDataType.COIL_STATUS, "复位", 0),
	/**
	 * 26、回原点 2059 0
	 */
	ReturnOrigin(2059, AddrDataType.COIL_STATUS, "回原点", 0),
	/**
	 * 27、上限位 2051 0
	 */
	UpperLimitPosition(2051, AddrDataType.COIL_STATUS, "上限位", 0),
	/**
	 * 28、下限位 2053 0
	 */
	LowerLimitPosition(2053, AddrDataType.COIL_STATUS, "下限位", 0),
	/**
	 * 29、点动上升 2060
	 */
	PointUp(2060, AddrDataType.COIL_STATUS, "点动上升", 0),
	/**
	 * 30、点动下降 2061 0
	 */
	PointDown(2061, AddrDataType.COIL_STATUS, "点动下降", 0);

	/** 编码 */
	private int address;
	/** modbus数据类型 */
	private AddrDataType dataType;
	/** 描述 */
	private String desc;
	/** 正常值 */
	private int regularValue;

	private AddrType(int address, AddrDataType dataType, String desc) {
		this.address = address;
		this.dataType = dataType;
		this.desc = desc;
	}

	private AddrType(int address, AddrDataType dataType, String desc, int regularValue) {
		this.address = address;
		this.dataType = dataType;
		this.desc = desc;
		this.regularValue = regularValue;
	}

	public int getAddress() {
		return this.address;
	}

	public AddrDataType getDataType() {
		return this.dataType;
	}

	public String getDesc() {
		return this.desc;
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