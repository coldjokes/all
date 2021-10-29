package com.dosth.comm.plc;

import com.dosth.comm.PLCCompoment;

public class PLCScanerReader extends PLCStatusReader {
	
	public PLCScanerReader(String ip, int i, int slave_id, PLCCompoment plcCompoment) {
		super(ip,i,slave_id,plcCompoment);
	}

	public void run() {
		while(true) {
			if(isScanSiganlSet()) {
				processer.onScanSignal();
			}
		}
	}
	
	private boolean isScanSiganlSet() {
		boolean flag = false;
		try {
			flag = readCoilValue(PLCUtil.ON_SCAN_COIL);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false; // exception
		}
		return flag;
	}	
}