package com.dosth.admin.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.admin.entity.OperationLog;
import com.dosth.common.warpper.PageWarpper;

/**
 * 操作日志分页查询包装类
 * 
 * @author liweifeng
 *
 */
public class OperationLogPageWarpper extends PageWarpper<OperationLog> {

	public OperationLogPageWarpper(Page<OperationLog> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof OperationLog) {
			map.put(key, ((OperationLog) obj).getLogName());
		}
	}
}
