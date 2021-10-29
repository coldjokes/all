package com.dosth.tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.dto.ApiFeignResponse;
import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.tool.FeignBorrow;
import com.cnbaosi.dto.tool.FeignCabinet;
import com.cnbaosi.dto.tool.FeignFeedingList;
import com.cnbaosi.dto.tool.FeignMat;
import com.cnbaosi.dto.tool.FeignReturnBack;
import com.cnbaosi.dto.tool.FeignStock;
import com.cnbaosi.dto.tool.FeignSupplier;
import com.dosth.common.restful.RestFulController;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.FeedingListService;
import com.dosth.tool.service.ManufacturerService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.tool.service.MatReturnBackService;
import com.dosth.tool.service.MatUseRecordService;

/**
 * @description 第三方远程接口
 * @author guozhidong
 *
 */
@RestController
@RequestMapping("/thirdfeign")
public class ThirdFeignController extends RestFulController{
	@Autowired
	private ManufacturerService manufacturerService;
	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private EquDetailStaService  equDetailStaService;
	@Autowired
	private FeedingListService feedingListService;
	@Autowired
	private MatUseRecordService matUseRecordService;
	@Autowired
	private MatReturnBackService matReturnBackService;
	
	/**
	 * @description 供应商同步
	 * @param supplierList 供应商列表
	 * @return
	 */
	@RequestMapping("/syncSupplier")
	public OpTip syncSupplier(@RequestBody List<FeignSupplier> supplierList) {
		return this.manufacturerService.syncSupplier(supplierList);
	}
	
	/**
	 * @description 物料同步
	 * @param matList 物料列表
	 * @return
	 */
	@RequestMapping("/syncMat")
	public OpTip syncMat(@RequestBody List<FeignMat> matList) {
		return this.matEquInfoService.syncMat(matList);
	}
	
	/**
	 * @description 获取库存汇总（按物料）
	 * @param wareHouseAlias 仓库别称
	 * @return
	 */
	@RequestMapping("/stockDetail")
	public ApiFeignResponse<FeignStock> getStockDetail(@RequestParam("wareHouseAlias") String wareHouseAlias) {
		return this.equDetailStaService.getStockDetail(wareHouseAlias);
	}
	
	/**
	 * @description 获取库存详情
	 * @param
	 * @return
	 */
	@RequestMapping("/staStock")
	public ApiFeignResponse<FeignCabinet> getStaStock() {
		return this.equDetailStaService.getStaStock();
	}

	/**
	 * @description 补料清单
	 * @param supplyList 物料补料单
	 * @return
	 */
	@RequestMapping("/feedList")
	public OpTip supplyList(@RequestBody List<FeignFeedingList> feedingList) {
		return this.feedingListService.feedingList(feedingList);
	}
	
	/**
	 * @description 同步借出记录
	 * @param 
	 * @return
	 */
	@RequestMapping("/syncBorrows")
	public ApiFeignResponse<FeignBorrow> syncBorrows(
			@RequestParam("cabinetName") String cabinetName, 
			@RequestParam(value = "endTime", required = false ) Long endTime) {
		return this.matUseRecordService.getSyncBorrows(cabinetName, endTime);
	}
	
	/**
	 * @description 同步归还记录
	 * @param
	 * @return
	 */
	@RequestMapping("/syncReturnBack")
	public ApiFeignResponse<FeignReturnBack> syncReturnBack(
			@RequestParam("cabinetName") String cabinetName, 
			@RequestParam(value = "endTime", required = false) Long endTime) {
		return this.matReturnBackService.getSyncReturnBack(cabinetName, endTime);
	}
}