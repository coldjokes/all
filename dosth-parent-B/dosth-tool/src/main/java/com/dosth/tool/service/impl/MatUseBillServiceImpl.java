package com.dosth.tool.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cnbaosi.dto.tool.FeignCodeName;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.DateUtil;
import com.dosth.tool.common.state.MatEqu;
import com.dosth.tool.common.state.ReceiveType;
import com.dosth.tool.common.util.CabinetInfoUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.MatUseBillRepository;
import com.dosth.tool.service.MatUseBillService;
import com.dosth.toolcabinet.dto.BillInfo;
import com.dosth.toolcabinet.dto.MatDetail;

/**
 * @description 物料使用流水Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class MatUseBillServiceImpl implements MatUseBillService {

	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private MatUseBillRepository matUseBillRepository;

	@Override
	public void save(MatUseBill matUseBill) throws DoSthException {
		this.matUseBillRepository.save(matUseBill);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<MatUseBill> getUnReturnList(String accountId) throws DoSthException {
		List<MatUseBill> matUseBillList = this.matUseBillRepository.getUnReturnback(accountId);
		return matUseBillList;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public MatUseBill get(Serializable id) throws DoSthException {
		return this.matUseBillRepository.findOne(id);
	}

	@Override
	public MatUseBill update(MatUseBill bill) throws DoSthException {
		return this.matUseBillRepository.saveAndFlush(bill);
	}

	@Override
	public void delete(MatUseBill bill) throws DoSthException {
		this.matUseBillRepository.delete(bill);
	}

	@Override
	public Page<MatUseBill> getPage(int pageNo, int pageSize, String beginTime, String endTime, String receiveType) throws DoSthException {
		Criteria<MatUseBill> c = new Criteria<>();
		if (beginTime != null && !"".equals(beginTime)) {
			c.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime)) {
			c.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		if (receiveType != null && !"".equals(receiveType) && !"-1".equals(receiveType)) {
			c.add(Restrictions.eq("matUseRecord.receiveType", ReceiveType.valueOf(receiveType), true));
		}
		Page<MatUseBill> page = this.matUseBillRepository.findAll(c, new PageRequest(pageNo, pageSize));
		for (MatUseBill bill : page.getContent()) {
			bill.setBarCode(bill.getBarCode());
			bill.setSpec(bill.getSpec());
		}
		return page;
	}

	@Override
	public void export(HttpServletResponse response, String cabinetId, String beginTime, String endTime) {
		Criteria<MatUseBill> criteria = new Criteria<>();
		List<Criterion> list = new ArrayList<>();
		if (cabinetId != null && !"".equals(cabinetId) && !cabinetId.toUpperCase().equals(MatEqu.STORAGE.name())) {
			if (cabinetId.startsWith("E_")) {
				cabinetId = cabinetId.replaceAll("E_", "");
				List<EquSetting> equSettingMainList = this.equSettingRepository.getEquSettingListByEquInfoId(cabinetId);
				if (equSettingMainList != null && equSettingMainList.size() > 0) {
					// 根据主柜Id获取级联柜子集合
					List<EquSetting> equSettingList = this.equSettingRepository.getEquSettingList(equSettingMainList.get(0).getId());
					List<EquDetailSta> equDetailStaList = null;
					if (equSettingList != null && equSettingList.size() > 0) {
						for (EquSetting setting : equSettingList) {
							equDetailStaList = this.equDetailStaRepository.getEquDetailStaListBySettingId(setting.getId());
							for (EquDetailSta sta : equDetailStaList) {
								list.add(Restrictions.eq("equDetailStaId", sta.getId(), true));
							}
						}
						criteria.add(Restrictions.or(list.toArray(new Criterion[list.size()])));
					}
				}
			} else {
				list.add(Restrictions.eq("equDetail.equSettingId", cabinetId, true));
			}
		}
		if (beginTime != null && !"".equals(beginTime) && !"1900-01-01".equals(beginTime)) {
			list.add(Restrictions.gte("opDate", DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime) && !"9999-12-31".equals(endTime)) {
			list.add(Restrictions.lte("opDate", DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		List<MatUseBill> billList = this.matUseBillRepository.findAll(criteria);
		OutputStream output = null;
		HSSFRow row3 = null;
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			// 建立新的sheet对象（excel的表单）
			HSSFSheet sheet = workbook.createSheet("领用详情表");
			// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
			HSSFRow row1 = sheet.createRow(0);
			// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
			HSSFCell cell = row1.createCell(0);
	
			// 设置单元格内容
			cell.setCellValue("领用详情表");
			// 合并单元格CellRangeAddress构造参数依次表示起始行，截止行，起始列， 截止列
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
			// 设置缺省列高
			sheet.setDefaultRowHeightInPoints(20);
			// 设置缺省列宽
			sheet.setDefaultColumnWidth(20);
	
			// 实例化样式对象
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			// 垂直居中
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			// 将样式应用于单元格
			cell.setCellStyle(cellStyle);
			// 将样式应用到行
			row1.setRowStyle(cellStyle);
	
			// 实例化字体对象
			HSSFFont fontStyle = workbook.createFont();
			// 字体
			fontStyle.setFontName("宋体");
			// 将字体应用于单元格样式中
			cellStyle.setFont(fontStyle);
	
			// 在sheet里创建第二行
			HSSFRow row2 = sheet.createRow(1);
			row2.createCell(0).setCellValue("设备详情");
			row2.createCell(1).setCellValue("物料名称");
			row2.createCell(2).setCellValue("领用数量");
			row2.createCell(3).setCellValue("领用类型");
			row2.createCell(4).setCellValue("领用类型明细");
			row2.createCell(5).setCellValue("价格");
			row2.createCell(6).setCellValue("领用人员");
			row2.createCell(7).setCellValue("领用时间");
	
			int rowNo = 2;
			for (MatUseBill bill : billList) {
				row3 = sheet.createRow(rowNo);
				// 创建单元格并设置单元格内容
				row3.createCell(0).setCellValue(CabinetInfoUtil.createCabinetInfoByEquDetailSta(bill.getEquDetailSta()));
				row3.createCell(1).setCellValue(bill.getEquDetailSta().getMatInfo().getMatEquName());
				row3.createCell(2).setCellValue(bill.getBorrowNum());
				row3.createCell(3).setCellValue(bill.getMatUseRecord().getReceiveType().getMessage());
				row3.createCell(4).setCellValue(bill.getMatUseRecord().getReceiveInfo());
				row3.createCell(5).setCellValue(bill.getStorePrice());
				row3.createCell(6).setCellValue(bill.getMatUseRecord().getUserName());
				row3.createCell(7).setCellValue(DateUtil.getTime(bill.getOpDate()));
				rowNo++;
			}
			// Excel文件名
			String fileName = "borrowList" +  DateUtil.getAllTime();
			// 取得输出流
			output = response.getOutputStream();
			// 清空输出流
//			response.reset();
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
	public BillInfo getBillInfo(String billId) throws DoSthException {
		MatDetail matDetail = new MatDetail();
		MatUseBill useBill = this.matUseBillRepository.getOne(billId);
		if(useBill.getMatUseRecord().getMatInfo() != null) {
			matDetail = new MatDetail(useBill.getMatInfoId(), useBill.getMatEquName(), 
					useBill.getBarCode(), useBill.getSpec(), useBill.getMatUseRecord().getMatInfo().getNum(),
					0, useBill.getMatUseRecord().getMatInfo().getBorrowType().name(), null);
		}
		return new BillInfo(useBill.getBorrowNum(), useBill.getAccountId(), useBill.getMatUseRecord().getReceiveInfo(), 
				useBill.getMatUseRecord().getReceiveType().name(), matDetail);
	}

	@Override
	public List<FeignCodeName> getUnReturnTypeList(String accountId, String cabinetId) throws DoSthException {
		List<FeignCodeName> list = new ArrayList<>();
		List<MatCategoryTree> treeList = this.matUseBillRepository.getUnReturnTypeList(accountId, cabinetId);
		for (MatCategoryTree tree : treeList) {
			list.add(new FeignCodeName(tree.getId(), tree.getName()));
		}
		return list;
	}
}