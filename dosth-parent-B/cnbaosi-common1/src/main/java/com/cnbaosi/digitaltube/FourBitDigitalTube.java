package com.cnbaosi.digitaltube;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.util.SerialTool;

import gnu.io.SerialPort;

/**
 * @description 四位数码管
 * @author guozhidong
 *
 */
public class FourBitDigitalTube {
	private static final Logger logger = LoggerFactory.getLogger(FourBitDigitalTube.class);
	/**
	 * @description 串口对象
	 */
	private static SerialPort serialPort;
	// 发送队列
	private BlockingQueue<DigitalTubeData> sendQueue = new LinkedBlockingQueue<>();
	// 发送的数据
	private DigitalTubeData data;
	/**
	 * @description 时间间隔
	 */
	private Integer interval = 2000;

	/**
	 * @description 设置数码管显示值
	 * @param addrNo 地址码
	 * @param numStr 显示码集合,如行号1,列号1;行号2,列号2;......;行号N,列号N;
	 */
	public void put(Integer addrNo, String numStr) {
		sendQueue.clear();
		String[] nums = numStr.split(";");
		String[] rowCol;
		int row;
		int col;
		for (int i = 0; i < nums.length; i++) {
			rowCol = nums[i].split(",");
			if (rowCol.length != 2) { // 不合规范数据忽略
				continue;
			}
			try {
				row = Integer.valueOf(rowCol[0]);
				col = Integer.valueOf(rowCol[1]);
			} catch (Exception e) {
				continue;
			}
			if (row < 0 || row > 9 || col < 0 || col > 10) { // 越界的数据忽略
				continue;
			}
			logger.info(row + "," + col + "加入到列表中");
			try {
				sendQueue.put(new DigitalTubeData(addrNo, row, col));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 启动
	 */
	public void start() {
		logger.info("数码管队列启动");
		while (true) {
			try {
				data = sendQueue.take();
				Thread.sleep(interval);
				logger.info("数码管打开数据:" + data.toString());
				SerialTool.sendToPort(serialPort, new StringBuilder("$00").append(data.getAddNo()).append(",").append(data.getRow()).append("-").append(data.getCol()).append("#").toString().getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @description 数码管清除
	 * @param addrNo 地址码
	 */
	public void clear(Integer addrNo) {
		try {
			logger.info("数码管清除");
			SerialTool.sendToPort(serialPort, new StringBuilder("$00").append(addrNo).append(",#").toString().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description 串口启动
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @param params 参数数组,params[0],命令间隔时长,默认2s
	 * @throws Exception
	 */
	public void startSerialPort(String portName, int baudrate, int... params) throws Exception {
		logger.info("数码管串口启动");
		serialPort = SerialTool.openPort(portName, baudrate);
		serialPort.notifyOnDataAvailable(true);
		serialPort.notifyOnBreakInterrupt(true);
		if (params != null && params.length > 0) {
			interval = params[0];
		}
	}

	/**
	 * @description 关闭端口
	 */
	public void closePort() {
		SerialTool.closePort(serialPort);
	}
}

/**
 * @description 数控管数据
 * @author guozhidong
 *
 */
class DigitalTubeData {
	private int addNo; // 地址号
	private int row; // 行号
	private int col; // 列号

	public DigitalTubeData(int addNo, int row, int col) {
		this.addNo = addNo;
		this.row = row;
		this.col = col;
	}

	public int getAddNo() {
		return this.addNo;
	}

	public int getRow() {
		return this.row;
	}

	public int getCol() {
		return this.col;
	}

	@Override
	public String toString() {
		return "DigitalTubeData [addNo=" + addNo + ", row=" + row + ", col=" + col + "]";
	}
}