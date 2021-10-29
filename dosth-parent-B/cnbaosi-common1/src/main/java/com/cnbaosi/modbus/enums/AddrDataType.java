package com.cnbaosi.modbus.enums;

/**
 * @description 地址数据类型
 * 
 * @author guozhidong
 *
 */
public enum AddrDataType {
	/**
	 * Coil Status
	 * 
	 * @Function 0X
	 */
	COIL_STATUS("01", "Coils(Output), ", "0X", ReadWrite.ReadWrite),
	/**
	 * Input Status/Discrete Inputs
	 * 
	 * @Function 1X
	 */
	INPUT_STATUS("02", "Input Status", "1X", ReadWrite.ReadOnly),
	/**
	 * Holding Register
	 * 
	 * @Function 4X
	 */
	HOLDING_REGISTER("03", "Holding Register", "4X", ReadWrite.ReadWrite),
	/**
	 * Input Registers
	 * 
	 * @Function 3X
	 */
	INPUT_REGISTERS("04", "Input Registers, R", "3X", ReadWrite.ReadOnly);

	/** 编码 */
	private String code;
	/** 名称 */
	private String name;
	/** 功能 */
	private String function;
	/** 读写 */
	private ReadWrite rw;

	private AddrDataType(String code, String name, String function, ReadWrite rw) {
		this.code = code;
		this.name = name;
		this.function = function;
		this.rw = rw;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}

	public String getFunction() {
		return this.function;
	}

	public ReadWrite getRw() {
		return this.rw;
	}
}