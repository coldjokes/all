package com.dosth.admin.rpc.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.mobile.Advice;
import com.dosth.admin.entity.mobile.Helper;
import com.dosth.admin.rpc.AppRpcService;
import com.dosth.admin.service.AccountService;
import com.dosth.admin.service.AdviceService;
import com.dosth.admin.service.HelperService;
import com.dosth.admin.util.face.FaceVerificationUtil;
import com.dosth.app.dto.AppAdvice;
import com.dosth.app.dto.AppHelper;
import com.dosth.app.dto.AppUser;
import com.dosth.common.exception.DoSthException;

/**
 * 
 * @description App远程Service接口实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class AppRpcServiceImpl implements AppRpcService {
	
	@Autowired
	private AccountService accountService;
//	@Autowired
//	private PhoneUserService phoneUserService;
	@Autowired
	private AdviceService adviceService;
	@Autowired
	private HelperService helperService;

	@Override
	public AppUser getCacheAppUser(String tokenId) throws DoSthException {
		return null; // new AppUser("1", "admin", "阿诺事业部", "245", "http://192.168.2.46:8080/img/0.jpg");
	}

	@Override
	public AppUser checkAppUser(String username, String userPassword) throws DoSthException {
		AppUser appUser = null;
		List<Account> accountList = this.accountService.getByLoginName(username);
		if (accountList != null && accountList.size() > 0) {
			Account account = accountList.get(0);
			if (userPassword.equals(FaceVerificationUtil.faceDecode(account.getFacePwd(), account.getSalt()))) {
				appUser = new AppUser(account.getId(), account.getLoginName(), account.getUser().getDept().getDeptName(),
						 account.getUser().getId(), account.getUser().getAvatar());
			}
		}
		return appUser;
	}

	@Override
	public void insertAdvice(AppAdvice advice) throws DoSthException {
		this.adviceService.save(new Advice(advice.getAccountId(), advice.getAdviceContent(), advice.getAdviceMail(), 
				advice.getAdviceImages(), advice.getAdviceContact()));
	}

//	@Override
//	public void userRegister(AppUser user) throws DoSthException {
//		try {
//			this.phoneUserService.save(new PhoneUser(user.getUserName(), 
//					user.getUserNo(), user.getUserPartment(), 
//					user.getPhoneNo(), user.getAppPwd()));
//		} catch (Exception e) {
//		}
//	}

	@Override
	public List<AppHelper> getAppHelperList() throws DoSthException {
		List<Helper> helperList = this.helperService.getHelperList();
		List<AppHelper> list = new ArrayList<>();
		for (Helper helper : helperList) {
			list.add(new AppHelper(helper.getQuestionName(), helper.getAnswer()));
		}
		return list;
	}
}