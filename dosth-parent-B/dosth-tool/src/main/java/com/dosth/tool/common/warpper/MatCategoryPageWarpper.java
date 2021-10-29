package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.entity.MatCategory;

/**
 * 物料关联设置分页封装类
 * 
 * @author chenlei
 *
 */
public class MatCategoryPageWarpper extends PageWarpper<MatCategory> {

	public MatCategoryPageWarpper(Page<MatCategory> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		
	}

}
