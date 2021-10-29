package com.exam.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.exam.serivce.ShiroService;

/**
 *  页面路由
 * 
 * @author Yifeng Wang  
 */
@Controller
public class RouterController {

	@Autowired
	private ShiroService shiroSvc;
	
	@GetMapping("/")
	public String index() {
		if(shiroSvc.getUser() != null) {
			return "index";
		} else {
			return "login";
		}
	}
	
	@GetMapping("/home")
    public String home() {
        return "home";
    }
	
	@GetMapping("/user/list")
	public String userList() {
		return "user/list";
	}
	
	@GetMapping("/log/serviceLog")
	public String serviceLog() {
		return "log/serviceLog";
	}
	@GetMapping("/setting/list")
	public String settingList() {
		return "setting/list";
	}
}

