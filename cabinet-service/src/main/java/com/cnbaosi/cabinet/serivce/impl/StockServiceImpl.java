package com.cnbaosi.cabinet.serivce.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.config.CabinetServiceConfig;
import com.cnbaosi.cabinet.entity.AppConsts;
import com.cnbaosi.cabinet.entity.criteria.MaterialStockCriteria;
import com.cnbaosi.cabinet.entity.criteria.StockCriteria;
import com.cnbaosi.cabinet.entity.modal.Stock;
import com.cnbaosi.cabinet.entity.modal.dto.StockDetailDto;
import com.cnbaosi.cabinet.entity.modal.dto.StockSummaryDto;
import com.cnbaosi.cabinet.mapper.MaterialStockMapper;
import com.cnbaosi.cabinet.mapper.StockMapper;
import com.cnbaosi.cabinet.serivce.StockService;
import com.cnbaosi.cabinet.serivce.UserService;
import com.cnbaosi.cabinet.util.DateTimeUtil;
import com.cnbaosi.cabinet.util.EmailUtil;
import com.cnbaosi.cabinet.util.ExcelUtil;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
	
	@Autowired
	private UserService userSvc;
	@Autowired
	private MaterialStockMapper materialStockMapper;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private CabinetServiceConfig cabinetConfig;
	
	@Override
	public boolean addStock(Stock stock) {
		return super.insert(stock);
	}

	@Override
	public boolean updateStock(Stock stock) {
		return super.updateById(stock);
	}

	@Override
	public boolean deleteStock(Stock stock) {
		return super.deleteById(stock);
	}

	@Override
	public void checkStockIfNeedEmail(String materialId) {
		MaterialStockCriteria criteria = new MaterialStockCriteria();
		criteria.setMaterialId(materialId);
		//1. 检查库存
		List<StockSummaryDto> results = this.getStockSummaryDto(criteria);
		if(CollectionUtils.isNotEmpty(results)) {
			StockSummaryDto materialStock = results.get(0);
			String materialName = materialStock.getMaterialName();
			String materialNo = materialStock.getMaterialNo();
			String materialSpec = materialStock.getMaterialSpec();
			Integer totalAmount = materialStock.getTotalAmount();
			Integer warnVal = materialStock.getMaterialWarnVal();
			
			//确定需要发送邮件
			if(totalAmount != null && warnVal != null && totalAmount <= warnVal) {
				
				String emailSubject = "库存预警通知";
				
				String emailContent = 
						"<p>尊敬的用户：<br/><br/>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;工具柜中 <b>" + materialName + "</b>（编号："+ materialNo +" 规格：" + materialSpec + "）库存已不足。"
										+ "当前库存为 <b>" + totalAmount + "</b> ，预警库存为 <b>" + warnVal + "</b> ，请及时安排补料。<br/><br/>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;附件为库存清单，请查收。"
								+ "</p>";
				
				
				//2. 获取需要发送的邮箱
				List<String> emailList = userSvc.getEmailList();
				
				String sendResult = "";
				if(CollectionUtils.isNotEmpty(emailList)) {
					
					//3. 生成库存文件
					String filePath = this.getMaterialStockExcelPath();
					
					//4. 发送邮件
					try {
						emailUtil.sendEmail(emailSubject, emailContent, emailList, AppConsts.EXCEL_NAME, filePath);
					} catch (MessagingException e) {
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} else {
					sendResult = "无收件人邮箱";
				}
				
				//5. 邮件历史记录  TODO
			}
		}
	}
	
	@Override
	public HSSFWorkbook createMaterialStockExcel(MaterialStockCriteria matStockCriteria) {
		String sheetName = "物料库存";
		String[] header = {"序号", "物料编码", "刀片型号", "图纸编号","备注", "库存预警值", "库存", "存储位置"};
		
		List<StockSummaryDto> results = this.getStockSummaryDto(matStockCriteria);
		
		List<List<String>> fieldDataList = new ArrayList<>();
		
		for (int i = 0; i < results.size(); i ++) {
			
			List<String> rowValue = Lists.newArrayList();
			StockSummaryDto materialStock = results.get(i);
			
			rowValue.add(i + 1 + "");
			rowValue.add(materialStock.getMaterialName());
			rowValue.add(materialStock.getMaterialNo());
			rowValue.add(materialStock.getMaterialSpec());
			rowValue.add(materialStock.getMaterialRemark());
			rowValue.add(materialStock.getMaterialWarnVal() + "");
			rowValue.add(materialStock.getTotalAmount() + "");
			
			List<StockDetailDto> stockDetailList = materialStock.getStockDetailList();
			if(CollectionUtils.isNotEmpty(stockDetailList)) {
				String location = "";
				for (int j = 0; j < stockDetailList.size(); j++) {
					StockDetailDto detailDto = stockDetailList.get(j);
					if(detailDto == null || detailDto.getAmount() == null || detailDto.getAmount() == 0) {
						continue;
					} else {
						location = location + detailDto.getCabinetName() + ":" + detailDto.getCellName() + ":" + detailDto.getAmount();
						if(j < (stockDetailList.size() - 1)){
							location += "\r\n";
						}
					}
				}
				
				if(StringUtil.isNotEmpty(location)) {
					rowValue.add(location);
				} else {
					rowValue.add(0 + "");
				}
			} else {
				rowValue.add(0 + "");
			}
			
			fieldDataList.add(rowValue);
		}
		
		List<String> sheetNames = Lists.newArrayList(sheetName);
		Map<String, List<String>> fieldNames = new HashMap<>();
		Map<String, List<List<String>>> fieldDataMap = new HashMap<>();
		
		fieldNames.put(sheetName, Arrays.asList(header));
		fieldDataMap.put(sheetName, fieldDataList);
		
		ExcelUtil exportUtil = new ExcelUtil(fieldNames, fieldDataMap);
		return exportUtil.createWorkbook(sheetNames);
	}

	public String getMaterialStockExcelPath() {
		MaterialStockCriteria matStockCriteria = new MaterialStockCriteria();
		HSSFWorkbook workBook = this.createMaterialStockExcel(matStockCriteria);
		String path = cabinetConfig.getProjectLocation() + cabinetConfig.getStockPath();
		
		File targetFile = new File(path);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		
		String name = DateTimeUtil.format(DateTimeUtil.FMT_YMDHMSSSS, new Date()) + ".xls";
		String fileTruePath = path + name;
//		String fileSavedPath = cabinetConfig.getStockPath() + "/" + name;
		File file = new File(fileTruePath);
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			workBook.write(os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileTruePath;
	}
	
	@Override
	public List<StockSummaryDto> getStockSummaryDto(MaterialStockCriteria criteria) {
		
		List<StockSummaryDto> summaryDtoList = Lists.newArrayList();

		//根据物料分组
		List<StockDetailDto> detailDtoList = materialStockMapper.getStockDetailDto(criteria);
		if(CollectionUtils.isNotEmpty(detailDtoList)) {
			Map<String, List<StockDetailDto>> map = detailDtoList.stream().collect(Collectors.groupingBy(StockDetailDto::getMaterialId));
			for (Map.Entry<String, List<StockDetailDto>> entry : map.entrySet()) {
				
				List<StockDetailDto> detailList = entry.getValue();
				StockDetailDto singleDetailDto = detailList.get(0);
				
				StockSummaryDto summaryDto = new StockSummaryDto();
				summaryDto.setMaterialId(singleDetailDto.getMaterialId());
				summaryDto.setMaterialName(singleDetailDto.getMaterialName());
				summaryDto.setMaterialNo(singleDetailDto.getMaterialNo());
				summaryDto.setMaterialSpec(singleDetailDto.getMaterialSpec());
				summaryDto.setMaterialPicture(singleDetailDto.getMaterialPicture());
				summaryDto.setMaterialRemark(singleDetailDto.getMaterialRemark());
				summaryDto.setMaterialWarnVal(singleDetailDto.getMaterialWarnVal());
				summaryDto.setLastStockOperateUserId(singleDetailDto.getLastStockOperateUserId());
				summaryDto.setLastStockOperateUserFullname(singleDetailDto.getLastStockOperateUserFullname());
				summaryDto.setLastStockOperateTime(singleDetailDto.getLastStockOperateTime());
				
				Integer totalAmount = 0;
				for(StockDetailDto dto : detailList) {
					if(dto.getAmount() != null) {
						totalAmount += dto.getAmount();
					}
				}
				summaryDto.setTotalAmount(totalAmount);
				summaryDto.setStockDetailList(detailList);
				summaryDtoList.add(summaryDto);
			}
		}
		
		return summaryDtoList;
	}

	@Override
	public List<Stock> getStockList(StockCriteria stockCriteria) {
		String cabinetId = stockCriteria.getCabinetId();
		String cabinetCellId = stockCriteria.getCabinetCellId();
		String materialId = stockCriteria.getMaterialId();
		
		EntityWrapper<Stock> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(cabinetId)) {
			wrapper.like("cabinet_id", cabinetId);
		}
		if(StringUtils.isNotBlank(cabinetCellId)) {
			wrapper.like("cabinet_cell_id", cabinetCellId);
		}
		if(StringUtils.isNotBlank(materialId)) {
			wrapper.like("material_id", materialId);
		}
		return super.selectList(wrapper);
	}
}
