package com.dosth.toolcabinet.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.ConsumingDetail;
import com.cnbaosi.dto.IndexPair;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.box.BoxMedium;
import com.cnbaosi.workspace.spring.Door;
import com.cnbaosi.workspace.spring.Lattice;
import com.cnbaosi.workspace.spring.Level;
import com.cnbaosi.workspace.spring.RecordStatus;
import com.cnbaosi.workspace.spring.SpringCabinet;
import com.cnbaosi.workspace.spring.TrolDrawerCabinet;
import com.dosth.dto.BorrowNotice;
import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.dto.TrolDrawerNotice;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.service.DoorService;
import com.dosth.toolcabinet.service.StorageMediumService;
import com.dosth.toolcabinet.util.BorrowNoticeUtil;
import com.dosth.toolcabinet.util.EquProblemUtil;
import com.dosth.toolcabinet.util.TrolDrawerNoticeUtil;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * @description 存储介质接口实现
 * @author guozhidong
 *
 */
@Service
public class StorageMediumServiceImpl implements StorageMediumService {
	
	private static final Logger logger = LoggerFactory.getLogger(StorageMediumServiceImpl.class);
	@Autowired
	private BorrowNoticeUtil borrowNoticeUtil;
	@Autowired
	private TrolDrawerNoticeUtil trolDrawerNoticeUtil;
	@Autowired
	private DoorService doorService;
	// 领取记录Id
	private String recordId = null;
	// 货道Id
	private String staId = null;

	@Override
	public void collarUse(List<ExtraCabinet> batchCabinetList) {
		// Byte groupBoardNo, Byte boardNo, List<IndexPair> indexPairList
		String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Cabinet.CABINET_TYPE);
		String groupBoardNoStr = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Det.DET_BOARD_NO);
		if (cabinetType.equals(CabinetType.KNIFE_CABINET_C.name())) {
			groupBoardNoStr = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.CCabinet.DET_BOARD_NO_A);
		}
		Byte groupBoardNo = Byte.valueOf(groupBoardNoStr);
		Byte mainBoardNo = null;
		List<Card> cardList;
		// 封装栈号,取料格口分组
		Map<Byte, List<com.dosth.dto.Lattice>> latticeMap;
		// 取料个口Id,取料封装
		Map<String, Lattice> statIdlatticeMap;
		// 取料队列
		BlockingQueue<Level> levelQueue;
		// 栈号,statId,取料封装对象
		Map<Byte, Map<String, Lattice>> latticeGroupMap;
		// 行号，栈号，取料列表
		Map<Card, Map<Byte, List<com.dosth.dto.Lattice>>> springCMap = new HashMap<>();
		
		Lattice lattice = null;
		
		StorageMedium storageMedium;
		for (ExtraCabinet cabinet : batchCabinetList) {
			switch (CabinetType.valueOf(cabinet.getCabinetType())) {
			case STORE_CABINET: // 储物柜
				Map<Byte, List<IndexPair>> boxMap = new HashMap<>();
				List<IndexPair> indexPairList;
				for (com.dosth.dto.Card card : cabinet.getCardList()) {
					for (com.dosth.dto.Lattice box : card.getLatticeList()) {
						indexPairList = boxMap.get(box.getBoardNo().byteValue());
						if (indexPairList == null) {
							indexPairList = new ArrayList<>();
						}
						indexPairList.add(new IndexPair(box.getLockIndex(), box.getBoxIndex()));
						boxMap.put(box.getBoardNo().byteValue(), indexPairList);
					}
				}
				for (Entry<Byte, List<IndexPair>> entry : boxMap.entrySet()) {
					storageMedium = new BoxMedium(groupBoardNo, entry.getKey(), entry.getValue()) {
						@Override
						public void receiveMessage(Message message) {
							if (ReturnMsgType.OPEN_SUCC.equals(message.getType())) { // 单次开门成功,提示消息
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
							} else if (ReturnMsgType.OPENED.equals(message.getType())) { // 全部开门成功,点亮取料完成
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
							} 
						}
					};
					StorageMediumPicker.putStorageMedium(storageMedium);
				}
				break;
			case SUB_CABINET: // 副柜
			case KNIFE_CABINET_DETB: // B型柜
				// Byte groupBoardNo, Byte mainBoardNo, Map<Byte, Door> doorMap, BlockingQueue<Level> levelQueue
				mainBoardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinet.getCabinetId(), SetupKey.Det.DET_BOARD_NO));
				cardList = cabinet.getCardList();
				levelQueue = new LinkedBlockingQueue<>();
				cardList.sort((c1, c2) -> c2.getRowNo() - c1.getRowNo());
				for (Card card : cardList) {
					// 层分组
					latticeGroupMap = new HashMap<>();
					statIdlatticeMap = new HashMap<>();
					for (com.dosth.dto.Lattice la : card.getLatticeList()) {
						lattice = statIdlatticeMap.get(la.getStaId());
						if (lattice == null) {
							lattice = new Lattice(la.getColNo(), la.getLockIndex());
						}
						for (Entry<String, Integer> keyValue : la.getRecordMap().entrySet()) {
							lattice.getStatusMap().put(keyValue.getKey(), new RecordStatus(keyValue.getValue()));
						}
						statIdlatticeMap.put(la.getStaId(), lattice);
					}
					latticeGroupMap.put(mainBoardNo, statIdlatticeMap);
					// 设置行队列
					try {
						levelQueue.put(new Level(card.getRowNo(), card.getLevelHeight(), latticeGroupMap));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				Map<Byte, Door> doorMap = this.doorService.getDoorMap(cabinet.getCabinetId());
				Map<Byte, Door> curDoor = new HashMap<>();
				for (Entry<Byte, Door> entry : doorMap.entrySet()) { // B柜或副柜只关联当前门
					if (entry.getKey().byteValue() == mainBoardNo.byteValue()) {
						curDoor.put(entry.getKey(), entry.getValue());
					}
				}
				storageMedium = new SpringCabinet(groupBoardNo, mainBoardNo, curDoor, levelQueue) {
					@Override
					public void receiveMessage(Message message) {
						switch (message.getType()) {
						case UNRECEIVED: // 未定义暂不提示
							break;
						case OPEN_SUCC: // 单次开门成功,提示消息
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
							break;
						case OPENED: // 全部开门成功,点亮取料完成
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
							break;
						case MOTORERR:
							try {
								EquProblemUtil.put(message.getHexString());
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.WARN_TIP, message.getCustomMsg()));
							break;
						case CLOSED_FAIL: // 关门失败
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOR_ERR, message.getCustomMsg()));
							break;
						case OPEN_FAIL: // 开门失败
						case SERVOR_FAIL:// 伺服故障
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.WARN_TIP, message.getCustomMsg()));
							break;
						default:
							break;
						}
					}
					
					@Override
					public void notice(String recordId, String staId, Boolean... params) {
						try {
							borrowNoticeUtil.put(new BorrowNotice(recordId, staId, params != null && params.length > 0 && params[0]));
						} catch (InterruptedException e) {
							logger.error("发送数据同步异常:recordId>" + recordId + ",staId>" + staId + ",flag>" + (params != null && params.length > 0 && params[0]));
							e.printStackTrace();
						}
					}
				};
				StorageMediumPicker.putStorageMedium(storageMedium);
				break;
			case KNIFE_CABINET_C_A:
				mainBoardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinet.getCabinetId(), SetupKey.CCabinet.DET_BOARD_NO_A));
				cardList = cabinet.getCardList();
				for (Card card : cardList) {
					latticeMap = springCMap.get(card);
					if (latticeMap == null) {
						latticeMap = new HashMap<>();
					}
					latticeMap.put(mainBoardNo, card.getLatticeList());
					springCMap.put(card, latticeMap);
				}
				break;
			case KNIFE_CABINET_C_B:
				mainBoardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinet.getCabinetId(), SetupKey.CCabinet.DET_BOARD_NO_B));
				cardList = cabinet.getCardList();
				for (Card card : cardList) {
					latticeMap = springCMap.get(card);
					if (latticeMap == null) {
						latticeMap = new HashMap<>();
					}
					latticeMap.put(mainBoardNo, card.getLatticeList());
					springCMap.put(card, latticeMap);
				}
				break;
			case TROL_DRAWER:
				cardList = cabinet.getCardList();
				List<com.dosth.dto.Lattice> latticeList;
				Byte boardNo = null; Integer drawerNo = null; Integer openNum = null;
				for (Card card : cardList) {
					latticeList = card.getLatticeList();
					for (com.dosth.dto.Lattice la : latticeList) {
						boardNo = la.getBoardNo().byteValue();
						drawerNo = la.getLockIndex();
						openNum = la.getCurReserve();
						recordId = la.getRecordId();
						staId = la.getStaId();
					}
				}
				storageMedium = new TrolDrawerCabinet(boardNo, drawerNo, openNum) {
					@Override
					public void receiveMessage(Message message) throws BscException {
						switch (message.getType()) {
						case BUSY:
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.WARN_TIP, message.getCustomMsg()));
							break;
						case NO_CLOSE:
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.TROL_DRAWER_AGAIN, message.getCustomMsg()));
							break;
						case TIME_OUT:
						case OPEN_SUCC:
							WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
							break;
						default:
							break;
						}
					}
					
					@Override
					public void notice(ConsumingDetail detail) {
						logger.info(detail.toString());
						try {
							trolDrawerNoticeUtil.put(new TrolDrawerNotice(recordId, staId, detail.getAmount(), detail.getRealNum()));
						} catch (InterruptedException e) {
							logger.error("可控抽屉的数据同步失败:" + detail.toString());
							e.printStackTrace();
						}
					}
				};
				StorageMediumPicker.putStorageMedium(storageMedium);
				break;
			default:
				break;
			}
		}
		// 弹簧C柜取料集合
		if (!springCMap.isEmpty()) {
			this.sendQueue(groupBoardNo, springCMap);
		}
	}
	
	/**
	 * @description 发送到队列
	 * @param groupBoardNo 分组栈号
	 * @param springMap 弹簧取料集合
	 */
	private void sendQueue(Byte groupBoardNo, Map<Card, Map<Byte, List<com.dosth.dto.Lattice>>> springMap) {
		Map<Card, Map<Byte, List<com.dosth.dto.Lattice>>> orderMap = new LinkedHashMap<>();
		springMap.entrySet().stream().sorted((l1, l2) -> l2.getKey().getRowNo() - l1.getKey().getRowNo()).forEachOrdered(card -> orderMap.put(card.getKey(), card.getValue()));
		BlockingQueue<Level> levelQueue = new LinkedBlockingQueue<>();
		Map<Byte, Map<String, Lattice>> latticeGroupMap;
		Map<String, Lattice> latticeMap;
		Lattice Lattice;
		for (Entry<Card, Map<Byte, List<com.dosth.dto.Lattice>>> entry : orderMap.entrySet()) {
			// 层分组
			latticeGroupMap = new HashMap<>();
			for (Entry<Byte, List<com.dosth.dto.Lattice>> latticeArr : entry.getValue().entrySet()) {
				// 一板,二板
				latticeMap = new HashMap<>();
				// 货道
				for (com.dosth.dto.Lattice lattice : latticeArr.getValue()) {
					Lattice = latticeMap.get(lattice.getStaId());
					if (Lattice == null) {
						Lattice = new Lattice(lattice.getColNo(), lattice.getLockIndex());
					}
					for (Entry<String, Integer> keyValue : lattice.getRecordMap().entrySet()) {
						Lattice.getStatusMap().put(keyValue.getKey(), new RecordStatus(keyValue.getValue()));
					}
					latticeMap.put(lattice.getStaId(), Lattice);
				}
				latticeGroupMap.put(latticeArr.getKey(), latticeMap);
			}
			// 设置行队列
			try {
				levelQueue.put(new Level(entry.getKey().getRowNo(), entry.getKey().getLevelHeight(), latticeGroupMap));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 主控栈号,C柜直接取A栈号
		Byte mainBoardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.CCabinet.DET_BOARD_NO_A));
		StorageMedium storageMedium = new SpringCabinet(groupBoardNo, mainBoardNo, this.doorService.getDoorMap(DosthToolcabinetRunnerInit.mainCabinetId), levelQueue) {
			@Override
			public void receiveMessage(com.cnbaosi.dto.Message message) {
				if (message != null && message.getType() != null) {
					logger.info(message.toString());
					switch (message.getType()) {
					case OPEN_SUCC: // 单次开门成功,提示消息
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.INFO_TIP, message.getCustomMsg()));
						break;
					case OPENED: // 全部开门成功,点亮取料完成
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOROPENED, message.getCustomMsg()));
						break;
					case MOTORERR: // 马达故障
						try {
							EquProblemUtil.put(message.getHexString());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.WARN_TIP, message.getCustomMsg()));
						break;
					case CLOSED_FAIL: // 关门失败
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.DOOR_ERR, message.getCustomMsg()));
						break;
					case OPEN_FAIL: // 开门失败
					case UNRECEIVED: // 未监测到信号
					case SERVOR_FAIL:// 伺服故障
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.WARN_TIP, message.getCustomMsg()));
						break;
					default:
						break;
					}
				} else {
					logger.error("返回消息为空!");
				}
			}
			
			@Override
			public void notice(String recordId, String staId, Boolean... params) {
				BorrowNotice notice = new BorrowNotice(recordId, staId, params != null && params.length > 0 && params[0]);
				try {
					borrowNoticeUtil.put(notice);
				} catch (InterruptedException e) {
					logger.error("设置领用通讯失败:" + notice.toString());
					e.printStackTrace();
				}
			}
		};
		StorageMediumPicker.putStorageMedium(storageMedium);
	}
}