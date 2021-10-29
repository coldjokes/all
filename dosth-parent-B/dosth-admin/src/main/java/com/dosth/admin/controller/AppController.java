package com.dosth.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.admin.rpc.AppRpcService;
import com.dosth.app.dto.AppAdvice;
import com.dosth.app.dto.AppHelper;
import com.dosth.app.dto.AppUser;
import com.dosth.common.exception.DoSthException;
import com.dosth.remote.ResponseTip;

/**
 * 
 * @description App远程接口
 * @author guozhidong
 *
 */
@RestController
@RequestMapping("/app")
public class AppController {

	private static final Logger logger = LoggerFactory.getLogger(AppController.class);

	@Autowired
	private AppRpcService appRpcService;

	/**
	 * @description 根据sessionId获取用户登录信息
	 * @param tokenId sessionId
	 * @return
	 */
	@RequestMapping("/getCacheAppUser")
	public AppUser getCacheAppUser(@RequestParam("tokenId") String tokenId) {
		AppUser user = null;
		try {
			user = this.appRpcService.getCacheAppUser(tokenId);
		} catch (DoSthException e) {
			logger.error("App获取缓存用户" + tokenId + "异常:" + e.getMessage());
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * @description 校验手机用户信息
	 * @param username     用户名
	 * @param userPassword 用户密码
	 * @return
	 */
	@RequestMapping("/checkAppUser")
	public AppUser checkAppUser(@RequestParam("username") String username,
			@RequestParam("userPassword") String userPassword) {
		AppUser user = null;
		try {
			user = this.appRpcService.checkAppUser(username, userPassword);
		} catch (DoSthException e) {
			logger.error("App校验用户:" + username + "异常:" + e.getMessage());
			e.printStackTrace();
		}
		return user;
	}

	/**
	 * @description 问题反馈
	 * @param advice 问题对象
	 * @return
	 */
	@RequestMapping("/insertAdvice")
	public ResponseTip insertAdvice(@RequestBody AppAdvice advice) {
		ResponseTip tip = new ResponseTip("添加成功");
		try {
			this.appRpcService.insertAdvice(advice);
		} catch (DoSthException e) {
			tip = new ResponseTip("添加失败");
			logger.error("问题反馈异常:" + e.getMessage());
			e.printStackTrace();
		}
		return tip;
	}

	/**
	 * @description 获取帮助中心列表
	 * @return
	 */
	@RequestMapping("/getAppHelperList")
	public List<AppHelper> getAppHelperList() {
		List<AppHelper> helpList = null;
		try {
			helpList = this.appRpcService.getAppHelperList();
		} catch (DoSthException e) {
			logger.error("获取帮助中心列表:" + e.getMessage());
			e.printStackTrace();
		}
		return helpList;
	}
}