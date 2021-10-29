package com.cnbaosi.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.util.MessageConsume;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.spring.Door;
import com.cnbaosi.workspace.spring.Lattice;
import com.cnbaosi.workspace.spring.Level;
import com.cnbaosi.workspace.spring.RecordStatus;
import com.cnbaosi.workspace.spring.SpringCabinet;

/**
 * @description 测试弹簧柜
 * @author guozhidong
 *
 */
public class TestSpringCabinet {
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
						consume.startListener("COM12", 19200);
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
			
			Byte groupBoardNo = 0x00;
			
			Byte mainBoardNo = 0x00;			
			Byte rightBoardNo = 0x01;
			
			Map<Byte, Door> doorMap = new HashMap<>();
			BlockingQueue<Level> levelQueue = new LinkedBlockingQueue<>();
			Door door;
			Level level;
			Map<Byte, Map<String, Lattice>> latticeGroupMap;
			Map<String, Lattice> latticeMap;
			
			Lattice lattice;
			
			
			// 门一
			door = new Door("左边", "LEFT", 326);
			doorMap.put((byte) 0, door);
			// 门二
			door = new Door("右边", "RIGHT", 326);
			doorMap.put((byte) 0x01, door);
			
			// 层
			level = new Level();
			level.setRowNo(9);
			level.setRowHeight(50 + (level.getRowNo() - 1) * 175);
			
			// 层分组
			latticeGroupMap = new HashMap<>();
			// 一板
			latticeMap = new HashMap<>();
			// 货道
			lattice = new Lattice(1, 97);
			lattice.getStatusMap().put("1A", new RecordStatus(1));
			lattice.getStatusMap().put("1B", new RecordStatus(1));
			latticeMap.put("091", lattice);
			lattice = new Lattice(2, 98);
			lattice.getStatusMap().put("1C", new RecordStatus(1));
			lattice.getStatusMap().put("1D", new RecordStatus(1));
			latticeMap.put("092", lattice);
			latticeGroupMap.put(mainBoardNo, latticeMap);
			// 二板
			latticeMap = new HashMap<>();
			lattice = new Lattice(1, 97);
			lattice.getStatusMap().put("2A", new RecordStatus(1));
			lattice.getStatusMap().put("2B", new RecordStatus(1));
			latticeMap.put("191", lattice);
			lattice = new Lattice(2, 98);
			lattice.getStatusMap().put("2C", new RecordStatus(1));
			lattice.getStatusMap().put("2D", new RecordStatus(1));
			latticeMap.put("192", lattice);
			latticeGroupMap.put(rightBoardNo, latticeMap);
			// 设置层分组
			level.setLatticeGroupMap(latticeGroupMap);				
			// 设置行队列
			levelQueue.put(level);
			
			StorageMedium storage = new SpringCabinet(groupBoardNo, mainBoardNo, doorMap, levelQueue) {
				@Override
				public void receiveMessage(Message message) {
					System.out.println(message.toString());
				}
				
				@Override
				public void notice(String recordId, String staId, Boolean... params) {
					System.out.println("recordId:" + recordId + ";statId:" + staId + ";flag:" + (params.length > 0 ? params[0] : "null"));
				}
			};
			
			StorageMediumPicker.putStorageMedium(storage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}