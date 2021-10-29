package com.exam.serivce;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.exam.entity.criteria.UserCriteria;
import com.exam.entity.criteria.common.PageCriteria;
import com.exam.entity.criteria.common.SortCriteria;
import com.exam.entity.modal.User;

/**
 * 用户相关方法
 * 
 * @author Yifeng Wang  
 */
public interface UserService extends IService<User> {

	/**
	 * 新增用户
	 * @param user 用户实体
	 */
	boolean addUser(User user);
	
	/**
	 * 批量新增用户
	 * @param userList 用户实体
	 */
	boolean addUsers(List<User> userList);

	/**
	 * 处理上传文件
	 * @param file
	 */
	String importUsers(MultipartFile file);
	
	/**
	 * 删除用户
	 * @param id 用户id
	 * @return
	 */
	boolean deleteUser(String id);

	/**
	 * 更新用户
	 * @param user 用户实体
	 * @return
	 */
	boolean updateUser(User user);

	/**
	 * 根据id获取用户
	 * @param id
	 * @return
	 */
	User getUserById(String id);
	
	/**
	 * 查找用户
	 * @param userCriteria 用户相关条件过滤
	 * @param pageCriteria 分页过滤
	 * @param sortCriteria 排序过滤
	 */
	Page<User> getUsers(UserCriteria userCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria);

	/**
	 * 根据用户名查找用户
	 * @param username 用户名
	 * @return
	 */
	User getUserByName(String username);
	
	/**
	 * 通过ic卡号获取用户
	 * @param icCard
	 * @return
	 */
	User getUserByIcCard(String icCard);

	/**
	 * TODO 对接外部数据源
	 * @return
	 */
	List<User> getOuterList();

	/**
	 * 重置用户密码
	 */
	String userPassUpdate(User user);

	/**
	 * 获取所有用户
	 */
	List<User> getAll();
}

