package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.entity.Unit;

/**
 * 单位分页包装类
 * 
 * @author guozhidong
 *
 */
public class UnitPageWarpper extends PageWarpper<Unit> {

	public UnitPageWarpper(Page<Unit> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
	}
}