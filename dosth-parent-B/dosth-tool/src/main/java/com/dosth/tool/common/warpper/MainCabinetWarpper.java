package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.MatEquInfo;

/**
 * @description 主柜分配封装类
 * @author guozhidong
 *
 */
public class MainCabinetWarpper extends PageWarpper<EquDetailSta> {

	public MainCabinetWarpper(Page<EquDetailSta> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof EquDetail) {
			map.put(key, ((EquDetail) obj).getEquSetting().getEquSettingName());
		} else if (obj instanceof MatEquInfo) {
			map.put(key, ((MatEquInfo) obj).getMatEquName());
		}
	}
}