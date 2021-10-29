package com.dosth.comm.scaner4verify;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scaner4verify {
	
	private static final Logger logger = LoggerFactory.getLogger(Scaner4verify.class);
	
	private static Scaner4verify scaner=new Scaner4verify();
	
	private Vapi sapi = new Vapi();
	private ProcessScanerResponse4verify scanerResponseUser;
	private boolean isKeepMonitoringMode = false;
	
	public Scaner4verify() {
		
	}
	
	public void setUser(ProcessScanerResponse4verify user) {
		scanerResponseUser = user;
	}
	
	public static Scaner4verify getScanerInstance(){
        return scaner;
    }
	
	// open COM
	public void openCOMPort(String comPort) {
		sapi.vbarClose();
		if (sapi.vbarOpen(comPort)) {
			// 配置一下串口
			if (sapi.vbarSetserial("115200-8-N-1")) {
				logger.info("配置串口成功 ");
			} else {
				logger.error("配置串口失败 ");
			}
		} else {
			logger.error("连接设备失败 ");
		}
	}

	// close COM
	public void closeCOMPort() {
		sapi.vbarClose();
	}
	
	// start decode
	public void startDecode() {
		logger.info("Enter start decode");
		startdecodeThread();
	}
	
	//
	public void stopDecode() {
		logger.info("Enter stop decode");
		stopdecodeThread();
	}
	
	//
	public void offLight() {
		sapi.vbarBacklight(false);
	}
	
	public void onLight() {
		sapi.vbarBacklight(true);
	}
	//
	class Devices implements Runnable {
		public void run() {
			while (true) {
				String decode = null;
				try {
					decode = sapi.vbarScan();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (decode != null) {
					sapi.vbarBeep((byte) 1);
					logger.info(decode);
					if (scanerResponseUser != null)
						scanerResponseUser.processScanerResponse(decode);
				}
			}
		}
	}


	Devices devices = new Devices();
	Thread device;

	private void startdecodeThread() {
		device = new Thread(devices);
		device.start();
	}
	
	@SuppressWarnings("deprecation")
	private void stopdecodeThread() {
		if (device.isAlive()) {
			device.stop();
		}
	}
	
	public void setMonitoringMode(boolean isKeepMonitoringMode) {
		this.isKeepMonitoringMode = isKeepMonitoringMode;
	}
	
	public boolean isKeepMonitoringMode() {
		return isKeepMonitoringMode;
	}
}