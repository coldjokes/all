package com.dosth.comm.scaner4verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.VerifyScanCompoment;

public class ScanerResponseUser4verify implements ProcessScanerResponse4verify {

	private static final Logger logger = LoggerFactory.getLogger(ScanerResponseUser4verify.class);
	private Scaner4verify scanObj;
	private VerifyScanCompoment verifyScanCompoment;
	
	public ScanerResponseUser4verify(VerifyScanCompoment verifyScanCompoment,Scaner4verify scanObj) {
		this.scanObj = scanObj;
		this.verifyScanCompoment = verifyScanCompoment;
	}

	@Override
	public synchronized String processScanerResponse(String response) {
		logger.info(this+" Process Scaner4verify Response "+response);
		response = response.trim();
		logger.info(this+" Process Scaner4verify Response trimed "+response);
		logger.warn("Roc ScanerResponseUser4verify.processScanerResponse(),Response trimed "+response);
		if(!scanObj.isKeepMonitoringMode()) {
			logger.info("Enter ScanerResponseUser4verify.recycle_cabinet_mode");
			verifyScanCompoment.processQRStr(response);
			scanObj.stopDecode();
		}
		else {
			logger.info("Enter ScanerResponseUser4verify.returns_verify_mode");
			verifyScanCompoment.processQRStr(response);
		}
		return response;
	}
}
