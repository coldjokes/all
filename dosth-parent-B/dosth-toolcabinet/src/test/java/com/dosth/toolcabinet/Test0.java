package com.dosth.toolcabinet;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.dosth.comm.NotASerialPort;
import com.dosth.comm.SendDataToSerialPortFailure;
import com.dosth.comm.SerialPortOutputStreamCloseFailure;
import com.dosth.comm.SerialPortParameterFailure;

import com.dosth.comm.printer.VertiLayout;
import com.dosth.util.SerialTool;
import com.dosth.comm.printer.HoriLayout;
import com.dosth.comm.printer.PageLayoutManager;
import com.dosth.comm.printer.PrintPage;
import com.dosth.comm.printer.PrinterQRCode;
import com.dosth.comm.printer.PrinterText;
import com.dosth.comm.printer.PrinterUtil;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

public class Test0 {
	
	private String comm = "COM5";

	/**
	 * 读取串口列表
	 */
	@Test
	public void findPort() {
		System.out.println("****");
		List<String> ports = SerialTool.findPort();
		ports.stream().forEach(action -> {
			System.out.println("--" + action);
		});
	}
	
	@Test
	public void test1() {
		byte[] order = new byte[] {
				// 标签开始
				0x1A, 0x5B, 0x01, 
				// 标签打印起始位置X、Y轴
				0x00, 0x00, 0x00, 0x00, 
				// 打印范围，宽、高
				0x36, 0x00, 0x1E, 0x00, 
				// 结束
				0x00, 
				// 二维码打印
				0x1A, 0x31, 0x00, 
				// 固定
				0x05, 0x02, 
				// 打印起始位置
				0x00, 0x00, 0x10, 0x00, 
				// 二维码大小,大小范围01-08
				0x05, 
				// 结束
				0x00, 
				// 二维码内容
				0x66, 0x64, 0x73, 0x66, 0x73, 0x64, 0x66, 0x73, 0x61, 0x65, 0x00, 
				
				// 打印文本
				0x1A, 0x54, 0x01,
				// 打印起始位置
				0x7F, 0x79, 0x7F, 0x79, 
				// 11 正常字体, 22字体放大一倍
				0x00, 0x60, 0x00, 0x11,
				// 打印内容 "MC 1001"
				0x4D, 0x43, 0x20, 0x31, 0x30, 0x30, 0x31, 
				// 结束
				0x00,
//				0x1A, 0x54, 0x01, 0xc5, 0x00, 0x2F, 0x00, 0x00, 0x60, 0x00, 0x11, 0xB5, 0xE7, 0x20, 0xB3, 0xD8, 0x00, 
//				0x1A, 0x54, 0x01, 0xc5, 0x00, 0x4B, 0x00, 0x00, 0x60, 0x00, 0x11, 0x32, 0x30, 0x31, 0x38, 0x2D, 0x30, 0x37, 0x2D, 0x31, 0x30, 0x00, 
//				0x1A, 0x54, 0x01, 0xc5, 0x00, 0x68, 0x00, 0x00, 0x60, 0x00, 0x11, 0x31, 0x37, 0x3A, 0x33, 0x34, 0x3A, 0x33, 0x30, 0x00, 
//				0x1A, 0x54, 0x01, 0xc5, 0x00, 0x83, 0x00, 0x00, 0x60, 0x00, 0x11, 0x53, 0x54 , 0x43, 0x50, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x33, 0x36, 0x00, 
//				0x1A, 0x54, 0x01, 0xc3, 0x00, 0x9C, 0x00, 0x00, 0x60, 0x00, 0x11, 0x32, 0x30, 0x31, 0x38, 0x2D, 0x30, 0x37, 0x2D, 0x32, 0x30, 0xC7, 0xB0, 0xD3, 0xD0, 0xD0, 0xA7, 0x00, 
//				0x1A, 0x54, 0x01, 0xc5, 0x00, 0xB3, 0x00, 0x00, 0x60, 0x00, 0x11, 0x4C, 0x53, 0x20, 0x33, 0x32, 0x30, 0x36, 0x30, 0x00, 
				// 
				0x1A, 0x5D, 0x00, 0x1A, 0x4F, 0x00};
		sendToPort(order);
	}
	
	@Test
	public void test2() {
		PrintPage  page = new PrintPage(384,250);
		PageLayoutManager layoutMgr = new HoriLayout();
//		layoutMgr = new VertiLayout();
		layoutMgr.setAlignment(VertiLayout.RIGHT);
		page.setLayoutManager(layoutMgr);
		
		ArrayList<String> qrStr = new ArrayList<>();
		ArrayList<String> textStr = new ArrayList<>();
		qrStr.add("QR_Str");
		textStr.add("铣刀(山特)");
		textStr.add("一车间 2线");
		textStr.add("还料人:徐天鹏");
		textStr.add("打印时间:");
		textStr.add("2018-9-21 13:35:48"); 
		

		PrinterQRCode qrObj = new PrinterQRCode(qrStr);
		PrinterText textObj = new PrinterText(textStr);
		
//		page.add(textObj);
		page.add(qrObj);
		page.add(textObj);
		
		qrObj.setQRSize(3);
		page.setInsetsTop(5);
		page.setInsetsLeft(10);
//		page.setCompoRate("1:1");
		page.triggerLayout();
		
		byte[] bytes = page.getPrinterIOData();
		sendToPort(bytes);
		System.out.println("Printer");
	}
	
	@Test
	public void test3() {
		// 打印二维码
		byte[] order = new byte[] {
				0x1b, 0x40, 
				// 位置偏左, 00 偏左;01 居中;02 偏右
				0x1B, 0x61, 0X00, // 设置无效
				0x1D, 0x6B, 0x61, 0x08, 0x04, 0x08, 0x00, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37
		};
		sendToPort(order);
	}
	
	@Test
	public void test4() {
		// 打印QR码
		byte[] order = new byte[] {
				0x1b, 0x40,
				0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31, 0x43, 0x03,
				0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31, 0x45, 0x30,
				0x1d, 0x28, 0x6b, 0x06, 0x00, 0x31, 0x50, 0x30, 0x41, 0x42, 0x43,
				0x1b, 0x61, 0x01,
				0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31, 0x52, 0x30,
				0x1d, 0x28, 0x6b, 0x03, 0x00, 0x31, 0x51, 0x30	
		};
		sendToPort(order);
	}
	
	@Test
	public void test5() {
		// 走纸指令到下一个切纸口
		byte[] order = new byte[] {
				0x1A, 0x0C, 0x01, 0x00, 0x00, 0x00
		};
		sendToPort(order);
	}
	
	@Test
	public void test6() {
		ArrayList<String> qrStr = new ArrayList<>();
		ArrayList<String> textStr = new ArrayList<>();
		qrStr.add("QR_Str");
		textStr.add("铣刀(山特)");
		textStr.add("一车间 2线");
		textStr.add("还料人:徐天鹏");
		textStr.add("打印时间:");
		textStr.add("2018-9-21 13:35:48"); 
		
		try { 
			PrinterUtil util = new PrinterUtil("COM5");
			util.print(qrStr, textStr);
		} catch (NoSuchPortException | PortInUseException | SerialPortParameterFailure | NotASerialPort
				| SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送命令到端口
	 * @param order
	 */
	private void sendToPort(byte[] order) {
		SerialPort port = null;
		try {
			port = SerialTool.openPort(this.comm, 115200);
			if (port != null) {
				SerialTool.sendToPort(port, order);
				SerialTool.closePort(port);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (port != null) {
				port.close();
			}
		}
	}
}