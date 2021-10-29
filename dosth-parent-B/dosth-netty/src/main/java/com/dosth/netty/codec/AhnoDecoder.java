package com.dosth.netty.codec;

import java.util.List;

import com.dosth.netty.constant.ConstantValue;
import com.dosth.netty.dto.AhnoProtocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @description 解码器
 * 
 * <pre> 
 * 自己定义的协议 
 *  数据包格式 
 * +——----——+——-----——+——----——+ 
 * |协议开始标志|  长度             |   数据       | 
 * +——----——+——-----——+——----——+ 
 * 1.协议开始标志head_data，为int类型的数据，16进制表示为0X76 
 * 2.传输数据的长度contentLength，int类型 
 * 3.要传输的数据,长度不应该超过2048，防止socket流的攻击 
 * </pre>
 * 
 * @author guozhidong
 *
 */
public class AhnoDecoder extends ByteToMessageDecoder {
	/** 
     * <pre> 
     * 协议开始的标准head_data，int类型，占据4个字节. 
     * 表示数据的长度contentLength，int类型，占据4个字节. 
     * </pre> 
     */  
	private final int BASE_LENGTH = 4 + 4;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out)
			throws Exception {
		// 可读长度必须大于基本长度
		if (byteBuf.readableBytes() >= BASE_LENGTH) {
			// 防止socket字节流攻击
			// 防止客户端传来的数据过大
			if (byteBuf.readableBytes() > 2048) {
				byteBuf.skipBytes(byteBuf.readableBytes());
			}
			// 记住包头开始的index
			int beginReader;
			while (true) {
				// 获取包头开始的index
				beginReader = byteBuf.readerIndex();
				// 标记包头开始的index
				byteBuf.markReaderIndex();
				// 读到协议开始的标记,结束while循环
				if (byteBuf.readInt() == ConstantValue.HEAD_DATA) {
					break;
				}
				// 未读到包头,略过一个字节
				// 每次略过,一个字节,去读取,包头信息的开始标记
				byteBuf.resetReaderIndex();
				byteBuf.readByte();
				// 当略过,一个字节之后,数据包的长度,又变得不满足,此时,应该结束.等待后面的数据到达
				if (byteBuf.readableBytes() < BASE_LENGTH) {
					return;
				}
			}
			// 消息的长度
			int length = byteBuf.readInt();
			// 判断请求数据包数据是否到齐
			if (byteBuf.readableBytes() < length) {
				// 还原读指针
				byteBuf.readerIndex(beginReader);
				return;
			}
			// 读取data数据
			byte[] data = new byte[length];
			byteBuf.readBytes(data);
			AhnoProtocol protocol = new AhnoProtocol(data.length, data);
			out.add(protocol);
		}
	}
}