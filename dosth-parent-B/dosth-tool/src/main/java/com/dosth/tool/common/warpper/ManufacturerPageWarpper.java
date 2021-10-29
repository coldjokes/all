package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.entity.Manufacturer;

/**
 * 生产商分页包装类
 * 
 * @author guozhidong
 *
 */
public class ManufacturerPageWarpper extends PageWarpper<Manufacturer> {

	public ManufacturerPageWarpper(Page<Manufacturer> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof Manufacturer) {
			map.put(key, ((Manufacturer) obj).getManufacturerName());
		}
	}
}