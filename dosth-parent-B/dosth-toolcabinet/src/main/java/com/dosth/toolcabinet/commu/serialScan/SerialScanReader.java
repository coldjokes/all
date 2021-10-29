package com.dosth.toolcabinet.commu.serialScan;

import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dosth.comm.NotASerialPort;
import com.dosth.comm.ReadDataFromSerialPortFailure;
import com.dosth.comm.SerialPortInputStreamCloseFailure;
import com.dosth.comm.SerialPortParameterFailure;
import com.dosth.constant.ScanCodeTypeEnum;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.util.SerialTool;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * 扫描仪串口用法
 * 
 * @author Yifeng Wang
 */
@Component
public class SerialScanReader {

	private static final Logger logger = LoggerFactory.getLogger(SerialScanReader.class);

	public static SerialPort serialPort;

	public void start() throws NoSuchPortException, PortInUseException, SerialPortParameterFailure, NotASerialPort,
			TooManyListenersException {
		serialPort = SerialTool.openPort(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.SCAN_COM),
				Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.SCAN_BAUD)));
		serialPort.notifyOnDataAvailable(true);
		serialPort.notifyOnBreakInterrupt(true);
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
					logger.info("与串口设备通讯中断");
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
						// 读取串口数据
						try {
							byte[] data = SerialTool.readFromPort(serialPort);
							String response = new String(data);
							if (response.contains(ScanCodeTypeEnum.NFC.getCode())) {
								response = response.replace(ScanCodeTypeEnum.NFC.getCode(), "").trim();
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.ICSWIPING, response));
							} else {
								if (response.contains(ScanCodeTypeEnum.APP.getCode())) { // app预约取料
									response = response.replace(ScanCodeTypeEnum.APP.getCode(), "");
								} else { // 给已打印条码赋值（常州）
									WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.PRINTED_CODE_INFO, response));
								}
							}
						} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		});
	}
}