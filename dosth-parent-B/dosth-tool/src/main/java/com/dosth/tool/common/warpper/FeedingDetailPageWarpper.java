package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.entity.FeedingDetail;

/**
 * @description 补料明细分页封装
 * @author guozhidong
 *
 */
public class FeedingDetailPageWarpper extends PageWarpper<FeedingDetail> {

	public FeedingDetailPageWarpper(Page<FeedingDetail> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
	}
}