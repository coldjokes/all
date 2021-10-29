package com.cnbaosi.cabinet.entity.enums;

/**
 *  物料库存状态
 * @author Yifeng Wang  
 */
public enum MaterialStatusEnum implements BaseEnum {
	
	OK(1, "可用"), 
	NOT_OK(0, "不可用");
	
	private Integer code;
    private String text;

    private MaterialStatusEnum(int code, String text) {
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
			for (MaterialStatusEnum source : MaterialStatusEnum.values()) {
				if (source.getCode().equals(code)) {
					return source.getText();
				}
			}
			return null;
		}
	}

	@Override
	public Integer getCodeByText(String text) {
		return MaterialStatusEnum.valueOf(text).getCode();
	}
}
