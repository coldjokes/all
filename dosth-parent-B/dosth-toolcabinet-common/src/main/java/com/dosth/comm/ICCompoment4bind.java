package com.dosth.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.iccard4bind.ICReaderWriter4bind;
import com.dosth.comm.iccard4bind.ICUser4bind;

public class ICCompoment4bind {
	
	private static final Logger logger = LoggerFactory.getLogger(ICCompoment4bind.class);

	private ICReaderWriter4bind ic;
	private ICUser4bind icUser;
	private String ICCOM = "";
	private boolean connMonitor = true;
	
//	private static ICCompoment4bind icCompo=new ICCompoment4bind();
	private boolean DEBUG = true;
	
	public ICCompoment4bind(String ICCOM ){
	   this.ICCOM = ICCOM;
	   icUser = new ICUser4bind();
	   ic = ICReaderWriter4bind.getICInstance(icUser,this.ICCOM);
       icUser.setICObj(ic);
	}
	
//	public static ICCompoment4bind getICCompoInstance(){
//
//        return icCompo;
//    }
	
	// before it,should call ic.stopReadThread(),after it call startReadThread()
	public synchronized String getICUID() {
		icUser.setMode(icUser.Bind_User_Mode);
		ic.stopReadThread();
		
		String uid = "666666";
		if(!ic.isCOMPortAvailabe(ICCOM) || !ic.isCOMConnected()) {
			connMonitor = false;
		}
		
		if(!connMonitor) {
			ic.connCOM(ICCOM);
			connMonitor = true;
			return uid;
		}
			
		ic.setReadUIDFlag(true);
		icUser.setDoneFlag(false);
		ic.startReadUID();
		
		int timeout = 5; // 5s
		long dur = timeout*1000; // millsecond
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		while(!icUser.isDone() && (now-start)<dur) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			now = System.currentTimeMillis();
		}

		String strReaded = icUser.getStrOnIC();
		if(strReaded!=null) {
			uid = strReaded;
			icUser.clearStrOnIC();
		}
		icUser.setMode(icUser.Log_In_Monitor_Mode);
		
		String str = uid.toUpperCase();
		uid = "666666";
		return str;
	}
	
	public void startMonitorICPin() {
		if(DEBUG)
			logger.debug("Enter ICCompoment4bind.startMonitorICPin()");
		ic.setReadUIDFlag(true);
		ic.startReadThread();
	}
	
	public void stopMonitorICPin() {
		ic.stopReadThread();;
	}
	
	public void startLoginMointor() {
		if(DEBUG)
			logger.debug("Enter ICCompoment4bind.startLoginMointor()");
		icUser.setMode(icUser.Log_In_Monitor_Mode);
		startMonitorICPin();
	}
}