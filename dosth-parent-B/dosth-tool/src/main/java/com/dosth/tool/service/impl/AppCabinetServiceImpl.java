package com.dosth.tool.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.app.dto.AppBorrowInfo;
import com.dosth.app.dto.AppCabinet;
import com.dosth.app.dto.AppCart;
import com.dosth.app.dto.AppKnifes;
import com.dosth.app.dto.AppMatInfoType;
import com.dosth.app.dto.AppOrder;
import com.dosth.app.dto.AppOrderItem;
import com.dosth.app.dto.AppRecycleResult;
import com.dosth.app.dto.AppRecycleReview;
import com.dosth.app.dto.AppShopping;
import com.dosth.app.dto.AppSubscribe;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.QrCreator;
import com.dosth.constant.ScanCodeTypeEnum;
import com.dosth.netty.dto.MsgType;
import com.dosth.netty.util.GlobalUserUtil;
import com.dosth.remote.ResponseTip;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.AuditStatus;
import com.dosth.tool.common.state.VerifyMode;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.MatReturnBack;
import com.dosth.tool.entity.mobile.PhoneCart;
import com.dosth.tool.entity.mobile.PhoneOrder;
import com.dosth.tool.entity.mobile.PhoneOrderMat;
import com.dosth.tool.entity.mobile.PhoneOrderMatDetail;
import com.dosth.tool.repository.EquDetailRepository;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.MatReturnBackRepository;
import com.dosth.tool.repository.PhoneCartRepository;
import com.dosth.tool.repository.PhoneOrderMatDetailRepository;
import com.dosth.tool.repository.PhoneOrderMatRepository;
import com.dosth.tool.repository.PhoneOrderRepository;
import com.dosth.tool.repository.PhoneOrderStaRepository;
import com.dosth.tool.repository.UserRepository;
import com.dosth.tool.service.AppCabinetService;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.toolcabinet.dto.PrintScan;

import net.sf.json.JSONObject;

/**
 * 
 * @description 手机App请求Service实现
 * @author liweifeng,xutianpeng,guozhidong
 *
 */
@Service
@Transactional
public class AppCabinetServiceImpl implements AppCabinetService {

	private static final Logger logger = LoggerFactory.getLogger(AppCabinetServiceImpl.class);

	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private EquDetailRepository equDetailRepository;
//	@Autowired
//	private MatEquTypeRepository matEquTypeRepository;
	@Autowired
	private PhoneOrderMatRepository phoneOrderMatRepository;
	@Autowired
	private PhoneOrderMatDetailRepository phoneOrderMatDetailRepository;
	@Autowired
	private PhoneOrderRepository phoneOrderRepository;
	@Autowired
	private PhoneOrderStaRepository phoneOrderStaRepository;
	@Autowired
	private PhoneCartRepository phoneCartRepository;
	@Autowired
	private MatReturnBackRepository matReturnBackRepository;
	@Autowired
	UserRepository userRepository;

	// 柜机列表
	@Override
	public List<AppCabinet> getAppCabinetList(Map<String, String> page, Map<String, String> params) {
		List<AppCabinet> appCabinetList = new ArrayList<>();
		AppCabinet appCabinet = null;
		Criteria<EquSetting> criteria = new Criteria<>();
		// 按库存数量排序
//		if (params.get("inventory") != null && !"".equals(params.get("inventory"))) {
//			criteria.add(Restrictions.eq("curNum", params.get("inventory"), true));
//		}
		Page<EquSetting> pageSetting = this.equSettingRepository.findAll(criteria,
				new PageRequest(Integer.valueOf(page.get("pageNo")) - 1, Integer.valueOf(page.get("pageSize"))));
		List<EquSetting> equSettingList = pageSetting.getContent();
		List<EquDetailSta> staList;

		Map<EquSetting, List<EquDetailSta>> map = this.equDetailStaRepository.findAll().stream()
				.collect(Collectors.groupingBy(EquDetailSta::getMainCabinet));

		for (EquSetting setting : equSettingList) {
			staList = map.get(setting);
			appCabinet = new AppCabinet(setting.getId(), setting.getEquSettingName(), "未确定位置",
					staList.stream().filter(sta -> sta.getMatInfo() != null).mapToInt(EquDetailSta::getCurNum).sum(),
					50);
			appCabinetList.add(appCabinet);
		}
		return appCabinetList;
	}

	// 已修改 4 机柜搜索
	@Override
	public List<AppCabinet> getAppCabinetListByKnifes(Map<String, String> page, Map<String, String> params) {
		List<AppCabinet> appCabinetList = new ArrayList<>();
		Criteria<EquDetailSta> criteria = new Criteria<>();
		if (params.get("keyword") != null && !"".equals(params.get("keyword"))) {
			criteria.add(Restrictions.like("matInfo.barCode", params.get("keyword"), true));
		}
		if (params.get("knifeld") != null && !"".equals(params.get("knifeld"))) {
			criteria.add(Restrictions.eq("matInfo.id", params.get("knifeld"), true));
		}
		Page<EquDetailSta> pageSta = this.equDetailStaRepository.findAll(criteria,
				new PageRequest(Integer.valueOf(page.get("pageNo")) - 1, Integer.valueOf(page.get("pageSize"))));
		List<EquDetailSta> staList = pageSta.getContent();
		EquSetting equSetting = null;
		for (EquDetailSta sta : staList) {
			equSetting = sta.getEquDetail().getEquSetting();
			appCabinetList.add(
					new AppCabinet(equSetting.getId(), equSetting.getEquSettingName(), "未确定位置", sta.getCurNum(), 5));
		}
		return appCabinetList;
	}

	// 已确认 5 刀具列表
	@Override
	public Map<String, List<AppKnifes>> getAppKnifesListByCupboardId(String cupboardId) {
		Map<String, List<AppKnifes>> map = new LinkedHashMap<>();
		List<AppKnifes> appKnivesList;
		List<EquDetail> equDetailList = this.equDetailRepository.getEquDetailListBySettingId(cupboardId);
		Collections.sort(equDetailList, new Comparator<EquDetail>() {
			@Override
			public int compare(EquDetail o1, EquDetail o2) {
				return o1.getRowNo() - o2.getRowNo();
			}
		});
		List<EquDetailSta> staList;
		for (EquDetail equDetail : equDetailList) {
			staList = this.equDetailStaService.getStaListByDetailId(equDetail.getId());
			appKnivesList = new ArrayList<>();
			for (EquDetailSta sta : staList) {
				if (sta.getMatInfo() == null) {
					continue;
				}
				appKnivesList.add(new AppKnifes(sta.getMatInfoId(), sta.getMatInfo().getBarCode(),
						sta.getMatInfo().getMatEquName(), sta.getMatInfo().getStorePrice(),
						equDetail.getRowNo() + "-" + sta.getColNo(), sta.getCurNum(),
						this.toolProperties.getImgUrlPath() + sta.getMatInfo().getIcon(), sta.getMatInfo().getSpec()));
			}
			if (appKnivesList.size() > 0) {
				map.put(String.valueOf(equDetail.getRowNo()), appKnivesList);
			}
		}
		return map;
	}

	// 已修改 6 刀具预约信息
	@Override
	public List<AppSubscribe> getAppSubscribeList(Map<String, String> page, String knifeId) {
		List<AppSubscribe> appSubscribeList = new ArrayList<>();
		Criteria<PhoneOrderMat> criteria = new Criteria<>();
		criteria.add(Restrictions.gt("phoneOrder.orderCreatedTime", DateUtil.getBeforeMin(-20), true));
		if (knifeId != null && !"".equals(knifeId)) {
			criteria.add(Restrictions.eq("matInfoId", knifeId, true));
		}
		Page<PhoneOrderMat> orderMatPage = this.phoneOrderMatRepository.findAll(criteria,
				new PageRequest(Integer.valueOf(page.get("pageNo")) - 1, Integer.valueOf(page.get("pageSize"))));
		List<PhoneOrderMat> orderMatList = orderMatPage.getContent();
		PhoneOrder order;
		for (PhoneOrderMat orderMat : orderMatList) {
			order = orderMat.getPhoneOrder();
			appSubscribeList.add(
					new AppSubscribe(order.getUser().getLoginName(), order.getOrderCreatedTime(), orderMat.getNum()));
		}
		return appSubscribeList;
	}

	// 已修改 7 刀具搜索
	@Override
	public List<AppKnifes> getAppKnifes(Map<String, String> page, Map<String, String> params) {
		List<AppKnifes> appSubscribeList = new ArrayList<>();
		AppKnifes appKnifes = null;
		Criteria<EquDetailSta> criteria = new Criteria<>();
		if (params.get("keyword") != null && !"".equals(params.get("keyword"))) {
			criteria.add(Restrictions.like("matInfo.barCode", params.get("keyword"), true));
		}
		if (params.get("cupboardId") != null && !"".equals(params.get("cupboardId"))) {
			criteria.add(Restrictions.eq("equDetail.equSettingId", params.get("cupboardId"), true));
		}
		Page<EquDetailSta> staPage = this.equDetailStaRepository.findAll(criteria,
				new PageRequest(Integer.valueOf(page.get("pageNo")) - 1, Integer.valueOf(page.get("pageSize"))));
		List<EquDetailSta> staList = staPage.getContent();
		for (EquDetailSta sta : staList) {
			if (sta.getMatInfo() == null) {
				continue;
			}
			appKnifes = new AppKnifes(sta.getMatInfoId(), sta.getMatInfo().getBarCode(),
					sta.getMatInfo().getMatEquName(), sta.getMatInfo().getStorePrice(),
					sta.getEquDetail().getRowNo() + "-" + sta.getColNo(), sta.getCurNum(), "KnifeImg",
					sta.getMatInfo().getSpec());
			appSubscribeList.add(appKnifes);
		}
		return appSubscribeList;
	}

//	// 8 分类类型列表
//	@Override
//	public List<AppMatInfoType> getAppTypeList() {
//		List<AppMatInfoType> appTypeList = new ArrayList<>();
//		List<MatEquType> matEquTypeList = this.matEquTypeRepository.findAll();
//		for (MatEquType type : matEquTypeList) {
//			// 过滤物料类型
//			if (MatEqu.MATERIAL.equals(type.getMatEqu())) {
//				appTypeList.add(new AppMatInfoType(type.getId(), type.getTypeName()));
//			}
//		}
//		return appTypeList;
//	}

	// 13 订单查询
	@Override
	public List<AppOrder> searchAppOrder(Map<String, String> page, Map<String, String> params) {
		List<AppOrder> list = new ArrayList<>();
		// 订单类型, 0待使用;1 已使用;-1 已失效
		String orderType = params.get("orderType");
		String accountId = params.get("userId");
		Page<PhoneOrder> orderPage; // 订单分页数据
		List<PhoneOrder> orderList; // 订单列表
		List<PhoneOrderMat> orderMatList; // 订单列表
		AppOrder appOrder;
		List<AppOrderItem> itemList; // app封装对象列表
		AppOrderItem item = null;
		if (orderType != null) {
			switch (orderType) {
			case "0":
				orderPage = this.phoneOrderStaRepository.getWait(accountId, DateUtil.getBeforeMin(-20), new PageRequest(
						Integer.valueOf(page.get("pageNo")) - 1, Integer.valueOf(page.get("pageSize"))));
				break;
			case "1":
				orderPage = this.phoneOrderStaRepository.getFinished(accountId, new PageRequest(
						Integer.valueOf(page.get("pageNo")) - 1, Integer.valueOf(page.get("pageSize"))));
				break;
			case "-1":
				orderPage = this.phoneOrderStaRepository.getUnUsed(accountId, DateUtil.getBeforeMin(-20),
						new PageRequest(Integer.valueOf(page.get("pageNo")) - 1,
								Integer.valueOf(page.get("pageSize"))));
				break;
			default:
				orderPage = this.phoneOrderRepository.getPageAll(accountId, new PageRequest(
						Integer.valueOf(page.get("pageNo")) - 1, Integer.valueOf(page.get("pageSize"))));
				break;
			}
			switch (orderType) {
			case "0":
				orderList = orderPage.getContent();
				for (PhoneOrder order : orderList) {
					itemList = new ArrayList<>();
					orderMatList = this.phoneOrderMatRepository.findPhoneOrderMatsByPhoneOrderId(order.getId());
					for (PhoneOrderMat orderMat : orderMatList) {
						item = new AppOrderItem(orderMat.getId(), orderMat.getMatInfoId(),
								orderMat.getMatInfo().getMatEquName(), "柜子名称",
								Integer.valueOf(String
										.valueOf(DateUtil.getDatePoorMinute(order.getOrderCreatedTime(), new Date()))),
								order.getOrderCode(), orderMat.getMatInfo().getStorePrice(), orderType,
								orderMat.getNum(),
								this.toolProperties.getImgUrlPath() + orderMat.getMatInfo().getIcon());
						item.setPurchaseCodeUrl(orderMat.getOrderMatCode());
						itemList.add(item);
					}
					appOrder = new AppOrder(order.getId(), orderType, order.getOrderCreatedTime(),
							order.getOrderCode());
					appOrder.setOrders(itemList);
					list.add(appOrder);
				}
				break;
			case "1":
				orderList = orderPage.getContent();
				for (PhoneOrder order : orderList) {
					itemList = new ArrayList<>();
					orderMatList = this.phoneOrderMatRepository.findPhoneOrderMatsByPhoneOrderId(order.getId());
					for (PhoneOrderMat orderMat : orderMatList) {
						item = new AppOrderItem(orderMat.getId(), orderMat.getMatInfoId(),
								orderMat.getMatInfo().getMatEquName(), "柜子名称",
								Integer.valueOf(String
										.valueOf(DateUtil.getDatePoorMinute(order.getOrderCreatedTime(), new Date()))),
								order.getOrderCode(), orderMat.getMatInfo().getStorePrice(), orderType,
								orderMat.getNum(),
								this.toolProperties.getImgUrlPath() + orderMat.getMatInfo().getIcon());
						item.setPurchaseCodeUrl(orderMat.getOrderMatCode());
						itemList.add(item);
					}
					appOrder = new AppOrder(order.getId(), orderType, order.getOrderCreatedTime(),
							order.getOrderCode());
					appOrder.setOrders(itemList);
					list.add(appOrder);
				}
				break;
			case "-1":
				orderList = orderPage.getContent();
				for (PhoneOrder order : orderList) {
					itemList = new ArrayList<>();
					orderMatList = this.phoneOrderMatRepository.findPhoneOrderMatsByPhoneOrderId(order.getId());
					for (PhoneOrderMat orderMat : orderMatList) {
						item = new AppOrderItem(orderMat.getId(), orderMat.getMatInfoId(),
								orderMat.getMatInfo().getMatEquName(), "柜子名称",
								Integer.valueOf(String
										.valueOf(DateUtil.getDatePoorMinute(order.getOrderCreatedTime(), new Date()))),
								order.getOrderCode(), orderMat.getMatInfo().getStorePrice(), orderType,
								orderMat.getNum(),
								this.toolProperties.getImgUrlPath() + orderMat.getMatInfo().getIcon());
						item.setPurchaseCodeUrl(orderMat.getOrderMatCode());
						itemList.add(item);
					}
					appOrder = new AppOrder(order.getId(), orderType, order.getOrderCreatedTime(),
							order.getOrderCode());
					appOrder.setOrders(itemList);
					list.add(appOrder);
				}
				break;
			default:
				orderList = orderPage.getContent();
				String status = null;
				List<PhoneOrder> phoneOrderList;
				for (PhoneOrder order : orderList) {
					itemList = new ArrayList<>();
					phoneOrderList = this.phoneOrderStaRepository.getFinishedOrderListByOrderId(order.getId());
					if (phoneOrderList != null && phoneOrderList.size() > 0) {
						status = "1";
					} else if (DateUtil.getDatePoorMinute(order.getOrderCreatedTime(), new Date()) > 20) {
						status = "-1";
					} else {
						status = "0";
					}
					orderMatList = this.phoneOrderMatRepository.findPhoneOrderMatsByPhoneOrderId(order.getId());
					for (PhoneOrderMat orderMat : orderMatList) {
						item = new AppOrderItem(orderMat.getId(), orderMat.getMatInfoId(),
								orderMat.getMatInfo().getMatEquName(), "柜子名称",
								Integer.valueOf(String
										.valueOf(DateUtil.getDatePoorMinute(order.getOrderCreatedTime(), new Date()))),
								order.getOrderCode(), orderMat.getMatInfo().getStorePrice(), status, orderMat.getNum(),
								this.toolProperties.getImgUrlPath() + orderMat.getMatInfo().getIcon());
						item.setPurchaseCodeUrl(orderMat.getOrderMatCode());
						itemList.add(item);
					}
					appOrder = new AppOrder(order.getId(), "", order.getOrderCreatedTime(), order.getOrderCode());
					appOrder.setOrders(itemList);
					list.add(appOrder);
				}
				break;
			}
		}
		return list;
	}

	@Override
	@Transactional
	public ResponseTip shoppingSubmit(AppShopping shopping, String accountId) {
		ResponseTip tip = new ResponseTip("预约成功");
		Map<String, Integer> detailMap = new HashMap<>(); // 预约物料,物料Id/物料数量
		PhoneCart phoneCart = null;
		for (AppCart cart : shopping.getData()) {
			phoneCart = this.phoneCartRepository.getOne(cart.getShoppingId());
			detailMap.put(phoneCart.getMatId(), phoneCart.getNum());
			this.phoneCartRepository.delete(phoneCart);
		}

		// 获取20分钟内已预约物料详情集合
		Map<String, IntSummaryStatistics> equDetailStaMap = getEquDetailStaMap();

		List<EquDetailSta> equDetailStaList = this.equDetailStaService.getEquDetailStaListByCabinetId(null);

		// 过滤状态有效且余量大于0
		Map<String, List<EquDetailSta>> detailStaMap = equDetailStaList.stream()
				.filter(sta -> UsingStatus.ENABLE.equals(sta.getStatus()) && sta.getMatInfoId() != null
						&& !"".equals(sta.getMatInfoId()) && sta.getCurNum() != null && sta.getCurNum() > 0)
				.collect(Collectors.groupingBy(EquDetailSta::getMatInfoId));

		// 添加订单号
		PhoneOrder phoneOrder = new PhoneOrder(accountId);
		phoneOrder = this.phoneOrderRepository.save(phoneOrder);

		// 全部订单内容
		// 物料/柜子/行号/PLCIP/列号,数量
		Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>> matMap = new HashMap<>();
		Map<String, Map<String, Map<String, Map<String, Integer>>>> cabinetMap; // 柜子标识
		Map<String, Map<String, Map<String, Integer>>> rowMap; // 行标识
		Map<String, Map<String, Integer>> plcMap = null; // plc板标识
		Map<String, Integer> columnMap = null; // 列标识

		EquSetting cabinet = null;
		String cabinetId; // 柜子Id
		String rowNo; // 行号
		String plcIp; // PLC板IP
		String colNo; // 列号
		int curNum; // 当前数量
		int leftNum = 0; // 剩余数量

		PhoneOrderMat orderMat = null;
		PhoneOrderMatDetail orderMatDetail = null;

		JSONObject obj = null;
		List<EquDetailSta> detailStaList;
		// 遍历全部订单项
		for (Entry<String, Integer> entry : detailMap.entrySet()) {
			// 获取当前物料存储详情
			detailStaList = detailStaMap.get(entry.getKey());
			curNum = entry.getValue();
			orderMat = this.phoneOrderMatRepository.save(new PhoneOrderMat(phoneOrder.getId(), entry.getKey(), curNum));

			// 按取料优先顺排序
			Collections.sort(detailStaList, new Comparator<EquDetailSta>() {
				@Override
				public int compare(EquDetailSta o1, EquDetailSta o2) {
					if (o1.getEquDetail().getRowNo() > o2.getEquDetail().getRowNo()) {
						return 1;
					} else if (o1.getEquDetail().getRowNo() < o2.getEquDetail().getRowNo()) {
						return -1;
					} else if (o1.getColNo() > o2.getColNo()) {
						return 1;
					} else {
						return -1;
					}
				}
			});

			for (EquDetailSta sta : detailStaList) {
				if (curNum < 1) {
					break;
				}

				leftNum = sta.getCurNum();
				// 存在已预约订单时，可预约数量 = 当前数量 - 已预约数量
				if (equDetailStaMap.containsKey(sta.getId())) {
					leftNum = sta.getCurNum() - (int) equDetailStaMap.get(sta.getId()).getSum();
				}
				cabinet = sta.getEquDetail().getEquSetting();
				cabinetId = cabinet.getId();
				rowNo = String.valueOf(sta.getEquDetail().getRowNo());
				plcIp = String.valueOf(sta.getEquDetail().getIp());
				colNo = String.valueOf(sta.getColNo());
				cabinetMap = matMap.get(entry.getKey());
				if (cabinetMap != null) {
					rowMap = cabinetMap.get(cabinetId);
					if (rowMap != null) {
						plcMap = rowMap.get(plcIp);
						if (plcMap != null) {
							columnMap = plcMap.get(colNo);
							if (columnMap == null) {
								columnMap = new HashMap<>();
							}
						}
					}
				} else {
					cabinetMap = new HashMap<>();
					rowMap = new HashMap<>();
					columnMap = new HashMap<>();
					plcMap = new HashMap<>();
				}

				// 当前存量大于提取数量
				if (leftNum >= curNum) {
					orderMatDetail = new PhoneOrderMatDetail(orderMat.getId(), cabinetId, sta.getId(),
							Integer.valueOf(rowNo), Integer.valueOf(colNo), curNum);
					columnMap.put(colNo, curNum);
					plcMap.put(plcIp, columnMap);
					rowMap.put(rowNo, plcMap);
					cabinetMap.put(cabinetId, rowMap);
					matMap.put(entry.getKey(), cabinetMap);
					curNum = 0;
				} else { // 空间存量不足则取全部存量
					curNum -= leftNum;
					orderMatDetail = new PhoneOrderMatDetail(orderMat.getId(), cabinetId, sta.getId(),
							Integer.valueOf(rowNo), Integer.valueOf(colNo), leftNum);
					columnMap.put(colNo, leftNum);
					plcMap.put(plcIp, columnMap);
					rowMap.put(rowNo, plcMap);
					cabinetMap.put(cabinetId, rowMap);
					matMap.put(entry.getKey(), cabinetMap);
				}
				GlobalUserUtil.writeMsg(cabinet.getId(), MsgType.PRINTSCAN,
						new PrintScan("accountId:" + accountId + "<<" + sta.getId()));
				logger.info("通知柜子上APP扫描仪启动");
				this.phoneOrderMatDetailRepository.save(orderMatDetail);
			}
			obj = JSONObject.fromObject(matMap.get(entry.getKey()));
			orderMat.setOrderMatText(obj.toString());
			orderMat.setOrderMatCode(QrCreator.getInstance(this.toolProperties.getTmpUploadPath())
					.convertQrToBase64Data(orderMat.getOrderMatText()));
		}
		// 回写订单二维码信息
		Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>>> orderMap = new HashMap<>();
		String key = ScanCodeTypeEnum.APP.getCode() + phoneOrder.getId() + "_" + accountId;
		orderMap.put(key, matMap);
		obj = JSONObject.fromObject(orderMap);
		phoneOrder.setOrderText(obj.toString());
		phoneOrder.setOrderCode(QrCreator.getInstance(this.toolProperties.getTmpUploadPath())
				.convertQrToBase64Data(phoneOrder.getOrderText()));

		return tip;
	}

	@Override
	public List<AppOrderItem> reOderAppOverdueOrder(String orderId) {
		List<AppOrderItem> list = new ArrayList<>();
		PhoneOrder phoneOrder;
		List<EquDetailSta> detailStaList;
		int curNum = 0;
		String accountId = null;
		Map<String, Integer> detailMap = new HashMap<>(); // 历史订单--物料/数量
		phoneOrder = this.phoneOrderRepository.getOne(orderId);
		accountId = phoneOrder.getAccountId();
		// 查询订单详情
		List<PhoneOrderMat> orderMatList = this.phoneOrderMatRepository.findPhoneOrderMatsByPhoneOrderId(orderId);
		for (PhoneOrderMat orderMat : orderMatList) {
			detailMap.put(orderMat.getMatInfoId(),
					(detailMap.get(orderMat.getMatInfoId()) == null ? 0 : detailMap.get(orderMat.getMatInfoId()))
							+ orderMat.getNum());
		}

		// 获取20分钟内已预约物料详情集合
		Map<String, IntSummaryStatistics> equDetailMap = getEquDetailStaMap();

		List<EquDetailSta> equDetailStaList = this.equDetailStaService.getEquDetailStaListByCabinetId(null);

		// 物料->柜子存储详情
		Map<String, List<EquDetailSta>> detailStaMap = equDetailStaList.stream().filter(
				sta -> UsingStatus.ENABLE.equals(sta.getStatus()) && sta.getCurNum() != null && sta.getCurNum() > 0)
				.collect(Collectors.groupingBy(EquDetailSta::getMatInfoId));
		// 添加订单号
		phoneOrder = new PhoneOrder(accountId);
		phoneOrder = this.phoneOrderRepository.save(phoneOrder);
		MatEquInfo matInfo = null;

		PhoneOrderMat orderMat = null;
		PhoneOrderMatDetail orderMatDetail = null;

		// 物料/柜子/行号/列号,数量
		Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>> matMap = new HashMap<>();
		Map<String, Map<String, Map<String, Map<String, Integer>>>> cabinetMap; // 柜子标识
		Map<String, Map<String, Map<String, Integer>>> rowMap; // 行标识
		Map<String, Map<String, Integer>> plcMap = null; // plc板标识
		Map<String, Integer> columnMap = null; // 列标识

		EquSetting cabinet = null;
		String cabinetId; // 柜子Id
		String rowNo; // 行号
		String plcIp; // PLC板IP
		String colNo; // 列号
		int leftNum = 0; // 剩余数量

		JSONObject obj = null;

		// 遍历全部订单项
		for (Entry<String, Integer> entry : detailMap.entrySet()) {
			// 获取当前物料存储详情
			detailStaList = detailStaMap.get(entry.getKey());
			curNum = entry.getValue();
			orderMat = this.phoneOrderMatRepository.save(new PhoneOrderMat(phoneOrder.getId(), entry.getKey(), curNum));

			// 按取料优先顺排序
			Collections.sort(detailStaList, new Comparator<EquDetailSta>() {
				@Override
				public int compare(EquDetailSta o1, EquDetailSta o2) {
					if (o1.getEquDetail().getRowNo() > o2.getEquDetail().getRowNo()) {
						return 1;
					} else if (o1.getEquDetail().getRowNo() < o2.getEquDetail().getRowNo()) {
						return -1;
					} else if (o1.getColNo() > o2.getColNo()) {
						return 1;
					} else {
						return -1;
					}
				}
			});

			for (EquDetailSta sta : detailStaList) {
				matInfo = sta.getMatInfo();

				if (curNum < 1) {
					break;
				}

				leftNum = sta.getCurNum();
				// 存在已预约订单时，可预约数量 = 当前数量 - 已预约数量
				if (equDetailMap.containsKey(sta.getId())) {
					leftNum = sta.getCurNum() - (int) equDetailMap.get(sta.getId()).getSum();
				}
				cabinet = sta.getEquDetail().getEquSetting();
				cabinetId = cabinet.getId();
				rowNo = String.valueOf(sta.getEquDetail().getRowNo());
				plcIp = String.valueOf(sta.getEquDetail().getIp());
				colNo = String.valueOf(sta.getColNo());
				cabinetMap = matMap.get(entry.getKey());
				if (cabinetMap != null) {
					rowMap = cabinetMap.get(cabinetId);
					if (rowMap != null) {
						plcMap = rowMap.get(rowNo);
						if (plcMap != null) {
							columnMap = plcMap.get(colNo);
							if (columnMap == null) {
								columnMap = new HashMap<>();
							}
						}
					}
				} else {
					cabinetMap = new HashMap<>();
					rowMap = new HashMap<>();
					columnMap = new HashMap<>();
					plcMap = new HashMap<>();
				}

				// 当前存量大于提取数量
				if (leftNum >= curNum) {
					orderMatDetail = new PhoneOrderMatDetail(orderMat.getId(), cabinetId, sta.getId(),
							Integer.valueOf(rowNo), Integer.valueOf(colNo), curNum);
					columnMap.put(colNo, curNum);
					plcMap.put(plcIp, columnMap);
					rowMap.put(rowNo, plcMap);
					cabinetMap.put(cabinetId, rowMap);
					matMap.put(entry.getKey(), cabinetMap);
					curNum = 0;
				} else { // 空间存量不足则取全部存量
					curNum -= leftNum;
					orderMatDetail = new PhoneOrderMatDetail(orderMat.getId(), cabinetId, sta.getId(),
							Integer.valueOf(rowNo), Integer.valueOf(colNo), leftNum);
					columnMap.put(colNo, leftNum);
					plcMap.put(plcIp, columnMap);
					rowMap.put(rowNo, plcMap);
					cabinetMap.put(cabinetId, rowMap);
					matMap.put(entry.getKey(), cabinetMap);
				}
				GlobalUserUtil.writeMsg(cabinet.getId(), MsgType.PRINTSCAN,
						new PrintScan("accountId:" + accountId + "<<" + sta.getId()));
				logger.info("通知柜子上APP扫描仪启动");
				this.phoneOrderMatDetailRepository.save(orderMatDetail);
			}
			obj = JSONObject.fromObject(matMap.get(entry.getKey()));
			orderMat.setOrderMatText(obj.toString());
			orderMat.setOrderMatCode(QrCreator.getInstance(this.toolProperties.getTmpUploadPath())
					.convertQrToBase64Data(orderMat.getOrderMatText()));
			list.add(new AppOrderItem(orderMat.getId(), entry.getKey(), matInfo.getMatEquName(),
					cabinet.getEquSettingName(), 20, orderMat.getOrderMatCode(), matInfo.getStorePrice(), "0", curNum,
					matInfo.getIcon() == null ? "" : this.toolProperties.getImgUrlPath() + matInfo.getIcon()));
		}
		// 回写订单二维码信息
		Map<String, Map<String, Map<String, Map<String, Map<String, Map<String, Integer>>>>>> orderMap = new HashMap<>();
		String key = ScanCodeTypeEnum.APP.getCode() + phoneOrder.getId() + "_" + accountId;
		orderMap.put(key, matMap);
		obj = JSONObject.fromObject(orderMap);
		phoneOrder.setOrderText(obj.toString());
		phoneOrder.setOrderCode(QrCreator.getInstance(this.toolProperties.getTmpUploadPath())
				.convertQrToBase64Data(phoneOrder.getOrderText()));

		return list;
	}

	@Override
	@Transactional
	public void shoppingInsert(String userId, String knifeId, int shoppingTotal) {
		// 判断购物车是否存在同类产品,存在则添加,否则添加新刀具到购物车
		List<PhoneCart> cartList = this.phoneCartRepository.getShoppingList(userId, knifeId);
		if (cartList != null && cartList.size() < 1) {
			this.phoneCartRepository.save(new PhoneCart(userId, knifeId, shoppingTotal));
		} else {
			PhoneCart cart = cartList.get(0);
			cart.setNum(cart.getNum() + shoppingTotal);
			this.phoneCartRepository.saveAndFlush(cart);
		}
	}

	@Override
	@Transactional
	public void shoppingDelete(String shoppingId) {
		PhoneCart cart = this.phoneCartRepository.findOne(shoppingId);
		if (cart != null) {
			this.phoneCartRepository.delete(cart);
		}
	}

	@Override
	public List<AppKnifes> getAppKnifesByKind(String kindId) {
		List<AppKnifes> appSubscribeList = new ArrayList<>();
		List<EquDetailSta> staList = this.equDetailStaRepository.findAll();
		// 过滤状态启用与剩余数量大于0
		staList = staList.stream()
				.filter((EquDetailSta sta) -> UsingStatus.ENABLE.equals(sta.getStatus()) && sta.getCurNum() > 0)
				.collect(Collectors.toList());
		// 过滤类型
		if (kindId != null && !"".equals(kindId)) {
			staList = staList.stream().filter((EquDetailSta sta) -> sta.getMatInfoId() != null
					&& !"".equals(sta.getMatInfoId())).collect(Collectors.toList());
		}
		for (EquDetailSta sta : staList) {
			appSubscribeList.add(new AppKnifes(sta.getMatInfoId(), sta.getMatInfo().getBarCode(),
					sta.getMatInfo().getMatEquName(), sta.getMatInfo().getStorePrice(), sta.getId(), sta.getCurNum(),
					this.toolProperties.getImgUrlPath() + sta.getMatInfo().getIcon(), sta.getMatInfo().getSpec()));
		}
		return appSubscribeList;
	}

	@Override
	@Transactional
	public void shoppingUpdate(String shoppingId, int shopingTotal) {
		PhoneCart cart = this.phoneCartRepository.findOne(shoppingId);
		if (cart != null) {
			cart.setNum(shopingTotal);
			this.phoneCartRepository.saveAndFlush(cart);
		}
	}

	@Override
	public List<AppCart> getShoppingList(String userId) {
		List<PhoneCart> cartList = this.phoneCartRepository.getShoppingList(userId);
		List<AppCart> list = new ArrayList<>();
		for (PhoneCart cart : cartList) {
			list.add(new AppCart(cart.getId(), cart.getMatId(), cart.getMat().getMatEquName(),
					cart.getMat().getStorePrice(), "", 55, cart.getNum(),
					this.toolProperties.getImgUrlPath() + cart.getMat().getIcon(), cart.getMat().getSpec()));
		}
		return list;
	}

	@Override
	public void removePurchase(String purchaseId) {
		// 删除手机订单物料信息相关详情
		this.phoneOrderMatDetailRepository
				.deleteInBatch(this.phoneOrderMatDetailRepository.findDetailListByOrderMatId(purchaseId));
		// 处理手机订单物料信息
		PhoneOrderMat orderMat = this.phoneOrderMatRepository.getOne(purchaseId);
		String orderId = orderMat.getPhoneOrderId(); // 订单序号
		String matId = orderMat.getMatInfoId(); // 物料序号
		this.phoneOrderMatRepository.delete(orderMat);
		// 当当前订单项为订单末项时，取消整个订单
		List<PhoneOrderMat> orderMatList = this.phoneOrderMatRepository.findPhoneOrderMatsByPhoneOrderId(orderId);
		if (orderMatList == null || orderMatList.size() < 1) {
			this.phoneOrderRepository.delete(this.phoneOrderRepository.getOne(orderId));
		} else {
			PhoneOrder order = this.phoneOrderRepository.getOne(orderId);
			JSONObject obj = JSONObject.fromObject(order.getOrderText());
			obj = JSONObject.fromObject(((JSONObject) obj.get(orderId)).discard(matId));
			JSONObject orderObj = new JSONObject();
			orderObj.put(orderId, obj);
			order.setOrderText(orderObj.toString());
			order.setOrderCode(QrCreator.getInstance(this.toolProperties.getTmpUploadPath())
					.convertQrToBase64Data(order.getOrderText()));
		}
	}

	@Override
	public void removeOrder(String orderId) {
		this.phoneOrderMatDetailRepository
				.deleteInBatch(this.phoneOrderMatDetailRepository.findPhoneOrderMatDetailsByPhoneOrderId(orderId));
		this.phoneOrderMatRepository
				.deleteInBatch(this.phoneOrderMatRepository.findPhoneOrderMatsByPhoneOrderId(orderId));
		this.phoneOrderRepository.delete(this.phoneOrderRepository.getOne(orderId));
	}

	@Override
	public AppRecycleReview recycleInfo(String code) {
		AppRecycleReview appRecycleReview = new AppRecycleReview();
		AppBorrowInfo appBorrowInfo = new AppBorrowInfo();
		// 状态设定
		appRecycleReview.setStatus(true);
		// 返回信息设定
		appRecycleReview.setMsg("信息查询成功");

		// 物料归还信息取得
		MatReturnBack matReturnBack = this.matReturnBackRepository.findById(code);

		if (matReturnBack != null) {
			appBorrowInfo = new AppBorrowInfo(matReturnBack.getMatUseBill().getMatUseRecord().getBarCode(),
					matReturnBack.getMatUseBill().getMatUseRecord().getMatInfoName(),
					matReturnBack.getMatUseBill().getMatUseRecord().getUserName(),
					String.valueOf(matReturnBack.getMatUseBill().getOpDate()),
					String.valueOf(matReturnBack.getMatUseBill().getMatUseRecord().getPackNum()), "", "支");
		} else {
			// 状态设定
			appRecycleReview.setStatus(false);
			// 返回信息设定
			appRecycleReview.setMsg("查询无此信息");
		}
		// 待审核刀具信息设定
		appRecycleReview.setData(appBorrowInfo);

		return appRecycleReview;
	}

	@Override
	public AppRecycleResult recycleReject(String code, String type, int num, String content, String userId) {

		AppRecycleResult appRecycleResult = new AppRecycleResult();

		try {
			MatReturnBack matReturnBack = matReturnBackRepository.findById(code);
			matReturnBack.setConfirmUser(this.userRepository.findUserByAccountId(userId).getUserName());
			matReturnBack.setConfirmMode(VerifyMode.APPCONF);
			matReturnBack.setConfirmDate(new Date());
			matReturnBack.setAuditStatus(AuditStatus.NOT_PASS);
			matReturnBack.setNum(num);
			matReturnBack.setRemark(content);
			this.matReturnBackRepository.saveAndFlush(matReturnBack);

			// 状态设定
			appRecycleResult.setStatus(true);
			// 返回信息设定
			appRecycleResult.setMsg("回收审核处理成功");
			return appRecycleResult;
		} catch (Exception e) {
			e.printStackTrace();
			// 状态设定
			appRecycleResult.setStatus(false);
			// 返回信息设定
			appRecycleResult.setMsg("回收审核处理异常");
			return appRecycleResult;
		}
	}

	@Override
	public AppRecycleResult recyclePass(String code, String userId) {

		AppRecycleResult appRecycleResult = new AppRecycleResult();

		try {
			int returnBackNo = 0;
			if(code.length() == 8 && (code.indexOf("M") == 0 || code.indexOf("S") == 0)) {
				returnBackNo = Integer.parseInt(code.substring(1)); 
			}
			List<MatReturnBack> backList = matReturnBackRepository.findBybarCode(code, returnBackNo);
			for(MatReturnBack matReturnBack : backList) {
				matReturnBack.setConfirmUser(this.userRepository.findUserByAccountId(userId).getUserName());
				matReturnBack.setConfirmMode(VerifyMode.APPCONF);
				matReturnBack.setConfirmDate(new Date());
				matReturnBack.setAuditStatus(AuditStatus.PASS);
				matReturnBack.setNum(matReturnBack.getMatUseBill().getMatUseRecord().getPackNum());
				matReturnBack.setRemark(null);
				this.matReturnBackRepository.saveAndFlush(matReturnBack);
			}
			// 状态设定
			appRecycleResult.setStatus(true);
			// 返回信息设定
			appRecycleResult.setMsg("回收审核处理成功");
			return appRecycleResult;
		} catch (Exception e) {
			e.printStackTrace();
			// 状态设定
			appRecycleResult.setStatus(false);
			// 返回信息设定
			appRecycleResult.setMsg("回收审核处理异常");
			return appRecycleResult;
		}

	}

	// 获取20分钟以内预约订单
	private Map<String, IntSummaryStatistics> getEquDetailStaMap() {

		// 获取已预约物料详情集合
		List<PhoneOrderMatDetail> phoneOrderMatDetailList = this.equDetailStaService.getPhoneOrderMatDetail();
		// 获取当前时间的前20分钟
		Calendar beforeTime = Calendar.getInstance();
		beforeTime.add(Calendar.MINUTE, -20);
		Date date = beforeTime.getTime();

		// 过滤20分钟以内数据，并按照物料ID分组
		Map<String, IntSummaryStatistics> phoneOrdermMap = phoneOrderMatDetailList.stream()
				.filter(detail -> detail.getPhoneOrderMat().getPhoneOrder().getOrderCreatedTime().after(date))
				.collect(Collectors.groupingBy(PhoneOrderMatDetail::getEquDetailStaId,
						Collectors.summarizingInt(PhoneOrderMatDetail::getNum)));

		return phoneOrdermMap;
	}

	@Override
	public List<AppMatInfoType> getAppTypeList() {
		// TODO Auto-generated method stub
		return null;
	}
}