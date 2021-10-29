package com.cnbaosi.scanner;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.SerialComm;
import com.cnbaosi.scanner.enums.ScanCodeTypeEnum;
import com.cnbaosi.scanner.enums.ScanNfcType;
import com.cnbaosi.util.SerialTool;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @description 扫描NFC二合一
 * @author guozhidong
 *
 */
public abstract class ScanNfcCompound extends SerialComm {

	private static final Logger logger = LoggerFactory.getLogger(ScanNfcCompound.class);

	private Vapi vapi = new Vapi();

	// 扫描NFC类型
	private ScanNfcType scanNfcType;
	// 常开状态
	private Boolean alwaysOpen = false;
	// 超时时间
	private int timeout = 60;
	// 接收标识
	private Boolean receiveFlag;

	/**
	 * @description 扫描NFC二合一构造方法
	 * @param scanNfcType 扫描NFC设备类型
	 * @param alwaysOpen  是否常开
	 */
	public ScanNfcCompound(ScanNfcType scanNfcType, Boolean alwaysOpen) {
		this.scanNfcType = scanNfcType;
		this.alwaysOpen = alwaysOpen;
	}

	/**
	 * @description 启动扫描NFC设备
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @param params   参数,params[0]超时时间,默认60s
	 * @throws Exception
	 */
	public void start(String portName, int baudrate, int... params) throws Exception {
		if (params != null && params.length > 0) {
			timeout = params[0];
		}
		switch (scanNfcType) {
		case DWSN:
		case DWS:
			this.startDw(portName, baudrate);
			break;
		case WGS:
		case WGSN:
			this.startWg(portName, baudrate);
			break;
		default:
			break;
		}
	}

	/**
	 * @description 启动德沃
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @throws Exception
	 */
	private void startWg(String portName, int baudrate) throws Exception {
		logger.info("启动微光互联,串口:" + portName);
		Thread.sleep(2000);
		receiveFlag = false;
		if (vapi.vbarOpen(portName)) {
			// 设置设备参数
			vapi.vbarSetserial(baudrate + "-8-N-1");
			new Thread(new Runnable() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					while (true) {
						String result = null;
						try {
							result = vapi.vbarScan();
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						vapi.vbarBacklight(true); // 先开灯
						if (result != null) {
							vapi.vbarBeep((byte) 1); // 调用解码响一声
							vapi.vbarBacklight(false);
							if (result.indexOf(ScanCodeTypeEnum.NFC.getCode()) != -1) { // NFC
								receiveNfcResult(result.trim().replace(ScanCodeTypeEnum.NFC.getCode(), ""));
								receiveFlag = true;
							} else { // 扫码
								receiveScanResult(result.trim().replace(ScanCodeTypeEnum.APP.getCode(), ""));
								receiveFlag = true;
							}
						}
						if (!alwaysOpen && (System.currentTimeMillis() - start > timeout * 1000 || receiveFlag)) {
							logger.info("非常开模式,已接收或超时关闭串口");
							vapi.vbarBacklight(false);
							vapi.vbarClose();
							if (!receiveFlag) {
								timeout();
							}
							break;
						}
						try {
							Thread.sleep(500);
							logger.info("等待接收信息");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		} else {
			throw new Exception("串口打开失败");
		}
	}

	/**
	 * @description 启动德沃
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @throws Exception
	 */
	private void startDw(String portName, int baudrate) throws Exception {
		logger.info("启动德沃");
		startSerialPort(portName, baudrate);
		serialPort.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				switch (serialPortEvent.getEventType()) {
				case SerialPortEvent.BI: // 10 通讯中断
					logger.error("与串口设备通讯中断");
					break;
				case SerialPortEvent.OE: // 7 溢位（溢出）错误
				case SerialPortEvent.FE: // 9 帧错误
				case SerialPortEvent.PE: // 8 奇偶校验错误
				case SerialPortEvent.CD: // 6 载波检测
				case SerialPortEvent.CTS: // 3 清除待发送数据
				case SerialPortEvent.DSR: // 4 待发送数据准备好了
				case SerialPortEvent.RI: // 5 振铃指示
				case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
					break;
				case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
					if (serialPort == null) {
						logger.error("串口对象为空！监听失败！");
					} else {
						try {
							byte[] data = SerialTool.readFromPort(serialPort);
							if (data.length > 1 && data[0] == 0x23 && data[data.length - 1] == 0x23) { // NFC
								String cardNo = new BigInteger(new String(data).substring(1, data.length - 1), 16).toString(10);
								// 卡号不足10位长,前面用0填满
								while (cardNo.length() < 10) {
									cardNo = "0" + cardNo;
								}
								receiveNfcResult(cardNo);
								receiveFlag = true;
							} else { // SCAN
								receiveScanResult(new String(data));
								receiveFlag = true;
							}
							receiveFlag = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				default:
					break;
				}
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				receiveFlag = false;
				while (!alwaysOpen) {
					if (System.currentTimeMillis() - start > timeout * 1000 || receiveFlag) {
						logger.info("非常开模式,已接收或超时关闭串口");
						serialPort.close();
						if (!receiveFlag) {
							timeout();
						}
						break;
					}
					try {
						Thread.sleep(500);
						logger.info("等待接收信息");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * @description 接收扫描结果
	 * @param result 扫描结果
	 */
	public abstract void receiveScanResult(String result);

	/**
	 * @description 接收扫描结果
	 * @param result 扫描结果
	 */
	public abstract void receiveNfcResult(String result);
	
	/**
	 * @description 超时回调方法
	 */
	public abstract void timeout();
}