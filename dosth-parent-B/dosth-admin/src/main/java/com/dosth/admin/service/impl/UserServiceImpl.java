package com.dosth.admin.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.cnbaosi.dto.admin.FeignDept;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.admin.constant.Const;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.AccountRole;
import com.dosth.admin.entity.Dept;
import com.dosth.admin.entity.User;
import com.dosth.admin.faceUtil.service.FaceEngineService;
import com.dosth.admin.repository.AccountRepository;
import com.dosth.admin.repository.AccountRoleRepository;
import com.dosth.admin.repository.DeptRepository;
import com.dosth.admin.repository.LoginLogRepository;
import com.dosth.admin.repository.OperationLogRepository;
import com.dosth.admin.repository.UserRepository;
import com.dosth.admin.rpc.ToolService;
import com.dosth.admin.service.DeptService;
import com.dosth.admin.service.UserService;
import com.dosth.admin.util.ListUtil;
import com.dosth.admin.util.face.FaceVerificationUtil;
import com.dosth.app.dto.FeignUser;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.util.ExcelUtil;
import com.dosth.common.util.ToolUtil;
import com.dosth.dto.ExtraBoxNum;
import com.dosth.toolcabinet.dto.AccountUserInfo;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.util.OpTip;

import cn.hutool.core.collection.CollectionUtil;

/**
 * 用户Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private FaceEngineService faceEngineService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DeptRepository deptRepository;
	@Autowired
	private DeptService deptService;
	@Autowired
	private ToolService toolService;
	@Autowired
	private AccountRoleRepository accountRoleRepository;
	@Autowired
	private LoginLogRepository loginLogRepository;
	@Autowired
	private OperationLogRepository operationLogRepository;

	@Override
	public void save(User user) throws DoSthException {

		Account account = new Account();
		account.setLoginName(user.getLoginName());
		account.setStatus(ManagerStatus.OK);
		account.setSalt(ShiroKit.getRandomSalt(5));

		// 如果前端传来的密码不为空则将使用传来的密码存入数据库。
		if (StringUtils.isNotBlank(user.getPassword())) {
			account.setPassword(ShiroKit.md5(user.getPassword(), account.getSalt()));
			account.setFacePwd(FaceVerificationUtil.faceEncode(user.getPassword(), account.getSalt()));
		} else {
			account.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, account.getSalt()));
			account.setFacePwd(FaceVerificationUtil.faceEncode(Const.DEFAULT_PWD, account.getSalt()));
		}

		this.accountRepository.save(account);

		user.setLimitSumNum(0);
		user.setNotReturnLimitNum(0);
		user.setStartTime("00:00:00");
		user.setEndTime("23:59:59");
		user.setIcCard(user.getIcCard());
		user.setAccountId(account.getId());
		this.userRepository.save(user);

		if (StringUtils.isNotBlank(user.getExtraBoxNum())) {
			this.toolService.updateExtraBoxNum(user.getAccountId(), user.getExtraBoxNum());
		} else {
			this.toolService.updateExtraBoxNum(user.getAccountId(), "0");
		}

		// 绑定管理员角色
		if (user.getIsAdminRole() != null && user.getIsAdminRole()) {
			this.accountRoleRepository.save(new AccountRole(account.getId(), "102"));
		}
	}

	@Override
	public User update(User user) throws DoSthException {
		User tmpUser = null;
		if (user.getFaceFeature() == null) {
			tmpUser = this.userRepository.findOne(user.getId());
			if (tmpUser != null) {
				user.setFaceFeature(tmpUser.getFaceFeature());
			}
		}

		if (user.getExtraBoxNum() != null && !"".equals(user.getExtraBoxNum())) {
			this.toolService.updateExtraBoxNum(user.getAccountId(), user.getExtraBoxNum());
		} else {
			this.toolService.updateExtraBoxNum(user.getAccountId(), "0");
		}
		// 查询角色列表
		List<String> roleIdList = this.accountRoleRepository.getRoleIdListByAccountId(user.getAccountId());

		if (user.getIsAdminRole() != null && user.getIsAdminRole()) { // 绑定
			// 不存在管理员角色,添加管理员角色
			if (roleIdList == null || !roleIdList.contains("102")) {
				this.accountRoleRepository.save(new AccountRole(user.getAccountId(), "102"));
			}
		} else { // 解绑
			// 存在管理员角色,解绑管理员角色
			if (roleIdList != null && roleIdList.contains("102")) {
				List<AccountRole> arList = this.accountRoleRepository
						.getAccountRoleListByAccountId(user.getAccountId());
				for (AccountRole ar : arList) {
					if ("102".equals(ar.getRoleId())) {
						this.accountRoleRepository.delete(ar);
					}
				}
			}
		}
		return this.userRepository.saveAndFlush(user);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public User findUserByAccountId(String accountId) throws DoSthException {
		return this.userRepository.findUserByAccountId(accountId);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public User get(Serializable userId) throws DoSthException {
		User user = this.userRepository.getOne(userId);
		List<String> roleIdList = this.accountRoleRepository.getRoleIdListByAccountId(user.getAccountId());
		// 管理员
		if (roleIdList != null && roleIdList.contains("102")) {
			user.setIsAdminRole(true);
		}
		return user;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<User> getPage(int pageNo, int pageSize, String dept, String name) throws BusinessException {
		Criteria<User> c = new Criteria<User>();
		c.add(Restrictions.eq("account.status", ManagerStatus.OK, true));
		c.add(Restrictions.ne("id", Const.SUPER_ADMINISTRATOR_ID, true));
		if (ToolUtil.isNotEmpty(dept)) {
			c.add(Restrictions.like("dept.deptName", dept.trim(), true));
		}
		if (ToolUtil.isNotEmpty(name)) {
			c.add(Restrictions.or(new Criterion[] { Restrictions.like("name", name.trim(), true),
					Restrictions.like("account.loginName", name.trim(), true), Restrictions.like("icCard", name.trim(), true) }));
		}

		ExtraBoxNum extraBoxNum;
		List<User> userList = this.userRepository.findAll(c);
		for (User user : userList) {
			extraBoxNum = this.toolService.getExtraBoxNum(user.getAccountId());
			if (extraBoxNum != null) {
				user.setExtraBoxNum(extraBoxNum.getExtraBoxNum() != null ? extraBoxNum.getExtraBoxNum() : "");
			}
			user.setDeptName(user.getDept().getDeptName());
		}
		Pageable pageable = new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "account.status"));
		return ListUtil.listConvertToPage(userList, pageable);
	}

	@Override
	public void delete(User user) throws DoSthException {
		this.userRepository.saveAndFlush(user);
	}

	@Override
	public UserInfo getUserByAccountId(FeignUser feignUser) {
		User user = this.userRepository.findUserByAccountId(feignUser.getAccountId());
		user.setStartTime(feignUser.getStartTime());
		user.setEndTime(feignUser.getEndTime());
		user.setLimitSumNum(feignUser.getLimitSumNum());
		user.setNotReturnLimitNum(feignUser.getNotReturnLimitNum());
		this.userRepository.saveAndFlush(user);
		UserInfo userInfo = new UserInfo(user.getName(), user.getAccount().getLoginName(), user.getStartTime(),
				user.getEndTime(), user.getLimitSumNum(), user.getNotReturnLimitNum(), user.getAccountId(),
				user.getDeptId(), user.getDept().getDeptName(), user.getIcCard());
		return userInfo;
	}

	@Override
	public boolean icBind(String accountId, String cardNo) {
		if (cardNo != null && !"".equals(cardNo)) {
			// 根据IC卡号获取用户，删除现有绑定
			User icCard = this.userRepository.findUserByIcCard(cardNo);
			if (icCard != null) {
				icCard.setIcCard(null);
				this.userRepository.saveAndFlush(icCard);
			}

			// 根据帐号ID获取用户，绑定
			User user = this.userRepository.findUserByAccountId(accountId);
			if (user != null) {
				user.setIcCard(cardNo);
				this.userRepository.saveAndFlush(user);
			}
		}

		return true;
	}

	@Override
	public List<User> findUserByDeptId(String deptId) {
		return this.userRepository.findUserByDeptId(deptId);
	}

	private String base64Process(String base64Str) {
		if (!StringUtils.isEmpty(base64Str)) {
			String photoBase64 = base64Str.substring(0, 30).toLowerCase();
			int indexOf = photoBase64.indexOf("base64,");
			if (indexOf > 0) {
				base64Str = base64Str.substring(indexOf + 7);
			}

			return base64Str;
		} else {
			return "";
		}
	}

	@Override
	public UserInfo getUserInfo(String accountId) {
		User user = this.userRepository.findUserByAccountId(accountId);
		UserInfo userInfo = new UserInfo(user.getName(), user.getAccount().getLoginName(), user.getStartTime(),
				user.getEndTime(), user.getLimitSumNum(), user.getNotReturnLimitNum(), user.getAccountId(),
				user.getDeptId(), user.getDept().getDeptName(), user.getIcCard());
		return userInfo;
	}

	@Override
	public boolean faceBind(String accountId, String file) {
		try {
			byte[] decode = Base64.decode(base64Process(file));
			ImageInfo imageInfo = ImageFactory.getRGBData(decode);
			// 人脸特征获取
			byte[] faceFeature;
			faceFeature = this.faceEngineService.extractFaceFeature(imageInfo);
			if (faceFeature == null) {
				return false;
			}

			// 人脸比对，获取比对结果
			List<User> userFaceInfoList;
			try {
				userFaceInfoList = this.faceEngineService.compareFaceFeature(faceFeature);
				if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
					for (User faceUserInfo : userFaceInfoList) {
						User userInfo = this.userRepository.getOne(faceUserInfo.getId());
						userInfo.setFaceFeature(null);
						this.userRepository.saveAndFlush(userInfo);
					}
				}
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			// 根据账户增加人脸特征
			User user = this.findUserByAccountId(accountId);
			user.setFaceFeature(faceFeature);
			this.userRepository.saveAndFlush(user);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public AccountUserInfo faceLogin(String file) {
		AccountUserInfo userInfo = null;
		try {
			byte[] decode = Base64.decode(base64Process(file));
			BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(decode));
			ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);
			// 人脸特征获取
			byte[] bytes = faceEngineService.extractFaceFeature(imageInfo);
			if (bytes != null) {
				// 人脸比对，获取比对结果
				List<User> userFaceInfoList = faceEngineService.compareFaceFeature(bytes);
				if (CollectionUtil.isNotEmpty(userFaceInfoList)) {
					User faceUserInfo = this.userRepository.getOne(userFaceInfoList.get(0).getId());
					userInfo = new AccountUserInfo(faceUserInfo.getAccountId(),
							faceUserInfo.getAccount().getLoginName(),
							FaceVerificationUtil.faceDecode(faceUserInfo.getAccount().getFacePwd(),
									faceUserInfo.getAccount().getSalt()),
							faceUserInfo.getDeptId(), faceUserInfo.getDept().getDeptName(), faceUserInfo.getId(),
							faceUserInfo.getName());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userInfo;
	}

	@Override
	public OpTip getListByExcel(InputStream inputStream, String originalFilename) {
		OpTip tip = new OpTip(200, "上传成功");

		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		User user = null;

		String userName = null; // 姓名
		String loginName = null; // 员工帐号
		String icCard = null; // IC卡号
		String deptName = null; // 部门名称

		try {
			List<User> userList = new ArrayList<>();
			// 创建Excel工作薄
			workbook = ExcelUtil.getWorkbook(inputStream);
			sheet = ExcelUtil.getSheet(workbook, 0);
			if (sheet == null) {
				throw new RuntimeException("无法查找到工作区");
			}
			for (int j = 2; j <= sheet.getLastRowNum(); j++) {
				row = ExcelUtil.getRow(sheet, j);
				if (row == null || row.getFirstCellNum() == j || row.getRowNum() <= 1) {
					continue;
				}

				user = new User();
				userName = ExcelUtil.getCellValue(ExcelUtil.getCell(row, 0)); // 姓名
				loginName = ExcelUtil.getCellValue(ExcelUtil.getCell(row, 1)); // 员工帐号
				icCard = ExcelUtil.getCellValue(ExcelUtil.getCell(row, 2)); // IC卡号
				deptName = ExcelUtil.getCellValue(ExcelUtil.getCell(row, 3)); // 部门

				if (userName != null && !"".equals(userName)) {
					user.setName(userName);
				} else {
					return new OpTip(201, "第" + (j + 1) + "行，姓名导入失败");
				}

				List<Account> accountName = this.userRepository.findAccountByLoginName(loginName);
				if (accountName != null && accountName.size() > 0) {
					return new OpTip(201, "第" + (j + 1) + "行，员工帐号重复");
				} else {
					if (loginName != null && !"".equals(loginName)) {
						user.setLoginName(loginName);
					} else {
						return new OpTip(201, "第" + (j + 1) + "行，员工编号导入失败");
					}
				}

				User userIcCard = this.userRepository.findUserByIcCard(icCard);
				if (userIcCard != null) {
					return new OpTip(201, "第" + (j + 1) + "行，IC卡号重复");
				} else {
					if (icCard != null && !"".equals(icCard)) {
						user.setIcCard(icCard);
					}
				}

				Dept dept = this.deptRepository.findDeptByDeptName(deptName);
				if (dept != null) {
					user.setDeptId(dept.getId());
				} else {
					return new OpTip(201, "第" + (j + 1) + "行，部门导入失败");
				}

				user.setLimitSumNum(0);
				user.setNotReturnLimitNum(0);
				user.setStartTime("00:00:00");
				user.setEndTime("23:59:59");
				userList.add(user);
			}

			if (userList != null && userList.size() > 0) {
				for (User info : userList) {
					List<Account> accountList = this.userRepository.findAccountByLoginName(info.getLoginName());
					if (accountList != null && accountList.size() > 0) {
						Account account = accountList.get(0);
						account.setStatus(ManagerStatus.OK);
						account.setSalt(ShiroKit.getRandomSalt(5));
						account.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, account.getSalt()));
						account.setFacePwd(FaceVerificationUtil.faceEncode(Const.DEFAULT_PWD, account.getSalt()));
						this.accountRepository.saveAndFlush(account);
						User u = this.userRepository.findUserByAccountId(account.getId());
						u.setName(info.getName());
						u.setDeptId(info.getDeptId());
						u.setAccountId(account.getId());
						u.setIcCard(info.getIcCard());
						this.userRepository.saveAndFlush(u);
					} else {
						Account account = new Account();
						account.setLoginName(info.getLoginName());
						account.setStatus(ManagerStatus.OK);
						account.setSalt(ShiroKit.getRandomSalt(5));
						account.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, account.getSalt()));
						account.setFacePwd(FaceVerificationUtil.faceEncode(Const.DEFAULT_PWD, account.getSalt()));
						this.accountRepository.save(account);
						info.setAccountId(account.getId());
						this.userRepository.save(info);
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return tip;
	}

	@Override
	public User findUserByIcCard(String icCard) {
		return this.userRepository.findUserByIcCard(icCard);
	}

	@Override
	public List<Account> findAccountByLoginName(String loginName) {
		return this.userRepository.findAccountByLoginName(loginName);
	}

	@Override
	public List<UserInfo> getUserInfos() {
		List<UserInfo> infos = new ArrayList<>();
		List<User> users = this.userRepository.findAll();
		for (User user : users) {
			UserInfo userInfo = new UserInfo(user.getName(), user.getAccount().getLoginName(), user.getStartTime(),
					user.getEndTime(), user.getLimitSumNum(), user.getNotReturnLimitNum(), user.getAccountId(),
					user.getDeptId(), user.getDept().getDeptName(), user.getIcCard());
			infos.add(userInfo);
		}
		return infos;
	}

	@Override
	public OpTip syncFeignUser(List<com.cnbaosi.dto.admin.FeignUser> userList) {
		OpTip tip = new OpTip(200, "人员账户同步信息成功");
		List<Dept> deptList;
		List<Account> accountList;
		Dept dept;
		FeignDept feignDept;
		Account account;
		User entityUser;
		try {
			for (com.cnbaosi.dto.admin.FeignUser user : userList) {
				if (user.getDeptNo() != null && !"".equals(user.getDeptNo())) {
					deptList = this.deptRepository.getDeptListByDeptNo(user.getDeptNo());
				} else {
					deptList = this.deptRepository.getDeptListByDeptName(user.getDeptName());
				}
				if (deptList != null && deptList.size() > 0) {
					dept = deptList.get(0);
				} else {
					feignDept = new FeignDept(user.getDeptName());
					feignDept.setDeptNo(user.getDeptNo());
					feignDept.setParentDeptName(UUID.randomUUID().toString());
					dept = this.deptService.saveFeignDept(feignDept);
				}
				accountList = this.userRepository.findAccountByLoginName(user.getLoginName());
				if (accountList != null && accountList.size() > 0) {
					account = accountList.get(0);
					entityUser = account.getUser();
					entityUser.setName(user.getUserName());
					entityUser.setDeptId(dept.getId());
					entityUser.setName(user.getUserName());
					entityUser.setIcCard(user.getIcCardNo());
					this.userRepository.saveAndFlush(entityUser);
				} else {
					account = new Account();
					account.setLoginName(user.getLoginName());
					account.setStatus(ManagerStatus.OK);
					account.setSalt(ShiroKit.getRandomSalt(5));
					account.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, account.getSalt()));
					account.setFacePwd(FaceVerificationUtil.faceEncode(Const.DEFAULT_PWD, account.getSalt()));
					this.accountRepository.save(account);
					entityUser = new User();
					entityUser.setName(user.getUserName());
					entityUser.setDeptId(dept.getId());
					entityUser.setLimitSumNum(0);
					entityUser.setNotReturnLimitNum(0);
					entityUser.setStartTime("00:00:00");
					entityUser.setEndTime("23:59:59");
					entityUser.setIcCard(user.getIcCardNo());
					entityUser.setAccountId(account.getId());
					entityUser = this.userRepository.save(entityUser);
					this.toolService.updateExtraBoxNum(entityUser.getAccountId(), "0");
				}
			}
		} catch (Exception e) {
			logger.error("人员同步信息失败:" + e.getMessage());
			e.printStackTrace();
			tip.setCode(201);
			tip.setMessage("人员账户同步信息失败");
		}
		return tip;
	}

	@Override
	public OpTip clearUser() {
		OpTip tip = new OpTip("清除人员信息成功");
		try {
			// 账户角色关联
			logger.info("清除账户角色关联(除管理员与超级管理员)");
			this.accountRoleRepository.clearByUser();
			// 登录日志
			logger.info("清除登录日志");
			this.loginLogRepository.clearByUser();
			// 操作日志
			logger.info("清除操作日志");
			this.operationLogRepository.clearByUser();
			// 用户
			logger.info("清除用户");
			this.userRepository.clearUser();
			// 账户
			logger.info("清除账户");
			this.accountRepository.clearByUser();
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("清除人员信息失败");
		}
		return tip;
	}
}