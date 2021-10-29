package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.toolcabinet.enums.ReturnBackType;

public class RestitutionTypePageWarpper extends PageWarpper<RestitutionType>{

	public RestitutionTypePageWarpper(Page<RestitutionType> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof RestitutionType) {
			map.put(key, ((RestitutionType) obj).getRestName());
		}else if(obj instanceof ReturnBackType) {
			map.put(key, ((ReturnBackType) obj).getDesc());
		}
	}
}
