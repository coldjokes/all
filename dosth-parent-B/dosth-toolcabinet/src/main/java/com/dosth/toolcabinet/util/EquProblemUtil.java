package com.dosth.toolcabinet.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.toolcabinet.service.ToolService;

/**
 * @description 设备故障工具类
 * @author guozhidong
 *
 */
@Component
public final class EquProblemUtil {
	private static final Logger logger = LoggerFactory.getLogger(EquProblemUtil.class);
	
	@Autowired
	private ToolService toolService;
	
	/**
	 * @description 故障队列
	 */
	private static BlockingQueue<String> problemQueue = new LinkedBlockingQueue<>();

	/**
	 * @description 设置设备到队列
	 * @param staId 设备详情Id
	 * @throws InterruptedException
	 */
	public static void put(String staId) throws InterruptedException {
		problemQueue.put(staId);
	}
	
	/**
	 * @description 启动线程池执行故障报备
	 */
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String staId = null;
				Boolean result = null;
				while (true) {
					try {
						staId = problemQueue.take();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (staId != null && !"".equals(staId)) {
						result = toolService.equStaProblem(staId);
						if (result != null && !result) {
							logger.error("地址位:"+ staId+"故障状态更新失败");
						}
					}
				}
			}
		}).start();
	}
}