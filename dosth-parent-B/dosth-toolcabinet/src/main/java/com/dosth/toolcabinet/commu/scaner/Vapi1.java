package com.dosth.toolcabinet.commu.scaner;

import java.io.UnsupportedEncodingException;

import com.dosth.comm.ReadDataFromSerialPortFailure;
import com.dosth.comm.SendDataToSerialPortFailure;
import com.dosth.comm.SerialPortInputStreamCloseFailure;
import com.dosth.comm.SerialPortOutputStreamCloseFailure;
import com.dosth.util.MathUtil;
import com.dosth.util.SerialTool;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class Vapi1 {

	static final String CODE_LED_ON = "55 AA 24 01 00 01 DB"; // LED开
	static final String CODE_LED_OFF = "55 AA 24 01 00 00 DA"; // LED关

	static final String CODE_BEE_ON = "55 AA 25 01 00 01 DA"; // 蜂鸣器开
	static final String CODE_BEE_OFF = "55 AA 25 01 00 00 DB"; // 蜂鸣器关

	static final String CODE_READ_MODE_AUTO = "55 AA 31 01 00 01 CE"; // 主动上报模式
	static final String CODE_READ_MODE_ORDER = "55 AA 31 01 00 00 CF"; // 命令模式

	static final String CODE_FREQ_NORMAL = "55 AA 22 01 00 01 DD"; // 普通模式
	static final String CODE_FREQ_ONE = "55 AA 22 01 00 02 DE"; // 单次模式
	static final String CODE_FREQ_INTERVAL = "55 AA 22 03 00 03 02 00 DF"; // 间隔模式(2s)

	static final String CODE_RETURN_BEE_SUCCESS = "55 AA 25 00 00 00 DA"; // 蜂鸣器成功码
	static final String CODE_RETURN_LED_SUCCESS = "55 AA 24 00 00 00 DB"; // LED成功码

	// 初始化设备变量
	SerialPort serialPort = null;

	/**
	 * 断开设备
	 */
	public void vbarClose() {
		if (serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
	}

	/**
	 * 打开设备
	 */
	public boolean vbarOpen(String devnum) {
		boolean result = false;
		CommPort commPort = null;
		try {
			// 通过端口名称得到端口
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(devnum);
			// 打开端口，（自定义名字，打开超时时间）
			commPort = portIdentifier.open(devnum, 2222);
		} catch (NoSuchPortException | PortInUseException e) {
			e.printStackTrace();
		}

		if (commPort != null && (commPort instanceof SerialPort)) {
			serialPort = (SerialPort) commPort;
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * 设置串口参数
	 */
	public boolean vbarSetserial(String portParams) { // 115200-8-N-1
		boolean result = false;
		if (portParams != null) {
			String[] settingArr = portParams.split("-");
			Integer baudRate = Integer.valueOf(settingArr[0]); // 波特率
			Integer dateBits = Integer.valueOf(settingArr[1]); // 数据位
			Integer stopBits = Integer.valueOf(settingArr[3]); // 停止位
			String parityStr = settingArr[2];
			Integer parity = SerialPort.PARITY_NONE; // 校验位
			switch (parityStr) {
			case "N":
				parity = SerialPort.PARITY_NONE;
				break;
			case "O":
				parity = SerialPort.PARITY_ODD;
				break;
			case "E":
				parity = SerialPort.PARITY_EVEN;
				break;
			case "M":
				parity = SerialPort.PARITY_MARK;
				break;
			case "S":
				parity = SerialPort.PARITY_SPACE;
				break;
			}
			try {
				// 设置串口参数（波特率，数据位8，停止位1，校验位无）
				serialPort.setSerialPortParams(baudRate, dateBits, stopBits, parity);

				// 初始化其他参数
				init();

				result = true;
			} catch (UnsupportedCommOperationException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 初始化相关配置
	 */
	private void init() {
		try {
			// led关
			SerialTool.sendToPort(serialPort, MathUtil.hexStringToBytes(CODE_LED_OFF));
			// 蜂鸣器关
			SerialTool.sendToPort(serialPort, MathUtil.hexStringToBytes(CODE_BEE_OFF));
			// 主动上报模式
			SerialTool.sendToPort(serialPort, MathUtil.hexStringToBytes(CODE_READ_MODE_AUTO));
			// 普通模式
			SerialTool.sendToPort(serialPort, MathUtil.hexStringToBytes(CODE_FREQ_NORMAL));
		} catch (SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
			e.printStackTrace();
		}
	}

	/**
	 * 蜂鸣器控制
	 */
	public boolean vbarBeep(byte times) {
		//将次数转成16进制
		String timesStr = MathUtil.byteToHexString(times);
		
		//命令字+数据域长度+数据域
		String source = "55 AA 04 05 00 08 " + timesStr + " 01 01 00";
		
		//获取校验码
		String checkCode = getCheckCode(source);
		try {
			String order = source + " " + checkCode;
			SerialTool.sendToPort(serialPort, MathUtil.hexStringToBytes(order));
		} catch (SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
			e.printStackTrace();
		}
		return retureCodeCheck(CODE_RETURN_BEE_SUCCESS);
	}

	/**
	 * 背光控制
	 */
	public boolean vbarBacklight(boolean bool) {
		String order = bool ? CODE_LED_ON : CODE_LED_OFF;
		try {
			SerialTool.sendToPort(serialPort, MathUtil.hexStringToBytes(order));
		} catch (SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
			e.printStackTrace();
		}
		return retureCodeCheck(CODE_RETURN_LED_SUCCESS);
	}

	/**
	 * 添加要支持的码制
	 */
	public boolean vbarAddSymbolType(byte symbol_type) {
		// TODO
		return true;
	}

	/**
	 * 设置间隔时间
	 */
	public Boolean vbarInterval(int times) {
		// TODO
		return true;
	}

	/**
	 * 扫码
	 */
	public String vbarScan() throws UnsupportedEncodingException { // 该异常抛出没有实际意义，纯粹为了减少其他代码的改动
		String result = null;
		try {
			Thread.sleep(500l);
			byte[] byteResult = SerialTool.readFromPort(serialPort);
			if (byteResult != null) {
				String hexString = MathUtil.bytesToHexString(byteResult);

				// 获取数据域内容
				String content = hexString.substring(12) // 去掉命令字1+数据域长度2
						.substring(0, hexString.length() - 12 - 2); // 去掉校验字

				// 每两个字符中加上空格
				String regex = "(.{2})";
				hexString = content.replaceAll(regex, "$1 ");

				result = MathUtil.hexStringToString(hexString);
			}
		} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure | InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 返回码验证
	 */
	private boolean retureCodeCheck(String returnCode) {
		boolean result = false;
		try {
			Thread.sleep(500l);
			byte[] byteResult = SerialTool.readFromPort(serialPort);
			String hexString = null;
			if(byteResult != null) {
				hexString = MathUtil.bytesToHexString(byteResult);
			}
			//验证返回码是否相同
			if (returnCode.trim().equalsIgnoreCase(hexString)) {
				result = true;
			}
		} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure | InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	   *  根据命令字+数据域长度+数据域去获取校验码 
	 */
	public static String getCheckCode(String code) {
		String[] codeArr = code.split(" ");
		String source = "0";
		for (String str : codeArr) {
			source = MathUtil.xor(source, str);
		}
		return source.toUpperCase();
	}

}
