package com.dosth.toolcabinet.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.dto.BorrowNotice;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.OpTip;

/**
 * @description 领用通讯工具类
 * @author guozhidong
 *
 */
@Component
public class BorrowNoticeUtil {
	private static final Logger logger = LoggerFactory.getLogger(BorrowNoticeUtil.class);
	
	@Autowired
	private ToolService toolService;
	
	private BlockingQueue<BorrowNotice> queue = new LinkedBlockingQueue<>();
	
	public void put(BorrowNotice notice) throws InterruptedException {
		this.queue.put(notice);
	}
	
	/**
	 * @description 启动
	 */
	public void start() {
		logger.info("领用通讯工具类启动");
		new Thread(new Runnable() {
			@Override
			public void run() {
				BorrowNotice tmp = null;
				while(true) {
					try {
						BorrowNotice notice = queue.take();
						tmp = notice;
						OpTip tip = null;
						FutureTask<OpTip> task = new FutureTask<>(new Callable<OpTip>() {
							@Override
							public OpTip call() throws Exception {
								return toolService.notice(notice);
							}
						});
						new Thread(task).start();
						tip = task.get();
						if (tip.getCode() == 200) {
							logger.info("领用信息同步成功:" + notice.toString());
						} else {
							logger.info("领用信息同步失败:" + notice.toString());
						}
					} catch (InterruptedException | ExecutionException e) {
						logger.error("领用同步信息发送失败:" + tmp.toString());
						e.printStackTrace();
					}
				}	
			}
		}).start();
	}
}