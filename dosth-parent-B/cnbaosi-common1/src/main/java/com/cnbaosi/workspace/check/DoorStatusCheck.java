package com.cnbaosi.workspace.check;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.dto.Message;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.spring.Door;

/**
 * @description 门检测
 * @author guozhidong
 *
 */
public class DoorStatusCheck extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(DoorStatusCheck.class);

	private Map<Byte, Door> doorMap;

	private DoorStatusCheck(Map<Byte, Door> doorMap) {
		this.doorMap = doorMap;
	}

	@Override
	public void start() {
		logger.info("开始读取门状态");
			try {
				for (Entry<Byte, Door> entry : doorMap.entrySet()) {
					super.readStatus(entry.getKey());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void getListenerData(byte[] data) {
		// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
		// 01 关闭 00 开启
		byte boardNo = data[5]; // 栈号
		if (data[12] != 0x01) { // 门非关闭状态
			logger.info("门处于非关闭状态,发送关门指令");
			try {
				super.closeDoor(boardNo);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receiveMessage(Message message) {
		logger.info("等待做些什么");
	}
}