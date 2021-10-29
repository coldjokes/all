package com.cnbaosi.workspace.door;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.spring.Door;

/**
 * @description 门协议
 * @author guozhidong
 *
 */
public abstract class DoorReader extends StorageMedium {
	private static final Logger logger = LoggerFactory.getLogger(DoorReader.class);
	// 门集合
	protected Map<Byte, Door> doorMap;

	public DoorReader(Byte boardNo, Map<Byte, Door> doorMap) {
		super.mainBoardNo = boardNo;
		this.doorMap = doorMap;
	}

	public Map<Byte, Door> getDoorMap() {
		return this.doorMap;
	}

	@Override
	public void start() {
		logger.info("门操作前门状态验证...");
		for (Entry<Byte, Door> entry : doorMap.entrySet()) {
			try {
				super.readStatus(entry.getKey());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		next(data);
	}
	
	/**
	 * @description 下一步操作
	 * @param data 返回信号数据
	 */
	protected abstract void next(byte[] data);
}