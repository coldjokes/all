package com.dosth.toolcabinet.util;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.comm.audio.MP3Player;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.DateUtil;
import com.dosth.util.OpTip;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * 
 * @description 归还工具类
 * @author guozhidong
 *
 */
@Component
public class ReturnBackUtil {

	private static final Logger logger = LoggerFactory.getLogger(ReturnBackUtil.class);

	@Autowired
	private ToolService toolService;

	/**
	 * @description 归还信息队列
	 */
	private static BlockingQueue<String> returnBackQueue = new LinkedBlockingQueue<>();

	/**
	 * @description 设置归还数据到队列
	 * @param returnBackInfo 归还数据
	 * @throws InterruptedException
	 */
	public static void putReturnBackInfo(String returnBackInfo) throws InterruptedException {
		returnBackQueue.put(returnBackInfo);
	}

	/**
	 * @description 启动线程池执行归还处理
	 */
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					logger.info("执行归还队列时间:" + DateUtil.getTime(new Date()));
					String returnInfo = null;
					try {
						returnInfo = returnBackQueue.take();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					try {
						if (returnInfo != null && !"".equals(returnInfo)) {
							String[] infos = returnInfo.split(";");
							if (infos.length < 0) {
								throw new Exception("数据异常");
							}
							OpTip result = toolService.returnBackSingle(infos[0].substring(1));
							if (result.getCode() != 200) {
								// web socket 推送到前端
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.RETURN_BACK_INFO, "归还失败"));
							} else {
								MP3Player.play("AUDIO_C2.mp3");
							}
						}
					} catch (Exception e) {
						logger.error("异常归还数据:" + returnInfo);
					}
				}
			}
		}).start();
	}
}