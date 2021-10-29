package com.dosth.constant;

/**
 *  扫描仪读码类型前缀
 * 
 * @author Yifeng Wang  
 */
public enum ScanCodeTypeEnum {
	
	NFC("_NFC_", "NFC"),
	APP("_APP_", "APP码");
	
	private String code;
	private String desc;

	private ScanCodeTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}
}

