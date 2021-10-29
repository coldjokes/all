package com.cnbaosi.common;

import java.util.HashMap;
import java.util.Map;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.util.MessageConsume;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.door.DoorOp;
import com.cnbaosi.workspace.spring.Door;

/**
 * @description 测试门操作
 * @author guozhidong
 *
 */
public class TestDoorOp {
	public static void main(String[] args) {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					MessageConsume consume = new MessageConsume() {
						@Override
						public void receiveMessage(Message message) {
							System.err.println(message.toString());
						}
					};
					try {
						consume.startListener("COM12", 9600);
					} catch (Exception e) {
						e.printStackTrace();
					}
					consume.start();
				}
			}).start();
			
			StorageMediumPicker picker = new StorageMediumPicker();
			new Thread(new Runnable() {
				@Override
				public void run() {
					picker.start();
				}
			}).start();
			
			Map<Byte, Door> doorMap = new HashMap<>();
			doorMap.put((byte)0, new Door("左柜", "LEFT", 346));
			doorMap.put((byte)1, new Door("右柜", "RIGHT", 346));
			StorageMedium door = new DoorOp((byte) 0, doorMap, false) {
				@Override
				public void receiveMessage(Message message) {
					System.err.println(message.toString());
				}
			};
			
			StorageMediumPicker.putStorageMedium(door);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}