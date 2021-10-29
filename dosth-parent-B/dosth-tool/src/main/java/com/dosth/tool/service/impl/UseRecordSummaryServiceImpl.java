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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
import com.dosth.common.util.NumUtil;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.dto.CategorySummary;
import com.dosth.tool.common.dto.UseRecordSummary;
import com.dosth.tool.common.state.SystemSwitch;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.SystemSetupService;
import com.dosth.tool.service.UseRecordSummaryService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.google.common.collect.Lists;

/**
 * @description 主柜Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class UseRecordSummaryServiceImpl implements UseRecordSummaryService {

	private static final Logger logger = LoggerFactory.getLogger(UseRecordSummaryServiceImpl.class);

	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;
	@PersistenceContext
	private EntityManager entityManager;
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
	public Page<UseRecordSummary> getPage(int curPage, int pageSize, String beginTime, String endTime, String matInfo) {
		List<UseRecordSummary> summaryList = getUseRecordSummary(beginTime, endTime, matInfo);
		Pageable pageable = new PageRequest(curPage, pageSize);
		return ListUtil.listConvertToPage(summaryList, pageable);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public void export(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String matInfo) {
		List<UseRecordSummary> summaryList = getUseRecordSummary(beginTime, endTime, matInfo);

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
			sheet = workbook.createSheet("领用记录汇总");
			row = sheet.createRow(0);
			cell = row.createCell(0);

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);
			cell.setCellStyle(cellStyle);
			cellStyle.setFont(fontStyle);

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
			cell.setCellValue("单位");
			cell = row.createCell(6);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("领料数量");
			cell = row.createCell(7);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("单位成本");
			cell = row.createCell(8);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("金额");

			row.setRowStyle(cellStyle);

			rowNo = 1;
			int numCount = 0;
			float moneyCount = 0;
			for (UseRecordSummary record : summaryList) {
				row = sheet.createRow(rowNo);
				row.createCell(0).setCellValue(record.getBarCode());
				row.createCell(1).setCellValue(record.getMatInfoName());
				row.createCell(2).setCellValue(record.getSpec());
				row.createCell(3).setCellValue(record.getSupplierName());
				row.createCell(4).setCellValue(record.getBrand());
				row.createCell(5).setCellValue(record.getBorrowUnit());
				row.createCell(6).setCellValue(record.getBorrowNum());
				numCount += record.getBorrowNum();
				row.createCell(7).setCellValue(NumUtil.keep2Point(record.getPrice()));
				row.createCell(8).setCellValue(NumUtil.keep2Point(record.getMoney()));
				moneyCount += record.getMoney();
				rowNo++;
			}
			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(6).setCellValue(numCount);
			row.createCell(8).setCellValue(NumUtil.keep2Point(moneyCount));

			String fileName = FileUtil.processFileName(request, "领用记录汇总" + DateUtil.getDays());
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

	private List<UseRecordSummary> getUseRecordSummary(String beginTime, String endTime, String matInfo) {
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<MatUseBill> root = query.from(MatUseBill.class);
		Path<String> barCode = root.get("barCode");
		Path<String> matEquName = root.get("matEquName");
		Path<String> spec = root.get("spec");
		Path<String> brand = root.get("brand");
		Path<String> manufacturer = root.get("manufacturer");
		Path<Integer> borrowNum = root.get("borrowNum");
		Path<String> matId = root.get("matInfoId");
		Path<Date> opDate = root.get("opDate");
		Join<MatUseRecord, Float> record = root.join("matUseRecord");
		List<Predicate> preAndList = new ArrayList<>();
		List<Predicate> preOrList = new ArrayList<>();
		// 拼接where条件
		if (beginTime != null && !"".equals(beginTime)) {
			preAndList.add(cb.greaterThanOrEqualTo(opDate,
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString())));
		}
		if (endTime != null && !"".equals(endTime)) {
			preAndList.add(cb.lessThanOrEqualTo(opDate,
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString())));
		}
		if (matInfo != null && !"".equals(matInfo)) {
			preOrList.add(cb.or(cb.like(barCode, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(barCode, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(matEquName, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(matEquName, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(spec, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(spec, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(brand, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(brand, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(manufacturer, "%" + matInfo.trim().toLowerCase() + "%")));
			preOrList.add(cb.or(cb.like(manufacturer, "%" + matInfo.trim().toLowerCase() + "%")));
		}

		Predicate[] preAdds = new Predicate[preAndList.size()];
		preAdds = preAndList.toArray(preAdds);
		Predicate[] preOrs = new Predicate[preOrList.size()];
		preOrs = preOrList.toArray(preOrs);

		Predicate and = cb.and(preAdds);
		cb.and(and);
		Predicate or = cb.or(preOrs);
		cb.or(preOrs);

		// 加上where条件
		if (preAdds.length < 1) {
			if (preOrs.length > 0) {
				query.where(or);
			}
		} else {
			if (preOrs.length > 0) {
				query.where(and, or);
			} else {
				query.where(and);
			}
		}

		query.groupBy(barCode, matEquName, spec, manufacturer, brand, record.get("price"), record.get("borrowType"),
				matId, record.get("packNum"));

		// 指定查询项，select后面的东西
		query.multiselect(barCode, matEquName, spec, manufacturer, brand, cb.sum(borrowNum), record.get("price"),
				record.get("borrowType"), matId, record.get("packNum"));

		TypedQuery<Object[]> q = this.entityManager.createQuery(query);

		List<UseRecordSummary> summaryList = new ArrayList<>();
		UseRecordSummary summary;
		List<SubCabinetDetail> subDetailList = this.subCabinetDetailRepository.getAllMatInfos();
		Map<String, Integer> subMatCountMap = subDetailList.stream().collect(
				Collectors.groupingBy(SubCabinetDetail::getMatInfoId, Collectors.summingInt(SubCabinetDetail::getNum)));

		for (Object[] obj : q.getResultList()) {
			summary = new UseRecordSummary();
			int subNum = 0;
			// 获取暂存柜库存量
			if (subMatCountMap.get(String.valueOf(obj[8])) != null) {
				subNum = subMatCountMap.get(String.valueOf(obj[8]));
			}
			summary.setBarCode(String.valueOf(obj[0]));
			summary.setMatInfoName(String.valueOf(obj[1]));
			summary.setSpec(String.valueOf(obj[2]));
			summary.setSupplierName(String.valueOf(obj[3]));
			summary.setBrand(String.valueOf(obj[4]));
			summary.setBorrowUnit(String.valueOf(obj[7]));
			// 实际使用量 = 取出数量 - 暂存柜库存量
			summary.setBorrowNum(Integer.valueOf(String.valueOf(obj[5])));
			summary.setPrice(Float.valueOf(String.valueOf(obj[6])));
			summary.setPackNum(Integer.valueOf(String.valueOf(obj[9])));
			if(valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
				summary.setMoney(summary.getBorrowNum() * summary.getPrice() * summary.getPackNum());
			}else {
				summary.setMoney(summary.getBorrowNum() * summary.getPrice());
			}
			summaryList.add(summary);
		}
		return summaryList;
	}

	@Override
	public Page<CategorySummary> getCategoryPage(int pageNo, int pageSize, String beginTime, String endTime) {
		List<CategorySummary> summaryList = getCategorySummary(beginTime, endTime);
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(summaryList, pageable);
	}

	@Override
	public void exportCategory(HttpServletRequest request, HttpServletResponse response, String beginTime,
			String endTime) {
		List<CategorySummary> summaryList = getCategorySummary(beginTime, endTime);

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
			sheet = workbook.createSheet("领用途径汇总");
			row = sheet.createRow(0);
			cell = row.createCell(0);

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);
			cell.setCellStyle(cellStyle);
			cellStyle.setFont(fontStyle);

			cell.setCellValue("领取途径");
			cell = row.createCell(1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("领取数量");
			cell = row.createCell(2);
			cell.setCellStyle(cellStyle);

			rowNo = 1;
			for (CategorySummary category : summaryList) {
				row = sheet.createRow(rowNo);
				row.createCell(0).setCellValue(category.getCategoryName());
				row.createCell(1).setCellValue(category.getBorrowNumSummary());
				rowNo++;
			}

			String fileName = FileUtil.processFileName(request, "领用途径汇总" + DateUtil.getDays());
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

	private List<CategorySummary> getCategorySummary(String beginTime, String endTime) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<MatUseBill> root = query.from(MatUseBill.class);
		Path<Integer> borrowNum = root.get("borrowNum");
		Path<Date> opDate = root.get("opDate");
		Join<MatUseRecord, Float> record = root.join("matUseRecord");
		List<Predicate> preAndList = new ArrayList<>();
		List<Predicate> preOrList = new ArrayList<>();
		// 拼接where条件
		if (beginTime != null && !"".equals(beginTime)) {
			preAndList.add(cb.greaterThanOrEqualTo(opDate,
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString())));
		}
		if (endTime != null && !"".equals(endTime)) {
			preAndList.add(cb.lessThanOrEqualTo(opDate,
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString())));
		}

		Predicate[] preAdds = new Predicate[preAndList.size()];
		preAdds = preAndList.toArray(preAdds);
		Predicate[] preOrs = new Predicate[preOrList.size()];
		preOrs = preOrList.toArray(preOrs);

		Predicate and = cb.and(preAdds);
		cb.and(and);
		Predicate or = cb.or(preOrs);
		cb.or(preOrs);

		// 加上where条件
		if (preAdds.length < 1) {
			if (preOrs.length > 0) {
				query.where(or);
			}
		} else {
			if (preOrs.length > 0) {
				query.where(and, or);
			} else {
				query.where(and);
			}
		}

		query.groupBy(record.get("tree"));

		// 指定查询项，select后面的东西
		query.multiselect(record.get("tree"), cb.sum(borrowNum));

		TypedQuery<Object[]> q = this.entityManager.createQuery(query);

		List<CategorySummary> summaryList = new ArrayList<>();
		MatCategoryTree tree;
		for (Object[] obj : q.getResultList()) {
			tree = (MatCategoryTree) obj[0];
			summaryList.add(new CategorySummary(tree.getName(), Integer.valueOf(String.valueOf(obj[1]))));
		}
		return summaryList;
	}

	@Override
	public void sendUseSummary(String beginTime, String endTime, String matInfo) {
		List<UseRecordSummary> summaryList = getUseRecordSummary(beginTime, endTime, matInfo);

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
			sheet = workbook.createSheet("领用记录汇总");
			row = sheet.createRow(0);
			cell = row.createCell(0);

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);
			cell.setCellStyle(cellStyle);
			cellStyle.setFont(fontStyle);

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
			cell.setCellValue("单位");
			cell = row.createCell(6);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("领料数量");
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
			for (UseRecordSummary record : summaryList) {
				row = sheet.createRow(rowNo);
				row.createCell(0).setCellValue(record.getBarCode());
				row.createCell(1).setCellValue(record.getMatInfoName());
				row.createCell(2).setCellValue(record.getSpec());
				row.createCell(3).setCellValue(record.getSupplierName());
				row.createCell(4).setCellValue(record.getBrand());
				row.createCell(5).setCellValue(record.getBorrowUnit());
				row.createCell(6).setCellValue(record.getBorrowNum());
				numCount += record.getBorrowNum();
				row.createCell(7).setCellValue(record.getPrice());
				row.createCell(8).setCellValue(record.getMoney());
				moneyCount += record.getMoney();
				rowNo++;
			}
			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(6).setCellValue(numCount);
			row.createCell(8).setCellValue(moneyCount);

			String filePath = this.toolProperties.getTmpUploadPath() + "领用汇总-" + DateUtil.getAllTime() + ".xls";
			File file = new File(filePath);
			output = new FileOutputStream(file);
			workbook.write(output);
			if (!file.exists()) {
				logger.error("生成领用汇总异常,请刷新后再试!");
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

			String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;附件为领用汇总,请查收!</p>";
			this.emailUtil.sendEmail(mailContent, "领用汇总", filePath, "领用汇总", mailList);
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

	@Override
	public Page<CategorySummary> getDeptPage(int pageNo, int pageSize, String beginTime, String endTime) {
		List<CategorySummary> summaryList = getDeptSummary(beginTime, endTime);
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(summaryList, pageable);
	}

	@Override
	public void exportDept(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime) {
		List<CategorySummary> summaryList = getDeptSummary(beginTime, endTime);

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
			sheet = workbook.createSheet("领用部门汇总");
			row = sheet.createRow(0);
			cell = row.createCell(0);

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);
			cell.setCellStyle(cellStyle);
			cellStyle.setFont(fontStyle);

			cell.setCellValue("领取部门");
			cell = row.createCell(1);
			cell.setCellStyle(cellStyle);
			cell.setCellValue("领取数量");
			cell = row.createCell(2);
			cell.setCellStyle(cellStyle);

			rowNo = 1;
			for (CategorySummary category : summaryList) {
				row = sheet.createRow(rowNo);
				row.createCell(0).setCellValue(category.getCategoryName());
				row.createCell(1).setCellValue(category.getBorrowNumSummary());
				rowNo++;
			}

			String fileName = FileUtil.processFileName(request, "领用部门汇总" + DateUtil.getDays());
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

	/**
	 * @description 获取部门汇总
	 * @param beginTime 起始日期
	 * @param endTime   截至日期
	 * @param matInfo   物料信息
	 * @return
	 */
	private List<CategorySummary> getDeptSummary(String beginTime, String endTime) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
		Root<MatUseBill> root = query.from(MatUseBill.class);
		Path<Integer> borrowNum = root.get("borrowNum");
		Path<Date> opDate = root.get("opDate");
		Join<MatUseRecord, Float> record = root.join("matUseRecord");
		List<Predicate> preAndList = new ArrayList<>();
		List<Predicate> preOrList = new ArrayList<>();
		// 拼接where条件
		if (beginTime != null && !"".equals(beginTime)) {
			preAndList.add(cb.greaterThanOrEqualTo(opDate,
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString())));
		}
		if (endTime != null && !"".equals(endTime)) {
			preAndList.add(cb.lessThanOrEqualTo(opDate,
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString())));
		}

		Predicate[] preAdds = new Predicate[preAndList.size()];
		preAdds = preAndList.toArray(preAdds);
		Predicate[] preOrs = new Predicate[preOrList.size()];
		preOrs = preOrList.toArray(preOrs);

		Predicate and = cb.and(preAdds);
		cb.and(and);
		Predicate or = cb.or(preOrs);
		cb.or(preOrs);

		// 加上where条件
		if (preAdds.length < 1) {
			if (preOrs.length > 0) {
				query.where(or);
			}
		} else {
			if (preOrs.length > 0) {
				query.where(and, or);
			} else {
				query.where(and);
			}
		}

		query.groupBy(record.get("deptName"));

		// 指定查询项，select后面的东西
		query.multiselect(record.get("deptName"), cb.sum(borrowNum));

		TypedQuery<Object[]> q = this.entityManager.createQuery(query);

		List<CategorySummary> summaryList = new ArrayList<>();
		for (Object[] obj : q.getResultList()) {
			summaryList.add(new CategorySummary(String.valueOf(obj[0]), Integer.valueOf(String.valueOf(obj[1]))));
		}
		return summaryList;
	}
}