package com.dosth.toolcabinet.service.impl;

import org.springframework.stereotype.Service;

import com.cnbaosi.common.CabinetConstant;
import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.check.ConnectCheck;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.service.CabinetCheckService;

/**
 * @description 柜子检测接口实现类
 * @author guozhidong
 *
 */
@Service
public class CabinetCheckServiceImpl implements CabinetCheckService {

	@Override
	public void check(String cabinetId, Boolean... params) throws BscException {
		// 非PLC控制验证状态
		String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE);
		if (cabinetType.equals(CabinetType.SUB_CABINET.name())) { // 副柜查找主柜类型
			cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID), SetupKey.Cabinet.CABINET_TYPE);
		}
		CabinetType type = CabinetType.valueOf(cabinetType);
		switch (type) {
		case KNIFE_CABINET_C:
		case KNIFE_CABINET_C_A:
		case KNIFE_CABINET_C_B:
		case KNIFE_CABINET_DETA:
		case KNIFE_CABINET_DETB:
			if (params != null && params.length > 1 && params[1]) {
				if (CabinetConstant.collisionFlag != null && CabinetConstant.collisionFlag) {
					throw new BscException("撞机故障,请与管理员联系");
				}
			}
			break;
		default:
			break;
		}
		switch (type) {
		case KNIFE_CABINET_C:
		case KNIFE_CABINET_C_A:
		case KNIFE_CABINET_C_B:
		case KNIFE_CABINET_DETA:
		case KNIFE_CABINET_DETB:
		case STORE_CABINET:
		case TEM_CABINET:
			if (CabinetConstant.connectFlag != null && !CabinetConstant.connectFlag) {
				throw new BscException("主控连接故障,请与管理员联系!");
			}
			if (CabinetConstant.busyFlag != null && CabinetConstant.busyFlag) {
				throw new BscException("系统正忙,请稍后再试!");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void checkConnect(String cabinetId) throws BscException {
		// 非PLC控制验证状态
		String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE);
		CabinetType type = CabinetType.valueOf(cabinetType);
		Byte boardNo = null;
		switch (type) {
		case KNIFE_CABINET_C:
			boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.CCabinet.DET_BOARD_NO_A));
			break;
		case KNIFE_CABINET_C_A:
		case KNIFE_CABINET_C_B:
		case KNIFE_CABINET_DETA:
		case KNIFE_CABINET_DETB:
		case STORE_CABINET:
		case TEM_CABINET:
			boardNo = Byte.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Det.DET_BOARD_NO));
			break;
		default:
			break;
		}
		if (boardNo != null) {
			StorageMedium check = new ConnectCheck(boardNo) {
				@Override
				public void receiveMessage(Message msg) {
					CabinetConstant.connectFlag = true;
					if (msg.getType().equals(ReturnMsgType.TIME_OUT)) {
						CabinetConstant.connectFlag = false;
					}
				}
			};
			StorageMediumPicker.putStorageMedium(check);
		}
	}
}