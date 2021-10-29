package com.dosth.comm.plc;

import com.dosth.comm.PLCCompoment;
import com.dosth.constant.EnumDoor;

public class PLCDoorStatsReader extends PLCStatusReader {

	int TIMEOUT = 10;
	private EnumDoor door;

	public PLCDoorStatsReader(String ip, int i, int slave_id, PLCCompoment plcCompoment, EnumDoor door) {
		super(ip, i, slave_id, plcCompoment);
		this.door = door;
	}

	public void run() {
		boolean opened = isDoorOpened(TIMEOUT);
		processer.onDoorOpened(opened);
	}

	private boolean isDoorOpened(int timeout) {
		boolean isOpen = false;
		long dur = timeout * 1000;
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		while ((now - start) < dur) {
			try {
				if (door.equals(EnumDoor.LEFT) || door.equals(EnumDoor.ALL)) {
					isOpen = readCoilValue(PLCUtil.ON_CLOSE_AND_CHECK_COIL);
				} else if (door.equals(EnumDoor.RIGHT) || door.equals(EnumDoor.ALL)) {
					isOpen = readCoilValue(PLCUtil.ON_CLOSE_AND_CHECK2_COIL);
				}
			} catch (Exception e) {
				e.printStackTrace();
				isOpen = false; // exception
			}
			if (isOpen) {
				break; // opened
			}
			now = System.currentTimeMillis();
		}
		return isOpen;
	}
}