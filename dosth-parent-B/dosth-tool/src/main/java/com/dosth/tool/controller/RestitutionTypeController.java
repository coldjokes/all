package com.dosth.tool.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.naming.NoPermissionException;
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
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.BaseController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.ToolUtil;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.tool.service.RestitutionTypeService;

/**
 * 归还类型定义Controller
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/restitutionType")
public class RestitutionTypeController extends BaseController {

	private static String PREFIX = "/tool/restitutionType/";

	@Autowired
	private RestitutionTypeService restitutionTypeService;

	/**
	 * 跳转到主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}

	/**
	 * 添加页面
	 */
	@RequestMapping("/restitutionType_add")
	public String addView(Model model) {
		model.addAttribute(new RestitutionType());
		return PREFIX + "edit.html";
	}

	/**
	 * 添加
	 */
	@RequestMapping("/add")
	@BussinessLog(businessName = "添加归还定义")
	@ResponseBody
	public Tip add(@Valid RestitutionType restitutionType, BindingResult result) {
		if (result.hasErrors()) {

		}
		restitutionType.setStatus(UsingStatus.ENABLE);
		this.restitutionTypeService.save(restitutionType);
		return SUCCESS_TIP;
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("/restitutionType_edit/{id}")
	public String editView(@PathVariable String id, Model model) {
		RestitutionType restitutionType = this.restitutionTypeService.get(id);
		model.addAttribute(restitutionType);
		LogObjectHolder.me().set(restitutionType);
		return PREFIX + "edit.html";
	}

	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改归还定义")
	@ResponseBody
	public Tip edit(@Valid RestitutionType restitutionType, BindingResult result) throws NoPermissionException {
		if (result.hasErrors()) {

		}
		RestitutionType tmpRestitutionType = this.restitutionTypeService.get(restitutionType.getId());
		if (tmpRestitutionType.getStatus().equals(UsingStatus.ENABLE)) {
			restitutionType.setStatus(UsingStatus.ENABLE);
		} else {
			restitutionType.setStatus(UsingStatus.DISABLE);
		}
		try {
			BeanUtils.copyProperties(tmpRestitutionType, restitutionType);
			this.restitutionTypeService.update(restitutionType);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return SUCCESS_TIP;
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@BussinessLog(businessName = "删除归还定义", ignore = true)
	@ResponseBody
	public Tip delete(@RequestParam String id) {
		if (ToolUtil.isEmpty(id)) {

		}
		RestitutionType restitutionType = this.restitutionTypeService.get(id);
		this.restitutionTypeService.delete(restitutionType);
		return SUCCESS_TIP;
	}

	/**
	 * 启用/禁用
	 */
	@RequestMapping("/update/{id}")
	@BussinessLog(businessName = "启用/禁用")
	@ResponseBody
	public Tip update(@PathVariable String id) {
		RestitutionType restitutionType = this.restitutionTypeService.get(id);
		if (restitutionType.getStatus().equals(UsingStatus.ENABLE)) {
			restitutionType.setStatus(UsingStatus.DISABLE);
		} else {
			restitutionType.setStatus(UsingStatus.ENABLE);
		}
		this.restitutionTypeService.update(restitutionType);
		return SUCCESS_TIP;
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
		Page<RestitutionType> page = this.restitutionTypeService.getPage(pageNo, pageSize, name, status);
		return page;
	}

	/**
	 * @Desc 创建tree[归还类型]
	 */
	@RequestMapping("/reTypeTree")
	@ResponseBody
	public List<ZTreeNode> reTypeTree() {
		List<ZTreeNode> tree = this.restitutionTypeService.reTypeTree();
		return tree;
	}
}
