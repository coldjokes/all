package com.cnbaosi.cabinet.serivce;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.MaterialRemindCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.SortCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialRemind;

/**
 * 物料提醒相关方法
 * 
 * @author Yifeng Wang  
 */

public interface MaterialRemindService extends IService<MaterialRemind> {

	/**
	 * 获取列表
	 */
	Page<MaterialRemind> getMaterialRemindPageList(MaterialRemindCriteria matRemindCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria);

	/**
	 * 获取列表
	 */
	List<MaterialRemind> getMaterialRemindList(MaterialRemindCriteria materialRemindCriteria);
	
	/**
	 * 加入提醒列表
	 */
	Boolean addRemind(MaterialRemind materialRedmind);
	
	/**
	 * 从提醒列表中删除
	 */
	boolean removeFromRemindList(String materialId);
	
	/**
	 * 检查此物料是否已有库存
	 * 
	 * result  true:已经存在库存 false:暂无此物料库存
	 */
	boolean checkIfExisted(String materialId);

	/**
	 * 从库存提醒列表中删除
	 */
	void removeIfExist(String materialId);
	
	

}

