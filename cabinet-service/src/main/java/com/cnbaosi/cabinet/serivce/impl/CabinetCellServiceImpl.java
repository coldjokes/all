package com.cnbaosi.cabinet.serivce.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.modal.CabinetCell;
import com.cnbaosi.cabinet.mapper.CabinetCellMapper;
import com.cnbaosi.cabinet.serivce.CabinetCellService;
import com.google.common.collect.Lists;

/**
 * 格口方法实现类
 * 
 * @author Yifeng Wang  
 */
@Service
public class CabinetCellServiceImpl extends ServiceImpl<CabinetCellMapper, CabinetCell> implements CabinetCellService{
	
	@Autowired
	private CabinetCellMapper cellMapper;
	
	@Override
	public boolean updateCabinetCells(List<CabinetCell> cellList) {

		return super.updateBatchById(cellList, cellList.size());
	}

	@Override
	public void updateDefaultCellPin() {
		List<CabinetCell> cellList = Lists.newArrayList();
		cellList.add(new CabinetCell("方格1", 1));
		cellList.add(new CabinetCell("方格2", 6));
		cellList.add(new CabinetCell("方格3", 31));
		cellList.add(new CabinetCell("方格4", 26));
		cellList.add(new CabinetCell("方格5", 2));
		cellList.add(new CabinetCell("方格6", 7));
		cellList.add(new CabinetCell("方格7", 32));
		cellList.add(new CabinetCell("方格8", 27));
		cellList.add(new CabinetCell("方格9", 3));
		cellList.add(new CabinetCell("方格10", 8));
		cellList.add(new CabinetCell("方格11", 33));
		cellList.add(new CabinetCell("方格12", 28));
		cellList.add(new CabinetCell("方格13", 4));
		cellList.add(new CabinetCell("方格14", 9));
		cellList.add(new CabinetCell("方格15", 34));
		cellList.add(new CabinetCell("方格16", 29));
		cellList.add(new CabinetCell("方格17", 5));
		cellList.add(new CabinetCell("方格18", 10));
		cellList.add(new CabinetCell("方格19", 35));
		cellList.add(new CabinetCell("方格20", 30));
		cellList.add(new CabinetCell("方格21", 36));
		cellList.add(new CabinetCell("方格22", 41));
		cellList.add(new CabinetCell("方格23", 16));
		cellList.add(new CabinetCell("方格24", 11));
		cellList.add(new CabinetCell("方格25", 37));
		cellList.add(new CabinetCell("方格26", 42));
		cellList.add(new CabinetCell("方格27", 17));
		cellList.add(new CabinetCell("方格28", 12));
		cellList.add(new CabinetCell("方格29", 38));
		cellList.add(new CabinetCell("方格30", 43));
		cellList.add(new CabinetCell("方格31", 18));
		cellList.add(new CabinetCell("方格32", 13));
		cellList.add(new CabinetCell("方格33", 39));
		cellList.add(new CabinetCell("方格34", 44));
		cellList.add(new CabinetCell("方格35", 19));
		cellList.add(new CabinetCell("方格36", 14));
		cellList.add(new CabinetCell("方格37", 40));
		cellList.add(new CabinetCell("方格38", 45));
		cellList.add(new CabinetCell("方格39", 20));
		cellList.add(new CabinetCell("方格40", 15));
		
		cellMapper.updateCellPinByName(cellList);
	}

}

