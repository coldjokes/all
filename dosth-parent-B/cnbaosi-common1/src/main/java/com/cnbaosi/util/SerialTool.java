package com.cnbaosi.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.DeterminantConstant;
import com.cnbaosi.enums.BoardInstruction;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * @description 串口服务类，提供打开、关闭串口，读取、发送串口数据等服务
 * 
 * @author guozhidong
 *
 */
public class SerialTool {
	private static final Logger logger = LoggerFactory.getLogger(SerialTool.class);

	static InputStream in = null;
	
	private SerialTool() {
	}

	/**
	 * @description 遍历所有串口
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final List<String> findPort() {
		Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
		List<String> portNameList = new ArrayList<>();
		CommPortIdentifier identifier = null;
		while (portList.hasMoreElements()) {
			identifier = portList.nextElement();
			portNameList.add(identifier.getName());
		}
		return portNameList;
	}

	/**
	 * @description 验证串口是否被打开
	 * @param portName 串口名称
	 * @return
	 * @throws NoSuchPortException
	 */
	public static final Boolean checkPort(String portName) throws NoSuchPortException {
		logger.info("验证串口" + portName + "是否被打开");
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			return true;
		}
		return false;
	}

	/**
	 * @description 打开串口
	 * 
	 * @param portName 端口名称
	 * @param baudrate 波特率
	 * @return 串口对象
	 * @throws Exception 
	 */
	public static final SerialPort openPort(String portName, int baudrate) throws Exception {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			CommPort commPort = portIdentifier.open(portName, 2000);
			if (commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
				serialPort.notifyOnDataAvailable(true);
				serialPort.notifyOnBreakInterrupt(true);
				return serialPort;
			} else {
				logger.warn("串口" + portName + "(" + baudrate + ")指向设备不是串口类型");
			}
		} catch (NoSuchPortException e) {
			logger.error("没有发现对应的串口" + portName + "(" + baudrate + ")");
			e.printStackTrace();
		} catch (PortInUseException e) {
			logger.error("串口" + portName + "(" + baudrate + ")已被使用");
			e.printStackTrace();
		} catch (UnsupportedCommOperationException e) {
			logger.error(portName + "(" + baudrate + ")不支持串口操作");
			e.printStackTrace();
		} catch (Exception e) {
			throw new Exception("串口" + portName + "(" + baudrate + ")异常,具体查看日志文件!");
		}
		return null;
	}

	/**
	 * @description 关闭串口
	 * 
	 * @param serialPort 待关闭的串口对象
	 */
	public static void closePort(SerialPort serialPort) {
		if (serialPort != null) {
			logger.info("关闭串口");
			serialPort.close();
			serialPort = null;
		}
	}

	/**
	 * @description 往串口发送数据
	 * 
	 * @param serialPort 串口对象
	 * @param order      待发送数据
	 * @throws Exception
	 */
	public static void sendToPort(SerialPort serialPort, byte[] order) throws Exception {
		OutputStream out = null;
		try {
			while (in != null && in.available() != 0) {
				Thread.sleep(new Random().nextInt(2) + 1);
			}
			out = serialPort.getOutputStream();
			out.write(order);
			out.flush();
		} catch (IOException e) {
			logger.error("向串口发送数据失败");
			throw new Exception("向串口发送数据失败");
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort 当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 * @throws Exception 
	 */
	public static byte[] readFromPort(SerialPort serialPort) throws Exception {
		byte[] bytes = null;
		try {
			in = serialPort.getInputStream();
			int buffLength = in.available();
			while (buffLength != 0) {
				bytes = new byte[buffLength];
				in.read(bytes);
				buffLength = in.available();
			}
		} catch (IOException e) {
			logger.error("读取串口数据失败");
			throw new Exception("读取串口数据失败");
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	/**
	 * 添加监听器
	 * 
	 * @param serialPort 串口对象
	 * @param listener   串口监听器
	 * @throws Exception
	 */
	public static void addListener(SerialPort serialPort, SerialPortEventListener listener) throws Exception {
		try {
			serialPort.addEventListener(listener);
			serialPort.notifyOnDataAvailable(true);
			serialPort.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			logger.error("监听类对象过多");
			throw new Exception("监听类对象过多");
		}
	}

	/**
	 * @description 二进制数组转换成字符串
	 * @param bs 二进制数组
	 * @return 转换后的字符串
	 */
	public static String bytesToHexString(byte[] bs) {
		if (bs == null || bs.length < 1) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		String hv;
		for (int i = 0; i < bs.length; i++) {
			hv = Integer.toHexString(bs[i] & 0xFF);
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
			builder.append(" ");
		}
		return builder.toString();
	}

	/**
	 * @description 位对象数组转换成二进制数组
	 * @param ba 位对象数组
	 * @return 转换后的二进制数组
	 */
	public static byte[] convertType(Byte[] ba) {
		byte[] data = new byte[ba.length];
		for (int i = 0; i < ba.length; i++) {
			data[i] = ba[i].byteValue();
		}
		return data;
	}
	
	/**
	 * @description 创建发送数据报文
	 * @param boardNo 栈号
	 * @param instruction 行列式协议
	 * @param data 数据
	 */
	public static byte[] createSendMsg(Byte boardNo, BoardInstruction instruction, byte... data) {
		byte[] bytes = null;
		if (data == null) {
			bytes = new byte[DeterminantConstant.START.length + 3 + 1];
		} else {
			bytes = new byte[DeterminantConstant.START.length + 3 + data.length + 1];
		}
		for (int i = 0; i < DeterminantConstant.START.length; i++) {
			bytes[i] = DeterminantConstant.START[i];
		}
		bytes[4] = instruction.getSendFrameLength();
		bytes[5] = (byte) boardNo.intValue();
		bytes[6] = instruction.getInstructionWord();
		if (data != null && data.length > 0) {
			for (int i = 0; i < data.length; i++) {
				bytes[DeterminantConstant.START.length + 3 + i] = data[i];
			}
		}
		bytes[bytes.length - 1] = getXor(bytes);
		return bytes;
	}

	/**
	 * @description 加总异或校验码生成
	 * @param datas 发送前数据结构
	 * @return
	 */
	public static byte getXor(byte[] datas) {
		byte temp = datas[0];
		for (int i = 1; i < datas.length; i++) {
			temp ^= datas[i];
		}
		return temp;
	}
}