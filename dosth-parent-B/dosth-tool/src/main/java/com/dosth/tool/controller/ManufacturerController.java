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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.BaseController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.entity.Manufacturer;
import com.dosth.tool.entity.ManufacturerCustom;
import com.dosth.tool.service.ManufacturerCustomService;
import com.dosth.tool.service.ManufacturerService;

/**
 * 供货商管理
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/manufacturer")
public class ManufacturerController extends BaseController {

	private static String PREFIX = "/tool/manufacturer/";

	@Autowired
	private ManufacturerService manufacturerService;
	@Autowired
	private ManufacturerCustomService manufacturerCustomService;

	/**
	 * 跳转到列表页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}

	/**
	 * 查询供应商列表
	 */
	@GetMapping("/list")
	@ResponseBody
	public Object list() {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {

		}
		String name = super.getPara("name");
		String status = super.getPara("status");
		Page<Manufacturer> page = this.manufacturerService.getPage(pageNo, pageSize, name, status);
		return page;
	}

	/**
	 * 查询供应商子表
	 */
	@GetMapping("/sublist")
	@ResponseBody
	public Object sublist() {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {

		}
		String name = super.getPara("name");
		String status = super.getPara("status");
		String manufacturerId = super.getPara("manufacturerId");
		Page<ManufacturerCustom> page = this.manufacturerCustomService.getPage(pageNo, pageSize, manufacturerId, name,
				status);
		return page;
	}

	/**
	 * 跳转到添加供应商
	 */
	@RequestMapping("/addView")
	public String addView(Model model) {
		model.addAttribute(new Manufacturer());
		return PREFIX + "edit.html";
	}

	/**
	 * 添加供应商
	 */
	@RequestMapping("/add")
	@ResponseBody
	public Tip add(@Valid Manufacturer manufacturer) {
		manufacturer.setStatus(UsingStatus.ENABLE);
		this.manufacturerService.save(manufacturer);
		return SUCCESS_TIP;
	}

	/**
	 * 跳转到修改供应商
	 */
	@RequestMapping("/editView/{manufacturerId}")
	public String editView(@PathVariable String manufacturerId, Model model) {
		Manufacturer manufacturer = this.manufacturerService.get(manufacturerId);
		model.addAttribute(manufacturer);
		LogObjectHolder.me().set(manufacturer);
		return PREFIX + "edit.html";
	}

	/**
	 * 修改供应商
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public Tip edit(@Valid Manufacturer manufacturer) throws NoPermissionException {
		Manufacturer tmpManufacturer = this.manufacturerService.get(manufacturer.getId());
		manufacturer.setStatus(tmpManufacturer.getStatus());
		try {
			BeanUtils.copyProperties(tmpManufacturer, manufacturer);
			this.manufacturerService.update(manufacturer);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return SUCCESS_TIP;
	}

	/**
	 * @description 更新供应商或联系人状态
	 * @param type 供应商或联系人 1 供应商 2 联系人
	 * @param manufacturerId 供应商或联系人Id
	 * @param status 状态 1 启用 0 停用
	 */
	@RequestMapping("/update/{type}/{manufacturerId}/{status}")
	@ResponseBody
	public Tip update(@PathVariable("type") String type,  @PathVariable("manufacturerId") String manufacturerId,
			@PathVariable("status") String status) {
		return this.manufacturerService.updateStatus(type, manufacturerId, status);
	}

	/**
	 * 跳转到添加供应商-详情
	 */
	@RequestMapping("/addInfoView/{manufacturerId}")
	public String addInfoView(@PathVariable String manufacturerId, Model model) {
		ManufacturerCustom manufacturerCustom = new ManufacturerCustom();
		manufacturerCustom.setManufacturerId(manufacturerId);
		model.addAttribute(manufacturerCustom);
		return PREFIX + "editInfo.html";
	}

	/**
	 * 添加供应商-详情
	 */
	@RequestMapping("/addInfo")
	@ResponseBody
	public Tip addInfo(@Valid ManufacturerCustom manufacturerCustom) {
		manufacturerCustom.setStatus(UsingStatus.ENABLE);
		this.manufacturerCustomService.save(manufacturerCustom);
		return SUCCESS_TIP;
	}

	/**
	 * 跳转到修改供应商-详情
	 */
	@RequestMapping("/editInfoView/{manufacturerCustomId}")
	public String editInfoView(@PathVariable String manufacturerCustomId, Model model) {
		ManufacturerCustom manufacturerCustom = this.manufacturerCustomService.get(manufacturerCustomId);
		model.addAttribute(manufacturerCustom);
		LogObjectHolder.me().set(manufacturerCustom);
		return PREFIX + "editInfo.html";
	}

	/**
	 * 修改供应商-详情
	 */
	@RequestMapping("/editInfo")
	@ResponseBody
	public Tip editInfo(@Valid ManufacturerCustom manufacturerCustom) throws NoPermissionException {
		ManufacturerCustom tmpManufacturerCustom = this.manufacturerCustomService.get(manufacturerCustom.getId());
		manufacturerCustom.setStatus(tmpManufacturerCustom.getStatus());
		try {
			BeanUtils.copyProperties(tmpManufacturerCustom, manufacturerCustom);
			this.manufacturerCustomService.update(manufacturerCustom);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return SUCCESS_TIP;
	}

	/**
	 * 删除供应商-详情
	 */
	@RequestMapping("/deleteInfo/{manufacturerCustomId}")
	@ResponseBody
	public Tip deleteInfo(@PathVariable String manufacturerCustomId) throws NoPermissionException {
		ManufacturerCustom manufacturerCustom = this.manufacturerCustomService.get(manufacturerCustomId);
		this.manufacturerCustomService.delete(manufacturerCustom);
		return SUCCESS_TIP;
	}

	/**
	 * 创建供应商tree
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<ZTreeNode> tree() {
		List<ZTreeNode> tree = this.manufacturerService.tree(null);
		return tree;
	}
}