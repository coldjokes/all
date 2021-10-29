package com.dosth.admin.service;

import java.io.InputStream;
import java.util.List;

import org.springframework.data.domain.Page;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.User;
import com.dosth.app.dto.FeignUser;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.toolcabinet.dto.AccountUserInfo;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.util.OpTip;

/**
 * 用户Service
 * 
 * @author guozhidong
 *
 */
public interface UserService extends BaseService<User> {

	/**
	 * 根据账户Id获取用户信息
	 * 
	 * @param accountId 账户Id
	 * @return
	 * @throws DoSthException
	 */
	public User findUserByAccountId(String accountId) throws DoSthException;

	/**
	 * 获取分页数据
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param deptid    部门Id
	 * @param name      姓名
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @return
	 * @throws BusinessException
	 */
	public Page<User> getPage(int pageNo, int pageSize, String dept, String name) throws BusinessException;

	/**
	 * 根据账户Id修改用户信息
	 * 
	 * @param accountId 账户Id
	 * @return
	 * @throws DoSthException
	 */
	public UserInfo getUserByAccountId(FeignUser feignUser);

	/**
	 * @description IC卡绑定
	 * @param accountId 帐户Id
	 * @param cardNo    卡号
	 * @return
	 */
	public boolean icBind(String accountId, String cardNo);

	/**
	 * 根据部门 Id获取用户信息
	 * 
	 * @param deptId 部门Id
	 * @return
	 */
	public List<User> findUserByDeptId(String deptId);

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	public List<UserInfo> getUserInfos();

	/**
	 * 根据账户Id获取用户信息
	 * 
	 * @param accountId 账户Id
	 * @return
	 */
	public UserInfo getUserInfo(String accountId);

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

	/**
	 * 用户导入
	 * 
	 * @param inputStream
	 * @param originalFilename
	 * @return
	 */
	public OpTip getListByExcel(InputStream inputStream, String originalFilename);

	/**
	 * 根据IC卡号获取用户信息
	 * 
	 * @param icCard
	 * @return
	 */
	public User findUserByIcCard(String icCard);

	/**
	 * 根据员工帐号获取帐号信息
	 * 
	 * @param loginName
	 * @return
	 */
	public List<Account> findAccountByLoginName(String loginName);

	/**
	 * @description 同步用户信息
	 * @param userList 用户信息列表
	 * @return 同步状态
	 */
	public OpTip syncFeignUser(List<com.cnbaosi.dto.admin.FeignUser> userList);

	/**
	 * @description 清理账户信息
	 */
	public OpTip clearUser();
}