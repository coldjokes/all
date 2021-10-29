package com.exam.entity.enums;

/**
 *   日志枚举
 * 
 * @author Yifeng Wang  
 */
public enum LogTypeEnum implements BaseEnum {
	
	 LOGIN(1, "登录"), 
	 ADD(2, "增加"),
	 DELETE(3, "删除"),
	 UPDATE(4, "修改"),
	 QUERY(5, "查询"),
	 LOGOUT(6, "登出");
	
	private Integer code;
    private String text;

    private LogTypeEnum(int code, String text) {
 		this.code = code;
 		this.text = text;
 	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public String getTextByCode(Integer code) {
		if (code == null) {
			return null;
		} else {
			for (LogTypeEnum logType : LogTypeEnum.values()) {
				if (logType.getCode().equals(code)) {
					return logType.getText();
				}
			}
			return null;
		}
	}

	@Override
	public Integer getCodeByText(String text) {
		return LogTypeEnum.valueOf(text).getCode();
	}
}
