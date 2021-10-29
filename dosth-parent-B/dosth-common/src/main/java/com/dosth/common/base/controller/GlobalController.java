package com.dosth.common.base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 全局的控制器
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/global")
public class GlobalController {

	/**
	 * 跳转到404页面
	 * 
	 * @return
	 */
	@RequestMapping(path = "/error")
	public String errorPage() {
		return "/timeOut.html";
	}

	/**
	 * 跳转到session超时页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(path = "/sessionError")
	public String errorPageInfo(Model model) {
		model.addAttribute("tips", "session超时");
		return "/login.html";
	}
}