package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.AppConsts;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.MaterialStockCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.modal.dto.StockSummaryDto;
import com.cnbaosi.cabinet.serivce.StockService;
import com.cnbaosi.cabinet.util.ExcelUtil;
import com.cnbaosi.cabinet.util.PageUtil;

/**
 * 物料库存
 * 
 * @author Weifeng Li
 */
@RestController
@RequestMapping("/api/materialStock")
public class MaterialStockController extends BaseController {

	@Autowired
	private StockService stockSvc;
	
	@LogRecord("查询物料库存列表")
	@GetMapping
	public RestFulResponse<StockSummaryDto> getMaterialStockDto(MaterialStockCriteria criteria,
			PageCriteria pageCriteria) {
		List<StockSummaryDto> results = stockSvc.getStockSummaryDto(criteria);
		List<StockSummaryDto> pagedResults = PageUtil.page2(results, pageCriteria.getCurPage(), pageCriteria.getPageSize());
		return success(results.size(), pagedResults);
	}
	
	@LogRecord("导出物料库存列表")
	@GetMapping("export")
	public void exportMaterialStock(MaterialStockCriteria matStockCriteria, HttpServletRequest request, HttpServletResponse response) {
		
		HSSFWorkbook workBook =  stockSvc.createMaterialStockExcel(matStockCriteria);
		String filename = ExcelUtil.processFileName(request, AppConsts.EXCEL_NAME);
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		try {
			ServletOutputStream os = response.getOutputStream();
			workBook.write(os); //将excel中的数据写到输出流中，用于文件的输出
	        os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@LogRecord("查询物料库存列表(设备)")
	@GetMapping("/device")
	public RestFulResponse<StockSummaryDto> getStockDto(MaterialStockCriteria criteria) {
		List<StockSummaryDto> results = stockSvc.getStockSummaryDto(criteria);
		return success(results.size(), results);
	}
}
