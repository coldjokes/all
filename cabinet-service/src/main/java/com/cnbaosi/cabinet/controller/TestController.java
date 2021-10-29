package com.cnbaosi.cabinet.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.modal.User;
import com.cnbaosi.cabinet.serivce.StockService;
import com.cnbaosi.cabinet.serivce.UserService;
import com.cnbaosi.cabinet.util.EmailUtil;
import com.google.common.collect.Lists;

@RestController
@RequestMapping("/test")
public class TestController extends BaseController{

	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private UserService userSvc;
	@Autowired
	private StockService stockSvc;
	
	@GetMapping("/1")
	public String get1() throws MessagingException {
		List<String> emailList = Lists.newArrayList();

		emailList.add("yifeng.wang@cnbaosi.com");
		
		String materialName = "车刀片";
		String materialNo = "no.0001";
		String materialSpec = "9asd8sf9";
		Integer totalAmount = 1;
		Integer warnVal = 2;
		
		String emailSubject = "库存预警通知";
		
		String emailContent = 
				"<p>尊敬的管理员：<br/><br/>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;工具柜中<b>" + materialName + "</b>（编号："+ materialNo +" 规格：" + materialSpec + "）库存已不足。"
								+ "当前库存为 <b>" + totalAmount + "</b> ，预警库存为 <b>" + warnVal + "</b> ，请及时安排补料。<br/><br/>"
						+ "&nbsp;&nbsp;&nbsp;&nbsp;附件为库存清单，请查收。"
						+ "</p>";
		
		String fileName = "库存清单";
			
		
		try {
			emailUtil.sendEmail(emailSubject, emailContent, emailList, "库存预警通知", null);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return "1";
	}
	
	@GetMapping("/2")
	public String get2() throws MessagingException {
		
		stockSvc.checkStockIfNeedEmail("bca4f89a22834a8d9f091391507902c1");
		
		return "1";
	}
	@PostMapping("/face")
	public String get2(@RequestParam("file") MultipartFile file) throws Exception{
		System.out.println(file);
		String fileFullName = file.getOriginalFilename();
		System.out.println(fileFullName);
		file.getBytes();
		
		return "face";
	}
	@GetMapping("/3")
	public RestFulResponse<User> get3() throws Exception{
		List<User> list = userSvc.getAll();
		return success(list.size(), list);
	}
}
