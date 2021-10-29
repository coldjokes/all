package com.cnbaosi.printer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.printer.dto.Label;
import com.cnbaosi.printer.dto.PrintInfo;
import com.cnbaosi.printer.dto.PrintTextInfo;
import com.cnbaosi.printer.dto.QrInfo;
import com.cnbaosi.printer.dto.Txt;
import com.cnbaosi.printer.enums.LabelType;
import com.cnbaosi.printer.enums.QrSize;
import com.cnbaosi.util.SerialTool;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @description 达普打印机
 * @author guozhidong
 *
 */
public abstract class DpPrinter {
	private static final Logger logger = LoggerFactory.getLogger(DpPrinter.class);

	private SerialPort serialPort = null;
	// 超时时间,默认5S
	private int timeout = 3;
	// 信号接收标识
	private Boolean receiveFlag = false;
	// 是否自带切刀
	private Boolean isAutoCut;
	// 是否半切
	private Boolean halfCut;
	// 打印信息
	private PrintInfo info;
	// 是否二维码打印
	private Boolean isQrCodePrint = false;
	
	private Lock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();
	
	private static BlockingQueue<PrintInfo> printInfoQueue = new LinkedBlockingQueue<>();
	
	public static void put(PrintInfo info) throws InterruptedException {
		if (printInfoQueue.size() > 3) {
			throw new BscException("打印机正忙,请稍后再试!");
		}
		printInfoQueue.put(info);
	}

	public Boolean getIsAutoCut() {
		if (this.isAutoCut == null) {
			this.isAutoCut = true;
		}
		return this.isAutoCut;
	}

	public void setIsAutoCut(Boolean isAutoCut) {
		this.isAutoCut = isAutoCut;
	}

	public Boolean getHalfCut() {
		if (this.halfCut == null) {
			this.halfCut = true;
		}
		return this.halfCut;
	}

	public void setHalfCut(Boolean halfCut) {
		this.halfCut = halfCut;
	}
	
	public Boolean getIsQrCodePrint() {
		if (this.isQrCodePrint == null) {
			this.isQrCodePrint = false;
		}
		return this.isQrCodePrint;
	}

	public void setIsQrCodePrint(Boolean isQrCodePrint) {
		this.isQrCodePrint = isQrCodePrint;
	}

	/**
	 * @description 创建条形码数据
	 * @return
	 */
	private Byte[] createBarCodeData() {
		List<Byte> dataList = new ArrayList<>();
		dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x5B, 0x01})); // 开始标签
		dataList.addAll(this.arrayToList(this.intToByte(this.info.getLeft())));// 标签开始打印X轴
		dataList.addAll(this.arrayToList(this.intToByte(this.info.getTop()))); // 标签开始打印Y轴
		dataList.addAll(this.arrayToList(this.intToByte(this.info.getWidth()))); // 打印范围宽
		dataList.addAll(this.arrayToList(this.intToByte(this.info.getHeight()))); // 打印范围高
//		dataList.addAll(this.arrayToList(new Byte[] {0x00, 0x00, 0x00, 0x00})); // 标签起始位置
//		dataList.addAll(this.arrayToList(new Byte[] {0x60, 0x01, (byte) 0xFB, 0x00})); // 标签打印范围
		dataList.add((byte) 0x00); // 结束符

		QrInfo qrInfo = this.info.getQrInfo(); // 条形码信息
		// 条形码输出
		dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x30, 0x00})); // 条码指令
		dataList.addAll(this.arrayToList(this.intToByte(qrInfo.getLeft()))); // 开始位置X轴
		dataList.addAll(this.arrayToList(this.intToByte(qrInfo.getTop()))); // 开始位置Y轴
		dataList.addAll(this.arrayToList(new Byte[] {0x0c, 0x45, 0x02, 0x00}));  // 固定值
		dataList.addAll(this.arrayToList(this.getGBKEncode(qrInfo.getContent()))); // 条码信息
		dataList.add((byte) 0x00); // 结束符
		
		int rowHeight = 0x70;
		int height = 0x20;
		Txt txt;
		List<PrintTextInfo> textInfoList = this.info.getTextInfoList();
		for (PrintTextInfo textInfo : textInfoList) {
			if (textInfo.getLabel() == null || textInfo.getLabel().getType() == null) {
				dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x54, 0x01})); // 打印文本
				dataList.addAll(this.arrayToList(new Byte[] {0x00, 0x00 })); // 文字打印起始位置X轴
				dataList.addAll(this.arrayToList(this.intToByte(rowHeight))); // 文字打印起始位置Y轴
				dataList.addAll(this.arrayToList(new Byte[] {0x00, 0x60, 0x00, 0x11})); // 设置字体大小, 11 字体大小，20 倍高，33，44，55，66
				dataList.addAll(this.arrayToList(this.getGBKEncode(textInfo.getTxt().getText()))); // 打印内容 
				dataList.add((byte) 0x00); // 结束符 
				rowHeight += height; 
				continue;
			}
			if (LabelType.MATNAME.equals(textInfo.getLabel().getType())
					|| LabelType.BARCODE.equals(textInfo.getLabel().getType())
					|| LabelType.MATSPEC.equals(textInfo.getLabel().getType())) {
				// 文字输出
				txt = textInfo.getTxt();
				// 空文本不输出
				if (txt.getText() == null || "".equals(txt.getText())) {
					continue;
				}
				dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x54, 0x01})); // 打印文本
				dataList.addAll(this.arrayToList(new Byte[] {0x00, 0x00 })); // 文字打印起始位置X轴
				dataList.addAll(this.arrayToList(this.intToByte(rowHeight))); // 文字打印起始位置Y轴
				dataList.addAll(this.arrayToList(new Byte[] {0x00, 0x60, 0x00, 0x11})); // 设置字体大小, 11 字体大小，20 倍高，33，44，55，66
				dataList.addAll(this.arrayToList(this.getGBKEncode(txt.getText()))); // 打印内容 
				dataList.add((byte) 0x00); // 结束符 
				
				rowHeight += height; 
			}
		}
		StringBuilder userTime = new StringBuilder();
		// 打印人员信息和打印时间
		for (PrintTextInfo textInfo : textInfoList) {
			if (textInfo.getLabel() == null || textInfo.getLabel().getType() == null) {
				continue;
			}
			if (LabelType.USERNAME.equals(textInfo.getLabel().getType())) {
				userTime.append(textInfo.getTxt().getText()).append(" ");
				break;
			}
		}

		for (PrintTextInfo textInfo : textInfoList) {
			if (textInfo.getLabel() == null || textInfo.getLabel().getType() == null) {
				continue;
			}
			if (LabelType.PRINTTIME.equals(textInfo.getLabel().getType())) {
				userTime.append(textInfo.getTxt().getText()).append(" ");
				break;
			}
		}
		if (userTime.length() > 0) {
			dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x54, 0x01})); // 打印文本
			dataList.addAll(this.arrayToList(new Byte[] {0x00, 0x00 })); // 文字打印起始位置X轴
			dataList.addAll(this.arrayToList(this.intToByte(rowHeight))); // 文字打印起始位置Y轴
			dataList.addAll(this.arrayToList(new Byte[] {0x00, 0x60, 0x00, 0x11})); // 设置字体大小, 11 字体大小，20 倍高，33，44，55，66
			dataList.addAll(this.arrayToList(this.getGBKEncode(userTime.toString()))); // 打印内容 
			dataList.add((byte) 0x00); // 结束符 
		}
		// 打印其他信息
		rowHeight = 0x05;
		for (PrintTextInfo textInfo : textInfoList) {
			if (textInfo.getLabel() == null || textInfo.getLabel().getType() == null) {
				continue;
			}
			switch (textInfo.getLabel().getType()) {
			case MATNAME:
			case BARCODE:
			case MATSPEC:
			case USERNAME:
			case PRINTTIME:
				break;
			default:
				dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x54, 0x01})); // 打印文本
				dataList.addAll(this.arrayToList(new Byte[] {(byte) 0xF0, 0x00 })); // 文字打印起始位置X轴
				dataList.addAll(this.arrayToList(this.intToByte(rowHeight))); // 文字打印起始位置Y轴
				dataList.addAll(this.arrayToList(new Byte[] {0x00, 0x60, 0x00, 0x11})); // 设置字体大小, 11 字体大小，20 倍高，33，44，55，66
				dataList.addAll(this.arrayToList(this.getGBKEncode(textInfo.getTxt().getText()))); // 打印内容 
				dataList.add((byte) 0x00); // 结束符 
				rowHeight += height;
				break;
			}
		}
		
		dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x5D, 0x00, 0x1A, 0x4F, 0x00}));

		// 切纸命令
		if (this.isAutoCut != null && this.isAutoCut) {
			if (getHalfCut()) { // 半切
				dataList.addAll(this.arrayToList(new Byte[] {0x1B, 0x6D}));
			} else { // 全切
				dataList.addAll(this.arrayToList(new Byte[] {0x1B, 0x69}));
			}
		}
		return dataList.toArray(new Byte[dataList.size()]);
	}
	
	/**
	 * @description 创建二维码数据
	 */
	private Byte[] createQrCodeData() {
		List<Byte> dataList = new ArrayList<>();
		dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x5B, 0x01})); // 开始标签
		dataList.addAll(this.arrayToList(this.intToByte(this.info.getLeft())));// 标签开始打印X轴
		dataList.addAll(this.arrayToList(this.intToByte(this.info.getTop()))); // 标签开始打印Y轴
		dataList.addAll(this.arrayToList(this.intToByte(this.info.getWidth()))); // 打印范围宽
		dataList.addAll(this.arrayToList(this.intToByte(this.info.getHeight()))); // 打印范围高
		dataList.add((byte) 0x00); // 结束符
		
		// 二维码输出
		QrInfo qrInfo = this.info.getQrInfo(); // 二维码信息
		dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x31, 0x00})); // 二维码打印
		dataList.addAll(this.arrayToList(new Byte[] {0x05, 0x02})); // 固定
		dataList.addAll(this.arrayToList(this.intToByte(qrInfo.getLeft()))); // 二维码开始位置X轴
		dataList.addAll(this.arrayToList(this.intToByte(qrInfo.getTop()))); // 二维码开始位置Y轴
		// 二维码尺寸大小根据二维码数据长度动态加载,长度超过95,则使用标准尺寸,否则使用大尺寸
//		if (qrInfo.getContent().length() >= 95) {
//			dataList.add(QrSize.NORMAL.getCode()); 
//		} else {
			dataList.add(QrSize.LARGE.getCode());
//		}
		dataList.add((byte) 0x00); // 结束符
		dataList.addAll(this.arrayToList(this.getGBKEncode(qrInfo.getContent()))); // 二维码信息
		dataList.add((byte) 0x00); // 结束符
		
		int rowHeight = qrInfo.getTop() >= 10 ? 1 : qrInfo.getTop();
		List<PrintTextInfo> textInfoList = this.info.getTextInfoList();
		int xLength; // X轴距离
		Label label = null;
		Txt txt;
		int height = 19;
//		int maxLength = textInfoList.size() > 3 ? textInfoList.size() - 3 : textInfoList.size();
		for (PrintTextInfo textInfo : textInfoList) {
//			textInfo = textInfoList.get(i);
//			// 标签输出
//			label = textInfo.getLabel();
//			if (label != null) {
//				dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x54, 0x01})); // 打印文本
//				xLength = qrInfo.getLeft() + qrInfo.getSize().getLeft(); // 标签左边距
//				if (rowHeight >= qrInfo.getSize().getHeight() + qrInfo.getTop() + height) { // 如果列高超过二维码高度,则至左
//					xLength = qrInfo.getLeft();
//				}
//				dataList.addAll(this.arrayToList(this.intToByte(xLength))); // 标签打印起始位置X轴
//				dataList.addAll(this.arrayToList(this.intToByte(rowHeight))); // 标签打印起始位置Y轴
//				dataList.addAll(this.arrayToList(new Byte[] {0x18, 0x60})); // 字体样式
//				dataList.add(label.getFontSpec().getCode()); // 字体规格
//				dataList.add(label.getFontSize().getCode()); // 字体大小
//				dataList.addAll(this.arrayToList(this.getGBKEncode(label.getType().getDesc()))); // 打印内容 
//				dataList.add((byte) 0x00); // 结束符
//			}
			// 二维码右侧不输出物料名称,编号,规格型号
			if (textInfo.getLabel() != null && textInfo.getLabel().getType() != null && (LabelType.MATNAME.equals(textInfo.getLabel().getType())
					|| LabelType.BARCODE.equals(textInfo.getLabel().getType())
					|| LabelType.MATSPEC.equals(textInfo.getLabel().getType()))) {
				continue;
			}
			
			// 文字输出
			txt = textInfo.getTxt();
			dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x54, 0x01})); // 打印文本
			xLength = qrInfo.getLeft() + qrInfo.getSize().getLeft() + (label != null ? label.getWidth() : 8); // 文字左边距
			if (rowHeight >= qrInfo.getSize().getHeight() + qrInfo.getTop() + height) { // 如果列高超过二维码高度,则至标签右
				xLength = qrInfo.getLeft() + (label != null ? label.getWidth() : 8);
			}
			dataList.addAll(this.arrayToList(this.intToByte(xLength))); // 文字打印起始位置X轴
			dataList.addAll(this.arrayToList(this.intToByte(rowHeight))); // 文字打印起始位置Y轴
			dataList.addAll(this.arrayToList(new Byte[] {0x18, 0x60})); // 字体样式
			dataList.add(txt.getFontSpec().getCode()); // 字体规格
			dataList.add(txt.getFontSize().getCode()); // 字体大小
			dataList.addAll(this.arrayToList(this.getGBKEncode(txt.getText()))); // 打印内容 
			dataList.add((byte) 0x00); // 结束符
//			// 下一行要超过二维码底边
//			if (rowHeight < qrInfo.getTop() + qrInfo.getSize().getHeight() && rowHeight + textInfo.getLineHeight() > qrInfo.getTop() + qrInfo.getSize().getHeight()) {
//				rowHeight = qrInfo.getTop() + qrInfo.getSize().getHeight() + height;
//			} else { // 叠加行高
				rowHeight += textInfo.getLineHeight(); 
//			}
		}
		
		rowHeight = qrInfo.getTop() + qrInfo.getSize().getHeight() + 15;
		// 二维码下只打印物料名称、编号、规格型号
		for (PrintTextInfo textInfo : textInfoList) {
			if (textInfo.getLabel() == null || textInfo.getLabel().getType() == null) {
				continue;
			}
			if (LabelType.MATNAME.equals(textInfo.getLabel().getType())
					|| LabelType.BARCODE.equals(textInfo.getLabel().getType())
					|| LabelType.MATSPEC.equals(textInfo.getLabel().getType())) {
				// 文字输出
				txt = textInfo.getTxt();
				// 空文本不输出
				if (txt.getText() == null || "".equals(txt.getText())) {
					continue;
				}
				dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x54, 0x01})); // 打印文本
				dataList.addAll(this.arrayToList(this.intToByte(qrInfo.getLeft() ))); // 文字打印起始位置X轴
				dataList.addAll(this.arrayToList(this.intToByte(rowHeight))); // 文字打印起始位置Y轴
				dataList.addAll(this.arrayToList(new Byte[] {0x18, 0x60})); // 字体样式
				dataList.add(txt.getFontSpec().getCode()); // 字体规格
				dataList.add(txt.getFontSize().getCode()); // 字体大小
				dataList.addAll(this.arrayToList(this.getGBKEncode(txt.getText()))); // 打印内容 
				dataList.add((byte) 0x00); // 结束符
				rowHeight += textInfo.getLineHeight(); 
			}
		}
		
		// 结束指令
		dataList.addAll(this.arrayToList(new Byte[] {0x1A, 0x5D, 0x00, 0x1A, 0x4F, 0x00}));
		
		// 切纸命令
		if (this.isAutoCut != null && this.isAutoCut) {
			if (getHalfCut()) { // 半切
				dataList.addAll(this.arrayToList(new Byte[] {0x0D, 0x0A, 0x1B, 0x6D}));
			} else { // 全切
				dataList.addAll(this.arrayToList(new Byte[] {0x0D, 0x0A, 0x1B, 0x69}));
			}
		}
		return dataList.toArray(new Byte[dataList.size()]);
	}

	/**
	 * @description 创建数据
	 * @param info 打印对象
	 */
	private Byte[] createData() {
		if (this.isQrCodePrint != null && this.isQrCodePrint) {
			return this.createQrCodeData();
		}	else {
			return this.createBarCodeData();
		}
	}
	
	/**
	 * @description 整型转高低位
	 * @param num 待转换数字
	 * @return 低位在前,高位在后
	 */
	private Byte[] intToByte(int num) {
		Byte[] bytes = new Byte[2];
		bytes[1] = (byte) ((num >> 8) & 0xff);
		bytes[0] = (byte) (num & 0xff);
		return bytes;
	}
	
	/**
	 * @description 数组转为List
	 * @param array 数组
	 * @return
	 */
	private List<Byte> arrayToList(Byte[] array) {
		List<Byte> list = new ArrayList<Byte>(array.length);
		Collections.addAll(list, array);
		return list;
	}
	
	/**
	 * @description 数组转为List
	 * @param array 数组
	 * @return
	 */
	private List<Byte> arrayToList(byte[] array) {
		List<Byte> list = new ArrayList<>();
		for (byte arr : array) {
			list.add(arr);
		}
		return list;
	}
	
	/**
	 * @description 获取字符串的GBK字节流
	 * @param str 待转换的字符串
	 * @return
	 */
	private byte[] getGBKEncode(String str) {
		try {
			return str.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @description 串口发送消息反馈
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @param params 动态参数, params[0]为超时时间,默认为5s
	 * @throws Exception
	 */
	public void startListener(String portName, int baudrate, int... params) throws Exception {
		if (params != null && params.length > 0) {
			this.timeout = params[0];
		}
		serialPort = SerialTool.openPort(portName, baudrate);
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
							if (data != null && data.length > 2) {
								Message message = new Message(data, SerialTool.bytesToHexString(data));
								if (data[1] == 0x23 && data[2] == 0x12) { // 打印机有纸状态
									send(createData());
								} else if (data[1] == 0x23 && data[2] == 0x1A) { //  打印机无纸状态
									receiveFlag = true;
									message.setType(ReturnMsgType.PRINT_NO_PAPER);
									receiveMessage(message);
								} else if(data[1] == 0x4F && data[2] == 0x4B) { // 打印成功
									receiveFlag = true;
									message.setType(ReturnMsgType.PRINT_SUCC);
									receiveMessage(message);
								} else if(data[1] == 0x6E && data[2] == 0x6F) { // 打印失败
									receiveFlag = true;
									message.setType(ReturnMsgType.PRINT_FIAL);
									receiveMessage(message);
								} else {
									logger.error("返回未知类型信号:" + SerialTool.bytesToHexString(data));
								}
							}
						} catch (Exception e) {
							logger.error("读取串口数据失败:" + e.getMessage());
							e.printStackTrace();
						}
					}
					break;
				}
			}
		});
	}
	
	/**
	 * @description 发送数据到串口
	 * @param dataArray 数据数组
	 */
	private void send(Byte[] dataArray) {
		try {
			byte[] data = new byte[dataArray.length];
			for (int i = 0; i < dataArray.length; i++) {
				data[i] = dataArray[i].byteValue();
			}
			logger.info("发送数据到打印机:" + SerialTool.bytesToHexString(data));
			SerialTool.sendToPort(this.serialPort, data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * @description 读取反馈信号状态
	 */
	class CheckStatus implements Runnable {
		@Override
		public void run() {
			long start = System.currentTimeMillis();
			try {
				while (true) {
					if (System.currentTimeMillis() - start > timeout * 1000 || receiveFlag) {
						if (receiveFlag) {
							logger.info("已经等待到监听结果");
						} else {
							Message message = new Message(null, null);
							if (isAutoCut != null && isAutoCut) {
								message.setType(ReturnMsgType.UNRECEIVED);
							} else {
								message.setType(ReturnMsgType.PRINT_SUCC);
							}
							receiveMessage(message);
						}
						lock.lock();
						try {
							condition.signal();
						} finally {
							lock.unlock();
						}
						break;
					} else {
						logger.info("等待监听结果");
                        Thread.sleep(500);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @description 读取打印机纸张状态
	 */
	private void readPaperStatus() {
		this.send(new Byte[] {0x10, 0x04, 0x01});
	}
	
	/**
	 * @description 打印消息启动
	 */
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					while (true) {
						try {
							info = printInfoQueue.take();
							// 自带切刀打印机启动监测纸状态
							if (isAutoCut != null && isAutoCut) {
								readPaperStatus();
							} else { // 非自动切刀打印机设置超时时间为3S
								timeout = 3;
								send(createData());
							}
							new Thread(new CheckStatus()).start();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				} finally {
					lock.unlock();
				}
			}
		}).start();
	}

	/**
	 * @description 接收消息
	 * @param message 消息内容
	 * @return
	 */
	public abstract void receiveMessage(Message message);
}