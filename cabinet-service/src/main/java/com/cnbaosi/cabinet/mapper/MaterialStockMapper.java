package com.cnbaosi.cabinet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cnbaosi.cabinet.entity.criteria.MaterialStockCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialStock;
import com.cnbaosi.cabinet.entity.modal.dto.StockDetailDto;

/**
 * 库存
 * 
 * @author Yifeng Wang
 */
public interface MaterialStockMapper extends BaseMapper<MaterialStock> {

	/**
	 * 获取设备上的物料库存信息
	 */
	List<StockDetailDto> getStockDetailDto(@Param("criteria") MaterialStockCriteria criteria);

}
