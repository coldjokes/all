package com.cnbaosi.cabinet.serivce;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialCategory;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;

/**
 * 
 * 
 * @author Yifeng Wang  
 */

public interface MaterialCategoryService extends IService<MaterialCategory> {
	
	
	/**
	 * 新增类别
	 * @param category
	 * @return
	 */
	Boolean addCategory(MaterialCategory category);
	
	/**
	 * 删除类别
	 * @param id
	 * @return
	 */
	String deleteCategory(String id);
	
	/**
	 * 修改类别
	 * @param category
	 * @return
	 */
	Boolean updateCategory(MaterialCategory category);

	/**
	 * 获取类别列表
	 * @param categoryCriteria
	 * @return
	 */
	List<MaterialCategory> getMaterialCategory(MaterialCategoryCriteria categoryCriteria);

	/**
	 * 获取类别树
	 * @return
	 */
	List<TreeNodes> getMaterialCategoryTree();
	/**
	 * 获取类别子节点
	 * @return
	 */
	List<TreeNodes> getChildrenNode(String id, List<MaterialCategory> source);

	
	List<MaterialCategory> getAllSonCategory(String id);
}

