package com.dosth.toolcabinet.enums;

/**
 * @description plc操作类型
 * @Author guozhidong
 */
public enum PlcOpType {
	ON_UP_COIL(2348, "料斗上升", 1), ON_DOWN_COIL(2349, "料斗下降", 1), ON_OPEN_DOOR_COIL(2350, "取料口开门", 1),
	ON_CLOSE_DOOR_COIL(2351, "取料口关门", 1), ON_RESET_LIFT_COIL(2352, "料斗复位", 1),
	CLOSE_BEER(2560, "禁用蜂鸣器", 1), RETURN_BACK_DOOR_REST(2364, "回收口复位", 1), TO_DOWN_ZERO(4186, "一键补料", 1),
	RESTART_PRINTER(2363, "重置打印机", 1),ON_RESET_PLC_STATUS(2365, "恢复领料故障", 1);

	private int code;
	private String desc;
	private int resVal;

	private PlcOpType(int code, String desc, int resVal) {
		this.code = code;
		this.desc = desc;
		this.resVal = resVal;
	}

	public int getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public int getResVal() {
		return this.resVal;
	}
}