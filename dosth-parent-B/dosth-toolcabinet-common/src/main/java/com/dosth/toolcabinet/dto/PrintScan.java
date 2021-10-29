package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 
 * @description 打印扫描
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class PrintScan implements Serializable {
	private Boolean scanStart; // 扫描状态
	private Object content; // 消息内容

	public PrintScan() {
		setScanStart(true);
	}

	public PrintScan(Boolean scanStart) {
		this.scanStart = scanStart;
	}

	public PrintScan(Object content) {
		setScanStart(true);
		this.content = content;
	}

	public PrintScan(Boolean scanStart, Object content) {
		this.scanStart = scanStart;
		this.content = content;
	}

	public Boolean getScanStart() {
		if (this.scanStart == null) {
			this.scanStart = true;
		}
		return this.scanStart;
	}

	public void setScanStart(Boolean scanStart) {
		this.scanStart = scanStart;
	}

	public Object getContent() {
		return this.content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}