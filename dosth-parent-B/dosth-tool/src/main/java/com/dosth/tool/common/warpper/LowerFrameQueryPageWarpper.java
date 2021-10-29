package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.LowerFrameQuery;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 下架分页封装对象
 * @author guozhidong
 *
 */
public class LowerFrameQueryPageWarpper extends PageWarpper<LowerFrameQuery> {

	public LowerFrameQueryPageWarpper(Page<LowerFrameQuery> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof MatEquInfo) {
			map.put(key, ((MatEquInfo) obj).getMatEquName());
		} else if (obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
		}
	}
}