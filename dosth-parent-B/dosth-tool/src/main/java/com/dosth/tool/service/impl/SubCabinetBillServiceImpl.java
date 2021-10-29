package com.dosth.tool.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.tool.common.state.SystemSwitch;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.entity.SubCabinetBill;
import com.dosth.tool.repository.SubCabinetBillRepository;
import com.dosth.tool.service.SubCabinetBillService;
import com.dosth.tool.service.SystemSetupService;

/**
 * @description 副柜流水Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class SubCabinetBillServiceImpl implements SubCabinetBillService {

	@Autowired
	private SubCabinetBillRepository subCabinetBillRepository;
	@Autowired
	private SystemSetupService systemSetupService;

	@Override
	public Page<SubCabinetBill> getPage(int pageNo, int pageSize, String beginTime, String endTime, String info,
			String subBoxName, String inOrOut) throws DoSthException {
		String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		Criteria<SubCabinetBill> criteria = getInfo(beginTime, endTime, info, subBoxName, inOrOut);
		Page<SubCabinetBill> page = subCabinetBillRepository.findAll(criteria,
				new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "opDate")));
		// 检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if (page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber() + 1)) {
			pageNo = page.getTotalPages() - 1;
			page = this.subCabinetBillRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		}
		for (SubCabinetBill bill : page.getContent()) {
			if(valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
				float totalMoney=bill.getPrice() * bill.getNum() * bill.getMatInfo().getNum();
				bill.setTmpMoney(totalMoney);
			}else {
				bill.setTmpMoney(bill.getMoney());
			}
			bill.setPosition("ZC-" + bill.getSubBox().getBoxIndex());
		}
		return page;
	}

	@Override
	public void save(SubCabinetBill bill) throws DoSthException {
		this.subCabinetBillRepository.save(bill);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public SubCabinetBill get(Serializable id) throws DoSthException {
		return this.subCabinetBillRepository.getOne(id);
	}

	@Override
	public SubCabinetBill update(SubCabinetBill bill) throws DoSthException {
		return this.subCabinetBillRepository.saveAndFlush(bill);
	}

	@Override
	public void delete(SubCabinetBill bill) throws DoSthException {
		this.subCabinetBillRepository.delete(bill);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<SubCabinetBill> getLastInTime(String subBoxId, String matInfoId, String accountId) {
		return this.subCabinetBillRepository.getLastInTime(subBoxId, matInfoId, accountId);
	}

	// 查询条件
	public Criteria<SubCabinetBill> getInfo(String beginTime, String endTime, String info, String subBoxName,
			String inOrOut) {
		Criteria<SubCabinetBill> c = new Criteria<>();
		if (beginTime != null && !"".equals(beginTime)) {
			c.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime)) {
			c.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		if (info != null && !"".equals(info)) {
			c.add(Restrictions.or(new Criterion[] { Restrictions.like("matInfoName", info.trim(), true),
					Restrictions.like("barCode", info.trim(), true), Restrictions.like("spec", info.trim(), true) }));
		}
		if (subBoxName != null && !"".equals(subBoxName) && !subBoxName.equals("-1")) {
			c.add(Restrictions.like("subBoxName", subBoxName, true));
		}
		if (inOrOut != null && !"".equals(inOrOut) && !inOrOut.equals("-1")) {
			if(YesOrNo.YES.toString().equals(inOrOut)) {
				c.add(Restrictions.eq("inOrOut", YesOrNo.YES, true));
			} else {
				c.add(Restrictions.eq("inOrOut", YesOrNo.NO, true));
			}
		}
		return c;
	}

	@Override
	public String infoExport(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String info, String subBoxName,String inOrOut) throws IOException {
		Criteria<SubCabinetBill> criteria = getInfo(beginTime, endTime, info, subBoxName, inOrOut);
		List<SubCabinetBill> subCabinetBillList = subCabinetBillRepository.findAll(criteria);
	//	String valueByKey = systemSetupService.getValueByKey(SystemSwitch.MATERIAL_SWITCH.toString());
		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("暂存柜领用记录");
			HSSFRow row1 = sheet.createRow(0);
			HSSFCell cell = row1.createCell(0);

			cell.setCellValue("暂存柜领用记录");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));
			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cell.setCellStyle(cellStyle);
			row1.setRowStyle(cellStyle);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			cellStyle.setFont(fontStyle);
			int cellIndex = 0;
			HSSFRow row2 = sheet.createRow(1);
			row2.createCell(cellIndex).setCellValue("暂存柜名称");
			row2.createCell(++cellIndex).setCellValue("货位");
			row2.createCell(++cellIndex).setCellValue("物料名称");
			row2.createCell(++cellIndex).setCellValue("物料编号");
			row2.createCell(++cellIndex).setCellValue("物料型号");
			row2.createCell(++cellIndex).setCellValue("数量");
			row2.createCell(++cellIndex).setCellValue("单位");
			row2.createCell(++cellIndex).setCellValue("单价（元）");
			row2.createCell(++cellIndex).setCellValue("金额（元）");
			row2.createCell(++cellIndex).setCellValue("领用人员");
			row2.createCell(++cellIndex).setCellValue("取出/暂存");
			row2.createCell(++cellIndex).setCellValue("操作时间");

			int rowNo = 2;
			int sumBorrowNum = 0; // 总领取数量
			float sumMoney = 0f; // 总金额
			for (SubCabinetBill subCabinetBill : subCabinetBillList) {
				HSSFRow row3 = sheet.createRow(rowNo);
				cellIndex = 0;
				// 创建单元格并设置单元格内容
				row3.createCell(cellIndex).setCellValue(subCabinetBill.getSubBoxName() != null ? subCabinetBill.getSubBoxName() : "");
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getSubBox() != null ? "ZC-" + subCabinetBill.getSubBox().getBoxIndex() : "");
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getMatInfoName() != null ? subCabinetBill.getMatInfoName() : "");
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getBarCode() != null ? subCabinetBill.getBarCode() : "");
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getSpec() != null ? subCabinetBill.getSpec() : "");
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getNum() != null ? subCabinetBill.getNum() : 0);
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getBorrowType() != null ? subCabinetBill.getBorrowType() : "");
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getPrice() != null ? String.format("%s", subCabinetBill.getPrice()) : "0");
//				if(valueByKey != null && valueByKey.equals(TrueOrFalse.TRUE.toString())) {
//					row3.createCell(++cellIndex).setCellValue(subCabinetBill.getMoney() != null ? String.format("%s", subCabinetBill.getNum() * subCabinetBill.getPrice() * subCabinetBill.getMatInfo().getNum()) : "0");
//				}else {
					row3.createCell(++cellIndex).setCellValue(subCabinetBill.getMoney() != null ? String.format("%s", subCabinetBill.getMoney()) : "0");
			//	}
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getUserName() != null ? subCabinetBill.getUserName() : "");
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getInOrOut().getCode() != 0 ? "暂存" : "取出");
				row3.createCell(++cellIndex).setCellValue(subCabinetBill.getOpDate() != null ? DateUtil.getTime(subCabinetBill.getOpDate()) : "");

				if (subCabinetBill.getNum() != null && subCabinetBill.getNum() != 0) {
					sumBorrowNum += subCabinetBill.getNum();
				}

				if (subCabinetBill.getMoney() != null && subCabinetBill.getMoney() != 0) {
					sumMoney += subCabinetBill.getMoney();
				}

				rowNo++;
			}

			HSSFRow row4 = sheet.createRow(rowNo);
			row4.createCell(0).setCellValue("合计");
			row4.createCell(5).setCellValue(sumBorrowNum);
			row4.createCell(8).setCellValue(String.format("%s", sumMoney));

			String fileName = FileUtil.processFileName(request, "暂存柜领用记录" + DateUtil.getDays());
			output = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			response.setContentType("application/msexcel");
			response.setCharacterEncoding("utf-8");
			workbook.write(output);
		} finally {
			if (output != null) {
				output.flush();
				output.close();
			}
			if (workbook != null) {
				workbook.close();
			}
		}
		return null;
	}
}