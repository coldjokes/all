package com.dosth.tool.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.node.ZTreeNode;
import com.dosth.common.controller.BaseController;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.BorrowPopedomService;
import com.dosth.util.OpTip;


/**
 * 领取权限Controller
 * 
 * @author Weifeng Li
 *
 */
@Controller
@RequestMapping("/borrowPopedom")
public class BorrowPopedomController extends BaseController {

	private static String PREFIX = "/tool/borrowPopedom/";
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private BorrowPopedomService borrowPopedomService;
	
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}
	
	/**
	 * @description 初始化帐户树
	 * @return
	 */
	@RequestMapping("/initAccountTree")
	@ResponseBody
	public List<ZTreeNode> initAccountTree() {
		return this.adminService.initAccountTree();
	}
	
	/**
	 * @description 初始化借出权限树
	 * @param opObjId 操作对象Id
	 * @return
	 */
	@RequestMapping("/initBorrowPopedomTree")
	@ResponseBody
	public List<ZTreeNode> initBorrowPopedomTree(@RequestParam("opObjId") String opObjId) {
		return this.borrowPopedomService.initBorrowPopedomTree(opObjId);
	}
	
	/**
	 * @description 绑定取料权限
	 * @param accountId 帐号Id
	 * @param popedoms  取料权限
	 * @return
	 */
	@RequestMapping("/bindBorrowPopedoms")
	@ResponseBody
	public OpTip bindBorrowPopedoms(@Valid String opObjId, String popedoms) {
		OpTip tip = new OpTip(200, "保存成功!");
		Boolean flag = this.borrowPopedomService.bindBorrowPopedoms(opObjId, popedoms);
		if (flag != null && flag) {
			return tip;
		} else {
			tip = new OpTip(201, "保存失败!");
			return tip;
		}
	}
}
