package com.dosth.netty.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.netty.codec.AhnoDecoder;
import com.dosth.netty.codec.AhnoEncoder;
import com.dosth.netty.constant.NettyConfig;
import com.dosth.netty.remote.CustomHeartbeatHandler;
import com.dosth.netty.remote.server.Server;
import com.dosth.netty.remote.server.exception.ServerException;
import com.dosth.netty.remote.server.exception.ServerStopException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServer implements Server {
	
	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

	//服务端要建立两个group,一个负责接收客户端的连接,一个负责处理数据传输  
    //连接处理group  
	private EventLoopGroup bossGroup = new NioEventLoopGroup();  
    //事件处理group  
	private EventLoopGroup workerGroup = new NioEventLoopGroup();  
	private Channel serverSocketChannel;

	private NettyConfig nettyConfig;
	private CustomHeartbeatHandler handler;
	
	public NettyServer(NettyConfig nettyConfig, CustomHeartbeatHandler handler) {
		this.nettyConfig = nettyConfig;
		this.handler = handler;
	}

	@Override
	public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();  
        // 绑定处理group  
        bootstrap.group(bossGroup, workerGroup) 
		        .channel(NioServerSocketChannel.class) // 类似NIO中serverSocketChannel  
		        .option(ChannelOption.SO_BACKLOG, 1024) // 配置TCP参数  
		        .option(ChannelOption.SO_SNDBUF, 32 * 1024) // 设置发送缓冲大小  
		        .option(ChannelOption.SO_RCVBUF, 32 * 1024) // 这是接收缓冲大小  
                .option(ChannelOption.TCP_NODELAY, true) // 有数据立即发送  
		        .option(ChannelOption.SO_KEEPALIVE, true) // 保持连接  
                //处理新连接  
                .childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 增加任务处理
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new AhnoDecoder());
						pipeline.addLast(new AhnoEncoder());
						pipeline.addLast(new IdleStateHandler(5, 7, 9)); // 心跳控制
						pipeline.addLast(handler);
					}
				});

		// 绑定端口，同步等待成功
		ChannelFuture future;
		try {
			future = bootstrap.bind(this.nettyConfig.getHost(), this.nettyConfig.getPort()).sync();
			if (future.isSuccess()) {
				serverSocketChannel = (ServerSocketChannel) future.channel();
				logger.info("服务端开启成功,host:" + this.nettyConfig.getHost() + ",port:" + this.nettyConfig.getPort());
			} else {
				logger.info("服务端开启失败,host:" + this.nettyConfig.getHost() + ",port:" + this.nettyConfig.getPort());
			}
			// 等待服务监听端口关闭,就是由于这里会将线程阻塞
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			throw new ServerException(Server.SYSTEM_MESSAGE_ID, e);
		}
	}

	@Override
	public void stop() {
		logger.info("服务端优雅关闭~~~~");
		if (serverSocketChannel == null) {
			throw new ServerStopException();
		}
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		serverSocketChannel.closeFuture().syncUninterruptibly();
		bossGroup = null;
		workerGroup = null;
		serverSocketChannel = null;
	}
}