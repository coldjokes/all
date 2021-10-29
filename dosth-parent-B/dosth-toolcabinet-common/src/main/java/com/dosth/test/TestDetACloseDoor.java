package com.dosth.test;

import com.dosth.instruction.board.util.DetACloseDoor;

public class TestDetACloseDoor {
	public static void main(String[] args) {
		DetACloseDoor closeDoor = new DetACloseDoor() {			
			@Override
			public void receiveMessage(String message) {
				System.err.println(message);
			}
		};
		try {
			closeDoor.start("COM6", 9600);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}