package com.dosth.admin.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.admin.common.exception.BizExceptionEnum;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.constant.factory.ConstantFactory;
import com.dosth.admin.entity.SystemInfo;
import com.dosth.admin.service.SystemInfoService;
import com.dosth.admin.warpper.SystemInfoPageWarpper;
import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.BaseController;
import com.dosth.common.util.ToolUtil;

/**
 * 系统维护Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/systemInfo")
public class SystemInfoController extends BaseController {
	
	private static String PREFIX = "/admin/systemInfo/";
	
	@Autowired
	private SystemInfoService systemInfoService;
	

	/**
	 * 跳转到系统维护的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("columns", new SystemInfoPageWarpper(null).createColumns(TableSelectType.RADIO));
		return PREFIX + "systemInfo.html";
	}
	
	/**
	 * 跳转到添加的页面
	 */
	@RequestMapping("/systemInfo_add")
	public String addView(Model model) {
		return PREFIX + "systemInfo_add.html";
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	@BussinessLog(businessName = "添加")
//	@Permission
	@ResponseBody
	public Tip add(@Valid SystemInfo systemInfo, BindingResult result) {
		if (result.hasErrors()) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		this.systemInfoService.save(systemInfo);
		return SUCCESS_TIP;
	}

	/**
	 * 跳转到修改的页面
	 */
	@RequestMapping("/systemInfo_edit/{id}")
	public String systemInfoEdit(@PathVariable String id, Model model) {
		if (ToolUtil.isEmpty(id)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		SystemInfo systemInfo = this.systemInfoService.get(id);
		model.addAttribute(systemInfo);
		model.addAttribute("systemName", ConstantFactory.me().getSystemName(systemInfo.getId()));
		LogObjectHolder.me().set(systemInfo);
		return PREFIX + "systemInfo_edit.html";
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改")
//	@Permission
	@ResponseBody
	public Tip edit(@Valid SystemInfo systemInfo, BindingResult result) {
		if (result.hasErrors()) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		systemInfo.setStatus(UsingStatus.ENABLE);
		this.systemInfoService.update(systemInfo);
		return SUCCESS_TIP;
	}
	
	/**
	 * 删除（逻辑删除）
	 */
	@RequestMapping("/delete")
	@BussinessLog(businessName = "删除", ignore = true)
//	@Permission
	@ResponseBody
	public Tip delete(@RequestParam String id) {
		if (ToolUtil.isEmpty(id)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		SystemInfo systemInfo = this.systemInfoService.get(id);
		this.systemInfoService.delete(systemInfo);
		return SUCCESS_TIP;
	}
	
	/**
	 * 启用/禁用
	 */
	@RequestMapping("/update/{id}")
	@BussinessLog(businessName = "启用/禁用")
	@ResponseBody
	public Tip update(@PathVariable String id) {
		SystemInfo systemInfo = this.systemInfoService.get(id);
		if (systemInfo.getStatus().equals(UsingStatus.ENABLE)) {
			systemInfo.setStatus(UsingStatus.DISABLE);
		} else {
			systemInfo.setStatus(UsingStatus.ENABLE);
		}
		this.systemInfoService.update(systemInfo);
		return SUCCESS_TIP;
	}

	/**
	 * 查询账户列表
	 */
	@RequestMapping("/list")
//	@Permission
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
		String name = super.getPara("systemName"); 
		Page<SystemInfo> page = this.systemInfoService.getPage(pageNo, pageSize, name);
		Map<String, Object> map = new SystemInfoPageWarpper(page).invokeObjToMap();
		return map;
	}
}
