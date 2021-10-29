package com.dosth.admin.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dosth.admin.common.config.properties.DosthProperties;
import com.dosth.admin.common.exception.InvalidKaptchaException;
import com.dosth.admin.common.log.LogTaskFactory;
import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.User;
import com.dosth.admin.service.RelationService;
import com.dosth.admin.service.UserService;
import com.dosth.admin.util.ApiMenuFilter;
import com.dosth.admin.util.KaptchaUtil;
import com.dosth.admin.util.face.FaceVerificationUtil;
import com.dosth.common.cache.TokenManager;
import com.dosth.common.controller.BaseController;
import com.dosth.common.log.LogManager;
import com.dosth.common.node.MenuNode;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.support.HttpKit;
import com.dosth.common.util.CookieUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.common.util.ToolUtil;
import com.google.code.kaptcha.Constants;

/**
 * 登录
 * 
 * @author guozhidong
 *
 */
@Controller
public class LoginController extends BaseController {

	@Autowired
	private RelationService relationService;

	@Autowired
	private UserService userService;

	@Autowired
	private TokenManager tokenManager;

	@Autowired
	private DosthProperties dosthProperties;

	/**
	 * 跳转到主页
	 */
	@GetMapping("/")
	public String index(Model model) {
		List<String> roleList = ShiroKit.getAccount().getRoleList();
		if (roleList == null || roleList.size() == 0) {
			model.addAttribute("tips", "该用户没有权限,无法登录");
			model.addAttribute("logo", this.dosthProperties.getLogo());
			return "/login.html";
		}
		List<MenuNode> menus = this.relationService.getMenusByRoleIds(roleList);
		List<MenuNode> titles = MenuNode.buildTitle(menus);
		titles = ApiMenuFilter.build(titles);
		model.addAttribute("titles", titles);

		// 获取用户头像
		String accountId = ShiroKit.getAccount().getId();
		User user = this.userService.findUserByAccountId(accountId);
		model.addAttribute("avatar", user.getAvatar());
		return "/index.html";
	}

	/**
	 * 跳转到登录页面
	 */
	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		request.setAttribute("logo", this.dosthProperties.getLogo());
		if (ShiroKit.isAuthenticated() || ShiroKit.getAccount() != null) {
			return REDIRECT + "/";
		}
		return "/login.html";
	}

	/**
	 * 登录
	 */
	@PostMapping("/login")
	public String loginVali(Model model) {
		String loginName = super.getPara("loginName").trim();
		String password = super.getPara("password").trim();
		String remember = super.getPara("remember");
		model.addAttribute("logo", this.dosthProperties.getLogo());
		// 验证验证码是否正确
		if (KaptchaUtil.getKaptchaOnOff()) {
			String kaptcha = super.getPara("kaptcha").trim();
			String code = (String) super.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
			if (ToolUtil.isEmpty(kaptcha) || !kaptcha.equalsIgnoreCase(code)) {
				throw new InvalidKaptchaException();
			}
		}
		return this.login(loginName, password, remember);
	}

	/**
	 * 通过用户名、密码登录
	 * 
	 * @param loginName
	 *            登录名
	 * @param password
	 *            密码
	 * @param remember
	 *            记住我
	 * @return
	 */
	private String login(String loginName, String password, String remember) {

		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(loginName, password.toCharArray());
		token.setRememberMe("on".equals(remember));
		currentUser.login(token);
		ShiroAccount shiroAccount = ShiroKit.getAccount();
		String tokenId = CookieUtil.getCookie(super.getHttpServletRequest(), TokenManager.TOKEN);
		this.tokenManager.add(tokenId, shiroAccount);
		super.getSession().setAttribute("shiroAccount", shiroAccount);
		super.getSession().setAttribute("loginName", shiroAccount.getLoginName());

		LogManager.me().executeLog(LogTaskFactory.loginLog(shiroAccount.getId(), HttpKit.getIp()));

		ShiroKit.getSession().setAttribute("sessionFlag", true);
		return REDIRECT + "/";
	}

	/**
	 * 跳转到脸谱登录界面
	 */
	@RequestMapping(path = "/picLogin", method = RequestMethod.GET)
	public String picLogin() {
		return "/piclogin.html";
	}

	/**
	 * 脸谱登录
	 */
	@PostMapping("/loginPic")
	public String loginPic() {
		String base64ImgData = super.getPara("base64ImgData");
		String fileName = FileUtil.generatePngImage(base64ImgData, this.dosthProperties.getFileTmpPath());
		String accountId = FaceVerificationUtil.compare(this.dosthProperties.getFileTmpPath() + fileName,
				this.dosthProperties.getFacePath());
		Account account = this.userService.findUserByAccountId(accountId).getAccount();
		return this.login(account.getLoginName(),
				FaceVerificationUtil.faceDecode(account.getFacePwd(), account.getSalt()), "off");
	}

	/**
	 * 退出
	 */
	@GetMapping("/logout")
	public String logOut() {
		LogManager.me().executeLog(LogTaskFactory.exitLog(ShiroKit.getAccount().getId(), HttpKit.getIp()));
		ShiroKit.getSubject().logout();
		return REDIRECT + "/login";
	}
}