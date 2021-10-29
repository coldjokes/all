package com.dosth.common.constant;

/**
 * 表格选择类型
 * 
 * @author guozhidong
 *
 */
public enum TableSelectType implements IEnumState {
	RADIO(0, "单选框", "radio"), BOX(1, "复选框", "checkbox");

	private Integer code;
	private String message;
	private String type;

	private TableSelectType(Integer code, String message, String type) {
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
		return this.type;
	}

	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return "";
		} else {
			for (TableSelectType type : TableSelectType.values()) {
				if (type.getCode() == code) {
					return type.getMessage();
				}
			}
			return "";
		}
	}

	@Override
	public String valueOfName(String name) {
		return TableSelectType.valueOf(name).getMessage();
	}
}