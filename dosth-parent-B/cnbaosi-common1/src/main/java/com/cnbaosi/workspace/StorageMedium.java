package com.cnbaosi.workspace;

import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.DeterminantConstant;
import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.util.SerialTool;
import com.cnbaosi.workspace.spring.Door;

/**
 * @description 仓储介质
 * @author guozhidong
 *
 */
public abstract class StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(StorageMedium.class);
	
	// 等待时间
	protected static final long TIME_WAIT = 500;
	
	// 归属于栈号
	protected Byte groupBoardNo;
	// 主控栈号
	protected Byte mainBoardNo;
	// 速度
	private int speed = 2;

	// 待发送命令
	public static BlockingQueue<byte[]> orderQueue = new LinkedBlockingQueue<>();
	// 串口接收命令
	public static BlockingQueue<Byte> messageQueue = new LinkedBlockingQueue<>();

	public Byte getGroupBoardNo() {
		return this.groupBoardNo;
	}

	public Byte getMainBoardNo() {
		return this.mainBoardNo;
	}

	/**
	 * @description 清空发送命令区
	 */
	public static void cleanOrderQueue() {
		orderQueue.clear();
	}

	/**
	 * @description 清空接收命令区
	 */
	public static void cleanMessageQueue() {
		messageQueue.clear();
	}

	/**
	 * @description 设置发送命令
	 * @param bytes 待发送命令
	 * @throws InterruptedException
	 */
	protected void putOrderBytes(byte[] bytes) throws InterruptedException {
		logger.info("推送消息>>" + SerialTool.bytesToHexString(bytes));
		orderQueue.put(bytes);
	}

	/**
	 * @description 设置接收反馈信息
	 * @param bytes 接收的反馈信号
	 * @throws InterruptedException
	 */
	public static void putMessageBytes(byte[] bytes) throws InterruptedException {
		logger.info("接收消息<<" + SerialTool.bytesToHexString(bytes));
		for (byte b : bytes) {
			messageQueue.put(b);
		}
	}

	/**
	 * @description 发送截止符
	 * @throws InterruptedException
	 */
	public void sendOver() throws InterruptedException {
		logger.info("发文截至符");
		putOrderBytes(DeterminantConstant.OK);
	}

	/**
	 * @description 接收截至符
	 * @throws InterruptedException
	 */
	public void receiveOver() throws InterruptedException {
		logger.info("收文截至符");
		putMessageBytes(new byte[] { DeterminantConstant.OK[0], DeterminantConstant.OK[0], DeterminantConstant.OK[1],
				DeterminantConstant.OK[1] });
	}
	
	/**
	 * @description 读取单个电磁锁状态
	 * @param boardNo 栈号
	 * @param lockIndex 锁索引号
	 * @throws InterruptedException
	 */
	protected void readSingle(Byte boardNo, Integer lockIndex) throws InterruptedException {
		putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.READSINGLE, new byte[] {lockIndex.byteValue()}));
	}
	
	/**
	 * @description 打开电磁锁
	 * @param boardNo 栈号
	 * @param lockIndex 锁索引号
	 * @throws InterruptedException
	 */
	protected void openSingle(Byte boardNo, Integer lockIndex) throws InterruptedException {
		putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.OPENSINGLE, new byte[] {lockIndex.byteValue()}));
	}
	
	/**
	 * @description 读状态
	 * @param boardNo 栈号
	 * @throws InterruptedException
	 */
	protected void readStatus(Byte boardNo) throws InterruptedException {
		putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.READSTATUS));
	}

	/**
	 * @description 关门
	 * @param boardNo 栈号
	 * @throws InterruptedException
	 */
	protected void closeDoor(Byte boardNo) throws InterruptedException {
		putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.CLOSEDOOR, new byte[] {( 0x01 )}));
	}
	
	/**
	 * @description 开门
	 * @param boardNo 栈号
	 * @throws InterruptedException
	 */
	protected void openDoor(Byte boardNo) throws InterruptedException {
		putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.OPENDOOR, new byte[] {( 0x00 )}));
	}
	
	/**
	 * @description 伺服驱动器 
	 * @param boardNo 栈号
	 * @param height 高度
	 * @param speeds 参数列表, params[0]速度,默认2
	 * @throws InterruptedException
	 */
	protected void servor(Byte boardNo, int height, int... speeds) throws InterruptedException {
		if (speeds != null && speeds.length > 0) {
			this.speed = speeds[0];
		}
		putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.SERVOCONTROL, new byte[] { (byte) this.speed, (byte) (height / 256), (byte) (height % 256)  }));
	}
	
	/**
	 * @description 伺服驱动器复位
	 * @param boardNo 栈号
	 * @throws InterruptedException
	 */
	protected void servorRest(Byte boardNo) throws InterruptedException {
		putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.SERVORESET));
	}
	
	/**
	 * @description 马达取料
	 * @param boardNo 栈号
	 * @param lockIndex 针脚索引
	 * @param amount 取料数量
	 * @throws InterruptedException
	 */
	protected void revolve(Byte boardNo, Integer lockIndex, Integer amount) throws InterruptedException {
		putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.REVOLVE,  new byte[] { lockIndex.byteValue(),  amount.byteValue() }));
	}
	
	/**
	 * @description 校验开启状态
	 * @param data
	 */
	protected void checkOpenStatus(byte[] data) {
		Boolean flag = true;
		Message message = new Message(data);
		if (!StorageMediumPicker.mainDoorMap.isEmpty()) {
			for (Entry<Byte, Door> entry : StorageMediumPicker.mainDoorMap.get(groupBoardNo).entrySet()) {
				flag = entry.getValue().getCheck();
				if (flag) {
					break;
				}
			}
			if (flag) {
				for (Entry<Byte, Door> entry : StorageMediumPicker.mainDoorMap.get(groupBoardNo).entrySet()) {
					flag = entry.getValue().getOpen();
					if (!flag) {
						break;
					}
				}
			}
		}
		if (flag) {
			if (!StorageMediumPicker.boxStatusMap.isEmpty()) {
				for (Entry<Integer, Boolean> entry : StorageMediumPicker.boxStatusMap.get(groupBoardNo).entrySet()) {
					flag = entry.getValue();
					if (!flag) {
						break;
					}
				}
			}
			if (flag) {
				message.setCustomMsg(null);
				message.setType(ReturnMsgType.OPENED);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				receiveMessage(message);
			}
		}
	}

	/**
	 * @description 启动类
	 */
	public abstract void start();

	/**
	 * @description 接收监听数据
	 * @param data 监听到的数据
	 */
	public abstract void getListenerData(byte[] data);

	/**
	 * @description 接收消息
	 * @param message
	 * @throws BscException 异常
	 */
	public abstract void receiveMessage(Message message) throws BscException;
}