package com.cnbaosi.cabinet.serivce;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.modal.CabinetCell;

/**
 * 格口相关方法
 * 
 * @author Yifeng Wang  
 */

public interface CabinetCellService extends IService<CabinetCell> {


	/**
	 * 更新格口信息
	 * @param cellList
	 * @return
	 */
	boolean updateCabinetCells(List<CabinetCell> cellList);

	/**
	 * 更新Pin脚与格口对应信息
	 */
	void updateDefaultCellPin();
}

