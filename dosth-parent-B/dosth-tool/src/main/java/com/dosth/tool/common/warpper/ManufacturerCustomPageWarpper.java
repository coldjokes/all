package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.entity.Manufacturer;
import com.dosth.tool.entity.ManufacturerCustom;

public class ManufacturerCustomPageWarpper extends PageWarpper<ManufacturerCustom>{

	public ManufacturerCustomPageWarpper(Page<ManufacturerCustom> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof Manufacturer) {
			map.put(key, ((Manufacturer) obj).getManufacturerName());
		}
	}

}
