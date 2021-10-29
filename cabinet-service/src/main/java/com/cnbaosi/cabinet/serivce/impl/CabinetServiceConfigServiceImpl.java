package com.cnbaosi.cabinet.serivce.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.cabinet.config.CabinetServiceConfig;
import com.cnbaosi.cabinet.serivce.CabinetServiceConfigService;

@Service
public class CabinetServiceConfigServiceImpl implements CabinetServiceConfigService{

	//TODO 后期可以优化为一个方法，全都通过注解来实现 类似${cabinet-service.face.status}
	@Autowired
	private CabinetServiceConfig config;
	
	@Override
	public CabinetServiceConfig getConfig() {
//		CabinetServiceConfig config = new CabinetServiceConfig();
		return config;
	}

}
