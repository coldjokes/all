package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 借出凭证
 * 
 * @author guozhidong
 *
 */
public enum ReceiveType implements IEnumState {

	GRID(1, "全部"),

	MATTYPE(2, "类型"),

	REQREF(3, "设备"),

	PROCREF(4, "工序"),

	PARTS(5, "零件"),

	CUSTOM(6, "自定义"),

//	GETNEWONE(7, "以旧换新"),

	APP(8, "APP预约"),

	APPLYVOUCHER(9, "申请单");

	private Integer code;
	private String message;

	private ReceiveType(Integer code, String message) {
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
			for (ReceiveType voucher : ReceiveType.values()) {
				if (voucher.getCode().equals(code)) {
					return voucher.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return ReceiveType.valueOf(name).getMessage();
	}
}