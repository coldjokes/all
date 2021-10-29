package com.dosth.toolcabinet.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.common.CabinetConstant;
import com.cnbaosi.common.constant.EnumDoor;
import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.modbus.monitor.DoorOp;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.door.DoorCheck;
import com.cnbaosi.workspace.door.DoorOpen;
import com.cnbaosi.workspace.spring.Door;
import com.dosth.dto.ExtraCabinet;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.instruction.board.util.DetACloseDoor;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.enums.PlcOpType;
import com.dosth.toolcabinet.service.DoorService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.toolcabinet.util.DetDoorUtil;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * @description 门Service实现
 * @author guozhidong
 *
 */
@Service
public class DoorServiceImpl implements DoorService {

	private static final Logger logger = LoggerFactory.getLogger(DoorServiceImpl.class);

	@Autowired
	private CabinetConfig cabinetConfig;
	@Autowired
	private ToolService toolService;

	@Override
	public void checkDoorStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(cabinetConfig.getSerialNo(), SetupKey.Cabinet.MAIN_CABINET_ID);
				String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Cabinet.CABINET_TYPE);
				if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) {
					op(mainCabinetId, PlcOpType.ON_CLOSE_DOOR_COIL);
				} else if (cabinetType.equals(CabinetType.KNIFE_CABINET_DETA.name())) {
					DetACloseDoor closeDoor = new DetACloseDoor() {
						@Override
						public void receiveMessage(String message) {
							if (message != null && message.equals(WsMsgType.DOOR_ERR.getName())) {
								WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.ERR_TIP, "门未归位"));
							}
						}
					};
					try {
						closeDoor.start(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Det.DET_COM),
								Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Det.DET_BAUD)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					Byte mainBoardNo;
					cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Cabinet.CABINET_TYPE);
					// C型柜获取A的栈号
					if (cabinetType.equals(CabinetType.KNIFE_CABINET_C.name())) {
						mainBoardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.CCabinet.DET_BOARD_NO_A));
					} else { // 获取主柜栈号
						mainBoardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Det.DET_BOARD_NO));
					}
					Map<Byte, Door> doorMap = getDoorMap(DosthToolcabinetRunnerInit.mainCabinetId);
					StorageMedium doorCheck = new DoorCheck(mainBoardNo, doorMap) {
						@Override
						public void receiveMessage(Message message) {
							switch (message.getType()) {
							case OPEN_SUCC:
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
								break;
							case OPENED:
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
								break;
							case CLOSED_SUCC:
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
								break;
							case CLOSED:
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOORCLOSED, message.getCustomMsg()));
								break;
							default:
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOR_ERR, message.getCustomMsg()));
								break;
							}
						}
					};
					StorageMediumPicker.putStorageMedium(doorCheck);
				}
			}
		}).start();
	}

	@Override
	public void op(String cabinetId, PlcOpType type) throws BscException {
		if (CabinetConstant.hopperRunningFlag && type.equals(PlcOpType.ON_OPEN_DOOR_COIL)) {
			throw new BscException("料斗运动中,不能作开门动作!");
		}
		String cabinet_type = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE);
		// 如果是副柜,则取上级柜体类型
		if (cabinet_type.equals(CabinetType.SUB_CABINET.name())) {
			cabinet_type = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID),
					SetupKey.Cabinet.CABINET_TYPE);
		}
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID);
		
		CabinetType cabinetType = CabinetType.valueOf(cabinet_type);
		
		Map<Byte, Door> doorMap = null;
		
		switch (cabinetType) {
		case KNIFE_CABINET_DETA: // A柜行列式的场合
			DetDoorUtil detA = new DetDoorUtil(type.equals(PlcOpType.ON_OPEN_DOOR_COIL)) {
				@Override
				public void receiveMessage(Message message) {
					WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
				}
			};
			try {
				detA.startListener(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DET_COM),
						Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DET_BAUD)),
						new int[] { 6 });
				detA.start(EnumDoor.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DOOR)));
			} catch (Exception e) {
				throw new BscException("发送开门指令失败");
			}
			break;
		case KNIFE_CABINET_PLC: // 使用PLC的场合
			DoorOp plc = new DoorOp(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP),
					Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_PORT)),
					type.equals(PlcOpType.ON_OPEN_DOOR_COIL)) {
				@Override
				public void receiveMessage(Message message) {
					WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.DOORCLOSED, message.getCustomMsg()));
				}
			};
			plc.start(cabinetType.equals(CabinetType.KNIFE_CABINET_C) ? EnumDoor.ALL
					: EnumDoor.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DOOR)));
			break;
		case KNIFE_CABINET_C_A:
			Byte boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.CCabinet.DET_BOARD_NO_A));
			doorMap = new HashMap<>();
			doorMap.put(boardNo, new Door(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_NAME), 
					DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_NAME), 
					Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID), SetupKey.Det.DET_DOOR_HEIGHT))));
			StorageMedium doorOp = new com.cnbaosi.workspace.door.DoorOp(boardNo, doorMap, type.equals(PlcOpType.ON_OPEN_DOOR_COIL)) {
				@Override
				public void receiveMessage(Message message) {
					switch (message.getType()) {
					case OPEN_SUCC:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case OPENED:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
						break;
					case CLOSED_SUCC:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case CLOSED:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOORCLOSED, message.getCustomMsg()));
						break;
					default:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOR_ERR, message.getCustomMsg()));
						break;
					}
				}
			};
			StorageMediumPicker.putStorageMedium(doorOp);
			break;
		case KNIFE_CABINET_C_B:
			boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.CCabinet.DET_BOARD_NO_B));
			doorMap = new HashMap<>();
			doorMap.put(boardNo, new Door(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_NAME), 
					DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_NAME), 
					Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID), SetupKey.Det.DET_DOOR_HEIGHT))));
			doorOp = new com.cnbaosi.workspace.door.DoorOp(boardNo, doorMap, type.equals(PlcOpType.ON_OPEN_DOOR_COIL)) {
				@Override
				public void receiveMessage(com.cnbaosi.dto.Message message) {
					switch (message.getType()) {
					case OPEN_SUCC:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case OPENED:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
						break;
					case CLOSED_SUCC:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case CLOSED:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOORCLOSED, message.getCustomMsg()));
						break;
					default:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOR_ERR, message.getCustomMsg()));
						break;
					}
				}
			};
			StorageMediumPicker.putStorageMedium(doorOp);
			break;
		case KNIFE_CABINET_C:
			boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.CCabinet.DET_BOARD_NO_A));
			doorOp = new com.cnbaosi.workspace.door.DoorOp(boardNo, getDoorMap(cabinetId), type.equals(PlcOpType.ON_OPEN_DOOR_COIL)) {
				@Override
				public void receiveMessage(com.cnbaosi.dto.Message message) {
					switch (message.getType()) {
					case OPEN_SUCC:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case OPENED:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
						break;
					case CLOSED_SUCC:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case CLOSED:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOORCLOSED, message.getCustomMsg()));
						break;
					default:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOR_ERR, message.getCustomMsg()));
						break;
					}
				}
			};
			StorageMediumPicker.putStorageMedium(doorOp);
			break;
		case KNIFE_CABINET_DETB:
			boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DET_BOARD_NO));
			doorMap = getDoorMap(cabinetId);
			int doorHeight = 0;
			for (Entry<Byte, Door> entry : doorMap.entrySet()) {
				if (entry.getKey().byteValue() == boardNo.byteValue()) {
					doorHeight = entry.getValue().getDoorHeight();
					continue;
				}
				doorMap.remove(entry.getKey());
			}
			
			if (type.equals(PlcOpType.ON_OPEN_DOOR_COIL)) {
				doorOp = new DoorOpen(boardNo, doorMap, doorHeight) {
					@Override
					public void receiveMessage(Message message) throws BscException {
						switch (message.getType()) {
							case UNRECEIVED:
								break;
							case OPEN_SUCC:
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
								break;
							case OPENED:
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
								break;
							default:
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
								break;
							}
					}
				};
			} else {
				doorOp = new com.cnbaosi.workspace.door.DoorOp(boardNo, doorMap, type.equals(PlcOpType.ON_OPEN_DOOR_COIL)) {
					@Override
					public void receiveMessage(Message message) {
						switch (message.getType()) {
						case UNRECEIVED:
							break;
						case CLOSED_SUCC:
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
							break;
						case CLOSED:
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOORCLOSED, message.getCustomMsg()));
							break;
						default:
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.TEXT, message.getCustomMsg()));
							break;
						}
					}
				};
			}
			StorageMediumPicker.putStorageMedium(doorOp);
			break;
		default:
			batchDoorOp(false);
			break;
		}
	}
	
	@Override
	public Map<Byte, Door> getDoorMap(String cabinetId) {
		Map<Byte, Door> doorMap = new HashMap<>();
		FutureTask<List<ExtraCabinet>> future = new FutureTask<>(new Callable<List<ExtraCabinet>>() {
			@Override
			public List<ExtraCabinet> call() throws Exception {
				return toolService.getCabinetTreeList(cabinetId);
			}
		});
		new Thread(future).start();
		try {
			List<ExtraCabinet> cabinetList = future.get();
			cabinetList = cabinetList.stream()
					.filter(c -> c.getCabinetType().equals(CabinetType.KNIFE_CABINET_DETB.name())
							|| c.getCabinetType().equals(CabinetType.SUB_CABINET.name())
							|| c.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_A.name())
							|| c.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_B.name()))
					.collect(Collectors.toList());
			for (ExtraCabinet extra : cabinetList) {
				doorMap.put(extra.getBoardNo().byteValue(), new Door(extra.getCabinetName(), extra.getCabinetName(), extra.getDoorHeight()));
			}
		} catch (Exception e) {
			logger.error("抓取门信息错误:" + e.getMessage());
			e.printStackTrace();
		}
		return doorMap;
	}

	@Override
	public void batchDoorOp(Boolean opFlag) throws BscException {
		if (CabinetConstant.hopperRunningFlag && opFlag) {
			throw new BscException("料斗运动中,不能作开门动作!");
		}
		String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Cabinet.CABINET_TYPE);
		if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC类型
			op(DosthToolcabinetRunnerInit.mainCabinetId, opFlag != null && opFlag ? PlcOpType.ON_OPEN_DOOR_COIL : PlcOpType.ON_CLOSE_DOOR_COIL);
		} else {
			Byte mainBoardNo;
			Map<Byte, Door> doorMap = this.getDoorMap(DosthToolcabinetRunnerInit.mainCabinetId);
			// C型柜获取A的栈号
			if (cabinetType.equals(CabinetType.KNIFE_CABINET_C.name())) { // C柜控制所有的门
				mainBoardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.CCabinet.DET_BOARD_NO_A));
			} else { // 获取主柜栈号,其他类型只控制当前柜子
				mainBoardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Det.DET_BOARD_NO));
			}
			StorageMedium doorOp = new com.cnbaosi.workspace.door.DoorOp(mainBoardNo, doorMap, false) {
				@Override
				public void receiveMessage(com.cnbaosi.dto.Message message) {
					switch (message.getType()) {
					case OPEN_SUCC:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case OPENED:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
						break;
					case CLOSED_SUCC:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case CLOSED:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOORCLOSED, message.getCustomMsg()));
						break;
					default:
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOR_ERR, message.getCustomMsg()));
						break;
					}
				}
			};
			StorageMediumPicker.putStorageMedium(doorOp);
		}
	}
}