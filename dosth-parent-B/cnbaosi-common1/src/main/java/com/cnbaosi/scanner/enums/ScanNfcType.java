package com.cnbaosi.scanner.enums;

/**
 * @description 扫描仪NFC类型
 * @author guozhidong
 *
 */
public enum ScanNfcType {
	/**
	 * @description 德沃扫描NFC二合一
	 */
	DWSN("德沃扫描NFC二合一"),
	/**
	 * @description 德沃扫描仪
	 */
	DWS("德沃扫描仪"),
	/**
	 * @description 微光扫描NFC二合一
	 */
	WGSN("微光扫描NFC二合一"),
	/**
	 * @description 微光扫描仪
	 */
	WGS("微光扫描仪"),
	/**
	 * @description IC读卡器
	 */
	IC("IC读卡器");
	
	private ScanNfcType(String desc) {
		this.desc = desc;
	}

	/**
	 * @description 说明
	 */
	public String desc;
}