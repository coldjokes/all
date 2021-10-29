package com.dosth.test;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.op.LightElectroStatus;
import com.dosth.util.SerialTool;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class TestReadStatus {
	public static void main(String[] args) {
		try {
			receive();
			LightElectroStatus read = new LightElectroStatus();
			read.readStatus();
			Thread.sleep(6000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Board.close();
		}
	}
	
	public static void receive() throws Exception {
		Board.startSerialPort("COM6", 9600);
		Board.serialPort.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				switch (serialPortEvent.getEventType()) {
				case SerialPortEvent.BI: // 10 通讯中断
					System.err.println("与串口设备通讯中断");
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
					System.err.println("串口监听到动静了~~~~~~~~~~~~~~~~~~~~~~~~");
					if (Board.serialPort == null) {
						System.err.println("串口对象为空！监听失败！");
					} else {
						// 读取串口数据
						byte[] data;
						try {
							data = SerialTool.readFromPort(Board.serialPort);
							if (data != null) {
								System.err.println(SerialTool.bytesToHexString(data));
							}
						} catch (Exception e) {
							System.err.println("读取串口数据失败:" + e.getMessage());
							e.printStackTrace();
						}
					}
					break;
				}
			}
		});
	}
}