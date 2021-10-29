package com.dosth.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dosth.comm.scaner4verify.Scaner4verify;
import com.dosth.comm.scaner4verify.ScanerResponseUser4verify;

public class VerifyScanCompoment {

	public static final Logger logger = LoggerFactory.getLogger(VerifyScanCompoment.class);
	private Scaner4verify scanObj;
	private ScanerResponseUser4verify scanUser;
	private String verifyScanCOM = "";
	private boolean isConnCOMSucc = true;

	public VerifyScanCompoment() {
		scanObj = new Scaner4verify();
		scanUser = new ScanerResponseUser4verify(this, scanObj);
		try {
			logger.info("VerifyScanCompoment COM:" + verifyScanCOM);
			scanObj.openCOMPort(verifyScanCOM);
		} catch (Exception e) {
			isConnCOMSucc = false;
		}
		if (isConnCOMSucc) {
			scanObj.onLight();
		}
		scanObj.setUser(scanUser);
	}

	public VerifyScanCompoment(String verifyScanCOM) {
		this.verifyScanCOM = verifyScanCOM;
		scanObj = new Scaner4verify();
		scanUser = new ScanerResponseUser4verify(this, scanObj);
		logger.info("VerifyScanCompoment COM:" + verifyScanCOM);
		scanObj.openCOMPort(verifyScanCOM);
		scanObj.setUser(scanUser);
		logger.info("Roc Enter VerifyScanCompoment(),COM:" + verifyScanCOM);
	}

	/**
	 * @description 扫描仪开始解码（等待用户扫描），同时检测扫描是否完成线程启动
	 */
	public synchronized void startMonitorScan() {
		logger.info("扫描仪开始解码（等待用户扫描）");
		scanObj.startDecode();
	}

	/**
	 * @description 扫描仪停止解码
	 */
	public synchronized void stopMonitorScan() {
		logger.info("Roc Enter VerifyScanCompoment.stopMonitorScan()");
		scanObj.stopDecode();
	}

	public void connCOM(String COM) {
		logger.info("Roc Enter VerifyScanCompoment.connCOM(),COM: " + COM);
		disconnCOM();
		scanObj.openCOMPort(verifyScanCOM);
	}

	public void disconnCOM() {
		logger.info("Roc Enter VerifyScanCompoment.disconnCOM()");
		scanObj.closeCOMPort();
	}

	public void setMonitoringMode(boolean isKeepMonitoringMode) {
		logger.info("Roc Enter VerifyScanCompoment.setMonitoringMode(),isKeepMonitoringMode:" + isKeepMonitoringMode);
		scanObj.setMonitoringMode(isKeepMonitoringMode);
	}

	public String processQRStr(String QR_Str) {
		String feedback = "";
		return feedback;
	}
}