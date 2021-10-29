package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.util.CabinetInfoUtil;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 补料查询分页封装对象
 * @author alter by guozhidong
 *
 */
public class FeedQueryPageWarpper extends PageWarpper<FeedingList> {

	public FeedQueryPageWarpper(Page<FeedingList> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof EquSetting) {
			map.put(key, CabinetInfoUtil.createCabinetInfoByEquSetting((EquSetting) obj));
		} else if (obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
		}
	}
}