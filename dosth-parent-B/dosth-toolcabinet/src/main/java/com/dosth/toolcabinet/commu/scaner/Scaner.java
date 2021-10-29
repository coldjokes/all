package com.dosth.toolcabinet.commu.scaner;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//扫描仪类，解码在这里做
public class Scaner {
	public static final Logger logger = LoggerFactory.getLogger(Scaner.class);
	private Vapi sapi = new Vapi();
	private ProcessScanerResponse scanerResponseUser;
	private boolean isKeepMonitoringMode = false;

	public Scaner() {
	}

	/**
	 * @description 设置需要扫描内容的对象（处理扫描出的二维码）
	 * @param user
	 */
	public void setUser(ProcessScanerResponse user) {
		logger.info("构造扫描仪对象");
		scanerResponseUser = user;
	}

	/**
	 * @description 打开连接扫描仪的串口
	 * @param comPort 串口号
	 */
	public boolean openCOMPort(String comPort) {
		return this.openCOMPort(comPort, "115200-8-N-1");
	}
	public boolean openCOMPort(String comPort, String params) {
		sapi.vbarClose();
		
		boolean openResult = sapi.vbarOpen(comPort);
		boolean setParamsResult = false;
		if (openResult) {
			logger.warn("扫描仪设备" + comPort + "初始化成功");
			// 配置串口
			setParamsResult = sapi.vbarSetserial(params);
			if (setParamsResult) {
				logger.info("扫描仪设备配置串口" + comPort + "成功");
			} else {
				logger.error("扫描仪设备配置串口" + comPort + "失败");
			}
		} else {
			logger.error("启动扫描仪设备串口" + comPort + "失败");
		}
		
		return openResult && setParamsResult;
	}

	/**
	 * @description 关闭扫描仪的串口
	 */
	public void closeCOMPort() {
		logger.info("关闭串口");
		sapi.vbarClose();
	}

	/**
	 * @description 启动解码
	 */
	public synchronized void startDecode() {
		logger.info("开灯并启动扫描线程");
		onLight();
		if (stopFlag) {
			startdecodeThread();
		}
	}

	/**
	 * @description 关闭解码
	 */
	public synchronized void stopDecode() {
		logger.info("扫描仪停止解码，并关灯");
		stopdecodeThread();
		offLight();
	}

	/**
	 * @description 关闭扫描仪内的灯
	 */
	public synchronized void offLight() {
		logger.info("扫描仪关灯");
		sapi.vbarBacklight(false);
	}

	/**
	 * @description 打开扫描仪内的灯
	 */
	public synchronized void onLight() {
		logger.info("扫描仪开灯");
		sapi.vbarBacklight(true);
	}

	/**
	 * @description 扫描解码线程
	 */
	public boolean stopFlag = true;

	class Devices implements Runnable {
		public void run() {
			logger.info("扫描仪启动线程，停止状态：" + stopFlag);
			while (!stopFlag) {
				String decode = null;
				try {
					decode = sapi.vbarScan();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				if (decode != null) {
					sapi.vbarBeep((byte) 1);
					if (scanerResponseUser != null) {
						scanerResponseUser.processScanerResponse(decode);
					}
				}
			}
		}
	}

	Devices devices = new Devices();
	Thread device;

	/**
	 * @description 启动扫描解码线程（扫描仪扫描作为线程运行）
	 */
	private void startdecodeThread() {
		logger.info("扫描仪启动扫描线程");
		device = new Thread(devices);
		stopFlag = false;
		device.start();
	}

	/**
	 * @description 关闭扫描解码线程
	 */
	private void stopdecodeThread() {
		logger.info("进入扫描线程停止，设置停止标识为true");
		if (device != null && device.isAlive()) {
			stopFlag = true;
		}
	}

	/**
	 * @description 设置扫描仪模式：扫描到一次就关闭 或者 一直处在ready（待扫描状态）
	 */
	public void setMonitoringMode(boolean isKeepMonitoringMode) {
		this.isKeepMonitoringMode = isKeepMonitoringMode;
	}

	/**
	 * @description 获得扫描仪状态
	 * @return 扫描仪模式：扫描到一次就关闭 或者 一直处在ready（待扫描状态）
	 */
	public boolean isKeepMonitoringMode() {
		return isKeepMonitoringMode;
	}
}