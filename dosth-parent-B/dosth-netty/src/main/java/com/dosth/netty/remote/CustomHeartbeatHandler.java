package com.dosth.netty.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.netty.dto.AhnoProtocol;
import com.dosth.netty.dto.MsgObj;
import com.dosth.netty.dto.MsgType;
import com.dosth.netty.util.ByteObjConverter;
import com.dosth.netty.util.GlobalUserUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @description 自定义心跳Handler
 * @author guozhidong
 *
 */
public abstract class CustomHeartbeatHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(CustomHeartbeatHandler.class);

	protected String name;
	private int heartbeatCount = 0;
	public CustomHeartbeatHandler() {}
	public CustomHeartbeatHandler(String name) {
		this.name = name;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof AhnoProtocol) {
			Object obj = ByteObjConverter.byteToObject(((AhnoProtocol) msg).getContent());
			if (obj instanceof MsgObj) {
				MsgObj msgObj = (MsgObj) obj;
				MsgType msgType = msgObj.getMsgType();
				switch (msgType) {
				case PING:
					sendPongMsg(ctx);
					break;
				case PONG:
					getPongMsg(msgObj);
					break;
				default:
					handleData(ctx, msg);
					break;
				}
			} else {
				logger.info("其他类型请求");
				handleData(ctx, msg);
			}
		} else {
			logger.error("有非法数据通讯~~~");
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		super.userEventTriggered(ctx, evt);
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			switch (e.state()) {
			case READER_IDLE:
				handleReaderIdle(ctx);
				break;
			case WRITER_IDLE:
				handleWriterIdle(ctx);
				break;
			case ALL_IDLE:
				handleAllIdle(ctx);
				break;
			default:
				break;
			}
		}
	}

	private void getPongMsg(MsgObj obj) {
		logger.info("Server return Pong to say:" + obj.getContent().toString());
	}

	/**
	 * @description 发送Ping
	 * 
	 * @param ctx
	 */
	protected void sendPingMsg(ChannelHandlerContext ctx) {
		MsgObj msgObj = new MsgObj(MsgType.PING,
				name + " send Ping message:" + this.name + ", total count:" + heartbeatCount);
		byte[] bytes = ByteObjConverter.objectToByte(msgObj);
		ctx.channel().writeAndFlush(new AhnoProtocol(bytes.length, bytes));
		heartbeatCount++;
		logger.info(msgObj.getContent().toString());
	}

	/**
	 * 服务端发送pong消息
	 * 
	 * @param ctx
	 */
	private void sendPongMsg(ChannelHandlerContext ctx) {
		MsgObj msgObj = new MsgObj(MsgType.PONG, "send Pong message to " + this.name + ", total count:" + heartbeatCount);
		byte[] bytes = ByteObjConverter.objectToByte(msgObj);
		ctx.channel().writeAndFlush(new AhnoProtocol(bytes.length, bytes));
		heartbeatCount++;
		logger.info("send Pong message:{}" + msgObj.getContent(), name);
	}

	protected abstract void handleData(ChannelHandlerContext ctx, Object msg);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("---" + this.name + " 上线---");
		GlobalUserUtil.channels.add(ctx.channel());
		GlobalUserUtil.clientChannelMap.put(ctx.channel().id().asShortText(), ctx.channel());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("---" + this.name + " 掉线---");
		GlobalUserUtil.channels.remove(ctx.channel());
		GlobalUserUtil.clientChannelMap.remove(ctx.channel().id().asShortText());
		ctx.close();
	}

	protected void handleAllIdle(ChannelHandlerContext ctx) {
		logger.info("---ALL_IDLE---");
	}

	protected void handleReaderIdle(ChannelHandlerContext ctx) {
		logger.info("---READER_IDLE---{}", "long time not receive server's message");
		sendPingMsg(ctx);
	}

	protected void handleWriterIdle(ChannelHandlerContext ctx) {
		logger.info("---WRITER_IDLE---{}", "long time not send message to server");
		sendPingMsg(ctx);
	}
}