package com.dosth.websocket.util;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.dosth.websocket.constant.WebSocketConstant;
import com.dosth.websocket.dto.WsMsg;

/**
 * WebSocket工具类
 * @author guozhidong
 *
 */
public class WebSocketUtil {

	private static SimpMessagingTemplate template;
	
	public WebSocketUtil(SimpMessagingTemplate template) {
		WebSocketUtil.template = template;
	}

	public static void sendMsgBroadcast(WsMsg msg) {
		template.convertAndSend(WebSocketConstant.PRODUCER_PATH, msg);
	}

	public static void sendMsgSingle(String customerId, WsMsg msg) {
		template.convertAndSendToUser(customerId, WebSocketConstant.P2P_PUSH_PATH, msg);
	}
}