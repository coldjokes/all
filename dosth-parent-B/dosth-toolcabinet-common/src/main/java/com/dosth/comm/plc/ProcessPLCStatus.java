package com.dosth.comm.plc;

public interface ProcessPLCStatus {
	
	public void onLiftArrive() ;
	public void onLiftNotArrive() ;  // lift not arrive within timeout limet
	public void onScanSignal();
	public void onDoorOpened(boolean isOpened);
}
