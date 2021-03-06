package com.dosth.websocket.constant;

/**
 * WebSocket常量
 * 
 * @author guozhidong
 *
 */
public class WebSocketConstant {
	/** WebSocket路径前缀 */
	public static String WEBSOCKET_PATH_PREFIX = "/ws-push";
	/** WebSocket路径 */
	public static String WEBSOCKET_PATH = "/endpointModbusStatus";
	/** 消息代理路径 */
	public static String WEB_SOCKET_BROADCAST_PATH = "/topic";
	/** 前端发送给服务端请求地址 */
	public static final String FORE_TO_SERVER_PATH = "/welcome";
	/** 服务端生产地址,客户端订阅此地址以接收服务端生产的消息 */
	public static final String PRODUCER_PATH = "/topic/getModbusStatus";
	/** 点对点消息推送地址前缀 */
	public static final String P2P_PUSH_BASEPATH = "/modbus";
	/** 点对点消息推送地址后缀,地址格式如:/user/用户识别码/msg */
	public static final String P2P_PUSH_PATH = "/status";
}