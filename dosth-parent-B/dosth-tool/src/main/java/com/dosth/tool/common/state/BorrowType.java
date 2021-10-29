package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 借出类型定义
 * @author guozhidong
 *
 */
public enum BorrowType implements IEnumState {
	
	PACK(1, "盒", "B1,B2"),METER(2, "支", "B1");
	
	private Integer code;
	
	private String message;
	
	private String type;

	private BorrowType(Integer code, String message, String type) {
		this.code = code;
		this.message = message;
		this.type = type;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public String getType() {
		return type;
	}

	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return null;
		} else {
			for (BorrowType type : BorrowType.values()) {
				if (type.getCode().equals(code)) {
					return type.getMessage();
				}
			}
			return null;
		}
	}

	public static BorrowType valueOfMessage(String message) {
		if (message == null) {
			return null;
		} else {
			for (BorrowType type : BorrowType.values()) {
				if (type.getMessage().equals(message)) {
					return type;
				}
			}
			return null;
		}
	}
	
	@Override
	public String valueOfName(String name) {
		return BorrowType.valueOf(name).getMessage();
	}
}