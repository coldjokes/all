package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 通知类型
 * @author Weifeng Li
 */
public enum NoticeType implements IEnumState {
	PRINT(1, "打印纸"), RECOVERY(2, "回收仓"), TEMCABINET(3, "暂存柜"), STOCK(4, "库存");

	private Integer code;
	private String message;

	private NoticeType(Integer code, String message) {
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
			for (NoticeType type : NoticeType.values()) {
				if (type.getCode().equals(code)) {
					return type.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return NoticeType.valueOf(name).getMessage();
	}

	public static NoticeType valueOfMessage(String message) {
		if (message == null) {
			return null;
		} else {
			for (NoticeType type : NoticeType.values()) {
				if (type.getMessage().equals(message)) {
					return type;
				}
			}
			return null;
		}
	}

}