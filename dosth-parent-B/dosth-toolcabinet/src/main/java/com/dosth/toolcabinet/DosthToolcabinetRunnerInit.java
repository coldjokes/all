package com.dosth.toolcabinet;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cnbaosi.common.CabinetConstant;
import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.TipLevel;
import com.cnbaosi.modbus.dto.MonitorData;
import com.cnbaosi.modbus.enums.AddrType;
import com.cnbaosi.modbus.monitor.ProblemMonitor;
import com.cnbaosi.printer.DpPrinter;
import com.cnbaosi.util.MessageConsume;
import com.dosth.comm.MotorCompoment;
import com.dosth.comm.PLCCompoment;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.instruction.board.enums.PrintCodeType;
import com.dosth.netty.client.NettyClient;
import com.dosth.netty.constant.NettyConfig;
import com.dosth.netty.remote.client.Client;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.netty.ClientHandler;
import com.dosth.toolcabinet.service.ScanNfcIcService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.toolcabinet.util.BorrowNoticeUtil;
import com.dosth.toolcabinet.util.EquProblemUtil;
import com.dosth.toolcabinet.util.LockBoardCabinetUtil;
import com.dosth.toolcabinet.util.ReturnBackUtil;
import com.dosth.toolcabinet.util.TrolDrawerNoticeUtil;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * @description toolCabinet初始化
 * @author chenlei
 */
@Component
@Order(2) //如果多个自定义ApplicationRunner，用来标明执行顺序
public class DosthToolcabinetRunnerInit implements ApplicationRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(DosthToolcabinetApplication.class);
	
	public static Map<String, Map<String, String>> cabinetSetupMap = new HashMap<>();
	
	public static int TIME = 5 * 1000; //等待5s
	
	public static String mainCabinetId;

	public static Client nettyClient;
	
	@Autowired
	private CabinetConfig cabinetConfig;
	@Autowired
	private ReturnBackUtil returnBackUtil;
//	@Autowired
//	private BorrowAgainUtil borrowAgainUtil;
//	@Autowired
//	private AppBorrowUtil appBorrowUtil;
	@Autowired
	private EquProblemUtil equProblemUtil;
	@Autowired
	private ScanNfcIcService scanNfcIcService;
	@Autowired
	private BorrowNoticeUtil borrowNoticeUtil;
	@Autowired
	private ToolService toolService;
	@Autowired
	private LockBoardCabinetUtil lockBoardCabinetUtil;
	@Autowired
	private TrolDrawerNoticeUtil trolDrawerNoticeUtil;
	
	@Autowired
	private PLCCompoment plcCompoment;
	@Autowired
	private MotorCompoment motorCompoment;
	
	@Override	
	public void run(ApplicationArguments args) throws Exception {
		Init();
	}
	
	public void Init() {
		while(true) {
			try {
				Thread.sleep(TIME);
				cabinetSetupMap = this.toolService.getCabinetSetupBySerialNo(this.cabinetConfig.getSerialNo());
				if(cabinetSetupMap != null) {
					break;
				}
				logger.info(">>>>结果为null，等待5s再次请求远程接口...");
			} catch (Exception e) {
				logger.info(">>>>异常，等待5s再次请求远程接口...");
				logger.error(">>>>cabinet启动时柜体参数查询异常："+ e.getMessage());
//				e.printStackTrace();
			}
		}
		mainCabinetId = getCabinetParam(this.cabinetConfig.getSerialNo(), SetupKey.Cabinet.MAIN_CABINET_ID);
		
		// 需要开启一个新的线程来执行netty server 服务器
		new Thread(new Runnable() {
			public void run() {
				logger.info("Netty客户端启动");
				nettyClient = new NettyClient(serverNettyConfig(), new ClientHandler(mainCabinetId));
				nettyClient.connect();
			}
		}).start();
		
		// 启动打印机线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				String printComm = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.PRINT_COM);
				if (printComm != null && !"".equals(printComm)) { // 打印机
					logger.info("打印机串口启动");
					DpPrinter printer = new DpPrinter() {
						@Override
						public void receiveMessage(Message message) {
							switch (message.getType()) {
							case PRINT_NO_PAPER:
								WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.PRINT_NO_PAPER, message.getCustomMsg()));
								break;
							case PRINT_FIAL:
								WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.PRINT_FIAL, message.getCustomMsg()));
								break;
							case PRINT_SUCC:
								WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.PRINT_SUCC, message.getCustomMsg()));
								break;
							default:
								WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
								break;
							}
						}
					};
					// 默认打印码格式(二维码)
					String printTypeCode = PrintCodeType.QR.name();
					if (getCabinetParam(mainCabinetId, SetupKey.Public.PRINT_TYPE_CODE) != null && !"".equals(getCabinetParam(mainCabinetId, SetupKey.Public.PRINT_TYPE_CODE))) {
						printTypeCode = getCabinetParam(mainCabinetId, SetupKey.Public.PRINT_TYPE_CODE);
					}
					printer.setIsQrCodePrint(PrintCodeType.QR.name().equals(printTypeCode));
					printer.setIsAutoCut(Boolean.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Public.PRINT_CUT)));
					try {
						printer.startListener(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Public.PRINT_COM), 
								Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Public.PRINT_BAUD)));
					} catch (Exception e) {
						e.printStackTrace();
					}
					printer.start();
				}
			}
		}).start();

		this.returnBackUtil.start();
//		this.borrowAgainUtil.start();
//		this.appBorrowUtil.start();
		this.equProblemUtil.start();
		this.scanNfcIcService.startScanNfcIc();
		this.borrowNoticeUtil.start();
		this.lockBoardCabinetUtil.start();
		this.trolDrawerNoticeUtil.start();

		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

		String cabinetType = getCabinetParam(mainCabinetId, SetupKey.Cabinet.CABINET_TYPE);
		if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) {
			// 设置PLC参数
			logger.info("设置PLC参数");
			this.plcCompoment.setCabinetId(mainCabinetId);
			this.plcCompoment.setIp(getCabinetParam(mainCabinetId, SetupKey.Plc.PLC_IP));
			// 设置马达参数
			logger.info("设置马达参数");
			this.motorCompoment.setCabinetId(mainCabinetId);
			
			// 故障监测线程池
			service.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					logger.info("故障监测线程执行");
					try {
						ProblemMonitor monitor = new ProblemMonitor() {
							@Override
							public void receiveMonitorData(MonitorData data) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								WsMsg msg = null;
								if (data.getLevel().equals(TipLevel.ERR)) {
									msg = new WsMsg(WsMsgType.ERR_TIP, data.toString());
								} else if (data.getLevel().equals(TipLevel.WARN)) {
									msg = new WsMsg(WsMsgType.WARN_TIP, data.toString());
								}
								WebSocketUtil.sendMsgSingle(mainCabinetId, msg);
							}
						};
						monitor.addAddrType(AddrType.TopBoundAlarm);
						monitor.addAddrType(AddrType.BottomBoundAlarm);
						monitor.addAddrType(AddrType.ServoAlarm);
						if (cabinetType.equals(CabinetType.KNIFE_CABINET_C.name())) { // C型柜
							monitor.addAddrType(AddrType.LeftDoorOpenFail);
							monitor.addAddrType(AddrType.LeftDoorCloseFail);
							monitor.addAddrType(AddrType.RightDoorOpenFail);
							monitor.addAddrType(AddrType.RightDoorCloseFail);
						} else { // 其他类型柜
							monitor.addAddrType(AddrType.DoorCheck);
						}
						monitor.startMonitor(getCabinetParam(mainCabinetId, SetupKey.Plc.PLC_IP), Integer.valueOf(getCabinetParam(mainCabinetId, SetupKey.Plc.PLC_PORT)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 1, 1 * 10, TimeUnit.SECONDS);
			
			// 获取储物柜协议
			String comm = getCabinetParam(mainCabinetId, SetupKey.Det.DET_COM);
			if (comm != null && !"".equals(comm)) {
				initLockBoard(comm, Integer.valueOf(getCabinetParam(mainCabinetId, SetupKey.Det.DET_BAUD)));
			}
		} else {
			initLockBoard(getCabinetParam(mainCabinetId, SetupKey.Det.DET_COM), Integer.valueOf(getCabinetParam(mainCabinetId, SetupKey.Det.DET_BAUD)));
		}
	}
	
	/**
	 * @description 初始化锁控板
	 * @param comm 串口
	 * @param baudrate 波特率
	 */
	private void initLockBoard(String comm, int baudrate) {
		logger.info("串口:" + comm + ",波特率:" + baudrate);
		// 启动串口监听
		new Thread(new Runnable() {
			@Override
			public void run() {
				MessageConsume consume = new MessageConsume() {
					@Override
					public void receiveMessage(Message message) {
						switch (message.getType()) {
						case TIME_OUT:
							CabinetConstant.connectFlag = false;
							WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TIME_OUT,  message.getType().getDesc()));
							break;
						case BUSY:
						case ERR_CODE:
							WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.WARN_TIP,  message.getType().getDesc()));
							break;
						default:
							break;
						}
					}
				};
				try {
					consume.startListener(comm, baudrate);
				} catch (Exception e) {
					e.printStackTrace();
				}
				consume.start();
			}
		}).start();
		// 启动取料队列线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				new StorageMediumPicker().start();
			}
		}).start();
	}
	
	/**
	 * @description 获取柜子参数值
	 * @param cabinetId 柜子Id
	 * @param paramAttr 参数属性
	 * @return
	 */
	public static String getCabinetParam(String cabinetId, String paramAttr) {
		if (cabinetId == null || "".equals(cabinetId)) {
			return null;
		}
		return cabinetSetupMap.get(cabinetId).get(paramAttr);
	}
	
	@Bean
	public NettyConfig serverNettyConfig() {
		return new NettyConfig(this.cabinetConfig.getNettyServerHost(), this.cabinetConfig.getNettyServerPort());
	}
	
	@PreDestroy
	public void destroy() {
		if (nettyClient != null) {
			nettyClient.close();
		}
	}
}