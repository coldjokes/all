package com.dosth.toolcabinet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dosth.comm.UICompoment;
import com.dosth.comm.audio.MP3Player;
import com.dosth.comm.plc.PLCUtil;
import com.dosth.comm.softhand.SoftHandComm;
import com.dosth.comm.softhand.SoftHandCommMsg;
import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.commu.ConcreteMediator;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.dto.BillInfo;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.enums.TrueOrFalse;
import com.dosth.toolcabinet.service.BorrowMatService;
import com.dosth.toolcabinet.service.StorageMediumService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.toolcabinet.util.DetABorrow;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * 取料Service实现
 * 
 * @author guozhidong
 *
 */
@Service
public class BorrowMatServiceImpl implements BorrowMatService {
	private static final Logger logger = LoggerFactory.getLogger(BorrowMatServiceImpl.class);

	@Autowired
	private ToolService toolService;
	@Autowired
	private CabinetConfig cabinetConfig;
	@Autowired
	private UICompoment uiCom;
	@Autowired
	private ConcreteMediator mediator;
	@Autowired
	private StorageMediumService storageMediumService;
	@Autowired
	private DetABorrow detABorrow;

	@Override
	public void borrowMat(String matBillId, String accountId) throws Exception {
		List<CartInfo> cartList = new ArrayList<>();
		int borrowNum = 1;

		BillInfo billInfo = toolService.getBillInfo(matBillId);
		// 计算取出数量（按盒领取：1；按支领取：包装数量；）
		if (billInfo.getMatDetail().getBorrowTypeCode().equals("METER")) {
			borrowNum = billInfo.getMatDetail().getPackNum();
		}
		cartList.add(new CartInfo(borrowNum, billInfo.getMatDetail().getBorrowTypeCode(), billInfo.getReceiveType(),
				billInfo.getReceiveInfo(), billInfo.getMatDetail().getMatId()));
		FutureTask<List<ExtraCabinet>> task = new FutureTask<>(new Callable<List<ExtraCabinet>>() {
			@Override
			public List<ExtraCabinet> call() throws Exception {
				// 暂存柜共享开关
				String shareSwitch = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
						SetupKey.TemCabinet.TEM_SHARE_SWITCH);
				if (StringUtils.isBlank(shareSwitch)) {
					shareSwitch = TrueOrFalse.FALSE.toString();
				}
				return toolService.sendCartToServer(DosthToolcabinetRunnerInit.mainCabinetId, cartList, shareSwitch,
						accountId);
			}
		});
		new Thread(task).start();
		List<ExtraCabinet> cabinetList = task.get();
		List<ExtraCabinet> batchCabinetList = new ArrayList<>();

		// A型柜、PLC独立处理
		for (ExtraCabinet cabinet : cabinetList) {
			if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_DETA.name())) { // A型柜
				this.detABorrow.callMotor(cabinet.getCardList());
			} else if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC
				this.callMotor(cabinet.getCardList());
			} else { // 行列式B型、储物柜、虚拟仓
				batchCabinetList.add(cabinet);
			}
		}
		if (!batchCabinetList.isEmpty()) {
			this.storageMediumService.collarUse(batchCabinetList);
		}
	}

	/**
	 * @description 通讯马达板
	 * @param cardList 封装马达板数据 synchronized
	 */
	private synchronized void callMotor(List<Card> cardList) {
		try {
			PLCUtil.setModbusConfig(
					DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
							SetupKey.Plc.PLC_IP),
					Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
							SetupKey.Plc.PLC_PORT)),
					1);
			logger.info("到达目标位初始化");
			PLCUtil.clearArriveFlag();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PLCUtil.close();
		}
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, "启动取料...."));
		if (mediator.isCurrShoppingCartTakenOut()) {
			mediator.fillShiftCmdStack(cardList);
			uiCom.send(new SoftHandCommMsg(SoftHandComm.SHIFT, mediator.getNextShift()));
			MP3Player.play("AUDIO_D1.mp3");
		} else {
			MP3Player.play("AUDIO_D2.mp3");
		}
	}
}