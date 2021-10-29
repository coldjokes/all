package com.dosth.comm.printer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.NotASerialPort;
import com.dosth.comm.SendDataToSerialPortFailure;
import com.dosth.comm.SerialPortOutputStreamCloseFailure;
import com.dosth.comm.SerialPortParameterFailure;
import com.dosth.util.SerialTool;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

 /**
  * @description 打印工具类
  * @author xutianpeng
  *
  */
public class PrinterUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PrinterUtil.class);

	/**
	 * @param printComm 打印机串口名称
	 */
	private String printComm;
	private SerialPort portForCheckPaper;
	private byte[] bytesResponsedFromCheckPaperCmd = null;
	public PrinterUtil() {
	}

	public PrinterUtil(String printComm) {
		this.printComm = printComm;
	}

	public static byte[] intToByte(int num) {
		byte[] bytes = new byte[2];
		bytes[1] = (byte) ((num >> 8) & 0xff);
		bytes[0] = (byte) (num & 0xff);
		return bytes;
	}

	public static List<Byte> array2List(byte[] array) {
		List<Byte> byteArrayList = new ArrayList<>();
		for (byte b : array) {
			byteArrayList.add(b);
		}
		return byteArrayList;
	}

	public static byte[] getStrGBKCode(String str) {
		byte[] bytes = null;
		try {
			bytes = str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (bytes == null) {
			logger.info("getStrGBKCode function exception occur");
		}
		return bytes;
	}

	/**
	 * @description 打印机打印
	 * @param comm     串口
	 * @param qrList   二维码信息
	 * @param textList 输出文字信息
	 * @throws NoSuchPortException
	 * @throws PortInUseException
	 * @throws SerialPortParameterFailure
	 * @throws NotASerialPort
	 * @throws SendDataToSerialPortFailure
	 * @throws SerialPortOutputStreamCloseFailure
	 */
	public void print(List<String> qrList, List<String> textList)
			throws NoSuchPortException, PortInUseException, SerialPortParameterFailure, NotASerialPort,
			SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
		PrintPage page = new PrintPage(384, 267);
		PageLayoutManager layoutMgr = new HoriLayout();
		layoutMgr.setAlignment(VertiLayout.LEADING);
		page.setLayoutManager(layoutMgr);
		PrinterQRCode qrObj = new PrinterQRCode(qrList);
		page.add(qrObj);
		page.add(new PrinterText(textList));
		qrObj.setQRSize(3);
		page.setCompoRate("1:1.3");
		page.setInsetsTop(5);
		page.setInsetsLeft(10);
		page.triggerLayout();
		byte[] bytes = page.getPrinterIOData();
		sendToPort(bytes);
	}

	/**
	 * 发送命令到端口
	 * 
	 * @param order
	 * @throws NotASerialPort
	 * @throws SerialPortParameterFailure
	 * @throws PortInUseException
	 * @throws NoSuchPortException
	 * @throws SerialPortOutputStreamCloseFailure
	 * @throws SendDataToSerialPortFailure
	 */
	private void sendToPort(byte[] order)
			throws NoSuchPortException, PortInUseException, SerialPortParameterFailure, NotASerialPort,
			SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
		SerialPort port = null;
		try {
			port = SerialTool.openPort(this.printComm, 115200);
			if (port != null) {
				SerialTool.sendToPort(port, order);
				SerialTool.closePort(port);
			}
		} finally {
			if (port != null) {
				port.close();
			}
		}
	}
    //检查打印机是否缺纸
	//发送检查缺纸指令 10 04 01       有纸返回12 ，无纸返回1A或0C
	//return ture if paper is available
	public boolean isPrinterPaperAvailable() 
			throws NoSuchPortException, PortInUseException, SerialPortParameterFailure, NotASerialPort,
			SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure
	{
		boolean flag = true;
		byte[] order = {0x10,0x04,0x01};
		byte readyByte = 0x12;
		byte noreadyByte1 = 0x1A;
		byte noreadyByte2 = 0x0C;
		int timeout = 5; // 5s
		bytesResponsedFromCheckPaperCmd = null;
		try {
			portForCheckPaper = SerialTool.openPort(this.printComm, 115200);
			if (portForCheckPaper != null) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						logger.info("Thread running");
						InputStream in = null;
						try {
							in = portForCheckPaper.getInputStream();
							int buffLength = in.available();
							boolean isKeepReceive = (buffLength==0);
							long dur = timeout*1000; // millsecond
							long start = System.currentTimeMillis();
							long now = System.currentTimeMillis();
							while (isKeepReceive && (now-start)<dur) {
								buffLength = in.available();
								now = System.currentTimeMillis();
								if(buffLength!=0) {
									bytesResponsedFromCheckPaperCmd = new byte[buffLength];
									in.read(bytesResponsedFromCheckPaperCmd);
									break;
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
							SerialTool.closePort(portForCheckPaper);
						} 
					}
				}).start();
				SerialTool.sendToPort(portForCheckPaper, order);
				
				long dur = timeout*1000; // millsecond
				long start = System.currentTimeMillis();
				long now = System.currentTimeMillis();
				while(bytesResponsedFromCheckPaperCmd==null && (now-start)<dur) {
					Thread.sleep(100);
					now = System.currentTimeMillis();
				}
				
				if(bytesResponsedFromCheckPaperCmd != null) {
					int len = bytesResponsedFromCheckPaperCmd.length;
					for(int i=0;i<len;i++) {
						byte val = bytesResponsedFromCheckPaperCmd[i];
						if(val == readyByte) {
							flag = true;
							break;
						}
						else if(val == noreadyByte1 || val == noreadyByte2){
							flag = false;
							break;
						}
					}
				}
				SerialTool.closePort(portForCheckPaper);
			}
		}
		catch(Exception e) {
			if (portForCheckPaper != null ) {
				SerialTool.closePort(portForCheckPaper);
			}
		}
		return flag;
	}
}