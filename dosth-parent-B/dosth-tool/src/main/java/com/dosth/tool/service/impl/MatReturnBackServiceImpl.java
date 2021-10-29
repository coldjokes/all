
package com.dosth.tool.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
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
import com.cnbaosi.dto.tool.FeignReturnBack;
import com.dosth.common.constant.IsReturnBack;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.AuditStatus;
import com.dosth.tool.common.state.BackType;
import com.dosth.tool.common.state.DataSyncType;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.entity.DataSyncState;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.MatReturnBack;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.DataSyncStateRepository;
import com.dosth.tool.repository.MatReturnBackRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.MatReturnBackService;
import com.dosth.tool.service.MatReturnDetailService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.toolcabinet.enums.EnumBorrowType;
import com.google.common.collect.Lists;

/**
 * 
 * @description 物料归还Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class MatReturnBackServiceImpl implements MatReturnBackService {
	
	private static final Logger logger = LoggerFactory.getLogger(MatReturnBackServiceImpl.class);

	@Autowired
	private MatReturnBackRepository matReturnBackRepository;
	@Autowired
	private DataSyncStateRepository dataSyncStateRepository;
	@Autowired
	private MatReturnDetailService matReturnDetailService;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private TimeTaskDetailRepository timeTaskDetailRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailUtil emailUtil;

	@Override
	public Page<MatReturnBack> getPage(int pageNo, int pageSize, String beginTime, String endTime, String userName,
			String status, String backType, String equSettingName) throws DoSthException {
		// 分页信息
		Criteria<MatReturnBack> criteria = getInfo(beginTime, endTime, userName, status, backType, equSettingName);
		Page<MatReturnBack> page = this.matReturnBackRepository.findAll(criteria,
				new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "opDate")));
		// 检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if (page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber() + 1)) {
			pageNo = page.getTotalPages() - 1;
			page = this.matReturnBackRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		}
		for (MatReturnBack info : page.getContent()) {
			MatUseRecord matUseRecord = info.getMatUseBill().getMatUseRecord();
			info.setKnifeId(matUseRecord.getBarCode() != null ? matUseRecord.getBarCode() : "");
			info.setKnifeSpec(matUseRecord.getSpec() != null ? matUseRecord.getSpec() : "");
			info.setPackageNum(matUseRecord.getPackNum() != null ? matUseRecord.getPackNum() : null);
			info.setReceiveType(matUseRecord.getReceiveType().getMessage() != null ? matUseRecord.getReceiveType().getMessage() : "");
			info.setReceiveInfo(matUseRecord.getTree() == null ? EnumBorrowType.GRID.getDesc() : matUseRecord.getTree().getfName());
			info.setMatLife(matUseRecord.getMatInfo().getUseLife() != null ? matUseRecord.getMatInfo().getUseLife() : null);
			info.setSurplusLife(matUseRecord.getMatInfo().getUseLife() != null ? matUseRecord.getMatInfo().getUseLife() - info.getRealLife() : null);
			info.setUserName(matUseRecord.getUserName() != null ? matUseRecord.getUserName() : "");
		}
		return page;
	}

	@Override
	public void save(MatReturnBack returnBack) throws DoSthException {
		this.matReturnBackRepository.save(returnBack);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public MatReturnBack get(Serializable id) throws DoSthException {
		return this.matReturnBackRepository.findOne(id);
	}

	@Override
	public MatReturnBack update(MatReturnBack returnBack) throws DoSthException {
		return this.matReturnBackRepository.saveAndFlush(returnBack);
	}

	@Override
	public void delete(MatReturnBack returnBack) throws DoSthException {
		this.matReturnBackRepository.delete(returnBack);
	}

	@Override
	public String infoExport(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String userName, String status, String backType, String equSettingName) throws IOException {

		Criteria<MatReturnBack> criteria = getInfo(beginTime, endTime, userName, status, backType, equSettingName);
		List<MatReturnBack> matReturnBackList = this.matReturnBackRepository.findAll(criteria);

		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("归还记录");
			HSSFRow row1 = sheet.createRow(0);
			HSSFCell cell = row1.createCell(0);

			cell.setCellValue("归还记录");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cell.setCellStyle(cellStyle);
			row1.setRowStyle(cellStyle);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			cellStyle.setFont(fontStyle);
			
			int cellIndex = 0;
			HSSFRow row2 = sheet.createRow(1);
			row2.createCell(cellIndex).setCellValue("刀具柜名称");
			row2.createCell(++cellIndex).setCellValue("归还流水号");
			row2.createCell(++cellIndex).setCellValue("物料名称");
			row2.createCell(++cellIndex).setCellValue("物料编号");
			row2.createCell(++cellIndex).setCellValue("物料型号");
			row2.createCell(++cellIndex).setCellValue("归还详情");
			row2.createCell(++cellIndex).setCellValue("领用类型");
			row2.createCell(++cellIndex).setCellValue("领用途径");
			row2.createCell(++cellIndex).setCellValue("使用寿命");
			row2.createCell(++cellIndex).setCellValue("制造产量");
			row2.createCell(++cellIndex).setCellValue("剩余寿命");
			row2.createCell(++cellIndex).setCellValue("产品流水号");
			row2.createCell(++cellIndex).setCellValue("归还人员");
			row2.createCell(++cellIndex).setCellValue("归还时间");
			row2.createCell(++cellIndex).setCellValue("审核人员");
			row2.createCell(++cellIndex).setCellValue("审核方式");
			row2.createCell(++cellIndex).setCellValue("审核时间");
			row2.createCell(++cellIndex).setCellValue("应还数量");
			row2.createCell(++cellIndex).setCellValue("实际数量");
			row2.createCell(++cellIndex).setCellValue("审核状态");
			row2.createCell(++cellIndex).setCellValue("备注");
			row2.createCell(++cellIndex).setCellValue("归还码");
			row2.createCell(++cellIndex).setCellValue("归还方式");
			row2.createCell(++cellIndex).setCellValue("是否归还");
			row2.createCell(++cellIndex).setCellValue("以旧换新");
			row2.createCell(++cellIndex).setCellValue("归还类型");

			int rowNo = 2;
			int sumBillBackNum = 0; // 应还数量
			int sumBillNum = 0; // 实际数量
			MatEquInfo matInfo = null;
			for (MatReturnBack returnBack : matReturnBackList) {
				HSSFRow row3 = sheet.createRow(rowNo);
				cellIndex = 0;
				int num = 0;
				if (returnBack.getNum() != null) {
					num = returnBack.getNum();
				}
				
				matInfo = returnBack.getMatUseBill().getMatUseRecord().getMatInfo();
				
				// 创建单元格并设置单元格内容
				row3.createCell(cellIndex).setCellValue(returnBack.getEquSettingName() != null ? returnBack.getEquSettingName() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getReturnBackNo() != null ? returnBack.getReturnBackNo().toString() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getMatUseBill() != null ? returnBack.getMatUseBill().getMatUseRecord().getMatInfoName() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getMatUseBill() != null ? returnBack.getMatUseBill().getMatUseRecord().getBarCode() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getMatUseBill() != null ? returnBack.getMatUseBill().getMatUseRecord().getSpec() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getReturnDetailInfo() != null ? returnBack.getReturnDetailInfo() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getMatUseBill().getMatUseRecord() != null ? returnBack.getMatUseBill().getMatUseRecord().getReceiveType().getMessage() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getMatUseBill().getMatUseRecord().getTree() != null ? returnBack.getMatUseBill().getMatUseRecord().getTree().getfName() : EnumBorrowType.GRID.getDesc() );
				row3.createCell(++cellIndex).setCellValue(matInfo.getUseLife() != null ? matInfo.getUseLife().toString() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getRealLife() != null ? returnBack.getRealLife().toString() : "");
				row3.createCell(++cellIndex).setCellValue(matInfo.getUseLife() != null && returnBack.getRealLife() != null ? String.valueOf(matInfo.getUseLife() - returnBack.getRealLife()) : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getSerialNum() != null ? returnBack.getSerialNum() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getUser() != null ? returnBack.getUser().getUserName() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getOpDate() != null ? DateUtil.getTime(returnBack.getOpDate()) : DateUtil.getTime());
				row3.createCell(++cellIndex).setCellValue(returnBack.getConfirmUser());
				row3.createCell(++cellIndex).setCellValue(returnBack.getConfirmMode() != null ? returnBack.getConfirmMode().getMessage() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getConfirmDate() != null ? DateUtil.getTime(returnBack.getConfirmDate()) : DateUtil.getTime());
				row3.createCell(++cellIndex).setCellValue(returnBack.getMatUseBill() != null ? returnBack.getMatUseBill().getMatUseRecord().getPackNum().toString() : "");
				row3.createCell(++cellIndex).setCellValue(String.valueOf(num) != null ? String.valueOf(num) : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getAuditStatus() != null ? returnBack.getAuditStatus().getMessage() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getRemark() != null ? returnBack.getRemark() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getBarCode() != null ? returnBack.getBarCode() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getBackWay() != null ? returnBack.getBackWay().getMessage() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getIsReturnBack() != null ? returnBack.getIsReturnBack().getMessage() : "");
				row3.createCell(++cellIndex).setCellValue("0".equals(returnBack.getIsGetNewOne()) ? "否" : "是");
				String returnBackType = null;
				if(BackType.PULLOFF.toString().equals(returnBack.getReturnBackType())) {
					returnBackType = "下架";
				} else if (BackType.NORMAL.toString().equals(returnBack.getReturnBackType())) {
					returnBackType = "正常归还";
				} else if (BackType.ABNORMAL.toString().equals(returnBack.getReturnBackType())) {
					returnBackType = "异常归还";
				}
				row3.createCell(++cellIndex).setCellValue(returnBackType);

				sumBillBackNum += returnBack.getMatUseBill().getMatUseRecord().getPackNum();
				sumBillNum += num;

				rowNo++;
			}

			HSSFRow row4 = sheet.createRow(rowNo);
			row4.createCell(0).setCellValue("合计");
			row4.createCell(17).setCellValue(sumBillBackNum);
			row4.createCell(18).setCellValue(sumBillNum);

			String fileName = FileUtil.processFileName(request, "归还记录" + DateUtil.getDays());
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

	// 查询条件
	public Criteria<MatReturnBack> getInfo(String beginTime, String endTime, String userName, String status,
			String backType, String equSettingName) {
		Criteria<MatReturnBack> c = new Criteria<>();
		c.add(Restrictions.eq("isReturnBack", IsReturnBack.ISBACK, true));
		if (beginTime != null && !"".equals(beginTime)) {
			c.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime)) {
			c.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		if (userName != null && !"".equals(userName)) {
			c.add(Restrictions.eq("user.userName", userName, true));
		}
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			c.add(Restrictions.eq("auditStatus", AuditStatus.valueOf(status), true));
		}
		if (backType != null && !"".equals(backType) && !"-1".equals(backType)) {
			List<String> ids = matReturnDetailService.getReturnIds(backType);
			c.add(Restrictions.in("id", ids, false));
		}
		if (equSettingName != null && !"".equals(equSettingName) && !"-1".equals(equSettingName)) {
			c.add(Restrictions.eq("equSettingName", equSettingName, true));
		}

		return c;
	}

	@Override
	public Boolean isBackCheck(String returnBackNo) {
		Boolean result = false;
		try {
			int backNo = Integer.parseInt(returnBackNo);
			List<MatReturnBack> list = this.matReturnBackRepository.findByReturnNo(backNo);
			for(MatReturnBack back : list) {
				if(back != null && back.getIsReturnBack().equals(IsReturnBack.ISBACK)) {
					result = true;
					break;
				}
			}
		} catch (Exception e) {
			logger.error("非法归还码：" + returnBackNo);
		}
		return result;
	}

	@Override
	public List<ReturnBackPrintInfo> getReturnInfo(String returnBackNo) {
		List<ReturnBackPrintInfo> info = new ArrayList<ReturnBackPrintInfo>();
		try {
			int backNo = Integer.parseInt(returnBackNo);
			List<MatReturnBack> matReturnBackList = this.matReturnBackRepository.findByReturnNo(backNo);
			for(MatReturnBack matReturnBack : matReturnBackList) {
				info.add(new ReturnBackPrintInfo(matReturnBack.getMatUseBillId(), matReturnBack.getAccountId(), matReturnBack.getIsGetNewOne(),
						matReturnBack.getMatUseBill().getMatInfoId(), matReturnBack.getReturnBackType()));
			}
		} catch (Exception e) {
			logger.error("非法归还码：" + returnBackNo);
		}
		return info;
	}

	@Override
	public ApiFeignResponse<FeignReturnBack> getSyncReturnBack(String cabinetName, Long endTime) {
		ApiFeignResponse<FeignReturnBack> response = new ApiFeignResponse<>();
		response.setCode(HttpServletResponse.SC_OK);
		response.setMessage("归还清单同步成功");
		DataSyncState syncInfo = this.dataSyncStateRepository.getSyncInfo(cabinetName, DataSyncType.BACK).get(0);
		Date time = new Date();
		if(endTime != null) {
			time = new Date(endTime);
		}
		
		try {
			List<MatReturnBack> list = this.matReturnBackRepository.getSyncBackInfo(cabinetName, syncInfo.getSyncTime(), time);
			List<FeignReturnBack> results = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(list)) {
				for (MatReturnBack returnBack : list) {
					// 柜子信息
					EquSetting setting = returnBack.getMatUseBill().getEquDetailSta().getEquDetail().getEquSetting();
					// 归还物料信息
					MatEquInfo equInfo = returnBack.getMatUseBill().getMatUseRecord().getMatInfo();
					FeignReturnBack back = new FeignReturnBack(returnBack.getUser().getLoginName(), returnBack.getUser().getUserName(),
							equInfo.getId(), returnBack.getMatUseBill().getMatUseRecord().getMatInfoName(),
							returnBack.getMatUseBill().getMatUseRecord().getSpec(),
							returnBack.getMatUseBill().getMatUseRecord().getBarCode(),
							equInfo.getManufacturer().getManufacturerName(), equInfo.getManufacturer().getId(),
							returnBack.getBarCode(), returnBack.getMatUseBill().getMatUseRecord().getPackNum(),
							setting.getId(), setting.getEquSettingName(), returnBack.getOpDate(),
							returnBack.getMatUseBill().getOpDate(),
							returnBack.getIsReturnBack().equals(IsReturnBack.ISBACK));
					results.add(back);
				}
				response.setResultList(results);
			}
		}catch(Exception e) {
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
	public void sendReturnRecord(String beginTime, String endTime, String userName, String status, String backType,
			String equSettingName) {
		Criteria<MatReturnBack> criteria = getInfo(beginTime, endTime, userName, status, backType, equSettingName);
		List<MatReturnBack> matReturnBackList = this.matReturnBackRepository.findAll(criteria);

		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("归还记录");
			HSSFRow row1 = sheet.createRow(0);
			HSSFCell cell = row1.createCell(0);

			cell.setCellValue("归还记录");
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			HSSFFont fontTitle = workbook.createFont();
			fontTitle.setFontName("宋体");
			fontTitle.setFontHeightInPoints((short) 14);
			fontTitle.setBold(true);
			cellStyle.setFont(fontTitle);
			cell.setCellStyle(cellStyle);
			row1.setHeight((short) 500);
			row1.setRowStyle(cellStyle);

			int cellIndex = 0;
			HSSFRow row2 = sheet.createRow(1);
			row2.createCell(cellIndex).setCellValue("刀具柜名称");
			row2.createCell(++cellIndex).setCellValue("产品流水号");
			row2.createCell(++cellIndex).setCellValue("物料名称");
			row2.createCell(++cellIndex).setCellValue("归还人员");
			row2.createCell(++cellIndex).setCellValue("编号");
			row2.createCell(++cellIndex).setCellValue("型号");
			row2.createCell(++cellIndex).setCellValue("归还时间");
			row2.createCell(++cellIndex).setCellValue("审核方式");
			row2.createCell(++cellIndex).setCellValue("审核人员");
			row2.createCell(++cellIndex).setCellValue("审核时间");
			row2.createCell(++cellIndex).setCellValue("应还数量");
			row2.createCell(++cellIndex).setCellValue("实际数量");
			row2.createCell(++cellIndex).setCellValue("归还详情");
			row2.createCell(++cellIndex).setCellValue("审核状态");
			row2.createCell(++cellIndex).setCellValue("备注");

			int rowNo = 2;
			int sumBillBackNum = 0; // 应还数量
			int sumBillNum = 0; // 实际数量

			for (MatReturnBack returnBack : matReturnBackList) {
				HSSFRow row3 = sheet.createRow(rowNo);
				cellIndex = 0;
				int num = 0;
				if (returnBack.getNum() != null) {
					num = returnBack.getNum();
				}
				// 创建单元格并设置单元格内容
				row3.createCell(cellIndex)
						.setCellValue(returnBack.getEquSettingName() != null ? returnBack.getEquSettingName() : "");
				row3.createCell(++cellIndex)
						.setCellValue(returnBack.getSerialNum() != null ? returnBack.getSerialNum() : "");
				row3.createCell(++cellIndex)
						.setCellValue(returnBack.getMatUseBill() != null
								? returnBack.getMatUseBill().getMatUseRecord().getMatInfoName()
								: "");
				row3.createCell(++cellIndex)
						.setCellValue(returnBack.getUser() != null ? returnBack.getUser().getUserName() : "");
				row3.createCell(++cellIndex)
						.setCellValue(returnBack.getMatUseBill() != null
								? returnBack.getMatUseBill().getMatUseRecord().getBarCode()
								: "");
				row3.createCell(++cellIndex)
						.setCellValue(returnBack.getMatUseBill() != null
								? returnBack.getMatUseBill().getMatUseRecord().getSpec()
								: "");
				row3.createCell(++cellIndex).setCellValue(
						returnBack.getOpDate() != null ? returnBack.getOpDate().toString() : new Date().toString());
				row3.createCell(++cellIndex).setCellValue(
						returnBack.getConfirmMode() != null ? returnBack.getConfirmMode().getMessage() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getConfirmUser());
				row3.createCell(++cellIndex).setCellValue(
						returnBack.getConfirmDate() != null ? returnBack.getConfirmDate().toString() : "");
				row3.createCell(++cellIndex)
						.setCellValue(returnBack.getMatUseBill() != null
								? returnBack.getMatUseBill().getMatUseRecord().getPackNum()
								: 0);
				row3.createCell(++cellIndex).setCellValue(num != 0 ? num : 0);
				row3.createCell(++cellIndex)
						.setCellValue(returnBack.getReturnDetailInfo() != null ? returnBack.getReturnDetailInfo() : "");
				row3.createCell(++cellIndex).setCellValue(
						returnBack.getAuditStatus() != null ? returnBack.getAuditStatus().getMessage() : "");
				row3.createCell(++cellIndex).setCellValue(returnBack.getRemark() != null ? returnBack.getRemark() : "");

				sumBillBackNum += returnBack.getMatUseBill().getMatUseRecord().getPackNum();
				sumBillNum += num;

				rowNo++;
			}

			HSSFRow row4 = sheet.createRow(rowNo);
			row4.createCell(0).setCellValue("合计");
			row4.createCell(9).setCellValue(sumBillBackNum);
			row4.createCell(10).setCellValue(sumBillNum);

			String filePath = this.toolProperties.getTmpUploadPath() + "归还记录-" + DateUtil.getAllTime() + ".xls";
			File file = new File(filePath);
			output = new FileOutputStream(file);
			workbook.write(output);
			if (!file.exists()) {
				logger.error("生成归还记录异常,请刷新后再试!");
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

			String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;附件为归还记录,请查收!</p>";
			this.emailUtil.sendEmail(mailContent, "归还记录", filePath, "归还记录", mailList);
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