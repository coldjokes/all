package com.cnbaosi.determinant;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.dto.IndexPair;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.box.BoxMedium;
import com.cnbaosi.workspace.check.ConnectCheck;
import com.cnbaosi.workspace.door.DoorCheck;
import com.cnbaosi.workspace.door.DoorOp;
import com.cnbaosi.workspace.door.DoorOpen;
import com.cnbaosi.workspace.door.DoorReader;
import com.cnbaosi.workspace.hopper.Hopper;
import com.cnbaosi.workspace.hopper.HopperOp;
import com.cnbaosi.workspace.hopper.HopperReset;
import com.cnbaosi.workspace.spring.B3Cabinet;
import com.cnbaosi.workspace.spring.Door;
import com.cnbaosi.workspace.spring.MiniCabinet;
import com.cnbaosi.workspace.spring.Screw;
import com.cnbaosi.workspace.spring.SpringCabinet;
import com.cnbaosi.workspace.spring.StressTest;

/**
 * @description 柜体领料抽象类
 * @author guozhidong
 *
 */
public final class StorageMediumPicker {

	private static final Logger logger = LoggerFactory.getLogger(StorageMediumPicker.class);
	
	// 仓储介质集合
	public static Map<Byte, StorageMedium> storageMap = new HashMap<>();
	
	// 仓储介质队列
	public static BlockingQueue<StorageMedium> storageQueue = new LinkedBlockingQueue<>();
	
	// 栈号映射对照 key 当前栈号, value 主栈号
	public static Map<Byte, Byte> boardNoMapping = new HashMap<>();
	
	// 主控柜门集合(待开门状态)
	public static Map<Byte, Map<Byte, Door>> mainDoorMap = new HashMap<>(); 
	// 储物柜状态
	public static Map<Byte, Map<Integer, Boolean>> boxStatusMap = new HashMap<>();
	
	/**
	 * @description 设置仓储介质到队列中
	 * @param boardNo 栈号
	 * @param storage 仓储对象
	 */
	public static void putStorageMedium(StorageMedium storage) {
		storageMap.put(storage.getMainBoardNo(), storage);
		// 连接校验
		if (storage instanceof ConnectCheck) {
			logger.info("连接检测,设置栈号映射关系");
			ConnectCheck check = (ConnectCheck) storage;
			boardNoMapping.put(check.getMainBoardNo(), check.getMainBoardNo());
		}
		// 微型柜介质
		if (storage instanceof MiniCabinet) {
			logger.info("当前操作媒介为微型柜,设置栈号映射关系");
			MiniCabinet cabinet = (MiniCabinet) storage;
			boardNoMapping.put(cabinet.getMainBoardNo(), cabinet.getMainBoardNo());
			Map<Byte, Door> doorMap = mainDoorMap.get(cabinet.getGroupBoardNo());
			if (doorMap == null) {
				doorMap = new HashMap<>();
			}
			doorMap.put(cabinet.getMainBoardNo(), new Door("", "", 0));
			mainDoorMap.put(cabinet.getGroupBoardNo(), doorMap);
		}
		// 弹簧柜介质
		if (storage instanceof SpringCabinet) {
			logger.info("当前操作媒体为弹簧柜,设置栈号映射关系");
			SpringCabinet cabinet = (SpringCabinet) storage;
			Map<Byte, Door> doorMap = mainDoorMap.get(cabinet.getGroupBoardNo());
			if (doorMap == null) {
				doorMap = new HashMap<>();
			}
			for (Entry<Byte, Door> entry : cabinet.getDoorMap().entrySet()) {
				boardNoMapping.put(entry.getKey(), cabinet.getMainBoardNo());
				doorMap.put(entry.getKey(), entry.getValue());
			}
			mainDoorMap.put(cabinet.getGroupBoardNo(), doorMap);
		}
		// B3柜
		if (storage instanceof B3Cabinet) {
			logger.info("当前操作媒体为B3,设置栈号映射关系");
			B3Cabinet cabinet = (B3Cabinet) storage;
			Map<Byte, Door> doorMap = mainDoorMap.get(cabinet.getGroupBoardNo());
			if (doorMap == null) {
				doorMap = new HashMap<>();
			}
			doorMap.put(cabinet.getMainBoardNo(), new Door("", "", 0));
			mainDoorMap.put(cabinet.getGroupBoardNo(), doorMap);
		}
		// 储物柜介质
		if (storage instanceof BoxMedium) {
			logger.info("当前操作媒体为储物柜,设置栈号映射关系");
			BoxMedium box = (BoxMedium) storage;
			Map<Integer, Boolean> statusMap = boxStatusMap.get(box.getGroupBoardNo());
			if (statusMap == null) {
				statusMap = new HashMap<>();
			}
			for (IndexPair indexPair : box.getIndexPairList()) {
				statusMap.put(indexPair.getBoxIndex(), false);
			}
			boxStatusMap.put(box.getGroupBoardNo(), statusMap);
		}
		// 门操作或门检测
		if (storage instanceof DoorOp || storage instanceof DoorCheck || storage instanceof DoorOpen) {
			logger.info("当前操作媒体为门操作,设置栈号映射关系");
			DoorReader doorReader = (DoorReader) storage;
			for (Entry<Byte, Door> entry : doorReader.getDoorMap().entrySet()) {
				boardNoMapping.put(entry.getKey(), doorReader.getMainBoardNo());
			}
		}
		// 料斗操作或料斗复位
		if (storage instanceof HopperOp || storage instanceof HopperReset) {
			logger.info("当前操作媒体料斗操作或料斗复位,设置门栈号映射关系");
			Hopper hopper = (Hopper) storage;
			for (Entry<Byte, Door> entry : hopper.getDoorMap().entrySet()) {
				boardNoMapping.put(entry.getKey(), hopper.getMainBoardNo());
			}
		}
		if (storage instanceof Screw || storage instanceof StressTest) {
			logger.info("弹簧测试程序或压力测试程序");
			boardNoMapping.put(storage.getGroupBoardNo(), storage.getMainBoardNo());
		}
		try {
			storageQueue.put(storage);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @description 线程启动
	 */
	public void start() {
		while(true) {
			try {
				StorageMedium storage = storageQueue.take();
				storage.start();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}