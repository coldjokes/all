package com.dosth.toolcabinet.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.dosth.dto.Card;

/**
 * @Desc WebSocket数据工具类
 * 
 * @author guozhidong
 *
 */
public class WebSocketDataUtil {
	/**
	 * @Desc 获取板子列表状态
	 * 
	 * @param cardList 板子列表
	 * @return
	 */
	public static Map<String, Integer> getCardListStatus(List<Card> cardList) {
		Map<String, Integer> map = new HashMap<>();
		ExecutorService threadPool = Executors.newSingleThreadExecutor();
		cardList.forEach(card -> {
			Future<Integer[]> future = threadPool.submit(new Callable<Integer[]>() {
				@Override
				public Integer[] call() throws Exception {
					return new Integer[] {};
							
//							ModbusUtil.readSlotStatus(card.getHost(), card.getPort(), SlotAddrType.IndicatorLamp);
				}
			});
			try {
				Integer[] result = future.get();
				card.getLatticeList().forEach(lattice -> {
					map.put(lattice.getStaId(), result[lattice.getColNo() - 1]);
				});
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});
		return map;
	}
}