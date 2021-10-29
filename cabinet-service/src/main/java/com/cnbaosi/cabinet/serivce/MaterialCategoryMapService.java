package com.cnbaosi.cabinet.serivce;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryMapCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialCategoryMap;

/**
 * 物料类别映射方法
 * 
 * @author Yifeng Wang  
 */

public interface MaterialCategoryMapService extends IService<MaterialCategoryMap> {

	/**
	 * 更新物料类别关系
	 * @param categoryMapCriteria
	 * @return
	 */
	String udpateCategoryMap(MaterialCategoryMapCriteria categoryMapCriteria);
	
	/**
	 * 批量更新映射关系
	 * @param maps
	 * @return
	 */
	boolean addMaterialCategoryMaps(List<MaterialCategoryMap> maps);
	
	/**
	 * 获取物料类别
	 * @param categoryMapCriteria
	 * @return
	 */
	List<MaterialCategoryMap> getMapList(MaterialCategoryMapCriteria categoryMapCriteria);


}

