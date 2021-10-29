package com.dosth.toolcabinet.commu;

import com.dosth.toolcabinet.commu.iccard.ICReaderWriter;
import com.dosth.toolcabinet.commu.iccard.ICUser;

public class ICCompoment {

	private ICReaderWriter ic;
	private ICUser icUser;
	private String ICCOM;
	private boolean connMonitor = true;
	
	private static ICCompoment icCompo;
	
	private ICCompoment(String icComm){
	   this.ICCOM = icComm;
	   icUser = new ICUser();
	   ic = ICReaderWriter.getICInstance(icUser,ICCOM);
       icUser.setICObj(ic);
	}
	
	public static ICCompoment getICCompoInstance(String icComm){
		icCompo = new ICCompoment(icComm);
        return icCompo;
    }
	
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
		ic.setReadUIDFlag(true);
		ic.startReadThread();
	}
	
	public void stopMonitorICPin() {
		ic.stopReadThread();;
	}
	
	public void startLoginMointor() {
		icUser.setMode(icUser.Log_In_Monitor_Mode);
		startMonitorICPin();
	}
}