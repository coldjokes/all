package com.dosth.admin.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.admin.entity.SystemInfo;
import com.dosth.common.warpper.PageWarpper;

public class SystemInfoPageWarpper extends PageWarpper<SystemInfo> {

	public SystemInfoPageWarpper(Page<SystemInfo> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof SystemInfo) {
			map.put(key, ((SystemInfo) obj).getSystemName());
		}
	}

}
