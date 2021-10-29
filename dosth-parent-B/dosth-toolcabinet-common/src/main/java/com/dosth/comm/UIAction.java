package com.dosth.comm;

public interface UIAction {
	
	/**
	 * @description The function will be called after the goods in the shopping cart are output from cabinet
	 * @param ShiftResEvent 
	 * @return
	 */
	public void processShiftRes(ShiftResEvent e);
	public void processDoorStas(boolean isDoorOpened);

}
