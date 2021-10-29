package com.cnbaosi.modbus.monitor;

/**
 * @description 监控接口
 * @author guozhidong
 *
 */
public interface Monitor {
	/**
	 * @description 接收消息
	 * @param message 消息内容
	 * @return
	 */
	public abstract void receiveMessage(String message);
}