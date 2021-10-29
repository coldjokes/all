package com.dosth.netty.util;

import java.util.HashMap;
import java.util.Map;

import com.dosth.netty.dto.AhnoProtocol;
import com.dosth.netty.dto.MsgObj;
import com.dosth.netty.dto.MsgType;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class GlobalUserUtil {
	// 保存全局的连接上服务器的客户
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	/**
	 * @description 客户端Channel集合
	 */
	public static Map<String, Channel> clientChannelMap = new HashMap<>();
	
	/**
	 * @description 输出指定信息类型的消息
	 * @param clientName 客户端名称
	 * @param msgType 消息类型
	 * @param msgContent 消息内容
	 */
	public static void writeMsg(String clientName, MsgType msgType, Object msgContent) {
		channels.forEach(channel -> {
			if (channel != null && channel.isActive()) {
				byte[] bytes = ByteObjConverter.objectToByte(new MsgObj(msgType, msgContent));
				channel.write(new AhnoProtocol(bytes.length, bytes));
			}
		});
	}
}