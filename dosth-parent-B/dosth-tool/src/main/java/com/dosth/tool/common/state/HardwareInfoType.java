package com.dosth.tool.common.state;

/**
 * @description 硬件信息类型
 * @author guozhidong
 *
 */
public enum HardwareInfoType {
	/**
	 * @description 马达
	 */
	MOTOR("马达"),
	/**
	 * @description 取料口门
	 */
	DOOR("取料口门"),
	/**
	 * @description 料斗
	 */
	HOPPER("料斗");
	
	/**
	 * @description 描述
	 */
	private String desc;

	private HardwareInfoType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}
}