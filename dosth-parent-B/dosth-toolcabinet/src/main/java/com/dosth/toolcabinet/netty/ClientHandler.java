package com.dosth.toolcabinet.netty;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dosth.netty.dto.AhnoProtocol;
import com.dosth.netty.dto.MsgObj;
import com.dosth.netty.remote.CustomHeartbeatHandler;
import com.dosth.netty.util.ByteObjConverter;
import com.dosth.netty.util.GlobalUserUtil;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;

/**
 * 
 * @description 客户端Handler
 * @author guozhidong
 *
 */
@Sharable
@Component
public class ClientHandler extends CustomHeartbeatHandler {

	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
	
	public static ClientHandler handle;

//	@Autowired
//	private AppointmentScanCompoment appScanCom;

//	@Autowired
//	private ConcreteMediator concreteMediator;
	
//	@Autowired
//	private CabinetConfig cabinetConfig;
	
//	@Autowired
//	private ToolService toolService;
	
	
	public ClientHandler() {}
	public ClientHandler(String name) {
		super(name);
	}

	@PostConstruct
	public void init() {
		handle = this;
//		handle.appScanCom.setCabinetID(this.cabinetConfig.getCabinetId());
//		handle.appScanCom.setToolService(this.toolService);
//		handle.appScanCom = this.appScanCom;
//		handle.concreteMediator = this.concreteMediator;
	}
	
	@Override
	protected void handleData(ChannelHandlerContext ctx, Object msg) {
		GlobalUserUtil.channels.forEach(channel -> {
			if (channel == ctx.channel()) {
				Object obj = ByteObjConverter.byteToObject(((AhnoProtocol) msg).getContent());
				if (obj instanceof MsgObj) {
					MsgObj msgObj = (MsgObj) obj;
					logger.info("服务端输出>>" + msgObj.getMsgType().getName() + "=" + msgObj.getContent());
					switch (msgObj.getMsgType()) {
					case TXT: // 文本信息直接推送
						WebSocketUtil.sendMsgSingle(super.name, new WsMsg(WsMsgType.TEXT, msgObj.getContent()));
						break;
					case LATTICEVALUE: // 服务端数量推送
						WebSocketUtil.sendMsgSingle(super.name, new WsMsg(WsMsgType.LATTICEVALUE, msgObj.getContent()));
						break;
					case SUBALERTINFO: // 暂存柜弹出信息
						WebSocketUtil.sendMsgSingle(super.name, new WsMsg(WsMsgType.SUBALERTINFO, msgObj.getContent()));
						break;
					default:
						break;
					}
				}
			}
		});
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);// 使用过程中断线重连
		final EventLoop eventLoop = ctx.channel().eventLoop();
		eventLoop.schedule(new Runnable() {
			@Override
			public void run() {
				logger.info("客户端{}在重连...", name);
				DosthToolcabinetRunnerInit.nettyClient.connect();
			}
		}, 1L, TimeUnit.SECONDS);
	}
}