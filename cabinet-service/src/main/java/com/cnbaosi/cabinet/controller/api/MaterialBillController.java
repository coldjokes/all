package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.cnbaosi.cabinet.entity.criteria.MaterialBillCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.DateRangeCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialBill;
import com.cnbaosi.cabinet.serivce.MaterialBillService;

/**
 * 物料流水记录
 * 
 * @author Yifeng Wang
 */

@RestController
@RequestMapping("/api/materialBill")
public class MaterialBillController extends BaseController {

	@Autowired
	private MaterialBillService matBillSvc;

	@LogRecord("新增流水")
	@PostMapping
	public RestFulResponse<MaterialBill> addBill(@RequestBody List<MaterialBill> materialBillList) {
		return actionResult(matBillSvc.addBills(materialBillList));
	}
	
	@LogRecord("查询物料流水列表")
	@GetMapping
	public RestFulResponse<MaterialBill> getMaterialBill(MaterialBillCriteria matBillCriteria, PageCriteria pageCriteria, DateRangeCriteria dateRangeCriteria) {
		Page<MaterialBill> results = this.matBillSvc.getPagedBillRecordList(matBillCriteria,pageCriteria, dateRangeCriteria);
		return success(results.getTotal(), results.getRecords());
	}
	
	@LogRecord("导出物料流水列表")
	@GetMapping("export")
	public void exportMaterialBill(MaterialBillCriteria matBillCriteria, DateRangeCriteria dateRangeCriteria,
			HttpServletRequest request, HttpServletResponse response) {
		matBillSvc.exportMaterialBillRecord(matBillCriteria, dateRangeCriteria, request, response);
	}

}
