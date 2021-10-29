package com.dosth.tool.controller;

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

import com.dosth.common.base.tips.Tip;
import com.dosth.common.controller.BaseController;
import com.dosth.tool.common.dto.CabinetName;
import com.dosth.tool.entity.LowerFrameQuery;
import com.dosth.tool.service.LowerFrameQueryService;

/**
 * 下架查询Controller
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/lowerFrameQuery")
public class LowerFrameQueryController extends BaseController {
	
	private static String PREFIX = "/tool/lowerFrameQuery/";
	
	@Autowired
	private LowerFrameQueryService lowerFrameQueryService;
	
	/**
	 * 跳转到下架查询的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		List<CabinetName> cabinetNameList = this.lowerFrameQueryService.getCabinetNameList();
		model.addAttribute("cabinetNameList", cabinetNameList);
		return PREFIX + "index.html";
	}
	
	/**
	 * @description 导出
	 * @param response servletResponse
	 * @param receiveType 领取类型
	 * @param beginTime 起始时间
	 * @param endTime 截止时间
	 */
	@RequestMapping("/export/{beginTime}/{endTime}/{name}")
	@ResponseBody
	public Tip export(HttpServletRequest request, HttpServletResponse response, 
			@PathVariable String beginTime, @PathVariable String endTime, @PathVariable String name) {
		this.lowerFrameQueryService.export(request, response, beginTime, endTime, name);
		return SUCCESS_TIP;
	}
	
	/**
	 * 下架查询列表
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
		String name = super.getPara("name");
		Page<LowerFrameQuery> page = this.lowerFrameQueryService.getPage(pageNo, pageSize, beginTime, endTime, name);
		return page;
	}
}
