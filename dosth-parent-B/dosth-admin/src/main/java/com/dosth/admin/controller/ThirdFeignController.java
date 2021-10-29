package com.dosth.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.dto.admin.FeignDept;
import com.cnbaosi.dto.admin.FeignUser;
import com.dosth.admin.service.DeptService;
import com.dosth.admin.service.UserService;
import com.dosth.util.OpTip;

/**
 * @description 第三方远程接口
 * @author guozhidong
 *
 */
@RestController
@RequestMapping("/thirdfeign")
public class ThirdFeignController {
	@Autowired
	private UserService userService;
	@Autowired
	private DeptService deptService;

	/**
	 * @description 同步用户信息
	 * @param userList 用户信息列表
	 * @return 同步状态
	 */
	@RequestMapping(value = "/syncFeignUser")
	public OpTip syncFeignUser(@RequestBody List<FeignUser> userList) {
		return this.userService.syncFeignUser(userList);
	}

	/**
	 * @description 同步部门信息
	 * @param deptList 部门信息列表
	 * @return 同步状态
	 */
	@RequestMapping(value = "/syncFeignDept")
	public OpTip syncFeignDept(@RequestBody List<FeignDept> deptList) {
		return this.deptService.syncFeignDept(deptList);
	}
}