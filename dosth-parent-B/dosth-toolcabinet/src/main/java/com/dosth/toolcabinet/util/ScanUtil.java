package com.dosth.toolcabinet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.RecoveryAction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.modbus.recovery.EnumRecoveryOp;
import com.cnbaosi.modbus.recovery.RecoveryBox;
import com.cnbaosi.scanner.ScanNfcCompound;
import com.cnbaosi.scanner.enums.ScanNfcType;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.recovery.Recovery;
import com.dosth.comm.audio.MP3Player;
import com.dosth.dto.ExtraCabinet;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.dto.BillInfo;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.toolcabinet.dto.enums.TrueOrFalse;
import com.dosth.toolcabinet.service.BorrowMatService;
import com.dosth.toolcabinet.service.StorageMediumService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

/**
 * @description 回收口扫描仪工具类
 * @author guozhidong
 *
 */
@Component
public class ScanUtil {

	private static final Logger logger = LoggerFactory.getLogger(ScanUtil.class);

	@Autowired
	private ToolService toolService;
	@Autowired
	private BorrowMatService borrowMatService;
	@Autowired
	private StorageMediumService storageMediumService;

	/**
	 * @description 回收口扫描仪启动
	 */
	public void start(String cabinetId) {
		try {
			String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE);
			String scanType = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.RecCabinet.REC_SCAN_TYPE);
			ScanNfcCompound scan = new ScanNfcCompound(ScanNfcType.valueOf(scanType), false) {
				@Override
				public void timeout() {
					MP3Player.play("AUDIO_C5.mp3");
				}

				@Override
				public void receiveScanResult(String result) {
					logger.info("微光扫描仪扫描结果:" + result);
					String backNo = result.substring(1, 8);
					logger.info("回传扫描结果:" + backNo);
					// 归还编号
					List<ReturnBackPrintInfo> infoList = toolService.getReturnInfo(backNo);
					if (infoList == null || infoList.size() == 0) {
						WebSocketUtil.sendMsgSingle(
								DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID),
								new WsMsg(WsMsgType.TEXT, "此二维码无效！"));
						return;
					}
					ReturnBackPrintInfo info = infoList.get(0);
					// 判断是否已归还
					Boolean isBack = toolService.isBackCheck(backNo);
					if (isBack) {
						// 已归还条码语音提示，处理中断
						MP3Player.play("AUDIO_C11.mp3");
						WebSocketUtil.sendMsgSingle(
								DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID),
								new WsMsg(WsMsgType.TEXT, "请勿重复归还！"));
						return;
					}
					// 回收口翻转
					int returnTypeId = info.getReturnType().equals("ABNORMAL") ? 101 : 102;
					logger.info("回收口翻转类型:" + returnTypeId);
					try {
						if ("0".equals(info.getIsGetNewOne())) { // 直接归还
							logger.info("刀具柜类型:" + CabinetType.valueOf(cabinetType).getMessage());
							if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) {
								RecoveryBox box = new RecoveryBox() {
									@Override
									public void receiveMessage(Message message) {
										logger.info("回收口返回信息:" + message);
										try {
											if (ReturnMsgType.RECOVERY_SUCC.equals(message.getType())) {
												MP3Player.play("AUDIO_C2.mp3");
												ReturnBackUtil.putReturnBackInfo(result);
											} else {
												MP3Player.play("AUDIO_C5.mp3");
												WebSocketUtil.sendMsgSingle(
														DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
																SetupKey.Cabinet.MAIN_CABINET_ID),
														new WsMsg(WsMsgType.TEXT, "回收口扫描等待超时"));
											}
										} catch (Exception e1) {
											e1.printStackTrace();
										}
									}
								};
								switch (returnTypeId) {
								case 101:
									box.op(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP),
											Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
													SetupKey.Plc.PLC_PORT)),
											EnumRecoveryOp.TURNLEFT);
									break;
								default:
									box.op(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP),
											Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
													SetupKey.Plc.PLC_PORT)),
											EnumRecoveryOp.TURNRIGHT);
									break;
								}
							} else {
								Byte boardNo = null;
								if (cabinetType.equals(CabinetType.KNIFE_CABINET_DETB.name())
										|| cabinetType.equals(CabinetType.KNIFE_CABINET_DETA.name())) { // DETA或DETB
									boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
											SetupKey.Det.DET_BOARD_NO));
								} else if (cabinetType.equals(CabinetType.KNIFE_CABINET_C.name())) { // C型柜柜体类型
									boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
											SetupKey.CCabinet.DET_BOARD_NO_A));
								}
								RecoveryAction action = RecoveryAction.RIGHT;
								if (returnTypeId == 101) {
									action = RecoveryAction.LEFT;
								}
								StorageMedium recovery = new Recovery(boardNo, action) {
									@Override
									public void receiveMessage(Message message) {
										logger.info("回收口返回信息:" + message.toString());
										try {
											if (ReturnMsgType.RECOVERY_SUCC.equals(message.getType())) {
												ReturnBackUtil.putReturnBackInfo(result);
											}
										} catch (InterruptedException e1) {
											e1.printStackTrace();
										}
									}
								};
								StorageMediumPicker.putStorageMedium(recovery);
							}
						} else { // 以旧换新
							if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC
								RecoveryBox box = new RecoveryBox() {
									@Override
									public void receiveMessage(Message message) {
										logger.info("回收口返回信息:" + message);
										try {
											if (ReturnMsgType.RECOVERY_SUCC.equals(message.getType())) {
												MP3Player.play("AUDIO_C2.mp3");
												ReturnBackUtil.putReturnBackInfo(result);
												borrowMatService.borrowMat(info.getMatUseBillId(), info.getAccountId());
											} else {
												MP3Player.play("AUDIO_C5.mp3");
												WebSocketUtil.sendMsgSingle(
														DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
																SetupKey.Cabinet.MAIN_CABINET_ID),
														new WsMsg(WsMsgType.TEXT, "回收口扫描等待超时"));
											}
										} catch (Exception e1) {
											e1.printStackTrace();
										}
									}
								};
								switch (returnTypeId) {
								case 101:
									box.op(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP),
											Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
													SetupKey.Plc.PLC_PORT)),
											EnumRecoveryOp.TURNLEFT);
									break;
								default:
									box.op(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP),
											Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
													SetupKey.Plc.PLC_PORT)),
											EnumRecoveryOp.TURNRIGHT);
									break;
								}
							} else {
								Byte boardNo = null;
								if (cabinetType.equals(CabinetType.KNIFE_CABINET_DETB.name())
										|| cabinetType.equals(CabinetType.KNIFE_CABINET_DETA.name())) { // DETA或DETB
									boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
											SetupKey.Det.DET_BOARD_NO));
								} else if (cabinetType.equals(CabinetType.KNIFE_CABINET_C.name())) { // C型柜柜体类型
									boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
											SetupKey.CCabinet.DET_BOARD_NO_A));
								}
								RecoveryAction action = RecoveryAction.RIGHT;
								if (returnTypeId == 101) {
									action = RecoveryAction.LEFT;
								}
								StorageMedium recovery = new Recovery(boardNo, action) {
									@Override
									public void receiveMessage(Message message) {
										logger.info("以旧换新返回信息:" + message.toString());
										if (ReturnMsgType.RECOVERY_SUCC.equals(message.getType())) { // 翻转成功
											MP3Player.play("AUDIO_C2.mp3");
											try {
												ReturnBackUtil.putReturnBackInfo(result);
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
											try {
												List<CartInfo> cartList = new ArrayList<>();
												int borrowNum = 1;
												BillInfo billInfo = toolService.getBillInfo(info.getMatUseBillId());
												if (billInfo.getMatDetail().getBorrowTypeCode().equals("METER")) {
													borrowNum = billInfo.getMatDetail().getPackNum();
												}
												cartList.add(new CartInfo(borrowNum,
														billInfo.getMatDetail().getBorrowTypeCode(),
														billInfo.getReceiveType(), billInfo.getReceiveInfo(),
														info.getMatInfoId()));
												FutureTask<List<ExtraCabinet>> task = new FutureTask<>(
														new Callable<List<ExtraCabinet>>() {
															@Override
															public List<ExtraCabinet> call() throws Exception {
																// 暂存柜共享开关
																String shareSwitch = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
																		SetupKey.TemCabinet.TEM_SHARE_SWITCH);
																if (StringUtils.isBlank(shareSwitch)) {
																	shareSwitch = TrueOrFalse.FALSE.toString();
																}
																return toolService.sendCartToServer(cabinetId, cartList,
																		shareSwitch, info.getAccountId());
															}
														});
												new Thread(task).start();
												List<ExtraCabinet> cabinetList = task.get();
												List<ExtraCabinet> batchCabinetList = new ArrayList<>();
												for (ExtraCabinet cabinet : cabinetList) {
													// 行列式B型、储物柜、虚拟仓
													batchCabinetList.add(cabinet);
												}
												if (!batchCabinetList.isEmpty()) {
													storageMediumService.collarUse(batchCabinetList);
												}
											} catch (Exception ex) {
												ex.printStackTrace();
											}
										} else {
											WebSocketUtil.sendMsgSingle(
													DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
															SetupKey.Cabinet.MAIN_CABINET_ID),
													new WsMsg(WsMsgType.ERR_TIP, message.getCustomMsg()));
										}
									}
								};
								StorageMediumPicker.putStorageMedium(recovery);
							}
						}
					} catch (Exception e) {
						logger.error("以旧换新操作失败" + e.getMessage());
						MP3Player.play("AUDIO_C10.mp3");
					}
				}

				@Override
				public void receiveNfcResult(String result) {
					logger.error("NFC扫描结果:" + result);
				}
			};
			scan.start(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.RecCabinet.REC_SCAN_COM), Integer
					.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.RecCabinet.REC_SCAN_BAUD)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}