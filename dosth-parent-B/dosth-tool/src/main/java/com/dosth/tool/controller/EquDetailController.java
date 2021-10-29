package com.dosth.tool.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.controller.ShiroController;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.util.OpTip;

/**
 * 设备详情Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/equdetail")
public class EquDetailController extends ShiroController {

	private final static String PREFIX = "/tool/equdetail/";

	@Autowired
	private EquDetailStaService equDetailStaService;

	@RequestMapping("")
	public String index() {
		return PREFIX + "index.html";
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("/editView/{equDetailStaId}")
	public String editView(@PathVariable String equDetailStaId, Model model) {
		EquDetailSta equDetailSta = this.equDetailStaService.get(equDetailStaId);
		model.addAttribute(equDetailSta);
		LogObjectHolder.me().set(equDetailSta);
		return PREFIX + "edit.html";
	}

	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改设备详情")
	@ResponseBody
	public OpTip edit(@Valid EquDetailSta equDetailSta) {
		OpTip tip = this.equDetailStaService.storage(equDetailSta, super.getShiroAccount().getId());
		return tip;
	}

	/**
	 * 生成补料清单
	 */
	@RequestMapping("/feedList")
	@ResponseBody
	public OpTip feedList(HttpServletRequest request) throws IOException {
		String accountId = super.getShiroAccount().getId();
		String arrs = request.getParameter("arrs");
		String equSettingId = request.getParameter("equSettingId");
		OpTip tip = this.equDetailStaService.feedList(arrs, equSettingId, accountId, FeedType.LIST);
		return tip;
	}
	
	/**
	 * @description 导出最新的补料单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/export")
	@ResponseBody
	public void exportLastFeedingList(HttpServletRequest request, HttpServletResponse response) {
		this.equDetailStaService.exportLastFeedingList(request, response);
	}
	
	/**
	 * @description 获取货道信息
	 * @param equSettingId 设备或托盘Id
	 * @param matNameBarCodeSpec 物料名称编号型号
	 * @return
	 */
	@RequestMapping("/getEquInfos")
	public String getEquInfos(@RequestParam("equSettingId") String equSettingId, 
			@RequestParam("matNameBarCodeSpec") String matNameBarCodeSpec, Model model) {
		model.addAttribute("staList", this.equDetailStaService.getEquInfos(equSettingId, matNameBarCodeSpec));
		model.addAttribute("netPath", ToolProperties.PREFIX);
		return PREFIX + "stalist.html";
	}
}