package com.cnbaosi.cabinet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cnbaosi.cabinet.serivce.ShiroService;

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

	@GetMapping("/user/dept")
	public String userDept() {
		return "user/dept";
	}

	@GetMapping("/cabinet/computer")
	public String computerList() {
		return "cabinet/computer";
	}
	@GetMapping("/cabinet/cabinet")
	public String cabinetList() {
		return "cabinet/cabinet";
	}
	
	@GetMapping("/report/matStatus")
	public String metStatus() {
		return "report/matStatus";
	}
	@GetMapping("/report/matBillRecord")
	public String matBillRecord() {
		return "report/matBillRecord";
	}
	
	@GetMapping("/material/list")
	public String materialList() {
		return "material/list";
	}
	
	@GetMapping("/material/category")
	public String materialCategory() {
		return "material/category";
	}

	@GetMapping("/material/jurisdiction")
	public String materialJurisdiction() {
		return "material/jurisdiction";
	}

	@GetMapping("/log/cabinetLog")
	public String cabinetLog() {
		return "log/cabinetLog";
	}
	
	@GetMapping("/log/serviceLog")
	public String serviceLog() {
		return "log/serviceLog";
	}
	@GetMapping("/setting/list")
	public String settingList() {
		return "setting/list";
	}
	@GetMapping("/remind/list")
	public String remindList() {
		return "remind/list";
	}
}

