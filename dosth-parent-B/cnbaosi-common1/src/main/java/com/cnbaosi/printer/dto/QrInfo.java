package com.cnbaosi.printer.dto;

import com.cnbaosi.printer.enums.QrSize;

/**
 * @description 二维码信息
 * @author guozhidong
 *
 */
public class QrInfo {
	// 左边距
	private Integer left;
	// 上边距
	private Integer top;
	// 二维码尺寸
	private QrSize size;
	// 二维码内容
	private String content;

	public QrInfo() {
	}

	public QrInfo(String content) {
		this.content = content;
	}

	public Integer getLeft() {
		if (this.left == null) {
			this.left = 0;
		}
		return this.left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getTop() {
		if (this.top == null) {
			this.top = 0;
		}
		return this.top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public QrSize getSize() {
		if (this.size == null) {
			this.size = QrSize.NORMAL;
		}
		return this.size;
	}

	public void setSize(QrSize size) {
		this.size = size;
	}

	public String getContent() {
		if (this.content == null || "".equals(this.content)) {
			this.content = " ";
		}
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}