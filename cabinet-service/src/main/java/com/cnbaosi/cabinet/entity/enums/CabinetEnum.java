package com.cnbaosi.cabinet.entity.enums;

/**
 *  主副柜
 * @author Yifeng Wang  
 */
public enum CabinetEnum implements BaseEnum {
	
	MAIN(1, "主柜"), 
	SUB(2, "副柜");
	
	private Integer code;
    private String text;

    private CabinetEnum(int code, String text) {
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
			for (CabinetEnum source : CabinetEnum.values()) {
				if (source.getCode().equals(code)) {
					return source.getText();
				}
			}
			return null;
		}
	}

	@Override
	public Integer getCodeByText(String text) {
		return CabinetEnum.valueOf(text).getCode();
	}
}
