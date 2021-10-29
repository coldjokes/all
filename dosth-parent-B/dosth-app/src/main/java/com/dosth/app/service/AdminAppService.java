package com.dosth.app.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dosth.app.dto.AppAdvice;
import com.dosth.app.dto.AppHelper;
import com.dosth.app.dto.AppUser;
import com.dosth.remote.ResponseTip;

/**
 * @description 用户管理远程接口
 * @Author guozhidong
 */
@FeignClient("service-admin")
public interface AdminAppService {

	/**
	 * @description 根据sessionId获取用户登录信息
	 * @param tokenId sessionId
	 * @return
	 */
	@RequestMapping("/app/getCacheAppUser")
	public AppUser getCacheAppUser(@RequestParam("tokenId") String tokenId);

	/**
	 * @description 校验手机用户信息
	 * @param username     用户名
	 * @param userPassword 用户密码
	 * @return
	 */
	@RequestMapping("/app/checkAppUser")
	public AppUser checkAppUser(@RequestParam("username") String username, @RequestParam("userPassword") String userPassword);

	/**
	 * @description 设置App用户信息
	 * @param tokenId sessionId
	 * @param appUser App用户信息
	 */
	@RequestMapping("/app/putCacheAppUser")
	public ResponseTip putCacheAppUser(@RequestParam("tokenId") String tokenId, @RequestBody AppUser appUser);

	/**
	 * @description 问题反馈
	 * @param advice 问题对象
	 * @return
	 */
	@RequestMapping("/app/insertAdvice")
	public ResponseTip insertAdvice(@RequestBody AppAdvice advice);

	/**
	 * @description 用户注册
	 * @param user 注册用户信息
	 * @return
	 */
	@RequestMapping("/app/userRegister")
	public ResponseTip userRegister(@RequestBody AppUser user);

	/**
	 * @description 获取帮助中心列表
	 * @return
	 */
	@RequestMapping("/app/getAppHelperList")
	public List<AppHelper> getAppHelperList();
}