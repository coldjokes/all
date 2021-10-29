package com.dosth.toolcabinet.commu.scaner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.audio.MP3Player;
import com.dosth.comm.plc.PLCUtil;
import com.dosth.toolcabinet.commu.ReturnScanCompoment;
import com.dosth.toolcabinet.util.ReturnBackUtil;

//这个类处理归还扫描仪扫出的内容
public class ScanerResponseUser implements ProcessScanerResponse {
	public static final Logger logger = LoggerFactory.getLogger(ScanerResponseUser.class);
	private Scaner scanObj;
	private volatile String accountID;
	private ReturnScanCompoment scom;
	private volatile boolean isScaned = false;
	
	public ScanerResponseUser(ReturnScanCompoment scom,Scaner scanObj) {
		this.scanObj = scanObj;
		this.scom = scom;
	}

	/**
	 * @description 设置用户ID
	 * @param accountID 柜子ID
	 */
	public synchronized void setAccountID(String accountID) {
		
		this.accountID = accountID;
	}
	
	public synchronized boolean isScanSucc() {
		return isScaned;
	}
	
	public synchronized void setScaStas(boolean isScaned) {
		 this.isScaned = isScaned;
	}
	
	/**
	 * @description 处理扫描仪扫出的二维码（字符串）
	 * @param response 扫描仪扫出的二维码（字符串）
     * 		  response format: matUseBillId;returnTypeId;backNum;brokenNum;loseNum;accountId
     * new response format: 
     * matUseBillId;returnTypeId;loseNum;brokenNum;backNum;grindingNum;wrongCollarNum;continuedUseNum;accountId;isGetNewOne;matInfoId
	 */
	@Override
	public synchronized String processScanerResponse(String response) {
		response = response.trim();
		String matUseBillId = response;
		String qrCached = scom.getQRByBillId(matUseBillId);
		if (qrCached != null && !"".equals(qrCached)) {
			scom.removeQRByBillId(matUseBillId);
		}
		if (!scanObj.isKeepMonitoringMode()) {
			String returnTypeId = parseScanStr(qrCached, 1);
			String accountId = parseScanStr(qrCached, 2);
			boolean isVerifySucc = false;
			try {
				if (accountId.equalsIgnoreCase(this.accountID)) {
					ReturnBackUtil.putReturnBackInfo(qrCached);
					isVerifySucc = true;
					MP3Player.play("AUDIO_C2.mp3");
					setScaStas(true);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("设置队列信息异常：" + e.getMessage());
			}
			if(isVerifySucc) {
				boolean isGetTheSameOneAsReturned = parseScanStr(qrCached, 3) != null && "1".equals(parseScanStr(qrCached, 3));
				if (isGetTheSameOneAsReturned) {
					if (!scom.isNonReturning()) {
						logger.warn("cabinet(lift) is being used, please wait");
						MP3Player.play("AUDIO_C6.mp3");
						return response;
					}
					int resToPLC = 3; // value 3 for 修磨
					if (returnTypeId != null && "101".equals(returnTypeId)) {
						resToPLC = 2;
					}
					PLCUtil.setScanRes(resToPLC);
					// 以旧换新
					if (!"".equals(qrCached)) {
						String matId = parseScanStr(qrCached, 4);
						triggerGetTheSameOneAsReturned(accountId, matId);
					} else {
						logger.warn("Roc Enter ScanerResponseUser.processScanerResponse(),cached QR is empty");
					}
				} else {
					int resToPLC = 3; // value 3 for 修磨
					if (returnTypeId != null && "101".equals(returnTypeId)) {
						resToPLC = 2;
					}
					PLCUtil.setScanRes(resToPLC); // write scan res to PLC register,in the condition of PLCUtil has been inited
				}
			} else {
				MP3Player.play("AUDIO_C3.mp3");
			}
			scom.stopMonitorScan();
		} else {
			logger.warn("Roc Enter ScanerResponseUser.processScanerResponse(),Enter ScanerResponseUser.returns_verify_mode");
		}
		return response;
	}
	
	private void triggerGetTheSameOneAsReturned(String accountId,String MatId) {
		scom.getTheSameOneAsReturned(accountId, MatId);
		logger.warn("Roc ScanerResponseUser.triggerGetTheSameOneAsReturned()");
	}
	
	/**
	 * @description 解析扫描字符串信息
	 * @param response 扫描字符串信息
	 * @param index 读取索引
	 * @return
	 */
	private String parseScanStr(String response, int index) {
		String[] tokens = response.split(";");
		return tokens.length > index ? tokens[index] : "";
	}
}