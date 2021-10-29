package com.cnbaosi.cabinet.serivce.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.criteria.MaterialBillCriteria;
import com.cnbaosi.cabinet.entity.criteria.StockCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.DateRangeCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.enums.MaterialStockEnum;
import com.cnbaosi.cabinet.entity.modal.Material;
import com.cnbaosi.cabinet.entity.modal.MaterialBill;
import com.cnbaosi.cabinet.entity.modal.Stock;
import com.cnbaosi.cabinet.mapper.MaterialBillMapper;
import com.cnbaosi.cabinet.serivce.MaterialBillService;
import com.cnbaosi.cabinet.serivce.MaterialRemindService;
import com.cnbaosi.cabinet.serivce.MaterialService;
import com.cnbaosi.cabinet.serivce.StockService;
import com.cnbaosi.cabinet.util.DateTimeUtil;
import com.cnbaosi.cabinet.util.ExcelUtil;
import com.google.common.collect.Lists;

/**
 * 物料流水实现类
 * 
 * @author Yifeng Wang
 */
@Service
public class MaterialBillServiceImpl extends ServiceImpl<MaterialBillMapper, MaterialBill> implements MaterialBillService {
	@Autowired
	private StockService stockSvc;
	@Autowired
	private MaterialService materialSvc;
	@Autowired
	private MaterialRemindService remindSvc;
	
	@Override
	public boolean addBills(List<MaterialBill>  materialBillList) {
		
		// 此次取、存的物料。因为柜子上都是单次操作，因此只可能有一种物料，并且是一种操作。
		String needCheckMaterialId = null;
		
		if(CollectionUtils.isNotEmpty(materialBillList)) {
			Date now = DateTimeUtil.now();
			for(MaterialBill materialBill : materialBillList) {
				
				// 1. 增加流水记录
				String materialId = materialBill.getMaterialId();
				
				if(needCheckMaterialId == null) {
					needCheckMaterialId = materialId;	
				}
				
				String materialNo = materialBill.getMaterialNo();
				String materialSpec = materialBill.getMaterialSpec();
				String materialPicture = materialBill.getMaterialPicture();
				String materialRemark = materialBill.getMaterialRemark();
				String stockId = materialBill.getStockId();
				Integer amountStart = materialBill.getAmountStart();
				Integer amountDiff = materialBill.getAmountDiff();
				Integer amountEnd = materialBill.getAmountEnd();
				String computerId = materialBill.getComputerId();
				String computerName = materialBill.getComputerName();
				String cabinetId = materialBill.getCabinetId();
				String cabinetName = materialBill.getCabinetName();
				String cellId = materialBill.getCellId();
				String cellName = materialBill.getCellName();
				String userId = materialBill.getUserId();
				String userFullname = materialBill.getUserFullname();
				
				MaterialStockEnum operateTypeEnum = null;
				if (amountDiff > 0) { // 存
					operateTypeEnum = MaterialStockEnum.ADD;
				} else { // 领
					operateTypeEnum = MaterialStockEnum.DELETE;
				}

				materialBill.setOperateType(operateTypeEnum.getCode());
				materialBill.setOperateName(operateTypeEnum.getText());
				materialBill.setCreateTime(now);
				super.insert(materialBill);

				// 2. 更新库存表记录
				if (operateTypeEnum.equals(MaterialStockEnum.ADD)) { //存入
					
					//获取物料有无已存在的库位
					StockCriteria stockCriteria = new StockCriteria();
					stockCriteria.setCabinetCellId(cellId);
					stockCriteria.setMaterialId(materialId);
					List<Stock> dbStockList = stockSvc.getStockList(stockCriteria);
					
					if(CollectionUtils.isNotEmpty(dbStockList)) { //更新原来库位数量
						Stock stock = dbStockList.get(0);
						stock.setAmount(amountEnd);
						stock.setUpdateTime(now);
						stockSvc.updateStock(stock);
					} else { //新增库存库位
						Stock stock = new Stock();
						stock.setComputerId(computerId);
						stock.setCabinetId(cabinetId);
						stock.setCabinetCellId(cellId);
						stock.setMaterialId(materialId);
						stock.setAmount(amountEnd);
						stock.setCreateTime(now);
						stock.setUpdateTime(now);
						stockSvc.addStock(stock);
					}
					
					//检查库存提醒列表中是否有此物料，如果有则直接去掉
					remindSvc.removeIfExist(materialId);
					
				} else if (operateTypeEnum.equals(MaterialStockEnum.DELETE)) { //领取
					if(amountEnd == 0) { //领完的话 ，直接删除此库存信息
						stockSvc.deleteById(stockId);
					} else { //更新库存
						Stock stock = new Stock();
						stock.setId(stockId);
						stock.setAmount(amountEnd);
						stockSvc.updateStock(stock);
					}
				}
				
				//3. 更新物料最后操作状态
				Material material = new Material();
				material.setId(materialId);
				material.setLastStockOperateUserId(userId);
				material.setLastStockOperateUserFullname(userFullname);
				material.setLastStockOperateTime(now);
				materialSvc.updateMaterial(material);
			}
			
			//检查此次取料的库存是否达到预警值，并发送邮件
			final String maId = needCheckMaterialId;
			new Thread(new Runnable() {
				@Override
				public void run() {
					if(StringUtils.isNotBlank(maId)) {
						stockSvc.checkStockIfNeedEmail(maId);
					}
				}
			}).start();
		}
		return true;
	}

	@Override
	public void exportMaterialBillRecord(MaterialBillCriteria matBillCriteria, DateRangeCriteria dateRangeCriteria,
			HttpServletRequest request, HttpServletResponse response) {
		String sheetName = "物料存取记录";
		String[] header = { "序号", "物料编码", "刀片型号", "图纸编号", "操作人", "操作类型", "操作前数量", "操作数量", "操作后数量", "设备名", "方格名", "操作时间"};

		List<MaterialBill> results = this.getBillRecordList(matBillCriteria, dateRangeCriteria);
		List<List<String>> fieldDataList = new ArrayList<>();

		for (int i = 0; i < results.size(); i++) {
			List<String> rowValue = Lists.newArrayList();
			MaterialBill materialBill = results.get(i);

			rowValue.add(i + 1 + "");
			rowValue.add(materialBill.getMaterialName());
			rowValue.add(materialBill.getMaterialNo());
			rowValue.add(materialBill.getMaterialSpec());
			rowValue.add(materialBill.getUserFullname());
			rowValue.add(materialBill.getOperateName());
			rowValue.add(materialBill.getAmountStart() + "");
			rowValue.add(Math.abs(materialBill.getAmountDiff()) + "");
			rowValue.add(materialBill.getAmountEnd() + "");
			rowValue.add(materialBill.getCabinetName());
			rowValue.add(materialBill.getCellName());
			
			if(materialBill.getCreateTime() != null) {
        		rowValue.add(DateTimeUtil.format(DateTimeUtil.FMT_YMDHM, materialBill.getCreateTime()));
        	} else {
        		rowValue.add("");
        	}

			fieldDataList.add(rowValue);
		}

		List<String> sheetNames = Lists.newArrayList(sheetName);
		Map<String, List<String>> fieldNames = new HashMap<>();
		Map<String, List<List<String>>> fieldDataMap = new HashMap<>();

		fieldNames.put(sheetName, Arrays.asList(header));
		fieldDataMap.put(sheetName, fieldDataList);

		ExcelUtil exportUtil = new ExcelUtil(fieldNames, fieldDataMap);
		exportUtil.createWorkbook(sheetNames);
		String filename = ExcelUtil.processFileName(request, "物料存取记录.xls");
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		try {
			exportUtil.expordExcel(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Page<MaterialBill> getPagedBillRecordList(MaterialBillCriteria matBillCriteria, PageCriteria pageCriteria, DateRangeCriteria dateRangeCriteria) {
		Integer curPage = pageCriteria.getCurPage();
		Integer pageSize = pageCriteria.getPageSize();
		EntityWrapper<MaterialBill> wrapper = this.buildMaterialBillWrapper(matBillCriteria, dateRangeCriteria);
		Page<MaterialBill> page = new Page<>(curPage, pageSize);
		return selectPage(page, wrapper);
	}

	private EntityWrapper<MaterialBill> buildMaterialBillWrapper(MaterialBillCriteria matBillCriteria, DateRangeCriteria dateRangeCriteria){
		String text = matBillCriteria.getText();
		String fullname = matBillCriteria.getFullname();
		String cabinetId = matBillCriteria.getCabinetId();
		Integer operateType = matBillCriteria.getOperateType();
		
		Long startTime = dateRangeCriteria.getStartTime();
		Long endTime = dateRangeCriteria.getEndTime();
		
		EntityWrapper<MaterialBill> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(text)) { //多字段匹配查找
			text = text.trim();
			wrapper.like("material_name", text).or().like("material_no", text).or().like("material_spec", text);
		}
		wrapper.andNew();
		
		if(StringUtils.isNotBlank(fullname)) {
			wrapper.like("user_fullname", fullname);
		}
		
		if(StringUtils.isNotBlank(cabinetId)) {
			wrapper.like("cabinet_id", cabinetId);
		}
		
		if(operateType != null) {
			wrapper.eq("operate_type", operateType);
		}
		
		if(startTime != null && endTime != null) {
			wrapper.ge("create_time", new Date(startTime)).le("create_time", new Date(endTime));
		}
		wrapper.orderBy("create_time", false);
		return wrapper;
	}

	@Override
	public List<MaterialBill> getBillRecordList(MaterialBillCriteria matBillCriteria, DateRangeCriteria dateRangeCriteria) {
		EntityWrapper<MaterialBill> wrapper = this.buildMaterialBillWrapper(matBillCriteria, dateRangeCriteria);
		return super.selectList(wrapper);
	}
}
