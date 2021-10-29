package com.dosth.tool.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.common.util.StringUtil;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.dto.FeedRecordSummary;
import com.dosth.tool.common.state.SystemSwitch;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.FeedingDetailRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.FeedRecordSummaryService;
import com.dosth.tool.service.SystemSetupService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.google.common.collect.Lists;

/**
 * @description 主柜Service实现
 * @author chenlei
 *
 */
@Service
@Transactional
public class FeedRecordSummaryServiceImpl implements FeedRecordSummaryService {

	public static final Logger logger = LoggerFactory.getLogger(FeedRecordSummaryServiceImpl.class);

	@Autowired
	private FeedingDetailRepository feedingDetailRepository;
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
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<FeedRecordSummary> getPage(int curPage, int pageSize, String beginTime, String endTime, String matInfo) {
		List<FeedRecordSummary> summaryList = getInfos(beginTime, endTime, matInfo);
		Pageable pageable = new PageRequest(curPage, pageSize);
		return ListUtil.listConvertToPage(summaryList, pageable);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public void export(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String matInfo) {
		List<FeedRecordSummary> summaryList = getInfos(beginTime, endTime, matInfo);

		OutputStream output = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		HSSFFont fontStyle = workbook.createFont();
		fontStyle.setFontName("宋体");
		fontStyle.setBold(true);
		int rowNo;
		try {
			sheet = workbook.createSheet("补料记录汇总");
			row = sheet.createRow(0);
			cell = row.createCell(0);

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);
			cell.setCellStyle(cellStyle);
			cellStyle.setFont(fontStyle);

			cell = row.createCell(0);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("物料编号");
			cell = row.createCell(1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("物料名称");
			cell = row.createCell(2);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("物料型号");
			cell = row.createCell(3);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("供应商");
			cell = row.createCell(4);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("品牌");
			cell = row.createCell(5);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("补料单位");
			cell = row.createCell(6);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("补料数量");
			cell = row.createCell(7);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("单位成本");
			cell = row.createCell(8);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("金额");

			row.setRowStyle(cellStyle);

			rowNo = 1;
			int numCount = 0;
			int moneyCount = 0;
			for (FeedRecordSummary record : summaryList) {
				row = sheet.createRow(rowNo);
				row.createCell(0).setCellValue(record.getBarCode());
				row.createCell(1).setCellValue(record.getMatInfoName());
				row.createCell(2).setCellValue(record.getSpec());
				row.createCell(3).setCellValue(record.getSupplierName());
				row.createCell(4).setCellValue(record.getBrand());
				row.createCell(5).setCellValue(record.getUnit());
				row.createCell(6).setCellValue(record.getFeedNum());
				numCount += record.getFeedNum();
				row.createCell(7).setCellValue(record.getPrice());
				row.createCell(8).setCellValue(record.getMoney());
				moneyCount += record.getMoney();
				rowNo++;
			}
			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(6).setCellValue(numCount);
			row.createCell(8).setCellValue(moneyCount);

			String fileName = FileUtil.processFileName(request, "补料记录汇总" + DateUtil.getDays());
			output = response.getOutputStream();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			response.setContentType("application/msexcel");
			response.setCharacterEncoding("utf-8");
			workbook.write(output);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
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

	public List<FeedRecordSummary> getInfos(String beginTime, String endTime, String matInfo) {
		Date begin = null;
		Date end = null;
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		if (beginTime != null && !"".equals(beginTime)) {
			begin = DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString());
		} else {
			begin = DateUtil.parseTime(new StringBuilder("1900-01-01").append(" 00:00:00").toString());
		}
		if (endTime != null && !"".equals(endTime)) {
			end = DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString());
		} else {
			end = DateUtil.parseTime(new StringBuilder("9999-12-31").append(" 23:59:59").toString());
		}

		List<FeedingDetail> recordList = this.feedingDetailRepository.getFeedSummary(begin, end);
		Map<String, List<FeedingDetail>> collect = recordList.stream()
				.collect(Collectors.groupingBy(FeedingDetail::getMatInfoId));
		List<FeedRecordSummary> summaryList = new ArrayList<>();
		List<FeedingDetail> list = new ArrayList<>();
		for (Map.Entry<String, List<FeedingDetail>> record : collect.entrySet()) {

			FeedRecordSummary summary = new FeedRecordSummary();
			int num = 0;
			list = record.getValue();
			if (StringUtil.isNotBlank(matInfo)) {
				if (list.get(0).getMatInfo().getBarCode().indexOf(matInfo.trim()) == -1
						&& list.get(0).getMatInfo().getMatEquName().indexOf(matInfo.trim()) == -1) {
					continue;
				}
			}
			summary.setBarCode(list.get(0).getMatInfo().getBarCode());
			summary.setMatInfoName(list.get(0).getMatInfo().getMatEquName());
			summary.setUnit(list.get(0).getMatInfo().getBorrowType().getMessage());
			for (FeedingDetail info : list) {
				num += info.getFeedingNum();
			}
			summary.setFeedNum(num);
			String name = list.get(0).getMatInfo().getManufacturer().getManufacturerName();
			summary.setSupplierName(StringUtil.isNotBlank(name) ? name : "-");
			String bar = list.get(0).getMatInfo().getBrand();
			summary.setBrand(StringUtil.isNotBlank(bar) ? bar : "-");
			String spec = list.get(0).getMatInfo().getSpec();
			summary.setSpec(StringUtil.isNotBlank(spec) ? spec : "-");
			summary.setPrice(list.get(0).getMatInfo().getStorePrice());
			if(valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
				summary.setMoney(list.get(0).getMatInfo().getStorePrice() * num * list.get(0).getMatInfo().getNum());
			}else {
				summary.setMoney(list.get(0).getMatInfo().getStorePrice() * num);
			}
			summaryList.add(summary);
		}
		return summaryList;
	}

	@Override
	public void sendFeedSummary(String beginTime, String endTime, String matInfo) {
		List<FeedRecordSummary> summaryList = getInfos(beginTime, endTime, matInfo);
		
		OutputStream output = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		HSSFFont fontStyle = workbook.createFont();
		fontStyle.setFontName("宋体");
		fontStyle.setBold(true);
		int rowNo;
		try {
			sheet = workbook.createSheet("补料记录汇总");
			row = sheet.createRow(0);
			cell = row.createCell(0);

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);
			cell.setCellStyle(cellStyle);
			cellStyle.setFont(fontStyle);

			cell = row.createCell(0);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("物料编号");
			cell = row.createCell(1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("物料名称");
			cell = row.createCell(2);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("物料型号");
			cell = row.createCell(3);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("供应商");
			cell = row.createCell(4);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("品牌");
			cell = row.createCell(5);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("补料单位");
			cell = row.createCell(6);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("补料数量");
			cell = row.createCell(7);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("单位成本");
			cell = row.createCell(8);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("金额");

			row.setRowStyle(cellStyle);

			rowNo = 1;
			int numCount = 0;
			int moneyCount = 0;
			for (FeedRecordSummary record : summaryList) {
				row = sheet.createRow(rowNo);
				row.createCell(0).setCellValue(record.getBarCode());
				row.createCell(1).setCellValue(record.getMatInfoName());
				row.createCell(2).setCellValue(record.getSpec());
				row.createCell(3).setCellValue(record.getSupplierName());
				row.createCell(4).setCellValue(record.getBrand());
				row.createCell(5).setCellValue(record.getUnit());
				row.createCell(6).setCellValue(record.getFeedNum());
				numCount += record.getFeedNum();
				row.createCell(7).setCellValue(record.getPrice());
				row.createCell(8).setCellValue(record.getMoney());
				moneyCount += record.getMoney();
				rowNo++;
			}
			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(6).setCellValue(numCount);
			row.createCell(8).setCellValue(moneyCount);

			String filePath = this.toolProperties.getTmpUploadPath() + "补料汇总-" + DateUtil.getAllTime() + ".xls";
			File file = new File(filePath);
			output = new FileOutputStream(file);
			workbook.write(output);
			if (!file.exists()) {
				logger.error("生成补料汇总异常,请刷新后再试!");
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

			String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;附件为补料汇总,请查收!</p>";
			this.emailUtil.sendEmail(mailContent, "补料汇总", filePath, "补料汇总", mailList);

		} catch (Exception e) {
			e.printStackTrace();
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