package com.cnbaosi.cabinet.serivce;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.CabinetCriteria;
import com.cnbaosi.cabinet.entity.modal.Cabinet;

/**
 * 柜体相关方法
 * 
 * @author Yifeng Wang  
 */

public interface CabinetService extends IService<Cabinet> {

	/**
	 * 新增柜体
	 * @param cabinet
	 * @return
	 */
	boolean addCabinet(Cabinet cabinet);

	/**
	 * 新增行和格口
	 * @param cabinet
	 * @return
	 */
	boolean addRowsAndCells(Cabinet cabinet);
	
	/**
	 * 删除柜体
	 * @param id
	 * @return
	 */
	String deleteCabinetById(String id);

	/**
	 * 修改柜体
	 * @param cabinet
	 * @return
	 */
	boolean updateCabinet(Cabinet cabinet);
	
	
	/**
	 * 获取柜体列表
	 * @param cabinetCriteria
	 * @return
	 */
	List<Cabinet> getCabinets(CabinetCriteria cabinetCriteria);


}

