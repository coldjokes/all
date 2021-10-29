package com.exam.entity.enums;

/**
 * 状态
 * @author Yifeng Wang  
 */
public enum StatusEnum implements BaseEnum {
	
	ON(1, "启用"), 
	OFF(2, "禁用");
	
	private Integer code;
    private String text;

    private StatusEnum(int code, String text) {
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
			for (StatusEnum status : StatusEnum.values()) {
				if (status.getCode().equals(code)) {
					return status.getText();
				}
			}
			return null;
		}
	}

	@Override
	public Integer getCodeByText(String text) {
		return StatusEnum.valueOf(text).getCode();
	}

}
