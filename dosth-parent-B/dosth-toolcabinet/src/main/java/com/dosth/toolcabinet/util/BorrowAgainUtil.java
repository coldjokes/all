package com.dosth.toolcabinet.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.commu.ConcreteMediator;
import com.dosth.toolcabinet.dto.BorrowInfo;
import com.dosth.toolcabinet.dto.enums.TrueOrFalse;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.DateUtil;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * 
 * @description 手机预约、以旧换新信息处理工具类
 * @author guozhidong
 *
 */
@Component
public class BorrowAgainUtil {

	public static final Logger logger = LoggerFactory.getLogger(BorrowAgainUtil.class);

	@Autowired
	private ConcreteMediator mediator;

	@Autowired
	private ToolService toolService;

	/**
	 * @description 领取信息队列
	 */
	private static BlockingQueue<BorrowInfo> borrowQueue = new LinkedBlockingQueue<>();

	/**
	 * @description 设置领取数据到队列
	 * @param borrowInfo 归还数据
	 * @throws InterruptedException
	 */
	public static void putBorrowInfo(BorrowInfo borrowInfo) throws InterruptedException {
		borrowQueue.put(borrowInfo);
	}

	/**
	 * @description 启动线程池执行归还处理
	 */
	public void start() {
		new Thread(new Runnable() {
			BorrowInfo borrowInfo = null;
			@Override
			public void run() {
				while (true) {
					logger.info("执行领料队列时间:" + DateUtil.getTime(new Date()));
					try {
						borrowInfo = borrowQueue.take();
						FutureTask<List<ExtraCabinet>> future = new FutureTask<>(new Callable<List<ExtraCabinet>>() {
							@Override
							public List<ExtraCabinet> call() throws Exception {
								// 暂存柜共享开关
								String shareSwitch = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
										SetupKey.TemCabinet.TEM_SHARE_SWITCH);
								if (StringUtils.isBlank(shareSwitch)) {
									shareSwitch = TrueOrFalse.FALSE.toString();
								}
								
								return toolService.sendCartToServer(DosthToolcabinetRunnerInit.mainCabinetId,
										borrowInfo.getCartList(), shareSwitch, borrowInfo.getAccountId());
							}
						});
						new Thread(future).start();
						try {
							List<ExtraCabinet> cabinetList = future.get();
							Map<ExtraCabinet, List<Card>> cardMap = new HashMap<>();
							for (ExtraCabinet cabinet : cabinetList) {
								cardMap.put(cabinet, cabinet.getCardList());
							}
							if (!cardMap.isEmpty()) {
								for (Entry<ExtraCabinet, List<Card>> entry : cardMap.entrySet()) {
									callMotor(borrowInfo.getCabinetId(), entry.getValue());
								}
							} else {
								logger.error("服务端取料信息处理失败" + borrowInfo != null ? borrowInfo.toString() : "取料信息空指针异常");
							}
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						logger.error("异常领取数据:" + borrowInfo != null ? borrowInfo.toString() : "取料信息空指针异常");
					}
				}
			}
		}).start();
	}

	/**
	 * @description 通讯马达板
	 * @param 柜子Id
	 * @param cardList 封装马达板数据 synchronized
	 */
	private synchronized void callMotor(String cabinetId, List<Card> cardList) {
		WebSocketUtil.sendMsgSingle(cabinetId, new WsMsg(WsMsgType.TEXT, "启动以旧换新...."));
		mediator.startGetingTheSameOneAsReturned(cardList);
	}
}