package com.dosth.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import com.dosth.comm.NotASerialPort;
import com.dosth.comm.ReadDataFromSerialPortFailure;
import com.dosth.comm.SendDataToSerialPortFailure;
import com.dosth.comm.SerialPortInputStreamCloseFailure;
import com.dosth.comm.SerialPortOutputStreamCloseFailure;
import com.dosth.comm.SerialPortParameterFailure;
import com.dosth.comm.TooManyListeners;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * 串口服务类，提供打开、关闭串口，读取、发送串口数据等服务
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("unchecked")
public class SerialTool {
	private static SerialTool serialTool = null;
	static {
		if (serialTool == null) {
			serialTool = new SerialTool();
		}
	}

	private SerialTool() {
	}

	public static SerialTool geSerialTool() {
		if (serialTool == null) {
			serialTool = new SerialTool();
		}
		return serialTool;
	}

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
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if (portIdentifier.isCurrentlyOwned()) {
			return true;
		}
		return false;
	}

	/**
	 * 打开串口
	 * 
	 * @param portName
	 *            端口名称
	 * @param baudrate
	 *            波特率
	 * @return 串口对象
	 * @throws NoSuchPortException
	 *             没有该端口对应的串口设备
	 * @throws PortInUseException
	 *             端口已被占用
	 * @throws SerialPortParameterFailure
	 * @throws NotASerialPort
	 */
	public static final SerialPort openPort(String portName, int baudrate)
			throws NoSuchPortException, PortInUseException, SerialPortParameterFailure, NotASerialPort {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		CommPort commPort = portIdentifier.open(portName, 2000);

		if (commPort instanceof SerialPort) {
			SerialPort serialPort = (SerialPort) commPort;

			try {
				serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				throw new SerialPortParameterFailure();
			}
			return serialPort;
		} else {
			throw new NotASerialPort();
		}
	}

	/**
	 * 关闭串口
	 * 
	 * @param serialPort
	 *            待关闭的串口对象
	 */
	public static void closePort(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.close();
			serialPort = null;
		}
	}

	/**
	 * 往串口发送数据
	 * 
	 * @param serialPort
	 *            串口对象
	 * @param order
	 *            待发送数据
	 * @throws SendDataToSerialPortFailure
	 * @throws SerialPortOutputStreamCloseFailure
	 */
	public static void sendToPort(SerialPort serialPort, byte[] order)
			throws SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
		OutputStream out = null;
		try {
			out = serialPort.getOutputStream();
			out.write(order);
			out.flush();
		} catch (IOException e) {
			throw new SendDataToSerialPortFailure();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					throw new SerialPortOutputStreamCloseFailure();
				}
			}
		}
	}

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort
	 *            当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 * @throws ReadDataFromSerialPortFailure
	 * @throws SerialPortInputStreamCloseFailure
	 */
	public static byte[] readFromPort(SerialPort serialPort)
			throws ReadDataFromSerialPortFailure, SerialPortInputStreamCloseFailure {
		InputStream in = null;
		byte[] bytes = null;
		try {
			in = serialPort.getInputStream();
			int buffLength = in.available();
			while (buffLength != 0) {
				bytes = new byte[buffLength];
				in.read(bytes);
				buffLength = in.available();
			}
//			in = serialPort.getInputStream();
//			int buffLength = 0;
//			while(buffLength == 0) {
//				buffLength = in.available();
//			}
//			bytes = new byte[buffLength];
//			in.read(bytes);
		} catch (IOException e) {
			throw new ReadDataFromSerialPortFailure();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				throw new SerialPortInputStreamCloseFailure();
			}
		}

		return bytes;
	}

	/**
	 * 添加监听器
	 * 
	 * @param serialPort
	 *            串口对象
	 * @param listener
	 *            串口监听器
	 * @throws TooManyListeners
	 *             监听类对象过多
	 */
	public static void addListener(SerialPort serialPort, SerialPortEventListener listener) throws TooManyListeners {
		try {
			serialPort.addEventListener(listener);
			serialPort.notifyOnDataAvailable(true);
			serialPort.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			throw new TooManyListeners();
		}
	}
	
    public static String bytesToHexString(byte[] bs) {
		
    	if (bs == null || bs.length < 1) {
			return null;
		}
		StringBuilder builder = new StringBuilder();
		String hv;
		
		for (int i = 0; i <bs.length; i++) {
			hv = Integer.toHexString(bs[i] & 0xFF);
			if (hv.length() < 2) {
				builder.append(0);
			}
			builder.append(hv);
		}
		return builder.toString();
	}
    
    public static byte[] convertType(Byte[] ba) { 
    	
    	byte[] data = new byte[ba.length];
    	for(int i=0;i<ba.length;i++) {
    		
    		data[i] = ba[i].byteValue();
    	}
    	return data;
    	
    }
}