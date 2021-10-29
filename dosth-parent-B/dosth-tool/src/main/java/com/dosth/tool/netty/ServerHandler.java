package com.dosth.tool.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.netty.dto.AhnoProtocol;
import com.dosth.netty.dto.MsgObj;
import com.dosth.netty.remote.CustomHeartbeatHandler;
import com.dosth.netty.util.ByteObjConverter;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @description 服务端Handler
 * @author guozhidong
 *
 */
@Sharable
public class ServerHandler extends CustomHeartbeatHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	public ServerHandler(String name) {
		super(name);
	}

	@Override
	protected void handleData(ChannelHandlerContext ctx, Object msg) {
		logger.info("Server~~~~~~~" + msg);
		Object obj = ByteObjConverter.byteToObject(((AhnoProtocol) msg).getContent());
		if (obj instanceof MsgObj) {
			MsgObj msgObj = (MsgObj) obj;
			logger.info(msgObj.getMsgType().getName() + ">>" + msgObj.getContent());
		}
	}
}