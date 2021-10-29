package com.dosth.admin.rpc;

import java.util.List;

import com.dosth.app.dto.AppAdvice;
import com.dosth.app.dto.AppHelper;
import com.dosth.app.dto.AppUser;
import com.dosth.common.exception.DoSthException;

/**
 * 
 * @description App远程Service接口
 * @author guozhidong
 *
 */
public interface AppRpcService {

	/**
	 * @description 获取缓存App用户
	 * @param tokenId tokenId
	 * @return
	 * @throws DoSthException
	 */
	public AppUser getCacheAppUser(String tokenId) throws DoSthException;

	/**
	 * @description 校验手机用户信息
	 * @param username     用户名
	 * @param userPassword 用户密码
	 * @return
	 * @throws DoSthException
	 */
	public AppUser checkAppUser(String username, String userPassword) throws DoSthException;

	/**
	 * @description 问题反馈
	 * @param advice 问题对象
	 * @return
	 * @throws DoSthException
	 */
	public void insertAdvice(AppAdvice advice) throws DoSthException;

//	/**
//	 * @description 用户注册
//	 * @param user 注册用户信息
//	 * @throws DoSthException
//	 */
//	public void userRegister(AppUser user) throws DoSthException;

	/**
	 * @description 获取帮助中心列表
	 * @return
	 * @throws DoSthException
	 */
	public List<AppHelper> getAppHelperList() throws DoSthException;
}