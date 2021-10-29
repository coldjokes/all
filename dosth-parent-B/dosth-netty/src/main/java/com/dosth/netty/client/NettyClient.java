package com.dosth.netty.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.netty.codec.AhnoDecoder;
import com.dosth.netty.codec.AhnoEncoder;
import com.dosth.netty.constant.NettyConfig;
import com.dosth.netty.dto.AhnoProtocol;
import com.dosth.netty.remote.CustomHeartbeatHandler;
import com.dosth.netty.remote.client.Client;
import com.dosth.netty.remote.client.exception.ClientCloseException;
import com.dosth.netty.remote.client.exception.ClientException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyClient implements Client {

	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
	
	private NettyConfig config;

	private EventLoopGroup workerGroup;
	private Bootstrap bootstrap;
	private Channel channel;
	private CustomHeartbeatHandler clientHandler;
	
	public NettyClient(NettyConfig config, CustomHeartbeatHandler clientHandler) {
		this.config = config;
		this.clientHandler = clientHandler;
	}

	@Override
	public void connect() {
		this.workerGroup = new NioEventLoopGroup(config.getMaxWorkThreads());
		bootstrap = new Bootstrap();
		bootstrap.group(this.workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new IdleStateHandler(4, 5, 7));
						pipeline.addLast(new AhnoEncoder());
						pipeline.addLast(new AhnoDecoder());
						pipeline.addLast(clientHandler);
					}
				});
		doConnect();
	}

	/**
	 * @description 重连机制,每隔2s重新连接一次服务器
	 */
	public void doConnect() {
		if (channel != null && channel.isActive()) {
			return;
		}
		ChannelFuture future = bootstrap.connect(config.getHost(), config.getPort());
		future.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture futureListener) throws Exception {
				if (futureListener.isSuccess()) {
					channel = futureListener.channel();
					logger.info("Connect to server successfully!");
				} else {
					logger.info("Failed to connect to server, try connect after 2s");
					futureListener.channel().eventLoop().schedule(new Runnable() {
						@Override
						public void run() {
							doConnect();
						}
					}, 2, TimeUnit.SECONDS);
				}
			}
		});
	}

	@Override
	public void send(AhnoProtocol protocol) {
		if (this.channel != null && this.channel.isActive()) {
			this.channel.writeAndFlush(protocol);
		}
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		SocketAddress remoteAddress = this.channel.remoteAddress();
		if (!(remoteAddress instanceof InetSocketAddress)) {
			throw new ClientException(new RuntimeException("Get remote address error, should be InetSocketAddress"));
		}
		return (InetSocketAddress) remoteAddress;
	}

	@Override
	public void close() {
		if (this.channel == null) {
			throw new ClientCloseException();
		}
		this.workerGroup.shutdownGracefully();
		this.channel.closeFuture().syncUninterruptibly();
		this.workerGroup = null;
		this.channel = null;
	}
}