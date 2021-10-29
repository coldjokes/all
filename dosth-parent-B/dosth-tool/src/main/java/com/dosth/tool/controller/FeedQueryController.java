package com.dosth.tool.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.controller.BaseController;
import com.dosth.tool.common.dto.CabinetName;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.service.FeedQueryService;
import com.dosth.tool.service.FeedingDetailService;
import com.dosth.tool.service.LowerFrameQueryService;

/**
 * @description 补料查询Controller
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/feedQuery")
public class FeedQueryController extends BaseController {

	private static String PREFIX = "/tool/feedQuery/";

	@Autowired
	private FeedQueryService feedQueryService;
	@Autowired
	private FeedingDetailService feedingDetailService;
	@Autowired
	private LowerFrameQueryService lowerFrameQueryService;

	/**
	 * 跳转到补料查询的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		List<CabinetName> cabinetList = this.lowerFrameQueryService.getCabinetNameList();
		model.addAttribute("cabinetList", cabinetList);
		return PREFIX + "index.html";
	}

	/**
	 * 补料查询列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list() {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		try {
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		String beginTime = super.getPara("beginTime");
		String endTime = super.getPara("endTime");
		String matInfo = super.getPara("matInfo");
		String feedingName = super.getPara("feedingName");
		String cabinetId = super.getPara("cabinetId");
		Page<FeedingDetail> page = this.feedingDetailService.getPage(pageNo, pageSize, beginTime, endTime, matInfo,
				feedingName, cabinetId);
		return page;
	}

	/**
	 * 跳转到补料明细查询的页面
	 */
	@RequestMapping("/feedDetailView/{feedingListId}")
	public String feedDetail(Model model, @PathVariable String feedingListId) {
		return PREFIX + "feedDetail.html";
	}

	/**
	 * 导出
	 */
	@RequestMapping("/exportExcel/{feedingListId}")
	@ResponseBody
	public String exportExcel(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String feedingListId) throws IOException {
		String feedingDetail = this.feedQueryService.exportExcel(request, response, feedingListId);
		return feedingDetail;
	}

	/**
	 * @description 导出补料详情
	 * @param request
	 * @param response
	 * @param beginTime 起始时间
	 * @param endTime   截止时间
	 * @param matInfo   物料信息
	 */
	@RequestMapping("/exportFeedingDetail/{beginTime}/{endTime}/{matInfo}/{feedingName}")
	@ResponseBody
	public void exportFeedingDetail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String beginTime, @PathVariable String endTime, @PathVariable String matInfo,
			@PathVariable String feedingName) throws IOException {
		this.feedingDetailService.exportFeedingDetail(request, response, beginTime, endTime, matInfo, feedingName);
	}
}