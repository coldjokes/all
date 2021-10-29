package com.cnbaosi.workspace.door;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.spring.Door;

/**
 * @description V型柜门读取状态
 * @author guozhidong
 *
 */
public abstract class VDoorReader extends StorageMedium {
	private static final Logger logger = LoggerFactory.getLogger(VDoorReader.class);

	// 门集合
	protected Map<Byte, Door> doorMap;

	public VDoorReader(Byte boardNo) {
		super.mainBoardNo = boardNo;
	}

	public Map<Byte, Door> getDoorMap() {
		return this.doorMap;
	}

	@Override
	public void start() {
		logger.info("V型柜门读取状态");
		try {
			super.readSingle(mainBoardNo, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
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