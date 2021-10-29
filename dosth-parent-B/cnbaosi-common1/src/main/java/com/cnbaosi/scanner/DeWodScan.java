package com.cnbaosi.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.SerialComm;
import com.cnbaosi.util.SerialTool;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @description 德沃扫描仪
 * @author guozhidong
 *
 */
@SuppressWarnings("deprecation")
public abstract class DeWodScan extends SerialComm {
	private static final Logger logger = LoggerFactory.getLogger(DeWodScan.class);
    
    private static Thread thread;
    // 超时
    private int timeout = 60;
    // 扫描状态
    private boolean scanFlag = false;
    
    /**
     * @description 串口发送消息反馈
     * @param portName 串口名称
     * @param baudrate 波特率
     * @throws Exception
     */
    private void startListener(String portName, int baudrate) throws Exception {
        startSerialPort(portName, baudrate);
        logger.info("添加监听");
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
                            scanFlag = true;
                            StringBuilder builder = new StringBuilder();
                            for (int index = 0; index < data.length; index++) {
                                builder.append((char) Integer.valueOf(String.valueOf(data[index])).intValue());
                            }
                            receiveScanResult(builder.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        });
    }
    
    /**
     * @description 启动扫描仪
     * @param portName 串口名称
     * @param baudrate 波特率
     * @param params 其他参数 params[0], 超时时间,默认60秒
     * @throws Exception
     */
	public void start(String portName, int baudrate, int ... params) throws Exception {
        long start = System.currentTimeMillis();
        if (params != null && params.length > 0) {
            this.timeout = params[0];
        }
        if (thread != null && thread.isAlive()) {
        	logger.info("线程存在就销毁");
        	thread.stop();
        }
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
					startListener(portName, baudrate);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
            	try {
	                while (true) {
	                    if (System.currentTimeMillis() - start > timeout * 1000 || scanFlag) {
	                        logger.info((scanFlag ? "被扫描" : "超时") + ",关闭串口");
	                        break;
	                    } else {
	                        logger.info("等待扫描");
	                        try {
	                            Thread.sleep(500);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
            	} finally {
					closePort();
				}
            }
        });
        thread.start();
    }
 
    /**
     * @description 接收扫描结果
     * @param result 扫描结果
     */
    public abstract void receiveScanResult(String result);
}