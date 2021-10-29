package com.dosth.tool.common.util;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.external.entity.ExternalFeginBorrow;
import com.dosth.util.DateUtil;

import net.sf.json.JSONObject;

/**
 * 
 * @description 取料推送工具类
 * @author chenlei
 *
 */
@Component
public class BorrowPostUtil {

	public static final Logger logger = LoggerFactory.getLogger(BorrowPostUtil.class);

	/**
	 * @description 信息处理线程池
	 */
	private ExecutorService service = Executors.newFixedThreadPool(10);

	@Autowired
	private ToolProperties toolProperties;

	/**
	 * @description 领用推送通知队列
	 */
	private static BlockingQueue<ExternalFeginBorrow> borrowPostQueue = new LinkedBlockingQueue<>();

	/**
	 * @description 设置领用推送到队列
	 * @param borrow 领用推送
	 * @throws InterruptedException
	 */
	public static void putBorrowPostInfo(ExternalFeginBorrow borrow) throws InterruptedException {
		borrowPostQueue.put(borrow);
	}

	/**
	 * @description 启动线程池执行领用推送
	 */
	public void start() {
		this.service.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					logger.info("执行领料推送时间:" + DateUtil.getTime(new Date()));
					ExternalFeginBorrow post = null;
					try {
						post = borrowPostQueue.take();
						logger.error("取料推送队列:" + post);
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					try {
						if (post != null) {
							RestTemplate restTemplate = new RestTemplate();
							JSONObject obj = JSONObject.fromObject(post);
							JSONObject result = restTemplate.postForObject(toolProperties.getBorrowPostUrl(), obj, JSONObject.class);
							logger.error("取料推送结果:" + result);
						}
					} catch (Exception e) {
						logger.error("异常推送预警通知:" + post.toString());
						e.printStackTrace();
					} 
				}
			}
		});
	}

	/**
	 * @description 关闭线程池
	 */
	public void stop() {
		this.service.shutdown();
	}
}