package com.dosth.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class ReadSerialPort implements Runnable, SerialPortEventListener {

	private static final Logger logger = LoggerFactory.getLogger(ReadSerialPort.class);
	
    private String headStr = "IO测试";
    private int timeout = 2000;//open 端口时的等待时间
    private int threadTime = 0;
    private String sport;
    private CommPortIdentifier commPort;
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile List<Byte> readDataList = new ArrayList<Byte>();
    private boolean isSetEndMark = true;  // this is only for IC communication
    private int c = 0;
    
    private IOReadWrite reader;
    /**
     * @方法名称 :listPort
     * @功能描述 :列出所有可用的串口
     * @返回值类型 :void
     */
    @SuppressWarnings("rawtypes")
    public void listPort(){
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        
        logger.info("now to list all Port of this PC：" +en);
        
        while(en.hasMoreElements()){
            cpid = (CommPortIdentifier)en.nextElement();
            if(cpid.getPortType() == CommPortIdentifier.PORT_SERIAL){
                logger.info(cpid.getName() + ", " + cpid.getCurrentOwner());
                logger.warn("Roc "+cpid.getName() + ", " + cpid.getCurrentOwner());
            }
        }
    }
    
    public ReadSerialPort() {
    }

    public ReadSerialPort(IOReadWrite reader) {
    	
    	this.reader = reader;
    }
    
    /**
     * @方法名称 :selectPort
     * @功能描述 :选择一个端口，比如：COM1
     * @返回值类型 :void
     * @param portName
     */
    @SuppressWarnings("rawtypes")
    public void selectPort(String portName){
    	logger.warn("Roc "+"Enter selectPort(),port name: "+portName);
        this.commPort = null;
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        
        while(en.hasMoreElements()){
            cpid = (CommPortIdentifier)en.nextElement();
            if(cpid.getPortType() == CommPortIdentifier.PORT_SERIAL
                    && cpid.getName().equals(portName)){
                this.commPort = cpid;
                break;
            }
        }
        
        openPort(portName);
    }
    
    public boolean isCOMPortAvailabe(String portName) {
    	boolean exist = false;
    	
    	this.commPort = null;
        CommPortIdentifier cpid;
        Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
        
        while(en.hasMoreElements()){
            cpid = (CommPortIdentifier)en.nextElement();
            if(cpid.getPortType() == CommPortIdentifier.PORT_SERIAL
                    && cpid.getName().equals(portName)){
                this.commPort = cpid;
                exist = true;
                break;
            }
        }
        
        return exist;
    }
    /**
     * @方法名称 :openPort
     * @功能描述 :打开SerialPort
     * @返回值类型 :void
     */
    private void openPort(String portName){
        if(commPort == null)
            log(String.format("无法找到名字为'%1$s'的串口！", portName)); // TO DO xtp
        else{
            log("端口选择成功，当前端口："+commPort.getName()+",现在实例化 SerialPort:");
            try{
                serialPort = (SerialPort)commPort.open(headStr, timeout);
                log("实例 SerialPort 成功！");
            }catch(PortInUseException e){
            	logger.warn("Roc"+commPort.getName()+" 端口正在使用中！");
                throw new RuntimeException(String.format("端口'%1$s'正在使用中！", 
                        commPort.getName()));
            }
        }
    }
    
    /**
     * @方法名称 :checkPort
     * @功能描述 :检查端口是否正确连接
     * @返回值类型 :void
     */
    private void checkPort(){
        if(commPort == null)
            throw new RuntimeException("没有选择端口，请使用 " +
                    "selectPort(String portName) 方法选择端口");
        
        if(serialPort == null){
            throw new RuntimeException("SerialPort 对象无效！");
        }
    }
    
    /**
     * @方法名称 :write
     * @功能描述 :向端口发送数据，请在调用此方法前 先选择端口，并确定SerialPort正常打开！
     * @返回值类型 :void
     * @param message
     */
    public void write(String message) {
    	
        try{
        	checkPort();
            outputStream = new BufferedOutputStream(serialPort.getOutputStream());
        }catch(IOException e){
        	logger.warn("Roc "+"获取端口的OutputStream出错："+e.getMessage());
            throw new RuntimeException("获取端口的OutputStream出错："+e.getMessage());
        }
        
        try{
            outputStream.write(message.getBytes());
            log("信息发送成功！");
        }catch(IOException e){
        	logger.warn("Roc"+"向端口发送信息时出错："+e.getMessage());
            throw new RuntimeException("向端口发送信息时出错："+e.getMessage());
        }finally{
            try{
                outputStream.close();
            }catch(Exception e){ 
            }
        }
    }
    
    public void write(byte[] data) {
        
    	try {
    		checkPort();
    	}
    	catch(RuntimeException e) {
    		e.printStackTrace();
    		logger.warn("Roc"+" checkPort() exception："+e.getMessage());
    		return;
    	}
    	
        try{
            outputStream = new BufferedOutputStream(serialPort.getOutputStream());
        }catch(IOException e){
        	logger.warn("Roc "+"获取端口的OutputStream出错："+e.getMessage());
            throw new RuntimeException("获取端口的OutputStream出错："+e.getMessage());
        }
        
        try{
            outputStream.write(data);
            log("******信息发送成功！");
            String readStr="";
            readStr = bytesToHexString(data);
            if(readStr != null)
            	log("发送数据(长度为"+readStr.length()/2+" byte)："+readStr);
        }catch(IOException e){
        	logger.warn("Roc "+"向端口发送信息时出错："+e.getMessage());
            throw new RuntimeException("向端口发送信息时出错："+e.getMessage());
        }finally{
            try{
                outputStream.close();
            }catch(Exception e){
            }
        }
    }
    
    /**
     * @方法名称 :startRead
     * @功能描述 :开始监听从端口中接收的数据
     * @返回值类型 :void
     *    @param time  监听程序的存活时间，单位为秒，0 则是一直监听
     */
    public void startRead(int time){
        
        try{
        	checkPort();
            inputStream = new BufferedInputStream(serialPort.getInputStream());
        }catch(IOException e){
        	logger.warn("Roc "+"startRead() 获取端口的InputStream出错："+e.getMessage());
            throw new RuntimeException("获取端口的InputStream出错："+e.getMessage());
        }
        
        try{
            serialPort.addEventListener(this);
        }catch(TooManyListenersException e){
        	logger.warn("Roc "+"startRead() serialPort.addEventListener exception："+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        
        serialPort.notifyOnDataAvailable(true);
        
        log(String.format("开始监听来自'%1$s'的数据--------------", commPort.getName()));
        if(time > 0){
            this.threadTime = time*1000;
            Thread t = new Thread(this);
            t.start();
            log(String.format("监听程序将在%1$d秒后关闭。。。。", time));
        }
    }
    public void startRead(){
        checkPort();
        
        try{
            inputStream = new BufferedInputStream(serialPort.getInputStream());
        }catch(IOException e){
        	logger.warn("Roc "+"startRead() 获取端口的InputStream出错："+e.getMessage());
            throw new RuntimeException("获取端口的InputStream出错："+e.getMessage());
        }
        
        try{
            serialPort.addEventListener(this);
        }catch(TooManyListenersException e){
        	logger.warn("Roc "+"startRead() serialPort.addEventListener exception："+e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        
        serialPort.notifyOnDataAvailable(true);
        
        log(String.format("开始监听来自'%1$s'的数据--------------", commPort.getName()));
        
    }
    
    /**
     * @方法名称 :close
     * @功能描述 :关闭 SerialPort
     * @返回值类型 :void
     */
    public void close(){
        serialPort.close();
        serialPort = null;
        commPort = null;
    }
    
    
    public void log(String msg){
        logger.warn("Roc "+msg);
    }


    /**
     * 数据接收的监听处理函数
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent arg0) {
        switch(arg0.getEventType()){
        case SerialPortEvent.BI:/*Break interrupt,通讯中断*/ 
        case SerialPortEvent.OE:/*Overrun error，溢位错误*/  
        case SerialPortEvent.FE:/*Framing error，传帧错误*/
        case SerialPortEvent.PE:/*Parity error，校验错误*/
        case SerialPortEvent.CD:/*Carrier detect，载波检测*/
        case SerialPortEvent.CTS:/*Clear to send，清除发送*/ 
        case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/ 
        case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/ 
            break;
        case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端*/
            byte[] readBuffer = new byte[128];
            String readStr="";
            if(isSetEndMark) {
            	
            	try {
                    boolean isReadEnd = false; 
                    
                    if (inputStream.available() > 0) 
                    {   
                    	c++;
                        int readedNum = inputStream.read(readBuffer); 
                        log("No."+c+" readedNum "+readedNum);
                        byte[] readData = new byte[readedNum];
                		System.arraycopy(readBuffer, 0, readData, 0, readedNum);
                		log("before receive readDataList size "+readDataList.size());
                		for(int i=0;i<readedNum;i++)
                			readDataList.add(readData[i]);
                		log("after receive readDataList size "+readDataList.size());
                        
                		isReadEnd = isICDataFrameEnd(readDataList,(byte)0x03); 
                        
                    }
                    if(isReadEnd) {
                    	log("Receive the whole data frame");
                    	byte[] data = List2Array(readDataList);
                    	readDataList.clear();
                        readStr = bytesToHexString(data);
                        if(readStr != null)
                        	log("接收到端口返回数据(长度为"+readStr.length()/2+" byte)："+readStr);
                        else
                        	log("端口未返回数据");
                        reader.processReadData(data);
                        c = 0;
                    }  
                } catch (IOException e) {
                    e.printStackTrace();
                }
            	
//            	try {
//                    if (inputStream.available() > 0) 
//                    {   
//                        int readedNum = inputStream.read(readBuffer); 
//                        byte[] readData = new byte[readedNum];
//                		System.arraycopy(readBuffer, 0, readData, 0, readedNum);
//                		reader.processReadData(readData); 
//                		
//                		readStr = bytesToHexString(readData);
//                        if(readStr != null)
//                        	log("IC返回到端口返回数据(长度为"+readStr.length()/2+" byte)："+readStr);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
            else {
            	try {
                    boolean isReadEnd = false; 
                    
                    if (inputStream.available() > 0) 
                    {   c++;
                        int readedNum = inputStream.read(readBuffer); 
                        log("No."+c+" readedNum "+readedNum);
                        byte[] readData = new byte[readedNum];
                		System.arraycopy(readBuffer, 0, readData, 0, readedNum);
                		log("before receive readDataList size "+readDataList.size());
                		if(readedNum==11) {
                			readDataList.clear();
                			log("receive a whole data frame,clear stored bytes");
                		}
                		for(int i=0;i<readedNum;i++)
                			readDataList.add(readData[i]);
                		log("after receive readDataList size "+readDataList.size());
                        isReadEnd = isDataFrameEnd(readDataList); 
                        
                    }
                    if(isReadEnd) {
                    	log("Receive the whole data frame");
                    	byte[] data = List2Array(readDataList);
                    	readDataList.clear();
                        readStr = bytesToHexString(data);
                        if(readStr != null)
                        	log("接收到端口返回数据(长度为"+readStr.length()/2+" byte)："+readStr);
                        else
                        	log("端口未返回数据");
                        reader.processReadData(data);
                        c = 0;
                    }  
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void run() {
        try{
            Thread.sleep(threadTime);
            serialPort.close();
            log(String.format("端口''监听关闭了！", commPort.getName()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }
    
    private String bytesToHexString(byte[] bs) {
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
    
    public void setEndMark(boolean flag) {
    	
    	isSetEndMark = flag;
    }
    
    // parameter mark is an end character
    
    
    private boolean isICDataFrameEnd(List<Byte> readDataList,byte mark) {
    	
    	int lengthReaded = readDataList.size();
    	byte endByte = readDataList.get(lengthReaded-1);
    	if(endByte!=mark)
    		return false;
    	else {
    		if(lengthReaded<7)
    			return false;
    		else 
    			return true;
    	}
    }
    
    private boolean isDataFrameEnd(List<Byte> dataList) {
    	
    	log("Enter isDataFrameEnd()");
    	byte[] data = List2Array(readDataList);
    	String readStr="";readStr = bytesToHexString(data);
        if(readStr != null)
        	log("接收到端口返回数据(长度为"+readStr.length()/2+" byte)："+readStr);
        else
        	log("端口未返回数据");
        
    	boolean isEnd = false;
    	if(isSetEndMark) {
    		isEnd = true; // now don't consider the IC communication
		} else {
			if (dataList.size() < 11) {
				return false;
			} else if (dataList.size() > 11) {
				readDataList.clear();
				return false;
			}
    			
			if (isLockBoardDataFrame(dataList)) {
				int lenFromProtocol = getBoardDataFrameLen(dataList);
				if (dataList.size() >= lenFromProtocol) {
					isEnd = true;
				}
			} else {
				readDataList.clear();
				log("Roc invalid data frame,clear readDataList ");
			}
    	}
    	return isEnd;
    }
    
    private boolean isLockBoardDataFrame(List<Byte> dataList){
    	log("Roc Enter isLockBoardDataFrame()");
		boolean flag = false;
		if (dataList.size() < 4) {
			return flag;
		} else {
    		//57 4B 4C 59
    		//53 4B 4C 59
    		boolean flag1 = (dataList.get(0)==(byte)(0x57)) || (dataList.get(0)==(byte)(0x53));
    		boolean flag2 = (dataList.get(1)==(byte)(0x4B));
    		boolean flag3 = (dataList.get(2)==(byte)(0x4C));
    		boolean flag4 = (dataList.get(3)==(byte)(0x59));
    		flag = flag1&&flag2&&flag3&&flag4;
    	}
    	log("Roc isLockBoardDataFrame? "+flag);
//    	flag = true; //lockboard itself issue,not comply with protocal doc
    	return flag;
    }
    
    private int getBoardDataFrameLen(List<Byte> dataList) {
    	log("Roc Enter getBoardDataFrameLen ");
    	int len = 0;
		if (dataList.size() > 5)
    		len = (int)dataList.get(4);
    	log("Roc getBoardDataFrameLen? "+len);
    	return len;
    }
    
    private byte[] List2Array(List<Byte> byteArray) {
    	
    	Byte[] byteObjs = (Byte[]) byteArray.toArray(new Byte[] {});
    	int len = byteObjs.length;
    	byte[] data  = new byte[len];
		for (int i = 0; i < len; i++) {
    		data[i] = byteObjs[i].byteValue();
    	}
    	return data;
    }
}

// add codes here
//byte[] writeData = {-0x40,-0x10};
//write(writeData);