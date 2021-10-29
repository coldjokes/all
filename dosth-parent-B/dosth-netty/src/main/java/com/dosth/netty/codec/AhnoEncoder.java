package com.dosth.netty.codec;

import com.dosth.netty.dto.AhnoProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * 
 * @author guozhidong
 *
 */
public class AhnoEncoder extends MessageToByteEncoder<AhnoProtocol> {
	@Override
	protected void encode(ChannelHandlerContext ctx, AhnoProtocol msg, ByteBuf out) throws Exception {
		// 写入消息的具体内容
		// 1.写入消息的开头的信息标志
		out.writeInt(msg.getHead_data());
		// 2.写入消息的长度
		out.writeInt(msg.getContentLength());
		// 3.写入消息的内容
		out.writeBytes(msg.getContent());
	}
}