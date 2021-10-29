package com.dosth.test;

import com.dosth.instruction.board.util.BorrowUtil;

public class Test {
	public static void main(String[] args) {
		BorrowUtil util = BorrowUtil.getInstance();
		util.setQueue(Data.rowQueue);
		util.start("COM3", 9600);
	}
}