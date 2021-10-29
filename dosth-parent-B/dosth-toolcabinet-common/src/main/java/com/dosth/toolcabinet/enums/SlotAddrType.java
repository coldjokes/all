package com.dosth.toolcabinet.enums;

/**
 * 槽位地址类型
 * 
 * @author guozhidong
 *
 */
public enum SlotAddrType {
	/**
	 * 故障复位
	 */
	Reset(40001, (byte) 0x00, AddrDataType.COIL_STATUS, "故障复位", 0),
	/**
	 * 过载故障
	 */
	Overload(40002, (byte) 0x01, AddrDataType.COIL_STATUS, "过载故障", 0),
	/**
	 * 电机开路故障
	 */
	OpenMotor(40003, (byte) 0x02, AddrDataType.COIL_STATUS, "电机开路故障", 0),
	/**
	 * 短路故障
	 */
	Circuit(40004, (byte) 0x03, AddrDataType.COIL_STATUS, "短路故障", 0),
	/**
	 * 故障指示灯
	 */
	IndicatorLamp(40005, (byte) 0x04, AddrDataType.COIL_STATUS, "故障指示灯", 0),
	/**
	 * 回原点
	 */
	ReturnOrigin(40006, (byte) 0x05, AddrDataType.COIL_STATUS, "回原点", 0),
	/**
	 * 定位指示灯
	 */
	PositioningIndicator(40007, (byte) 0x06, AddrDataType.COIL_STATUS, "定位指示灯", 0),
	/**
	 * 微动开关故障
	 */
	Microswitch(40008, (byte) 0x07, AddrDataType.COIL_STATUS, "微动开关故障", 0);

	/** 编码 */
	private int address;
	/** 地址位 */
	private byte bit;
	/** modbus数据类型 */
	private AddrDataType dataType;
	/** 描述 */
	private String desc;
	/** 正常值 */
	private int regularValue;

	private SlotAddrType(int address, byte bit, AddrDataType dataType, String desc) {
		this.address = address;
		this.bit = bit;
		this.dataType = dataType;
		this.desc = desc;
	}

	private SlotAddrType(int address, byte bit, AddrDataType dataType, String desc, int regularValue) {
		this.address = address;
		this.bit = bit;
		this.dataType = dataType;
		this.desc = desc;
		this.regularValue = regularValue;
	}

	public int getAddress() {
		return this.address;
	}

	public byte getBit() {
		return this.bit;
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