package com.cnbaosi.printer.dto;

/**
 * @description 打印文字对象
 * @author guozhidong
 *
 */
public class PrintTextInfo {
	// 打印标签
	private Label label;
	// 打印文本
	private Txt txt;
	// 行高
	private Integer lineHeight;

	public PrintTextInfo(Label label, Txt txt) {
		this.label = label;
		this.txt = txt;
	}

	public PrintTextInfo(Txt txt) {
		this.txt = txt;
	}

	public Label getLabel() {
		return this.label;
	}

	public Txt getTxt() {
		return this.txt;
	}

	public Integer getLineHeight() { 
		// 未设定行高的情况下,首先按文本字体大小,如果标签字体大于文本字体,按标签字体行高设定
		if (this.lineHeight == null) {
			this.lineHeight = this.txt.getFontSize().getLineHeight();
			if (this.label != null && this.label.getFontSize().getLineHeight() > this.txt.getFontSize().getLineHeight()) {
				this.lineHeight = this.label.getFontSize().getLineHeight();
			}
		}
		return this.lineHeight;
	}

	public void setLineHeight(Integer lineHeight) {
		this.lineHeight = lineHeight;
	}
}