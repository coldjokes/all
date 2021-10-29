package com.dosth.admin.controller;

import java.util.List;

import javax.naming.NoPermissionException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.admin.common.exception.BizExceptionEnum;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.admin.constant.Const;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Account;
import com.dosth.admin.service.AccountService;
import com.dosth.admin.util.face.FaceVerificationUtil;
import com.dosth.admin.warpper.AccountPageWarpper;
import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.ErrorTip;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.controller.BaseController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.util.ToolUtil;

/**
 * 账户 RestController
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/mgrAccount")
public class AccountController extends BaseController {

	private static String PREFIX = "/admin/account/";

	@Autowired
	private AccountService accountService;
	
	/**
	 * 跳转到查看账户列表的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("columns", new AccountPageWarpper(null).createColumns(TableSelectType.RADIO));
		return PREFIX + "account.html";
	}
	
	/**
	 * 查询账户列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list() {
		int pageNo = 1; 
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		try {
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		String deptid = ShiroKit.getAccount().getDeptId();
		try {
			// 前台取消部门Id
			deptid = super.getPara("deptid");
		} catch (Exception e) {
		}
		String name = super.getPara("name"); 
		if (ToolUtil.isEmpty(deptid)) {
			deptid = ShiroKit.getAccount().getDeptId();
		}
		Page<Account> page = this.accountService.getPage(pageNo, pageSize, deptid, name);
		return new AccountPageWarpper(page).invokeObjToMap();
	}
	
	/**
	 * 跳转到修改账户页面
	 */
	@RequestMapping("/account_edit/{accountId}")
	public String userEdit(@PathVariable String accountId, Model model) {
		if (ToolUtil.isEmpty(accountId)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		Account account = this.accountService.get(accountId);
		model.addAttribute(account);
		LogObjectHolder.me().set(account);
		return PREFIX + "account_edit.html";
	}

	/**
	 * 修改账户
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改账户")
	@ResponseBody
	public Tip edit(@Valid Account account, BindingResult result) throws NoPermissionException {
		if (result.hasErrors()) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		Account tmpAccount = this.accountService.get(account.getId());
		if(ManagerStatus.FREEZED.equals(tmpAccount.getStatus())) {
			return new ErrorTip(201, "账户已冻结");
		}
		if (ShiroKit.hasRole(Const.ADMIN_NAME)) {
			String newPwd = account.getPassword();
			account.setSalt(ShiroKit.getRandomSalt(5));
			account.setPassword(ShiroKit.md5(newPwd, account.getSalt()));
			account.setFacePwd(FaceVerificationUtil.faceEncode(newPwd, account.getSalt()));
			this.accountService.update(account);
			return SUCCESS_TIP;
		} else {
			ShiroAccount shiroAccount = ShiroKit.getAccount();
			if (shiroAccount.getId().equals(account.getId())) {
				this.accountService.update(account);
				return SUCCESS_TIP;
			} else {
				throw new BusinessException(BizExceptionEnum.NO_PERMITION);
			}
		}
	}

	/**
	 * 重置账户的密码
	 */
	@RequestMapping("/reset")
	@BussinessLog(businessName = "重置账户密码", ignore = true)
	@ResponseBody
	public Tip reset(@RequestParam String accountId) {
		if (ToolUtil.isEmpty(accountId)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		Account account = this.accountService.get(accountId);
		if(ManagerStatus.FREEZED.equals(account.getStatus())){
			return new ErrorTip(201, "账户已冻结");
		}
		account.setSalt(ShiroKit.getRandomSalt(5));
		account.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, account.getSalt()));
		account.setFacePwd(FaceVerificationUtil.faceEncode(Const.DEFAULT_PWD, account.getSalt()));
		this.accountService.update(account);
		return SUCCESS_TIP;
	}
	
	/**
	 * 跳转到修改密码界面
	 */
	@RequestMapping("/user_chpwd")
	public String chPwd() {
		return PREFIX + "user_chpwd.html";
	}

	/**
	 * 修改当前用户的密码
	 */
	@RequestMapping("/changePwd")
	@BussinessLog(businessName = "修改密码", ignore = true)
	@ResponseBody
	public Object changePwd(@RequestParam String oldPwd, @RequestParam String newPwd, @RequestParam String rePwd) {
		if (!newPwd.equals(rePwd)) {
			throw new BusinessException(BizExceptionEnum.TWO_PWD_NOT_MATCH);
		}
		String accountId = ShiroKit.getAccount().getId();
		Account account = this.accountService.get(accountId);
		String oldMd5 = ShiroKit.md5(oldPwd, account.getSalt());
		if (account.getPassword().equals(oldMd5)) {
			String newMd5 = ShiroKit.md5(newPwd, account.getSalt());
			account.setPassword(newMd5);
			account.setFacePwd(FaceVerificationUtil.faceEncode(newPwd, account.getSalt()));
			this.accountService.update(account);
			return SUCCESS_TIP;
		} else {
			throw new BusinessException(BizExceptionEnum.OLD_PWD_NOT_RIGHT);
		}
	}
	
	/**
	 * 创建账户树
	 */
	@RequestMapping("/crAccTree")
	@ResponseBody
	public List<ZTreeNode> createAccountTree() {
		List<ZTreeNode> accountTree = this.accountService.createAccountTree();
		return accountTree;
	}
}