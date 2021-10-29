package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.MaterialRemindCriteria;
import com.cnbaosi.cabinet.entity.criteria.MaterialStockCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.SortCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialRemind;
import com.cnbaosi.cabinet.entity.modal.dto.StockDetailDto;
import com.cnbaosi.cabinet.mapper.MaterialStockMapper;
import com.cnbaosi.cabinet.serivce.MaterialRemindService;

/**
 * 
 * 
 * @author Yifeng Wang
 * @date 2021年9月20日
 */

@RestController
@RequestMapping("/api/materialRemind")
public class MaterialRemindController extends BaseController {

	@Autowired
	private MaterialRemindService remindSvc;
	
	@LogRecord("查询物料提醒列表")
	@GetMapping
	public RestFulResponse<MaterialRemind> getMaterials(MaterialRemindCriteria matRemindCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria) {
		Page<MaterialRemind> pagedMaterials = remindSvc.getMaterialRemindPageList(matRemindCriteria, pageCriteria, sortCriteria);
		return success(pagedMaterials.getTotal(), pagedMaterials.getRecords());
	}
	
	@LogRecord("添加入物料提醒列表")
	@PostMapping
	public RestFulResponse<MaterialRemind> addRemind(@RequestBody MaterialRemind materialRedmind) {
		return actionResult(remindSvc.addRemind(materialRedmind));
	}
	
	
	@LogRecord("检查物料是否有库存")
	@GetMapping("/check")
	public RestFulResponse<MaterialRemind> check(String materialId) {
		
		return actionResult(remindSvc.checkIfExisted(materialId));
	}
	
}
