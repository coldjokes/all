package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.constant.YesOrNo;
import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.entity.PlcSetting;

/**
 * PLC设置分页封装类
 * 
 * @author liweifeng
 *
 */
public class PlcSettingPageWarpper extends PageWarpper<PlcSetting> {

	public PlcSettingPageWarpper(Page<PlcSetting> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof PlcSetting) {
			map.put(key, ((PlcSetting) obj).getPlcName());
		} else if (obj instanceof YesOrNo) {
			map.put(key, ((YesOrNo) obj).getMessage());
		}
	}
}