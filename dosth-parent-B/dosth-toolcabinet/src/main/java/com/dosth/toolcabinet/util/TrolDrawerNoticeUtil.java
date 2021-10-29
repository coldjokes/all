package com.dosth.toolcabinet.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.dto.TrolDrawerNotice;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.OpTip;

/**
 * @description 可控抽屉柜通知
 * @author Zhidong.Guo
 *
 */
@Component
public class TrolDrawerNoticeUtil {
	private static final Logger logger = LoggerFactory.getLogger(TrolDrawerNoticeUtil.class);
	
	@Autowired
	private ToolService toolService;
	
	private BlockingQueue<TrolDrawerNotice> queue = new LinkedBlockingQueue<>();
	
	public void put(TrolDrawerNotice notice) throws InterruptedException {
		this.queue.put(notice);
	}
	
	public void start() {
		logger.info("可控抽屉柜工具类启动");
		new Thread(() -> {
			TrolDrawerNotice notice = null;
			while (true) {
				try {
					notice = queue.take();
					OpTip tip = toolService.notice(notice);
					if (tip.getCode() == 200) {
						logger.info("可控抽屉柜数据同步成功:" + notice.toString());
					} else {
						logger.info("可控抽屉柜数据同步失败:" + notice.toString());
					}
				} catch (InterruptedException e) {
					logger.error("可控抽屉柜数据发送失败:" + notice.toString());
					e.printStackTrace();
				}
			}
		}).start();
	}
}