package com.dosth.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.admin.service.AccountRoleService;
import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.controller.BaseController;

/**
 * 账户角色Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/mgrAcRole")
public class AccountRoleController extends BaseController {
	
	private static String PREFIX = "/admin/accrole/";

	@Autowired
	private AccountRoleService accountRoleService;

	@RequestMapping("")
	public String index() {
		return PREFIX + "role_assign.html";
	}
	
	@RequestMapping("/getRoleIdListByAccountId/{accountId}")
	@ResponseBody
	public List<String> getRoleIdList(@PathVariable String accountId) {
		List<String> roleIdList = this.accountRoleService.getRoleIdListByAccountId(accountId);
		return roleIdList;
	}

	@RequestMapping("/updateRoles")
	@BussinessLog(businessName = "角色分配")
	@ResponseBody
	public Tip updateRoles(@RequestParam String accountId, @RequestParam String roleIds) {
		this.accountRoleService.updateRoles(accountId, roleIds);
		return SUCCESS_TIP;
	}
}