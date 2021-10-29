package com.dosth.test;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.op.CloseDoor;
import com.dosth.instruction.board.op.OffLight;

public class CloseDoor2 {
	public static void main(String[] args) {
		try {
			Board.startSerialPort("COM3", 9600);
			Board.receive();
			// 关门
			new CloseDoor().start();
			Thread.sleep(200);
			// 关灯
			new OffLight().start();
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Board.close();
		}
	}
}