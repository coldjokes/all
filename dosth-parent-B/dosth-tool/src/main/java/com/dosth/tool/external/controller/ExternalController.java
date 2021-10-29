package com.dosth.tool.external.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.common.restful.ApiResponse;
import com.dosth.common.restful.RestFulController;
import com.dosth.tool.external.entity.ExternalMatInfo;
import com.dosth.tool.external.entity.ExternalSupplierInfo;
import com.dosth.tool.external.service.ExternalService;
import com.dosth.tool.service.AdminService;
import com.dosth.toolcabinet.dto.DeptInfo;
import com.dosth.toolcabinet.dto.UserInfo;

/**
  *  对外接口相关
 * 
 * @author Yifeng Wang  
 */

@RestController
@RequestMapping("/external")
public class ExternalController extends RestFulController{

	@Autowired
	private ExternalService externalSvc;
	@Autowired
	private AdminService adminService;
	
	/**
	 * 人员信息
	 * @return
	 */
	@GetMapping("/userInfo")
	public ApiResponse<UserInfo> userInfo() {
		List<UserInfo> userInfos = adminService.getUserInfos();
		return success(userInfos.size(), userInfos);
	}
	
	/**
	 * 部门信息
	 * @return
	 */
	@GetMapping("/deptInfo")
	public ApiResponse<DeptInfo> deptInfo() {
		List<DeptInfo> deptInfo = adminService.getDeptInfo();
		return success(deptInfo.size(), deptInfo);
	}
	
	/**
	 * 供应商信息
	 * @return
	 */
	@GetMapping("/supplierInfo")
	public ApiResponse<ExternalSupplierInfo> supplierInfo() {
		List<ExternalSupplierInfo> supplierInfo = externalSvc.getSupplierInfo();
		return success(supplierInfo.size(), supplierInfo);
	}
	
	/**
	 * 物料信息
	 * @return
	 */
	@GetMapping("/matInfo")
	public ApiResponse<ExternalMatInfo> matInfo() {
		List<ExternalMatInfo> matInfos = externalSvc.getMatInfo();
		return success(matInfos.size(), matInfos);
	}
	
	/**
	 * 补料
	 * @param accountId 补料人id
	 * @param equId 设备id
	 * @param arrs 补料详情 12,1;22,10 (格式:单元格id,数量;单元格id,数量;..) 多个用英文逗号分隔
	 * @return 补料结果
	 */
	@PostMapping("/feedings")
	public ApiResponse<String> feedings(
			@RequestParam(value = "accountId", required = false) String accountId,
			@RequestParam(value = "equId") String equId,
			@RequestParam(value = "arrs") String arrs //数量-单元格id,多个用英文逗号分割   staId,num;staId,num;
		) {
		String resultMsg = externalSvc.feeding(accountId, equId, arrs);
		return resultMsg == null ? success("补料成功！") : error(resultMsg);
	}
}

