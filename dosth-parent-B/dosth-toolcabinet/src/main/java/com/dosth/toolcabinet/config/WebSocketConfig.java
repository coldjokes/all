package com.dosth.toolcabinet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.dosth.websocket.constant.WebSocketConstant;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * WebSocket消息代理端点,即stomp服务端
 * 
 * @author guozhidong
 *
 */
@Configuration
@EnableWebSocketMessageBroker // 通过@EnableWebSocketMessageBroker 注解凯旗使用STOMP协议来传输基于代理（message broker）的消息
// 这时控制器支持使用@MessageMapping，就像使用@RequestMapping一样
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// 注册一个Stomp的节点(endpoint),并指定使用SockJS协议
		registry.addEndpoint(WebSocketConstant.WEBSOCKET_PATH).withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 服务端发送消息给客户端的域,多个用逗号隔开
		registry.enableSimpleBroker(WebSocketConstant.WEB_SOCKET_BROADCAST_PATH, WebSocketConstant.P2P_PUSH_BASEPATH);
		// 定义一对一推送的前缀
		registry.setUserDestinationPrefix(WebSocketConstant.P2P_PUSH_BASEPATH);
		// 定义websocket前缀
		registry.setApplicationDestinationPrefixes(WebSocketConstant.WEBSOCKET_PATH_PREFIX);
	}
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Bean
	public WebSocketUtil webSocketUtil() {
		return new WebSocketUtil(template);
	}
}