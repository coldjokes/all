package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 扫描仪类型
 * @author Weifeng.Li
 */
public enum ScanType implements IEnumState {
	DWSN(1, "德沃扫描NFC二合一"), DWS(2, "德沃扫描仪"), WGSN(3, "微光扫描NFC二合一"), WGS(4, "微光扫描仪"), IC(5, "IC/ID");

	private Integer code;
	private String message;

	private ScanType(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return null;
		} else {
			for (ScanType state : ScanType.values()) {
				if (state.getCode().equals(code)) {
					return state.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return ScanType.valueOf(name).getMessage();
	}

}
