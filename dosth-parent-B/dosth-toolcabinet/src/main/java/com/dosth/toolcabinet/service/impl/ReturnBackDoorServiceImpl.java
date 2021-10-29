package com.dosth.toolcabinet.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.RecoveryAction;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.recovery.Recovery;
import com.dosth.comm.plc.PLCUtil;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.enums.PlcOpType;
import com.dosth.toolcabinet.service.ReturnBackDoorService;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * @description 回收口操作接口实现
 * @author guozhidong
 *
 */
@Service
public class ReturnBackDoorServiceImpl implements ReturnBackDoorService {

	@Autowired
	private CabinetConfig cabinetConfig;

	@Override
	public void op(String cabinetId, PlcOpType type) throws BscException {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(), SetupKey.Cabinet.MAIN_CABINET_ID);
		String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Cabinet.CABINET_TYPE);
		RecoveryAction action = RecoveryAction.RESET;
		if (type.equals(PlcOpType.RETURN_BACK_DOOR_REST)) {
			action = RecoveryAction.RESET;
		}
		switch (CabinetType.valueOf(cabinetType)) {
		case KNIFE_CABINET_PLC:
			try {
				PLCUtil.opPlc(type);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case KNIFE_CABINET_DETA:
		case KNIFE_CABINET_DETB:
			Byte boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DET_BOARD_NO)); // A或B获取栈号
			StorageMedium returnBack = new Recovery(boardNo, action) {
				@Override
				public void receiveMessage(Message message) {
					WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
				}
			};
			StorageMediumPicker.putStorageMedium(returnBack);
			break;
		default:
			boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.CCabinet.DET_BOARD_NO_A)); // C获取A栈号
			returnBack = new Recovery(boardNo, action) {
				@Override
				public void receiveMessage(Message message) {
					WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
				}
			};
			StorageMediumPicker.putStorageMedium(returnBack);
			break;
		}
	}
}