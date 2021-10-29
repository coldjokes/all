package com.dosth.toolcabinet.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dosth.comm.node.ZTreeNode;
import com.dosth.toolcabinet.dto.AccountUserInfo;
import com.dosth.toolcabinet.service.AdminService;
import com.dosth.toolcabinet.service.AuthorService;

/**
 * 
 * @description 授权与验证Service实现
 * @author guozhidong
 *
 */
@Service
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AdminService adminService;

	@Override
	public AccountUserInfo getAccountUserInfo(String cardStr) throws Exception {
		AccountUserInfo user = this.adminService.getAccountUserInfoByCardStr(cardStr);
		if (user == null) {
			throw new Exception("无效卡片信息");
		}
		return user;
	}

	@Override
	public AccountUserInfo checkUser(String loginName, String pwd) throws Exception {
		AccountUserInfo user = this.adminService.checkUserPwd(loginName, pwd);
		if (user == null) {
			throw new Exception("无效用户名或密码");
		}
		return user;
	}

	@Override
	public List<ZTreeNode> initAccountTree() {
		return this.adminService.initAccountTree();
	}

	@Override
	public boolean icBind(String accountId, String cardNo, String popedoms) {
//		this.toolService.bindBorrowPopedoms(accountId, popedoms);
		return this.adminService.icBind(accountId, cardNo);
	}

	@Override
	public boolean faceBind(String accountId, String file) {
		return this.adminService.faceBind(accountId, file);
	}

	@Override
	public AccountUserInfo faceLogin(String file) {
		return this.adminService.faceLogin(file);
	}
}