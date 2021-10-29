package com.cnbaosi.thirdfeign.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnbaosi.dto.ApiFeignResponse;
import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.tool.FeignBorrow;
import com.cnbaosi.dto.tool.FeignCabinet;
import com.cnbaosi.dto.tool.FeignFeedingList;
import com.cnbaosi.dto.tool.FeignMat;
import com.cnbaosi.dto.tool.FeignReturnBack;
import com.cnbaosi.dto.tool.FeignStock;
import com.cnbaosi.dto.tool.FeignSupplier;

/**
 * @description 工具访问接口
 * 
 * @author guozhidong
 *
 */
@FeignClient("service-tool")
public interface ToolService {
	/**
	 * @description 供应商同步
	 * @param supplierList 供应商列表
	 * @return
	 */
	@RequestMapping("/thirdfeign/syncSupplier")
	public OpTip syncSupplier(@RequestBody List<FeignSupplier> supplierList);

	/**
	 * @description 物料同步
	 * @param matList 物料列表
	 * @return
	 */
	@RequestMapping("/thirdfeign/syncMat")
	public OpTip syncMat(@RequestBody List<FeignMat> matList);

	/**
	 * @description 获取库存汇总（按物料）
	 * @param wareHouseAlias 仓库别称
	 * @return
	 */
	@RequestMapping("/thirdfeign/stockDetail")
	public ApiFeignResponse<FeignStock> getStockDetail(@RequestParam("wareHouseAlias") String wareHouseAlias);
	
	/**
	 * @description 获取货道库存明细
	 * @param 
	 * @return
	 */
	@RequestMapping("/thirdfeign/staStock")
	public ApiFeignResponse<FeignCabinet> getStaStock();

	/**
	 * @description 补料清单
	 * @param supplyList 物料补料单
	 * @return
	 */
	@RequestMapping("/thirdfeign/feedList")
	public OpTip supplyList(@RequestBody List<FeignFeedingList> feedingList);
	
	/**
	 * @description 同步领用记录
	 * @param supplyList 物料补料单
	 * @return
	 */
	@RequestMapping("/thirdfeign/syncBorrows")
	public ApiFeignResponse<FeignBorrow> syncBorrows(
			@RequestParam("cabinetName") String cabinetName, 
			@RequestParam(value = "endTime", required = false) Long endTime);
	
	/**
	 * @description 同步归还记录
	 * @param supplyList 物料补料单
	 * @return
	 */
	@RequestMapping("/thirdfeign/syncReturnBack")
	public ApiFeignResponse<FeignReturnBack> syncReturnBack(
			@RequestParam("cabinetName") String cabinetName, 
			@RequestParam(value = "endTime", required = false) Long endTime);
}