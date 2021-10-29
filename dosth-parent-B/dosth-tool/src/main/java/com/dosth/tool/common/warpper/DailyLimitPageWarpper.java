package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.DailyLimit;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.vo.ViewUser;

/**
 * 每日限额
 * 
 * @author liweifeng
 *
 */
public class DailyLimitPageWarpper extends PageWarpper<DailyLimit> {

	public DailyLimitPageWarpper(Page<DailyLimit> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof MatEquInfo) {
			map.put(key, ((MatEquInfo) obj).getMatEquName() + " " + ((MatEquInfo) obj).getSpec());
		}else if(obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser)obj));
		}
	}

}
