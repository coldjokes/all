package com.dosth.tool.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.SuccessTip;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.BaseController;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.criteria.EquSettingCriteria;
import com.dosth.enums.CabinetType;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.MatEquInfoService;

/**
 * 物料/设备定义Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/matequinfo")
public class MatEquInfoController extends BaseController {

	private static String PREFIX = "/tool/matequinfo/";

	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private EquSettingService equSettingService;

	/**
	 * 跳转到业务主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
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
		String name = super.getPara("name");
		String status = super.getPara("status");
		Page<MatEquInfo> page = this.matEquInfoService.getPage(pageNo, pageSize, name, status);
		return page;
	}

	/**
	 * 加载添加页面
	 */
	@RequestMapping("/addView")
	public String addView(Model model) {
		EquSettingCriteria equSettingCriteria = new EquSettingCriteria();
		equSettingCriteria.setCabinetType(CabinetType.TEM_CABINET);
		List<EquSetting> list = this.equSettingService.findAllCabinet(equSettingCriteria);
		if (list == null || list.size() == 0) {
			model.addAttribute("ver", "B2");
		} else {
			model.addAttribute("ver", "B1");
		}
		model.addAttribute(new MatEquInfo());
		return PREFIX + "edit.html";
	}

	/**
	 * 添加
	 */
	@RequestMapping("/add")
	@BussinessLog(businessName = "添加物料/设备信息")
	@ResponseBody
	public Tip add(@Valid MatEquInfo matEquInfo, BindingResult result) {
		if (result.hasErrors()) {

		}
		matEquInfo.setStatus(UsingStatus.ENABLE);
		if (matEquInfo.getManufacturerId() == null || "".equals(matEquInfo.getManufacturerId())) {
			matEquInfo.setManufacturerId(null);
		}
		this.matEquInfoService.save(matEquInfo);
		return SUCCESS_TIP;
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("/editView/{infoId}")
	public String editView(@PathVariable String infoId, Model model) {
		EquSettingCriteria equSettingCriteria = new EquSettingCriteria();
		equSettingCriteria.setCabinetType(CabinetType.TEM_CABINET);
		List<EquSetting> list = this.equSettingService.findAllCabinet(equSettingCriteria);
		if (list == null || list.size() == 0) {
			model.addAttribute("ver", "B2");
		} else {
			model.addAttribute("ver", "B1");
		}
		MatEquInfo info = this.matEquInfoService.get(infoId);
		model.addAttribute(info);
		LogObjectHolder.me().set(info);
		return PREFIX + "edit.html";
	}

	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改物料/设备信息")
	@ResponseBody
	public Tip edit(@Valid MatEquInfo matEquInfo, BindingResult result) throws NoPermissionException {
		if (result.hasErrors()) {

		}
		MatEquInfo tmpMatEquInfo = this.matEquInfoService.get(matEquInfo.getId());
		if (tmpMatEquInfo.getStatus().equals(UsingStatus.ENABLE)) {
			matEquInfo.setStatus(UsingStatus.ENABLE);
		} else {
			matEquInfo.setStatus(UsingStatus.DISABLE);
		}
		try {
			BeanUtils.copyProperties(tmpMatEquInfo, matEquInfo);
			if (matEquInfo.getManufacturerId() == null || "".equals(matEquInfo.getManufacturerId())) {
				tmpMatEquInfo.setManufacturerId(null);
			}
			this.matEquInfoService.edit(tmpMatEquInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS_TIP;
	}

	/**
	 * 启用/禁用
	 */
	@RequestMapping("/update/{infoId}")
	@BussinessLog(businessName = "启用/禁用")
	@ResponseBody
	public Tip update(@PathVariable String infoId) {
		Tip tip = new SuccessTip();
		try {
			this.matEquInfoService.updateStatus(infoId);
		} catch (DoSthException e) {
			tip.setCode(e.getCode());
			tip.setMessage(e.getMessage());
		}
		return tip;
	}

	/**
	 * 创建物料/设备tree
	 * 
	 * @param type   类型 MatEqu枚举值
	 * @param isRoot 是否有根节点
	 */
	@RequestMapping("/tree/{type}/{isRoot}")
	@ResponseBody
	public List<ZTreeNode> tree(@PathVariable String type, @PathVariable String isRoot) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("isRoot", isRoot);
		List<ZTreeNode> tree = this.matEquInfoService.tree(params);
		return tree;
	}

	/**
	 * 导出
	 */
	@RequestMapping("/exportExcel/{name}/{status}")
	@ResponseBody
	public String exportExcel(HttpServletRequest request, HttpServletResponse response, @PathVariable String name,
			@PathVariable String status) throws IOException {
		this.matEquInfoService.exportExcel(request, response, name, status);
		return null;
	}
}