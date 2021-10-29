package com.dosth.comm;

public class ShiftResEvent {

	public int row = 0;
	public int colunm = 0;
	public int count = 0;
	public boolean isShiftSucc = false;
	public String msg = "";
	public int res_code = -1; // 1 for motor error

	ShiftResEvent(boolean isShiftSucc, String msg) {
		this.isShiftSucc = isShiftSucc;
		this.msg = msg;
	}

	ShiftResEvent(int row, int colunm, boolean isShiftSucc, String msg, int res_code) {
		this.row = row;
		this.colunm = colunm;
		this.isShiftSucc = isShiftSucc;
		this.msg = msg;
		this.res_code = res_code;
	}

	ShiftResEvent(int row, int colunm, int count, boolean isShiftSucc, String msg, int res_code) {

		this.row = row;
		this.colunm = colunm;
		this.count = count;
		this.isShiftSucc = isShiftSucc;
		this.msg = msg;
		this.res_code = res_code;
	}
}
