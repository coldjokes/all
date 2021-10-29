package com.dosth.comm.lockboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.IOReadWrite;
import com.dosth.comm.ReadSerialPort;
import com.dosth.comm.audio.AudioSegments;
import com.dosth.comm.audio.MP3Player;
import com.dosth.comm.plc.PLCUtil;
import com.dosth.util.SerialTool;

import javafx.util.Pair;

public class LockReaderWriter implements IOReadWrite {

	public static final Logger logger = LoggerFactory.getLogger(LockReaderWriter.class);
	private ReadSerialPort sp;

	private volatile String currCMD = "";
	private volatile boolean isCurrCMDSucc = false;
	private volatile String errMsg = "";
	private volatile boolean isOneHandShakeDone = true;
	private volatile boolean iscurrLockLocked = true;
	private volatile int currQueryLockerIndex = 0;
	// Pair<Integer, Long> lockindex:unlock_time pair
	private volatile Stack<Pair<Integer, Long>> queryLocksStack = new Stack<Pair<Integer, Long>>();
	private volatile boolean isStopQuery = false;
	private volatile List<Integer> slinkDrawerList = new ArrayList<Integer>();

	private volatile boolean isBoard1Unlocked = false;
	private volatile boolean isBoard2Unlocked = false;

	private final String UNLOCK_ONE = "UNLOCK_ONE";
	private final String QUERY_ONE = "QUERY_ONE";
	private final String UNLOCK_ALL = "UNLOCK_ALL";

	private int boardAddr = 0;
	private int lockIndex = 0;

	private static int QueryTimeout = 15;
	private static int UserLockActionTimeout = 120;
	private static int WarnningAudioInternal = 30;

//	private boolean isConnCOMSucc = true;

	private String comm;

	private COMService service = new COMService();

	public LockReaderWriter(String comm) {
		this.comm = comm;
		sp = new ReadSerialPort(this);
		try {
			sp.listPort();
			sp.selectPort(this.comm);
			sp.startRead(0);
			sp.setEndMark(false);
		} catch (RuntimeException e) {
			e.printStackTrace();
//			isConnCOMSucc = false;
		}

	}

	private byte[] getUnlockOneData(int boardAddr, int lockIndex) {

		// 57 4B 4C 59 09 00 82 01 83

		final int FRAME_LEN = 4;
		final int BOARD_ADDR_INDEX = 5;
		final int CMD_INDEX = 6;
		final int LOCK_INDEC = 7;

		byte[] head = { 0x57, 0x4B, 0x4C, 0x59 };
		byte frame_len = 0x09;
		byte board_addr = (byte) boardAddr;
		byte cmd = -0x7E; // 0x82:-0x7E
		byte lock = (byte) lockIndex;

		byte[] cmdData = new byte[(int) frame_len];
		System.arraycopy(head, 0, cmdData, 0, head.length);
		cmdData[FRAME_LEN] = frame_len;
		cmdData[BOARD_ADDR_INDEX] = board_addr;
		cmdData[CMD_INDEX] = cmd;
		cmdData[LOCK_INDEC] = lock;

		int crcIndex = (int) frame_len - 1;
		byte[] crcInput = new byte[(int) frame_len - 1];
		for (int i = 0; i < crcInput.length; i++) {
			crcInput[i] = cmdData[i];
		}
		cmdData[crcIndex] = getCRC(crcInput);

		return cmdData;
	}

	private byte[] getUnlockAllData(int boardAddr) {

		// 57 4B 4C 59 08 00 86 87

		final int FRAME_LEN = 4;
		final int BOARD_ADDR_INDEX = 5;
		final int CMD_INDEX = 6;

		byte[] head = { 0x57, 0x4B, 0x4C, 0x59 };
		byte frame_len = 0x08;
		byte board_addr = (byte) boardAddr;
		byte cmd = -0x7A; // 0x86:-0x7A

		//
		byte[] cmdData = new byte[(int) frame_len];
		System.arraycopy(head, 0, cmdData, 0, head.length);
		cmdData[FRAME_LEN] = frame_len;
		cmdData[BOARD_ADDR_INDEX] = board_addr;
		cmdData[CMD_INDEX] = cmd;

		int crcIndex = (int) frame_len - 1;
		byte[] crcInput = new byte[(int) frame_len - 1];
		for (int i = 0; i < crcInput.length; i++) {
			crcInput[i] = cmdData[i];
		}
		cmdData[crcIndex] = getCRC(crcInput);

		return cmdData;
	}

	//
	private byte[] getQueryStasCmdData(int boardAddr, int lockIndex) {

		// 57 4B 4C 59 09 00 83 01 82

		final int FRAME_LEN = 4;
		final int BOARD_ADDR_INDEX = 5;
		final int CMD_INDEX = 6;
		final int LOCK_INDEC = 7;

		byte[] head = { 0x57, 0x4B, 0x4C, 0x59 };
		byte frame_len = 0x09;
		byte board_addr = (byte) boardAddr;
		byte cmd = -0x7D; // 0x83:-0x7D
		byte lock = (byte) lockIndex;

		byte[] cmdData = new byte[(int) frame_len];
		System.arraycopy(head, 0, cmdData, 0, head.length);
		cmdData[FRAME_LEN] = frame_len;
		cmdData[BOARD_ADDR_INDEX] = board_addr;
		cmdData[CMD_INDEX] = cmd;
		cmdData[LOCK_INDEC] = lock;

		int crcIndex = (int) frame_len - 1;
		byte[] crcInput = new byte[(int) frame_len - 1];
		for (int i = 0; i < crcInput.length; i++) {
			crcInput[i] = cmdData[i];
		}
		cmdData[crcIndex] = getCRC(crcInput);

		return cmdData;
	}

	/**
	 * @description unlock the door
	 * @param lockIndex 电子锁索引号
	 */
	public synchronized boolean unlock(int lockIndex) {
		logger.warn("Roc LockReaderWriter.unlock(),lockIndex " + lockIndex);
		boolean isSuccUnlock = false;
		isStopQuery = true;
		try {
			// 计算栈号
			// 如果是刚好50的倍数,就是当前栈号的最后一位
			int boardIndex = lockIndex % 50 == 0 ? (lockIndex - 1) / 50 : lockIndex / 50;
			int lockIndexOnBoard = lockIndex % 50; // 默认按50针脚排列
			if (lockIndex % 50 == 0) { // 如果索引为是50的倍数,就是当前栈号板的50位
				lockIndexOnBoard = 50;
			}
			logger.warn("Roc LockReaderWriter.unlock(),boardIndex,lockIndexOnBoard: " + boardIndex + ","
					+ lockIndexOnBoard);
			isSuccUnlock = unlock(boardIndex, lockIndexOnBoard);

		} catch (RuntimeException e) {
			e.printStackTrace();
		}

		if (isSuccUnlock) {
			if (slinkDrawerList.size() == 0)
				PLCUtil.turnOnTempoCabinetLight();

			currQueryLockerIndex = lockIndex;
			Pair<Integer, Long> lockIndex2UnlockTimePair = new Pair<>(new Integer(lockIndex),
					new Long(System.currentTimeMillis()));
			queryLocksStack.push(lockIndex2UnlockTimePair);
			logger.warn("queryLocksStack.push() " + currQueryLockerIndex + " ,time" + (new Date()).toString());
			new COMServiceThread(service).start();

		}
		isStopQuery = false;
		return isSuccUnlock;
	}

	/**
	 * @description query the the door status,locked or unlocked
	 */
	public synchronized boolean isLocked(int lockIndex) {

		myWait(100);

		boolean flag = true;

		this.lockIndex = lockIndex;
		// 计算栈号
		// 如果是刚好50的倍数,就是当前栈号的最后一位
		int boardIndex = lockIndex % 50 == 0 ? (lockIndex - 1) / 50 : lockIndex / 50;
		int lockIndexOnBoard = lockIndex % 50; // 默认按50针脚排列
		if (lockIndex % 50 == 0) { // 如果索引为是50的倍数,就是当前栈号板的50位
			lockIndexOnBoard = 50;
		}

		flag = isLocked(boardIndex, lockIndexOnBoard);

		return flag;
	}

	/**
	 * @description unlock all the doors
	 */
	public boolean unlockAll() {
		isBoard1Unlocked = unlockAll(0);
		logger.warn("Board1 is unlocked? " + isBoard1Unlocked);
		isBoard2Unlocked = unlockAll(1);
		logger.warn("Board2 is unlocked? " + isBoard2Unlocked);
		logger.warn("Both the 2 Boards are unlocked? " + (isBoard1Unlocked && isBoard2Unlocked));
		return isBoard1Unlocked && isBoard2Unlocked;
	}

	private synchronized boolean unlock(int boardAddr, int lockIndex) {

		boolean isRunWell = true;
		boolean isNotTimeout = true;

		int lockIndexOnBoard = lockIndex;
		byte[] data = getUnlockOneData(boardAddr, lockIndexOnBoard);
		this.write(data);

		currCMD = UNLOCK_ONE;
		isOneHandShakeDone = false;

		long dur = QueryTimeout * 1000;
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		while (!isOneHandShakeDone) {
//			logger.warn("Querying...");
			now = System.currentTimeMillis();
			if ((now - start) > dur) {
				errMsg = currCMD + " timeout";
				isNotTimeout = false;
				break;
			}
		}
		currCMD = "";
		if (errMsg != "")
			logger.warn(errMsg);

		if (isNotTimeout && isCurrCMDSucc)
			isRunWell = true;
		else
			isRunWell = false;

		logger.warn("Run Unlcok " + (50 * boardAddr + lockIndex) + " cmd is succ? " + isRunWell);
		isOneHandShakeDone = true;
		return isRunWell;
	}

	private boolean unlockAll(int boardAddr) {

		boolean isRunWell = true;
		boolean isNotTimeout = true;

		byte[] data = getUnlockAllData(boardAddr);
		this.write(data);

		currCMD = UNLOCK_ALL;
		isOneHandShakeDone = false;

		long dur = QueryTimeout * 1000;
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		while (!isOneHandShakeDone) {
//			logger.warn("Querying...");
			now = System.currentTimeMillis();
			if ((now - start) > dur) {
				errMsg = currCMD + " timeout";
				isNotTimeout = false;
				break;
			}
		}
		currCMD = "";
		if (errMsg != "")
			logger.warn(errMsg);

		if (isNotTimeout && isCurrCMDSucc)
			isRunWell = true;
		else
			isRunWell = false;

		logger.warn("Run Unlcok one board cmd is succ? " + isRunWell);

		return isRunWell;

	}

	private synchronized boolean isLocked(int boardAddr, int lockIndex) {

		byte[] data = getQueryStasCmdData(boardAddr, lockIndex);
		currCMD = QUERY_ONE;
		isOneHandShakeDone = false;
		this.write(data);

		long dur = QueryTimeout * 1000;
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		while (!isOneHandShakeDone) {
//			logger.warn("Querying...");
			now = System.currentTimeMillis();
			if ((now - start) > dur) {
				errMsg = currCMD + " timeout";
				break;
			}
		}
		currCMD = "";
		if (errMsg != "")
			logger.warn(errMsg);

		isOneHandShakeDone = true;
		return iscurrLockLocked;
	}

	@Override
	public void write(byte[] data) {

		this.isCurrCMDSucc = false;
		sp.write(data);
	}

	@Override
	public void processReadData(byte[] data) {

		String readStr = SerialTool.bytesToHexString(data);
		logger.warn("LockReaderWriter 接收到端口返回数据(长度为" + readStr.length() / 2 + " byte)：" + readStr);

		if (currCMD != "")
			parseICResponse(data);

	}

	private void parseICResponse(byte[] data) {
		logger.warn("currCMD:" + currCMD);
		switch (currCMD) {
		case QUERY_ONE: {
			// response 57 4B 4C 59 0B 00 83 00 01 00 80
			int cmdIndx = 6;
			int cmd_status_index = 7;
			int lock_status_index = 9;
			byte status = data[cmd_status_index];
			byte cmd = data[cmdIndx];
			byte cmdByte = -125;
			logger.warn("cmd:" + cmd);
			if (cmd != cmdByte)
				break;
			isCurrCMDSucc = (status == 0) ? true : false;
			isOneHandShakeDone = true;
			if (isCurrCMDSucc) {
				errMsg = "";
				byte lock_status = data[lock_status_index];
				if (lock_status == 0)
					iscurrLockLocked = false;
				else if (lock_status == 1)
					iscurrLockLocked = true;
			} else
				errMsg = QUERY_ONE;
			break;
		}
		// UNLOCK_ONE
		case UNLOCK_ONE: {
			// response 57 4B 4C 59 0B 00 82 00 01 00 81
			int cmdIndx = 6;
			int cmd_status_index = 7;
			int lock_status_index = 9;
			byte status = data[cmd_status_index];
			byte cmd = data[cmdIndx];
			byte cmdByte = -126;
			logger.warn("cmd:" + cmd);
			if (cmd != cmdByte)
				break;

			isCurrCMDSucc = (status == 0) ? true : false;
			isOneHandShakeDone = true;
			if (isCurrCMDSucc) {
				errMsg = "";
				byte lock_status = data[lock_status_index];
				if (lock_status == 0)
					iscurrLockLocked = false;
				else if (lock_status == 1)
					iscurrLockLocked = true;
			} else
				errMsg = UNLOCK_ONE;
			break;
		}
		// UNLOCK_ALL
		case UNLOCK_ALL: {
			// response 57 4B 4C 59 09 00 86 00 86
			int board_index_index = 5;
			int lock_status_index = 7;
			byte lock_status = data[lock_status_index];
			byte board_index = data[board_index_index];
			isCurrCMDSucc = (lock_status == 0) ? true : false;
			isOneHandShakeDone = true;
			if (isCurrCMDSucc) {
				errMsg = "";
				if (board_index == 0)
					isBoard1Unlocked = true;
				else if (board_index == 1)
					isBoard2Unlocked = true;
			} else {
				errMsg = UNLOCK_ALL;
				if (board_index == 0)
					isBoard1Unlocked = false;
				else if (board_index == 1)
					isBoard2Unlocked = false;
			}
			break;
		}
		}
	}

	public byte getCRC(byte[] ba) {

		byte crc = ba[0];
		for (int i = 1; i < ba.length; i++) {

			crc = (byte) (crc ^ ba[i]);
		}

		return crc;
	}

	public void close() {
		sp.close();
	}

	public void setBoardAddr(int addr) {

		boardAddr = addr;
	}

	public void setLockIndex(int index) {

		lockIndex = index;
	}

	public int getBoardAddr() {

		return boardAddr;
	}

	public int getLockIndex() {

		return lockIndex;
	}

	public boolean isRunCmdSucc() {

		return (errMsg != "") && isCurrCMDSucc;
	}

	//
	class COMService {
		private int lockIndex = 0;
		private boolean stopFlag = false;
		private Lock threadlock = new ReentrantLock();

		COMService() {
		}

		public void comService1() {
			threadlock.lock();
			Pair<Integer, Long> lockIndex2UnlockTimePair = queryLocksStack.pop();
			if (lockIndex2UnlockTimePair != null) {
				lockIndex = (lockIndex2UnlockTimePair.getKey()).intValue();
				for (int i = 0; i < 5; i++) {
					myWait(5000);
					logger.warn("ThreadName = " + Thread.currentThread().getName()
							+ (" lockindex " + lockIndex + " " + (i + 1)));
				}
			}

			threadlock.unlock();
		}

		public void comService() {
			threadlock.lock();

			//
			boolean isAlreadyLocked = false;
			boolean isAlreadySlink = false;
			Pair<Integer, Long> lockIndex2UnlockTimePair = queryLocksStack.pop();
			if (lockIndex2UnlockTimePair != null) {
				lockIndex = (lockIndex2UnlockTimePair.getKey()).intValue();
				stopFlag = false;

				logger.warn("service for lock " + lockIndex + " running...");
				while (!stopFlag) {
					long dur = UserLockActionTimeout * 1000;
					long start = lockIndex2UnlockTimePair.getValue().longValue();
					long now = System.currentTimeMillis();
					while ((now - start) < dur) {

						if (!isOneHandShakeDone || isStopQuery)
							continue;

						if (isLocked(lockIndex)) {
							PLCUtil.setModbusConfig(PLCUtil.HOST, PLCUtil.PORT, PLCUtil.SLAVE_ID);
							if (slinkDrawerList.size() == 0)
								PLCUtil.turnOffTempoCabinetLight();
							isAlreadyLocked = true;
							break;
						}
						myWait(500);
						now = System.currentTimeMillis();
					}

					if (!isAlreadyLocked) {
						long audioFirstTime = System.currentTimeMillis();
						String audioFile = AudioSegments.getLockerMappedAudio(lockIndex);
						MP3Player.play(audioFile);
						int audioTimes = 1;
						myWait(500);

						while (!isOneHandShakeDone || isStopQuery) {
							myWait(100);
						}

						if (!isLocked(lockIndex)) {
							if (!isAlreadySlink) {
								logger.warn("Set slink");
								PLCUtil.setModbusConfig(PLCUtil.HOST, PLCUtil.PORT, PLCUtil.SLAVE_ID);
								PLCUtil.slinkTempoCabinetLight();
								isAlreadySlink = true;
							}
							slinkDrawerList.add(new Integer(lockIndex));
						}
						myWait(100);
						while (!isOneHandShakeDone || isStopQuery) {
							myWait(100);
						}

						while (!isLocked(lockIndex)) {
							long audioCheckTime = System.currentTimeMillis();
							if (audioTimes < 50000
									&& (audioCheckTime - audioFirstTime) > audioTimes * WarnningAudioInternal * 1000) {
								MP3Player.play(audioFile);
								audioTimes++;
							}
						}

						isAlreadyLocked = true;
						PLCUtil.setModbusConfig(PLCUtil.HOST, PLCUtil.PORT, PLCUtil.SLAVE_ID);
						for (Integer iObj : slinkDrawerList) {
							if (iObj.intValue() == lockIndex) {
								slinkDrawerList.remove(iObj);
								break;
							}
						}
						if (slinkDrawerList.size() == 0)
							PLCUtil.turnOffTempoCabinetLight();
						stopFlag = true;

						logger.warn("service for lock " + lockIndex + " stop...");
					} else {
						logger.warn("out timeout dur,querying,then find locked");
						stopFlag = true;
						for (Integer iObj : slinkDrawerList) {
							if (iObj.intValue() == lockIndex) {
								slinkDrawerList.remove(iObj);
								break;
							}
						}
						logger.warn("service for lock " + lockIndex + " stop...");
					}
				}
			}
			//
			threadlock.unlock();
		}
	}

	class COMServiceThread extends Thread {
		private COMService service;

		public COMServiceThread(COMService service) {
			this.service = service;
		}

		@Override
		public void run() {
			logger.warn("COMServiceThread " + this.getId() + " running *****");
			service.comService();
		}
	}

	private void myWait(int millSec) {
		try {
			Thread.sleep(millSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] string) {
		PLCUtil.setModbusConfig("192.168.2.46", 502, 2); // need this

		LockReaderWriter ic = new LockReaderWriter("COM3");
		ic.unlock(1);
		ic.myWait(1000);
		ic.unlock(2);
		ic.myWait(1000);
		ic.unlock(3);
//		   byte[] data = ic.getUnlockOneData(0,1);
//		   byte[] data = ic.getQueryStasCmdData(0,1);
//		   ic.write(data);
//		   ic.isLocked(1);
//		   ic.unlock(2); 
//		   ic.isLocked(1);
//		ic.unlockAll();
	}
}