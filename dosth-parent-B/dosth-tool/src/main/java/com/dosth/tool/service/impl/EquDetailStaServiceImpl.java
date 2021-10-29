package com.dosth.tool.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cnbaosi.dto.ApiFeignResponse;
import com.cnbaosi.dto.tool.FeignCabinet;
import com.cnbaosi.dto.tool.FeignMat;
import com.cnbaosi.dto.tool.FeignStaDetail;
import com.cnbaosi.dto.tool.FeignStock;
import com.cnbaosi.dto.tool.FeignStockMat;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.util.FileUtil;
import com.dosth.dto.StockDetail;
import com.dosth.dto.StockTip;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.BorrowType;
import com.dosth.tool.common.state.EquSta;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.mobile.PhoneOrderMatDetail;
import com.dosth.tool.entity.vo.EquDetailStaVo;
import com.dosth.tool.repository.EquDetailRepository;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.FeedingDetailRepository;
import com.dosth.tool.repository.FeedingListRepository;
import com.dosth.tool.repository.PhoneOrderMatDetailRepository;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.LowerFrameQueryService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.toolcabinet.dto.FeedDetailSta;
import com.dosth.util.DateUtil;
import com.dosth.util.OpTip;

/**
 * 设备详情Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class EquDetailStaServiceImpl implements EquDetailStaService {

	private static final Logger logger = LoggerFactory.getLogger(EquDetailStaServiceImpl.class);

	@Autowired
	private LowerFrameQueryService lowerFrameQueryService;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private FeedingDetailRepository feedingDetailRepository;
	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private FeedingListRepository feedingListRepository;
	@Autowired
	private PhoneOrderMatDetailRepository phoneOrderMatDetailRepository;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private EquDetailRepository equDetailRepository;

	@Override
	public void save(EquDetailSta entity) throws DoSthException {
		this.equDetailStaRepository.save(entity);
	}

	@Override
	public EquDetailSta get(Serializable id) throws DoSthException {
		return this.equDetailStaRepository.findOne(id);
	}

	@Override
	public EquDetailSta update(EquDetailSta entity) throws DoSthException {
		return this.equDetailStaRepository.saveAndFlush(entity);
	}

	@Override
	public void delete(EquDetailSta entity) throws DoSthException {
		this.equDetailStaRepository.delete(entity);
	}

	@Override
	public OpTip storage(EquDetailSta equDetailSta, String accountId) throws DoSthException {
		OpTip tip = new OpTip(200, "设置成功！");
		EquDetailSta tmpSta = get(equDetailSta.getId());

		tmpSta.setMaxReserve(equDetailSta.getMaxReserve());
		tmpSta.setWarnVal(equDetailSta.getWarnVal());
		tmpSta.setLastFeedTime(new Date());

		this.equDetailStaRepository.saveAndFlush(tmpSta);
		return tip;
	}

	@Override
	public List<EquDetailSta> getEquDetailStaListByCabinetId(String cabinetId) throws DoSthException {
		if (cabinetId == null || "".equals(cabinetId)) {
			return this.equDetailStaRepository.findAll();
		}
		return this.equDetailStaRepository.getEquDetailStaListBySettingId(cabinetId);
	}

	@Override
	public List<PhoneOrderMatDetail> getPhoneOrderMatDetail() throws DoSthException {
		return this.phoneOrderMatDetailRepository.findAll();
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Map<String, Integer> getLatticeValueMap(String cabinetId) throws DoSthException {
		Map<String, Integer> latticeValueMap = new HashMap<>();
		List<EquDetailSta> staList = this.equDetailStaRepository.getEquDetailStaListBySettingId(cabinetId);
		staList.stream().filter((EquDetailSta s) -> s.getStatus().equals(UsingStatus.ENABLE)).forEach(sta -> {
			latticeValueMap.put(sta.getId(), sta.getCurNum());
		});

		return latticeValueMap;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<EquDetailSta> getStaListByDetailId(String equDetailId) throws DoSthException {
		return this.equDetailStaRepository.getStaListByDetailId(equDetailId);
	}

	@Override
	public List<MatEquInfo> getMatInfoListByCabinetId(String cabinetId) throws DoSthException {
		return this.equDetailStaRepository.getMatInfoListByCabinetId(cabinetId, UsingStatus.ENABLE);
	}

	public static String getTimeStamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	@Override
	public OpTip feedList(String arrs, String equSettingId, String accountId, FeedType feedType) throws IOException {
		OpTip tip = new OpTip(200, "生成清单成功！");
		String[] arrsList = arrs.split(";");
		String[] staIdNum;
		EquDetailSta equDetailSta;
		List<FeedingDetail> feedingDetailList = null;
		for (String arrList : arrsList) {
			staIdNum = arrList.split(",");
			feedingDetailList = this.feedingDetailRepository.getNoFinishFeedingDetailListByEquDetialStaId(staIdNum[0]);
			if (feedingDetailList != null && feedingDetailList.size() > 0) {
				equDetailSta = this.equDetailStaRepository.findOne(staIdNum[0]);
				tip.setCode(204);
				tip.setMessage(
						"F" + equDetailSta.getEquDetail().getRowNo() + "-" + equDetailSta.getColNo() + "已经存在未完成的补料单");
				return tip;
			}
		}
		// 托盘ID获取柜子Id
		if (equSettingId.startsWith("D_")) {
			EquDetail detail = this.equDetailRepository.getOne(equSettingId.substring(2));
			equSettingId = detail.getEquSettingId();
		}
		FeedingList feedingList = new FeedingList(equSettingId, DateUtil.getAllTime(), accountId, YesOrNo.NO, feedType);
		feedingList = this.feedingListRepository.save(feedingList);
		for (String arrList : arrsList) {
			staIdNum = arrList.split(",");
			equDetailSta = this.equDetailStaRepository.findOne(staIdNum[0]);
			this.feedingDetailRepository.save(new FeedingDetail(feedingList.getId(), equDetailSta.getId(),
					equDetailSta.getMatInfo().getId(), Integer.valueOf(staIdNum[1])));
		}
		return tip;
	}

	@Override
	public List<FeedDetailSta> getFeedDetailStaList(String cabinetId, String query, String search) {
		List<EquDetailSta> staList = this.equDetailStaRepository.getEquDetailStaListBySettingId(query);
		List<FeedingDetail> feedingDetailList = this.feedingDetailRepository.getWaitFeedingDetailList(query);

		Map<String, IntSummaryStatistics> feedingDetailMap = null;
		if (feedingDetailList != null && feedingDetailList.size() > 0) {
			feedingDetailMap = feedingDetailList.stream().collect(Collectors.groupingBy(
					FeedingDetail::getEquDetailStaId, Collectors.summarizingInt(FeedingDetail::getFeedingNum)));
		}

		List<FeedDetailSta> list = new ArrayList<>();
		MatEquInfo mat = null;
		String pattern = "^F[1-9]{1}-([1-9]|10)$";
		String[] row_col;
		Collections.sort(staList, new Comparator<EquDetailSta>() {
			@Override
			public int compare(EquDetailSta o1, EquDetailSta o2) {
				if (o1.getEquDetail().getRowNo() > o2.getEquDetail().getRowNo()) {
					return -1;
				} else if (o1.getEquDetail().getRowNo() < o2.getEquDetail().getRowNo()) {
					return 1;
				} else if (o1.getColNo() > o2.getColNo()) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		for (EquDetailSta sta : staList) {
			if (UsingStatus.DISABLE.equals(sta.getStatus()) || sta.getMatInfoId() == null
					|| "".equals(sta.getMatInfoId())) {
				continue;
			}
			mat = sta.getMatInfo();
			if (search != null && !"".equals(search)) {
				if (Pattern.matches(pattern, search)) { // 匹配位置过滤位置条件,如F1-10
					row_col = search.substring(1).split("-");
					if (row_col[0].indexOf(String.valueOf(sta.getEquDetail().getRowNo())) == -1
							|| !String.valueOf(sta.getColNo()).equals(row_col[1])) {
						continue;
					}
				} else if (!mat.getMatEquName().toLowerCase().contains(search.toLowerCase())
						&& !mat.getBarCode().toLowerCase().contains(search.toLowerCase())
						&& !mat.getSpec().toLowerCase().contains(search.toLowerCase())) {
					continue;
				}
			}
			list.add(new FeedDetailSta(sta.getId(), sta.getMatInfoId(),
					"F" + sta.getEquDetail().getRowNo() + "-" + sta.getColNo(),
					this.toolProperties.getImgUrlPath() + mat.getIcon(), mat.getMatEquName(), mat.getBarCode(),
					mat.getSpec(), mat.getNum(), mat.getPackUnit(), sta.getCurNum(),
					Integer.valueOf(String.valueOf(feedingDetailMap != null && feedingDetailMap.get(sta.getId()) != null
							? feedingDetailMap.get(sta.getId()).getSum()
							: 0)),
					sta.getMaxReserve()));
		}
		return list;
	}

	@Override
	public List<EquDetailSta> getEquDetailStaTreeListByCabinetId(String cabinetId) throws DoSthException {
		if (cabinetId == null || "".equals(cabinetId)) {
			return this.equDetailStaRepository.findAll();
		}
		return this.equDetailStaRepository.getEquDetailStaTreeListBySettingId(cabinetId);
	}

	@Override
	public OpTip upFrame(String equDetailStaId, String matInfoId, String accountId) {
		OpTip tip = new OpTip(200, "上架成功！");
		EquDetailSta equDetailSta = get(equDetailStaId);
		if (equDetailSta.getMatInfoId() != null) {
			if (equDetailSta.getCurNum() > 0) {
				tip = new OpTip(201, "物料未消耗完，请先下架！");
				return tip;
			} else {
				equDetailSta.setMatInfoId(matInfoId);
				equDetailSta.setLastFeedTime(new Date());
			}
		} else {
			equDetailSta.setMatInfoId(matInfoId);
			equDetailSta.setLastFeedTime(new Date());
		}

		this.equDetailStaRepository.saveAndFlush(equDetailSta);
		return tip;
	}

	@Override
	public OpTip upFrameAll(String accountId, String cabinetId, List<String> list) {
		OpTip tip = new OpTip(200, "批量上架成功");
		List<EquDetailSta> equDetailStaList = this.equDetailStaRepository.getEmptyNumByCabinetId(cabinetId);

		if (equDetailStaList == null || equDetailStaList.size() == 0) {
			tip = new OpTip(201, "没有空闲货位");
			return tip;
		}

		if (list.size() > equDetailStaList.size()) {
			tip = new OpTip(201, "物料种类 " + list.size() + "，空闲货位 " + equDetailStaList.size() + "</br>上架数量大于空闲货位");
			return tip;
		}

		for (int i = 0; i < list.size(); i++) {
			for (int j = i; j <= i; j++) {
				equDetailStaList.get(i).setMatInfoId(list.get(i));
			}
		}

		for (EquDetailSta sta : equDetailStaList) {
			this.equDetailStaRepository.saveAndFlush(sta);
		}

		return tip;
	}

	@Override
	public Boolean equStaProblem(String staId) {
		EquDetailSta sta = this.equDetailStaRepository.getOne(staId);
		// 设置马达位置为开路故障
		if (sta != null) {
			sta.setEquSta(EquSta.OPENFAULT);
			this.equDetailStaRepository.saveAndFlush(sta);
			return true;
		}
		return false;
	}

	@Override
	public Boolean resetProblem(String cabinetId) {
		List<EquDetailSta> staList = this.equDetailStaRepository.getEquDetailStaListBySettingId(cabinetId);
		staList.stream().filter(
				(EquDetailSta s) -> s.getStatus().equals(UsingStatus.ENABLE) && !s.getEquSta().equals(EquSta.NONE))
				.forEach(sta -> {
					sta.setEquSta(EquSta.NONE);
				});
		return true;
	}

	@Override
	public OpTip curNumCheck(String staId, int num, String cabinetId) {
		OpTip tip = new OpTip(200, "库存充足");
		int curNum = this.equDetailStaRepository.curNumCheck(staId, cabinetId);
		if (curNum < num) {
			tip = new OpTip(201, "库存不足");
		}
		return tip;
	}

	@Override
	public List<EquDetailSta> getStaListByCabinetId(String cabinetId) {
		List<EquDetailSta> staList = this.equDetailStaRepository.getStaListByCabinetId(cabinetId);
		return staList;
	}

	@Override
	public OpTip matIn(String matIds, String staIds) {
		StringBuilder result = new StringBuilder();
		String[] matIdArr = matIds.split(";");
		String[] staIdArr = staIds.split(";");

		MatEquInfo mat;
		for (String matId : matIdArr) {
			mat = this.matEquInfoService.get(matId);
			if (mat != null && UsingStatus.DISABLE.equals(mat.getStatus())) {
				return new OpTip(201, "物料【" + mat.getMatEquName() + "】已禁用，上架失败");
			}
		}

		int matIndex = 0;
		EquDetailSta sta;
		for (String staId : staIdArr) {
			if (matIndex == matIdArr.length) {
				matIndex = 0;
			}
			sta = this.equDetailStaRepository.getOne(staId);
			sta.setMatInfoId(matIdArr[matIndex]);
			this.equDetailStaRepository.saveAndFlush(sta);
			result.append(staId);
			result.append(",");
			result.append(matIdArr[matIndex]);
			result.append(";");
			matIndex++;
		}
		OpTip opTip = new OpTip(200, result.toString());
		return opTip;
	}

	@Override
	public OpTip matOut(String staIds, String accountId, String cabinetId) {
		List<String> staIdList = Arrays.asList(staIds.split(";"));
		return this.lowerFrameQueryService.lowMainFrame(staIdList, accountId, cabinetId);
	}

	@Override
	public List<StockTip> getStockTip(String mainCabinetId) {
		List<EquDetailSta> staList = this.equDetailStaRepository.getEquDetailStaTreeListBySettingId(mainCabinetId);
		Map<MatEquInfo, List<EquDetailSta>> map = staList.stream()
				.filter(sta -> sta.getMatInfoId() != null && !"".equals(sta.getMatInfoId()))
				.collect(Collectors.groupingBy(EquDetailSta::getMatInfo));
		int curNum;
		StockTip tip;
		List<StockTip> tipList = new ArrayList<>();
		List<StockDetail> detailList;
		// 按物料低于物料安全库存，加载所有格子情况
		for (Entry<MatEquInfo, List<EquDetailSta>> entry : map.entrySet()) {
			staList = entry.getValue();
			curNum = staList.stream().mapToInt(EquDetailSta::getCurNum).sum();
			if (curNum <= entry.getKey().getLowerStockNum()) {
				detailList = new ArrayList<>();
				for (EquDetailSta sta : staList) {
					detailList.add(new StockDetail(sta.getEquDetail().getRowNo(), sta.getColNo(), sta.getCurNum()));
				}
				tip = new StockTip();
				tip.setName(entry.getKey().getMatEquName());
				tip.setDetailList(detailList);
				tipList.add(tip);
			}
		}
		return tipList;
	}

	@Override
	public Map<String, Integer> getMatNumMapByCabinetId(String cabinetId) {
		List<EquDetailSta> staList = this.equDetailStaRepository.getEquDetailStaListByCabinetId(cabinetId);
		Map<MatEquInfo, IntSummaryStatistics> collect = staList.stream()
				.filter(sta -> sta.getStatus().equals(UsingStatus.ENABLE) && sta.getCurNum() > 0).collect(Collectors
						.groupingBy(EquDetailSta::getMatInfo, Collectors.summarizingInt(EquDetailSta::getCurNum)));
		Map<String, Integer> map = new HashMap<>();
		for (Entry<MatEquInfo, IntSummaryStatistics> entry : collect.entrySet()) {
			map.put(entry.getKey().getId(),
					(BorrowType.METER.equals(entry.getKey().getBorrowType()) ? entry.getKey().getNum() : 1)
							* Integer.valueOf(String.valueOf(entry.getValue().getSum())));
		}
		return map;
	}

	@Override
	public ApiFeignResponse<FeignStock> getStockDetail(String wareHouseAlias) {
		ApiFeignResponse<FeignStock> response = new ApiFeignResponse<>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage("获取物料库存成功");
		try {
			Map<MatEquInfo, List<EquDetailSta>> map = getStaListGroupByMat(wareHouseAlias);
			List<EquDetailSta> list;
			List<FeignStock> stockList = new ArrayList<>();
			MatEquInfo mat;
			FeignStock stock;
			for (Entry<MatEquInfo, List<EquDetailSta>> entry : map.entrySet()) {
				mat = entry.getKey();
				list = entry.getValue();
				stock = new FeignStock();
				stock.setFeignStockMat(new FeignStockMat(mat.getBarCode(), mat.getMatEquName(), mat.getSpec(),
						mat.getNum(), mat.getBrand()));
				stock.setMaxStockNum(list.stream().mapToInt(EquDetailSta::getMaxReserve).sum());
				stock.setCurStockNum(list.stream().mapToInt(EquDetailSta::getCurNum).sum());
				stock.setSupplyNum(stock.getMaxStockNum() - stock.getCurStockNum());
				stockList.add(stock);
			}
			response.setResultList(stockList);
		} catch (Exception e) {
			response.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setMessage("获取物料库存失败");
		}
		return response;
	}

	@Override
	public Map<MatEquInfo, List<EquDetailSta>> getStaListGroupByMat(String wareHouseAlias) {
		List<EquDetailSta> staList = this.equDetailStaRepository.getStaListByWareHouseAlias(wareHouseAlias);
		staList = staList.stream().filter(sta -> sta.getMatInfoId() != null && !"".equals(sta.getMatInfoId()))
				.collect(Collectors.toList());
		Map<MatEquInfo, List<EquDetailSta>> map = staList.stream()
				.filter(sta -> sta.getMatInfoId() != null && !"".equals(sta.getMatInfoId()))
				.collect(Collectors.groupingBy(EquDetailSta::getMatInfo));
		return map;
	}

	@Override
	public ApiFeignResponse<FeignCabinet> getStaStock() {
		ApiFeignResponse<FeignCabinet> response = new ApiFeignResponse<>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage("获取物料库存成功");

		try {
			List<FeignCabinet> results = new ArrayList<FeignCabinet>();
			List<EquSetting> equList = this.equSettingRepository.findAll();
			for (EquSetting equ : equList) {
				String equSettingId = equ.getId();

				// 查询待补数量
				List<FeedingDetail> feedingDetailList = this.feedingDetailRepository
						.getWaitFeedingDetailList(equSettingId);
				Map<String, IntSummaryStatistics> feedingDetailMap = null;
				if (feedingDetailList != null && feedingDetailList.size() > 0) {
					feedingDetailMap = feedingDetailList.stream().collect(Collectors.groupingBy(
							FeedingDetail::getEquDetailStaId, Collectors.summarizingInt(FeedingDetail::getFeedingNum)));
				}

				List<EquDetailSta> staList = getEquDetailStaListByCabinetId(equSettingId);
				if (CollectionUtils.isNotEmpty(staList)) {
					FeignCabinet feignCabinet = new FeignCabinet();

					// 柜子基础信息
					feignCabinet.setEquId(equSettingId);
					feignCabinet.setEquName(equ.getWareHouseAlias());
					feignCabinet.setRowNum(equ.getRowNum());
					feignCabinet.setColNum(equ.getColNum());

					List<FeignStaDetail> cellList = new ArrayList<FeignStaDetail>();
					for (EquDetailSta sta : staList) {
						Integer warnVal = sta.getWarnVal();
						Integer curNum = sta.getCurNum();

						EquDetail equDetail = sta.getEquDetail();
						MatEquInfo matInfo = sta.getMatInfo();

						// 待补数量
						int waitNum = Integer.valueOf(
								String.valueOf(feedingDetailMap != null && feedingDetailMap.get(sta.getId()) != null
										? feedingDetailMap.get(sta.getId()).getSum()
										: 0));

						// 物料信息
						FeignMat feignMat = new FeignMat();
						if (matInfo != null) {
							feignMat.setMatId(matInfo.getId());
							feignMat.setMatName(matInfo.getMatEquName());
							feignMat.setSpec(matInfo.getSpec());
							feignMat.setBarCode(matInfo.getBarCode());
							feignMat.setPackNum(matInfo.getNum());
							feignMat.setManufacturerName(
									matInfo.getManufacturer() != null ? matInfo.getManufacturer().getManufacturerName()
											: "");
							feignMat.setManufacturerNo(
									matInfo.getManufacturer() != null ? matInfo.getManufacturer().getManufacturerNo()
											: "");
						}

						// 单元格信息
						FeignStaDetail staInfo = new FeignStaDetail();
						if (equDetail != null) {
							staInfo.setRowNo(equDetail.getRowNo());
						}
						staInfo.setStaId(sta.getId());
						staInfo.setColNo(sta.getColNo());
						staInfo.setCurNum(curNum);
						staInfo.setWaitNum(waitNum);
						staInfo.setMaxReserve(sta.getMaxReserve());
						staInfo.setStatus(sta.getStatus().getMessage());
						staInfo.setWarnVal(warnVal);
						staInfo.setMatInfo(feignMat);

						cellList.add(staInfo);
					}
					feignCabinet.setStaList(cellList);
					results.add(feignCabinet);
				}
			}
			response.setResultList(results);
		} catch (Exception e) {
			response.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setMessage("获取物料库存失败");
		}
		return response;
	}

	@Override
	public List<EquDetailSta> getStaByRowCol(String wareHouseAlias, Integer rowNo, Integer colNo) {
		return this.equDetailStaRepository.getStaByRowCol(wareHouseAlias, rowNo, colNo);
	}

	@Override
	public void exportLastFeedingList(HttpServletRequest request, HttpServletResponse response) {
		List<FeedingList> list = this.feedingListRepository.findAll();
		list.sort((l1, l2) -> l1.getCreateTime().after(l2.getCreateTime()) ? -1 : 1);

		if (list != null && list.size() > 0) {
			HSSFWorkbook workbook = new HSSFWorkbook();
			OutputStream output = null;
			try {
				HSSFSheet sheet = workbook.createSheet("补料清单");

				sheet.setDefaultRowHeightInPoints(20);
				sheet.setDefaultColumnWidth(20);

				HSSFCellStyle cellStyle = workbook.createCellStyle();
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

				HSSFFont fontStyle = workbook.createFont();
				fontStyle.setFontName("宋体");
				cellStyle.setFont(fontStyle);

				FeedingList l = list.get(0);
				int rowIndex = 0;
				int colIndex = 0;
				HSSFRow row = sheet.createRow(rowIndex++);
				row.createCell(colIndex++).setCellValue("补料货体:" + l.getEquSetting().getEquSettingName());
				row.createCell(colIndex++).setCellValue("物料单号:" + l.getFeedingName());
				row.createCell(colIndex++).setCellValue("时间:" + DateUtil.getTime(l.getCreateTime()));
				row = sheet.createRow(rowIndex++);
				colIndex = 0;
				row.createCell(colIndex++).setCellValue("补料货位");
				row.createCell(colIndex++).setCellValue("物料名称");
				row.createCell(colIndex++).setCellValue("物料编号");
				row.createCell(colIndex++).setCellValue("物料型号");
				row.createCell(colIndex++).setCellValue("补充数量");

				List<FeedingDetail> detailList = this.feedingDetailRepository
						.getFeedingDetailListByFeedingListId(l.getId());
				MatEquInfo info;
				for (FeedingDetail detail : detailList) {
					row = sheet.createRow(rowIndex++);
					colIndex = 0;
					row.createCell(colIndex++).setCellValue("F" + detail.getEquDetailSta().getEquDetail().getRowNo()
							+ "-" + detail.getEquDetailSta().getColNo());
					info = detail.getMatInfo();
					if (info != null) {
						row.createCell(colIndex++).setCellValue(info.getMatEquName());
						row.createCell(colIndex++).setCellValue(info.getBarCode());
						row.createCell(colIndex++).setCellValue(info.getSpec());
					}
					row.createCell(colIndex++).setCellValue(detail.getFeedingNum());
				}
				String fileName = FileUtil.processFileName(request, "补料信息" + DateUtil.getDays());
				output = response.getOutputStream();
				response.reset();
				response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
				response.setContentType("application/msexcel");
				response.setCharacterEncoding("utf-8");
				workbook.write(output);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("生成清单失败:" + e.getMessage());
			} finally {
				if (output != null) {
					try {
						output.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (workbook != null) {
					try {
						workbook.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public List<EquDetailSta> getStaListByCabinetIdOrDetailId(String cabinetId, String staMatNameBarCodeSpec) {
		Criteria<EquDetailSta> criteria = new Criteria<>();
		if (staMatNameBarCodeSpec != null && !"".equals(staMatNameBarCodeSpec)) {
			List<Criterion> list = new ArrayList<>();
			list.add(Restrictions.like("matInfo.barCode", staMatNameBarCodeSpec.trim().toLowerCase(), true));
			list.add(Restrictions.like("matInfo.matEquName", staMatNameBarCodeSpec.trim().toLowerCase(), true));
			list.add(Restrictions.like("matInfo.spec", staMatNameBarCodeSpec.trim().toLowerCase(), true));
			list.add(Restrictions.like("matInfo.barCode", staMatNameBarCodeSpec.trim().toUpperCase(), true));
			list.add(Restrictions.like("matInfo.matEquName", staMatNameBarCodeSpec.trim().toUpperCase(), true));
			list.add(Restrictions.like("matInfo.spec", staMatNameBarCodeSpec.trim().toUpperCase(), true));
			criteria.add(Restrictions.or(list.toArray(new Criterion[list.size()])));
		}
		if (cabinetId != null && cabinetId.startsWith("D_")) {
			criteria.add(Restrictions.eq("equDetailId", cabinetId.substring("D_".length()), true));
		} else {
			criteria.add(Restrictions.eq("equDetail.equSettingId", cabinetId, true));
		}
		return this.equDetailStaRepository.findAll(criteria);
	}

	@Override
	public List<EquDetailStaVo> getEquInfos(String equSettingId, String matNameBarCodeSpec) {
		Criteria<EquDetailSta> criteria = new Criteria<>();
		if (matNameBarCodeSpec != null && !"".equals(matNameBarCodeSpec)) {
			List<Criterion> list = new ArrayList<>();
			list.add(Restrictions.like("matInfo.barCode", matNameBarCodeSpec.toLowerCase(), true));
			list.add(Restrictions.like("matInfo.matEquName", matNameBarCodeSpec.toLowerCase(), true));
			list.add(Restrictions.like("matInfo.spec", matNameBarCodeSpec.toLowerCase(), true));
			list.add(Restrictions.like("matInfo.barCode", matNameBarCodeSpec.toUpperCase(), true));
			list.add(Restrictions.like("matInfo.matEquName", matNameBarCodeSpec.toUpperCase(), true));
			list.add(Restrictions.like("matInfo.spec", matNameBarCodeSpec.toUpperCase(), true));
			criteria.add(Restrictions.or(list.toArray(new Criterion[list.size()])));
		}
		if (equSettingId != null && equSettingId.startsWith("D_")) {
			criteria.add(Restrictions.eq("equDetailId", equSettingId.substring("D_".length()), true));
		} else {
			criteria.add(Restrictions.eq("equDetail.equSettingId", equSettingId, true));
		}
		List<EquDetailSta> staList = this.equDetailStaRepository.findAll(criteria);

		List<FeedingDetail> detailList = this.feedingDetailRepository.getNoFinishFeedingDetailList();
		List<EquDetailStaVo> list = new ArrayList<>();
		for (EquDetailSta sta : staList) {
			list.add(new EquDetailStaVo(sta.getId(), sta.getMatInfo() != null ? sta.getMatInfo().getId() : "",
					sta.getMatInfo() != null ? sta.getMatInfo().getMatEquName() : "",
					sta.getMatInfo() != null ? sta.getMatInfo().getBarCode() : "",
					sta.getMatInfo() != null ? sta.getMatInfo().getSpec() : "",
					sta.getMaxReserve() != null ? sta.getMaxReserve() : 0,
					sta.getWarnVal() != null ? sta.getWarnVal() : 0, sta.getMatInfo() != null ? sta.getCurNum() : 0,
					"F" + sta.getEquDetail().getRowNo() + "-" + sta.getColNo(),
					detailList.stream().filter(detail -> detail.getEquDetailStaId().equals(sta.getId()))
							.mapToInt(FeedingDetail::getFeedingNum).sum(),
					sta.getMatInfo() != null ? sta.getMatInfo().getIcon() : ""));
		}
		return list;
	}

	public List<EquDetailSta> findEquDetailStaListByMatInfoId(String infoId) {
		return this.equDetailStaRepository.findEquDetailStaListByMatInfoId(infoId);
	}
}