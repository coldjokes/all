package com.dosth.tool.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cnbaosi.dto.ApiFeignResponse;
import com.cnbaosi.dto.tool.ApplyMatDetail;
import com.cnbaosi.dto.tool.ApplyVoucher;
import com.cnbaosi.dto.tool.FeignBorrow;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.db.SimpleExpression;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.statistics.dto.MonthCost;
import com.dosth.statistics.enums.MonthEnum;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.DataSyncType;
import com.dosth.tool.common.state.ReceiveType;
import com.dosth.tool.common.state.SystemSwitch;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.entity.DataSyncState;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.DataSyncStateRepository;
import com.dosth.tool.repository.MatCategoryTreeRepository;
import com.dosth.tool.repository.MatUseBillRepository;
import com.dosth.tool.repository.MatUseRecordRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.repository.UserRepository;
import com.dosth.tool.service.MatUseRecordService;
import com.dosth.tool.service.SystemSetupService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.toolcabinet.enums.EnumBorrowType;
import com.google.common.collect.Lists;

/**
 * @description 物料领用记录Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class MatUseRecordServiceImpl implements MatUseRecordService {
	
	private static final Logger logger = LoggerFactory.getLogger(MatUseRecordServiceImpl.class);

	@Autowired
	private MatCategoryTreeRepository matCategoryTreeRepository;
	@Autowired
	private MatUseRecordRepository matUseRecordRepository;
	@Autowired
	private MatUseBillRepository matUseBillRepository;
	@Autowired
	private DataSyncStateRepository dataSyncStateRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private TimeTaskDetailRepository timeTaskDetailRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private SystemSetupService systemSetupService;

	@Override
	public void save(MatUseRecord record) throws DoSthException {
		this.matUseRecordRepository.save(record);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public MatUseRecord get(Serializable id) throws DoSthException {
		return this.matUseRecordRepository.findOne(id);
	}

	@Override
	public MatUseRecord update(MatUseRecord record) throws DoSthException {
		return this.matUseRecordRepository.saveAndFlush(record);
	}

	@Override
	public void delete(MatUseRecord record) throws DoSthException {
		this.matUseRecordRepository.delete(record);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	@Override
	public Page<MatUseRecord> getPage(int pageNo, int pageSize, String beginTime, String endTime, String receiveType,
			String isReturnBack, String equSettingName, String searchCondition) throws DoSthException {
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		Criteria<MatUseRecord> c = new Criteria<>();
		if (beginTime != null && !"".equals(beginTime)) {
			c.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime)) {
			c.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		if (receiveType != null && !"".equals(receiveType) && !"-1".equals(receiveType)) {
			c.add(Restrictions.eq("receiveType", ReceiveType.valueOf(receiveType), true));
		}
		if (isReturnBack != null && !"".equals(isReturnBack) && !"-1".equals(isReturnBack)) {
			c.add(Restrictions.eq("isReturnBack", isReturnBack, true));
		}

		if (equSettingName != null && !"".equals(equSettingName) && !"-1".equals(equSettingName)) {
			c.add(Restrictions.like("borrowOrigin", equSettingName, true));
		}
		
		if (searchCondition != null && !"".equals(searchCondition)) {
			SimpleExpression[] simples = new SimpleExpression[8];
			simples[0] = Restrictions.like("barCode", searchCondition.trim().toLowerCase(), true);
			simples[1] = Restrictions.like("barCode", searchCondition.trim().toUpperCase(), true);
			simples[2] = Restrictions.like("matInfoName", searchCondition.trim().toLowerCase(), true);
			simples[3] = Restrictions.like("matInfoName", searchCondition.trim().toUpperCase(), true);
			simples[4] = Restrictions.like("spec", searchCondition.trim().toLowerCase(), true);
			simples[5] = Restrictions.like("spec", searchCondition.trim().toUpperCase(), true);
			simples[6] = Restrictions.like("userName", searchCondition.trim().toLowerCase(), true);
			simples[7] = Restrictions.like("userName", searchCondition.trim().toUpperCase(), true);
			c.add(Restrictions.or(simples));
		}
		
		Page<MatUseRecord> page = this.matUseRecordRepository.findAll(c,
				new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "opDate")));
		// 检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if (page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber() + 1)) {
			pageNo = page.getTotalPages() - 1;
			page = this.matUseRecordRepository.findAll(c,
					new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "opDate")));
		}

		List<MatUseRecord> recordList = page.getContent();
		for (MatUseRecord record : recordList) {
			if(valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
				record.setMoney(record.getRealNum() * record.getPrice() * record.getPackNum());
			}
			if (record.getTree() == null || "".equals(record.getReceiveInfo())) {
				record.setReceiveInfo1("全部");
			} else {
				if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 1) {
					record.setReceiveInfo1(record.getTree().getfName().split(",")[1]);
				}
				if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 2) {
					record.setReceiveInfo2(record.getTree().getfName().split(",")[2]);
				}
				if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 3) {
					record.setReceiveInfo3(record.getTree().getfName().split(",")[3]);
				}
			}
		}
		return page;
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	@Override
	public void export(HttpServletRequest request, HttpServletResponse response, String receiveType, String beginTime,
			String endTime, String isReturnBack, String equSettingName, String searchCondition) {
		Criteria<MatUseRecord> c = new Criteria<>();
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		if (beginTime != null && !"".equals(beginTime) && !"-1".equals(beginTime)) {
			c.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime) && !"-1".equals(endTime)) {
			c.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		if (receiveType != null && !"".equals(receiveType) && !"-1".equals(receiveType)) {
			c.add(Restrictions.eq("receiveType", ReceiveType.valueOf(receiveType), true));
		}
		if (isReturnBack != null && !"".equals(isReturnBack) && !"-1".equals(isReturnBack)) {
			c.add(Restrictions.eq("isReturnBack", isReturnBack, true));
		}

		if (equSettingName != null && !"".equals(equSettingName) && !"-1".equals(equSettingName)) {
			c.add(Restrictions.like("borrowOrigin", equSettingName, true));
		}
		
		if (searchCondition != null && !"".equals(searchCondition) && !"-1".equals(searchCondition)) {
			SimpleExpression[] simples = new SimpleExpression[8];
			simples[0] = Restrictions.like("barCode", searchCondition.trim().toLowerCase(), true);
			simples[1] = Restrictions.like("barCode", searchCondition.trim().toUpperCase(), true);
			simples[2] = Restrictions.like("matInfoName", searchCondition.trim().toLowerCase(), true);
			simples[3] = Restrictions.like("matInfoName", searchCondition.trim().toUpperCase(), true);
			simples[4] = Restrictions.like("spec", searchCondition.trim().toLowerCase(), true);
			simples[5] = Restrictions.like("spec", searchCondition.trim().toUpperCase(), true);
			simples[6] = Restrictions.like("userName", searchCondition.trim().toLowerCase(), true);
			simples[7] = Restrictions.like("userName", searchCondition.trim().toUpperCase(), true);
			c.add(Restrictions.or(simples));
		}
		List<MatUseRecord> recordList = this.matUseRecordRepository.findAll(c);

		OutputStream output = null;
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// 建立新的sheet对象（excel的表单）
			HSSFSheet sheet = workbook.createSheet("领用记录");
			// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			HSSFRow row1 = sheet.createRow(0);
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
			HSSFCell cell = row1.createCell(0);

			cell.setCellValue("领用记录");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
			cell.setCellStyle(cellStyle);
			row1.setRowStyle(cellStyle);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			cellStyle.setFont(fontStyle);

			int cellIndex = 0;
			HSSFRow row2 = sheet.createRow(1);
			row2.createCell(cellIndex).setCellValue("物料名称");
			row2.createCell(++cellIndex).setCellValue("物料编号");
			row2.createCell(++cellIndex).setCellValue("物料型号");
			row2.createCell(++cellIndex).setCellValue("品牌（供应商）");
			row2.createCell(++cellIndex).setCellValue("领取来源");
			row2.createCell(++cellIndex).setCellValue("领取数量");
			row2.createCell(++cellIndex).setCellValue("实领数量");
			row2.createCell(++cellIndex).setCellValue("领取单位");
			row2.createCell(++cellIndex).setCellValue("包装数量");
			row2.createCell(++cellIndex).setCellValue("包装单位");
			row2.createCell(++cellIndex).setCellValue("类型");
			row2.createCell(++cellIndex).setCellValue("领用类型1");
			row2.createCell(++cellIndex).setCellValue("领用类型2");
			row2.createCell(++cellIndex).setCellValue("领用类型3");
			row2.createCell(++cellIndex).setCellValue("单价（元）");
			row2.createCell(++cellIndex).setCellValue("金额（元）");
			row2.createCell(++cellIndex).setCellValue("使用寿命");
			row2.createCell(++cellIndex).setCellValue("部门");
			row2.createCell(++cellIndex).setCellValue("领用人员");
			row2.createCell(++cellIndex).setCellValue("已归还数量");
			row2.createCell(++cellIndex).setCellValue("是否归还");
			row2.createCell(++cellIndex).setCellValue("制造产量");
			row2.createCell(++cellIndex).setCellValue("领用时间");

			int rowNo = 2;
			int sumBorrowNum = 0; // 总领取数量
			int sumRealNum = 0; // 实际领取数量
			float sumMoney = 0f; // 总金额
			for (MatUseRecord record : recordList) {
				HSSFRow row3 = sheet.createRow(rowNo);
				cellIndex = 0;
				// 创建单元格并设置单元格内容
				
				row3.createCell(cellIndex).setCellValue(record.getMatInfoName() != null ? record.getMatInfoName() : "");
				row3.createCell(++cellIndex).setCellValue(record.getBarCode() != null ? record.getBarCode() : "");
				row3.createCell(++cellIndex).setCellValue(record.getSpec() != null ? record.getSpec() : "");
				row3.createCell(++cellIndex).setCellValue(record.getBrand() != null ? record.getBrand() : "");
				row3.createCell(++cellIndex).setCellValue(record.getBorrowOrigin() != null ? record.getBorrowOrigin() : "");
				row3.createCell(++cellIndex).setCellValue(record.getBorrowNum() != null ? record.getBorrowNum() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getRealNum() != null ? record.getRealNum() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getBorrowType() != null ? record.getBorrowType() : "");
				row3.createCell(++cellIndex).setCellValue(record.getPackNum() != null ? record.getPackNum() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getPackUnit() != null ? record.getPackUnit() : "");
				row3.createCell(++cellIndex).setCellValue(record.getReceiveType() != null ? record.getReceiveType().getMessage() : "-");

				if (record.getTree() == null || "".equals(record.getReceiveInfo())) {
					row3.createCell(++cellIndex).setCellValue("全部");
					row3.createCell(++cellIndex).setCellValue("");
					row3.createCell(++cellIndex).setCellValue("");
				} else {
					if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 1) {
						row3.createCell(++cellIndex).setCellValue(record.getTree().getfName().split(",")[1]);
					} else {
						row3.createCell(++cellIndex).setCellValue("");
					}
					if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 2) {
						row3.createCell(++cellIndex).setCellValue(record.getTree().getfName().split(",")[2]);
					} else {
						row3.createCell(++cellIndex).setCellValue("");
					}
					if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 3) {
						row3.createCell(++cellIndex).setCellValue(record.getTree().getfName().split(",")[3]);
					} else {
						row3.createCell(++cellIndex).setCellValue("");
					}
				}
				row3.createCell(++cellIndex).setCellValue(record.getPrice() != null ? String.format("%s", record.getPrice()) : "0");
				if(valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
					record.setMoney(record.getRealNum() * record.getPrice() * record.getPackNum());
					row3.createCell(++cellIndex).setCellValue(record.getMoney() != null ? String.format("%s", record.getMoney()) : "0");
				}else {
					row3.createCell(++cellIndex).setCellValue(record.getMoney() != null ? String.format("%s", record.getMoney()) : "0");
				}
				row3.createCell(++cellIndex).setCellValue(record.getUseLife() != null ? record.getUseLife() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getDeptName() != null ? record.getDeptName() : "");
				row3.createCell(++cellIndex).setCellValue(record.getUserName() != null ? record.getUserName() : "");
				row3.createCell(++cellIndex).setCellValue(record.getReturnBackNum() != null ? record.getReturnBackNum() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getIsReturnBack() != null ? record.getIsReturnBack() : "");
				row3.createCell(++cellIndex).setCellValue(record.getRealLife() != null ? record.getRealLife() : 0);
				row3.createCell(++cellIndex).setCellValue(DateUtil.getTime(record.getOpDate() != null ? record.getOpDate() : null));
				if (record.getBorrowNum() != null && record.getBorrowNum() != 0) {
					sumBorrowNum += record.getBorrowNum();
				}

				if (record.getRealNum() != null && record.getRealNum() != 0) {
					sumRealNum += record.getRealNum();
				}

				if (record.getMoney() != null && record.getMoney() != 0) {
					sumMoney += record.getMoney();
				}

				rowNo++;
			}

			HSSFRow row4 = sheet.createRow(rowNo);
			row4.createCell(0).setCellValue("合计");
			row4.createCell(5).setCellValue(sumBorrowNum);
			row4.createCell(6).setCellValue(sumRealNum);
			row4.createCell(15).setCellValue(String.format("%s", sumMoney));

			// Excel文件名
			String fileName = FileUtil.processFileName(request, "领用记录" + DateUtil.getDays());
			// 取得输出流
			output = response.getOutputStream();
			// 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			response.setContentType("application/msexcel");
			response.setCharacterEncoding("utf-8");
			workbook.write(output);
			output.flush();
		} catch (Exception e) {
			throw new DoSthException(DoSthExceptionEnum.SERVER_ERROR);
		} finally {
			if (output != null) {
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
	
	@Override
	public Map<String, List<MonthCost>> getThrYearPriceSumGroupByMonth() throws DoSthException {
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		Map<String, List<MonthCost>> map = new HashMap<>();
		List<MonthCost> list;
		List<MatUseRecord> recordList = this.matUseRecordRepository.getMatUseRecordListBetween(DateUtil.getYearStartTime(-2), DateUtil.getYearEndTime(0));
		for(MatUseRecord record : recordList) {
			if(valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
				record.setMoney(record.getPrice() * record.getRealNum() * record.getPackNum());
			}else {
				record.setMoney(record.getPrice() * record.getRealNum());
			}
		}
		Map<String, List<MatUseRecord>> groupBy = recordList.stream().collect(Collectors.groupingBy(MatUseRecord::getSimpleYear));
		for (Entry<String, List<MatUseRecord>> entry : groupBy.entrySet()) {
			
			if (map.keySet().contains(entry.getKey())) {
				list = map.get(entry.getKey());
			} else {
				list = new ArrayList<>();
			}
			for (MonthEnum month : MonthEnum.values()) {
				list.add(new MonthCost(month,
						entry.getValue().stream()
								.filter(bill -> bill.getSimpleMonth().equals(entry.getKey() + month.toStr()))
								.mapToDouble(MatUseRecord::getMoney).sum()));
			}
			map.put(entry.getKey(), list);
		}
		return map;
	}

	@Override
	public Map<String, Integer> getCurMonthGroupByMat() throws DoSthException {
		Map<String, Integer> map = new HashMap<>();
		List<MatUseRecord> recordList = this.matUseRecordRepository
				.getMatUseRecordListBetween(DateUtil.getMonthFirstTime(0, 0), DateUtil.getMonthLastTime(0, 0));
		Map<String, IntSummaryStatistics> result = recordList.stream().collect(Collectors
				.groupingBy(MatUseRecord::getMatInfoName, Collectors.summarizingInt(MatUseRecord::getRealNum)));
		for (Entry<String, IntSummaryStatistics> entry : result.entrySet()) {
			map.put(entry.getKey(), Integer.valueOf(String.valueOf(entry.getValue().getSum())));
		}
		return map;
	}
	@Override
	public Map<String, Map<String, Integer>> getThrYCntGroupByMatType() throws DoSthException {
		Map<String, Map<String, Integer>> map = new HashMap<>();
		Map<String, Integer> inner;
		List<MatUseRecord> recordList = this.matUseRecordRepository
				.getMatUseRecordListBetween(DateUtil.getYearStartTime(-2), DateUtil.getYearEndTime(0));
		List<MatCategoryTree> treeList = this.matCategoryTreeRepository.findAll(UsingStatus.ENABLE);
		Map<String, List<MatUseRecord>> groupBy = recordList.stream()
				.collect(Collectors.groupingBy(MatUseRecord::getSimpleYear));
		for (Entry<String, List<MatUseRecord>> entry : groupBy.entrySet()) {
			if (map.keySet().contains(entry.getKey())) {
				inner = map.get(entry.getKey());
			} else {
				inner = new HashMap<>();
			}
			for (MatCategoryTree tree : treeList) {
				inner.put(tree.getName(), entry.getValue().stream()
						.filter(record -> record.getReceiveInfo() != null && record.getReceiveInfo().equals(tree.getId()))
						.mapToInt(MatUseRecord::getRealNum).sum());
			}
			map.put(entry.getKey(), inner);
		}
		groupBy = recordList.stream().collect(Collectors.groupingBy(MatUseRecord::getSimpleYear));
		for (Entry<String, List<MatUseRecord>> entry : groupBy.entrySet()) {
			if (map.keySet().contains(entry.getKey())) {
				inner = map.get(entry.getKey());
			} else {
				inner = new HashMap<>();
			}
			inner.put(EnumBorrowType.GRID.getDesc(), entry.getValue().stream()
					.filter(record -> record.getReceiveInfo() == null).mapToInt(MatUseRecord::getRealNum).sum());
			map.put(entry.getKey(), inner);
		}
		return map;
	}

	@Override
	public List<ApplyVoucher> getApplyVoucherResult(String applyVoucherResult) {
		List<ApplyVoucher> voucherList = new ArrayList<>();
		ApplyVoucher voucher = new ApplyVoucher();
		String[] arr = applyVoucherResult.split("\\|");
		voucher.setApplyNo(arr[0]);
		arr = arr[1].split(",");
		MatUseRecord record;
		List<ApplyMatDetail> detailList = new ArrayList<>();
		ApplyMatDetail detail;
		for (int i = 0; i < arr.length; i++) {
			record = this.matUseRecordRepository.getOne(arr[i]);
			detail = new ApplyMatDetail();
			detail.setBarCode(record.getBarCode());
			detail.setBorrowNum(record.getBorrowNum().floatValue());
			detail.setBorrowType(record.getReceiveInfo());
			detail.setMatName(record.getMatInfoName());
			detail.setRealNum(record.getRealNum());
			detail.setSpec(record.getSpec());
			detailList.add(detail);
		}
		voucher.setDetailList(detailList);
		voucherList.add(voucher);
		return voucherList;
	}
	
	@Override
	public ApiFeignResponse<FeignBorrow> getSyncBorrows(String cabinetName, Long endTime) {
		ApiFeignResponse<FeignBorrow> response = new ApiFeignResponse<>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage("领用清单同步成功");
		DataSyncState syncInfo = this.dataSyncStateRepository.getSyncInfo(cabinetName, DataSyncType.BORROW).get(0);
		Date time = new Date();
		if(endTime != null) {
			time = new Date(endTime);
		}
		
		try {
			List<MatUseRecord> list = matUseBillRepository.getSyncRecord(cabinetName, syncInfo.getSyncTime(), time);
			List<FeignBorrow> results = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(list)) {
				for (MatUseRecord record : list) {
					ViewUser user = this.userRepository.findUserByAccountId(record.getAccountId());
					FeignBorrow borrow = new FeignBorrow();
					borrow.setAccountId(user.getLoginName());
					borrow.setAccountName(user.getUserName());
					borrow.setOpDate(record.getOpDate());
					borrow.setBorrowNum(record.getBorrowNum());
					borrow.setBorrowOrigin(record.getReceiveInfo());
					borrow.setEquId(syncInfo.getCabinetId());
					borrow.setEquName(cabinetName);
					borrow.setMatId(record.getMatInfoId());
					borrow.setMatName(record.getMatInfoName());
					borrow.setMatSpec(record.getSpec());
					borrow.setMatBarCode(record.getBarCode());
					borrow.setSupplyID(record.getMatInfo().getManufacturerId());
					borrow.setSupplyFullName(record.getMatInfo().getManufacturer().getManufacturerName());
					results.add(borrow);
				}
				response.setResultList(results);
			}
		}catch (Exception e) {
			response.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setMessage("服务端异常");
			return response;
		}
		// 更新同步信息
		syncInfo.setSyncTime(time);
		this.dataSyncStateRepository.saveAndFlush(syncInfo);
		return response;
	}

	@Override
	public Map<String, Integer> getBorrowNumGroupByDept() {
		Map<String, Integer> map = new HashMap<>();
		List<Object[]> list = this.matUseRecordRepository.getBorrowNumGroupByDept();
		for (Object[] obj : list) {
			map.put(String.valueOf(obj[0]), Integer.valueOf(String.valueOf(obj[1])));
		}
		return map;
	}

	@Override
	public void sendUseRecord(String receiveType, String beginTime, String endTime, String isReturnBack,
			String equSettingName, String searchCondition) {


		// 查询领用记录
		Criteria<MatUseRecord> c = new Criteria<>();
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		if (beginTime != null && !"".equals(beginTime) && !"-1".equals(beginTime)) {
			c.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime) && !"-1".equals(endTime)) {
			c.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		if (receiveType != null && !"".equals(receiveType) && !"-1".equals(receiveType)) {
			c.add(Restrictions.eq("receiveType", ReceiveType.valueOf(receiveType), true));
		}
		if (isReturnBack != null && !"".equals(isReturnBack) && !"-1".equals(isReturnBack)) {
			c.add(Restrictions.eq("isReturnBack", isReturnBack, true));
		}

		if (equSettingName != null && !"".equals(equSettingName) && !"-1".equals(equSettingName)) {
			c.add(Restrictions.like("borrowOrigin", equSettingName, true));
		}

		if (searchCondition != null && !"".equals(searchCondition) && !"-1".equals(searchCondition)) {
			SimpleExpression[] simples = new SimpleExpression[8];
			simples[0] = Restrictions.like("barCode", searchCondition.toLowerCase(), true);
			simples[1] = Restrictions.like("barCode", searchCondition.toUpperCase(), true);
			simples[2] = Restrictions.like("matEquName", searchCondition.toLowerCase(), true);
			simples[3] = Restrictions.like("matEquName", searchCondition.toUpperCase(), true);
			simples[4] = Restrictions.like("spec", searchCondition.toLowerCase(), true);
			simples[5] = Restrictions.like("spec", searchCondition.toUpperCase(), true);
			simples[6] = Restrictions.like("userName", searchCondition.toLowerCase(), true);
			simples[7] = Restrictions.like("userName", searchCondition.toUpperCase(), true);
			c.add(Restrictions.or(simples));
		}
		List<MatUseRecord> recordList = this.matUseRecordRepository.findAll(c);

		// 创建HSSFWorkbook对象(excel的文档对象)
		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// 建立新的sheet对象（excel的表单）
			HSSFSheet sheet = workbook.createSheet("领用记录");
			// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			HSSFRow row1 = sheet.createRow(0);
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
			HSSFCell cell = row1.createCell(0);

			cell.setCellValue("领用记录");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
			cell.setCellStyle(cellStyle);
			row1.setRowStyle(cellStyle);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			cellStyle.setFont(fontStyle);

			int cellIndex = 0;
			HSSFRow row2 = sheet.createRow(1);
			row2.createCell(cellIndex).setCellValue("物料名称");
			row2.createCell(++cellIndex).setCellValue("物料编号");
			row2.createCell(++cellIndex).setCellValue("物料型号");
			row2.createCell(++cellIndex).setCellValue("品牌（供应商）");
			row2.createCell(++cellIndex).setCellValue("领取来源");
			row2.createCell(++cellIndex).setCellValue("领取数量");
			row2.createCell(++cellIndex).setCellValue("实领数量");
			row2.createCell(++cellIndex).setCellValue("领取单位");
			row2.createCell(++cellIndex).setCellValue("包装数量");
			row2.createCell(++cellIndex).setCellValue("包装单位");
//			row2.createCell(++cellIndex).setCellValue("领用类型");

			row2.createCell(++cellIndex).setCellValue("类型");
			row2.createCell(++cellIndex).setCellValue("领用类型1");
			row2.createCell(++cellIndex).setCellValue("领用类型2");
			row2.createCell(++cellIndex).setCellValue("领用类型3");
			
			row2.createCell(++cellIndex).setCellValue("单价（元）");
			row2.createCell(++cellIndex).setCellValue("金额（元）");
			row2.createCell(++cellIndex).setCellValue("部门");
			row2.createCell(++cellIndex).setCellValue("领用人员");
			row2.createCell(++cellIndex).setCellValue("已归还数量");
			row2.createCell(++cellIndex).setCellValue("是否归还");
			row2.createCell(++cellIndex).setCellValue("领用时间");

			int rowNo = 2;
			int sumBorrowNum = 0; // 总领取数量
			int sumRealNum = 0; // 实际领取数量
			float sumMoney = 0f; // 总金额
			for (MatUseRecord record : recordList) {
				HSSFRow row3 = sheet.createRow(rowNo);
				cellIndex = 0;
				// 创建单元格并设置单元格内容
				row3.createCell(cellIndex).setCellValue(record.getMatInfoName() != null ? record.getMatInfoName() : "");
				row3.createCell(++cellIndex).setCellValue(record.getBarCode() != null ? record.getBarCode() : "");
				row3.createCell(++cellIndex).setCellValue(record.getSpec() != null ? record.getSpec() : "");
				row3.createCell(++cellIndex).setCellValue(record.getBrand() != null ? record.getBrand() : "");
				row3.createCell(++cellIndex).setCellValue(record.getBorrowOrigin() != null ? record.getBorrowOrigin() : "");
				row3.createCell(++cellIndex).setCellValue(record.getBorrowNum() != null ? record.getBorrowNum() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getRealNum() != null ? record.getRealNum() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getBorrowType() != null ? record.getBorrowType() : "");
				row3.createCell(++cellIndex).setCellValue(record.getPackNum() != null ? record.getPackNum() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getPackUnit() != null ? record.getPackUnit() : "");
//				row3.createCell(++cellIndex).setCellValue(record.getTree() != null ? record.getTree().getName() : "全部");
				row3.createCell(++cellIndex).setCellValue(record.getReceiveType() != null ? record.getReceiveType().getMessage() : "-");

				if (record.getTree() == null || "".equals(record.getReceiveInfo())) {
					row3.createCell(++cellIndex).setCellValue("全部");
					row3.createCell(++cellIndex).setCellValue("");
					row3.createCell(++cellIndex).setCellValue("");
				} else {
					if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 1) {
						row3.createCell(++cellIndex).setCellValue(record.getTree().getfName().split(",")[1]);
					} else {
						row3.createCell(++cellIndex).setCellValue("");
					}
					if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 2) {
						row3.createCell(++cellIndex).setCellValue(record.getTree().getfName().split(",")[2]);
					} else {
						row3.createCell(++cellIndex).setCellValue("");
					}
					if (record.getTree().getfName() != null && record.getTree().getfName().split(",").length > 3) {
						row3.createCell(++cellIndex).setCellValue(record.getTree().getfName().split(",")[3]);
					} else {
						row3.createCell(++cellIndex).setCellValue("");
					}
				}
				
				row3.createCell(++cellIndex).setCellValue(record.getPrice() != null ? String.format("%s", record.getPrice()) : "0");
				if(valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
					record.setMoney(record.getRealNum()*record.getPrice()*record.getPackNum());
					row3.createCell(++cellIndex).setCellValue(record.getMoney() != null ? String.format("%s", record.getMoney()) : "0");
				}else {
					row3.createCell(++cellIndex).setCellValue(record.getMoney() != null ? String.format("%s", record.getMoney()) : "0");
				}
				row3.createCell(++cellIndex).setCellValue(record.getDeptName() != null ? record.getDeptName() : "");
				row3.createCell(++cellIndex).setCellValue(record.getUserName() != null ? record.getUserName() : "");
				row3.createCell(++cellIndex).setCellValue(record.getReturnBackNum() != null ? record.getReturnBackNum() : 0);
				row3.createCell(++cellIndex).setCellValue(record.getIsReturnBack() != null ? record.getIsReturnBack() : "");
				row3.createCell(++cellIndex).setCellValue(DateUtil.getTime(record.getOpDate() != null ? record.getOpDate() : null));

				if (record.getBorrowNum() != null && record.getBorrowNum() != 0) {
					sumBorrowNum += record.getBorrowNum();
				}

				if (record.getRealNum() != null && record.getRealNum() != 0) {
					sumRealNum += record.getRealNum();
				}

				if (record.getMoney() != null && record.getMoney() != 0) {
					sumMoney += record.getMoney();
				}

				rowNo++;
			}

			HSSFRow row4 = sheet.createRow(rowNo);
			row4.createCell(0).setCellValue("合计");
			row4.createCell(5).setCellValue(sumBorrowNum);
			row4.createCell(6).setCellValue(sumRealNum);
			row4.createCell(15).setCellValue(String.format("%s", sumMoney));

			String filePath = this.toolProperties.getTmpUploadPath() + "领用记录-" + DateUtil.getAllTime() + ".xls";
			File file = new File(filePath);
			output = new FileOutputStream(file);
			workbook.write(output);
			if (!file.exists()) {
				logger.error("生成领用记录异常,请刷新后再试!");
			}

			List<String> mailList = Lists.newArrayList();
			List<TimeTaskDetail> timeTaskDetailList = this.timeTaskDetailRepository.findAll();
			if (CollectionUtils.isNotEmpty(timeTaskDetailList)) {
				ViewUser viewUser = null;
				TimeTaskDetail timeTaskDetail = timeTaskDetailList.get(0);
				if (timeTaskDetail != null && timeTaskDetail.getAccountId() != null) {
					String accountIds[] = timeTaskDetail.getAccountId().split(",");
					for (int i = 0; i < accountIds.length; i++) {
						viewUser = this.userService.getViewUser(accountIds[i]);
						if (viewUser != null && viewUser.getEmail() != null) {
							if (viewUser.getEmail() == null || viewUser.getEmail().equals("")) {
								continue;
							}
							mailList.add(viewUser.getEmail());
						}
					}
				}
			}

			String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;附件为领用记录,请查收!</p>";
			this.emailUtil.sendEmail(mailContent, "领用记录", filePath, "领用记录", mailList);
		} catch (Exception e) {
			throw new DoSthException(DoSthExceptionEnum.SERVER_ERROR);
		} finally {
			if (output != null) {
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