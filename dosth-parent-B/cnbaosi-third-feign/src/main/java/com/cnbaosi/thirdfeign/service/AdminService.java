package com.cnbaosi.thirdfeign.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.admin.FeignDept;
import com.cnbaosi.dto.admin.FeignUser;

/**
 * @description 用户管理远程接口
 * @Author guozhidong
 */
@FeignClient("service-admin")
public interface AdminService {
	/**
	 * @description 同步用户信息
	 * @param userList 用户信息列表
	 * @return 同步状态
	 */
	@RequestMapping(value = "/thirdfeign/syncFeignUser")
	public OpTip syncFeignUser(@RequestBody List<FeignUser> userList);
	
	/**
	 * @description 同步部门信息
	 * @param deptList 部门信息列表
	 * @return 同步状态
	 */
	@RequestMapping(value = "/thirdfeign/syncFeignDept")
	public OpTip syncFeignDept(@RequestBody List<FeignDept> deptList);
}