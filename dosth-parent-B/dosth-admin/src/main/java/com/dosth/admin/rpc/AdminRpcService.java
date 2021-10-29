package com.dosth.admin.rpc;

import java.util.List;

import com.dosth.common.node.ZTreeNode;
import com.dosth.toolcabinet.dto.AccountUserInfo;

/**
 * @description 用户管理远程接口方法
 * @Author guozhidong
 */
public interface AdminRpcService {
	/**
	 * @description 根据卡片获取帐户用户信息
	 * @param cardStr 卡片
	 * @return
	 */
	public AccountUserInfo getAccountUserInfoByCardStr(String cardStr);

	/**
	 * @description 校验登录名和密码
	 * @param loginName 登录名
	 * @param pwd       密码
	 * @return
	 */
	public AccountUserInfo checkUserPwd(String loginName, String pwd);

	/**
	 * @description 初始化帐户树
	 * @return
	 */
	public List<ZTreeNode> initAccountTree();

	/**
	 * @description IC卡绑定
	 * @param accountId 帐户Id
	 * @param cardNo    卡号
	 * @return
	 */
	public boolean icBind(String accountId, String cardNo);

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