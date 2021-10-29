package com.dosth.toolcabinet.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.dosth.comm.node.ZTreeNode;
import com.dosth.toolcabinet.dto.AccountUserInfo;

/**
 * @description 用户管理远程接口
 * @Author guozhidong
 */
@FeignClient("service-admin")
public interface AdminService {
	/**
	 * @description 根据卡片获取帐户用户信息
	 * @param cardStr 卡片
	 * @return
	 */
	@RequestMapping(value = "/feign/accountUserInfo/{cardStr}")
	public AccountUserInfo getAccountUserInfoByCardStr(@PathVariable(value = "cardStr") String cardStr);

	/**
	 * @description 校验登录名和密码
	 * @param loginName 登录名
	 * @param pwd       密码
	 * @return
	 */
	@RequestMapping(value = "/feign/checkUserPwd/{loginName}/{pwd}")
	public AccountUserInfo checkUserPwd(@PathVariable String loginName, @PathVariable String pwd);

	/**
	 * @description 初始化帐户树
	 * @return
	 */
	@RequestMapping(value = "/feign/initAccountTree")
	public List<ZTreeNode> initAccountTree();

	/**
	 * @description IC卡绑定
	 * @param accountId 帐户Id
	 * @param cardNo    卡号
	 * @return
	 */
	@RequestMapping(value = "/feign/icBind")
	public boolean icBind(@RequestParam("accountId") String accountId, @RequestParam("cardNo") String cardNo);

	/**
	 * @description 人脸识别绑定
	 * @param accountId 帐户Id
	 * @param file      人像
	 * @return
	 */
	@RequestMapping(value = "/feign/faceBind", method = RequestMethod.POST)
	public boolean faceBind(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "file") String file);

	/**
	 * @description 人脸识别登录
	 * @param file 人像
	 * @return
	 */
	@RequestMapping(value = "/feign/faceLogin", method = RequestMethod.POST)
	public AccountUserInfo faceLogin(@RequestParam(value = "file") String file);
}