package com.dosth.toolcabinet.util;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.DateUtil;

/**
 * @description app领料工具类
 * @Author guozhidong
 */
@Component
public class AppBorrowUtil {

	public static final Logger logger = LoggerFactory.getLogger(AppBorrowUtil.class);

	@Autowired
	private ToolService toolService;

	/**
	 * @description app借出信息队列
	 */
	private static BlockingQueue<String> appBorrowQueue = new LinkedBlockingQueue<>();

	/**
	 * @description 设置app借出数据到队列
	 * @param appOrderId app借出订单Id
	 * @throws InterruptedException
	 */
	public static void putReturnBackInfo(String appOrderId) throws InterruptedException {
		appBorrowQueue.put(appOrderId);
	}

	/**
	 * @description 启动线程池执行app借出处理
	 */
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					logger.info("执行app借出队列时间:" + DateUtil.getTime(new Date()));
					String appBorrowOrderId = null;
					try {
						appBorrowOrderId = appBorrowQueue.take();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					try {
						if (appBorrowOrderId != null && !"".equals(appBorrowOrderId)) {
							toolService.setAppointmentCompletedByID(appBorrowOrderId, true);
						}
					} catch (Exception e) {
						logger.error("异常app订单编号:" + appBorrowOrderId);
					}
				}
			}
		}).start();
	}
}