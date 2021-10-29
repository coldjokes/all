package com.dosth.tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.common.controller.BaseController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.service.CabinetService;

/**
 * @description 树形控件Controller
 * @author guozhidong
 *
 */
@RestController
@RequestMapping("/tree")
public class TreeController extends BaseController {
	
	@Autowired
	private CabinetService cabinetService;
	
	/**
	 * @description 创建柜体树
	 */
	@PostMapping(value = "/createCabinetTree")
	public List<ZTreeNode> createCabinetTree() {
		return this.cabinetService.createCabinetTree();
	}
	
	/**
	 * @description 创建主柜树形数据
	 * @return
	 */
	@PostMapping(value = "/createMainCabinetTree")
	public List<ZTreeNode> createMainCabinetTree() {
		return this.cabinetService.createMainCabinetTree();
	}
}