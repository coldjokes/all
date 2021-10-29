package com.cnbaosi.printer.dto;

import com.cnbaosi.printer.enums.FontSize;
import com.cnbaosi.printer.enums.FontSpec;
import com.cnbaosi.printer.enums.LabelType;

/**
 * @description 标签
 * @author guozhidong
 *
 */
public class Label {
	// 标签类型
	private LabelType type;
	// 字体规格
	private FontSpec fontSpec;
	// 字体大小
	private FontSize fontSize;
	// 宽度
	private Integer width;

	public Label(LabelType type) {
		this.type = type;
	}

	public LabelType getType() {
		return this.type;
	}

	public FontSpec getFontSpec() {
		if (this.fontSpec == null) {
			this.fontSpec = FontSpec.NORMAL;
		}
		return this.fontSpec;
	}

	public void setFontSpec(FontSpec fontSpec) {
		this.fontSpec = fontSpec;
	}

	public FontSize getFontSize() {
		if (this.fontSize == null) {
			this.fontSize = FontSize.S;
		}
		return this.fontSize;
	}

	public void setFontSize(FontSize fontSize) {
		this.fontSize = fontSize;
	}
	
	public Integer getWidth() {
		switch (this.type) {
		// 两个汉字长度
		case MATNAME:
		case BARCODE:
		case MATSPEC:
		case PRINTTIME:
			switch (getFontSize()) {
			case S:
				this.width = 60;
				break;
			default:
				this.width = 70;
				break;
			}
			break;
		// 三个汉字长度
		case USERNAME:
			switch (getFontSize()) {
			case S:
				this.width = 90;
				break;
			default:
				this.width = 105;
				break;
			}
			break;
		// 五个汉字长度
		case PACKBACKNUM:
			switch (getFontSize()) {
			case S:
				this.width = 170;
				break;
			default:
				this.width = 190;
				break;
			}
			break;
		default:
			switch (getFontSize()) {
			case S:
				this.width = 110;
				break;
			default:
				this.width = 120;
				break;
			}
			break;
		}
		return this.width;
	}
}