package com.dosth.tool.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.controller.ShiroController;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.MatCategoryService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.util.OpTip;

/**
 * @description 物料上下架
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/matInOrOut")
public class MatInOrOutCabController extends ShiroController {

	private final static String PREFIX = "/tool/matinorout/";

	@Autowired
	private EquSettingService equSettingService;
	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private MatCategoryService matCategoryService;

	/**
	 * @description 初始化
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("cabList", this.equSettingService.getCabList());
		return PREFIX + "index.html";
	}

	/**
	 * @description 初始化物料列表
	 * @param matNameBarCodeSpec 物料名称编号型号
	 * @param request
	 * @return
	 */
	@RequestMapping("/initMatList")
	public String init(@RequestParam("matNameBarCodeSpec") String matNameBarCodeSpec, HttpServletRequest request) {
		List<MatEquInfo> matInfoList = this.matEquInfoService.getMatInfoList(matNameBarCodeSpec);
		request.setAttribute("matList", matInfoList);
		request.setAttribute("netPath", ToolProperties.PREFIX);
		return PREFIX + "matlist.html";
	}

	/**
	 * @description 获取柜子详情列表
	 * @param cabinetId 柜子Id
	 * @param model
	 * @return
	 */
	@RequestMapping("/initStaList")
	public String initStaList(@RequestParam String cabinetId,
			@RequestParam("staMatNameBarCodeSpec") String staMatNameBarCodeSpec, Model model) {
		model.addAttribute("staList",
				this.equDetailStaService.getStaListByCabinetIdOrDetailId(cabinetId, staMatNameBarCodeSpec));
		model.addAttribute("netPath", ToolProperties.PREFIX);
		return PREFIX + "stalist.html";
	}

	/**
	 * @description 物料上架
	 * @param matIds 物料Id
	 * @param staIds 货位Id
	 * @return
	 */
	@RequestMapping("/matIn")
	@ResponseBody
	public OpTip matIn(@RequestParam("matIds") String matIds, @RequestParam("staIds") String staIds) {
		return this.equDetailStaService.matIn(matIds, staIds);
	}

	/**
	 * @description 物料下架确认
	 * @param matIds 物料Id
	 * @return
	 */
	@RequestMapping("/matCheck")
	@ResponseBody
	public OpTip matCheck(@RequestParam("matIds") String matIds) {
		OpTip tip = new OpTip();
		try {
			tip = this.matCategoryService.matCheck(matIds);
		} catch (Exception e) {
			tip.setCode(204);
			tip.setMessage("下架失败");
		}
		return tip;
	}

	/**
	 * @description 物料下架
	 * @param staIds 货位Id
	 * @return
	 */
	@RequestMapping("/matOut")
	@ResponseBody
	public OpTip matOut(@RequestParam("staIds") String staIds, @RequestParam("cabinetId") String cabinetId) {
		String accountId = super.getShiroAccount().getId();
		OpTip tip = new OpTip("下架成功");
		try {
			tip = this.equDetailStaService.matOut(staIds, accountId, cabinetId);
		} catch (Exception e) {
			tip.setCode(204);
			tip.setMessage("下架失败");
		}
		return tip;
	}
}