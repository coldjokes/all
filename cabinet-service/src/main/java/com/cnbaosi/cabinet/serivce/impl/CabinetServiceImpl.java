package com.cnbaosi.cabinet.serivce.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.criteria.CabinetCriteria;
import com.cnbaosi.cabinet.entity.criteria.StockCriteria;
import com.cnbaosi.cabinet.entity.modal.Cabinet;
import com.cnbaosi.cabinet.entity.modal.CabinetCell;
import com.cnbaosi.cabinet.entity.modal.CabinetRow;
import com.cnbaosi.cabinet.entity.modal.Computer;
import com.cnbaosi.cabinet.entity.modal.Stock;
import com.cnbaosi.cabinet.mapper.CabinetMapper;
import com.cnbaosi.cabinet.serivce.CabinetCellService;
import com.cnbaosi.cabinet.serivce.CabinetRowService;
import com.cnbaosi.cabinet.serivce.CabinetService;
import com.cnbaosi.cabinet.serivce.ComputerService;
import com.cnbaosi.cabinet.serivce.ShiroService;
import com.cnbaosi.cabinet.serivce.StockService;
import com.cnbaosi.cabinet.util.DateTimeUtil;

/**
 * 柜体方法实现类
 * 
 * @author Yifeng Wang  
 */
@Service
public class CabinetServiceImpl extends ServiceImpl<CabinetMapper, Cabinet> implements CabinetService{

	@Autowired
	private ShiroService shiroSvc;
	@Autowired
	private CabinetRowService cabinetRowSvc;
	@Autowired
	private CabinetCellService cabinetCellSvc;
	@Autowired
	private ComputerService computerSvc;
	@Autowired
	private StockService stockSvc;

	@Override
	public boolean addCabinet(Cabinet cabinet) {
		//1.添加柜体
		boolean insertCabinetResult = super.insert(cabinet);
		//2.添加行和格口
		boolean inserRowsAndCells = this.addRowsAndCells(cabinet); 
		return insertCabinetResult && inserRowsAndCells;
	}

	@Override
	public boolean addRowsAndCells(Cabinet cabinet) {
		String cabinetId = cabinet.getId();
		Computer computer = computerSvc.getComputerById(cabinet.getComputerId());
		String cellPrefix = "方格";
		if(computer != null) {
			cellPrefix = computer.getCellPrefix();
		}
		List<CabinetRow> rowList = cabinet.getRows();
		if(CollectionUtils.isNotEmpty(rowList)) {
			
			int nameSuffix = 1;
			int stack = 0;
			int pin = 1;
					
			for(int i = 0; i < rowList.size() ; i ++) {
				CabinetRow row = new CabinetRow();
				int rowNum = i + 1;
				row.setCabinetId(cabinetId);
				row.setName("行" + rowNum);
				row.setSort(rowNum);
				
				//添加行
				cabinetRowSvc.insert(row); 
				
				//添加格口
				String rowId = row.getId();
				List<CabinetCell> cellList = rowList.get(i).getCells();
				if(CollectionUtils.isNotEmpty(cellList)) {
					for(int j = 0; j < cellList.size(); j ++) {
						
						//格口名称
						String cellName = cellPrefix;
						if(nameSuffix < 10) {
							cellName = cellName + "00" + nameSuffix;
						} else if(nameSuffix < 100){
							cellName = cellName + "0" + nameSuffix;
						} else {
							cellName = cellName + nameSuffix;
						}
						
						//目前一块板子只用50个针脚，如果超过50，说明已经是第二块板子了。所以板子栈号+1，针脚重新计算
						if(pin > 50) {
							stack ++;
							pin = 1;
						}
						
						CabinetCell cell = cellList.get(j);
						cell.setCabinetId(cabinetId);
						cell.setRowId(rowId);
						cell.setName(cellName);
						cell.setSort(nameSuffix);
						cell.setPin(pin);
						cell.setStack(stack);
						
						nameSuffix ++;
						pin ++;
						
					}
					cabinetCellSvc.insertBatch(cellList, cellList.size()); 
				}
			}
		}
		
//		cabinetCellSvc.updateDefaultCellPin();
		
		return true;
	}
	
	@Override
	public String deleteCabinetById(String id) {
		String msg = null;

		StockCriteria stockCriteria = new StockCriteria();
		stockCriteria.setCabinetId(id);
		List<Stock> stockList = stockSvc.getStockList(stockCriteria);
		if(CollectionUtils.isEmpty(stockList)) {
			Cabinet cabinet = new Cabinet(id);
			cabinet.setDeleteTime(DateTimeUtil.now());
			boolean deleteCabinet = super.updateById(cabinet); 
			//删除已绑定行
			EntityWrapper<CabinetRow> rowWrapper = new EntityWrapper<>();
			rowWrapper.eq("cabinet_id", id);
			boolean deleteRows = cabinetRowSvc.delete(rowWrapper);
			//删除已绑定格口
			EntityWrapper<CabinetCell> cellWrapper = new EntityWrapper<>();
			cellWrapper.eq("cabinet_id", id);
			boolean deleteCells = cabinetCellSvc.delete(cellWrapper);
			
		} else {
			msg = "所选设备下存在物料，请先取出后再删除！";
		}
		return msg;
	}

	@Override
	public boolean updateCabinet(Cabinet cabinet) {
		cabinet.setUpdateTime(DateTimeUtil.now());
		
		//更新柜体信息
		boolean updateCabinet = super.updateById(cabinet);
		
		String cabinetId = cabinet.getId();
		
		//删除已绑定行
		EntityWrapper<CabinetRow> rowWrapper = new EntityWrapper<>();
		rowWrapper.eq("cabinet_id", cabinetId);
		boolean deleteRows = cabinetRowSvc.delete(rowWrapper);
		//删除已绑定格口
		EntityWrapper<CabinetCell> cellWrapper = new EntityWrapper<>();
		cellWrapper.eq("cabinet_id", cabinetId);
		boolean deleteCells = cabinetCellSvc.delete(cellWrapper);
		
		//添加行和格口
		boolean inserRowsAndCells = this.addRowsAndCells(cabinet); 
		
		return updateCabinet && deleteRows && deleteCells && inserRowsAndCells;
	}
	
	@Override
	public List<Cabinet> getCabinets(CabinetCriteria cabinetCriteria) {
		String computerId = cabinetCriteria.getComputerId();
		String name = cabinetCriteria.getName();
		
		//TODO 可能为bug
		//从页面上传值 or shiro(app)传值
//		if(StringUtils.isBlank(computerId)) {
//			computerId = shiroSvc.getComputerId();
//		}
		
		EntityWrapper<Cabinet> cabinetWrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(computerId)) {
			cabinetWrapper.eq("computer_id", computerId);
		}
		if(StringUtils.isNotBlank(name)) {
			name = name.trim();
			cabinetWrapper.like("name", name);
		}
//		cabinetWrapper.orderBy("computer_id");
		cabinetWrapper.orderBy("create_time");
		
		cabinetWrapper.isNull("delete_time");
		List<Cabinet> cabinetList = super.selectList(cabinetWrapper);
		
		//获取行
		if(CollectionUtils.isNotEmpty(cabinetList)) {
			
			for(Cabinet cabinet : cabinetList) {
				String cabinetId = cabinet.getId();
				
				EntityWrapper<CabinetRow> cabinetRowWrapper = new EntityWrapper<>();
				cabinetRowWrapper.eq("cabinet_id", cabinetId);
				cabinetRowWrapper.orderBy("sort");				
				List<CabinetRow> cabinetRowList = cabinetRowSvc.selectList(cabinetRowWrapper);
				
				//获取格口
				if(CollectionUtils.isNotEmpty(cabinetRowList)) {
					for(CabinetRow row : cabinetRowList) {
						String rowId = row.getId();
						
						EntityWrapper<CabinetCell> cabinetCellWrapper = new EntityWrapper<>();
						cabinetCellWrapper.eq("cabinet_id", cabinetId);
						cabinetCellWrapper.eq("row_id", rowId);
						cabinetCellWrapper.orderBy("sort");	
						List<CabinetCell> cabinetCellList = cabinetCellSvc.selectList(cabinetCellWrapper);
						row.setCells(cabinetCellList);
					}
				}
				cabinet.setRows(cabinetRowList);
			}
		}
		
		return cabinetList;
	}



}

