package com.dosth.tool.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.SubBoxAccountRef;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.entity.vo.SubBoxVo;
import com.dosth.tool.repository.SubBoxAccountRefRepository;
import com.dosth.tool.repository.SubBoxRepository;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.TempCabinetService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.google.common.collect.Lists;

/**
 * @description 暂存柜Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class TempCabinetServiceImpl implements TempCabinetService {

	public static final Logger logger = LoggerFactory.getLogger(TempCabinetServiceImpl.class);

	@Autowired
	private SubBoxRepository subBoxRepository;
	@Autowired
	private SubBoxAccountRefRepository subBoxAccountRefRepository;
	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private TimeTaskDetailRepository timeTaskDetailRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailUtil emailUtil;

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<SubBox> getSubBoxPage(int pageNo, int pageSize, String cabinetId) {
		Criteria<SubBox> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("equSettingId", cabinetId, true));
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(Direction.ASC, "equSettingId"));
		orders.add(new Order(Direction.ASC, "boxIndex"));
		orders.add(new Order(Direction.ASC, "rowNo"));
		orders.add(new Order(Direction.ASC, "colNo"));
		Page<SubBox> page = this.subBoxRepository.findAll(criteria,
				new PageRequest(pageNo, pageSize, new Sort(orders)));
		SubBoxAccountRef ref = null;
		MatEquInfo matInfo = null;
		List<SubCabinetDetail> detailList = null;
		SubCabinetDetail detail = null;
		float sumPrice = 0;
		for (SubBox box : page.getContent()) {
			ref = this.subBoxAccountRefRepository.getAccountBySubBoxId(box.getId());
			if (ref != null && ref.getUser() != null) {
				box.setUser(ref.getUser());
			}
			detailList = this.subCabinetDetailRepository.getSubDetailListBySubBoxId(box.getId());
			if (detailList != null && detailList.size() > 0) {
				detail = detailList.get(0);
				if (detail.getMatInfo() != null) {
					matInfo = detail.getMatInfo();
					box.setMatInfoId(matInfo.getId() != null ? matInfo.getId() : "-");
					box.setMatInfo(matInfo.getMatEquName() != null ? matInfo.getMatEquName() : "-");
					box.setBarCode(matInfo.getBarCode() != null ? matInfo.getBarCode() : "-");
					box.setSpec(matInfo.getSpec() != null ? matInfo.getSpec() : "-");
					box.setNum(detail.getNum() != null ? detail.getNum() : 0);
					box.setPrice(matInfo.getStorePrice() != null ? matInfo.getStorePrice() : 0);
					box.setManufacturerName(matInfo.getManufacturer() != null ? matInfo.getManufacturer().getManufacturerName() : "-");
					if (matInfo.getStorePrice() != null && detail.getNum() != null) {
						sumPrice = (float) (Math.round(matInfo.getStorePrice() * detail.getNum() * 10000)) / 10000;
					} else {
						sumPrice = 0;
					}
					box.setSumPrice(sumPrice);
				}
			}
		}
		return page;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public void export(HttpServletRequest request, HttpServletResponse response, String equSettingId) {
		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("库存明细");

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			fontStyle.setBold(true);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setFont(fontStyle);

			int colNo = 0;
			HSSFRow row = sheet.createRow(0);
			row.createCell(colNo).setCellValue("暂存柜名称");
			row.createCell(++colNo).setCellValue("索引号");
			row.createCell(++colNo).setCellValue("行号");
			row.createCell(++colNo).setCellValue("列号");
			row.createCell(++colNo).setCellValue("状态");
			row.createCell(++colNo).setCellValue("可存库位");
			row.createCell(++colNo).setCellValue("当前用户");
			row.createCell(++colNo).setCellValue("物料名称");
			row.createCell(++colNo).setCellValue("物料编号");
			row.createCell(++colNo).setCellValue("物料型号");
			row.createCell(++colNo).setCellValue("供应商");
			row.createCell(++colNo).setCellValue("存储数量");
			row.createCell(++colNo).setCellValue("单价");
			row.createCell(++colNo).setCellValue("金额");

			String equSettingName = null;
			MatEquInfo matInfo = null;
			SubBoxAccountRef ref = null;
			SubCabinetDetail detail = null;
			List<SubCabinetDetail> detailList = null;
			List<SubBox> boxList = this.subBoxRepository.getSubBoxListByCabinetId(equSettingId);

			boxList = boxList.stream().sorted(Comparator.comparingInt(SubBox::getBoxIndex))
					.collect(Collectors.toList());

			int rowNo = 1;
			int sumNum = 0;
			float price = 0;
			float sumStorePrice = 0;
			for (SubBox box : boxList) {
				colNo = 0;
				row = sheet.createRow(rowNo);

				equSettingName = box.getEquSetting() != null ? box.getEquSetting().getEquSettingName() : "";

				row.createCell(colNo).setCellValue(equSettingName);
				row.createCell(++colNo).setCellValue(box.getBoxIndex() != null ? box.getBoxIndex() : 0);
				row.createCell(++colNo).setCellValue(box.getRowNo() != null ? box.getRowNo() : 0);
				row.createCell(++colNo).setCellValue(box.getColNo() != null ? box.getColNo() : 0);
				row.createCell(++colNo).setCellValue(box.getCabinetSta() != null ? box.getCabinetSta().getMessage() : "");
				row.createCell(++colNo).setCellValue(box.getExtraNum() != null ? box.getExtraNum() : 0);

				ref = this.subBoxAccountRefRepository.getAccountBySubBoxId(box.getId());
				row.createCell(++colNo).setCellValue(ref != null ? ref.getUser().getUserName() : "");

				detailList = this.subCabinetDetailRepository.getSubDetailListBySubBoxId(box.getId());
				if (detailList != null && detailList.size() > 0) {
					detail = detailList.get(0);
					if (detail.getMatInfo() != null) {
						matInfo = detail.getMatInfo();
						row.createCell(++colNo)
								.setCellValue(matInfo.getMatEquName() != null ? matInfo.getMatEquName() : "");
						row.createCell(++colNo).setCellValue(matInfo.getBarCode() != null ? matInfo.getBarCode() : "");
						row.createCell(++colNo).setCellValue(matInfo.getSpec() != null ? matInfo.getSpec() : "");
						row.createCell(++colNo).setCellValue(matInfo.getManufacturer().getManufacturerName() != null ? matInfo.getManufacturer().getManufacturerName() : "-");
						row.createCell(++colNo).setCellValue(detail.getNum() != null ? detail.getNum() : 0);
						row.createCell(++colNo)
								.setCellValue(matInfo.getStorePrice() != null
										? (float) (Math.round(matInfo.getStorePrice() * 10000) / 10000)
										: 0);
						if (matInfo.getStorePrice() != null && detail.getNum() != null) {
							price = (float) (Math.round(matInfo.getStorePrice() * detail.getNum() * 10000)) / 10000;
						} else {
							price = 0;
						}

						row.createCell(++colNo).setCellValue(price);

						if (detail.getNum() != null) {
							sumNum += detail.getNum();
						}
						sumStorePrice += price;
					}
				}

				rowNo++;
			}

			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(8).setCellValue(sumNum);
			row.createCell(11).setCellValue((float) (Math.round(sumStorePrice * 10000)) / 10000);

			String fileName = FileUtil.processFileName(request, equSettingName + "-库存明细" + DateUtil.getDays());
			output = response.getOutputStream();
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
	public Page<SubBoxVo> getSubBoxVoPage(int pageNo, int pageSize, String cabinetId) {
		cabinetId = cabinetId.replaceAll("TEMP_", "");
		Criteria<SubBox> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("equSettingId", cabinetId, true));
		List<Order> orders = new ArrayList<>();
		orders.add(new Order(Direction.ASC, "equSettingId"));
		orders.add(new Order(Direction.ASC, "boxIndex"));
		orders.add(new Order(Direction.ASC, "rowNo"));
		orders.add(new Order(Direction.ASC, "colNo"));
		Page<SubBox> page = this.subBoxRepository.findAll(criteria,
				new PageRequest(pageNo, pageSize, new Sort(orders)));
		SubBoxAccountRef ref = null;
		MatEquInfo matInfo = null;
		List<SubCabinetDetail> detailList = null;
		SubCabinetDetail detail = null;
		for (SubBox box : page.getContent()) {
			ref = this.subBoxAccountRefRepository.getAccountBySubBoxId(box.getId());
			if (ref != null && ref.getUser() != null) {
				box.setUser(ref.getUser());
			}
			detailList = this.subCabinetDetailRepository.getSubDetailListBySubBoxId(box.getId());
			if (detailList != null && detailList.size() > 0) {
				detail = detailList.get(0);
				if (detail.getMatInfo() != null) {
					matInfo = detail.getMatInfo();
					box.setMatInfoId(matInfo.getId());
					box.setMatInfo(matInfo.getMatEquName());
					box.setBarCode(matInfo.getBarCode());
					box.setSpec(matInfo.getSpec());
					box.setNum(detail.getNum());
				}
			}
		}

		List<SubBoxVo> results = Lists.newArrayList();
		for (SubBox box : page.getContent()) {
			results.add(new SubBoxVo(box.getId(), box.getEquSetting().getEquSettingName(), box.getBoxIndex(),
					box.getRowNo(), box.getColNo(), box.getCabinetSta().getMessage(),
					box.getUser() != null ? box.getUser().getUserName() : "",
					box.getMatInfo() != null ? box.getMatInfo() : "", box.getBarCode() != null ? box.getBarCode() : "",
					box.getSpec() != null ? box.getSpec() : "", box.getNum() != null ? box.getNum() : 0));
		}
		Pageable pageable = new PageRequest(pageNo, pageSize);

		return new PageImpl<SubBoxVo>(results, pageable, page.getTotalElements());
	}

	@Override
	public void sendStockRecord(String equSettingId) {
		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("库存明细");

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			fontStyle.setBold(true);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setFont(fontStyle);

			int colNo = 0;
			HSSFRow row = sheet.createRow(0);
			row.createCell(colNo).setCellValue("暂存柜名称");
			row.createCell(++colNo).setCellValue("索引号");
			row.createCell(++colNo).setCellValue("行号");
			row.createCell(++colNo).setCellValue("列号");
			row.createCell(++colNo).setCellValue("当前用户");
			row.createCell(++colNo).setCellValue("物料名称");
			row.createCell(++colNo).setCellValue("物料编号");
			row.createCell(++colNo).setCellValue("物料型号");
			row.createCell(++colNo).setCellValue("存储数量");
			row.createCell(++colNo).setCellValue("包装单位");
			row.createCell(++colNo).setCellValue("单价");
			row.createCell(++colNo).setCellValue("金额");

			String equSettingName = null;
			MatEquInfo matInfo = null;
			SubBoxAccountRef ref = null;
			SubCabinetDetail detail = null;
			List<SubCabinetDetail> detailList = null;
			List<SubBox> boxList = this.subBoxRepository.getSubBoxListByCabinetId(equSettingId);

			boxList = boxList.stream().sorted(Comparator.comparingInt(SubBox::getBoxIndex))
					.collect(Collectors.toList());

			int rowNo = 1;
			int sumNum = 0;
			float price = 0;
			float sumStorePrice = 0;
			for (SubBox box : boxList) {
				colNo = 0;
				row = sheet.createRow(rowNo);

				equSettingName = box.getEquSetting() != null ? box.getEquSetting().getEquSettingName() : "";

				row.createCell(colNo).setCellValue(equSettingName);
				row.createCell(++colNo).setCellValue(box.getBoxIndex() != null ? box.getBoxIndex() : 0);
				row.createCell(++colNo).setCellValue(box.getRowNo() != null ? box.getRowNo() : 0);
				row.createCell(++colNo).setCellValue(box.getColNo() != null ? box.getColNo() : 0);

				ref = this.subBoxAccountRefRepository.getAccountBySubBoxId(box.getId());
				if (ref != null) {
					row.createCell(++colNo).setCellValue(ref.getUser() != null ? ref.getUser().getUserName() : "");
				}

				detailList = this.subCabinetDetailRepository.getSubDetailListBySubBoxId(box.getId());
				if (detailList != null && detailList.size() > 0) {
					detail = detailList.get(0);
					if (detail.getMatInfo() != null) {
						matInfo = detail.getMatInfo();
						row.createCell(++colNo)
								.setCellValue(matInfo.getMatEquName() != null ? matInfo.getMatEquName() : "");
						row.createCell(++colNo).setCellValue(matInfo.getBarCode() != null ? matInfo.getBarCode() : "");
						row.createCell(++colNo).setCellValue(matInfo.getSpec() != null ? matInfo.getSpec() : "");
						row.createCell(++colNo).setCellValue(detail.getNum() != null ? detail.getNum() : 0);
						row.createCell(++colNo)
								.setCellValue(matInfo.getPackUnit() != null ? matInfo.getPackUnit() : "");
						row.createCell(++colNo)
								.setCellValue(matInfo.getStorePrice() != null
										? (float) (Math.round(matInfo.getStorePrice() * 10000) / 10000)
										: 0);
						if (matInfo.getStorePrice() != null && detail.getNum() != null) {
							price = (float) (Math.round(matInfo.getStorePrice() * detail.getNum() * 10000)) / 10000;
						} else {
							price = 0;
						}

						row.createCell(++colNo).setCellValue(price);

						if (detail.getNum() != null) {
							sumNum += detail.getNum();
						}
						sumStorePrice += price;
					}
				}

				rowNo++;
			}

			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(8).setCellValue(sumNum);
			row.createCell(11).setCellValue((float) (Math.round(sumStorePrice * 10000)) / 10000);

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
			this.emailUtil.sendEmail(mailContent, "库存明细", filePath, equSettingName + "库存明细", mailList);
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