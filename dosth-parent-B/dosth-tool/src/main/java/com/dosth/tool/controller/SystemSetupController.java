package com.dosth.tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.SuccessTip;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.controller.BaseController;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.entity.SystemSetup;
import com.dosth.tool.service.SystemSetupService;

/**
 * @description 系统设置Controller
 * 
 * @author ZhongYan.He
 *
 */
@Controller
@RequestMapping("/systemSetup")
public class SystemSetupController extends BaseController {
	
	private static String PREFIX = "/tool/systemSetup/";

	@Autowired
	private SystemSetupService systemSetupService; 
	
	/**
	 * 跳转到业务主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		List<SystemSetup> systemList = this.systemSetupService.findAllSystemSetup();
		model.addAttribute("systemList", systemList);
		return PREFIX + "index.html";
	}
	
	/**
	 * 启用/禁用
	 */
	@RequestMapping("/update/{id}")
	@BussinessLog(businessName = "启用/禁用")
	@ResponseBody
	public Tip update(@PathVariable String id) {
		Tip tip = new SuccessTip();
		SystemSetup setup = systemSetupService.get(id);
		if(setup.getSetupValue().equals("TRUE")) {
			setup.setSetupValue(TrueOrFalse.FALSE.toString());
		}else {
			setup.setSetupValue(TrueOrFalse.TRUE.toString());
		}
		this.systemSetupService.update(setup);
		return tip;
	}
	
}
