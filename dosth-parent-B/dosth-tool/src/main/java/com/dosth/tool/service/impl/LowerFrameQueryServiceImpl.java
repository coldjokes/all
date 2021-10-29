package com.dosth.tool.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.IsReturnBack;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.enums.CabinetType;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.dto.CabinetName;
import com.dosth.tool.common.state.AuditStatus;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.entity.LowerFrameQuery;
import com.dosth.tool.entity.MatReturnBack;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.entity.SubBoxAccountRef;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.LowerFrameQueryRepository;
import com.dosth.tool.repository.MatReturnBackRepository;
import com.dosth.tool.repository.MatUseBillRepository;
import com.dosth.tool.repository.MatUseRecordRepository;
import com.dosth.tool.repository.SubBoxAccountRefRepository;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.FeedingDetailService;
import com.dosth.tool.service.FeedingListService;
import com.dosth.tool.service.LowerFrameQueryService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.util.OpTip;
import com.google.common.collect.Lists;

/**
 * @description 下架查询Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class LowerFrameQueryServiceImpl implements LowerFrameQueryService {
	
	private static final Logger logger = LoggerFactory.getLogger(LowerFrameQueryServiceImpl.class);

	@Autowired
	private AdminService adminService;
	@Autowired
	private FeedingListService feedingListService;
	@Autowired
	private FeedingDetailService feedingDetailService;
	@Autowired
	private LowerFrameQueryRepository lowerFrameQueryRepository;
	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private SubBoxAccountRefRepository subBoxAccountRefRepository;
	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;
	@Autowired
	private MatUseBillRepository matUseBillRepository;
	@Autowired
	private MatReturnBackRepository matReturnBackRepository;
	@Autowired
	private MatUseRecordRepository matUseRecordRepository;
	private ToolProperties toolProperties;
	@Autowired
	private TimeTaskDetailRepository timeTaskDetailRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailUtil emailUtil;

	@Override
	public OpTip lowMainFrame(List<String> frameIds, String accountId, String cabinetId) {
		OpTip tip = new OpTip(200, "下架成功！");
		Integer waitFeedingNum = 0; // 待确认数量
		UserInfo user = this.adminService.getUserInfo(accountId);
		List<FeedingDetail> detailList = new ArrayList<>();
		// 获取补料清单
		List<FeedingList> feedingList = this.feedingListService.getWaitFeedingNum(cabinetId);
		for (String frameId : frameIds) {
			// 获取刀具柜货位详情
			EquDetailSta equDetailSta = this.equDetailStaRepository.findOne(frameId);
			// 判断当前货位是否设置物料
			if (equDetailSta.getMatInfo() == null) {
				continue;
			}
			// 判断是否有未完成的待确认数量
			for (FeedingList feeding : feedingList) {
				// 获取补料清单详情
				detailList = this.feedingDetailService.getFeedingDetailListByFeedingListId(feeding.getId());
				for (FeedingDetail detail : detailList) {
					if (detail.getMatInfoId().equals(equDetailSta.getMatInfoId())) {
						waitFeedingNum += detail.getFeedingNum();
					}
				}
			}
			if (waitFeedingNum > 0) {
				tip = new OpTip(201, "补料未完成，请先操作补料确认！");
				return tip;
			}
			// 添加下架信息
			this.lowerFrameQueryRepository
					.save(new LowerFrameQuery(equDetailSta.getEquDetail().getEquSetting().getEquSettingName(),
							equDetailSta.getId(), null, equDetailSta.getMatInfo().getMatEquName(),
							equDetailSta.getMatInfo().getBarCode(), equDetailSta.getMatInfo().getSpec(),
							equDetailSta.getCurNum(), equDetailSta.getMatInfo().getBorrowType().getMessage(), null,
							null, user.getAccountId(), user.getUserName()));
			// 更新柜体信息
			equDetailSta.setCurNum(0);
			equDetailSta.setMatInfoId(null);
			this.equDetailStaRepository.saveAndFlush(equDetailSta);
		}
		return tip;
	}

	@Override
	public Page<LowerFrameQuery> getPage(int pageNo, int pageSize, String beginTime, String endTime, String name)
			throws DoSthException {
		Criteria<LowerFrameQuery> c = new Criteria<>();
		if (beginTime != null && !"".equals(beginTime)) {
			c.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime)) {
			c.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}

		if (name != null && !"".equals(name) && !"-1".equals(name)) {
			c.add(Restrictions.like("equName", name.trim(), true));
		}

		Page<LowerFrameQuery> page = this.lowerFrameQueryRepository.findAll(c,
				new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "opDate")));
		for (LowerFrameQuery lowerFrame : page.getContent()) {
			if (lowerFrame.getEquDetailSta() != null) {
				lowerFrame.setEquName(lowerFrame.getEquDetailSta().getEquDetail().getEquSetting().getEquSettingName());
				lowerFrame.setPosition("F-" + lowerFrame.getEquDetailSta().getEquDetail().getRowNo() + "-"
						+ lowerFrame.getEquDetailSta().getColNo());
			} else if (lowerFrame.getSubBox() != null) {
				int subNum = lowerFrame.getSubBox().getBoxIndex();
				String subIndex = null;
				if (subNum < 10) {
					subIndex = String.format("%02d", subNum);
				} else if (subNum < 100 && subNum > 9) {
					subIndex = String.format("%01d", subNum);
				}
				lowerFrame.setPosition("ZC-" + subIndex);
				lowerFrame.setEquName(lowerFrame.getSubBox().getEquSetting().getEquSettingName());
			}
		}
		return page;
	}

	@Override
	public OpTip lowTempFrame(List<String> frameIds, String accountId, String cabinetId) {
		OpTip tip = new OpTip(202, "下架成功！");
		EquSetting equSetting = this.equSettingRepository.findOne(cabinetId);
		UserInfo user = this.adminService.getUserInfo(accountId);
		for (String frameId : frameIds) {
			List<SubCabinetDetail> detailList = this.subCabinetDetailRepository.getSubDetailListBySubBoxId(frameId);
			if (detailList.size() == 0) {
				continue;
			}
			SubBoxAccountRef ref = this.subBoxAccountRefRepository.getAccountBySubBoxId(frameId);
			UserInfo owner = this.adminService.getUserInfo(ref.getAccountId());
			List<MatUseBill> useBillList = null;
			for (SubCabinetDetail detail : detailList) {
				// 添加下架信息
				this.lowerFrameQueryRepository
						.save(new LowerFrameQuery(detail.getSubBox().getEquSetting().getEquSettingName(), null, frameId,
								detail.getMatInfo().getMatEquName(), detail.getMatInfo().getBarCode(),
								detail.getMatInfo().getSpec(), detail.getNum(),
								detail.getMatInfo().getBorrowType().getMessage(), owner.getAccountId(),
								owner.getUserName(), user.getAccountId(), user.getUserName()));
				// 释放暂存柜物料详情
				this.subCabinetDetailRepository.delete(detail);
				// 处理历史领用记录作归还处理
				useBillList = this.matUseBillRepository.getUnReturnback(ref.getAccountId());
				useBillList = useBillList.stream()
						.filter(useBill -> useBill.getMatInfoId().equals(detail.getMatInfoId()))
						.collect(Collectors.toList());
				for (MatUseBill bill : useBillList) {
					int maxReturnNo = 1000000;
					List<Integer> backNoList = matReturnBackRepository.getMaxReturnNo();
					if(backNoList != null && backNoList.size() > 0) {
						maxReturnNo = backNoList.get(0);
					}
					MatReturnBack returnBack = new MatReturnBack(maxReturnNo + 1, equSetting.getEquSettingName(), null, bill.getId(),
							ref.getAccountId(), AuditStatus.NO_CHECK, IsReturnBack.ISBACK, null, bill.getMatUseRecord().getMatInfo().getNum(), "0");
					returnBack.setNum(detail.getNum());
					this.matReturnBackRepository.save(returnBack);

					MatUseBill matUseBill = this.matUseBillRepository.findOne(bill.getId());
					if (matUseBill != null) {
						MatUseRecord matUseRecord = this.matUseRecordRepository
								.findOne(matUseBill.getMatUseRecord().getId());
						if (matUseRecord != null) {
							matUseRecord.setReturnBackNum(matUseRecord.getReturnBackNum() + matUseBill.getBorrowNum());
							if (matUseRecord.getBorrowNum() == matUseRecord.getReturnBackNum()) {
								matUseRecord.setIsReturnBack(IsReturnBack.ISBACK.getMessage());
							} else {
								matUseRecord.setIsReturnBack(IsReturnBack.NOTBACK.getMessage());
							}
							this.matUseRecordRepository.saveAndFlush(matUseRecord);
						}
					}
				}
			}
			// 释放暂存柜所属关系
			this.subBoxAccountRefRepository.delete(ref);
		}
		return tip;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public void export(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String name) {
		List<LowerFrameQuery> lowerFrameList = this.lowerFrameQueryRepository.findAll();
		if (beginTime != null && !"".equals(beginTime) && !"1900-01-01".equals(beginTime)) {
			lowerFrameList = lowerFrameList.stream()
					.filter((LowerFrameQuery lowerFrame) -> lowerFrame.getOpDate()
							.after(DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString())))
					.collect(Collectors.toList());
		}
		if (endTime != null && !"".equals(endTime) && !"9999-12-31".equals(endTime)) {
			lowerFrameList = lowerFrameList.stream()
					.filter((LowerFrameQuery lowerFrame) -> lowerFrame.getOpDate()
							.before(DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString())))
					.collect(Collectors.toList());
		}
		if (name != null && !"".equals(name) && !"-1".equals(name)) {
			lowerFrameList = lowerFrameList.stream()
					.filter((LowerFrameQuery lowerFrame) -> lowerFrame.getEquName().contains(name.trim()))
					.collect(Collectors.toList());
		}

		OutputStream output = null;
		HSSFRow row2 = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("下架记录");
			HSSFRow row1 = sheet.createRow(0);
			HSSFCell cell = row1.createCell(0);

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
			row1.createCell(cellIndex).setCellValue("刀具柜名称");
			row1.createCell(++cellIndex).setCellValue("下架货位");
			row1.createCell(++cellIndex).setCellValue("物料名称");
			row1.createCell(++cellIndex).setCellValue("物料编号");
			row1.createCell(++cellIndex).setCellValue("物料型号");
			row1.createCell(++cellIndex).setCellValue("下架数量");
			row1.createCell(++cellIndex).setCellValue("单位");
			row1.createCell(++cellIndex).setCellValue("使用人员");
			row1.createCell(++cellIndex).setCellValue("操作人员");
			row1.createCell(++cellIndex).setCellValue("操作时间");

			int rowNo = 1;
			for (LowerFrameQuery lowerFrame : lowerFrameList) {
				row2 = sheet.createRow(rowNo);
				cellIndex = 0;

				String equName = null;
				String position = null;
				if (lowerFrame.getEquDetailSta() != null) {
					equName = lowerFrame.getEquDetailSta().getEquDetail().getEquSetting().getEquSettingName();
					position = "F-" + lowerFrame.getEquDetailSta().getEquDetail().getRowNo() + "-"
							+ lowerFrame.getEquDetailSta().getColNo();
				} else if (lowerFrame.getSubBox() != null) {
					equName = lowerFrame.getSubBox().getEquSetting().getEquSettingName();

					int subNum = lowerFrame.getSubBox().getBoxIndex();
					String subIndex = null;
					if (subNum < 10) {
						subIndex = String.format("%02d", subNum);
					} else if (subNum < 100 && subNum > 9) {
						subIndex = String.format("%01d", subNum);
					}

					position = "ZC-" + subIndex;
				}

				// 创建单元格并设置单元格内容
				row2.createCell(cellIndex).setCellValue(equName);
				row2.createCell(++cellIndex).setCellValue(position);
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getMatInfoName() != null ? lowerFrame.getMatInfoName() : "");
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getBarCode() != null ? lowerFrame.getBarCode() : "");
				row2.createCell(++cellIndex).setCellValue(lowerFrame.getSpec() != null ? lowerFrame.getSpec() : "");
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getLowFrameNum() != null ? lowerFrame.getLowFrameNum() : 0);
				row2.createCell(++cellIndex).setCellValue(lowerFrame.getUnit() != null ? lowerFrame.getUnit() : "");
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getOwnerName() != null ? lowerFrame.getOwnerName() : "");
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getUserName() != null ? lowerFrame.getUserName() : "");
				row2.createCell(++cellIndex)
						.setCellValue(DateUtil.getTime(lowerFrame.getOpDate() != null ? lowerFrame.getOpDate() : null));

				rowNo++;
			}

			String fileName = FileUtil.processFileName(request, "下架记录" + DateUtil.getDays());
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
	public List<CabinetName> getCabinetNameList() {
		CabinetName cabinetName = null;
		List<CabinetName> cabinetNameList = new ArrayList<>();

		// 查询所有机柜信息
		Criteria<EquSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		criteria.add(Restrictions.ne("cabinetType", CabinetType.KNIFE_CABINET_C, true));
		List<EquSetting> cabinetList = this.equSettingRepository.findAll(criteria);
		if (cabinetList != null && cabinetList.size() > 0) {
			for (EquSetting list : cabinetList) {
				cabinetName = new CabinetName();
				cabinetName.setCabinetId(list.getId());
				cabinetName.setCabinetName(list.getEquSettingName());
				cabinetNameList.add(cabinetName);
			}
		}

		return cabinetNameList;
	}

	@Override
	public void sendLowerRecord(String beginTime, String endTime, String name) {
		List<LowerFrameQuery> lowerFrameList = this.lowerFrameQueryRepository.findAll();
		if (beginTime != null && !"".equals(beginTime) && !"1900-01-01".equals(beginTime)) {
			lowerFrameList = lowerFrameList.stream()
					.filter((LowerFrameQuery lowerFrame) -> lowerFrame.getOpDate()
							.after(DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString())))
					.collect(Collectors.toList());
		}
		if (endTime != null && !"".equals(endTime) && !"9999-12-31".equals(endTime)) {
			lowerFrameList = lowerFrameList.stream()
					.filter((LowerFrameQuery lowerFrame) -> lowerFrame.getOpDate()
							.before(DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString())))
					.collect(Collectors.toList());
		}
		if (name != null && !"".equals(name) && !"-1".equals(name)) {
			lowerFrameList = lowerFrameList.stream()
					.filter((LowerFrameQuery lowerFrame) -> lowerFrame.getEquName().contains(name))
					.collect(Collectors.toList());
		}

		OutputStream output = null;
		HSSFRow row2 = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("下架记录");
			HSSFRow row1 = sheet.createRow(0);
			HSSFCell cell = row1.createCell(0);

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
			row1.createCell(cellIndex).setCellValue("刀具柜名称");
			row1.createCell(++cellIndex).setCellValue("下架货位");
			row1.createCell(++cellIndex).setCellValue("物料名称");
			row1.createCell(++cellIndex).setCellValue("物料编号");
			row1.createCell(++cellIndex).setCellValue("物料型号");
			row1.createCell(++cellIndex).setCellValue("下架数量");
			row1.createCell(++cellIndex).setCellValue("单位");
			row1.createCell(++cellIndex).setCellValue("使用人员");
			row1.createCell(++cellIndex).setCellValue("操作人员");
			row1.createCell(++cellIndex).setCellValue("操作时间");

			int rowNo = 1;
			for (LowerFrameQuery lowerFrame : lowerFrameList) {
				row2 = sheet.createRow(rowNo);
				cellIndex = 0;

				String equName = null;
				String position = null;
				if (lowerFrame.getEquDetailSta() != null) {
					equName = lowerFrame.getEquDetailSta().getEquDetail().getEquSetting().getEquSettingName();
					position = "F-" + lowerFrame.getEquDetailSta().getEquDetail().getRowNo() + "-"
							+ lowerFrame.getEquDetailSta().getColNo();
				} else if (lowerFrame.getSubBox() != null) {
					equName = lowerFrame.getSubBox().getEquSetting().getEquSettingName();

					int subNum = lowerFrame.getSubBox().getBoxIndex();
					String subIndex = null;
					if (subNum < 10) {
						subIndex = String.format("%02d", subNum);
					} else if (subNum < 100 && subNum > 9) {
						subIndex = String.format("%01d", subNum);
					}

					position = "ZC-" + subIndex;
				}

				// 创建单元格并设置单元格内容
				row2.createCell(cellIndex).setCellValue(equName);
				row2.createCell(++cellIndex).setCellValue(position);
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getMatInfoName() != null ? lowerFrame.getMatInfoName() : "");
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getBarCode() != null ? lowerFrame.getBarCode() : "");
				row2.createCell(++cellIndex).setCellValue(lowerFrame.getSpec() != null ? lowerFrame.getSpec() : "");
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getLowFrameNum() != null ? lowerFrame.getLowFrameNum() : 0);
				row2.createCell(++cellIndex).setCellValue(lowerFrame.getUnit() != null ? lowerFrame.getUnit() : "");
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getOwnerName() != null ? lowerFrame.getOwnerName() : "");
				row2.createCell(++cellIndex)
						.setCellValue(lowerFrame.getUserName() != null ? lowerFrame.getUserName() : "");
				row2.createCell(++cellIndex)
						.setCellValue(DateUtil.getTime(lowerFrame.getOpDate() != null ? lowerFrame.getOpDate() : null));

				rowNo++;
			}

			String filePath = this.toolProperties.getTmpUploadPath() + "下架记录-" + DateUtil.getAllTime() + ".xls";
			File file = new File(filePath);
			output = new FileOutputStream(file);
			workbook.write(output);
			if (!file.exists()) {
				logger.error("生成下架记录异常,请刷新后再试!");
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

			String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;附件为下架记录,请查收!</p>";
			this.emailUtil.sendEmail(mailContent, "下架记录", filePath, "下架记录", mailList);
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