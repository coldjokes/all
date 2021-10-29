package com.dosth.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.admin.entity.AccountRole;
import com.dosth.admin.rpc.AdminRpcService;
import com.dosth.admin.service.AccountRoleService;
import com.dosth.admin.service.AccountService;
import com.dosth.admin.service.DeptService;
import com.dosth.admin.service.UserService;
import com.dosth.app.dto.FeignUser;
import com.dosth.common.node.ZTreeNode;
import com.dosth.toolcabinet.dto.AccountUserInfo;
import com.dosth.toolcabinet.dto.DeptInfo;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.util.OpTip;

@RestController
@RequestMapping("/feign")
public class FeignController {

	@Autowired
	private AdminRpcService adminRpcService;
	@Autowired
	private UserService userService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private AccountRoleService accountRoleService;
	@Autowired
	private AccountService accountService;

	/**
	 * @description 根据卡片获取帐户用户信息
	 * @param cardStr 卡片
	 * @return
	 */
	@RequestMapping(value = "/accountUserInfo/{cardStr}")
	public AccountUserInfo getAccountUserInfoByCardStr(@PathVariable String cardStr) {
		return this.adminRpcService.getAccountUserInfoByCardStr(cardStr);
	}

	/**
	 * @description 校验登录名和密码
	 * @param loginName 登录名
	 * @param pwd       密码
	 * @return
	 */
	@RequestMapping(value = "/checkUserPwd/{loginName}/{pwd}")
	public AccountUserInfo checkUserPwd(@PathVariable String loginName, @PathVariable String pwd) {
		return this.adminRpcService.checkUserPwd(loginName, pwd);
	}

	/**
	 * @description 初始化帐户树
	 * @return
	 */
	@RequestMapping(value = "/initAccountTree")
	public List<ZTreeNode> initAccountTree() {
		return this.adminRpcService.initAccountTree();
	}

	/**
	 * @description IC卡绑定
	 * @param accountId 帐户Id
	 * @param cardNo    卡号
	 * @return
	 */
	@RequestMapping(value = "/icBind")
	public boolean icBind(@RequestParam("accountId") String accountId, @RequestParam("cardNo") String cardNo) {
		return this.adminRpcService.icBind(accountId, cardNo);
	}

	/**
	 * @description 根据帐号Id获取用户信息
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping(value = "/getUserByAccountId")
	public UserInfo getUserByAccountId(@RequestBody FeignUser feignUser) {
		UserInfo userInfo = this.userService.getUserByAccountId(feignUser);
		return userInfo;
	}

	/**
	 * @description 获取所有用户信息
	 * @return
	 */
	@RequestMapping(value = "/getUserInfos")
	public List<UserInfo> getUserInfos() {
		List<UserInfo> userInfo = this.userService.getUserInfos();
		return userInfo;
	}

	/**
	 * @description 获取用户信息
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping(value = "/getUserInfo")
	public UserInfo getUserInfo(@RequestBody String AccountId) {
		UserInfo userInfo = this.userService.getUserInfo(AccountId);
		return userInfo;
	}

	/**
	 * @description 获取部门信息
	 * @return
	 */
	@RequestMapping(value = "/getDeptInfo")
	public List<DeptInfo> getDeptInfo() {
		List<DeptInfo> deptInfoList = this.deptService.getDeptInfo();
		return deptInfoList;
	}

	/**
	 * @description 人脸识别绑定
	 * @param accountId 帐户Id
	 * @param file      人像
	 * @return
	 */
	@RequestMapping(value = "/faceBind", method = RequestMethod.POST)
	public boolean faceBind(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "file") String file) {
		return this.adminRpcService.faceBind(accountId, file);
	}

	/**
	 * @description 人脸识别登录
	 * @param file 人像
	 * @return
	 */
	@RequestMapping(value = "/faceLogin", method = RequestMethod.POST)
	public AccountUserInfo faceLogin(@RequestParam(value = "file") String file) {
		return this.adminRpcService.faceLogin(file);
	}

	/**
	 * @description 根据帐号ID获取用户角色
	 * @param accountId 帐号ID
	 * @return
	 */
	@RequestMapping(value = "/getUserRole")
	public UserInfo getUserRole(@RequestParam(value = "accountId") String accountId) {
		UserInfo userRole = null;
		AccountRole accountRole = this.accountRoleService.getUserRole(accountId);
		if (accountRole != null) {
			userRole = new UserInfo();
			userRole.setRoleId(accountRole.getRoleId());
		}
		return userRole;
	}

	/**
	 * @description 根据部门Id获取部门下所有员工
	 * @param deptId 部门Id
	 */
	@RequestMapping(value = "/getAccountIdListByDeptId")
	public List<String> getAccountIdListByDeptId(@RequestParam("deptId") String deptId) {
		return this.accountService.getAccountIdListByDeptId(deptId);
	}

	/**
	 * @description 清理用户信息
	 * @return
	 */
	@RequestMapping(value = "/clearUser")
	public OpTip clearUser() {
		return this.userService.clearUser();
	}
}