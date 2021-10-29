package com.dosth.tool.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.BaseController;
import com.dosth.common.util.ToolUtil;
import com.dosth.tool.common.warpper.PlcSettingPageWarpper;
import com.dosth.tool.entity.PlcSetting;
import com.dosth.tool.service.PlcSettingService;

/**
 * PLC设置Controller
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/plcSetting")
public class PlcSettingController extends BaseController {
	
	private static String PREFIX = "/tool/plcSetting/";
	
	@Autowired
	private PlcSettingService plcSettingService;
	
	/**
	 * 跳转到plc设置页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("columns", new PlcSettingPageWarpper(null).createColumns(TableSelectType.RADIO));
		return PREFIX + "plcSetting.html";
	}
	
	/**
	 * 跳转到添加页面
	 */
	@RequestMapping("/plcSetting_add")
	public String addView(Model model) {
		model.addAttribute(new PlcSetting());
		return PREFIX + "plcSetting_add.html";
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	@BussinessLog(businessName = "添加PLC设置")
	@ResponseBody
	public Tip add(@Valid PlcSetting plcSetting, BindingResult result) {
		if (result.hasErrors()) {
			
		}
		plcSetting.setStatus(UsingStatus.ENABLE);
		this.plcSettingService.save(plcSetting);
		return SUCCESS_TIP;
	}
	
	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("/plcSetting_edit/{id}")
	public String plcSettingEdit(@PathVariable String id, Model model) {
		PlcSetting plcSetting = this.plcSettingService.get(id);
		model.addAttribute(plcSetting);
		LogObjectHolder.me().set(plcSetting);
		return PREFIX + "plcSetting_edit.html";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改")
	@ResponseBody
	public Tip edit(@Valid PlcSetting plcSetting, BindingResult result) {
		if (result.hasErrors()) {

		}
		PlcSetting tmpPlcSetting = this.plcSettingService.get(plcSetting.getId());
		if(tmpPlcSetting.getStatus().equals(UsingStatus.ENABLE)) {
			plcSetting.setStatus(UsingStatus.ENABLE);
		}else {
			plcSetting.setStatus(UsingStatus.DISABLE);
		}
		try {
			BeanUtils.copyProperties(tmpPlcSetting, plcSetting);
			this.plcSettingService.update(plcSetting);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return SUCCESS_TIP;
	}
	
	/**
	 * 删除（逻辑删除）
	 */
	@RequestMapping("/delete")
	@BussinessLog(businessName = "删除", ignore = true)
	@ResponseBody
	public Tip delete(@RequestParam String id) {
		if (ToolUtil.isEmpty(id)) {
			
		}
		PlcSetting plcSetting = this.plcSettingService.get(id);
		this.plcSettingService.delete(plcSetting);
		return SUCCESS_TIP;
	}
	
	/**
	 * 启用/禁用
	 */
	@RequestMapping("/update/{id}")
	@BussinessLog(businessName = "启用/禁用")
	@ResponseBody
	public Tip update(@PathVariable String id) {
		PlcSetting plcSetting = this.plcSettingService.get(id);
		if (plcSetting.getStatus().equals(UsingStatus.ENABLE)) {
			plcSetting.setStatus(UsingStatus.DISABLE);
		} else {
			plcSetting.setStatus(UsingStatus.ENABLE);
		}
		this.plcSettingService.update(plcSetting);
		return SUCCESS_TIP;
	}
	
	/**
	 * 查询列表
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
		String plcName = super.getPara("plcName");
		String status = super.getPara("status");
		Page<PlcSetting> page = this.plcSettingService.getPage(pageNo, pageSize, plcName, status);
		Map<String, Object> map = new PlcSettingPageWarpper(page).invokeObjToMap();
		return map;
	}
}
