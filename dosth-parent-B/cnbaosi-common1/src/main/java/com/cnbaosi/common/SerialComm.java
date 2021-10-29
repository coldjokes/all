package com.cnbaosi.common;

import com.cnbaosi.util.SerialTool;

import gnu.io.SerialPort;

/**
 * @description 串口公共类
 * @author guozhidong
 *
 */
public class SerialComm {
	protected SerialPort serialPort;
	
	 /**
     * @description 串口启动
     * @param portName 串口名称
     * @param baudrate 波特率
     * @throws Exception
     */
    protected void startSerialPort(String portName, int baudrate) throws Exception {
    	if (SerialTool.checkPort(portName)) {
    		SerialTool.closePort(serialPort);
    	}
        serialPort = SerialTool.openPort(portName, baudrate);
    }

    /**
     * @description 关闭端口
     */
    protected void closePort() {
    	SerialTool.closePort(serialPort);
    }
}