package com.dosth.toolcabinet.service;

import java.util.List;

import com.dosth.comm.node.ZTreeNode;
import com.dosth.toolcabinet.dto.AccountUserInfo;

/**
 * 
 * @description 授权与验证Service
 * @author guozhidong
 *
 */
public interface AuthorService {
	/**
	 * @description 获取用户信息
	 * @param cardStr 卡片内容
	 * @return
	 * @throws Exception
	 */
	public AccountUserInfo getAccountUserInfo(String cardStr) throws Exception;

	/**
	 * @description 验证用户
	 * @param loginName 登录名
	 * @param pwd       密码
	 * @return
	 */
	public AccountUserInfo checkUser(String loginName, String pwd) throws Exception;

	/**
	 * @description 初始化帐户树
	 * @return
	 */
	public List<ZTreeNode> initAccountTree();

	/**
	 * @description 绑定Ic卡
	 * @param accountId 帐户Id
	 * @param cardNo    卡片No
	 * @param popedoms  权限信息
	 * @return
	 */
	public boolean icBind(String accountId, String cardNo, String popedoms);

	/**
	 * @description 人脸识别绑定
	 * @param accountId 帐户Id
	 * @param file      人像
	 * @return
	 */
	public boolean faceBind(String accountId, String file);

	/**
	 * @description 人脸识别登录
	 * @param file 人像
	 * @return
	 */
	public AccountUserInfo faceLogin(String file);
}