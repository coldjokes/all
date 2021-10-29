package com.dosth.admin.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.LoginLog;
import com.dosth.common.warpper.PageWarpper;

/**
 * 登录日志分页查询包装类
 * 
 * @author guozhidong
 *
 */
public class LoginLogPageWarpper extends PageWarpper<LoginLog> {

	public LoginLogPageWarpper(Page<LoginLog> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof LoginLog) {
			map.put(key, ((LoginLog) obj).getLogName());
		}else if (obj instanceof Account){
			map.put(key, ((Account) obj).getLoginName());
		}
	}
}
