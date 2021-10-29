package com.dosth.admin.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.admin.entity.Dept;
import com.dosth.common.warpper.PageWarpper;

/**
 * 部门分页查询包装类
 * 
 * @author liweifeng
 *
 */
public class DeptPageWarpper extends PageWarpper<Dept>{

	public DeptPageWarpper(Page<Dept> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof Dept) {
			map.put(key, ((Dept) obj).getDeptName());
		}
	}
}
