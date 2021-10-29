package com.dosth.admin.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.admin.entity.Dept;
import com.dosth.admin.entity.Roles;
import com.dosth.common.warpper.PageWarpper;

/**
 * 角色分页查询包装类
 * 
 * @author guozhidong
 *
 */
public class RolePageWarpper extends PageWarpper<Roles> {

	public RolePageWarpper(Page<Roles> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof Dept) {
			map.put(key, ((Dept) obj).getDeptName());
		}
	}
}