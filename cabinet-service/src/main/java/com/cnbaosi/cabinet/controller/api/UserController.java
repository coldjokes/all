package com.cnbaosi.cabinet.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.UserCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.SortCriteria;
import com.cnbaosi.cabinet.entity.modal.User;
import com.cnbaosi.cabinet.serivce.UserService;
import com.google.common.collect.Lists;

import java.util.List;

/**
  * 用户
 * 
 * @author Yifeng Wang
 */

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {
	@Autowired
	private UserService userSvc;

	@LogRecord("增加用户")
	@PostMapping
	public RestFulResponse<String> addUser(@RequestBody User user) {
		return actionResult(userSvc.addUser(user));
	}
	
	@LogRecord("删除用户")
	@DeleteMapping("/{id}")
	public RestFulResponse<String> deleteUser(@PathVariable("id") String id) {
		return actionResult(userSvc.deleteUser(id));
	}
	
	@LogRecord("修改用户")
	@PutMapping
	public RestFulResponse<String> updateUser(@RequestBody User user) {
		return actionResult(userSvc.updateUser(user));
	}

	@LogRecord("查询用户")
	@GetMapping("/{id}")
	public RestFulResponse<User> getUser(@PathVariable String id) {
		return success(1, Lists.newArrayList(userSvc.getUserById(id)));
	}
	
	@LogRecord("查询用户列表")
	@GetMapping
	public RestFulResponse<User> getUsers(UserCriteria userCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria) {
		Page<User> pagedUsers = userSvc.getUsers(userCriteria, pageCriteria, sortCriteria);
		return success(pagedUsers.getTotal(), pagedUsers.getRecords());
	}

	@LogRecord("根据部门查询用户列表")
	@GetMapping("/byDepts")
	public RestFulResponse<User> getUserByDept(String deptId) {
		List<User> results = userSvc.getUserByDept(deptId);
		return success(results.size(), results);
	}

	@LogRecord("用户名校验")
	@GetMapping("/nameCheck")
	public boolean usernameCheck(@RequestParam String username) {
		User user = userSvc.getUserByName(username);
		return user == null;
	}
	
	@LogRecord("用户导入")
	@PostMapping("/import")
	public RestFulResponse<String> userUpload(@RequestParam("file") MultipartFile userFile) {
		String msg = userSvc.importUsers(userFile);
		if(msg == null) {
			return success("导入成功！");
		} else {
			return error("导入失败！" + msg);
		}
	}
	
	@LogRecord("用户密码修改")
	@PostMapping("/passUpdate")
	public RestFulResponse<String> passReset(@RequestBody User user) {
		String msg = userSvc.userPassUpdate(user);
		if(msg == null) {
			return success("密码修改成功！");
		} else {
			return error("密码修改失败！" + msg);
		}
	}
	
}
