package com.cnbaosi.cabinet.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.cabinet.aop.db.DataSource;
import com.cnbaosi.cabinet.entity.enums.DataSourceEnum;
import com.cnbaosi.cabinet.mapper.MesMapper;
import com.cnbaosi.cabinet.serivce.MesService;

/**
 * mes相关方法实现
 * 
 * @author Yifeng Wang
 */
@Service
public class MesServiceImpl implements MesService{

	@Autowired
	private MesMapper mesMapper;
	
	@Override
	@DataSource(DataSourceEnum.OUTER)
	public String getMaterialNamesByCode(String code) {
		return mesMapper.getMaterialNamesByCode(code);
	}

}
