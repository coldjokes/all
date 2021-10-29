package com.dosth.toolcabinet.commu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.Compoment;
import com.dosth.comm.Mediator;
import com.dosth.comm.audio.MP3Player;
import com.dosth.toolcabinet.commu.scaner.Scaner;
import com.dosth.toolcabinet.commu.scaner.ScanerResponseUser;
import com.dosth.toolcabinet.dto.BorrowInfo;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.toolcabinet.util.BorrowAgainUtil;

import javafx.util.Pair;

public class ReturnScanCompoment extends Compoment {

	private static final Logger logger = LoggerFactory.getLogger(ReturnScanCompoment.class);
	private ToolService toolService = null;
	private String cabinetID = "";
	private Scaner scanObj;
	private ScanerResponseUser scanUser;
	private String ReturnScanCOM = "";
	private boolean isConnCOMSucc = true;
	private volatile boolean isAlreadyPut = false; // flag,indicate user has already put into when he is returning.
	private int Put_Timeout = 300000; // 5 minutes
	private int warnning_audio_interval = 30;
	// billId:QR_Str pair
	private List<Pair<String, String>> qrCache = Collections.synchronizedList(new ArrayList<Pair<String, String>>());

	public ReturnScanCompoment() {
		scanObj = new Scaner();
		scanUser = new ScanerResponseUser(this, scanObj);

		try {
			scanObj.openCOMPort(ReturnScanCOM);
		} catch (Exception e) {
			isConnCOMSucc = false;
		}
		if (isConnCOMSucc) {
			scanObj.onLight();
		}
		scanObj.setUser(scanUser);
	}

	public ReturnScanCompoment(Mediator mediator, String ReturnScanCOM) {
		super(mediator);
		this.ReturnScanCOM = ReturnScanCOM;
		scanObj = new Scaner();
		scanUser = new ScanerResponseUser(this, scanObj);
		scanObj.openCOMPort(ReturnScanCOM);
		scanObj.setUser(scanUser);
	}

	/**
	 * @description 扫描仪开始解码（等待用户扫描），同时检测扫描是否完成线程启动
	 */
	public synchronized void startMonitorScan() {
		isAlreadyPut = false;
		scanUser.setScaStas(false);
		scanObj.startDecode();
		startWarnningThread();
		MP3Player.play("AUDIO_C1.mp3");
	}

	/**
	 * @description 扫描仪停止解码
	 */
	public synchronized void stopMonitorScan() {
		isAlreadyPut = true;
		scanObj.stopDecode();
	}

	public void connCOM(String COM) {
		disconnCOM();
		scanObj.openCOMPort(ReturnScanCOM);
	}

	public void disconnCOM() {
		scanObj.closeCOMPort();
	}

	public void setMonitoringMode(boolean isKeepMonitoringMode) {
		scanObj.setMonitoringMode(isKeepMonitoringMode);
	}

	public synchronized void setAccountID(String accountID) {
		scanUser.setAccountID(accountID);
	}

	private void myWait(int millSec) {
		try {
			Thread.sleep(millSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description 检测扫描是否完成线程 1.5分钟内未归还（未扫描到）结束此次归还操作 2.5分钟内没隔30秒一次语音提示
	 */
	class WarnningRunnable implements Runnable {
		public void run() {
			myWait(1000);
			long dur = Put_Timeout;
			long start = System.currentTimeMillis();
			long now = System.currentTimeMillis();
			boolean timeoutFlag = false;
			int passed_internal_count = 1;
			while (!isAlreadyPut) {
				now = System.currentTimeMillis();
				if ((now - start) > dur) {
					timeoutFlag = true;
					break;
				}
				int second = (int) (now - start) / 1000;
				if ((now - start) > passed_internal_count * warnning_audio_interval * 1000
						&& second % warnning_audio_interval == 0) {
					passed_internal_count++;
					logger.info("Play Audio");
					if (!scanUser.isScanSucc()) {
						MP3Player.play("AUDIO_C4.mp3");
					}
				}
			}
			if (timeoutFlag) {
				stopMonitorScan();
				MP3Player.play("AUDIO_C5.mp3");
			}
		}
	}

	/**
	 * @description 启动检测扫描是否完成线程
	 */
	public synchronized void startWarnningThread() {
		new Thread(new WarnningRunnable()).start();
	}

	/**
	 * @description 设置柜子ID
	 * @param cabinetID 柜子ID
	 */
	public synchronized void setCabinetID(String cabinetID) {
		this.cabinetID = cabinetID;
	}

	/**
	 * @description 柜子是否有人在使用（料斗，马达弹簧在使用中）
	 * @return true 没有用户使用，ready状态
	 */
	public synchronized boolean isNonReturning() {
		boolean flag = ((ConcreteMediator) mediator).isCurrShoppingCartTakenOut();
		return flag;
	}

	public synchronized void getTheSameOneAsReturned(String accountId, String MatId) {
		if (toolService == null) {
			logger.error("getTheSameOneAsReturned, toolService is null");
		}
		List<CartInfo> cartList = new ArrayList<>();
		String receiveType = "GETNEWONE";
		String receiveInfo = "以旧换新";
		cartList.add(new CartInfo(1, "PACK", receiveType, receiveInfo, MatId));
		BorrowInfo borrowInfo = new BorrowInfo(cabinetID, cartList, accountId);
		try {
			BorrowAgainUtil.putBorrowInfo(borrowInfo);
		} catch (InterruptedException e) {
			logger.error("putBorrowInfo exception:" + e.getMessage());
		}
	}

	/**
	 * @description set ToolService对象供后面使用
	 */
	public synchronized void setToolService(ToolService toolService) {
		this.toolService = toolService;
	}

	public synchronized void addQR2Cache(Pair<String, String> qrPair) {
		qrCache.add(qrPair);
	}

	public synchronized String getQRByBillId(String billId) {
		if (billId == null || "".equals(billId)) {
			return "";
		}
		for (Pair<String, String> qrPair : qrCache) {
			if (qrPair.getKey().equalsIgnoreCase(billId)) {
				return qrPair.getValue();
			}
		}
		return "";
	}

	public synchronized boolean removeQRByBillId(String billId) {
		boolean flag = false;
		for (Pair<String, String> qrPair : qrCache) {
			if (qrPair.getKey().equalsIgnoreCase(billId)) {
				qrCache.remove(qrPair);
				flag = true;
				break;
			}
		}
		return flag;
	}
}