package com.dosth.admin.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.admin.common.exception.BizExceptionEnum;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.constant.factory.ConstantFactory;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Dept;
import com.dosth.admin.entity.User;
import com.dosth.admin.service.DeptService;
import com.dosth.admin.service.UserService;
import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.controller.BaseController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.ToolUtil;
import com.dosth.util.OpTip;

/**
 * 部门Controller
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/mgrDept")
public class DeptController extends BaseController {

	private static String PREFIX = "/admin/dept/";

	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;

	/**
	 * 跳转到部门管理的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "dept.html";
	}

	/**
	 * 跳转到添加部门的页面
	 */
	@RequestMapping("/dept_add")
	public String addView(Model model) {
		return PREFIX + "dept_add.html";
	}

	/**
	 * 添加部门
	 */
	@RequestMapping("/add")
	@ResponseBody
	public OpTip add(@Valid Dept dept) {
		OpTip tip = new OpTip(200, "添加成功");
		Dept tmpDept = this.deptService.findDeptByDeptName(dept.getDeptName());
		if (tmpDept != null) {
			tip = new OpTip(201, "部门名称重复");
			return tip;
		}
		this.deptService.save(dept);
		return tip;
	}

	/**
	 * 跳转到修改部门页面
	 */
	@RequestMapping("/dept_edit/{id}")
	public String deptEdit(@PathVariable String id, Model model) {
		if (ToolUtil.isEmpty(id)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		Dept dept = this.deptService.get(id);
		model.addAttribute(dept);
		model.addAttribute("deptName", ConstantFactory.me().getDeptName(dept.getId()));
		if (StringUtils.isNotBlank(dept.getpId())) {
			model.addAttribute("pIdName", ConstantFactory.me().getDeptName(dept.getpId()));
		}
		LogObjectHolder.me().set(dept);
		return PREFIX + "dept_edit.html";
	}

	/**
	 * 修改部门
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改部门")
	@ResponseBody
	public Tip edit(@Valid Dept dept) {
		this.deptService.update(dept);
		dept.setFullIds(dept.getpId() + "," + dept.getId());
		this.deptService.update(dept);
		return SUCCESS_TIP;
	}

	/**
	 * 删除部门（逻辑删除）
	 */
	@RequestMapping("/delete")
	@BussinessLog(businessName = "删除部门", ignore = true)
	@ResponseBody
	public OpTip delete(@RequestParam String deptId) {
		OpTip tip = new OpTip(200, "删除成功！");
		if (ToolUtil.isEmpty(deptId)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		
		// 部门下有人员，不可删除
		List<User> userList = this.userService.findUserByDeptId(deptId);
		if (userList != null && userList.size() > 0) {
			tip = new OpTip(201, "部门下有人员，不能删除！");
			return tip;
		}

		// 删除所有子部门如果子部门下有人员，不可删除
		List<Dept> sonDeptList = this.deptService.getDeptByPid(deptId);
		if (CollectionUtils.isNotEmpty(sonDeptList)) {
			for (Dept sonDept : sonDeptList) {
				List<User> sonUserList = this.userService.findUserByDeptId(sonDept.getId());
				if(sonUserList !=null && sonUserList.size()>0) {
					tip = new OpTip(201, "子部门下有人员，不能删除！");
					return tip;
				}
				sonDept.setStatus(ManagerStatus.DELETED);
				this.deptService.update(sonDept);
			}
		}

		Dept dept = this.deptService.get(deptId);
		dept.setStatus(ManagerStatus.DELETED);
		this.deptService.update(dept);
		return tip;
	}

	/**
	 * 创建部门tree
	 * 
	 * @return
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<ZTreeNode> tree() {
		List<ZTreeNode> tree = this.deptService.tree(null);
		return tree;
	}

	/**
	 * 查询账户列表
	 */
	@RequestMapping("/list")
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
		Page<Dept> page = this.deptService.getPage(pageNo, pageSize, name);
		return page;
	}

}