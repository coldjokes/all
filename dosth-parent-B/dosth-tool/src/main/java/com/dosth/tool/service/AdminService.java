package com.dosth.tool.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnbaosi.dto.OpTip;
import com.dosth.app.dto.FeignUser;
import com.dosth.common.node.ZTreeNode;
import com.dosth.toolcabinet.dto.DeptInfo;
import com.dosth.toolcabinet.dto.UserInfo;

/**
 * @description dosth访问接口
 * 
 * @author liweifeng
 *
 */
@FeignClient("service-admin")
public interface AdminService {

	/**
	 * @description 根据帐号Id获取用户信息
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/getUserByAccountId")
	public UserInfo getUserByAccountId(@RequestBody FeignUser feignUser);

	/**
	 * @description 获取所有用户信息
	 * @return
	 */
	@RequestMapping("/feign/getUserInfos")
	public List<UserInfo> getUserInfos();

	/**
	 * @description 获取用户信息
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/getUserInfo")
	public UserInfo getUserInfo(@RequestBody String AccountId);

	/**
	 * @description 获取部门信息
	 * @return
	 */
	@RequestMapping("/feign/getDeptInfo")
	public List<DeptInfo> getDeptInfo();

	/**
	 * @description 初始化帐户树
	 * @return
	 */
	@RequestMapping(value = "/feign/initAccountTree")
	public List<ZTreeNode> initAccountTree();

	/**
	 * @description 根据帐号ID获取用户角色
	 * @return
	 */
	@RequestMapping(value = "/feign/getUserRole")
	public UserInfo getUserRole(@RequestParam("accountId") String accountId);
	
	/**
	 * @description 根据部门Id获取部门下所有员工
	 * @param deptId 部门Id
	 */
	@RequestMapping(value = "/feign/getAccountIdListByDeptId")
	public List<String> getAccountIdListByDeptId(@RequestParam("deptId") String deptId);

	/**
	 * @description 清理用户信息
	 * @return
	 */
	@RequestMapping(value = "/feign/clearUser")
	public OpTip clearUser();
}