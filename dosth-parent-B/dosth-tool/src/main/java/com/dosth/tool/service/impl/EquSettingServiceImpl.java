package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.StringUtil;
import com.dosth.criteria.EquSettingCriteria;
import com.dosth.dto.ExtraCabinet;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.instruction.board.enums.PrintCodeType;
import com.dosth.tool.common.state.BaudType;
import com.dosth.tool.common.state.DataSyncType;
import com.dosth.tool.common.state.HardwareCom;
import com.dosth.tool.common.state.NoticeType;
import com.dosth.tool.common.state.ScanType;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.entity.CabinetSetup;
import com.dosth.tool.entity.DataSyncState;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.LockParam;
import com.dosth.tool.entity.NoticeMgr;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.enums.EnumDoor;
import com.dosth.tool.repository.CabinetSetupRepository;
import com.dosth.tool.repository.DataSyncStateRepository;
import com.dosth.tool.repository.EquDetailRepository;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.LockParamRepository;
import com.dosth.tool.repository.NoticeMgrRepository;
import com.dosth.tool.repository.SubBoxRepository;
import com.dosth.tool.service.EquSettingService;
import com.dosth.toolcabinet.dto.CabinetInfo;
import com.google.common.collect.Lists;

/**
 * ????????????Service??????
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class EquSettingServiceImpl implements EquSettingService {

	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private EquDetailRepository equDetailRepository;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private SubBoxRepository subBoxRepository;
	@Autowired
	private CabinetSetupRepository cabinetSetupRepository;
	@Autowired
	private LockParamRepository lockParamSetupRepository;
	@Autowired
	private NoticeMgrRepository noticeMgrRepository;
	@Autowired
	private DataSyncStateRepository dataSyncStateRepository;

	/**
	 * ???????????????????????????
	 */

	public void addNoticeMgr(EquSetting equSetting) {
		List<NoticeMgr> noticeMgrList = Lists.newArrayList();
		noticeMgrList.add(new NoticeMgr(equSetting.getEquSettingName(), equSetting.getId(), NoticeType.PRINT, 0, 0, 0,
				"admin,", "1,"));
		noticeMgrList.add(new NoticeMgr(equSetting.getEquSettingName(), equSetting.getId(), NoticeType.RECOVERY, 0, 0,
				0, "admin,", "1,"));
		noticeMgrList.add(new NoticeMgr(equSetting.getEquSettingName(), equSetting.getId(), NoticeType.STOCK, 0, 0, 0,
				"admin,", "1,"));
		noticeMgrList.add(new NoticeMgr(equSetting.getEquSettingName(), equSetting.getId(), NoticeType.TEMCABINET, 0, 0,
				0, "admin,", "1,"));

		for (NoticeMgr noticeMgr : noticeMgrList) {
			this.noticeMgrRepository.save(noticeMgr);
		}
	}

	/**
	 * ?????????????????????
	 */
	public void addStoreCabinet(String equSettingId, CabinetType cabinetType) {
		// ????????????????????????
		Map<Integer, Integer[]> stoSetupMap = new HashMap<>();

		// ????????????????????????
		Integer[] stoSetupArray = new Integer[3];
		// ????????????
		stoSetupArray[0] = 2;
		// ????????????
		stoSetupArray[1] = 10;
		// ????????????
		stoSetupArray[2] = 5;
		stoSetupMap.put(1, stoSetupArray);

		// ??????????????????????????????
		Integer boxIndex = 1;// ?????????
		for (Entry<Integer, Integer[]> entry : stoSetupMap.entrySet()) {
			Integer[] setupArray = entry.getValue();
			Integer boardNo = setupArray[0];
			Integer rowNo = setupArray[1];
			Integer colNo = setupArray[2];
			this.lockParamSetupRepository.save(new LockParam(equSettingId, cabinetType, boardNo, rowNo, colNo));

			// ?????????????????????
			Integer lockIndex = 1;// ?????????
			EquDetail equDetail = new EquDetail();
			for (int i = 1; i <= rowNo; i++) {
				equDetail = this.equDetailRepository.save(new EquDetail(equSettingId, i));
				for (int j = 1; j <= colNo; j++) {
					this.equDetailStaRepository.save(
							new EquDetailSta(equDetail.getId(), i, j, 16, 3, new Date(), boardNo, lockIndex, boxIndex));
					boxIndex++;
					lockIndex++;
				}
			}
		}
	}

	/**
	 * ?????????????????????
	 */
	public void addSubBox(String equSettingId, CabinetType cabinetType) {
		// ????????????????????????
		Map<Integer, Integer[]> temSetupMap = new HashMap<>();

		// ????????????????????????
		Integer[] temSetupArray1 = new Integer[3];
		// ????????????
		temSetupArray1[0] = 1;
		// ????????????
		temSetupArray1[1] = 10;
		// ????????????
		temSetupArray1[2] = 5;
		temSetupMap.put(1, temSetupArray1);

		Integer[] temSetupArray2 = new Integer[3];
		// ????????????
		temSetupArray2[0] = 2;
		// ????????????
		temSetupArray2[1] = 10;
		// ????????????
		temSetupArray2[2] = 5;
		temSetupMap.put(2, temSetupArray2);

		// ??????????????????????????????
		Integer boxIndex = 1;// ?????????
		for (Entry<Integer, Integer[]> entry : temSetupMap.entrySet()) {
			Integer[] setupArray = entry.getValue();
			Integer boardNo = setupArray[0];
			Integer rowNo = setupArray[1];
			Integer colNo = setupArray[2];
			this.lockParamSetupRepository.save(new LockParam(equSettingId, cabinetType, boardNo, rowNo, colNo));

			// ?????????????????????
			Integer lockIndex = 1;// ?????????
			for (int i = 1; i <= rowNo; i++) {
				for (int j = 1; j <= colNo; j++) {
					this.subBoxRepository.save(new SubBox(equSettingId, boardNo, lockIndex, boxIndex, i, j));
					boxIndex++;
					lockIndex++;
				}
			}
		}
	}

	/**
	 * ?????????????????????
	 */
	public void addEquDetailSta(EquSetting equSetting, CabinetType cabinetType) {
		EquDetail detail = null;
		EquDetailSta detailSta = null;
		Map<String, String> defaultMap = new HashMap<>();

		// PLC?????????
		defaultMap.put(SetupKey.Plc.PLC_IP, "192.168.6.1");
		defaultMap.put(SetupKey.Plc.PLC_PORT, "502");

		// ?????????A????????????B?????????
		defaultMap.put(SetupKey.Det.DET_BOARD_NO, "0");
		defaultMap.put(SetupKey.Det.DET_LEVEL_HEIGHT, "25");
		defaultMap.put(SetupKey.Det.DET_LEVEL_SPACING, "175");

		// C??????
		defaultMap.put(SetupKey.CCabinet.DET_BOARD_NO_A, "0");
		defaultMap.put(SetupKey.CCabinet.DET_BOARD_NO_B, "1");

		Integer PLC_TOP_LEVEL = 1;
		Integer DET_TOP_HEIGHT = 0;

		for (int rowNo = 1; rowNo <= equSetting.getRowNum(); rowNo++) {
			PLC_TOP_LEVEL = rowNo;
			detail = new EquDetail(equSetting.getId(), rowNo);

			switch (cabinetType) {
			case KNIFE_CABINET_PLC:
				detail.setIp(defaultMap.get(SetupKey.Plc.PLC_IP) + rowNo);
				detail.setPort(defaultMap.get(SetupKey.Plc.PLC_PORT));
				break;
			case KNIFE_CABINET_DETA:
			case KNIFE_CABINET_DETB:
			case KNIFE_CABINET_C:
			case KNIFE_CABINET_C_A:
			case KNIFE_CABINET_C_B:
				DET_TOP_HEIGHT = (detail.getRowNo() - 1)
						* Integer.valueOf(defaultMap.get(SetupKey.Det.DET_LEVEL_SPACING))
						+ Integer.valueOf(defaultMap.get(SetupKey.Det.DET_LEVEL_HEIGHT));
				detail.setLevelHt(DET_TOP_HEIGHT);
				break;
			default:
				break;
			}

			detail = this.equDetailRepository.save(detail);

			for (int colNo = 1; colNo <= equSetting.getColNum(); colNo++) {
				detailSta = new EquDetailSta();
				detailSta.setEquDetailId(detail.getId());
				detailSta.setColNo(colNo);
				detailSta.setMaxReserve(16);
				detailSta.setWarnVal(3);
				detailSta.setLastFeedTime(new Date());

				switch (cabinetType) {
				case KNIFE_CABINET_DETA:
				case KNIFE_CABINET_DETB:
				case KNIFE_CABINET_C:
					detailSta.setBoardNo(Integer.valueOf(defaultMap.get(SetupKey.Det.DET_BOARD_NO)));
					detailSta.setLockIndex((rowNo - 1) * 12 + colNo);
					break;
				case KNIFE_CABINET_C_A:
					detailSta.setBoardNo(Integer.valueOf(defaultMap.get(SetupKey.CCabinet.DET_BOARD_NO_A)));
					detailSta.setLockIndex((rowNo - 1) * 12 + colNo);
					break;
				case KNIFE_CABINET_C_B:
					detailSta.setBoardNo(Integer.valueOf(defaultMap.get(SetupKey.CCabinet.DET_BOARD_NO_B)));
					detailSta.setLockIndex((rowNo - 1) * 12 + colNo);
					break;
				default:
					break;
				}

				this.equDetailStaRepository.save(detailSta);
			}
		}

		switch (cabinetType) {
		case KNIFE_CABINET_PLC:
			this.cabinetSetupRepository.save(
					new CabinetSetup(equSetting.getId(), SetupKey.Plc.PLC_TOP_LEVEL, String.valueOf(PLC_TOP_LEVEL)));
			break;
		case KNIFE_CABINET_DETA:
		case KNIFE_CABINET_DETB:
		case KNIFE_CABINET_C:
			this.cabinetSetupRepository.save(
					new CabinetSetup(equSetting.getId(), SetupKey.Det.DET_TOP_HEIGHT, String.valueOf(DET_TOP_HEIGHT)));
			break;
		default:
			break;
		}

	}

	@Override
	public void save(EquSetting equSetting) throws DoSthException {
		Map<String, String> map = new HashMap<>();
		Map<String, String> defaultMap = new HashMap<>();

		// ??????????????????
		defaultMap.put(SetupKey.Public.FACE_LOGIN, TrueOrFalse.TRUE.toString());
		defaultMap.put(SetupKey.Public.SCAN_COM, HardwareCom.COM3.toString());
		defaultMap.put(SetupKey.Public.SCAN_TYPE, ScanType.DWSN.toString());
		defaultMap.put(SetupKey.Public.SCAN_BAUD, BaudType.BAUD_115200.getMessage());
		defaultMap.put(SetupKey.Public.PRINT_COM, HardwareCom.COM4.toString());
		defaultMap.put(SetupKey.Public.PRINT_CUT, TrueOrFalse.FALSE.toString());
		defaultMap.put(SetupKey.Public.PRINT_BAUD, BaudType.BAUD_115200.getMessage());
		defaultMap.put(SetupKey.Public.PRINT_TYPE_CODE, PrintCodeType.QR.toString());

		// PLC????????????
		defaultMap.put(SetupKey.Plc.PLC_IP, "192.168.1.67");
		defaultMap.put(SetupKey.Plc.PLC_PORT, "502");

		// ?????????????????????
		defaultMap.put(SetupKey.Det.DET_COM, HardwareCom.COM2.toString());
		defaultMap.put(SetupKey.Det.DET_BAUD, BaudType.BAUD_19200.getMessage());
		defaultMap.put(SetupKey.Det.DET_BOARD_NO, "0");
		defaultMap.put(SetupKey.Det.DET_DOOR_HEIGHT, "406");
		defaultMap.put(SetupKey.Det.DET_LEVEL_HEIGHT, "25");
		defaultMap.put(SetupKey.Det.DET_LEVEL_SPACING, "175");

		// C??????????????????
		defaultMap.put(SetupKey.CCabinet.DET_BOARD_NO_A, "0");
		defaultMap.put(SetupKey.CCabinet.DET_BOARD_NO_B, "1");

		// ?????????????????????
		defaultMap.put(SetupKey.TemCabinet.TEM_COM, HardwareCom.COM6.toString());
		defaultMap.put(SetupKey.TemCabinet.TEM_BAUD, BaudType.BAUD_19200.getMessage());
		defaultMap.put(SetupKey.TemCabinet.TEM_SHARE_SWITCH, equSetting.getShareSwitch());
		
		// ?????????????????????
		defaultMap.put(SetupKey.TrolDrawerCabinet.TROL_COM, HardwareCom.COM6.toString());
		defaultMap.put(SetupKey.TrolDrawerCabinet.TROL_BAUD, BaudType.BAUD_19200.getMessage());

		// ?????????????????????
		defaultMap.put(SetupKey.StoreCabinet.STORE_COM, HardwareCom.COM6.toString());
		defaultMap.put(SetupKey.StoreCabinet.STORE_BAUD, BaudType.BAUD_19200.getMessage());

		// ?????????????????????
		defaultMap.put(SetupKey.RecCabinet.REC_SCAN_COM, "");
		defaultMap.put(SetupKey.RecCabinet.REC_SCAN_TYPE, "");
		defaultMap.put(SetupKey.RecCabinet.REC_SCAN_BAUD, "");

		switch (equSetting.getCabinetType()) {
		case TROL_DRAWER:
			// ????????????
			equSetting.setDoor(EnumDoor.LEFT);
			equSetting.setStatus(UsingStatus.ENABLE);
			equSetting.setRowNum(0);
			equSetting.setColNum(0);
			equSetting = this.equSettingRepository.save(equSetting);

			// ????????????
			map.put(SetupKey.TrolDrawerCabinet.TROL_COM, defaultMap.get(SetupKey.TrolDrawerCabinet.TROL_COM));
			map.put(SetupKey.TrolDrawerCabinet.TROL_BAUD, defaultMap.get(SetupKey.TrolDrawerCabinet.TROL_BAUD));
			for (Entry<String, String> entry : map.entrySet()) {
				this.cabinetSetupRepository
						.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
			}
			break;
		// ?????????
		case RECOVERY_CABINET:
			// ????????????
			equSetting.setDoor(EnumDoor.LEFT);
			equSetting.setStatus(UsingStatus.ENABLE);
			equSetting.setRowNum(0);
			equSetting.setColNum(0);
			equSetting = this.equSettingRepository.save(equSetting);

			// ????????????
			map.put(SetupKey.RecCabinet.REC_SCAN_COM, defaultMap.get(SetupKey.RecCabinet.REC_SCAN_COM));
			map.put(SetupKey.RecCabinet.REC_SCAN_TYPE, defaultMap.get(SetupKey.RecCabinet.REC_SCAN_TYPE));
			map.put(SetupKey.RecCabinet.REC_SCAN_BAUD, defaultMap.get(SetupKey.RecCabinet.REC_SCAN_BAUD));
			for (Entry<String, String> entry : map.entrySet()) {
				this.cabinetSetupRepository
						.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
			}
			break;
		// ?????????
		case VIRTUAL_WAREHOUSE:
			// ????????????
			equSetting.setDoor(EnumDoor.LEFT);
			equSetting.setStatus(UsingStatus.ENABLE);
			equSetting = this.equSettingRepository.save(equSetting);

			// ?????????????????????
			addEquDetailSta(equSetting, equSetting.getCabinetType());
			break;
		// ?????????
		case STORE_CABINET:
			// ????????????
			equSetting.setDoor(EnumDoor.LEFT);
			equSetting.setStatus(UsingStatus.ENABLE);
			equSetting.setRowNum(0);
			equSetting.setColNum(0);
			equSetting = this.equSettingRepository.save(equSetting);

			// ?????????????????????
			addStoreCabinet(equSetting.getId(), CabinetType.STORE_CABINET);

			// ????????????
			map.put(SetupKey.StoreCabinet.STORE_COM, defaultMap.get(SetupKey.StoreCabinet.STORE_COM));
			map.put(SetupKey.StoreCabinet.STORE_BAUD, defaultMap.get(SetupKey.StoreCabinet.STORE_BAUD));
			for (Entry<String, String> entry : map.entrySet()) {
				this.cabinetSetupRepository
						.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
			}
			break;
		// ?????????
		case TEM_CABINET:
			// ????????????
			equSetting.setDoor(EnumDoor.LEFT);
			equSetting.setStatus(UsingStatus.ENABLE);
			equSetting.setRowNum(0);
			equSetting.setColNum(0);
			equSetting = this.equSettingRepository.save(equSetting);

			// ?????????????????????
			addSubBox(equSetting.getId(), CabinetType.TEM_CABINET);

			// ????????????
			map.put(SetupKey.TemCabinet.TEM_COM, defaultMap.get(SetupKey.TemCabinet.TEM_COM));
			map.put(SetupKey.TemCabinet.TEM_BAUD, defaultMap.get(SetupKey.TemCabinet.TEM_BAUD));
			map.put(SetupKey.TemCabinet.TEM_SHARE_SWITCH, defaultMap.get(SetupKey.TemCabinet.TEM_SHARE_SWITCH));
			
			for (Entry<String, String> entry : map.entrySet()) {
				this.cabinetSetupRepository
						.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
			}
			break;
		// PLC?????????
		case KNIFE_CABINET_PLC:
			// ????????????
			equSetting.setDoor(EnumDoor.LEFT);
			equSetting.setStatus(UsingStatus.ENABLE);
			equSetting = this.equSettingRepository.save(equSetting);

			// ?????????????????????
			addNoticeMgr(equSetting);

			// ?????????????????????
			addEquDetailSta(equSetting, equSetting.getCabinetType());

			// ????????????
			map.put(SetupKey.Public.FACE_LOGIN, defaultMap.get(SetupKey.Public.FACE_LOGIN));
			map.put(SetupKey.Public.SCAN_COM, defaultMap.get(SetupKey.Public.SCAN_COM));
			map.put(SetupKey.Public.SCAN_TYPE, defaultMap.get(SetupKey.Public.SCAN_TYPE));
			map.put(SetupKey.Public.SCAN_BAUD, defaultMap.get(SetupKey.Public.SCAN_BAUD));
			map.put(SetupKey.Public.PRINT_COM, defaultMap.get(SetupKey.Public.PRINT_COM));
			map.put(SetupKey.Public.PRINT_CUT, defaultMap.get(SetupKey.Public.PRINT_CUT));
			map.put(SetupKey.Public.PRINT_BAUD, defaultMap.get(SetupKey.Public.PRINT_BAUD));
			map.put(SetupKey.Public.PRINT_TYPE_CODE, defaultMap.get(SetupKey.Public.PRINT_TYPE_CODE));
			map.put(SetupKey.Plc.PLC_IP, defaultMap.get(SetupKey.Plc.PLC_IP));
			map.put(SetupKey.Plc.PLC_PORT, defaultMap.get(SetupKey.Plc.PLC_PORT));
			for (Entry<String, String> entry : map.entrySet()) {
				this.cabinetSetupRepository
						.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
			}
			break;
		// ?????????A,B?????????
		case KNIFE_CABINET_DETA:
		case KNIFE_CABINET_DETB:
			// ????????????
			equSetting.setDoor(EnumDoor.LEFT);
			equSetting.setStatus(UsingStatus.ENABLE);
			equSetting = this.equSettingRepository.save(equSetting);

			// ?????????????????????
			addNoticeMgr(equSetting);

			// ????????????
			addEquDetailSta(equSetting, equSetting.getCabinetType());

			// ????????????
			map.put(SetupKey.Public.FACE_LOGIN, defaultMap.get(SetupKey.Public.FACE_LOGIN));
			map.put(SetupKey.Public.SCAN_COM, defaultMap.get(SetupKey.Public.SCAN_COM));
			map.put(SetupKey.Public.SCAN_TYPE, defaultMap.get(SetupKey.Public.SCAN_TYPE));
			map.put(SetupKey.Public.SCAN_BAUD, defaultMap.get(SetupKey.Public.SCAN_BAUD));
			map.put(SetupKey.Public.PRINT_COM, defaultMap.get(SetupKey.Public.PRINT_COM));
			map.put(SetupKey.Public.PRINT_CUT, defaultMap.get(SetupKey.Public.PRINT_CUT));
			map.put(SetupKey.Public.PRINT_BAUD, defaultMap.get(SetupKey.Public.PRINT_BAUD));
			map.put(SetupKey.Public.PRINT_TYPE_CODE, defaultMap.get(SetupKey.Public.PRINT_TYPE_CODE));
			map.put(SetupKey.Det.DET_COM, defaultMap.get(SetupKey.Det.DET_COM));
			map.put(SetupKey.Det.DET_BAUD, defaultMap.get(SetupKey.Det.DET_BAUD));
			map.put(SetupKey.Det.DET_BOARD_NO, defaultMap.get(SetupKey.Det.DET_BOARD_NO));
			map.put(SetupKey.Det.DET_DOOR_HEIGHT, defaultMap.get(SetupKey.Det.DET_DOOR_HEIGHT));
			map.put(SetupKey.Det.DET_LEVEL_HEIGHT, defaultMap.get(SetupKey.Det.DET_LEVEL_HEIGHT));
			map.put(SetupKey.Det.DET_LEVEL_SPACING, defaultMap.get(SetupKey.Det.DET_LEVEL_SPACING));
			for (Entry<String, String> entry : map.entrySet()) {
				this.cabinetSetupRepository
						.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
			}
			break;
		// C??????-??????
		case KNIFE_CABINET_C:
			// ????????????-??????
			equSetting.setDoor(EnumDoor.LEFT);
			equSetting.setStatus(UsingStatus.ENABLE);
			equSetting.setRowNum(0);
			equSetting.setColNum(0);
			equSetting.setDoor(EnumDoor.ALL);
			equSetting = this.equSettingRepository.save(equSetting);

			// ????????????-A???
			EquSetting equSettingA = this.equSettingRepository.save(new EquSetting("C??????-A???", "010", 9, 10,
					CabinetType.KNIFE_CABINET_C_A, equSetting.getId(), "1", UsingStatus.ENABLE, EnumDoor.LEFT));

			// ??????A?????????
			addEquDetailSta(equSettingA, equSettingA.getCabinetType());

			// ????????????-B???
			EquSetting equSettingB = this.equSettingRepository.save(new EquSetting("C??????-B???", "020", 9, 10,
					CabinetType.KNIFE_CABINET_C_B, equSetting.getId(), "1", UsingStatus.ENABLE, EnumDoor.RIGHT));

			// ??????B?????????
			addEquDetailSta(equSettingB, equSettingB.getCabinetType());

			// ?????????????????????
			addNoticeMgr(equSetting);

			// ????????????
			map.put(SetupKey.Public.FACE_LOGIN, defaultMap.get(SetupKey.Public.FACE_LOGIN));
			map.put(SetupKey.Public.SCAN_COM, defaultMap.get(SetupKey.Public.SCAN_COM));
			map.put(SetupKey.Public.SCAN_TYPE, defaultMap.get(SetupKey.Public.SCAN_TYPE));
			map.put(SetupKey.Public.SCAN_BAUD, defaultMap.get(SetupKey.Public.SCAN_BAUD));
			map.put(SetupKey.Public.PRINT_COM, defaultMap.get(SetupKey.Public.PRINT_COM));
			map.put(SetupKey.Public.PRINT_CUT, defaultMap.get(SetupKey.Public.PRINT_CUT));
			map.put(SetupKey.Public.PRINT_BAUD, defaultMap.get(SetupKey.Public.PRINT_BAUD));
			map.put(SetupKey.Public.PRINT_TYPE_CODE, defaultMap.get(SetupKey.Public.PRINT_TYPE_CODE));
			map.put(SetupKey.Det.DET_COM, defaultMap.get(SetupKey.Det.DET_COM));
			map.put(SetupKey.Det.DET_BAUD, defaultMap.get(SetupKey.Det.DET_BAUD));
			map.put(SetupKey.Det.DET_DOOR_HEIGHT, defaultMap.get(SetupKey.Det.DET_DOOR_HEIGHT));
			map.put(SetupKey.Det.DET_LEVEL_HEIGHT, defaultMap.get(SetupKey.Det.DET_LEVEL_HEIGHT));
			map.put(SetupKey.Det.DET_LEVEL_SPACING, defaultMap.get(SetupKey.Det.DET_LEVEL_SPACING));

			for (Entry<String, String> entry : map.entrySet()) {
				this.cabinetSetupRepository
						.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
			}

			this.cabinetSetupRepository.save(new CabinetSetup(equSettingA.getId(), SetupKey.CCabinet.DET_BOARD_NO_A,
					defaultMap.get(SetupKey.CCabinet.DET_BOARD_NO_A)));
			this.cabinetSetupRepository.save(new CabinetSetup(equSettingB.getId(), SetupKey.CCabinet.DET_BOARD_NO_B,
					defaultMap.get(SetupKey.CCabinet.DET_BOARD_NO_B)));
			break;
		// ??????
		case SUB_CABINET:
			// ?????????????????????
			Criteria<EquSetting> criteria = new Criteria<>();
			criteria.add(Restrictions.eq("id", equSetting.getEquSettingParentId(), true));
			List<EquSetting> equSettingList = this.equSettingRepository.findAll(criteria);

			CabinetType parentCabinetType = null;
			if (equSettingList != null && equSettingList.size() > 0) {
				parentCabinetType = equSettingList.get(0).getCabinetType();
			}

			switch (parentCabinetType) {
			// PLC?????????
			case KNIFE_CABINET_PLC:
				// ????????????
				equSetting.setDoor(EnumDoor.LEFT);
				equSetting.setStatus(UsingStatus.ENABLE);
				equSetting = this.equSettingRepository.save(equSetting);

				// ?????????????????????
				addEquDetailSta(equSetting, parentCabinetType);

				// ????????????
				map.put(SetupKey.Plc.PLC_IP, defaultMap.get(SetupKey.Plc.PLC_IP));
				map.put(SetupKey.Plc.PLC_PORT, defaultMap.get(SetupKey.Plc.PLC_PORT));
				for (Entry<String, String> entry : map.entrySet()) {
					this.cabinetSetupRepository
							.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
				}
				break;
			// ?????????A,B?????????
			case KNIFE_CABINET_DETA:
			case KNIFE_CABINET_DETB:
			case KNIFE_CABINET_C:
				// ????????????
				equSetting.setDoor(EnumDoor.LEFT);
				equSetting.setStatus(UsingStatus.ENABLE);
				equSetting = this.equSettingRepository.save(equSetting);

				// ????????????
				addEquDetailSta(equSetting, parentCabinetType);

				// ????????????
				map.put(SetupKey.Det.DET_COM, defaultMap.get(SetupKey.Det.DET_COM));
				map.put(SetupKey.Det.DET_BAUD, defaultMap.get(SetupKey.Det.DET_BAUD));
				map.put(SetupKey.Det.DET_BOARD_NO, defaultMap.get(SetupKey.Det.DET_BOARD_NO));
				map.put(SetupKey.Det.DET_DOOR_HEIGHT, defaultMap.get(SetupKey.Det.DET_DOOR_HEIGHT));
				map.put(SetupKey.Det.DET_LEVEL_HEIGHT, defaultMap.get(SetupKey.Det.DET_LEVEL_HEIGHT));
				map.put(SetupKey.Det.DET_LEVEL_SPACING, defaultMap.get(SetupKey.Det.DET_LEVEL_SPACING));
				for (Entry<String, String> entry : map.entrySet()) {
					this.cabinetSetupRepository
							.save(new CabinetSetup(equSetting.getId(), entry.getKey(), entry.getValue()));
				}
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		// ???????????????????????????
		for (DataSyncType type : DataSyncType.values()) {
			this.dataSyncStateRepository.save(new DataSyncState(equSetting.getId(), type, new Date()));
		}
	}

	@Override
	public EquSetting update(EquSetting equSetting) throws DoSthException {
		return this.equSettingRepository.saveAndFlush(equSetting);
	}

	@Override
	public void delete(EquSetting equSetting) throws DoSthException {
		// ????????????
		equSetting.setStatus(UsingStatus.DISABLE);
		update(equSetting);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public EquSetting get(Serializable id) throws DoSthException {
		return this.equSettingRepository.findOne(id);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<EquSetting> getPage(int pageNo, int pageSize, String name, String cabinetType) throws DoSthException {
		Criteria<EquSetting> criteria = new Criteria<>();
		criteria.add(
				Restrictions.or(new Criterion[] { Restrictions.eq("cabinetType", CabinetType.KNIFE_CABINET_PLC, true),
						Restrictions.eq("cabinetType", CabinetType.KNIFE_CABINET_DETA, true),
						Restrictions.eq("cabinetType", CabinetType.KNIFE_CABINET_DETB, true),
						Restrictions.eq("cabinetType", CabinetType.KNIFE_CABINET_C, true),
						Restrictions.eq("cabinetType", CabinetType.VIRTUAL_WAREHOUSE, true) }));
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.like("equSettingName", name.trim(), true));
		}
		if (cabinetType != null && !"".equals(cabinetType)) {
			criteria.add(Restrictions.eq("cabinetType", CabinetType.valueOf(cabinetType), true));
		}
		List<EquSetting> list = this.equSettingRepository.findAll(criteria).stream()
				.filter(equSetting -> equSetting.getEquSettingParentId() == null
						|| "".equals(equSetting.getEquSettingParentId()))
				.collect(Collectors.toList());
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(list, pageable);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public EquSetting getEquInfos(String equSettingId) throws DoSthException {

		EquSetting equSetting = null;
		List<EquDetail> infoList = null;
		Map<Integer, Map<Integer, EquDetailSta>> infoMap = new HashMap<>();

		if (StringUtil.isNotBlank(equSettingId) && equSettingId.startsWith("D_")) {
			infoList = this.equDetailRepository.getEquDetailListById(equSettingId.substring(2));
			equSetting = get(infoList.get(0).getEquSettingId());
		} else {
			equSetting = get(equSettingId);
			infoList = this.equDetailRepository.getEquDetailListBySettingId(equSettingId);
		}

		Map<Integer, EquDetailSta> rowMap;
		List<EquDetailSta> staList = null;
		for (EquDetail detail : infoList) {
			rowMap = infoMap.get(detail.getRowNo());
			if (rowMap == null) {
				rowMap = new HashMap<>();
			}
			staList = this.equDetailStaRepository.getStaListByDetailId(detail.getId());
			// ??????????????????????????????
			for (EquDetailSta sta : staList) {
				if (UsingStatus.DISABLE.equals(sta.getStatus())) {
					continue;
				}
				rowMap.put(sta.getColNo(), sta);
			}
			// ????????????????????????????????????
			infoMap.put(detail.getRowNo(), rowMap);
		}
		equSetting.setEquInfoMap(infoMap);
		return equSetting;
	}

	@Override
	public List<ZTreeNode> treeEqu() throws DoSthException {
		List<ZTreeNode> tree = new ArrayList<>();
		Criteria<EquSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		criteria.add(Restrictions.ne("cabinetType", CabinetType.TEM_CABINET, true));
		criteria.add(Restrictions.ne("cabinetType", CabinetType.RECOVERY_CABINET, true));
		List<EquSetting> settingList = this.equSettingRepository.findAll(criteria);
		for (EquSetting setting : settingList) {
			if (setting.getEquSettingParentId() != null && !"".equals(setting.getEquSettingParentId())) {
				if (setting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_A)
						|| setting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_B)
						|| setting.getEquSettingParent().getCabinetType().equals(CabinetType.KNIFE_CABINET_C)) {
					tree.add(new ZTreeNode(setting.getId(), "C_" + setting.getEquSettingParentId(),
							setting.getEquSettingName()));
				} else {
					tree.add(new ZTreeNode(setting.getId(), setting.getEquSettingParentId(),
							setting.getEquSettingName()));
				}
			} else {
				if (setting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C)) {
					tree.add(new ZTreeNode("C_" + setting.getId(), setting.getId(), setting.getEquSettingName()));
				} else {
					tree.add(new ZTreeNode(setting.getId(), setting.getId(), setting.getEquSettingName()));
				}
			}
		}
		return tree;
	}

	@Override
	public List<ZTreeNode> initEquSettingTree(String equSettingId) throws DoSthException {
		List<ZTreeNode> treeList = new ArrayList<>();
		ZTreeNode node;
		Criteria<EquSetting> criteria = new Criteria<EquSetting>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		criteria.add(Restrictions.eq("id", equSettingId, true));
		List<EquSetting> list = this.equSettingRepository.findAll(criteria);

		for (EquSetting setting : list) {
			node = new ZTreeNode("B_" + setting.getId(), setting.getId(), setting.getEquSettingName());
			node.setIcon("/static/img/one.png");
			treeList.add(node);
		}

		Criteria<EquDetail> criteriaDetail = new Criteria<EquDetail>();
		criteriaDetail.add(Restrictions.eq("equSettingId", equSettingId, true));
		List<EquDetail> detailList = this.equDetailRepository.findAll(criteriaDetail);

		for (EquDetail detail : detailList) {
			node = new ZTreeNode("C_" + detail.getId(), "B_" + detail.getEquSettingId(), "???" + detail.getRowNo() + "???");
			node.setIcon("/static/img/two.png");
			treeList.add(node);
		}

		return treeList;
	}

	@Override
	public EquSetting editCabinet(String equSettingId) throws DoSthException {
		EquSetting equSetting = this.equSettingRepository.getOne(equSettingId);
		this.equSettingRepository.saveAndFlush(equSetting);
		return equSetting;
	}

	@Override
	public List<EquSetting> findAllCabinet(EquSettingCriteria equSettingCriteria) {
		Criteria<EquSetting> criteria = new Criteria<>();
//		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));

		String equSettingId = equSettingCriteria.getId();
		if (equSettingId != null && !"".equals(equSettingId)) {
			criteria.add(Restrictions.eq("id", equSettingId, true));
		}

		String equSettingName = equSettingCriteria.getEquSettingName();
		if (equSettingName != null && !"".equals(equSettingName)) {
			criteria.add(Restrictions.eq("equSettingName", equSettingName, true));
		}

		String serialNo = equSettingCriteria.getSerialNo();
		if (serialNo != null && !"".equals(serialNo)) {
			criteria.add(Restrictions.eq("serialNo", serialNo, true));
		}

		CabinetType cabinetType = equSettingCriteria.getCabinetType();
		if (cabinetType != null) {
			criteria.add(Restrictions.eq("cabinetType", cabinetType, true));
		}

		String equSettingParentId = equSettingCriteria.getEquSettingParentId();
		if (equSettingParentId != null && !"".equals(equSettingParentId)) {
			criteria.add(Restrictions.eq("equSettingParentId", equSettingParentId, true));
		}

		String accountId = equSettingCriteria.getAccountId();
		if (accountId != null && !"".equals(accountId)) {
			criteria.add(Restrictions.eq("accountId", accountId, true));
		}

		List<EquSetting> equSetting = this.equSettingRepository.findAll(criteria);
		return equSetting;
	}

	@Override
	public String getEquSettingListByEquInfoId(String cabinetId) throws DoSthException {
		List<EquSetting> list = equSettingRepository.getEquSettingList(cabinetId);
		for (EquSetting cabinet : list) {
			String sonId = cabinet.getId();
			if (!sonId.equals(cabinetId)) {
				return sonId;
			}
		}
		return cabinetId;
	}

	@Override
	public CabinetInfo getCabinetInfo(String cabinetId) throws DoSthException {
		EquSetting equSetting = this.equSettingRepository.findOne(cabinetId);
		CabinetInfo cabinetInfo = new CabinetInfo(equSetting.getId(), equSetting.getEquSettingName(),
				equSetting.getCabinetType().toString(), equSetting.getRowNum(), equSetting.getColNum(),
				equSetting.getStatus().toString());
		return cabinetInfo;
	}

	@Override
	public List<ExtraCabinet> getCabinetList(@RequestParam("serialNo") String serialNo) {
		EquSetting mainSetting = this.equSettingRepository.getEquSettingBySerialNo(serialNo);
		List<EquSetting> settingList = this.equSettingRepository.getEquSettingList(mainSetting.getId());
		//
		List<ExtraCabinet> cabinetList = new ArrayList<>();
		for (EquSetting setting : settingList) {
			cabinetList.add(new ExtraCabinet(setting.getSerialNo(), setting.getId(), setting.getEquSettingParentId(),
					setting.getEquSettingName(), null,
					setting.getDoor() == null ? EnumDoor.LEFT.name() : setting.getDoor().name(), null,
					setting.getCabinetType().name()));
		}
		return cabinetList;
	}

	@Override
	public EquSetting getEquSettingBySerialNo(String serialNo) {
		return this.equSettingRepository.getEquSettingBySerialNo(serialNo);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<EquSetting> getsubPage(int pageNo, int pageSize, String equSettingParentId) {
		Criteria<EquSetting> criteria = new Criteria<>();
		if (equSettingParentId != null && !"".equals(equSettingParentId)) {
			criteria.add(Restrictions.eq("equSettingParentId", equSettingParentId, true));
		}
		List<EquSetting> list = this.equSettingRepository.findAll(criteria);
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(list, pageable);
	}

	@Override
	public List<ExtraCabinet> getCabinetTreeList(String mainCabinetId) {
		List<EquSetting> settingList = this.equSettingRepository.getCabinetTreeList(mainCabinetId);
		List<ExtraCabinet> cabinetList = new ArrayList<>();
		ExtraCabinet cabinet = null;
		List<String> boardNoList = null;
		String doorHeightKeyId = null;
		for (EquSetting setting : settingList) {
			cabinet = new ExtraCabinet();
			// ??????Id
			cabinet.setCabinetId(setting.getId());
			// ???????????????Id
			cabinet.setParentCabinetId(setting.getEquSettingParentId());
			// ????????????
			cabinet.setCabinetName(setting.getEquSettingName());
			// ????????????
			cabinet.setCabinetType(setting.getCabinetType().name());
			// ???????????????
			cabinet.setBoardNo(0);
			switch (setting.getCabinetType()) {
			case KNIFE_CABINET_DETB:
			case KNIFE_CABINET_DETA:
			case SUB_CABINET:
				doorHeightKeyId = cabinet.getCabinetId();
				boardNoList = cabinetSetupRepository.getValueByCabinetIdAndKey(setting.getId(),
						SetupKey.Det.DET_BOARD_NO);
				break;
			case KNIFE_CABINET_C_A:
				doorHeightKeyId = cabinet.getParentCabinetId();
				boardNoList = cabinetSetupRepository.getValueByCabinetIdAndKey(setting.getId(),
						SetupKey.CCabinet.DET_BOARD_NO_A);
				break;
			case KNIFE_CABINET_C_B:
				doorHeightKeyId = cabinet.getParentCabinetId();
				boardNoList = cabinetSetupRepository.getValueByCabinetIdAndKey(setting.getId(),
						SetupKey.CCabinet.DET_BOARD_NO_B);
				break;
			default:
				break;
			}
			if (boardNoList != null && boardNoList.size() > 0) {
				cabinet.setBoardNo(Integer.valueOf(boardNoList.get(0)));
			}
			// ?????????
			cabinet.setDoorHeight(0);
			List<String> doorHeightList = this.cabinetSetupRepository.getValueByCabinetIdAndKey(doorHeightKeyId,
					SetupKey.Det.DET_DOOR_HEIGHT);
			if (doorHeightList != null && doorHeightList.size() > 0) {
				cabinet.setDoorHeight(Integer.valueOf(doorHeightList.get(0)));
			}
			cabinetList.add(cabinet);
		}
		return cabinetList;
	}

	@Override
	public List<EquSetting> getCabList() {
		List<EquSetting> settingList = this.equSettingRepository.findAll();
		return settingList.stream()
				.filter(cab -> !CabinetType.KNIFE_CABINET_C.equals(cab.getCabinetType())
						&& !CabinetType.RECOVERY_CABINET.equals(cab.getCabinetType())
						&& !CabinetType.TEM_CABINET.equals(cab.getCabinetType()))
				.collect(Collectors.toList());
	}

	@Override
	public List<EquSetting> getCabinetListByWareHouseAlias(String wareHouseAlias) {
		return this.equSettingRepository.getCabinetListByWareHouseAlias(wareHouseAlias);
	}

	@Override
	public List<ZTreeNode> treeCabDetail() {
		List<EquSetting> settingList = this.equSettingRepository.findAll();
		settingList = settingList.stream()
				.filter(setting -> UsingStatus.ENABLE.equals(setting.getStatus())
						&& !CabinetType.TEM_CABINET.equals(setting.getCabinetType())
						&& !CabinetType.RECOVERY_CABINET.equals(setting.getCabinetType()))
				.collect(Collectors.toList());
		List<ZTreeNode> tree = new ArrayList<>();
		ZTreeNode node;
		for (EquSetting setting : settingList) {
			switch (setting.getCabinetType()) {
			case KNIFE_CABINET_C: // C??????
				node = new ZTreeNode("C_" + setting.getId(), setting.getEquSettingParentId(),
						setting.getEquSettingName());
				node.setLevel(0);
				node.setIcon("/static/img/one.png");
				tree.add(node);
				break;
			case KNIFE_CABINET_PLC: // (1, "PLC?????????"),
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			case KNIFE_CABINET_DETA: // (2, "?????????A?????????"),
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			case KNIFE_CABINET_DETB: // (3, "?????????B?????????"),
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			case SUB_CABINET: // (5, "??????"),
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			case STORE_CABINET: // (7, "?????????"),
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			case VIRTUAL_WAREHOUSE: // (8, "?????????"),
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			case KNIFE_CABINET_C_A: // (10, "C??????-A???"),
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			case KNIFE_CABINET_C_B: // (11, "C??????-B???"),
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			case TROL_DRAWER: // (12, "???????????????")
				node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(), setting.getEquSettingName());
				node.setLevel(1);
				node.setIcon("/static/img/two.png");
				tree.add(node);
				break;
			default:
				break;
			}
		}
		List<EquDetail> detailList = null;
		for (EquSetting setting : settingList) {
			if (CabinetType.KNIFE_CABINET_C.equals(setting.getCabinetType())) {
				continue;
			}
			detailList = this.equDetailRepository.findAll();
			detailList = detailList.stream().filter(detail -> setting.getId().equals(detail.getEquSettingId()))
					.collect(Collectors.toList());
			for (EquDetail detail : detailList) {
				node = new ZTreeNode("D_" + detail.getId(), setting.getId(), "???" + detail.getRowNo() + "???");
				node.setLevel(2);
				node.setIcon("/static/img/three.png");
				tree.add(node);
			}
		}
		return tree;
	}
}