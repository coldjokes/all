package com.dosth.toolcabinet.config;

public class CabinetConfiguration {
	
	/**
	 * @description session超时标识
	 * @desc Y 表示需超时 N标识不超时
	 */
	public static String SESSION_TIMEOUT_FLAG = "SESSION_TIMEOUT_FLAG";

	/**
	 * 柜子Id
	 */
	private static String cabinetId;
	
	/**
	 * 柜子名称
	 */
	private static String cabinetName;
	
	/**
	 * 柜子版本
	 */
	private static String cabinetVer;


	public static String getCabinetId() {
		return cabinetId;
	}

	public static void setCabinetId(String cabinetId) {
		CabinetConfiguration.cabinetId = cabinetId;
	}

	public static String getCabinetName() {
		return cabinetName;
	}

	public static void setCabinetName(String cabinetName) {
		CabinetConfiguration.cabinetName = cabinetName;
	}

	public static String getCabinetVer() {
		return cabinetVer;
	}

	public static void setCabinetVer(String cabinetVer) {
		CabinetConfiguration.cabinetVer = cabinetVer;
	}
	
}
