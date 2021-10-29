package com.cnbaosi.cabinet.serivce;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.MaterialStockCriteria;
import com.cnbaosi.cabinet.entity.criteria.StockCriteria;
import com.cnbaosi.cabinet.entity.modal.Stock;
import com.cnbaosi.cabinet.entity.modal.dto.StockSummaryDto;

public interface StockService extends IService<Stock> {

	boolean addStock(Stock stock);
	
	boolean updateStock(Stock stock);
	
	boolean deleteStock(Stock stock);

	void checkStockIfNeedEmail(String materialId);
	
	List<StockSummaryDto> getStockSummaryDto(MaterialStockCriteria criteria);
	
	HSSFWorkbook createMaterialStockExcel(MaterialStockCriteria matStockCriteria);
	
	List<Stock> getStockList(StockCriteria stockCriteria);
}
