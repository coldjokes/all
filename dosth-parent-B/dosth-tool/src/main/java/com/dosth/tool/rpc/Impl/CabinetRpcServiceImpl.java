package com.dosth.tool.rpc.Impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cnbaosi.dto.tool.FeignCodeName;
import com.dosth.comm.LockPara;
import com.dosth.common.constant.IsReturnBack;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.criteria.EquSettingCriteria;
import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.dto.Lattice;
import com.dosth.dto.MatInfo;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.netty.dto.MsgType;
import com.dosth.netty.util.GlobalUserUtil;
import com.dosth.tool.MyPage;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.AuditStatus;
import com.dosth.tool.common.state.BackWay;
import com.dosth.tool.common.state.BorrowType;
import com.dosth.tool.common.state.EquSta;
import com.dosth.tool.common.state.ReceiveType;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.common.util.MatInfoUtil;
import com.dosth.tool.entity.CabinetSetup;
import com.dosth.tool.entity.Cart;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.MatCategory;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.MatReturnBack;
import com.dosth.tool.entity.MatReturnDetail;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.SubBoxAccountRef;
import com.dosth.tool.entity.SubCabinetBill;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.entity.mobile.PhoneOrder;
import com.dosth.tool.entity.mobile.PhoneOrderMat;
import com.dosth.tool.entity.mobile.PhoneOrderMatDetail;
import com.dosth.tool.entity.mobile.PhoneOrderSta;
import com.dosth.tool.repository.CartRepository;
import com.dosth.tool.repository.MatCategoryRepository;
import com.dosth.tool.repository.MatReturnBackRepository;
import com.dosth.tool.repository.MatReturnDetailRepository;
import com.dosth.tool.repository.MatUseBillRepository;
import com.dosth.tool.repository.PhoneOrderMatDetailRepository;
import com.dosth.tool.repository.PhoneOrderMatRepository;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.rpc.CabinetRpcService;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.CabinetLocalService;
import com.dosth.tool.service.CabinetSetupService;
import com.dosth.tool.service.CartService;
import com.dosth.tool.service.EquDetailService;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.ExtraBoxNumSettingService;
import com.dosth.tool.service.LockParamService;
import com.dosth.tool.service.MatCategoryService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.tool.service.MatReturnBackService;
import com.dosth.tool.service.MatUseBillService;
import com.dosth.tool.service.MatUseRecordService;
import com.dosth.tool.service.PhoneOrderStaService;
import com.dosth.tool.service.RestitutionTypeService;
import com.dosth.tool.service.SubBoxAccountRefService;
import com.dosth.tool.service.SubBoxService;
import com.dosth.tool.service.SubCabinetBillService;
import com.dosth.tool.service.SubCabinetDetailService;
import com.dosth.tool.service.ToolPullService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.toolcabinet.dto.BorrowGrid;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.MatDetail;
import com.dosth.toolcabinet.dto.ReBackType;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.toolcabinet.dto.SubMatDetail;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.toolcabinet.enums.EnumBorrowType;
import com.dosth.toolcabinet.enums.PrintInfoType;
import com.dosth.toolcabinet.enums.ReturnBackType;
import com.dosth.util.OpTip;

/**
 * @description 柜子远程访问接口实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CabinetRpcServiceImpl implements CabinetRpcService {

	private static final Logger logger = LoggerFactory.getLogger(CabinetRpcServiceImpl.class);

	@Autowired
	private AdminService adminService;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private EquDetailService equDetailService;
	@Autowired
	private CabinetLocalService cabinetLocalService;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private ToolPullService toolPullService;
	@Autowired
	private SubCabinetBillService subCabinetBillService;
	@Autowired
	private SubBoxAccountRefService subBoxAccountRefService;
	@Autowired
	private SubBoxService subBoxService;
	@Autowired
	private MatUseBillService matUseBillService;
	@Autowired
	private SubCabinetDetailService subCabinetDetailService;
	@Autowired
	private MatReturnBackService matReturnBackService;
	@Autowired
	private RestitutionTypeService restitutionTypeService;
	@Autowired
	private UserService userService;
	@Autowired
	private EquSettingService equSettingService;
	@Autowired
	private PhoneOrderStaService phoneOrderStaService;
	@Autowired
	private ExtraBoxNumSettingService extraBoxNumSettingService;
	@Autowired
	private MatCategoryService matCategoryService;
	@Autowired
	private PhoneOrderMatDetailRepository phoneOrderMatDetailRepository;
	@Autowired
	private MatUseBillRepository matUseBillRepository;
	@Autowired
	private PhoneOrderMatRepository phoneOrderMatRepository;
	@Autowired
	private MatReturnBackRepository matReturnBackRepository;
	@Autowired
	private MatReturnDetailRepository matReturnDetailRepository;
	@Autowired
	private MatCategoryRepository matCategoryRepository;
	@Autowired
	private MatUseRecordService matUseRecordService;
	@Autowired
	private CartService cartService;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CabinetSetupService cabinetSetupService;
	@Autowired
	private LockParamService lockParamService;
	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;

	@Override
	public Map<String, Map<String, String>> getCabinetSetupBySerialNo(String serialNo) {
		Map<String, Map<String, String>> cabinetSetupMap = new HashMap<>();
		// 添加序列号与主柜Id映射
		Map<String, String> setupMap = null;
		// 根据序列号获取柜子所有配置信息
		List<CabinetSetup> setupList = this.cabinetSetupService.getCabinetSetupBySerialNo(serialNo);
		if (setupList != null && setupList.size() > 0) {
			for (CabinetSetup list : setupList) {
				setupMap = cabinetSetupMap.get(list.getEquSettingId());
				if (setupMap == null) {
					setupMap = new HashMap<String, String>();
				}
				setupMap.put(list.getSetupKey(), list.getSetupValue());
				setupMap.put(SetupKey.Cabinet.CABINET_TYPE, list.getEquSetting().getCabinetType().toString());
				cabinetSetupMap.put(list.getEquSettingId(), setupMap);
			}
		}
		Map<String, Map<String, String>> cabinetParamMap = new HashMap<>();

		Map<String, String> mainParamMap = new HashMap<>();

		EquSetting setting;
		Map<String, String> mainSetupMap;
		for (Entry<String, Map<String, String>> entry : cabinetSetupMap.entrySet()) {
			setting = this.equSettingService.get(entry.getKey());
			setupMap = cabinetSetupMap.get(setting.getId());
			if (setupMap == null) {
				setupMap = new HashMap<>();
			}
			// 设置主柜信息
			if (setting.getEquSettingParentId() == null || "".equals(setting.getEquSettingParentId())) {
				mainParamMap.put(SetupKey.Det.DOOR, setting.getDoor().name());
				mainParamMap.put(SetupKey.Cabinet.MAIN_CABINET_ID, setting.getId());
				mainParamMap.put(SetupKey.Cabinet.CABINET_TYPE, setting.getCabinetType().name());
				// 主柜为PLC,获取储物柜串口
				if (CabinetType.KNIFE_CABINET_PLC.equals(setting.getCabinetType())) {
					setupList = this.cabinetSetupService.getStoreCabinetSetupByEquSettingId(setting.getId());
					if (setupList != null && setupList.size() > 0) {
						for (CabinetSetup setup : setupList) {
							if (setup.getSetupKey().equals(SetupKey.StoreCabinet.STORE_COM)) {
								mainParamMap.put(SetupKey.Det.DET_COM, setup.getSetupValue());
								continue;
							}
							if (setup.getSetupKey().equals(SetupKey.StoreCabinet.STORE_BAUD)) {
								mainParamMap.put(SetupKey.Det.DET_BAUD, setup.getSetupValue());
							}
						}
						List<Integer> boardNoList = this.lockParamService
								.getStoreBoardNoListByEquSettingId(setting.getId());
						setupMap.put(SetupKey.Det.DET_BOARD_NO, "0");
						// 储物柜栈号默认填0
						if (boardNoList != null && boardNoList.size() > 0) {
							setupMap.put(SetupKey.Det.DET_BOARD_NO, boardNoList.get(0).toString());
						}
					}
				}
				mainParamMap.put(SetupKey.Cabinet.CABINET_NAME, setting.getEquSettingName());
				for (Entry<String, String> main : setupMap.entrySet()) {
					mainParamMap.put(main.getKey(), main.getValue());
				}

				// 添加序列号,主柜Id映射
				setupMap = new HashMap<>();
				setupMap.put(SetupKey.Cabinet.MAIN_CABINET_ID, setting.getId());
				cabinetParamMap.put(setting.getSerialNo(), setupMap);
				continue;
			} else {
				// 其他设备表信息
				setupMap.put(SetupKey.Cabinet.MAIN_CABINET_ID,
						setting.getEquSettingParentId() == null || "".equals(setting.getEquSettingParentId())
								? setting.getId()
								: setting.getEquSettingParentId());
				setupMap.put(SetupKey.Cabinet.CABINET_TYPE, setting.getCabinetType().name());
				setupMap.put(SetupKey.Cabinet.CABINET_NAME, setting.getEquSettingName());

				switch (setting.getCabinetType()) {
				case KNIFE_CABINET_PLC:
				case KNIFE_CABINET_DETA:
				case KNIFE_CABINET_DETB:
				case SUB_CABINET:
					setupMap.put(SetupKey.Det.DOOR, setting.getDoor().name());
					break;
				case RECOVERY_CABINET: // 回收柜(将回收柜的信息设置到主柜关联)
					String pCabinetId = setting.getEquSettingParentId();
					mainSetupMap = cabinetParamMap.get(pCabinetId);
					if (mainSetupMap == null) {
						mainSetupMap = new HashMap<>();
					}
					for (Entry<String, String> re : setupMap.entrySet()) {
						switch (re.getKey()) {
						case SetupKey.RecCabinet.REC_SCAN_COM:
						case SetupKey.RecCabinet.REC_SCAN_BAUD:
						case SetupKey.RecCabinet.REC_SCAN_TYPE:
							mainParamMap.put(re.getKey(), re.getValue());
							break;
						default:
							break;
						}
					}
					cabinetParamMap.put(pCabinetId, mainSetupMap);
					break;
				case KNIFE_CABINET_C_A:
					setupMap.put(SetupKey.Det.DOOR, setting.getDoor().name());
					mainParamMap.put(SetupKey.CCabinet.DET_BOARD_NO_A_ID, setting.getId());
					mainParamMap.put(SetupKey.CCabinet.DET_BOARD_NO_A, setupMap.get(SetupKey.CCabinet.DET_BOARD_NO_A));
					List<EquDetail> staList = this.equDetailService.getEquDetailListBySettingId(setting.getId());
					staList.sort((d1, d2) -> d2.getLevelHt() - d1.getLevelHt());
					mainParamMap.put(SetupKey.Det.DET_TOP_HEIGHT, staList.get(0).getLevelHt().toString());
					break;
				case KNIFE_CABINET_C_B:
					setupMap.put(SetupKey.Det.DOOR, setting.getDoor().name());
					mainParamMap.put(SetupKey.CCabinet.DET_BOARD_NO_B_ID, setting.getId());
					mainParamMap.put(SetupKey.CCabinet.DET_BOARD_NO_B, setupMap.get(SetupKey.CCabinet.DET_BOARD_NO_B));
					break;
				case TEM_CABINET:
					// 待完善
					mainParamMap.put(SetupKey.TemCabinet.TEM_SHARE_SWITCH,
							setupMap.get(SetupKey.TemCabinet.TEM_SHARE_SWITCH));
					break;
				case STORE_CABINET:
					List<Integer> boardNoList = this.lockParamService.getBoardNoListByEquSettingId(setting.getId());
					setupMap.put(SetupKey.StoreCabinet.STORE_BOARD_NO, "0");
					// 储物柜栈号默认填0
					if (boardNoList != null && boardNoList.size() > 0) {
						setupMap.put(SetupKey.StoreCabinet.STORE_BOARD_NO, boardNoList.get(0).toString());
					}
					break;
				default:
					break;
				}
			}
			cabinetParamMap.put(setting.getId(), setupMap);
		}

		EquSetting mainEquSetting = this.equSettingService.getEquSettingBySerialNo(serialNo);
		EquSettingCriteria equSettingCriteria = new EquSettingCriteria();
		equSettingCriteria.setEquSettingParentId(mainEquSetting.getId());
		List<EquSetting> subList = this.equSettingService.findAllCabinet(equSettingCriteria);
		Map<String, String> subMap;
		if (subList != null && subList.size() > 0) {
			for (EquSetting sub : subList) {
				subMap = cabinetParamMap.get(sub.getId());
				if (subMap == null) {
					subMap = new HashMap<>();
				}
				subMap.put(SetupKey.Cabinet.MAIN_CABINET_ID,
						sub.getEquSettingParentId() == null || "".equals(sub.getEquSettingParentId()) ? sub.getId()
								: sub.getEquSettingParentId());
				subMap.put(SetupKey.Cabinet.CABINET_TYPE, sub.getCabinetType().name());
				subMap.put(SetupKey.Cabinet.CABINET_NAME, sub.getEquSettingName());
				cabinetParamMap.put(sub.getId(), subMap);
			}
		}

		cabinetParamMap.put(cabinetParamMap.get(serialNo).get(SetupKey.Cabinet.MAIN_CABINET_ID), mainParamMap);
		return cabinetParamMap;
	}

	@Override
	public List<Card> getCardList(String cabinetId) {
		List<Card> cardList = new ArrayList<>();
		Card card = null;
		MatInfo info = null;
		List<Lattice> latticeList = null;
		List<EquDetail> detailList;
		EquSetting setting = this.equSettingService.get(cabinetId);
		detailList = this.equDetailService.getEquDetailListBySettingId(cabinetId);
		// 默认按行号降序排列
		detailList.sort((a, b) -> a.getRowNo() - b.getRowNo());
		CabinetType cabinetType = setting.getCabinetType();
		// 弹簧柜按行号降序排列
		switch (cabinetType) {
		case KNIFE_CABINET_PLC:
		case KNIFE_CABINET_DETA:
		case KNIFE_CABINET_DETB:
		case SUB_CABINET:
		case KNIFE_CABINET_C_A:
		case KNIFE_CABINET_C_B:
			Collections.reverse(detailList);
			break;
		default:
			break;
		}
		List<EquDetailSta> staList;
		Lattice lattice;
		Map<MatEquInfo, Integer> tqMap = this.cabinetLocalService.getTotalQuantityGroupByMatEquInfo(cabinetId);
		for (EquDetail detail : detailList) {
			card = new Card(detail.getId(), detail.getIp(), detail.getPort(), detail.getRowNo(), detail.getLevelHt());
			if (cardList.contains(card)) {
				card = cardList.remove(cardList.indexOf(card));
				latticeList = card.getLatticeList();
			} else {
				latticeList = new ArrayList<>();
			}
			card.setDoor(detail.getEquSetting().getDoor().name());
			staList = this.equDetailStaService.getStaListByDetailId(detail.getId());

			for (EquDetailSta sta : staList) {
				if (sta.getStatus().equals(UsingStatus.DISABLE)) {
					continue;
				}
				info = MatInfoUtil.createMatInfo(sta.getMatInfo());

				lattice = new Lattice();
				if (info != null) {
//					lattice.setMatInfo(info);
					info.setIcon(this.toolProperties.getImgUrlPath() + info.getIcon());
					lattice.setRemainNum(tqMap.get(sta.getMatInfo()));
					if (CabinetType.TROL_DRAWER.equals(cabinetType)
							|| CabinetType.VIRTUAL_WAREHOUSE.equals(cabinetType)) {
						lattice.setRemainNum(sta.getCurNum());
					}
					lattice.setMatId(info.getMatId());
					lattice.setName(info.getName());
					lattice.setSpec(info.getSpec());
					lattice.setBarCode(info.getBarCode());
					lattice.setIcon(info.getIcon());
					lattice.setTypeCode(info.getTypeCode());
				}
				lattice.setStaId(sta.getId());
				lattice.setColNo(sta.getColNo());
				lattice.setWarnVal(sta.getWarnVal());
				lattice.setCurReserve(sta.getCurNum());
				lattice.setMaxReserve(sta.getMaxReserve());
				lattice.setEquSta(sta.getEquSta().name());
				lattice.setComm(sta.getComm());
				lattice.setBoardNo(sta.getBoardNo());
				lattice.setLockIndex(sta.getLockIndex());
				lattice.setBoxIndex(sta.getBoxIndex());
				lattice.setStaName(sta.getStaName());
				lattice.setInterval(sta.getInterval());
				latticeList.add(lattice);
				card.setLatticeList(latticeList);
			}
			cardList.add(card);
		}
		return cardList;
	}

	@Override
	public Map<String, Integer> getLatticeValueMap(String cabinetId) {
		return this.equDetailStaService.getLatticeValueMap(cabinetId);
	}

	@Override
	public List<MatDetail> getMatListByEqu(String cabinetId, String accountId, String equId) {
		List<MatDetail> list = new ArrayList<>();
		Map<MatEquInfo, Integer> tqMap = this.cabinetLocalService.getTotalQuantityGroupByMatEquInfo(cabinetId);
		List<MatCategory> equList = this.matCategoryService.dataFilter(accountId, equId);
		List<MatCategory> allMategory = this.matCategoryRepository.findAllNodes();
		MatEquInfo info;
		for (MatCategory equ : equList) {
			info = equ.getMatEquInfo();
			// 当前柜子没有此物料,忽略
			if (tqMap.get(info) == null) {
				continue;
			}

			String matInfoId = equ.getMatInfoId();
			// 获取所有包含该物料的类别id集合
			String categoryTreeIds = allMategory.stream().filter(item -> item.getMatInfoId().equals(matInfoId)) // 1.获取tree
					.map(MatCategory::getCategoryTreeId) // 2.获取treeId
					.distinct() // 3.去重
					.collect(Collectors.joining(",", "", "")); // 4.用,分隔并拼接成字符串

			list.add(new MatDetail(info.getId(), info.getMatEquName(), info.getBarCode(), info.getSpec(), info.getNum(),
					tqMap.get(info), info.getBorrowType().name(), this.toolProperties.getImgUrlPath() + info.getIcon(),
					equ.getCategoryTreeId(), categoryTreeIds));
		}
		return list;
	}

	@Override
	public OpTip stockCkeck(String cabinetId, List<CartInfo> cartList) {
		List<String> cartIdList = new ArrayList<>();
		List<EquDetailSta> staList = new ArrayList<>();
		OpTip tip = new OpTip(200, "校验成功");
		// 获取领取物料的购物车信息
		for (CartInfo info : cartList) {
			cartIdList.add(info.getCartId());
		}
		List<Cart> carts = this.cartRepository.getCartInfo(cartIdList);

		// 汇总领取物料总和
		Map<String, IntSummaryStatistics> cartMap = carts.stream()
				.collect(Collectors.groupingBy(Cart::getMatId, Collectors.summarizingInt(Cart::getNum)));
		// 获取所有有效库存的集合
		staList = this.equDetailStaService.getEquDetailStaTreeListByCabinetId(cabinetId);
		// 汇总物料库存总和
		Map<String, IntSummaryStatistics> staMap = staList.stream()
				.filter(sta -> sta.getMatInfoId() != null && !sta.getMatInfoId().equals("")).collect(Collectors
						.groupingBy(EquDetailSta::getMatInfoId, Collectors.summarizingInt(EquDetailSta::getCurNum)));
		// 库存校验
		for (String key : cartMap.keySet()) {
			MatEquInfo matEquInfo = this.matEquInfoService.get(key);
			long stockNum = staMap.get(key).getSum();// 库存数量
			long cartNum = cartMap.get(key).getSum();// 取出数量

			// 按支领取库存数量=包装数量*盒数量
			if (matEquInfo.getBorrowType().equals(BorrowType.METER)) {
				stockNum = stockNum * matEquInfo.getNum();
			}
			// 判断取出数量是否大于库存数量
			if (cartNum > stockNum) {
				tip = new OpTip(204, "【库存不足提醒】  " + matEquInfo.getMatEquName() + " " + matEquInfo.getSpec() + " " + matEquInfo.getBarCode() + " 剩余：" + stockNum);
			}
		}
		return tip;
	}

	@Override
	@Transactional
	public List<ExtraCabinet> sendCartToServer(String cabinetId, List<CartInfo> cartList, String shareSwitch,
			String accountId) {
		Map<ExtraCabinet, List<Card>> cartMap = new HashMap<>();
		// 获取员工信息
		UserInfo user = this.adminService.getUserInfo(accountId);
		// 获取暂存柜分配最大数量
		Integer extraBoxNum = this.extraBoxNumSettingService.getExtraBoxNumByAccountId(accountId);

		if (extraBoxNum == null) {
			extraBoxNum = 0;
		}

		List<Cart> localCartList = new ArrayList<Cart>();
		for (CartInfo info : cartList) {
			if (info.getCartId() != null) {
				localCartList.add(this.cartService.get(info.getCartId()));
			} else {
				localCartList.add(new Cart(accountId, info.getMatId(), info.getReceiveType(), info.getReceiveInfo(),
						info.getNum(), cabinetId));
			}
		}

		MatEquInfo info;
		BorrowType borrowType;
		List<EquDetailSta> staList;
		Integer pullNum; // 领取数量
		Integer subNum;
		String receiveType;
		String receiveInfo;
		EquSetting setting = this.equSettingService.get(cabinetId);
		Map<String, Integer> map = this.subCabinetDetailService.getTotalQuantityGroupByMatId(shareSwitch, accountId);
		// 弹簧位置对应已取数量
		Map<String, Integer> staMap = new HashMap<>();
		// 弹簧已取数量
		Integer staNum;
		// 获取当前柜子存储详情集合
		List<EquDetailSta> detailList = this.equDetailStaService.getEquDetailStaTreeListByCabinetId(cabinetId);
		// 物料->柜子存储详情分组
		Map<String, List<EquDetailSta>> detailStaMap = detailList.stream()
				.filter(sta -> sta.getMatInfoId() != null && !"".equals(sta.getMatInfoId())
						&& EquSta.NONE.equals(sta.getEquSta()) && UsingStatus.ENABLE.equals(sta.getStatus())
						&& sta.getCurNum() > 0)
				.collect(Collectors.groupingBy(EquDetailSta::getMatInfoId));

		// 遍历购物车信息
		for (Cart cart : localCartList) {
			subNum = 0;
			// 当前物料需领取数量
			pullNum = cart.getNum();
			// 取料数量小于1忽略
			if (pullNum < 1) {
				continue;
			}
			receiveType = cart.getReceiveType();
			receiveInfo = cart.getReceiveInfo();
			// 直接领取,领取类型赋空
			if (receiveInfo != null && ("".equals(receiveInfo) || EnumBorrowType.GRID.name().equals(receiveInfo)
					|| EnumBorrowType.GRID.getDesc().equals(receiveInfo))) {
				receiveInfo = null;
			}
			// 获取当前物料信息
			if (cart.getMatInfo() != null) {
				info = cart.getMatInfo();
			} else {
				info = this.matEquInfoService.get(cart.getMatId());
			}
			borrowType = info.getBorrowType();
			// 统计物料数量
			int curNum = pullNum;
			// 如果是按计量单位领取的重新计算数量,如果按包装计算,则忽略包装数量、剩余数量
			if (BorrowType.METER.equals(info.getBorrowType())) {
				// 如果按包装取数,则按单只计算数量
				if (BorrowType.PACK.equals(borrowType)) {
					pullNum *= info.getNum();
				}
				// 获取当前物料暂存柜存量
				subNum = map.get(cart.getMatId()) == null ? 0 : map.get(cart.getMatId());
				// 存量大于取量,直接通过暂存柜数量扣除
				if (subNum >= pullNum) {
					continue;
				} else { // 暂存数量不足
					// 重新计算需取出数量
					curNum = (pullNum - subNum) % info.getNum() == 0 ? (pullNum - subNum) / info.getNum()
							: (pullNum - subNum) / info.getNum() + 1;
				}
			}

			MatUseRecord record = new MatUseRecord(info.getId(), null, info.getMatEquName(), info.getBarCode(),
					info.getSpec(),
					info.getBrand() + "-"
							+ (info.getManufacturer() != null ? info.getManufacturer().getManufacturerName() : ""),
					BorrowType.METER.equals(info.getBorrowType()) ? curNum * info.getNum() : curNum,
					info.getBorrowType().getMessage(), info.getNum(), info.getPackUnit(), receiveInfo,
					ReceiveType.valueOf(receiveType), info.getStorePrice(), 0F, user.getAccountId(), user.getUserName(),
					user.getDeptName(), 0, IsReturnBack.NOTBACK.getMessage());
			record.setDeptId(user.getDeptId());
			// 物料使用生命周期
			record.setUseLife(info.getUseLife() * record.getBorrowNum());
			if (BorrowType.METER.equals(info.getBorrowType())) {
				record.setUseLife(info.getUseLife() * info.getNum());
			}
			this.matUseRecordService.save(record);

			// 统计物料数量
			int result = 0;
			// 如果是按计量单位领取的重新计算数量,如果按包装计算,则忽略包装数量、剩余数量
			if (BorrowType.METER.equals(info.getBorrowType())) {
				// 副柜需回存剩余数量(需取出数量*包装数量-取出数量)
				result = curNum * info.getNum() - pullNum;
				if (BorrowType.PACK.equals(borrowType)) { // 片装按包取
					result = curNum * info.getNum() - pullNum;
				}
				if (result > 0) { // 有剩余数量时
					String msg = "";
					// 获取正在用的暂存柜
					List<SubBox> subList = this.subBoxAccountRefService.getUsingSubBoxList(accountId);
					// 库位可用余量
					int extraNum = 0;
					if (subList != null && subList.size() > 0) {
						if (subList.size() == extraBoxNum) {
							for (SubBox box : subList) {
								extraNum += box.getExtraNum();
							}
							if (extraNum > 0) {
								msg = "请将多余物料存入暂存柜！";
							} else {
								msg = "你使用的暂存柜数量已达上限!";
							}
						} else if (subList.size() < extraBoxNum) {
							msg = "请将多余物料存入暂存柜！";
						} else {
							msg = "你使用的暂存柜数量已达上限!";
						}
					} else {
						msg = "请将多余物料存入暂存柜！";
					}
					GlobalUserUtil.writeMsg(setting.getId(), MsgType.SUBALERTINFO, msg);
				} else if (result != 0) {
					String msg = "剩余物料请在暂存柜中领取！";
					GlobalUserUtil.writeMsg(setting.getId(), MsgType.SUBALERTINFO, msg);
				}
			}

			// 获取当前物料存储详情
			// 按照批次号先进先出原则排序
			staList = detailStaMap.get(cart.getMatId());
			if (staList == null || staList.size() < 1) {
				continue;
			}
			Collections.sort(staList, new Comparator<EquDetailSta>() {
				@Override
				public int compare(EquDetailSta o1, EquDetailSta o2) {
					return o1.getLastFeedTime().compareTo(o2.getLastFeedTime());
				}
			});

			String borrowOrigin = "";// 取货来源初始化
			CabinetType cabinetType = null;
			for (EquDetailSta sta : staList) {
				if (curNum < 1) {
					break;
				}

				Boolean isSyn = false; // 是否同步数据
				cabinetType = sta.getEquDetail().getEquSetting().getCabinetType();
				if (cabinetType.equals(CabinetType.SUB_CABINET)) {
					cabinetType = sta.getEquDetail().getEquSetting().getEquSettingParent().getCabinetType();
				}
				switch (cabinetType) {
				case KNIFE_CABINET_C:
				case KNIFE_CABINET_C_A:
				case KNIFE_CABINET_C_B:
				case KNIFE_CABINET_DETA:
				case KNIFE_CABINET_DETB:
					isSyn = true;
					break;
				default:
					break;
				}
				// 异步库存数量
				if (isSyn != null && !isSyn) {
					// 工作空间存量大于提取数量
					if (sta.getCurNum() >= curNum) {
						this.procCartMap(cartMap, sta, record.getId(), curNum);
						this.toolPullService.toolPullBill(record, sta, curNum, accountId, receiveType, receiveInfo,
								false);
						borrowOrigin += getBorrowOrigin(sta, curNum);
						break;
					} else {
						curNum -= sta.getCurNum();
						// 空间存量不足则取全部存量
						this.procCartMap(cartMap, sta, record.getId(), sta.getCurNum());
						this.toolPullService.toolPullBill(record, sta, sta.getCurNum(), accountId, receiveType,
								receiveInfo, false);
						borrowOrigin += getBorrowOrigin(sta, sta.getCurNum());
					}
				} else { // 同步库存数量
					staNum = staMap.get(sta.getId()) == null ? 0 : staMap.get(sta.getId());
					if (staNum.intValue() >= sta.getCurNum().intValue()) {
						continue;
					}
					// 工作空间存量大于提取数量
					if (sta.getCurNum() - staNum >= curNum) {
						this.procCartMap(cartMap, sta, record.getId(), curNum);
						staMap.put(sta.getId(), staNum + curNum);
						borrowOrigin += getBorrowOrigin(sta, curNum);
						break;
					} else {
						int sy = sta.getCurNum() - staNum;
						if (sy > 0) { // 还有剩
							curNum -= sy; // 还要继续取的数量
							// 空间存量不足则取全部存量
							this.procCartMap(cartMap, sta, record.getId(), sy);
							staMap.put(sta.getId(), staNum + sy);
							borrowOrigin += getBorrowOrigin(sta, sy);
						}
					}
				}
			}
			record.setBorrowOrigin(borrowOrigin);
			this.matUseRecordService.update(record);
			// 推送领用记录到前台
			GlobalUserUtil.writeMsg(setting.getId(), MsgType.BORROWPOST, record.getId());
		}

		List<ExtraCabinet> cabinetList = new ArrayList<>();
		ExtraCabinet cabinet;
		for (Entry<ExtraCabinet, List<Card>> entry : cartMap.entrySet()) {
			cabinet = entry.getKey();
			cabinet.setCardList(entry.getValue());
			cabinetList.add(cabinet);
		}
		return cabinetList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public OpTip borrowByGrid(BorrowGrid borrowGrid, String accountId, String receiveType, String receiveInfo) {
		// 直接领取,领取类型赋空
		receiveInfo = null;
		// 获取员工信息
		UserInfo user = this.adminService.getUserInfo(accountId);
		// 异步判断FLAG
		Boolean isSyncStockNum = false;
		try {
			MatEquInfo info = this.matEquInfoService.get(borrowGrid.getMatInfoId());
			EquDetailSta sta = this.equDetailStaService.get(borrowGrid.getEquDetailStaId());
			String cabinetType = sta.getEquDetail().getEquSetting().getCabinetType().name();
			// 副柜的的场合，刀具柜类型继承父柜的类型
			if (cabinetType.equals(CabinetType.SUB_CABINET.name())) {
				String parentId = sta.getEquDetail().getEquSetting().getEquSettingParentId();
				EquSetting setting = equSettingService.get(parentId);
				cabinetType = setting.getCabinetType().name();
			}
			// 行列式刀具柜异步结算流水
			if (cabinetType.equals(CabinetType.KNIFE_CABINET_DETB.name())
					|| cabinetType.equals(CabinetType.KNIFE_CABINET_DETA.name())
					|| cabinetType.equals(CabinetType.KNIFE_CABINET_C_A.name())
					|| cabinetType.equals(CabinetType.KNIFE_CABINET_C_B.name())
					|| cabinetType.equals(CabinetType.TROL_DRAWER.name())) {
				isSyncStockNum = true;
			}
			MatUseRecord record = new MatUseRecord(info.getId(), getBorrowOrigin(sta, borrowGrid.getBorrowNum()),
					info.getMatEquName(), info.getBarCode(), info.getSpec(),
					info.getBrand() + "-" + info.getManufacturer().getManufacturerName(),
					info.getBorrowType().equals(BorrowType.METER) ? borrowGrid.getBorrowNum() * info.getNum()
							: borrowGrid.getBorrowNum(),
					info.getBorrowType().getMessage(), info.getNum(), info.getPackUnit(), receiveInfo,
					ReceiveType.valueOf(receiveType), info.getStorePrice(), 0F, user.getAccountId(), user.getUserName(),
					user.getDeptName(), 0, IsReturnBack.NOTBACK.getMessage());
			record.setDeptId(user.getDeptId());
			this.matUseRecordService.save(record);
			// 库存数量异步或非弹簧柜记录领用流水
			if (!isSyncStockNum) {
				this.toolPullService.toolPullBill(record, this.equDetailStaService.get(borrowGrid.getEquDetailStaId()),
						borrowGrid.getBorrowNum(), accountId, receiveType, receiveInfo, false);
			}
			// 推送领用记录到前台
			GlobalUserUtil.writeMsg(sta.getEquDetail().getEquSettingId(), MsgType.BORROWPOST, record.getId());
			return new OpTip(record.getId());
		} catch (Exception e) {
			logger.error("栅栏借出物料异常" + e.getMessage());
			return new OpTip();
		}
	}

	/**
	 * @description 设置柜子槽位数量处理
	 * @param cardMap  货柜-马达板列表分组
	 * @param sta      货道弹簧Id
	 * @param recordId 借出记录Id
	 * @param pullNum  取出数量
	 */
	private void procCartMap(Map<ExtraCabinet, List<Card>> cardMap, EquDetailSta sta, String recordId, int pullNum) {
		EquDetail detail = sta.getEquDetail();
		EquSetting setting = sta.getEquDetail().getEquSetting();
		ExtraCabinet cabinet = new ExtraCabinet();
		cabinet.setCabinetId(setting.getId());
		cabinet.setParentCabinetId(setting.getEquSettingParentId());
		cabinet.setCabinetName(setting.getEquSettingName());
		// 主控板栈号
		String mainBoardNo;
		// 门高
		String doorHeightKeyId = null;
		switch (setting.getCabinetType()) {
		case KNIFE_CABINET_C_A:
		case KNIFE_CABINET_C_B:
			doorHeightKeyId = setting.getEquSettingParentId();
			mainBoardNo = this.cabinetSetupService.getValueByCabinetIdAndKey(setting.getEquSettingParentId(),
					SetupKey.CCabinet.DET_BOARD_NO_A);
			break;
		default:
			doorHeightKeyId = cabinet.getCabinetId();
			mainBoardNo = this.cabinetSetupService.getValueByCabinetIdAndKey(setting.getId(),
					SetupKey.Det.DET_BOARD_NO);
			break;
		}
		cabinet.setBoardNo(0);
		if (mainBoardNo != null && !"".equals(mainBoardNo)) {
			cabinet.setBoardNo(Integer.valueOf(mainBoardNo));
		}
		// 门高度
		cabinet.setDoorHeight(0);
		String doorHeight = this.cabinetSetupService.getValueByCabinetIdAndKey(doorHeightKeyId,
				SetupKey.Det.DET_DOOR_HEIGHT);
		if (doorHeight != null && !"".equals(doorHeight)) {
			cabinet.setDoorHeight(Integer.valueOf(doorHeight));
		}
		cabinet.setDoor(setting.getDoor().name());
		cabinet.setCabinetType(setting.getCabinetType().name());
		// 副柜类型与主柜的类型相同
		if (setting.getCabinetType().equals(CabinetType.SUB_CABINET)) {
			cabinet.setCabinetType(setting.getEquSettingParent().getCabinetType().name());
		}
		List<Card> cardList = cardMap.get(cabinet);
		if (cardList == null) {
			cardList = new ArrayList<Card>();
		}
		Card card = new Card(detail.getId(), detail.getIp(), detail.getPort(), detail.getRowNo(), detail.getLevelHt());
		card.setDoor(setting.getDoor().name());
		if (cardList.contains(card)) {
			card = cardList.remove(cardList.indexOf(card));
		}
		List<Lattice> latticeList = card.getLatticeList();

		Lattice lattice = new Lattice();
		lattice.setStaId(sta.getId());
		// 验证当前格子需要获取数量是否存在
		if (latticeList.contains(lattice)) {
			lattice = latticeList.remove(latticeList.indexOf(lattice));
		}
		lattice.getRecordMap().put(recordId, pullNum);
		lattice.setColNo(sta.getColNo());
		// 数量和原始数量叠加
		lattice.setCurReserve(lattice.getCurReserve() + pullNum);
		lattice.setLevelHeight(detail.getLevelHt());
		lattice.setComm(sta.getComm());
		// 当前柜体部分栈号
		String boardNo = null;
		// 如槽位未设置栈号,则按柜体栈号设置
		switch (setting.getCabinetType()) {
		case KNIFE_CABINET_DETB:
		case KNIFE_CABINET_DETA:
			boardNo = this.cabinetSetupService.getValueByCabinetIdAndKey(setting.getId(), SetupKey.Det.DET_BOARD_NO);
			break;
		case KNIFE_CABINET_C_A:
			boardNo = this.cabinetSetupService.getValueByCabinetIdAndKey(setting.getId(),
					SetupKey.CCabinet.DET_BOARD_NO_A);
			break;
		case KNIFE_CABINET_C_B:
			boardNo = this.cabinetSetupService.getValueByCabinetIdAndKey(setting.getId(),
					SetupKey.CCabinet.DET_BOARD_NO_B);
			break;
		case STORE_CABINET: // 储物柜按sta栈号对应
			boardNo = sta.getBoardNo() == null ? "0" : sta.getBoardNo().toString();
			break;
		default:
			break;
		}
		lattice.setBoardNo(0);
		if (boardNo != null && !"".equals(boardNo)) {
			lattice.setBoardNo(Integer.valueOf(boardNo));
		}
		lattice.setLockIndex(sta.getLockIndex());
		lattice.setBoxIndex(sta.getBoxIndex());
		card.getLatticeList().add(lattice);

		cardList.add(card);

		cardMap.put(cabinet, cardList);
	}

	@Override
	public List<SubMatDetail> getUnReturnList(String accountId) {
		List<SubMatDetail> list = new ArrayList<>();
		List<MatUseBill> billList = this.matUseBillService.getUnReturnList(accountId);
		billList.forEach(bill -> {
			list.add(new SubMatDetail(
					bill.getEquDetailSta() != null ? bill.getEquDetailSta().getEquDetail().getEquSetting().getSerialNo()
							: "",
					bill.getMatInfoId() != null ? bill.getMatInfoId() : "",
					bill.getMatEquName() != null ? bill.getMatEquName() : "",
					bill.getMatUseRecord().getMatInfo() != null ? bill.getMatUseRecord().getMatInfo().getNum() : 0, 1,
					this.toolProperties.getImgUrlPath() + bill.getMatUseRecord().getMatInfo().getIcon(),
					bill.getMatUseRecord().getMatInfo() != null
							? bill.getMatUseRecord().getMatInfo().getBorrowType().getMessage()
							: "",
					bill.getMatUseRecord().getMatInfo() != null
							? bill.getMatUseRecord().getMatInfo().getOldForNew().name()
							: "",
					bill.getId(), bill.getOpDate(), bill.getBarCode(), bill.getSpec(),
					bill.getMatUseRecord().getReceiveType().name(), bill.getMatUseRecord().getReceiveInfo(),
					bill.getMatUseRecord().getMatInfo().getUseLife()));
		});
		return list;
	}

	@Override
	public List<FeignCodeName> getUnReturnTypeList(String accountId, String cabinetId) {
		// 获取未归还物料类型
		return this.matUseBillService.getUnReturnTypeList(accountId, cabinetId);
	}

	@Override
	public Map<String, List<SubMatDetail>> getUnReturnList(String accountId, String serialNo) {
		List<SubMatDetail> list = new ArrayList<>();
		// 根据帐号查询所有取料记录
		List<MatUseBill> billList = this.matUseBillService.getUnReturnList(accountId);
		// 根据序列号查询所有关联柜子
		List<ExtraCabinet> extraCabinetList = this.equSettingService.getCabinetList(serialNo);
		// 所有关联柜子id
		List<String> settingIdList = new ArrayList<String>();
		for (ExtraCabinet equSetting : extraCabinetList) {
			settingIdList.add(equSetting.getCabinetId());
		}

		// 查询所有公用虚拟柜主柜
		EquSettingCriteria equSettingCriteria = new EquSettingCriteria();
		equSettingCriteria.setCabinetType(CabinetType.VIRTUAL_WAREHOUSE);
		List<EquSetting> equSettingList = this.equSettingService.findAllCabinet(equSettingCriteria).stream()
				.filter(equSetting -> equSetting.getEquSettingParentId() == null
						|| "".equals(equSetting.getEquSettingParentId()))
				.collect(Collectors.toList());
		// 添加所有公用虚拟主柜id
		for (EquSetting equSetting : equSettingList) {
			settingIdList.add(equSetting.getId());
		}

		// 所有取料记录按所有刀具柜id过滤
		billList = billList.stream()
				.filter(bill -> settingIdList.contains(bill.getEquDetailSta().getEquDetail().getEquSettingId()))
				.collect(Collectors.toList());

		billList.stream().filter(bill -> bill.getMatUseRecord().getReceiveInfo() != null).forEach(bill -> {
			list.add(new SubMatDetail(
					bill.getEquDetailSta() != null ? bill.getEquDetailSta().getEquDetail().getEquSetting().getSerialNo()
							: "",
					bill.getMatInfoId(), bill.getMatEquName(),
					bill.getMatUseRecord().getMatInfo() != null ? bill.getMatUseRecord().getMatInfo().getNum() : 0, 1,
					this.toolProperties.getImgUrlPath() + bill.getMatUseRecord().getMatInfo().getIcon(),
					bill.getMatUseRecord().getMatInfo() != null
							? bill.getMatUseRecord().getMatInfo().getBorrowType().getMessage()
							: "",
					bill.getMatUseRecord().getMatInfo() != null
							? bill.getMatUseRecord().getMatInfo().getOldForNew().name()
							: "",
					bill.getId(), bill.getOpDate(), bill.getBarCode(), bill.getSpec(),
					bill.getMatUseRecord().getReceiveType().name(), bill.getMatUseRecord().getReceiveInfo(),
					bill.getMatUseRecord().getMatInfo().getUseLife()));
		});
		Map<String, List<SubMatDetail>> map = list.stream()
				.collect(Collectors.groupingBy(SubMatDetail::getReceiveInfo));

		return map;
	}

	@Override
	public MyPage<SubMatDetail> getBackNormalList(String accountId, String serialNo, int pageNo) {
		List<SubMatDetail> list = new ArrayList<>();
		// 根据帐号查询所有取料记录
		List<MatUseBill> billList = this.matUseBillService.getUnReturnList(accountId);
		// 根据序列号查询所有关联柜子
		List<ExtraCabinet> extraCabinetList = this.equSettingService.getCabinetList(serialNo);
		// 所有关联柜子id
		List<String> settingIdList = new ArrayList<String>();
		for (ExtraCabinet equSetting : extraCabinetList) {
			settingIdList.add(equSetting.getCabinetId());
		}

		// 查询所有公用虚拟柜主柜
		EquSettingCriteria equSettingCriteria = new EquSettingCriteria();
		equSettingCriteria.setCabinetType(CabinetType.VIRTUAL_WAREHOUSE);
		List<EquSetting> equSettingList = this.equSettingService.findAllCabinet(equSettingCriteria).stream()
				.filter(equSetting -> equSetting.getEquSettingParentId() == null
						|| "".equals(equSetting.getEquSettingParentId()))
				.collect(Collectors.toList());
		// 添加所有公用虚拟主柜id
		for (EquSetting equSetting : equSettingList) {
			settingIdList.add(equSetting.getId());
		}

		// 所有取料记录按所有刀具柜id过滤
		billList = billList.stream()
				.filter(bill -> settingIdList.contains(bill.getEquDetailSta().getEquDetail().getEquSettingId()))
				.collect(Collectors.toList());

		billList.forEach(bill -> {
			list.add(new SubMatDetail(
					bill.getEquDetailSta() != null ? bill.getEquDetailSta().getEquDetail().getEquSetting().getSerialNo()
							: "",
					bill.getMatInfoId(), bill.getMatEquName(),
					bill.getMatUseRecord().getMatInfo() != null ? bill.getMatUseRecord().getMatInfo().getNum() : 0, 1,
					this.toolProperties.getImgUrlPath() + bill.getMatUseRecord().getMatInfo().getIcon(),
					bill.getMatUseRecord().getMatInfo() != null
							? bill.getMatUseRecord().getMatInfo().getBorrowType().getMessage()
							: "",
					bill.getMatUseRecord().getMatInfo() != null
							? bill.getMatUseRecord().getMatInfo().getOldForNew().name()
							: "",
					bill.getId(), bill.getOpDate(), bill.getBarCode(), bill.getSpec(),
					bill.getMatUseRecord().getReceiveType().name(),
					bill.getMatUseRecord().getReceiveInfo() == null ? "全部" : bill.getMatUseRecord().getTree().getName(),
					bill.getMatUseRecord().getMatInfo().getUseLife()));
		});

		int pageSize = 10;
		Pageable pageable = new PageRequest(pageNo - 1, pageSize);
		Page<SubMatDetail> page = ListUtil.listConvertToPage(list, pageable);

		MyPage<SubMatDetail> myPage = new MyPage<>(page);

		return myPage;
	}

	@Override
	public List<SubMatDetail> getBackBatchList(String accountId, String serialNo) {
		List<SubMatDetail> list = new ArrayList<>();
		// 根据帐号查询所有取料记录
		List<MatUseBill> billList = this.matUseBillService.getUnReturnList(accountId);
		// 根据序列号查询所有关联柜子
		List<ExtraCabinet> extraCabinetList = this.equSettingService.getCabinetList(serialNo);
		// 所有关联柜子id
		List<String> settingIdList = new ArrayList<String>();
		for (ExtraCabinet equSetting : extraCabinetList) {
			settingIdList.add(equSetting.getCabinetId());
		}

		// 查询所有公用虚拟柜主柜
		EquSettingCriteria equSettingCriteria = new EquSettingCriteria();
		equSettingCriteria.setCabinetType(CabinetType.VIRTUAL_WAREHOUSE);
		List<EquSetting> equSettingList = this.equSettingService.findAllCabinet(equSettingCriteria).stream()
				.filter(equSetting -> equSetting.getEquSettingParentId() == null
						|| "".equals(equSetting.getEquSettingParentId()))
				.collect(Collectors.toList());
		// 添加所有公用虚拟主柜id
		for (EquSetting equSetting : equSettingList) {
			settingIdList.add(equSetting.getId());
		}

		// 所有取料记录按所有刀具柜id过滤
		billList = billList.stream()
				.filter(bill -> settingIdList.contains(bill.getEquDetailSta().getEquDetail().getEquSettingId()))
				.collect(Collectors.toList());

		Map<String, List<MatUseBill>> collect = billList.stream()
				.collect(Collectors.groupingBy(MatUseBill::getMatInfoId));

		for (Entry<String, List<MatUseBill>> billMap : collect.entrySet()) {
			List<MatUseBill> borrowList = billMap.getValue();
			int borrowTotal = borrowList.stream().mapToInt(MatUseBill::getBorrowNum).sum();

			MatEquInfo matInfo = borrowList.get(0).getMatUseRecord().getMatInfo();
			list.add(new SubMatDetail(
					borrowList.get(0).getEquDetailSta() != null
							? borrowList.get(0).getEquDetailSta().getEquDetail().getEquSetting().getSerialNo()
							: "",
					billMap.getKey(), matInfo != null ? matInfo.getMatEquName() : "",
					matInfo != null ? matInfo.getNum() : 0, borrowTotal,
					this.toolProperties.getImgUrlPath() + matInfo.getIcon(),
					matInfo != null ? matInfo.getBorrowType().getMessage() : "",
					matInfo != null ? matInfo.getOldForNew().name() : "", "", null,
					matInfo != null ? matInfo.getBarCode() : "", matInfo != null ? matInfo.getSpec() : "", "", "",
					matInfo.getUseLife()));
		}
		return list;
	}

	@Override
	@Transactional
	public void tempStor(String matUseBillId, int borrowNum, String accountId) {
		// 获取员工信息
		UserInfo user = this.adminService.getUserInfo(accountId);

		// 获取暂存柜分配最大数量
		Integer extraBoxNum = this.extraBoxNumSettingService.getExtraBoxNumByAccountId(accountId);
		if (extraBoxNum == null) {
			extraBoxNum = 0;
		}

		MatUseBill bill = this.matUseBillService.get(matUseBillId);
		List<SubBox> boxList = this.subBoxAccountRefService.getUsingSubBoxList(accountId);

		// 获取物料信息
		MatEquInfo info = this.matEquInfoService.get(bill.getMatInfoId());

		SubBox subBox;
		if (boxList != null && boxList.size() > 0) {// 有正在使用副柜格子
			List<SubCabinetDetail> detailList = this.subCabinetDetailService
					.getSubDetailListByAccountIdAndMatId(accountId, bill.getMatInfoId());
			if (detailList != null && detailList.size() > 0) { // 副柜详情已经存在相关信息
				SubCabinetDetail detail = detailList.get(0);
				detail.setNum(detail.getNum() + borrowNum);
				this.subCabinetDetailService.update(detail);
			} else { // 不存在相关信息,新增一条关联信息
				if (boxList != null && boxList.size() >= extraBoxNum) { // 暂存柜数量已达上限数
					String msg = "你使用的暂存柜数量已达上限";
					logger.info(msg);
					GlobalUserUtil.writeMsg(bill.getEquDetailSta().getEquDetail().getEquSettingId(), MsgType.TXT, msg);
				} else { // 暂存柜数量未达上限数,直接分配
					boxList = this.subBoxService.getUnUsedSubBoxList();
					if (boxList != null && boxList.size() > 0) {
						subBox = boxList.get(0);
						this.subBoxAccountRefService.save(new SubBoxAccountRef(accountId, subBox.getId()));
						this.subCabinetDetailService
								.save(new SubCabinetDetail(subBox.getId(), bill.getMatInfoId(), borrowNum));
						float subMoney = (float) (Math.round(info.getStorePrice() * borrowNum * 10000)) / 10000;
						// 添加暂存柜领取信息
						this.subCabinetBillService.save(new SubCabinetBill(subBox.getId(),
								subBox.getEquSetting().getEquSettingName(), info.getMatEquName(), info.getBarCode(),
								info.getSpec(), borrowNum, info.getBorrowType().getMessage(), info.getStorePrice(),
								subMoney, user.getAccountId(), user.getUserName(), YesOrNo.YES, info.getId()));
					} else {
						String msg = "没有空闲副柜格子了";
						logger.info(msg);
						GlobalUserUtil.writeMsg(bill.getEquDetailSta().getEquDetail().getEquSettingId(), MsgType.TXT,
								msg);
					}
				}
			}
		} else { // 没有正在使用的副柜盒子,则分配一个未使用的盒子
			boxList = this.subBoxService.getUnUsedSubBoxList();
			if (boxList != null && boxList.size() > 0) {
				subBox = boxList.get(0);
				this.subBoxAccountRefService.save(new SubBoxAccountRef(accountId, subBox.getId()));
				this.subCabinetDetailService.save(new SubCabinetDetail(subBox.getId(), bill.getMatInfoId(), borrowNum));
			} else {
				logger.info("没有空闲副柜格子了");
			}
		}
	}

	@Override
	public Map<String, List<ReBackType>> getReBackTypeMap() {
		Map<String, List<ReBackType>> map = new HashMap<>();
		List<ReBackType> list;
		List<RestitutionType> typeList = this.restitutionTypeService.getReTypeList();
		for (RestitutionType type : typeList) {
			if (UsingStatus.DISABLE.equals(type.getStatus())) {
				continue;
			}
			list = map.get(type.getReturnBackType().getDesc());
			if (list == null) {
				list = new ArrayList<>();
			}
			list.add(new ReBackType(type.getId(), type.getRestName()));
			map.put(type.getReturnBackType().getDesc(), list);
		}
		return map;
	}

	@Override
	public void returnBackSingle(String returnBackNo) throws Exception {
		try {
			int backNo = Integer.parseInt(returnBackNo);
			List<MatReturnBack> backList = this.matReturnBackRepository.findByReturnNo(backNo);
			if (backList != null && backList.size() > 0) {
				for (MatReturnBack matReturnBack : backList) {
					matReturnBack.setIsReturnBack(IsReturnBack.ISBACK);
					matReturnBack.setOpDate(new Date());
					this.matReturnBackService.update(matReturnBack);

					MatUseBill matUseBill = matReturnBack.getMatUseBill();
					if (matUseBill != null) {
						MatUseRecord matUseRecord = this.matUseRecordService.get(matUseBill.getMatUseRecord().getId());
						if (matUseRecord != null) {
							int checkNum = matUseBill.getBorrowNum();
							// if(matUseBill.getMatUseRecord().getMatInfo().getBorrowType().equals(BorrowType.METER))
							// {
							// checkNum = matUseBill.getBorrowNum() *
							// matUseBill.getMatUseRecord().getMatInfo().getNum();
							// }
							matUseRecord.setReturnBackNum(
									(matUseRecord.getReturnBackNum() != null ? matUseRecord.getReturnBackNum() : 0)
											+ checkNum);
							if (matUseRecord.getBorrowNum() == matUseRecord.getReturnBackNum()) {
								matUseRecord.setIsReturnBack(IsReturnBack.ISBACK.getMessage());
							} else {
								matUseRecord.setIsReturnBack(IsReturnBack.NOTBACK.getMessage());
							}
							this.matUseRecordService.update(matUseRecord);
						}
					}
				}
			} else {
				logger.error("归还码未找到匹配记录:" + returnBackNo);
				throw new Exception("归还码未找到匹配记录");
			}
		} catch (Exception e) {
			logger.error("非法归还码:" + returnBackNo);
			throw new Exception("非法归还码");
		}
	}

	@Override
	public OpTip findBybarcode(String barCode) {
		// 条形码是否在使用中
		MatReturnBack info = this.matReturnBackRepository.checkbarCode(barCode);
		if (info != null) {
			return new OpTip(202, "条形码在使用中...");
		}
		return new OpTip("归还成功");
	}

	@Override
	public List<SubMatDetail> getSubCabinetRpcList(String shareSwitch, String accountId) {
		List<SubMatDetail> matDetailList = new ArrayList<>();
		List<SubCabinetDetail> detailInfo = new ArrayList<>();

		// 暂存柜共享开关
		if (TrueOrFalse.TRUE.toString().equals(shareSwitch)) {
			detailInfo = this.subCabinetDetailService.getSubCabinetDetailList();
		} else {
			detailInfo = this.subCabinetDetailService.findByAccountId(accountId);
		}

		List<SubCabinetBill> subCabinetBillList;
		Date borrowTime = null;

		for (SubCabinetDetail detail : detailInfo) {
			subCabinetBillList = this.subCabinetBillService.getLastInTime(detail.getSubBox().getId(),
					detail.getMatInfoId(), accountId);
			borrowTime = subCabinetBillList != null && subCabinetBillList.size() > 0
					? subCabinetBillList.get(0).getOpDate()
					: new Date();
			matDetailList.add(
					new SubMatDetail(detail.getSubBoxId(), detail.getMatInfoId(), detail.getMatInfo().getMatEquName(),
							detail.getMatInfo().getBarCode(), detail.getMatInfo().getSpec(),
							detail.getMatInfo().getNum(), detail.getNum(), detail.getMatInfo().getBorrowType().name(),
							detail.getMatInfo().getBorrowType().getMessage(), detail.getMatInfo().getOldForNew().name(),
							FileUtil.convertImageToBase64Data(
									new File(this.toolProperties.getUploadPath() + detail.getMatInfo().getIcon())),
							borrowTime, detail.getSubBox().getEquSettingId(), this.createSubBoxName(detail.getSubBox()),
							new LockPara(detail.getSubBox().getBoxIndex(), detail.getSubBox().getBoardNo(),
									detail.getSubBox().getLockIndex())));
		}
		return matDetailList;
	}

	/**
	 * @description 创建副柜名称
	 * @param subBox
	 * @return
	 */
	private String createSubBoxName(SubBox subBox) {
		return new StringBuffer(subBox.getEquSetting().getEquSettingName()).append("[")
				.append(String.format("%03d", subBox.getBoxIndex())).append("]").toString();
	}

	@Override
	@Transactional
	public void opSubBox(String subBoxId, String matInfoId, Integer num, String accountId) {
		// 获取物料信息
		MatEquInfo info = this.matEquInfoService.get(matInfoId);
		// 获取员工信息
		UserInfo user = this.adminService.getUserInfo(accountId);

		List<SubCabinetDetail> detailList;
		SubCabinetDetail detail;
		detailList = this.subCabinetDetailService.getSubDetailList(subBoxId, matInfoId);
		if (detailList != null && detailList.size() > 0) {
			detail = detailList.get(0);
			logger.info("~~~~~~~~~~~~~~~~~~~~~~~" + detail.getNum());
			if (detail.getNum() < num) { // 数量不足
				logger.error("当前物料数量不足,请核对后再试!");
			} else {
				detail.setNum(detail.getNum() - num);
				if (detail.getNum() > 0) {
					this.subCabinetDetailService.update(detail);// 记录副柜流水
				} else { // 剩余数量为0
					// 抽屉可暂存种类位置增加 1
					SubBox box = detail.getSubBox();
					box.setExtraNum(box.getExtraNum() + 1);
					this.subBoxService.update(box);
					// 清空暂存柜明细,清除暂存柜帐户关联关系
					this.subCabinetDetailService.delete(detail);
					// 抽屉存放一种或小于一种物料时，剩余为0时删除该抽屉关联，释放抽屉资源
					List<SubCabinetDetail> list = this.subCabinetDetailService.getSubDetailListBySubBoxId(subBoxId);
					if (list.size() == 0) {
						this.subBoxAccountRefService
								.delete(this.subBoxAccountRefService.getAccountBySubBoxId(subBoxId));
					}
				}

				float subMoney = (float) (Math.round(info.getStorePrice() * num * 10000)) / 10000;
				this.subCabinetBillService.save(new SubCabinetBill(subBoxId,
						detail.getSubBox().getEquSetting().getEquSettingName(), info.getMatEquName(), info.getBarCode(),
						info.getSpec(), num, info.getBorrowType().getMessage(), info.getStorePrice(), subMoney,
						user.getAccountId(), user.getUserName(), YesOrNo.NO, info.getId()));
			}
		} else {
			logger.error("副柜中无相关物料匹配信息,请核对后再试!");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public ReturnBackPrintInfo getPrintInfo(String matBillId, String accountId, String isGetNewOne, String matInfoId,
			int num, String cabinetId, String barcode, String returnInfo, String backWay, String realLife,
			String serialNum) {
		EquSetting equSetting = this.equSettingService.get(cabinetId);
		ViewUser user = this.userService.getViewUser(accountId);

		Map<String, String> returnMap = JSON.parseObject(returnInfo, HashMap.class);
		// 可用归还类型获取
		StringBuilder normalbuild = new StringBuilder();
		List<RestitutionType> normalList = this.restitutionTypeService.getGroupList(ReturnBackType.NORMAL);
		for (RestitutionType type : normalList) {
			normalbuild.append(type.getId() + ";");
		}

		int maxReturnNo = 1000000;
		List<Integer> backNoList = matReturnBackRepository.getMaxReturnNo();
		if (backNoList != null && backNoList.size() > 0) {
			if (backNoList.get(0) != null) {
				maxReturnNo = backNoList.get(0).intValue();
			}
		}
		List<MatReturnBack> backList = new ArrayList<MatReturnBack>();
		int realBackNum = 0;
		if (backWay.equals("M")) {// 批量归还
			List<MatUseBill> billList = this.matUseBillRepository.getBatchReturnBillList(accountId, matInfoId,
					cabinetId);
			List<MatUseBill> list = billList.stream().limit(num).collect(Collectors.toList());

			for (MatUseBill bill : list) {
				MatReturnBack matReturnBack = this.matReturnBackRepository.findById(bill.getId());
				if (matReturnBack == null) {
					matReturnBack = this.matReturnBackRepository
							.save(new MatReturnBack(maxReturnNo + 1, equSetting.getEquSettingName(), barcode,
									bill.getId(), accountId, AuditStatus.NO_CHECK, IsReturnBack.NOTBACK,
									BackWay.MULTIPLE, bill.getMatUseRecord().getMatInfo().getNum(), isGetNewOne));
				} else {
					matReturnBack.setBackWay(BackWay.MULTIPLE);
					matReturnBack.setReturnBackNo(maxReturnNo + 1);
					// 重新设置以旧换新状态
					matReturnBack.setIsGetNewOne(isGetNewOne);
					matReturnBack = this.matReturnBackRepository.saveAndFlush(matReturnBack);
					// 删除历史详情
					this.matReturnDetailRepository.delByReturnBackId(matReturnBack.getId());
				}
				realBackNum += bill.getMatUseRecord().getMatInfo().getNum();
				backList.add(matReturnBack);
			}
		} else {// 单盒归还（常规归还、分类归还）
			MatUseBill bill = this.matUseBillService.get(matBillId);
			MatReturnBack matReturnBack = this.matReturnBackRepository.findById(matBillId);
			if (matReturnBack == null) {
				matReturnBack = this.matReturnBackRepository
						.save(new MatReturnBack(maxReturnNo + 1, equSetting.getEquSettingName(), barcode, matBillId,
								accountId, AuditStatus.NO_CHECK, IsReturnBack.NOTBACK, BackWay.SINGLE,
								bill.getMatUseRecord().getMatInfo().getNum(), isGetNewOne));
			} else {
				matReturnBack.setBackWay(BackWay.SINGLE);
				matReturnBack.setReturnBackNo(maxReturnNo + 1);
				// 重新设置以旧换新状态
				matReturnBack.setIsGetNewOne(isGetNewOne);
				matReturnBack = this.matReturnBackRepository.saveAndFlush(matReturnBack);
				// 删除历史详情
				this.matReturnDetailRepository.delByReturnBackId(matReturnBack.getId());
			}
			realBackNum += bill.getMatUseRecord().getMatInfo().getNum();
			backList.add(matReturnBack);
		}

		// 归还详情保存
		StringBuilder build = new StringBuilder();
		ReturnBackType backType = ReturnBackType.ABNORMAL; // 不可用归还，回收口左翻;其它，回收口右翻
		for (String key : returnMap.keySet()) {
			// value格式: 数量,类型名称
			String[] value = returnMap.get(key).split(",");
			int finalNum = Integer.parseInt(value[0]);
			if (backWay.equals("M")) {
				// 因为批量归还的数量为包装盒数，所以要乘以包装数量才是最终归还总数量
				MatEquInfo matInfo = this.matEquInfoService.get(matInfoId);
				finalNum = finalNum * matInfo.getNum();
			}
			for (MatReturnBack back : backList) {
				this.matReturnDetailRepository.save(new MatReturnDetail(back.getId(), key, finalNum));
			}
			// 可复用归还
			if (normalbuild.toString().contains(key)) {
				backType = ReturnBackType.NORMAL; // 可复用归还,回收口右翻
			}
			// 拼接归还详情字段
			build.append(value[1]);
			build.append(":");
			build.append(finalNum);
			build.append(";");
		}
		for (MatReturnBack returnBack : backList) {
			// 设置产品流水号
			returnBack.setSerialNum(serialNum);
			// 制造产量
			if (realLife == null || realLife.equals("")) {
				returnBack.setRealLife(0);
			} else {
				returnBack.setRealLife(Integer.valueOf(realLife));
			}
			// 更新归还详情字段
			returnBack.setReturnDetailInfo(build.toString());
			// 归还类型
			returnBack.setReturnBackType(backType.name());
			this.matReturnBackRepository.saveAndFlush(returnBack);
		}

		ReturnBackPrintInfo info = new ReturnBackPrintInfo();
		// 拼接归还信息
		StringBuilder builder = new StringBuilder(backWay);
		builder.append(maxReturnNo + 1);
		builder.append(";");
		builder.append(accountId);
		builder.append(";");
		builder.append(isGetNewOne);
		builder.append(";");
		builder.append(matInfoId);

		MatEquInfo matInfo = this.matEquInfoService.get(matInfoId);
		info.getInfoMap().put(PrintInfoType.QRINFO, builder.toString());
		// 归还数量
		info.getInfoMap().put(PrintInfoType.BACKNUM, String.valueOf(realBackNum));
		// 归还详情
		info.getInfoMap().put(PrintInfoType.RETURNBACKINFO, build.toString());
		// 刀具名称
		info.getInfoMap().put(PrintInfoType.MATNAME, matInfo.getMatEquName());
		// 刀具编号
		info.getInfoMap().put(PrintInfoType.BARCODE, matInfo.getBarCode());
		// 型号
		info.getInfoMap().put(PrintInfoType.MATSPEC, matInfo.getSpec());
		// 还料人
		info.getInfoMap().put(PrintInfoType.USERNAME, user.getUserName());
		// 打印日期
		info.getInfoMap().put(PrintInfoType.PRINTTIME, DateUtil.getTime(backList.get(0).getOpDate()));
		// 包装数量
		info.getInfoMap().put(PrintInfoType.PACKNUM, String.valueOf(matInfo.getNum()));
		// 以旧换新标识
		info.getInfoMap().put(PrintInfoType.ISGETNEWONE, isGetNewOne != null && "1".equals(isGetNewOne) ? "1" : "0");
		// 归还序列号
		info.getInfoMap().put(PrintInfoType.RETURNBACKNO, backWay + String.valueOf(maxReturnNo + 1));
		return info;
	}

	@Override
	public Map<String, String> getMotorBoardIPMap(String cabinetID) {

		logger.error("tool getMotorBoardIPMap:" + cabinetID);
		Map<String, String> map = new HashMap<>();
		List<EquDetail> detailList = this.equDetailService.getEquDetailListBySettingId(cabinetID);
		logger.error("tool detailList.size():" + detailList.size());
		detailList.forEach(detail -> {
			map.put(String.valueOf(detail.getRowNo()),
					new StringBuilder(detail.getIp()).append(":").append(detail.getPort()).toString());
		});
		logger.error(map + "");
		return map;
	}

	@Override
	public boolean isAppointmentCompletedByID(String appointmentID) {
		List<PhoneOrder> list = this.phoneOrderStaService.getFinishedOrderListByOrderId(appointmentID);
		return list != null && list.size() > 0;
	}

	@Override
	@Transactional
	public OpTip setAppointmentCompletedByID(String appointmentID, boolean isCompleted) {
		OpTip opTip = new OpTip("设置成功");
		if (isCompleted) {
			try {
				this.phoneOrderStaService.save(new PhoneOrderSta(appointmentID));
				List<PhoneOrderMat> phoneOrderMat = this.phoneOrderMatRepository
						.findPhoneOrderMatsByPhoneOrderId(appointmentID);
				// 订单记录消除，sta数量同步.
				String accountId = phoneOrderMat.get(0).getPhoneOrder().getAccountId();
				UserInfo user = this.adminService.getUserInfo(accountId);
				MatEquInfo info;
				MatUseRecord record;
				for (PhoneOrderMat OrderMat : phoneOrderMat) {
					List<PhoneOrderMatDetail> detailList = this.phoneOrderMatDetailRepository
							.findDetailListByOrderMatId(OrderMat.getId());
					info = this.matEquInfoService.get(OrderMat.getMatInfoId());

					// 计算金额
					float money = (float) (Math.round(info.getStorePrice() * OrderMat.getNum() * 10000)) / 10000;
					// 新增主柜领用记录
					record = new MatUseRecord(info.getId(), detailList.get(0).getEquSetting().getEquSettingName(),
							info.getMatEquName(), info.getBarCode(), info.getSpec(),
							info.getBrand() + "-" + info.getManufacturer().getManufacturerName(), OrderMat.getNum(),
							info.getBorrowType().getMessage(), info.getNum(), info.getPackUnit(),
							ReceiveType.APP.name(), ReceiveType.APP, info.getStorePrice(), money, user.getAccountId(),
							user.getUserName(), user.getDeptName(), 0, IsReturnBack.NOTBACK.getMessage());

					this.matUseRecordService.save(record);
					for (PhoneOrderMatDetail detail : detailList) {
						this.toolPullService.toolPullBill(record, detail.getEquDetailSta(), detail.getNum(), accountId,
								ReceiveType.APP.name(), ReceiveType.APP.name(), false);
					}
				}
			} catch (Exception e) {
				opTip = new OpTip(201, "设置失败");
			}
		}
		return opTip;
	}

	@Override
	public Map<String, Map<String, List<LockPara>>> getSubBoxMapGroupByBoardNo(String cabinetId) {
		Map<String, Map<String, List<LockPara>>> subBoxMapGroupByBoardNo = new LinkedHashMap<>();
		Map<String, List<LockPara>> innerMap;
		List<LockPara> paraList;
		// 查询暂存柜所有格子
		List<SubBox> boxList = this.subBoxService.getSubBoxList(cabinetId);
		Map<Integer, List<SubBox>> subBoxMap = boxList.stream().collect(Collectors.groupingBy(SubBox::getBoardNo));
		for (Entry<Integer, List<SubBox>> entry : subBoxMap.entrySet()) {
			innerMap = new LinkedHashMap<>();
			for (SubBox box : entry.getValue()) {
				paraList = innerMap.get(String.valueOf(box.getRowNo()));
				if (paraList == null) {
					paraList = new ArrayList<>();
				}
				// 盒子封装通讯对象
				paraList.add(new LockPara(box.getBoxIndex(), box.getBoardNo(), box.getLockIndex()));
				// 设置行盒子信息分组
				innerMap.put(String.valueOf(box.getRowNo()), paraList);
			}
			subBoxMapGroupByBoardNo.put(entry.getKey().toString(), innerMap);
		}
		return subBoxMapGroupByBoardNo;
	}

	@Override
	public List<SubMatDetail> getUnSubMatInfoList(String accountId) {
		List<SubMatDetail> matList = new ArrayList<>();
		// 待暂存物料信息
		List<MatUseBill> billList = this.matUseBillService.getUnReturnList(accountId);
		for (MatUseBill bill : billList) {
			matList.add(new SubMatDetail(bill.getId(), bill.getMatInfoId(), bill.getMatEquName(), bill.getBarCode(),
					bill.getSpec()));
		}
		return matList;
	}

	@Override
	public SubMatDetail outsubcabinet(String subboxId, String matInfoId, String accountId) {
		SubCabinetDetail info = this.subCabinetDetailService.outsubcabinet(subboxId, matInfoId);
		SubMatDetail detail = new SubMatDetail(info.getSubBoxId(), info.getMatInfoId(),
				info.getMatInfo().getMatEquName(), info.getMatInfo().getBarCode(), info.getMatInfo().getSpec(),
				info.getMatInfo().getNum(), info.getNum(), info.getMatInfo().getBorrowType().name(),
				info.getMatInfo().getBorrowType().getMessage(), info.getMatInfo().getOldForNew().name(),
				FileUtil.convertImageToBase64Data(
						new File(this.toolProperties.getUploadPath() + info.getMatInfo().getIcon())),
				null, info.getSubBox().getEquSettingId(), info.getSubBox().getEquSetting().getEquSettingName(),
				new LockPara(info.getSubBox().getBoxIndex(), info.getSubBox().getBoardNo(),
						info.getSubBox().getLockIndex()));

		return detail;
	}

	@Override
	public SubMatDetail getMatDetail(String matId, String accountId) {
		// 获取借出统计数按物料分类
		MatEquInfo info = this.matEquInfoService.get(matId);
		// 获取该物料所在抽屉
		SubBox box = null;
		List<SubCabinetDetail> detailInfo = this.subCabinetDetailRepository
				.getSubDetailListByAccountIdAndMatId(accountId, matId);
		if (detailInfo != null && detailInfo.size() > 0) {
			box = detailInfo.get(0).getSubBox();
		}

		SubMatDetail detail = new SubMatDetail(box != null ? box.getId() : "", matId, info.getMatEquName(),
				info.getBarCode(), info.getSpec(), info.getNum(), info.getNum(), info.getBorrowType().name(),
				info.getBorrowType().getMessage(), info.getOldForNew().getMessage(),
				FileUtil.convertImageToBase64Data(new File(this.toolProperties.getUploadPath() + info.getIcon())), null,
				"", "", new LockPara(0, 0, box != null ? box.getBoxIndex() : 0));
		return detail;
	}

	@Override
	public MatDetail getMatRemainNumByMatId(String cabinetId, String matId) {
		MatDetail matDetail = null;
		Map<MatEquInfo, Integer> tqMap = this.cabinetLocalService.getTotalQuantityGroupByMatEquInfo(cabinetId);
		List<MatEquInfo> infoList = this.matEquInfoService.findByMatId(matId);
		for (MatEquInfo info : infoList) {
			// 当前柜子没有此物料,忽略
			if (tqMap.get(info) == null) {
				continue;
			}
			matDetail = new MatDetail(info.getId(), info.getMatEquName(), info.getBarCode(), info.getSpec(),
					info.getNum(), tqMap.get(info), info.getBorrowType().name(),
					FileUtil.convertImageToBase64Data(new File(this.toolProperties.getUploadPath() + info.getIcon())));
		}
		return matDetail;
	}

//	/**
//	 * @description 归还信息对外接口推送
//	 * @param returnBack
//	 * @param useRecord
//	 * @param ExternalSetting
//	 * @author chenlei
//	 */
//	private void returnBackPost(MatReturnBack returnBack, MatUseRecord useRecord, ExternalSetting urlSetting) {
//		LinkedMultiValueMap<String, Object> backMap = new LinkedMultiValueMap<>();
//		// 获取员工信息
//		UserInfo user = this.adminService.getUserInfo(useRecord.getAccountId());
//		// 柜子信息
//		EquSetting setting = returnBack.getMatUseBill().getEquDetailSta().getEquDetail().getEquSetting();
//		// 领用信息
//		ExternalBack backInfo = new ExternalBack(useRecord.getAccountId(), user.getUserName(), useRecord.getMatInfoId(),
//				useRecord.getMatInfoName(), useRecord.getSpec(), useRecord.getBarCode(),
//				returnBack.getMatUseBill().getMatInfo().getManufacturer().getManufacturerName(),
//				returnBack.getMatUseBill().getMatInfo().getManufacturerId(), returnBack.getBarCode(),
//				returnBack.getMatUseBill().getMatUseRecord().getPackNum(), setting.getId(), setting.getEquSettingName(),
//				returnBack.getOpDate(), returnBack.getMatUseBill().getOpDate(), true);
//		backMap.add("returnBackReceives", backInfo);
//		String param = JSON.toJSONString(backMap);
//
//		String token = null;
//		if (urlSetting.getTokenMethod() != null) {
//			token = ExternalUtil.ExternalGet(urlSetting.getUrl() + urlSetting.getTokenMethod());
//		}
//		JSONObject result = ExternalUtil.ExternalPost(param, token, urlSetting.getUrl() + urlSetting.getBackMethod());
//
//		// 请求不成功时记录
//		YesOrNo yesOrNo = YesOrNo.YES;
//		String message = null;
//		if (result != null) {
//			message = result.getString("massage");
//			String code = result.getString("code");
//			if (code == null || !code.equals("OK")) {
//				yesOrNo = YesOrNo.NO;
//			}
//		} else {
//			yesOrNo = YesOrNo.NO;
//			message = "接口连接失败";
//		}
//		this.postStatusRepository.save(new PostStatus(null, null, true,
//				urlSetting.getUrl() + urlSetting.getBackMethod(), param, yesOrNo, message));
//	}

	/**
	 * @description 领取来源汇总
	 * @param sta
	 * @param num
	 * @author chenlei
	 */
	public String getBorrowOrigin(EquDetailSta sta, int num) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(sta.getEquDetail().getEquSetting().getEquSettingName());
		buffer.append("(");
		buffer.append(sta.getEquDetail().getRowNo());
		buffer.append("-");
		buffer.append(sta.getColNo());
		buffer.append("):");
		buffer.append(num);
		buffer.append(";");
		return buffer.toString();
	}

	@Override
	public OpTip stockCheck(String cabinetId, Map<String, List<CartInfo>> voucherMap) {
		OpTip tip = new OpTip(200, "校验成功");
		Map<String, Integer> matMap = new HashMap<>();
		List<CartInfo> infoList;
		Integer borrowNum;
		for (Entry<String, List<CartInfo>> entry : voucherMap.entrySet()) {
			infoList = entry.getValue();
			for (CartInfo info : infoList) {
				borrowNum = matMap.get(info.getMatId());
				matMap.put(info.getMatId(), (borrowNum == null ? 0 : borrowNum) + info.getNum());
			}
		}

		// 获取所有有效库存的集合
		List<EquDetailSta> staList = this.equDetailStaService.getEquDetailStaTreeListByCabinetId(cabinetId);
		// 汇总物料库存总和
		Map<String, IntSummaryStatistics> staMap = staList.stream()
				.filter(sta -> sta.getMatInfoId() != null && !sta.getMatInfoId().equals("")).collect(Collectors
						.groupingBy(EquDetailSta::getMatInfoId, Collectors.summarizingInt(EquDetailSta::getCurNum)));
		// 库存校验
		for (Entry<String, Integer> entry : matMap.entrySet()) {
			MatEquInfo matEquInfo = this.matEquInfoService.get(entry.getKey());
			long stockNum = staMap.get(entry.getKey()).getSum();// 库存数量

			// 按支领取库存数量=包装数量*盒数量
			if (matEquInfo.getBorrowType().equals(BorrowType.METER)) {
				stockNum = stockNum * matEquInfo.getNum();
			}
			// 判断取出数量是否大于库存数量
			if (matMap.get(entry.getKey()) > stockNum) {
				tip = new OpTip(204, "【库存不足提醒】  " + matEquInfo.getMatEquName() + " 剩余：" + stockNum);
			}
		}
		return tip;
	}

	@Override
	public List<ExtraCabinet> sendApplyVoucherToServer(String cabinetId, Map<String, List<CartInfo>> voucherMap,
			String shareSwitch, String accountId) {
		Map<ExtraCabinet, List<Card>> cartMap = new HashMap<>();
		// 获取员工信息
		UserInfo user = this.adminService.getUserInfo(accountId);
		// 获取暂存柜分配最大数量
		Integer extraBoxNum = this.extraBoxNumSettingService.getExtraBoxNumByAccountId(accountId);

		if (extraBoxNum == null) {
			extraBoxNum = 0;
		}

		List<Cart> localCartList = new ArrayList<Cart>();
		List<CartInfo> infoList;
		StringBuffer voucherNo = new StringBuffer(); // 申请单号
		for (Entry<String, List<CartInfo>> entry : voucherMap.entrySet()) {
			voucherNo.append(entry.getKey());
			voucherNo.append("|");
			infoList = entry.getValue();
			for (CartInfo info : infoList) {
				localCartList.add(new Cart(accountId, info.getMatId(), info.getReceiveType(), info.getReceiveInfo(),
						info.getNum(), cabinetId));
			}
		}

		MatEquInfo info;
		BorrowType borrowType;
		List<EquDetailSta> staList;
		Integer pullNum; // 领取数量
		Integer subNum;
		String receiveType;
		String receiveInfo;
		EquSetting setting = this.equSettingService.get(cabinetId);
		Map<String, Integer> map = this.subCabinetDetailService.getTotalQuantityGroupByMatId(shareSwitch, accountId);
		// 弹簧位置对应已取数量
		Map<String, Integer> staMap = new HashMap<>();
		// 弹簧已取数量
		Integer staNum;
		// 获取当前柜子存储详情集合
		List<EquDetailSta> detailList = this.equDetailStaService.getEquDetailStaTreeListByCabinetId(cabinetId);
		// 物料->柜子存储详情分组
		Map<String, List<EquDetailSta>> detailStaMap = detailList.stream()
				.filter(sta -> sta.getMatInfoId() != null && !"".equals(sta.getMatInfoId())
						&& EquSta.NONE.equals(sta.getEquSta()) && UsingStatus.ENABLE.equals(sta.getStatus())
						&& sta.getCurNum() > 0)
				.collect(Collectors.groupingBy(EquDetailSta::getMatInfoId));

		// 遍历购物车信息
		for (Cart cart : localCartList) {
			subNum = 0;
			// 当前物料需领取数量
			pullNum = cart.getNum();
			// 取料数量小于1忽略
			if (pullNum < 1) {
				continue;
			}
			receiveType = cart.getReceiveType();
			receiveInfo = cart.getReceiveInfo();
			// 获取当前物料信息
			if (cart.getMatInfo() != null) {
				info = cart.getMatInfo();
			} else {
				info = this.matEquInfoService.get(cart.getMatId());
			}
			borrowType = info.getBorrowType();
			// 统计物料数量
			int curNum = pullNum;
			// 如果是按计量单位领取的重新计算数量,如果按包装计算,则忽略包装数量、剩余数量
			if (BorrowType.METER.equals(info.getBorrowType())) {
				// 如果按包装取数,则按单只计算数量
				if (BorrowType.PACK.equals(borrowType)) {
					pullNum *= info.getNum();
				}
				// 获取当前物料暂存柜存量
				subNum = map.get(cart.getMatId()) == null ? 0 : map.get(cart.getMatId());
				// 存量大于取量,直接通过暂存柜数量扣除
				if (subNum >= pullNum) {
					continue;
				} else { // 暂存数量不足
					// 重新计算需取出数量
					curNum = (pullNum - subNum) % info.getNum() == 0 ? (pullNum - subNum) / info.getNum()
							: (pullNum - subNum) / info.getNum() + 1;
				}
			}

			MatUseRecord record = new MatUseRecord(info.getId(), null, info.getMatEquName(), info.getBarCode(),
					info.getSpec(),
					info.getBrand() + "-"
							+ (info.getManufacturer() != null ? info.getManufacturer().getManufacturerName() : ""),
					BorrowType.METER.equals(info.getBorrowType()) ? curNum * info.getNum() : curNum,
					info.getBorrowType().getMessage(), info.getNum(), info.getPackUnit(), receiveInfo,
					ReceiveType.valueOf(receiveType), info.getStorePrice(), 0F, user.getAccountId(), user.getUserName(),
					user.getDeptName(), 0, IsReturnBack.NOTBACK.getMessage());

			this.matUseRecordService.save(record);
			voucherNo.append(record.getId());
			voucherNo.append(",");
			// 统计物料数量
			int result = 0;
			// 如果是按计量单位领取的重新计算数量,如果按包装计算,则忽略包装数量、剩余数量
			if (BorrowType.METER.equals(info.getBorrowType())) {
				// 副柜需回存剩余数量(需取出数量*包装数量-取出数量)
				result = curNum * info.getNum() - pullNum;
				if (BorrowType.PACK.equals(borrowType)) { // 片装按包取
					result = curNum * info.getNum() - pullNum;
				}
				if (result > 0) { // 有剩余数量时
					String msg = "";
					// 获取正在用的暂存柜
					List<SubBox> subList = this.subBoxAccountRefService.getUsingSubBoxList(accountId);
					// 库位可用余量
					int extraNum = 0;
					if (subList != null && subList.size() > 0) {
						if (subList.size() == extraBoxNum) {
							for (SubBox box : subList) {
								extraNum += box.getExtraNum();
							}
							if (extraNum > 0) {
								msg = "请将多余物料存入暂存柜！";
							} else {
								msg = "你使用的暂存柜数量已达上限!";
							}
						} else if (subList.size() < extraBoxNum) {
							msg = "请将多余物料存入暂存柜！";
						} else {
							msg = "你使用的暂存柜数量已达上限!";
						}
					} else {
						msg = "请将多余物料存入暂存柜！";
					}
					GlobalUserUtil.writeMsg(setting.getId(), MsgType.SUBALERTINFO, msg);
				} else if (result != 0) {
					String msg = "剩余物料请在暂存柜中领取！";
					GlobalUserUtil.writeMsg(setting.getId(), MsgType.SUBALERTINFO, msg);
				}
			}

			// 获取当前物料存储详情
			// 按照批次号先进先出原则排序
			staList = detailStaMap.get(cart.getMatId());
			if (staList == null || staList.size() < 1) {
				continue;
			}
			Collections.sort(staList, new Comparator<EquDetailSta>() {
				@Override
				public int compare(EquDetailSta o1, EquDetailSta o2) {
					return o1.getLastFeedTime().compareTo(o2.getLastFeedTime());
				}
			});

			String borrowOrigin = "";// 取货来源初始化
			CabinetType cabinetType = null;
			for (EquDetailSta sta : staList) {
				if (curNum < 1) {
					break;
				}

				Boolean isSyn = false; // 是否同步数据
				cabinetType = sta.getEquDetail().getEquSetting().getCabinetType();
				if (cabinetType.equals(CabinetType.SUB_CABINET)) {
					cabinetType = sta.getEquDetail().getEquSetting().getEquSettingParent().getCabinetType();
				}
				switch (cabinetType) {
				case KNIFE_CABINET_C:
				case KNIFE_CABINET_C_A:
				case KNIFE_CABINET_C_B:
				case KNIFE_CABINET_DETA:
				case KNIFE_CABINET_DETB:
					isSyn = true;
					break;
				default:
					break;
				}
				// 异步库存数量
				if (isSyn != null && !isSyn) {
					// 工作空间存量大于提取数量
					if (sta.getCurNum() >= curNum) {
						this.procCartMap(cartMap, sta, record.getId(), curNum);
						this.toolPullService.toolPullBill(record, sta, curNum, accountId, receiveType, receiveInfo,
								false);
						borrowOrigin += getBorrowOrigin(sta, curNum);
						break;
					} else {
						curNum -= sta.getCurNum();
						// 空间存量不足则取全部存量
						this.procCartMap(cartMap, sta, record.getId(), sta.getCurNum());
						this.toolPullService.toolPullBill(record, sta, sta.getCurNum(), accountId, receiveType,
								receiveInfo, false);
						borrowOrigin += getBorrowOrigin(sta, sta.getCurNum());
					}
				} else { // 同步库存数量
					staNum = staMap.get(sta.getId()) == null ? 0 : staMap.get(sta.getId());
					if (staNum.intValue() >= sta.getCurNum().intValue()) {
						continue;
					}
					// 工作空间存量大于提取数量
					if (sta.getCurNum() - staNum >= curNum) {
						this.procCartMap(cartMap, sta, record.getId(), curNum);
						staMap.put(sta.getId(), staNum + curNum);
						borrowOrigin += getBorrowOrigin(sta, curNum);
						break;
					} else {
						int sy = sta.getCurNum() - staNum;
						if (sy > 0) { // 还有剩
							curNum -= sy; // 还要继续取的数量
							// 空间存量不足则取全部存量
							this.procCartMap(cartMap, sta, record.getId(), sy);
							staMap.put(sta.getId(), staNum + sy);
							borrowOrigin += getBorrowOrigin(sta, sy);
						}
					}
				}
			}
			record.setBorrowOrigin(borrowOrigin);
			this.matUseRecordService.update(record);
		}

		GlobalUserUtil.writeMsg(setting.getId(), MsgType.BORROWPOST, voucherNo.toString());

		List<ExtraCabinet> cabinetList = new ArrayList<>();
		ExtraCabinet cabinet;
		for (Entry<ExtraCabinet, List<Card>> entry : cartMap.entrySet()) {
			cabinet = entry.getKey();
			cabinet.setCardList(entry.getValue());
			cabinetList.add(cabinet);
		}
		return cabinetList;
	}
}