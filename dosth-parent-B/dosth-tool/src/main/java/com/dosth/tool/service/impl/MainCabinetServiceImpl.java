package com.dosth.tool.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.common.util.StringUtil;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.SystemSwitch;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.entity.vo.EquDetailStaVo;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.MainCabinetService;
import com.dosth.tool.service.SystemSetupService;
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
public class MainCabinetServiceImpl implements MainCabinetService {

	public static final Logger logger = LoggerFactory.getLogger(MainCabinetServiceImpl.class);

	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
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
	public Page<EquDetailSta> getEquDetailStaPage(int pageNo, int pageSize, String cabinetId, String matInfo,
			String rowNo, String colNo) {
		Criteria<EquDetailSta> criteria = new Criteria<>();
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		if (matInfo != null && !"".equals(matInfo) && !matInfo.equals("-1")) {
			criteria.add(Restrictions.or(new Criterion[] { Restrictions.like("matInfo.barCode", matInfo.trim(), true),
					Restrictions.like("matInfo.matEquName", matInfo.trim(), true),
					Restrictions.like("matInfo.spec", matInfo.trim(), true) }));
		}
		if (cabinetId != null) {
			if (cabinetId.startsWith("E_")) {
				cabinetId = cabinetId.replaceAll("E_", "");
				List<EquSetting> equSettingMainList = this.equSettingRepository.getEquSettingListByEquInfoId(cabinetId);
				if (equSettingMainList != null && equSettingMainList.size() > 0) {
					List<EquSetting> equSettingList = this.equSettingRepository
							.getEquSettingList(equSettingMainList.get(0).getId());
					if (equSettingList != null && equSettingList.size() > 0) {
						List<Criterion> list = new ArrayList<>();
						for (EquSetting setting : equSettingList) {
							list.add(Restrictions.eq("equDetail.equSettingId", setting.getId(), true));
						}
						criteria.add(Restrictions.or(list.toArray(new Criterion[list.size()])));
					}
				}
			} else {
				criteria.add(Restrictions.eq("equDetail.equSettingId", cabinetId, true));
			}
		}

		if (StringUtil.isNotBlank(rowNo)) {
			criteria.add(Restrictions.eq("equDetail.rowNo", rowNo.trim(), true));
		}

		if (StringUtil.isNotBlank(colNo)) {
			criteria.add(Restrictions.eq("colNo", colNo.trim(), true));
		}

		List<Order> orders = new ArrayList<>();
		orders.add(new Order(Direction.ASC, "equDetail.equSetting"));
		orders.add(new Order(Direction.ASC, "equDetail.rowNo"));
		orders.add(new Order(Direction.ASC, "colNo"));
		Page<EquDetailSta> page = this.equDetailStaRepository.findAll(criteria,
				new PageRequest(pageNo, pageSize, new Sort(orders)));

		float sumPrice = 0f;
		for (EquDetailSta sta : page.getContent()) {
			sta.setRowNo(sta.getEquDetail() != null ? sta.getEquDetail().getRowNo() : 0);
			sta.setMatInfoName(sta.getMatInfo() != null ? sta.getMatInfo().getMatEquName() : "-");
			sta.setBarCode(sta.getMatInfo() != null ? sta.getMatInfo().getBarCode() : "-");
			sta.setSpec(sta.getMatInfo() != null ? sta.getMatInfo().getSpec() : "-");
			sta.setPackUnit(sta.getMatInfo() != null ? sta.getMatInfo().getPackUnit() : "-");
			sta.setPrice(sta.getMatInfo() != null ? sta.getMatInfo().getStorePrice() : 0);
			sta.setManufacturerName(
					sta.getMatInfo() != null ? sta.getMatInfo().getManufacturer().getManufacturerName() : "-");
			if (sta.getMatInfo() != null && sta.getCurNum() != null) {
				if (valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
					sumPrice = (float) (Math.round(
							sta.getMatInfo().getStorePrice() * sta.getCurNum() * sta.getMatInfo().getNum() * 10000))
							/ 10000;
				} else {
					sumPrice = (float) (Math.round(sta.getMatInfo().getStorePrice() * sta.getCurNum() * 10000)) / 10000;
				}
			} else {
				sumPrice = 0;
			}
			sta.setSumPrice(sumPrice);

		}
		return page;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public void export(HttpServletRequest request, HttpServletResponse response, String cabinetId) {
		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		try {
			HSSFSheet sheet = workbook.createSheet("库存明细");
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = row.createCell(0);

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cell.setCellStyle(cellStyle);
			row.setRowStyle(cellStyle);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			fontStyle.setBold(true);
			cellStyle.setFont(fontStyle);

			int colNo = 0;
			row.createCell(colNo).setCellValue("刀具柜名称");
			row.createCell(++colNo).setCellValue("行号");
			row.createCell(++colNo).setCellValue("列号");
			row.createCell(++colNo).setCellValue("物料名称");
			row.createCell(++colNo).setCellValue("物料编号");
			row.createCell(++colNo).setCellValue("物料型号");
			row.createCell(++colNo).setCellValue("供应商");
			row.createCell(++colNo).setCellValue("当前数量");
			row.createCell(++colNo).setCellValue("包装单位");
			row.createCell(++colNo).setCellValue("单价");
			row.createCell(++colNo).setCellValue("金额");
			row.createCell(++colNo).setCellValue("最大存储");
			row.createCell(++colNo).setCellValue("警告阀值");
			row.createCell(++colNo).setCellValue("最后上架时间");
			row.createCell(++colNo).setCellValue("状态");

			HSSFFont redFont = workbook.createFont();
			redFont.setFontName("宋体");
			redFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());

			HSSFCellStyle redStyle = workbook.createCellStyle();
			redStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			redStyle.setFont(redFont);

			List<EquDetailSta> staList = this.equDetailStaRepository.getEquDetailStaListBySettingId(cabinetId);

			int rowNo = 1;
			int sumCurNum = 0;
			float price = 0f;
			float sumStorePrice = 0f;
			String equSettingName = null;
			for (EquDetailSta sta : staList) {
				colNo = 0;
				row = sheet.createRow(rowNo);
				equSettingName = sta.getEquDetail() != null ? sta.getEquDetail().getEquSetting().getEquSettingName()
						: "";
				row.createCell(colNo).setCellValue(equSettingName);
				row.createCell(++colNo).setCellValue(sta.getEquDetail().getRowNo());
				row.createCell(++colNo).setCellValue(sta.getColNo());
				row.createCell(++colNo).setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getMatEquName() : "");
				row.createCell(++colNo).setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getBarCode() : "");
				row.createCell(++colNo).setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getSpec() : "");
				row.createCell(++colNo).setCellValue(
						sta.getMatInfo() != null ? sta.getMatInfo().getManufacturer().getManufacturerName() : "");
				row.createCell(++colNo).setCellValue(sta.getCurNum() != null ? sta.getCurNum() : 0);
				row.createCell(++colNo).setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getPackUnit() : "");
				row.createCell(++colNo)
						.setCellValue(sta.getMatInfo() != null
								? (float) (Math.round(sta.getMatInfo().getStorePrice() * 10000) / 10000)
								: 0);

				if (sta.getMatInfo() != null) {
					if (valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
						price = (float) (Math.round(
								sta.getMatInfo().getStorePrice() * sta.getCurNum() * sta.getMatInfo().getNum() * 10000))
								/ 10000;
					} else {
						price = (float) (Math.round(sta.getMatInfo().getStorePrice() * sta.getCurNum() * 10000))
								/ 10000;
					}
				} else {
					price = 0;
				}

				row.createCell(++colNo).setCellValue((float) (Math.round(price * 10000)) / 10000);

				// 库存总数
				if (sta.getCurNum() != null) {
					sumCurNum += sta.getCurNum();
				}
				// 库存金额合计
				sumStorePrice += price;

				if (sta.getCurNum() <= sta.getWarnVal()) {
					cell.setCellStyle(redStyle);
				}

				row.createCell(++colNo).setCellValue(sta.getMaxReserve() != null ? sta.getMaxReserve() : 0);
				row.createCell(++colNo).setCellValue(sta.getWarnVal() != null ? sta.getWarnVal() : 0);
				row.createCell(++colNo)
						.setCellValue(sta.getLastFeedTime() != null ? DateUtil.getTime(sta.getLastFeedTime()) : "");
				row.createCell(++colNo).setCellValue(sta.getStatus() != null ? sta.getStatus().getMessage() : "");

				rowNo++;
			}

			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(6).setCellValue(sumCurNum);
			row.createCell(10).setCellValue((float) (Math.round(sumStorePrice * 10000)) / 10000);

			String fileName = FileUtil.processFileName(request, equSettingName + "-库存明细" + DateUtil.getDays());
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

	@Override
	public Page<EquDetailStaVo> getEquDetailStaVoPage(int pageNo, int pageSize, String cabinetId) {
		cabinetId = cabinetId.replaceAll("MAIN_", "");

		Criteria<EquDetailSta> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("equDetail.equSettingId", cabinetId, true));
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));

		List<Order> orders = new ArrayList<>();
		orders.add(new Order(Direction.ASC, "equDetail.equSetting"));
		orders.add(new Order(Direction.ASC, "boxIndex"));
		orders.add(new Order(Direction.ASC, "equDetail.rowNo"));
		orders.add(new Order(Direction.ASC, "colNo"));

		Page<EquDetailSta> page = this.equDetailStaRepository.findAll(criteria,
				new PageRequest(pageNo, pageSize, new Sort(orders)));

		for (EquDetailSta sta : page.getContent()) {
			sta.setRowNo(sta.getEquDetail() != null ? sta.getEquDetail().getRowNo() : 0);
			sta.setBarCode(sta.getMatInfo() != null ? sta.getMatInfo().getBarCode() : "");
			sta.setSpec(sta.getMatInfo() != null ? sta.getMatInfo().getSpec() : "");
		}

		List<EquDetailStaVo> results = Lists.newArrayList();
		for (EquDetailSta sta : page.getContent()) {
			results.add(new EquDetailStaVo(sta.getId() != null ? sta.getId() : "",
					sta.getEquDetail() != null ? sta.getEquDetail().getEquSetting().getEquSettingName() : "",
					sta.getRowNo() != null ? sta.getRowNo() : 0, sta.getColNo() != null ? sta.getColNo() : 0,
					sta.getMatInfo() != null ? sta.getMatInfo().getMatEquName() : "",
					sta.getMatInfo() != null ? sta.getBarCode() : "", sta.getMatInfo() != null ? sta.getSpec() : "",
					sta.getMaxReserve() != null ? sta.getMaxReserve() : 0,
					sta.getWarnVal() != null ? sta.getWarnVal() : 0, sta.getCurNum() != null ? sta.getCurNum() : 0,
					sta.getLastFeedTime() != null ? sta.getLastFeedTime() : new Date(),
					sta.getStatus() != null ? sta.getStatus().getMessage() : ""));
		}
		Pageable pageable = new PageRequest(pageNo, pageSize);

		return new PageImpl<EquDetailStaVo>(results, pageable, page.getTotalElements());
	}

	@Override
	public void sendStockRecord(String cabinetId) {
		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		try {
			HSSFSheet sheet = workbook.createSheet("库存明细");
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = row.createCell(0);

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cell.setCellStyle(cellStyle);
			row.setRowStyle(cellStyle);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			fontStyle.setBold(true);
			cellStyle.setFont(fontStyle);

			int colNo = 0;
			row.createCell(colNo).setCellValue("刀具柜名称");
			row.createCell(++colNo).setCellValue("行号");
			row.createCell(++colNo).setCellValue("列号");
			row.createCell(++colNo).setCellValue("物料名称");
			row.createCell(++colNo).setCellValue("物料编号");
			row.createCell(++colNo).setCellValue("物料型号");
			row.createCell(++colNo).setCellValue("供应商");
			row.createCell(++colNo).setCellValue("当前数量");
			row.createCell(++colNo).setCellValue("包装单位");
			row.createCell(++colNo).setCellValue("单价");
			row.createCell(++colNo).setCellValue("金额");
			row.createCell(++colNo).setCellValue("最大存储");
			row.createCell(++colNo).setCellValue("警告阀值");
			row.createCell(++colNo).setCellValue("最后上架时间");

			HSSFFont redFont = workbook.createFont();
			redFont.setFontName("宋体");
			redFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());

			HSSFCellStyle redStyle = workbook.createCellStyle();
			redStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			redStyle.setFont(redFont);

			List<EquDetailSta> staList = this.equDetailStaRepository.getEquDetailStaListBySettingId(cabinetId);

			int rowNo = 1;
			int sumCurNum = 0;
			float price = 0f;
			float sumStorePrice = 0f;
			String equSettingName = null;
			for (EquDetailSta sta : staList) {
				colNo = 0;
				row = sheet.createRow(rowNo);
				equSettingName = sta.getEquDetail() != null ? sta.getEquDetail().getEquSetting().getEquSettingName()
						: "";
				row.createCell(colNo).setCellValue(equSettingName);
				row.createCell(++colNo).setCellValue(sta.getEquDetail().getRowNo());
				row.createCell(++colNo).setCellValue(sta.getColNo());
				row.createCell(++colNo).setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getMatEquName() : "");
				row.createCell(++colNo).setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getBarCode() : "");
				row.createCell(++colNo).setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getSpec() : "");
				row.createCell(++colNo).setCellValue(
						sta.getMatInfo() != null ? sta.getMatInfo().getManufacturer().getManufacturerName() : "");
				row.createCell(++colNo).setCellValue(sta.getCurNum() != null ? sta.getCurNum() : 0);
				row.createCell(++colNo).setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getPackUnit() : "");
				row.createCell(++colNo)
						.setCellValue(sta.getMatInfo() != null
								? (float) (Math.round(sta.getMatInfo().getStorePrice() * 10000) / 10000)
								: 0);

				if (sta.getMatInfo() != null) {
					if (valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
						price = (float) (Math.round(
								sta.getMatInfo().getStorePrice() * sta.getCurNum() * sta.getMatInfo().getNum() * 10000))
								/ 10000;
					} else {
						price = (float) (Math.round(sta.getMatInfo().getStorePrice() * sta.getCurNum() * 10000))
								/ 10000;
					}
				} else {
					price = 0;
				}

				row.createCell(++colNo).setCellValue((float) (Math.round(price * 10000)) / 10000);

				// 库存总数
				if (sta.getCurNum() != null) {
					sumCurNum += sta.getCurNum();
				}
				// 库存金额合计
				sumStorePrice += price;

				if (sta.getCurNum() <= sta.getWarnVal()) {
					cell.setCellStyle(redStyle);
				}
				row.createCell(++colNo).setCellValue(sta.getMaxReserve() != null ? sta.getMaxReserve() : 0);
				row.createCell(++colNo).setCellValue(sta.getWarnVal() != null ? sta.getWarnVal() : 0);
				row.createCell(++colNo)
						.setCellValue(sta.getLastFeedTime() != null ? DateUtil.getTime(sta.getLastFeedTime()) : "");
				rowNo++;
			}

			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(6).setCellValue(sumCurNum);
			row.createCell(9).setCellValue((float) (Math.round(sumStorePrice * 10000)) / 10000);

			String filePath = this.toolProperties.getTmpUploadPath() + "库存明细-" + DateUtil.getAllTime() + ".xls";
			File file = new File(filePath);
			output = new FileOutputStream(file);
			workbook.write(output);
			if (!file.exists()) {
				logger.error("生成库存明细异常,请刷新后再试!");
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

			String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;附件为库存明细,请查收!</p>";
			this.emailUtil.sendEmail(mailContent, "库存明细", filePath, equSettingName + "-库存明细", mailList);
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