package com.dosth.comm.softhand;

/**
 * @description 软硬件通讯消息
 * @author guozhidong
 *
 */
public class SoftHandCommMsg {
	// 软硬件通讯
	private SoftHandComm softHandComm;
	// 消息体
	private String message;

	public SoftHandCommMsg() {
	}

	public SoftHandCommMsg(SoftHandComm softHandComm, String message) {
		this.softHandComm = softHandComm;
		this.message = message;
	}

	public SoftHandComm getSoftHandComm() {
		return this.softHandComm;
	}

	public void setSoftHandComm(SoftHandComm softHandComm) {
		this.softHandComm = softHandComm;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}