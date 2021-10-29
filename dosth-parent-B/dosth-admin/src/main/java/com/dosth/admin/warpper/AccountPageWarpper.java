package com.dosth.admin.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.User;
import com.dosth.common.warpper.PageWarpper;

/**
 * 账户分页查询包装类
 * 
 * @author guozhidong
 *
 */
public class AccountPageWarpper extends PageWarpper<Account> {

	public AccountPageWarpper(Page<Account> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof Account) {
			map.put(key, ((Account) obj).getLoginName());
		}else if (obj instanceof User) {
			map.put(key, ((User) obj).getName());
		}
	}
}