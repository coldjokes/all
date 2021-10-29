package com.cnbaosi.cabinet.entity.enums;

/**
 *  物料库存出入情况
 * @author Yifeng Wang  
 */
public enum MaterialStockEnum implements BaseEnum {
	
	ADD(1, "存入"), 
	DELETE(2, "领取");
	
	private Integer code;
    private String text;

    private MaterialStockEnum(int code, String text) {
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
			for (MaterialStockEnum source : MaterialStockEnum.values()) {
				if (source.getCode().equals(code)) {
					return source.getText();
				}
			}
			return null;
		}
	}

	@Override
	public Integer getCodeByText(String text) {
		return MaterialStockEnum.valueOf(text).getCode();
	}
}
