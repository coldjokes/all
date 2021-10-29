package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.vo.ViewUser;

public class SubBoxWarpper extends PageWarpper<SubBox> {

	public SubBoxWarpper(Page<SubBox> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof EquSetting) {
			map.put(key, ((EquSetting) obj).getEquSettingName());
		}
		if (obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
		}
	}
}