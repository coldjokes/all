package com.dosth.admin.controller;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dosth.admin.common.config.properties.DosthProperties;
import com.dosth.admin.common.exception.BizExceptionEnum;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.admin.constant.Const;
import com.dosth.admin.constant.factory.ConstantFactory;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.User;
import com.dosth.admin.rpc.ToolService;
import com.dosth.admin.service.AccountService;
import com.dosth.admin.service.UserService;
import com.dosth.admin.util.face.FaceVerificationUtil;
import com.dosth.comm.node.ZTreeNode;
import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.controller.BaseController;
import com.dosth.common.util.ToolUtil;
import com.dosth.dto.ExtraBoxNum;
import com.dosth.util.OpTip;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户 Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/mgrUser")
public class UserController extends BaseController {

	private static String PREFIX = "/admin/user/";

	@Autowired
	private UserService userService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private DosthProperties dosthProperties;
	@Autowired
	private ToolService toolService;

	/**
	 * 跳转到查看管理员列表的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "user.html";
	}

	/**
	 * 查询管理员列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list() {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		String name = super.getPara("name");
		String dept = super.getPara("dept");
		Page<User> page = this.userService.getPage(pageNo, pageSize, dept, name);
		return page;
	}

	/**
	 * 跳转到添加用户的页面
	 */
	@RequestMapping("/user_add")
	public String addView(Model model) {
		return PREFIX + "user_add.html";
	}

	/**
	 * 添加用户
	 */
	@RequestMapping("/add")
	@BussinessLog(businessName = "添加用户")
	@ResponseBody
	public OpTip add(@Valid User user, BindingResult result) {
		OpTip tip = new OpTip(200, "添加成功");

		List<Account> loginName = this.userService.findAccountByLoginName(user.getLoginName());
		if (loginName != null && loginName.size() > 0) {
			return new OpTip(201, "员工帐号重复");
		}

		if (user.getIcCard() != null && !"".equals(user.getIcCard())) {
			User icCard = this.userService.findUserByIcCard(user.getIcCard());
			if (icCard != null) {
				return new OpTip(201, "IC卡号重复");
			}
		}

		this.userService.save(user);
		return tip;
	}

	/**
	 * 跳转到修改用户页面
	 */
	@RequestMapping("/user_edit/{userId}")
	public String userEdit(@PathVariable String userId, Model model) {
		if (ToolUtil.isEmpty(userId)) {
			throw new BusinessException(BizExceptionEnum.REQUEST_NULL);
		}
		User user = this.userService.get(userId);

		ExtraBoxNum extraBoxNum = this.toolService.getExtraBoxNum(user.getAccountId());
		if (extraBoxNum != null) {
			user.setExtraBoxNum(extraBoxNum.getExtraBoxNum());
		}
		model.addAttribute(user);
		model.addAttribute("deptName", ConstantFactory.me().getDeptName(user.getDeptId()));
		LogObjectHolder.me().set(user);
		return PREFIX + "user_edit.html";
	}

	/**
	 * 修改用户
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改用户")
	@ResponseBody
	public OpTip edit(@Valid User user, BindingResult result) throws NoPermissionException {
		OpTip tip = new OpTip(200, "修改成功");
		List<Account> loginName = this.userService.findAccountByLoginName(user.getLoginName());

		if (CollectionUtils.isNotEmpty(loginName)) {
			for (Account name : loginName) {
				if (!name.getId().equals(user.getAccountId())) {
					return new OpTip(201, "员工帐号重复");
				}
			}
		}

		if (StringUtils.isNotBlank(user.getIcCard())) {
			User icCard = this.userService.findUserByIcCard(user.getIcCard());
			if (icCard != null && !icCard.getId().equals(user.getId())) {
				return new OpTip(201, "IC卡号重复");
			} else {

			}
		}

		Account account = this.accountService.get(user.getAccountId());
		if ("admin".equals(account.getLoginName()) || "administrator".equals(account.getLoginName())) {
			if (!user.getLoginName().equals(account.getLoginName())) {
				return new OpTip(201, "管理员账号不可修改");
			}
		}

		account.setLoginName(user.getLoginName());
		account.setStatus(ManagerStatus.OK);
		// 如果前端传来密码则发生不改变
		if (StringUtils.isNotBlank(user.getPassword())) {
			account.setSalt(ShiroKit.getRandomSalt(5));
			account.setPassword(ShiroKit.md5(user.getPassword(), account.getSalt()));
			account.setFacePwd(FaceVerificationUtil.faceEncode(user.getPassword(), account.getSalt()));
		}
		this.accountService.update(account);

		this.userService.update(user);
		return tip;
	}

	/**
	 * 删除用户（逻辑删除）
	 */
	@RequestMapping("/delete")
	@BussinessLog(businessName = "删除用户", ignore = true)
	@ResponseBody
	public OpTip delete(HttpServletRequest request) {
		OpTip tip = new OpTip(200, "删除成功");
		String data = request.getParameter("data");
		JSONObject obj = null;
		JSONArray array = JSONArray.fromObject(data);
		List<String> userIds = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			userIds.add(obj.get("id").toString());
		}

		for (String userId : userIds) {
			// 不能删除超级管理员
			if (userId.equals(Const.ADMIN_ID)) {
				tip = new OpTip(201, "不能删除管理员");
				continue;
			}
			User user = this.userService.get(userId);
			user.getAccount().setStatus(ManagerStatus.DELETED);
			this.toolService.delDailyLimit(user.getAccountId());
			this.toolService.delExtraBoxNum(user.getAccountId());
			this.toolService.delNoticeMgr(user.getAccountId());
			this.userService.update(user);
		}

		return tip;
	}

	/**
	 * 跳转导入页面
	 */
	@RequestMapping("/uploadView")
	public String uploadView() {
		return PREFIX + "upload.html";
	}

	/**
	 * 用户导入
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/userUpload", method = RequestMethod.POST)
	@ResponseBody
	public OpTip uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			return new OpTip(201, "文件不能为空");
		}
		InputStream inputStream = file.getInputStream();
		OpTip tip = this.userService.getListByExcel(inputStream, file.getOriginalFilename());
		inputStream.close();
		return tip;
	}

	/**
	 * 上传图片(上传到项目的webapp/static/img)
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/upload")
	@ResponseBody
	public String upload(@RequestPart("file") MultipartFile picture) {
		String pictureName = UUID.randomUUID().toString() + ".jpg";
		try {
			String fileSavePath = dosthProperties.getFileTmpPath();
			picture.transferTo(new File(fileSavePath + pictureName));
		} catch (Exception e) {
			throw new BusinessException(BizExceptionEnum.UPLOAD_ERROR);
		}
		return pictureName;
	}

	/**
	 * @description 初始化借出权限树
	 * @return
	 */
	@RequestMapping("/initBorrowPopedomTree/{cabinetId}")
	@ResponseBody
	public List<ZTreeNode> initBorrowPopedomTree(@PathVariable String cabinetId) {
		return this.toolService.initBorrowPopedomTree(cabinetId);
	}

}