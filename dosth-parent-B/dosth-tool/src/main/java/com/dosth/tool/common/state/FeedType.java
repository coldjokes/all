package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 补料类型
 * @author guozhidong
 *
 */
public enum FeedType implements IEnumState {
	LIST(1, "后台"),
	HAND(2, "前台"),
	API(3, "外部接口");
	

	private Integer code;
	private String message;

	private FeedType(Integer code, String message) {
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
			for (FeedType type : FeedType.values()) {
				if (type.getCode().equals(code)) {
					return type.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return FeedType.valueOf(name).getMessage();
	}
}