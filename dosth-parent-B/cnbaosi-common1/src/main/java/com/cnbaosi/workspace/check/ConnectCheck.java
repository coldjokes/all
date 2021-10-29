package com.cnbaosi.workspace.check;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.DeterminantConstant;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.StorageMedium;

/**
 * @description 连接校验
 * @author guozhidong
 *
 */
public abstract class ConnectCheck extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(ConnectCheck.class);
	// 栈号
	private Byte boardNo;
	// 连接标识
	private Boolean isConnnect = false;
	// 超时时间(默认3000ms)
	private final int timeOut = 3000;
	
	public ConnectCheck(Byte boardNo) {
		this.boardNo = boardNo;
		super.mainBoardNo = boardNo;
	}

	@Override
	public void start() {
		logger.info("读取状态检测通讯");
		try {
			super.readStatus(boardNo);
			// 启动连接检测线程
			new Thread(new ConnectStatusCheck(System.currentTimeMillis())).start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		isConnnect = true;
	}

	/**
	 * @description 连接状态检测
	 * @author guozhidong
	 *
	 */
	class ConnectStatusCheck implements Runnable {

		private long start;
		
		ConnectStatusCheck(long start) {
			this.start = start;
		}
		
		@Override
		public void run() {
			Message message = new Message(DeterminantConstant.OK);
			while (true) {
				if (System.currentTimeMillis() - this.start > timeOut) {
					if (isConnnect) {
						logger.info("连接正常");
					} else {
						logger.error("连接超时");
						message.setType(ReturnMsgType.TIME_OUT);
						message.setCustomMsg("发送并重试失败,请联系管理员!");
					}
					receiveMessage(message);
					break;
				} else {
					if (isConnnect) {
						logger.info("连接正常");
						receiveMessage(message);
						break;
					}
					try {
						logger.info("检测通讯有效期内,间隔200ms");
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}		
	}
}