package com.dosth.tool.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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

import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.dto.Card;
import com.dosth.dto.Lattice;
import com.dosth.dto.MatInfo;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.util.CabinetInfoUtil;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.common.util.MatInfoUtil;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.FeedingDetailRepository;
import com.dosth.tool.repository.FeedingListRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.EquDetailService;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.FeedingDetailService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.google.common.collect.Lists;

/**
 * 
 * @description 补料清单明细Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class FeedingDetailServiceImpl implements FeedingDetailService {

	public static final Logger logger = LoggerFactory.getLogger(FeedingDetailServiceImpl.class);

	@Autowired
	private FeedingListRepository feedingListRepository;
	@Autowired
	private FeedingDetailRepository feedingDetailRepository;
	@Autowired
	private EquDetailService equDetailService;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private TimeTaskDetailRepository timeTaskDetailRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailUtil emailUtil;

	@Override
	public void save(FeedingDetail feedingDetail) throws DoSthException {
		this.feedingDetailRepository.save(feedingDetail);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public FeedingDetail get(Serializable id) throws DoSthException {
		return this.feedingDetailRepository.findOne(id);
	}

	@Override
	public FeedingDetail update(FeedingDetail feedingDetail) throws DoSthException {
		return this.feedingDetailRepository.saveAndFlush(feedingDetail);
	}

	@Override
	public void delete(FeedingDetail feedingDetail) throws DoSthException {
		this.feedingDetailRepository.delete(feedingDetail);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Card> getFeedingDetailListByListId(String feedingListId) {
		FeedingList feedingList = this.feedingListRepository.findOne(feedingListId);
		Map<String, List<FeedingDetail>> map = this.feedingDetailRepository
				.getFeedingDetailListByFeedingListId(feedingListId).stream()
				.collect(Collectors.groupingBy(FeedingDetail::getEquDetailStaId));
		List<FeedingDetail> feedingDetailList;
		List<Card> cardList = new ArrayList<>();
		Card card = null;
		Lattice lattice = null;
		MatInfo info = null;
		List<Lattice> latticeList = null;
		List<EquDetail> detailList = this.equDetailService.getEquDetailListBySettingId(feedingList.getCabinetId());
		List<EquDetailSta> staList;
		for (EquDetail detail : detailList) {
			latticeList = new ArrayList<>();
			card = new Card(detail.getId(), detail.getIp(), detail.getPort(), detail.getRowNo(), detail.getLevelHt());
			card.setDoor(detail.getEquSetting().getDoor().name());
			staList = this.equDetailStaService.getStaListByDetailId(detail.getId());
			for (EquDetailSta sta : staList) {
				feedingDetailList = map.get(sta.getId());
				info = MatInfoUtil.createMatInfo(sta.getMatInfo());
				if (feedingDetailList != null) {
					lattice = new Lattice();
					lattice.setStaId(sta.getId());
					lattice.setColNo(sta.getColNo());
					lattice.setMaxReserve(sta.getMaxReserve());
					lattice.setMatInfo(info);
					lattice.setCurReserve(feedingDetailList.get(0).getFeedingNum());
					if (info.getIcon() != null && !"".equals(info.getIcon())) {
						info.setIcon(FileUtil.convertImageToBase64Data(
								new File(this.toolProperties.getUploadPath() + info.getIcon())));
					}
					lattice.setFeedNum(feedingDetailList.get(0).getFeedingNum());
					latticeList.add(lattice);
				} else {
					if (info != null) {
						lattice = new Lattice();
						lattice.setStaId(sta.getId());
						lattice.setColNo(sta.getColNo());
						lattice.setMaxReserve(sta.getMaxReserve());
						lattice.setMatInfo(info);
						latticeList.add(lattice);
					}
				}
			}
			card.setLatticeList(latticeList);
			cardList.add(card);
		}
		return cardList;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<FeedingDetail> getFeedingDetailListByFeedingListId(String feedListId) {
		return this.feedingDetailRepository.getFeedingDetailListByFeedingListId(feedListId);
	}

	@Override
	public Page<FeedingDetail> getPage(int pageNo, int pageSize, String beginTime, String endTime, String matInfo,
			String feedingName, String cabinetId) {
		Criteria<FeedingDetail> criteria = new Criteria<>();
		if (matInfo != null && !"".equals(matInfo) && !matInfo.equals("-1")) {
			criteria.add(Restrictions.or(new Criterion[] { Restrictions.like("matInfo.barCode", matInfo.trim(), true),
					Restrictions.like("matInfo.matEquName", matInfo.trim(), true),
					Restrictions.like("matInfo.spec", matInfo.trim(), true) }));
		}
		if (feedingName != null && !"".equals(feedingName)) {
			criteria.add(Restrictions.like("feedingList.feedingName", feedingName.trim(), true));
		}
		if (beginTime != null && !"".equals(beginTime) && !"1900-01-01".equals(beginTime)) {
			criteria.add(Restrictions.gte("feedingList.feedingTime",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime) && !"9999-12-31".equals(endTime)) {
			criteria.add(Restrictions.lte("feedingList.feedingTime",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		if (cabinetId != null && !"".equals(cabinetId) && !"-1".equals(cabinetId)) {
			criteria.add(Restrictions.eq("feedingList.cabinetId", cabinetId, true));
		}
		Page<FeedingDetail> page = this.feedingDetailRepository.findAll(criteria,
				new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "feedingList.feedingTime")));
		// 检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if (page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber() + 1)) {
			pageNo = page.getTotalPages() - 1;
			page = this.feedingDetailRepository.findAll(criteria,
					new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "feedingList.feedingTime")));
		}
		for (FeedingDetail detail : page.getContent()) {
			detail.setCabinetName(detail.getEquDetailSta().getEquDetail().getEquSetting().getEquSettingName());
			detail.setFeedingName(detail.getFeedingList().getFeedingName());
			detail.setFeedType(detail.getFeedingList().getFeedType().getMessage());
			detail.setPosition("F" + detail.getEquDetailSta().getEquDetail().getRowNo() + "-"
					+ detail.getEquDetailSta().getColNo());
			detail.setMatName(detail.getMatInfo().getMatEquName());
			detail.setBarCode(detail.getMatInfo().getBarCode());
			detail.setSpec(detail.getMatInfo().getSpec());
			detail.setFeedingAccount(ViewUserUtil.createViewUserName(detail.getFeedingList().getFeedingAccount()));
			if (detail.getFeedingList().getFeedingTime() != null) {
				detail.setFeedingTime(DateUtil.getTime(detail.getFeedingList().getFeedingTime()));
			}
			detail.setIsFinish(detail.getFeedingList().getIsFinish().getMessage());
		}
		return page;
	}

	@Override
	public void exportFeedingDetail(HttpServletRequest request, HttpServletResponse response, String beginTime,
			String endTime, String matInfo, String feedListName) {
		Criteria<FeedingDetail> criteria = new Criteria<>();
		if (matInfo != null && !"".equals(matInfo) && !matInfo.equals("-1")) {
			criteria.add(Restrictions.or(new Criterion[] { Restrictions.like("matInfo.barCode", matInfo.trim(), true),
					Restrictions.like("matInfo.matEquName", matInfo.trim(), true),
					Restrictions.like("matInfo.spec", matInfo.trim(), true) }));
		}
		if (feedListName != null && !"".equals(feedListName) && !feedListName.equals("-1")) {
			criteria.add(Restrictions.like("feedingList.feedingName", feedListName.trim(), true));
		}
		if (beginTime != null && !"".equals(beginTime) && !"1900-01-01".equals(beginTime)) {
			criteria.add(Restrictions.gte("feedingList.feedingTime",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime) && !"9999-12-31".equals(endTime)) {
			criteria.add(Restrictions.lte("feedingList.feedingTime",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}

		List<FeedingDetail> feedingDetailList = this.feedingDetailRepository.findAll(criteria);

		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("补料记录");
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
			row1.createCell(cellIndex).setCellValue("刀具柜名称");
			row1.createCell(++cellIndex).setCellValue("补料清单编号");
			row1.createCell(++cellIndex).setCellValue("补料类型");
			row1.createCell(++cellIndex).setCellValue("补料货位");
			row1.createCell(++cellIndex).setCellValue("物料名称");
			row1.createCell(++cellIndex).setCellValue("物料编号");
			row1.createCell(++cellIndex).setCellValue("物料型号");
			row1.createCell(++cellIndex).setCellValue("补充数量");
			row1.createCell(++cellIndex).setCellValue("补料人员");
			row1.createCell(++cellIndex).setCellValue("补料时间");
			row1.createCell(++cellIndex).setCellValue("是否完成");

			int rowNo = 1;
			int sumFeedingNum = 0;
			for (FeedingDetail detail : feedingDetailList) {
				HSSFRow row2 = sheet.createRow(rowNo);
				cellIndex = 0;
				row2.createCell(cellIndex).setCellValue(
						CabinetInfoUtil.createCabinetInfoByEquSetting(detail.getFeedingList().getEquSetting()));
				row2.createCell(++cellIndex).setCellValue(detail.getFeedingList().getFeedingName());
				row2.createCell(++cellIndex).setCellValue(detail.getFeedingList().getFeedType().getMessage());
				row2.createCell(++cellIndex).setCellValue("F" + detail.getEquDetailSta().getEquDetail().getRowNo() + "-"
						+ detail.getEquDetailSta().getColNo());
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getMatEquName());
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getBarCode());
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getSpec());
				row2.createCell(++cellIndex).setCellValue(detail.getFeedingNum());
				row2.createCell(++cellIndex)
						.setCellValue(ViewUserUtil.createViewUserName(detail.getFeedingList().getFeedingAccount()));
				if (detail.getFeedingList().getFeedingTime() != null) {
					row2.createCell(++cellIndex)
							.setCellValue(DateUtil.getTime(detail.getFeedingList().getFeedingTime()));
				} else {
					row2.createCell(++cellIndex).setCellValue("");
				}
				row2.createCell(++cellIndex).setCellValue(detail.getFeedingList().getIsFinish().getMessage());
				sumFeedingNum += detail.getFeedingNum();
				rowNo++;
			}

			HSSFRow row4 = sheet.createRow(rowNo);
			row4.createCell(0).setCellValue("合计");
			row4.createCell(7).setCellValue(sumFeedingNum);

			String fileName = FileUtil.processFileName(request, "补料记录" + DateUtil.getDays());
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

	@Override
	public void sendFeedRecord(String beginTime, String endTime, String matInfo, String feedingName) {
		Criteria<FeedingDetail> criteria = new Criteria<>();
		if (matInfo != null && !"".equals(matInfo) && !matInfo.equals("-1")) {
			criteria.add(Restrictions.or(new Criterion[] { Restrictions.like("matInfo.barCode", matInfo, true),
					Restrictions.like("matInfo.matEquName", matInfo, true),
					Restrictions.like("matInfo.spec", matInfo, true) }));
		}
		if (feedingName != null && !"".equals(feedingName) && !feedingName.equals("-1")) {
			criteria.add(Restrictions.like("feedingList.feedingName", feedingName, true));
		}
		if (beginTime != null && !"".equals(beginTime) && !"1900-01-01".equals(beginTime)) {
			criteria.add(Restrictions.gte("feedingList.feedingTime",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime) && !"9999-12-31".equals(endTime)) {
			criteria.add(Restrictions.lte("feedingList.feedingTime",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}

		List<FeedingDetail> feedingDetailList = this.feedingDetailRepository.findAll(criteria);

		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("补料记录");
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
			row1.createCell(cellIndex).setCellValue("刀具柜名称");
			row1.createCell(++cellIndex).setCellValue("补料清单名称");
			row1.createCell(++cellIndex).setCellValue("补料类型");
			row1.createCell(++cellIndex).setCellValue("补料货位");
			row1.createCell(++cellIndex).setCellValue("物料名称");
			row1.createCell(++cellIndex).setCellValue("物料编号");
			row1.createCell(++cellIndex).setCellValue("物料型号");
			row1.createCell(++cellIndex).setCellValue("补充数量");
			row1.createCell(++cellIndex).setCellValue("补料人员");
			row1.createCell(++cellIndex).setCellValue("补料时间");
			row1.createCell(++cellIndex).setCellValue("是否完成");

			int rowNo = 1;
			int sumFeedingNum = 0;
			for (FeedingDetail detail : feedingDetailList) {
				HSSFRow row2 = sheet.createRow(rowNo);
				cellIndex = 0;
				row2.createCell(cellIndex).setCellValue(
						CabinetInfoUtil.createCabinetInfoByEquSetting(detail.getFeedingList().getEquSetting()));
				row2.createCell(++cellIndex).setCellValue(detail.getFeedingList().getFeedingName());
				row2.createCell(++cellIndex).setCellValue(detail.getFeedingList().getFeedType().getMessage());
				row2.createCell(++cellIndex).setCellValue("F" + detail.getEquDetailSta().getEquDetail().getRowNo() + "-"
						+ detail.getEquDetailSta().getColNo());
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getMatEquName());
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getBarCode());
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getSpec());
				row2.createCell(++cellIndex).setCellValue(detail.getFeedingNum());
				row2.createCell(++cellIndex)
						.setCellValue(ViewUserUtil.createViewUserName(detail.getFeedingList().getFeedingAccount()));
				if (detail.getFeedingList().getFeedingTime() != null) {
					row2.createCell(++cellIndex)
							.setCellValue(DateUtil.getTime(detail.getFeedingList().getFeedingTime()));
				} else {
					row2.createCell(++cellIndex).setCellValue("");
				}
				row2.createCell(++cellIndex).setCellValue(detail.getFeedingList().getIsFinish().getMessage());
				sumFeedingNum += detail.getFeedingNum();
				rowNo++;
			}

			HSSFRow row4 = sheet.createRow(rowNo);
			row4.createCell(0).setCellValue("合计");
			row4.createCell(7).setCellValue(sumFeedingNum);

			String filePath = this.toolProperties.getTmpUploadPath() + "补料记录-" + DateUtil.getAllTime() + ".xls";
			File file = new File(filePath);
			output = new FileOutputStream(file);
			workbook.write(output);
			if (!file.exists()) {
				logger.error("生成补料记录异常,请刷新后再试!");
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

			String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;附件为补料记录,请查收!</p>";
			this.emailUtil.sendEmail(mailContent, "补料记录", filePath, "补料记录", mailList);
		} catch (Exception e) {
			throw new DoSthException(DoSthExceptionEnum.SERVER_ERROR);
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