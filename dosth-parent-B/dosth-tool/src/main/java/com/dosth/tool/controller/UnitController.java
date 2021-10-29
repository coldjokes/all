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
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.BaseController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.entity.Unit;
import com.dosth.tool.service.UnitService;

/**
 * 单位管理Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/unit")
public class UnitController extends BaseController {

	private static String PREFIX = "/tool/unit/";
	
	@Autowired
	private UnitService unitService;

	/**
	 * 跳转到业务主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
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
		String name = super.getPara("name");
		String status = super.getPara("status");
		Page<Unit> page = this.unitService.getPage(pageNo, pageSize, name, status);
		return page;
	}
	
	/**
	 * 加载添加页面
	 */
	@RequestMapping("/addView")
	public String addView(Model model) {
		model.addAttribute(new Unit());
		return PREFIX + "edit.html";
	}
	
	/**
	 * 添加
	 */
	@RequestMapping("/add")
	@BussinessLog(businessName = "添加单位")
	@ResponseBody
	public Tip add(@Valid Unit unit, BindingResult result) {
		if (result.hasErrors()) {
			
		}
		unit.setStatus(UsingStatus.ENABLE);
		this.unitService.save(unit);
		return SUCCESS_TIP;
	}
	
	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("/editView/{unitId}")
	public String editView(@PathVariable String unitId, Model model) {
		Unit unit = this.unitService.get(unitId);
		model.addAttribute(unit);
		LogObjectHolder.me().set(unit);
		return PREFIX + "edit.html";
	}

	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改单位")
	@ResponseBody
	public Tip edit(@Valid Unit unit, BindingResult result) throws NoPermissionException {
		if (result.hasErrors()) {
			
		}
		Unit tmpUnit = this.unitService.get(unit.getId());
		if(tmpUnit.getStatus().equals(UsingStatus.ENABLE)) {
			unit.setStatus(UsingStatus.ENABLE);
		}else {
			unit.setStatus(UsingStatus.DISABLE);
		}
		try {
			BeanUtils.copyProperties(tmpUnit, unit);
			this.unitService.update(tmpUnit);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return SUCCESS_TIP;
	}
	
	/**
	 * 启用/禁用
	 */
	@RequestMapping("/update/{unitId}")
	@BussinessLog(businessName = "启用/禁用")
	@ResponseBody
	public Tip update(@PathVariable String unitId) {
		Unit unit = this.unitService.get(unitId);
		if (unit.getStatus().equals(UsingStatus.ENABLE)) {
			unit.setStatus(UsingStatus.DISABLE);
		} else {
			unit.setStatus(UsingStatus.ENABLE);
		}
		this.unitService.update(unit);
		return SUCCESS_TIP;
	}
	
	/**
	 * 创建单位tree
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<ZTreeNode> tree() {
		List<ZTreeNode> tree = this.unitService.tree(null);
		return tree;
	}
}