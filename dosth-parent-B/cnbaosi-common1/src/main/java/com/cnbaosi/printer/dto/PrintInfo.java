package com.cnbaosi.printer.dto;

import java.util.List;

/**
 * @description 打印信息
 * @author guozhidong
 *
 */
public class PrintInfo {
	 // 左边距
	private Integer left;
	 // 上边距
	private Integer top;
	// 打印宽度范围
	private Integer width;
	// 打印高度范围
	private Integer height;
	// 二维码内容
	private QrInfo qrInfo;
	// 打印文本信息
	private List<PrintTextInfo> textInfoList;

	public PrintInfo(QrInfo qrInfo, List<PrintTextInfo> textInfoList) {
		this.qrInfo = qrInfo;
		this.textInfoList = textInfoList;
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
			this.top = 10;
		}
		return this.top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getWidth() {
		if (this.width == null) {
			this.width = 384;
		}
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		if (this.height == null) {
			this.height = 250;
		}
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public QrInfo getQrInfo() {
		return this.qrInfo;
	}

	public List<PrintTextInfo> getTextInfoList() {
		return this.textInfoList;
	}
}