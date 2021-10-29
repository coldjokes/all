package com.dosth.toolcabinet.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.HopperWork;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.modbus.ModbusServerConfig;
import com.cnbaosi.modbus.ModbusUtil;
import com.cnbaosi.modbus.enums.AddrType;
import com.cnbaosi.modbus.hopper.HopperPlcOp;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.hopper.HopperOp;
import com.cnbaosi.workspace.hopper.HopperReset;
import com.cnbaosi.workspace.spring.Door;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.enums.PlcOpType;
import com.dosth.toolcabinet.service.DoorService;
import com.dosth.toolcabinet.service.HopperService;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

import net.wimpi.modbus.ModbusException;

/**
 * @description 料斗Service实现
 * @author guozhidong
 *
 */
@Service
public class HopperServiceImpl implements HopperService {
	
	@Autowired
	private DoorService doorService;

	@Override
	public void op(String cabinetId, PlcOpType type) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID);
		String cabinet_type = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE);
		CabinetType cabinetType = CabinetType.valueOf(cabinet_type);
		if (cabinetType.equals(CabinetType.SUB_CABINET)) {
			cabinetType = CabinetType.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Cabinet.CABINET_TYPE));
		}
		switch (cabinetType) {
		case KNIFE_CABINET_PLC:
			String host = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP);
			int port = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_PORT) == null 
					|| "".equals(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_PORT)) 
					? 502 : Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_PORT));
			ModbusUtil.getInstance(new ModbusServerConfig(host, port));
			try {
				int result = ModbusUtil.readModbusValue(1, AddrType.TargetLvl);
				if (result != 0) {
					throw new BscException("料斗运动中");
				}
			} catch (ModbusException e) {
				e.printStackTrace();
				throw new BscException("料斗运动中");
			}
			HopperPlcOp hopperPlcOp = new HopperPlcOp() {
				@Override
				public void receiveMessage(String message) {
					WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message));
				}
			};
			switch (type) {
			case ON_UP_COIL:
				hopperPlcOp.setHopperWork(HopperWork.UP);
				hopperPlcOp.setTopLevel(Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_TOP_LEVEL)));
				break;
			case ON_DOWN_COIL:
				hopperPlcOp.setHopperWork(HopperWork.DOWN);
				break;
			case ON_RESET_LIFT_COIL:
				hopperPlcOp.setHopperWork(HopperWork.RESET);
				break;
			default:
				break;
			}
			hopperPlcOp.start(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP), Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_PORT)), new int [] {15});
			break;
		case KNIFE_CABINET_DETA:
		case KNIFE_CABINET_DETB:
			Byte boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DET_BOARD_NO)); // A或B获取柜体栈号
			Map<Byte, Door> doorMap = this.doorService.getDoorMap(cabinetId);
			StorageMedium hopper = null;
			switch (type) {
			case ON_UP_COIL:
				hopper = new HopperOp(boardNo, doorMap, Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DET_TOP_HEIGHT))) {
					@Override
					public void receiveMessage(Message message) {
						WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
					}
				};
				break;
			case ON_DOWN_COIL:
				hopper = new HopperOp(boardNo, doorMap, 0) {
					@Override
					public void receiveMessage(Message message) {
						WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
					}
				};
				break;
			case ON_RESET_LIFT_COIL:
				hopper = new HopperReset(boardNo, doorMap) {
					@Override
					public void receiveMessage(Message message) {
						WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
					}
				};
				break;
			default:
				break;
			}
			StorageMediumPicker.putStorageMedium(hopper);
			break;
		case KNIFE_CABINET_C:
			boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.CCabinet.DET_BOARD_NO_A)); // C取A栈号
			doorMap = this.doorService.getDoorMap(cabinetId);
			hopper = null;
			switch (type) {
			case ON_UP_COIL:
				hopper = new HopperOp(boardNo, doorMap, Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DET_TOP_HEIGHT))) {
					@Override
					public void receiveMessage(Message message) {
						WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
					}
				};
				break;
			case ON_DOWN_COIL:
				hopper = new HopperOp(boardNo, doorMap, 0) {
					@Override
					public void receiveMessage(Message message) {
						WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
					}
				};
				break;
			case ON_RESET_LIFT_COIL:
				hopper = new HopperReset(boardNo, doorMap) {
					@Override
					public void receiveMessage(Message message) {
						WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
					}
				};
				break;
			default:
				break;
			}
			StorageMediumPicker.putStorageMedium(hopper);
			break;
		default:
			break;
		}
	}
}