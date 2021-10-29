package com.dosth.toolcabinet.commu.iccard;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.IOReadWrite;
import com.dosth.comm.ReadSerialPort;
import com.dosth.util.SerialTool;

@SuppressWarnings("unused")
public class ICReaderWriter implements IOReadWrite{
	
	private static final Logger logger = LoggerFactory.getLogger(ICReaderWriter.class);
	
	private byte[] uidData = new byte[4];
	
	private ReadSerialPort sp; 
	
	private volatile String currCMD="";
	private boolean isCurrCMDSucc = false;
	private volatile String errMsg = "";
	private boolean isRead = true;   //true for read,false for write
	private volatile boolean isOneHandShakeDone = true;
	
	private final String IDLE = "IDLE";
	private final String ANTICOLL = "ANTICOLL";
	private final String SELECTED = "SELECTED";
	private final String LOADKEY = "LOADKEY";
	private final String AUTHEN = "AUTHEN";
	private final String READ = "READ";
	private final String WRITE = "WRITE";

	private List<Byte> readContentDataList = new ArrayList<>(); 
	private ICCardSwipe icUser;
	
	private int blockAddr = 5;
	private int blockNum = 3;
	private List<Byte> writeData = new ArrayList<>(); 
	private boolean isReadUID = false;
	private boolean isConnCOMSucc = true;
	private String COM = null;
	
	private static ICReaderWriter icObj=new ICReaderWriter();
	
	private boolean DEBUG = true;
	
	private ICReaderWriter() {
		sp = new ReadSerialPort(this);
	}
	
	public static ICReaderWriter getICInstance(ICCardSwipe icUser,String COM){
		icObj.COM = COM;
		icObj.connCOM(COM);
		icObj.icUser = icUser;
        return icObj;
    }
	
	public void connCOM(String COM) {
		
		try {
			sp.listPort();  
		    sp.selectPort(COM); 
		    sp.startRead(0);
		}
		catch(RuntimeException e) {
			isConnCOMSucc = false;
		}
	}
	
	public boolean isCOMConnected() {
		return isConnCOMSucc;
	}
	
	public boolean isCOMPortAvailabe(String portName) {
		boolean comExist = sp.isCOMPortAvailabe(portName);
		return comExist;
	}
	
	public void setUIDData(byte[] uidData) {
		
		for(int i=0;i<uidData.length;i++)
			this.uidData[i] = uidData[i];
	}
	
	public String getUID() {
		
		String uidStr = SerialTool.bytesToHexString(uidData);
		logger.info("getUID "+uidStr);

		return uidStr;
		
	}
	public void setPreCMDStatus(boolean isOK) {
		isCurrCMDSucc = isOK;
	}
	
	
	public byte[] getRequestIdleData() {
		
		byte[] data = {0x02, -0x50, 0x00, 0x30, 0x02, 0x00, 0x26, -0x5C, 0x03}; //0x80:-0x80,0xA4:-0x5C
		
		return data;
		
	}
	
	public byte[] getAntiCollData() {
		
		byte[] data = {0x02,-0x40,0x00,0x31,0x01,0x00,-0x10,0x03}; //0xC0:-0x40,0xF0:-0x10
		
		return data;
		
	}
	
	public byte[] getSelectData() {
		
//		02 D0 00 32 05 00 B4 DD 1D 49 DA 03
		byte[] data = {0x02, -0x30, 0x00, 0x32, 0x05, 0x00, 0x12, 0x12, 0x12, 0x12, 0x66, 0x03}; //0xD0:-0x30,0xDA:-0x26
		
		int  uidIndex = 6;
		data[uidIndex] = uidData[0];
		data[uidIndex+1] = uidData[1];
		data[uidIndex+2] = uidData[2];
		data[uidIndex+3] = uidData[3];
		
		int crcIndex = data.length-2;
		
		int s = 1;
		int e = crcIndex-1;
		byte[] crcInput = new byte[e-s+1];
		for(int i=s;i<=e;i++)
		{
			crcInput[i-s] = data[i];
		}
		data[crcIndex] = getCRC(crcInput);
				
		return data;
	}
	
   public byte[] getLoadKeyData() {
		
//	     02 E0 00 45 07 00 FF FF FF FF FF FF A2 03
	    //0xE0:-0x20,0xFF:-0x01,0xA2:-0x5E
		byte[] data = {0x02,-0x20,0x00,0x45,0x07,0x00,-0x01,-0x01,-0x01,-0x01,-0x01,-0x01,-0x5E,0x03};
		
		return data;
		
	}
   
   public byte[] getAuthenData() {
		
//	    02 F0 00 40 07 00 60 B4 DD 1D 49 00 EA 03
	   //0xF0:-0x10,0xB4:-0x4C,0xDD:-0x23,0xEA:-0x16
		byte[] data = {0x02,-0x10,0x00,0x40,0x07,0x00,0x60,0x12,0x12,0x12,0x12,0x00,0x66,0x03};
		
		int  uidIndex = 7;
		data[uidIndex] = uidData[0];
		data[uidIndex+1] = uidData[1];
		data[uidIndex+2] = uidData[2];
		data[uidIndex+3] = uidData[3];
		data[uidIndex+4] = (byte)blockAddr;
	
		//
		int crcIndex = data.length-2;
		int s = 1;
		int e = crcIndex-1;
		byte[] crcInput = new byte[e-s+1];
		for(int i=s;i<=e;i++)
		{
			crcInput[i-s] = data[i];
		}
		data[crcIndex] = getCRC(crcInput);
		
		return data;
	}
   
   public byte[] getReadCmdData() {
		
//	    02 80 00 41 03 00 00 01 C3 03
		byte[] data = {0x02, -0x80, 0x00, 0x41, 0x03, 0x00, 0x66, 0x77, -0x3D, 0x03}; //0xC3:-0x3D
		
		int blockIndex = 6;
		data[blockIndex] = (byte)blockAddr;
		data[blockIndex+1] = (byte)blockNum;
		
		int crcIndex = data.length-2;
		int s = 1;
		int e = crcIndex-1;
		byte[] crcInput = new byte[e-s+1];
		for(int i=s;i<=e;i++)
		{
			crcInput[i-s] = data[i];
		}
		data[crcIndex] = getCRC(crcInput);
		
		return data;
	}
   
   public byte[] getWriteCmdData() {
		
	    // 02 80 00 42 13 00 05 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 67 8A 38 03

	    //0x8A:-0x76
//		byte[] data = {0x02,-0x80,0x00,0x42,0x13,0x00,0x05,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x67,-0x76,0x38,0x03};

		byte[] head = {0x02,-0x80,0x00,0x42,0x13,0x00};
		byte[] addr = {(byte)blockAddr,(byte)blockNum};
		byte tail = 0x03;
		
		Byte[] ba = writeData.toArray(new Byte[] {});
		byte[] data = new byte[ba.length];
		for(int i=0;i<ba.length;i++) {
			data[i] = ba[i].byteValue();
		}
		
		int totalLen = head.length+addr.length+data.length+1+1;
		byte[] cmdData = new byte[totalLen];
		
		System.arraycopy(head, 0, cmdData, 0, head.length);
		System.arraycopy(addr, 0, cmdData, head.length, addr.length);
		System.arraycopy(data, 0, cmdData, head.length+addr.length, data.length);
		cmdData[totalLen-1] = tail;
		
		//
		int crcIndex = cmdData.length-2;
		int s = 1;
		int e = crcIndex-1;
		byte[] crcInput = new byte[e-s+1];
		for(int i=s;i<=e;i++)
		{
			crcInput[i-s] = cmdData[i];
		}
		cmdData[crcIndex] = getCRC(crcInput);
		
		return cmdData;
		
	}


	@Override
	public void write(byte[] data) {
		
		this.isCurrCMDSucc = false;
		sp.write(data);  
	}
	
	@Override
	public void processReadData(byte[] data) {

		String readStr = SerialTool.bytesToHexString(data);
        logger.info("ICReaderWriter 接收到端口返回数据(长度为"+readStr.length()/2+" byte)："+readStr);
        
        if(currCMD!="")
        	parseICResponse(data);  
        
	}

	
	private void parseICResponse(byte[] data) {
		
		int statusIndex = 4;
		int dataIndex = 5;
		byte status = data[statusIndex];
		isCurrCMDSucc = (status==0)?true:false;
		
		if(!isCurrCMDSucc) isOneHandShakeDone = true;
		
		switch (currCMD) {
			case IDLE:{
				
				if(isCurrCMDSucc) {
					currCMD = ANTICOLL;
					write(getAntiCollData());
				}
				else
					errMsg = IDLE;
				break;
			}
			case ANTICOLL:{
				
				if(isCurrCMDSucc) {
					uidData[0] = data[dataIndex];
					uidData[1] = data[dataIndex+1];
					uidData[2] = data[dataIndex+2];
					uidData[3] = data[dataIndex+3];
					logger.info("UID "+getUID());
					
					currCMD = SELECTED;
					write(getSelectData());
				}
				else
					errMsg = ANTICOLL;
				break;
			}
			case SELECTED:{
				if(isCurrCMDSucc) {
					currCMD = LOADKEY;
					write(getLoadKeyData());
				}
				else
					errMsg = SELECTED;
				break;
			}
			case LOADKEY:{
				if(isCurrCMDSucc) {
					currCMD = AUTHEN;
					write(getAuthenData());
				}
				else
					errMsg = LOADKEY;
				break;
			}
			case AUTHEN:{
				if(isCurrCMDSucc) {
					
					logger.info("authen succ");
					if(isRead) {
						logger.info("to send read cmd");
						currCMD = READ;
						write(getReadCmdData());
					}
					else {
						logger.info("to send write cmd");
						currCMD = WRITE;
						write(getWriteCmdData());
					}

				}
				else
					errMsg = AUTHEN;
				break;
			}
			case READ:{

				if(isCurrCMDSucc) {
					
					//read done
					// Length: 4, Data: 02 80 00 11  Length: 19, Data: 00 B4 DD 1D 49 3D 08 04 00 62 63 64 65 66 67 68 69 9D 03 
					int readDataEndIndex = data.length-3;
					readContentDataList.clear();
					for(int i=dataIndex;i<=readDataEndIndex;i++) {
						readContentDataList.add(data[i]);
					}
					
					Byte[] data1 = readContentDataList.toArray(new Byte[] {});
					String readDataStr = SerialTool.bytesToHexString(SerialTool.convertType(data1));
					logger.info("Read Content Data:"+readDataStr);
					
					logger.info("before callback,isOneHandShakeDone="+isOneHandShakeDone);
					if(isReadUIDMode())
						this.icUser.onICSwipe(this.getUID());
					else
						this.icUser.onICSwipe(readDataStr);
					
					isOneHandShakeDone = true;
					logger.info("after callback,isOneHandShakeDone="+isOneHandShakeDone);
				}
				else
					errMsg = READ;
				break;
			}
			case WRITE:{
				if(isCurrCMDSucc) {
					//write done
					isOneHandShakeDone = true;
				}
				else
					errMsg = WRITE;
				break;
			}
		}
	}
	
	public void prepareReadWrite() {
		
		errMsg = "";
		currCMD = IDLE;
		write(getRequestIdleData());
	}
	
	public void startReadUID() {
		
		isOneHandShakeDone = false;
		prepareReadWrite();
	}
	
	public void startRead() {
		
	}
	private void nextState() {
		
	}
	
	public boolean stopFlag = false;
	class Devices implements Runnable{ 
		
	    public void run(){ 
	    	while(!stopFlag)
			{
	    		if(isOneHandShakeDone) {
	    			isOneHandShakeDone = false;
	    			prepareReadWrite();
	    			
	    			if(errMsg!="") icUser.onICSwipe("Read IC failed at "+errMsg);
	    		}
	    	}	
		}
	}

	Devices devices = new Devices();
	Thread device;
	public void startReadThread(){
//		stopFlag = false;
//		device = new Thread(devices);
//		device.start();
	}
	
	public void stopReadThread(){
		if(device!=null && device.isAlive())
		{
			stopFlag = true;
		}
		
	}

	public void setReadFlag(boolean flag) {
		
		isRead = flag;
	}
	
	public byte getCRC(byte[] ba) {
		
		byte crc = ba[0];
		for(int i=1;i<ba.length;i++) {
			
			crc = (byte) (crc^ba[i]);
		}
		
		return crc;
	}
	
	public void close() {
		sp.close();
	}
	
	public void setBlockAddr(int addr) {
		
		blockAddr = addr;
	}
	
	public void setBlockNum(int count) {
		
		blockNum = count;
	}
	
	public void setReadUIDFlag(boolean flag) {
		isReadUID = flag;
	}
	
	public boolean isReadUIDMode() {
		
		return isReadUID;
	}
	public void setICWriteData(byte[] data) {
		
		writeData.clear();
		for(byte b:data) {
			writeData.add(b);
		}
	}
	
	public static void main(String[] string) {
		   
//		   ReadSerialPort sp = new ReadSerialPort();  
//	       sp.listPort();  
//	       sp.selectPort("COM2");  
//	       sp.write("123456 \n");  
//	       sp.write("hello \n");  
//	       sp.startRead(0);  
		   
		   ICUser icUsere = new ICUser();
//		   ICReaderWriter ic = new ICReaderWriter(icUsere,"COM3");
		   ICReaderWriter ic = ICReaderWriter.getICInstance(icUsere,"COM3");
		   ic.setReadUIDFlag(true);
		   ic.startReadUID();
		   
//		   byte[] wd = {0x01,0x02,0x03,0x01,0x02,0x03,0x01,0x02,0x03,0x01,0x02,0x03,0x01,0x02,0x03,0x66};
//		   ic.setICWriteData(wd);
//		   ic.setReadFlag(false);

//		   ic.startReadThread();
//		   
//		   try {
//			Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		   
//		   ic.stopReadThread();
		   
//		   byte[] a = {-0x50, 0x00, 0x30, 0x02, 0x00, 0x26};
//		   byte b = ic.getCRC(a);
//		   String hv = Integer.toHexString(b & 0xFF);
//		   logger.info("CRC "+hv);	   
		   
		   // An example that ic is used as reader,only keep reading for 10s
//		   ICUser icUsere = new ICUser();
//		   ICReaderWriter ic = new ICReaderWriter(icUsere);
//		   ic.startReadThread();
//		   try {
//			Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		   ic.stopReadThread();
	   }
}