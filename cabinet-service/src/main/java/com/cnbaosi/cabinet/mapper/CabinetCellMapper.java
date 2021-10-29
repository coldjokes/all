package com.cnbaosi.cabinet.mapper;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cnbaosi.cabinet.entity.modal.CabinetCell;

/**
 *  格口
 * 
 * @author Yifeng Wang  
 */
public interface CabinetCellMapper extends BaseMapper<CabinetCell> {

	void updateCellPinByName(List<CabinetCell> cellList);

}

