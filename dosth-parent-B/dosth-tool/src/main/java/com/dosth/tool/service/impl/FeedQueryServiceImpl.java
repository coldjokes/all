package com.dosth.tool.service.impl;

import java.io.IOException;
import java.io.OutputStream;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.repository.FeedingListRepository;
import com.dosth.tool.service.FeedQueryService;
import com.dosth.tool.service.FeedingDetailService;

/**
 * 补料查询Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class FeedQueryServiceImpl implements FeedQueryService {

	@Autowired
	private FeedingDetailService feedingDetailService;
	@Autowired
	private FeedingListRepository feedingListRepository;

	@Override
	public Page<FeedingList> getPage(int pageNo, int pageSize, String beginTime, String endTime) throws DoSthException {
		Criteria<FeedingList> c = new Criteria<>();
		if (beginTime != null && !"".equals(beginTime)) {
			c.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime)) {
			c.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}

		return this.feedingListRepository.findAll(c, new PageRequest(pageNo, pageSize));
	}

	@Override
	public String exportExcel(HttpServletRequest request, HttpServletResponse response, String feedingListId)
			throws IOException {
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
			row1.createCell(++cellIndex).setCellValue("物料名称");
			row1.createCell(++cellIndex).setCellValue("物料编号");
			row1.createCell(++cellIndex).setCellValue("物料型号");
			row1.createCell(++cellIndex).setCellValue("补料数量");

			List<FeedingDetail> feedingDetailList = this.feedingDetailService
					.getFeedingDetailListByFeedingListId(feedingListId);

			int rowNo = 1;
			int sumFeedingNum = 0;
			for (FeedingDetail detail : feedingDetailList) {
				HSSFRow row2 = sheet.createRow(rowNo);
				cellIndex = 0;
				row2.createCell(cellIndex)
						.setCellValue(detail.getEquDetailSta().getEquDetail().getEquSetting().getEquSettingName() + "("
								+ detail.getEquDetailSta().getEquDetail().getRowNo() + "--"
								+ detail.getEquDetailSta().getColNo() + ")");
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getMatEquName());
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getBarCode());
				row2.createCell(++cellIndex).setCellValue(detail.getMatInfo().getSpec());
				row2.createCell(++cellIndex).setCellValue(String.valueOf(detail.getFeedingNum()));
				sumFeedingNum += detail.getFeedingNum();
				rowNo++;
			}

			HSSFRow row4 = sheet.createRow(rowNo);
			row4.createCell(0).setCellValue("合计");
			row4.createCell(4).setCellValue(sumFeedingNum);

			String fileName = FileUtil.processFileName(request, "补料记录" + DateUtil.getDays());
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