package com.cnbaosi.cabinet.serivce.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.criteria.CabinetCriteria;
import com.cnbaosi.cabinet.entity.criteria.ComputerCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.modal.Cabinet;
import com.cnbaosi.cabinet.entity.modal.Computer;
import com.cnbaosi.cabinet.mapper.ComputerMapper;
import com.cnbaosi.cabinet.serivce.CabinetService;
import com.cnbaosi.cabinet.serivce.ComputerService;
import com.cnbaosi.cabinet.util.DateTimeUtil;

/**
 * 主机方法实现类
 * 
 * @author Yifeng Wang  
 */
@Service
public class ComputerServiceImpl extends ServiceImpl<ComputerMapper, Computer> implements ComputerService{

	@Autowired
	private CabinetService cabinetSvc;
	
	@Override
	public boolean addComputer(Computer computer) {
		return super.insert(computer);
	}

	@Override
	public String deleteComputer(String id) {
		Computer computer = new Computer(id);
		
		String msg = null;
		CabinetCriteria cabinetCriteria = new CabinetCriteria();
		cabinetCriteria.setComputerId(id);
		List<Cabinet> cabinetList = cabinetSvc.getCabinets(cabinetCriteria);
		
		if(CollectionUtils.isEmpty(cabinetList)) {
			
			computer.setDeleteTime(DateTimeUtil.now());
			super.updateById(computer);
		} else {
			msg = "主机已绑定设备，请删除设备后再操作！";
		}
		return msg;
		
	}

	@Override
	public boolean updateComputer(Computer computer) {
		computer.setUpdateTime(DateTimeUtil.now());
		boolean updateComputer = super.updateById(computer);
		
		//更新柜体表中的主机名称
		EntityWrapper<Cabinet> cabinetWrapper = new EntityWrapper<>();
		Cabinet cabinet = new Cabinet();
		cabinet.setComputerName(computer.getName());
		cabinetWrapper.eq("computer_id", computer.getId());
		cabinetSvc.update(cabinet, cabinetWrapper);
		
		return updateComputer;
	}

	@Override
	public Page<Computer> getComputers(ComputerCriteria computerCriteria, PageCriteria pageCriteria) {
		
		String name = computerCriteria.getName();
		
		Integer curPage = pageCriteria.getCurPage();
		Integer pageSize = pageCriteria.getPageSize();
		
		EntityWrapper<Computer> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(name)) {
			name = name.trim();
			wrapper.like("name", name);
		}
		wrapper.isNull("delete_time");
		wrapper.orderBy("create_time");
		
		Page<Computer> page = new Page<>(curPage, pageSize);
		
		return super.selectPage(page, wrapper);
	}
	
	@Override
	public Computer getComputerByIdentifyCode(String identifyCode) {
		EntityWrapper<Computer> wrapper = new EntityWrapper<>();
		wrapper.eq("identify_code", identifyCode);
		wrapper.isNull("delete_time");
		return super.selectOne(wrapper);
	}

	@Override
	public Computer getComputerById(String id) {
		return super.selectById(id);
	}

	@Override
	public Computer getComputerByName(String name) {
		EntityWrapper<Computer> wrapper = new EntityWrapper<>();
		wrapper.eq("name", name);
		wrapper.isNull("delete_time");
		return super.selectOne(wrapper);
	}



}

