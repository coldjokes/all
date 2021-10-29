package com.cnbaosi.printer.dto;

import com.cnbaosi.printer.enums.FontSize;
import com.cnbaosi.printer.enums.FontSpec;

/**
 * @description 文本
 * @author guozhidong
 *
 */
public class Txt {
	// 文本内容
	private String text;
	// 字体规格
	private FontSpec fontSpec;
	// 字体大小
	private FontSize fontSize;

	public Txt(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
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
}