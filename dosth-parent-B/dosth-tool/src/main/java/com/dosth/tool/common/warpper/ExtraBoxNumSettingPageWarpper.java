package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.ExtraBoxNumSetting;
import com.dosth.tool.vo.ViewUser;

public class ExtraBoxNumSettingPageWarpper extends PageWarpper<ExtraBoxNumSetting>{

	public ExtraBoxNumSettingPageWarpper(Page<ExtraBoxNumSetting> page) {
		super(page);
	}	

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
		}
	}
}
