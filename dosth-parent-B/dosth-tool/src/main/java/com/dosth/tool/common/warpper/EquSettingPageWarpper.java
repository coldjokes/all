package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.vo.ViewUser;

/**
 * 设备设置分页封装类
 * 
 * @author guozhidong
 *
 */
public class EquSettingPageWarpper extends PageWarpper<EquSetting> {

	public EquSettingPageWarpper(Page<EquSetting> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
		} else if (obj instanceof EquSetting) {
			EquSetting setting = (EquSetting) obj;
			map.put(key, setting.getEquSettingName());
		}
	}
}