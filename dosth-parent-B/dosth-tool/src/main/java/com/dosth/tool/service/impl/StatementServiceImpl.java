package com.dosth.tool.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.common.util.NumUtil;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.entity.Statement;
import com.dosth.tool.entity.StatementDetail;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.repository.MatUseRecordRepository;
import com.dosth.tool.repository.StatementDetailRepository;
import com.dosth.tool.repository.StatementRepository;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.service.StatementService;

/**
 * @description 供应商核对Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class StatementServiceImpl implements StatementService {

	@Autowired
	private StatementRepository statementRepository;
	@Autowired
	private MatUseRecordRepository matUseRecordRepository;
	@Autowired
	private StatementDetailRepository statementDetailRepository;
	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;

	@Override
	public void save(Statement statement) throws DoSthException {
		this.statementRepository.save(statement);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Statement get(Serializable id) throws DoSthException {
		Statement statement = this.statementRepository.getOne(id);
		return statement;
	}

	@Override
	public Statement update(Statement statement) throws DoSthException {
		return this.statementRepository.saveAndFlush(statement);
	}

	@Override
	public void delete(Statement statement) throws DoSthException {
		this.statementRepository.delete(statement);
	}

	@Override
	public void statement(String manufacturerId, String startDate, String endDate) {
		Date beginTime = DateUtil.parseTime(new StringBuilder(startDate).append(" 00:00:00").toString());
		Date endTime = DateUtil.parseTime(new StringBuilder(endDate).append(" 23:59:59").toString());
		
		// 获取所有核对信息
		List<Statement> oldList = this.statementRepository.findAll();
		// 已核对列表
		List<Statement> alreadyList;
		// 获取未核对,并清空
		List<Statement> wait = oldList.stream().filter(old -> YesOrNo.NO.equals(old.getIsHD())).collect(Collectors.toList());
		List<StatementDetail> detailList;
		for (Statement statement : wait) {
			detailList = this.statementDetailRepository.getStatementDetailByStatementId(statement.getId());
			for (StatementDetail detail : detailList) {
				this.statementDetailRepository.delete(detail);
			}
			this.statementRepository.delete(statement);
		}
		// 获取未核算
		List<MatUseRecord> matUseRecordList = this.matUseRecordRepository.getUnStatementList(manufacturerId, beginTime, endTime);
		// 区间内存在未核对的记录,则重置未核对记录
		if (matUseRecordList != null && matUseRecordList.size() > 0) {
			Statement statement;
			// 暂存柜流水列表
			List<SubCabinetDetail> subDetailList = this.subCabinetDetailRepository.findAll();
			// 上期结余
			int balance;
			// 主柜按物料、价格分组
			Map<MatEquInfo, Map<Float, List<MatUseRecord>>> map = matUseRecordList.stream()
					.filter(record -> record.getRealNum() > 0)
					.collect(Collectors.groupingBy(MatUseRecord::getMatInfo, Collectors.groupingBy(MatUseRecord::getPrice)));
			for (Entry<MatEquInfo, Map<Float, List<MatUseRecord>>> mat : map.entrySet()) {
				for (Entry<Float, List<MatUseRecord>> price : mat.getValue().entrySet()) {
					statement = new Statement(mat.getKey().getId(), beginTime, endTime, 0, 0, 0, 0, price.getKey(), 0F);
					statement.setMatInfo(mat.getKey());
					statement.setIsHD(YesOrNo.NO);
					statement = this.statementRepository.save(statement);
					for (MatUseRecord record : price.getValue()) {
						this.statementDetailRepository.save(new StatementDetail(statement.getId(), record.getId()));
					}
					// 上期结余，最后核对时间的暂存数量
					alreadyList = oldList.stream().filter(old -> YesOrNo.YES.equals(old.getIsHD())
							&& old.getMatInfoId().equals(mat.getKey().getId())).collect(Collectors.toList());
					balance = 0;
					if (alreadyList != null && alreadyList.size() > 0) {
						alreadyList.sort((s1, s2) -> (int) (s1.getOpDate().getTime() - s2.getOpDate().getTime()));
						balance = alreadyList.get(0).getTempNum();
						statement.setBalance(balance);
					}
					// 领取数量
					statement.setOuterNum(price.getValue().stream().filter(record -> record.getRealNum() > 0).mapToInt(MatUseRecord::getRealNum).sum());
					// 暂存数量
					statement.setTempNum(subDetailList.stream().filter(detail -> detail.getMatInfoId().equals(mat.getKey().getId())).mapToInt(SubCabinetDetail::getNum).sum());
					// 核对数量
					statement.setInventoryNum(statement.getOuterNum() + balance - statement.getTempNum());
					// 金额
					statement.setCost(statement.getInventoryNum() * price.getKey());
				}
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Statement> getStatementList(String manufacturerId, String startDate, String endDate)
			throws DoSthException {
		List<Statement> result = new ArrayList<>();
		Date beginTime = DateUtil.parseTime(new StringBuilder(startDate).append(" 00:00:00").toString());
		Date endTime = DateUtil.parseTime(new StringBuilder(endDate).append(" 23:59:59").toString());
		List<Statement> statementList;
		// 已核对
		statementList = this.statementRepository.getStatementListAlready(manufacturerId, beginTime, endTime);
		if (statementList != null && statementList.size() > 0) {
			result.addAll(statementList);
		}
		// 未核对
		statementList = this.statementRepository.getUnStatementList();
		if (statementList != null && statementList.size() > 0) {
			result.addAll(statementList);
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<Statement> getPager(int pageNo, int pageSize, String startDate, String endDate) throws DoSthException {
		Criteria<Statement> criteria = new Criteria<>();
		if (startDate != null && !"".equals(startDate)) {
			criteria.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(startDate).append(" 00:00:00").toString()), true));
		}
		if (endDate != null && !"".equals(endDate)) {
			criteria.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endDate).append(" 23:59:59").toString()), true));
		}
		return this.statementRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
	}

	@Override
	public List<MatUseRecord> getMatUseRecordView(String statementId) {
		return this.statementDetailRepository.getMatUseRecordViewByStatementId(statementId);
	}
	
	@Override
	public void exportDetail(String statementId, HttpServletRequest request, HttpServletResponse response) {
		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("核对明细");
			HSSFRow row1 = sheet.createRow(0);
			HSSFCell cell = row1.createCell(0);

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
			row1.createCell(cellIndex).setCellValue("序号");
			row1.createCell(++cellIndex).setCellValue("编码");
			row1.createCell(++cellIndex).setCellValue("名称");
			row1.createCell(++cellIndex).setCellValue("规格");
			row1.createCell(++cellIndex).setCellValue("数量");
			row1.createCell(++cellIndex).setCellValue("单价");
			row1.createCell(++cellIndex).setCellValue("金额");
			row1.createCell(++cellIndex).setCellValue("领用人");
			row1.createCell(++cellIndex).setCellValue("领用时间");

			List<MatUseRecord>recordList = this.statementDetailRepository.getMatUseRecordViewByStatementId(statementId);
			recordList = recordList.stream().filter(record -> record.getRealNum() > 0).collect(Collectors.toList());
			
			int rowNo = 1;
			int sumRealNum = 0;
			Float sumMoney = 0F;
			HSSFRow row;
			for (MatUseRecord record : recordList) {
				row = sheet.createRow(rowNo);
				cellIndex = 0;
				row.createCell(cellIndex).setCellValue(rowNo);
				row.createCell(++cellIndex).setCellValue(record.getMatInfo().getBarCode());
				row.createCell(++cellIndex).setCellValue(record.getMatInfo().getMatEquName());
				row.createCell(++cellIndex).setCellValue(record.getMatInfo().getSpec());
				row.createCell(++cellIndex).setCellValue(record.getRealNum());
				row.createCell(++cellIndex).setCellValue(NumUtil.keep2Point(record.getPrice()));
				row.createCell(++cellIndex).setCellValue(NumUtil.keep2Point(record.getMoney()));
				row.createCell(++cellIndex).setCellValue(record.getUserName());
				row.createCell(++cellIndex).setCellValue(DateUtil.getTime(record.getOpDate()));
				
				sumRealNum += record.getRealNum();
				sumMoney += record.getMoney();
				rowNo++;
			}

			row = sheet.createRow(rowNo);
			row.createCell(0).setCellValue("合计");
			row.createCell(4).setCellValue(sumRealNum);
			row.createCell(6).setCellValue(NumUtil.keep2Point(sumMoney));

			String fileName = FileUtil.processFileName(request, "核对明细记录" + DateUtil.getDays());
			output = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			response.setContentType("application/msexcel");
			response.setCharacterEncoding("utf-8");
			workbook.write(output);
		} catch (IOException e) {
			e.printStackTrace();
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