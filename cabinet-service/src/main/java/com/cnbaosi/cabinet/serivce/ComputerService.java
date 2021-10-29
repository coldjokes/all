package com.cnbaosi.cabinet.serivce;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.ComputerCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.modal.Computer;

/**
 * 主机相关方法
 * 
 * @author Yifeng Wang  
 */

public interface ComputerService extends IService<Computer> {


	/**
	 * 新增主机
	 * @param computer
	 * @return
	 */
	boolean addComputer(Computer computer);
	

	/**
	 * 删除主机
	 * @param id
	 * @return
	 */
	String deleteComputer(String id);

	/**
	 * 更新主机
	 * @param computer
	 * @return
	 */
	boolean updateComputer(Computer computer);
	
	/**
	 * 获取主机列表
	 * @param pageCriteria 
	 * @param computerCriteria 
	 * @return
	 */
	Page<Computer> getComputers(ComputerCriteria computerCriteria, PageCriteria pageCriteria);
	
	/**
	 * 通过唯一识别码获取主机
	 * @param identifyCode
	 * @return
	 */
	Computer getComputerByIdentifyCode(String identifyCode);
	
	/**
	 * 通过id获取主机
	 * @param id
	 * @return
	 */
	Computer getComputerById(String id);


	Computer getComputerByName(String name);


}

