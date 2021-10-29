package com.dosth.tool.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.base.tips.Tip;
import com.dosth.common.controller.ShiroController;
import com.dosth.common.util.DateUtil;
import com.dosth.tool.common.dto.FeedRecordSummary;
import com.dosth.tool.service.FeedRecordSummaryService;


@Controller
@RequestMapping("/feedRecordSummary")
public class FeedRecordSummaryController extends ShiroController {
	
	private static String PREFIX = "/tool/feedRecordSummary/";
	
	@Autowired
	private FeedRecordSummaryService feedRecordSummaryService;

	/**
	 * 跳转到班次设定列表的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("startDate", DateUtil.getPreMonthDay());
		model.addAttribute("endDate", DateUtil.getDay(new Date()));
		return PREFIX + "index.html";
	}
	
	/**
	 * 查询列表
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
		Page<FeedRecordSummary> page = this.feedRecordSummaryService.getPage(pageNo, pageSize, beginTime, endTime, matInfo);
		return page;
	}

	/**
	 * @description 导出
	 * @param response    servletResponse
	 * @param beginTime   起始时间
	 * @param endTime     截止时间
	 */
	@RequestMapping("/export/{params}")
	@ResponseBody
	public Tip export(HttpServletRequest request, HttpServletResponse response, @PathVariable String[] params) {
		this.feedRecordSummaryService.export(request, response, params[0], params[1], params[2]);
		return SUCCESS_TIP;
	}
}
