package com.dosth.toolcabinet.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.ElecLock;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.tmp.TmpMedium;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * @description 锁控板工具类
 * @author guozhidong
 *
 */
@Component
public class LockBoardCabinetUtil {

	private static final Logger logger = LoggerFactory.getLogger(LockBoardCabinetUtil.class);
	
	@Autowired
	private CabinetConfig cabinetConfig;
	
	private BlockingQueue<ElecLock> elecLockQueue = new LinkedBlockingQueue<>();
	
	public void putElecLock(ElecLock elecLock) throws InterruptedException {
		this.elecLockQueue.put(elecLock);
	}
	
	/**
	 * @description 锁控板队列消费启动
	 */
	public void start() {
		logger.info("锁控板队列消费启动");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Lock lock = new ReentrantLock();
				Condition next = lock.newCondition();
				while (true) {
					lock.lock();
					try {
						ElecLock elecLock = elecLockQueue.take();
						elecLock.setSoundPath(cabinetConfig.getAudioPath() + elecLock.getBoxIndex() + ".mp3");
						Thread.sleep(2000);
						logger.info("开启位置:" + elecLock.getBoxIndex() + ",栈号:" + elecLock.getBoardNo() + ",针脚:" + elecLock.getLockIndex());
						StorageMedium medium = new TmpMedium(elecLock.getBoardNo().byteValue(), elecLock) {
							@Override
							public void receiveMessage(com.cnbaosi.dto.Message message) {
								if (ReturnMsgType.UNRECEIVED.equals(message.getType())) {
									WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.SUB_UNRECEIVED, 
											new StringBuffer("未检测到").append(elecLock.getBoxIndex()).append("号抽屉信号").toString()));
								} else {
									WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.SUB_OPEN_SUC, message.getCustomMsg()));
								}
								lock.lock();
								try {
									next.signal();
								} finally {
									lock.unlock();
								}
							}
						};
						StorageMediumPicker.putStorageMedium(medium);
						next.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						lock.unlock();
					}
				}
			}
		}).start();
	}
}