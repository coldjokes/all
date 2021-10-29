package com.dosth.tool.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dosth.common.constant.UsingStatus;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.tool.entity.CabinetSetup;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.LockParam;
import com.dosth.tool.repository.CabinetSetupRepository;
import com.dosth.tool.repository.EquDetailRepository;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.LockParamRepository;
import com.dosth.tool.service.TrolDrawerService;
import com.dosth.util.OpTip;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description 可控抽屉柜接口实现
 * @author Zhidong.Guo
 *
 */
@Service
public class TrolDrawerServiceImpl implements TrolDrawerService {
	
	private static final Logger logger = LoggerFactory.getLogger(TrolDrawerServiceImpl.class);
	
	@Autowired
	private EquDetailRepository equDetailRepository;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private CabinetSetupRepository cabinetSetupRepository;
	@Autowired
	private LockParamRepository lockParamRepository;

	@Override
	public OpTip editTroSetup(String equSettingId, String cabinetType, String trolCom, String trolBaud, String data) {
		OpTip tip = new OpTip("设置成功");
		try {
			// 删除锁控参数
			this.lockParamRepository.deleteLockParamsByCabinetId(equSettingId);
			// 删除柜体参数
			this.cabinetSetupRepository.deleteCabinetSetupsByCabinetId(equSettingId);
			// 删除货道
			this.equDetailStaRepository.deleteEquDetailStaByCabinetId(equSettingId);
			// 删除托盘行
			this.equDetailRepository.deleteEquDetailByCabinetId(equSettingId);
			

			JSONArray array = JSONArray.fromObject(data);
			JSONObject obj;
			int rowNo;
			int boardNo;
			int colNo;
			EquDetail detail;
			EquDetailSta sta;
			int boxIndex = 1;
			this.cabinetSetupRepository.save(new CabinetSetup(equSettingId, SetupKey.TrolDrawerCabinet.TROL_COM, trolCom));
			this.cabinetSetupRepository.save(new CabinetSetup(equSettingId, SetupKey.TrolDrawerCabinet.TROL_BAUD, trolBaud));
			for (int index = 0; index < array.size(); index++) {
				obj = JSONObject.fromObject(array.get(index));
				rowNo = obj.getInt("rowNo");
				boardNo = obj.getInt("boardNo");
				colNo = obj.getInt("colNo");
				this.lockParamRepository.save(new LockParam(equSettingId, CabinetType.TROL_DRAWER, boardNo, rowNo, colNo));
				detail = this.equDetailRepository.save(new EquDetail(equSettingId, rowNo));
				for (int j = 1; j <= colNo; j++) {
					sta = new EquDetailSta();
					sta.setBoardNo(boardNo);
					sta.setBoxIndex(boxIndex++);
					sta.setColNo(colNo);
					sta.setEquDetailId(detail.getId());
					sta.setInterval(1);
					sta.setLockIndex(colNo);
					sta.setMaxReserve(14);
					sta.setStatus(UsingStatus.ENABLE);
					sta.setWarnVal(3);
					this.equDetailStaRepository.save(sta);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("可控柜编辑参数失败:" + e.getMessage());
			tip.setCode(201);
			tip.setMessage("设置失败");
		}
		return tip;
	}
}