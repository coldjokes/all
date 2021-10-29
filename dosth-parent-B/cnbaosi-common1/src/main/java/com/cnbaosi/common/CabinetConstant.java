package com.cnbaosi.common;

/**
 * @description 柜体常量
 * @author guozhidong
 *
 */
public final class CabinetConstant {
	private CabinetConstant() {
	}
	
	/**
	 * @description 连接标识
	 */
	public static Boolean connectFlag = true;
	
	/**
	 * @description 系统繁忙标识
	 */
	public static Boolean busyFlag = false;

	/**
	 * @description 撞机标识
	 */
	public static Boolean collisionFlag = false;
	
	/**
	 * @description 检测门标识
	 */
	public static Boolean checkDoorFlag = false;
	
	/**
	 * @description 
	 */
	public static Boolean hopperRunningFlag = false;
}