package com.dosth.test;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.op.OnLight;
import com.dosth.instruction.board.op.OpenDoor;

public class OpenDoor2 {
	public static void main(String[] args) {
		try {
			Board.startSerialPort("COM3", 9600);
			Board.receive();
			
			// 开门
			new OpenDoor().start();
			Thread.sleep(200);
			// 开灯
			new OnLight().start();
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Board.close();
		}
	}
}
