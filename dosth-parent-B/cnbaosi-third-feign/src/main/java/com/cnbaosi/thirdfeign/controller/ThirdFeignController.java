package com.cnbaosi.thirdfeign.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.dto.ApiFeignResponse;
import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.admin.FeignDept;
import com.cnbaosi.dto.admin.FeignUser;
import com.cnbaosi.dto.tool.FeignBorrow;
import com.cnbaosi.dto.tool.FeignCabinet;
import com.cnbaosi.dto.tool.FeignFeedingList;
import com.cnbaosi.dto.tool.FeignMat;
import com.cnbaosi.dto.tool.FeignReturnBack;
import com.cnbaosi.dto.tool.FeignStock;
import com.cnbaosi.dto.tool.FeignSupplier;
import com.cnbaosi.thirdfeign.service.AdminService;
import com.cnbaosi.thirdfeign.service.ToolService;

/**
 * @description 第三方远程接口
 * @author guozhidong
 *
 */
@RestController
@RequestMapping("/thirdFeign")
public class ThirdFeignController {
	@Autowired
	private AdminService adminService;
	@Autowired
	private ToolService toolService;
	
	/**
	 * @description 同步部门信息
	 * @param deptList 部门列表
	 * @return
	 */
	@RequestMapping("/syncFeignDept")
	public OpTip syncFeignDept(@RequestBody List<FeignDept> deptList) {
		return this.adminService.syncFeignDept(deptList);
	}
	
	/**
	 * @description 同步用户信息
	 * @param userList 用户列表
	 * @return
	 */
	@RequestMapping("/syncFeignUser")
	public OpTip syncFeignUser(@RequestBody List<FeignUser> userList) {
		return this.adminService.syncFeignUser(userList);
	}
	

	/**
	 * @description 供应商同步
	 * @param supplierList 供应商列表
	 * @return
	 */
	@RequestMapping("/syncSupplier")
	public OpTip syncSupplier(@RequestBody List<FeignSupplier> supplierList) {
		return this.toolService.syncSupplier(supplierList);
	}
	
	/**
	 * @description 物料同步
	 * @param matList 物料列表
	 * @return
	 */
	@RequestMapping("/syncMat")
	public OpTip syncMat(@RequestBody List<FeignMat> matList) {
		return this.toolService.syncMat(matList);
	}
	
	/**
	 * @description 获取库存汇总（按物料）
	 * @param wareHouseAlias 仓库别称
	 * @return
	 */
	@RequestMapping("/stockDetail")
	public ApiFeignResponse<FeignStock> getStockDetail(@RequestParam("wareHouseAlias") String wareHouseAlias) {
		return this.toolService.getStockDetail(wareHouseAlias);
	}
	
	/**
	 * @description 获取货道库存明细
	 * @param 
	 * @return
	 */
	@RequestMapping("/staStock")
	public ApiFeignResponse<FeignCabinet> getStaStock() {
		return this.toolService.getStaStock();
	}
	
	/**
	 * @description 生成补料清单
	 * @param supplyList 物料补料单
	 * @return
	 */
	@RequestMapping("/feedList")
	public OpTip supplyList(@RequestBody List<FeignFeedingList> feedingList) {
		return this.toolService.supplyList(feedingList);
	}
	
	/**
	 * 获取借出记录
	 * @param beginTime 查询起始时间
	 * @param endTime 查询截止时间
	 * @return
	 */
	@RequestMapping("/borrows")
	public ApiFeignResponse<FeignBorrow> borrows(@RequestParam(value = "cabinetName") String cabinetName,
			@RequestParam(value = "endTime", required = false) Long endTime) {
		return this.toolService.syncBorrows(cabinetName, endTime);
	}
	
	/**
	 * 获取归还记录
	 * @param beginTime 查询起始时间
	 * @param endTime 查询截止时间
	 * @return 
	 */
	@RequestMapping("/returnBack")
	public ApiFeignResponse<FeignReturnBack> returnBack(@RequestParam(value = "cabinetName") String cabinetName,
			@RequestParam(value = "endTime", required = false) Long endTime) {
		return this.toolService.syncReturnBack(cabinetName, endTime);
	}
}