package com.exam.controller;

import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.entity.RestFulResponse;
import com.exam.entity.modal.User;
import com.exam.serivce.UserService;

@RestController
@RequestMapping("/test")
public class TestController extends BaseController{

	@Autowired
	private UserService userSvc;
	
	@GetMapping("/1")
	public String get1() throws MessagingException {
		return "1";
	}
	
	@GetMapping("/3")
	public RestFulResponse<User> get3() throws Exception{
		List<User> list = userSvc.getAll();
		return success(list.size(), list);
	}
}
