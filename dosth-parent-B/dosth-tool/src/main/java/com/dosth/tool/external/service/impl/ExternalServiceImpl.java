package com.dosth.tool.external.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.util.StringUtil;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.common.util.BorrowPostUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.Manufacturer;
import com.dosth.tool.entity.ManufacturerCustom;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.external.entity.ExternalFeginBorrow;
import com.dosth.tool.external.entity.ExternalMatInfo;
import com.dosth.tool.external.entity.ExternalSupplierInfo;
import com.dosth.tool.external.entity.ExternalSupport;
import com.dosth.tool.external.service.ExternalService;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.ManufacturerCustomRepository;
import com.dosth.tool.repository.ManufacturerRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.repository.MatUseBillRepository;
import com.dosth.tool.repository.MatUseRecordRepository;
import com.dosth.tool.service.EquDetailStaService;

/**
 * 
 * 
 * @author Yifeng Wang
 */
@Service
//@Transactional
public class ExternalServiceImpl implements ExternalService {

	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private MatUseBillRepository useBillRepository;
	@Autowired
	private MatEquInfoRepository matEquInfoRepository;
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	@Autowired
	private ManufacturerCustomRepository customRepository;
	@Autowired
	private MatUseRecordRepository matUseRecordRepository;
	
	@Override
	public List<ExternalMatInfo> getMatInfo() {
		List<ExternalMatInfo> list = new ArrayList<>();

		// 获取所有物料信息
		List<MatEquInfo> infoList = this.matEquInfoRepository.findAll();
		for (MatEquInfo mat : infoList) {
			ExternalMatInfo matInfo = new ExternalMatInfo();
			matInfo.setMatId(mat.getId());
			matInfo.setMatName(mat.getMatEquName());
			matInfo.setMatSpec(mat.getSpec());
			matInfo.setMatBarCode(mat.getBarCode());
			matInfo.setBorrowType(mat.getBorrowType().toString());
			matInfo.setPackNum(mat.getNum());
			matInfo.setBrand(mat.getBrand());
			matInfo.setIcon(mat.getIcon());
			matInfo.setManufacturerId(mat.getManufacturerId());
			matInfo.setStorePrice(mat.getStorePrice());
			list.add(matInfo);
		}
		return list;
	}

	@Override
	public String feeding(String accountId, String equId, String arrs) {
		// 错误信息初始化
		String msg = null;

		// 补料信息CHECK
		String[] arrsList = arrs.split(";");
		for (String arrList : arrsList) {
			String[] arr = arrList.split(",");
			EquDetailSta sta = this.equDetailStaService.get(arr[0]);

			if (arr.length != 2) {// 补料信息格式不正确
				msg = "补料信息有误：" + arrList;
				return msg;
			} else if (sta.getMatInfoId() == null) {// 补料货道为空货道
				msg = "该货道未上架物品：" + sta.getEquDetailId() + "-" + sta.getColNo();
				return msg;
			} else if (sta.getCurNum() + Integer.parseInt(arr[1]) > sta.getMaxReserve()) {// 补料数量超出最大可补数量
				msg = "超过该货道最大库存：" + sta.getEquDetailId() + "-" + sta.getColNo();
				return msg;
			}
		}
		// 补料信息写入库存
		try {
			this.equDetailStaService.feedList(arrs, equId, accountId, FeedType.API);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	public List<ExternalSupplierInfo> getSupplierInfo() {
		List<ExternalSupplierInfo> supplierList = new ArrayList<>();
		List<Manufacturer> manufacturerList = this.manufacturerRepository.findAll();
		for (Manufacturer info : manufacturerList) {
			// 获取售后服务人员信息
			List<ExternalSupport> externalSupportList = new ArrayList<>();
			List<ManufacturerCustom> supportList = this.customRepository.getCustomListByManufacturerId(info.getId(),
					UsingStatus.ENABLE);
			for (ManufacturerCustom support : supportList) {
				ExternalSupport externalSupport = new ExternalSupport(support.getId(), support.getContactName(),
						support.getContactPhone(), support.getMailAddress(), support.getManufacturerId(),
						support.getStatus(), support.getRemark());
				externalSupportList.add(externalSupport);
			}
			ExternalSupplierInfo supplierInfo = new ExternalSupplierInfo(info.getId(), info.getManufacturerName(),
					info.getContact(), info.getAddress(), info.getPhone(), externalSupportList);
			supplierList.add(supplierInfo);
		}
		return supplierList;
	}

	@Override
	public void borrowPost(String recordId) {
		if(StringUtil.isNotBlank(this.toolProperties.getBorrowPostUrl())) {// 推送地址不为空的场合，做推送处理
			List<String> idList = Arrays.asList(recordId);
			List<MatUseRecord> recordList = this.matUseRecordRepository.getRecordList(idList);
			for(MatUseRecord record : recordList) {
				List<MatUseBill> bill = this.useBillRepository.getInfoByRecordId(record.getId());
				EquSetting equSetting = this.equSettingRepository.findOne(bill.get(0).getEquDetailSta().getEquDetail().getEquSettingId());
				ExternalFeginBorrow info = new ExternalFeginBorrow();
				info.setCompanyCode("");
				info.setItemNo(record.getMatInfo().getBarCode());
				info.setOperatorDate(record.getOpDate());
				info.setQtyReal(record.getBorrowNum());
				info.setStockEmp(record.getUserName());
				info.setStockNo(equSetting.getWareHouseAlias());
				info.setWmsNo(record.getId());
				try {
					BorrowPostUtil.putBorrowPostInfo(info);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
