package com.dosth.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.admin.common.exception.BizExceptionEnum;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Roles;
import com.dosth.admin.service.RoleService;
import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.annotion.Permission;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.controller.BaseController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.ToolUtil;

/**
 * 角色管理Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/mgrRole")
public class RoleController extends BaseController {

	private static String PREFIX = "/admin/role/";

	@Autowired
	private RoleService roleService;

	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "role.html";
	}

	/**
	 * 加载树形数据
	 */
	@RequestMapping("/tree/{isRoot}")
	@ResponseBody
	public List<ZTreeNode> tree(@PathVariable int isRoot) {
		List<ZTreeNode> tree = this.roleService.tree(null);
		if (isRoot == 1) {
			tree.add(ZTreeNode.createParent("角色"));
		}
		return tree;
	}

	/**
	 * 添加角色
	 */
	@RequestMapping("/add")
	@BussinessLog(businessName = "添加角色")
	@Permission
	@ResponseBody
	public Tip add(@Valid Roles role, String menuIds, BindingResult result) {
		if (result.hasErrors()) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		this.roleService.save(role, menuIds);
		return SUCCESS_TIP;
	}

	/**
	 * 加载角色json数据
	 */
	@RequestMapping("/role_edit/{roleId}")
	@ResponseBody
	public Roles userEdit(@PathVariable String roleId, Model model) {
		if (ToolUtil.isEmpty(roleId)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		Roles role = this.roleService.get(roleId);
		model.addAttribute(role);
		LogObjectHolder.me().set(role);
		return role;
	}

	/**
	 * 修改角色
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改角色")
	@ResponseBody
	public Tip edit(@Valid Roles role, String menuIds, BindingResult result) {
		if (result.hasErrors()) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		role = this.roleService.update(role, menuIds);
		return SUCCESS_TIP;
	}

	/**
	 * 删除角色
	 */
	@RequestMapping("/delete")
	@BussinessLog(businessName = "删除角色")
	@Permission
	@ResponseBody
	public Tip delete(@RequestParam String roleId) {
		if (ToolUtil.isEmpty(roleId)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		Roles role = this.roleService.get(roleId);
		this.roleService.delete(role);
		return SUCCESS_TIP;
	}

	/**
	 * 创建角色树
	 */
	@RequestMapping("/createRoleTree")
	@ResponseBody
	public List<ZTreeNode> createRoleTree() {
		List<ZTreeNode> roleTree = this.roleService.createRoleTree();
		return roleTree;
	}
}