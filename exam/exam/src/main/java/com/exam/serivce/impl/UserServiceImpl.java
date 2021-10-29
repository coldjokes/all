package com.exam.serivce.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.exam.entity.AppConsts;
import com.exam.entity.criteria.UserCriteria;
import com.exam.entity.criteria.common.PageCriteria;
import com.exam.entity.criteria.common.SortCriteria;
import com.exam.entity.enums.StatusEnum;
import com.exam.entity.modal.User;
import com.exam.mapper.UserMapper;
import com.exam.serivce.UserService;
import com.exam.util.DateTimeUtil;
import com.exam.util.ExcelUtil;
import com.exam.util.StringUtil;
import com.google.common.collect.Lists;

/**
 *  用户方法实现类
 *
 * @author Yifeng Wang  
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * 新增用户
	 * @param user 用户实体
	 */
	@Override
	public boolean addUser(User user) {
		user.setPassword(StringUtil.md5Password(user.getPassword()));

		if(StringUtils.isBlank(user.getEmail())) {
			user.setEmail(null);
		}

		return super.insert(user);
	}

	/**
	 * 批量新增用户
	 * @param userList 用户实体
	 */
	@Override
	public boolean addUsers(List<User> userList) {
		return super.insertBatch(userList, userList.size());
	}

	/**
	 * 处理上传文件
	 * @param file
	 */
	@Override
	public String importUsers(MultipartFile file) {

		List<User> importUserList = Lists.newArrayList();
		List<List<Object>> results = ExcelUtil.executeExcel(file);

		//导入默认值设定
		String password = StringUtil.md5Password(AppConsts.DEFAULT_PASSWORD_USER);
		Integer status = StatusEnum.ON.getCode();

		String msg = null;
		if(CollectionUtils.isNotEmpty(results)) {

			// 编译正则表达式
			Pattern pattern = Pattern.compile(AppConsts.STRING_REG_EX);
			for(List<Object> cellList : results) {

				//excel取值
				String username = String.valueOf(cellList.get(0));
				String fullname = String.valueOf(cellList.get(1));
				String icCard = String.valueOf(cellList.get(2));

				//空值判断
				username = AppConsts.NULL.equals(username) ? null : username;
				fullname = AppConsts.NULL.equals(fullname) ? null : fullname;
				icCard = AppConsts.NULL.equals(icCard) ? null : icCard;

				if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(fullname)) {
					User user = this.getUserByName(username);
					if(user != null) {
						msg = "已存在用户名相同用户！";
						break;
					}

					if(username.length() > 12) {
						msg = "用户名长度不得超过12位！【" + username + "】";
						break;
					}

					Matcher usernameMatcher = pattern.matcher(username);
					if(!usernameMatcher.matches()) {
						msg = "用户名【" + username + "】不符合规范，只能包括中文字、英文字母、数字和下划线！";
						break;
					}

					if(fullname.length() > 12) {
						msg = "姓名长度不得超过12位！【" + fullname + "】";
						break;
					}
					Matcher fullnameMatcher = pattern.matcher(fullname);
					if(!fullnameMatcher.matches()) {
						msg = "姓名【" + fullname + "】不符合规范，只能包括中文字、英文字母、数字和下划线！";
						break;
					}

					if(StringUtils.isNotBlank(icCard) && icCard.length() > 12) {
						msg = "IC卡号长度不得超过12位！【" + icCard + "】";
						break;
					}

					user = new User();
					user.setUsername(username);
					user.setFullname(fullname);
					user.setPassword(password);
					user.setStatus(status);

					importUserList.add(user);
				} else {
					msg = "必填字段存在空值！";
					break;
				}
			}
		} else {
			msg = "未检测到相关数据！";
		}

		//入库
		if(msg == null) {
			this.addUsers(importUserList);
		}
		return msg;
	}

	/**
	 * 删除用户
	 * @param id 用户id
	 * @return
	 */
	@Override
	public boolean deleteUser(String id) {
		User user = new User(id);
		user.setDeleteTime(DateTimeUtil.now());
		return super.updateById(user);
	}

	/**
	 * 更新用户
	 * @param user 用户实体
	 * @return
	 */
	@Override
	public boolean updateUser(User user) {


		//密码加密
//		String password = user.getPassword();
//		if(StringUtils.isNotBlank(password)) {
//			user.setPassword(StringUtil.md5Password(password));
//		}

		user.setUpdateTime(DateTimeUtil.now());
		boolean updateUserResult = super.updateById(user);
		return updateUserResult;
	}

	@Override
	public User getUserById(String id) {
		return super.selectById(id);
	}

	/**
	 * 根据id获取用户
	 * @param id
	 * @return
	 */
	@Override
	public Page<User> getUsers(UserCriteria userCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria) {

		String text = userCriteria.getText();
		String username = userCriteria.getUsername();
		Integer role = userCriteria.getRole();
		Integer source = userCriteria.getSource();
		Integer status = userCriteria.getStatus();
		String icCard = userCriteria.getIcCard();
		String fullname = userCriteria.getFullname();

		Integer curPage = pageCriteria.getCurPage();
		Integer pageSize = pageCriteria.getPageSize();

		String sortField = sortCriteria.getSortField();
		String sortOrder = sortCriteria.getSortOrder();

		List<String> idList = userCriteria.getIdList();

		EntityWrapper<User> wrapper = new EntityWrapper<>();

		if(CollectionUtils.isNotEmpty(idList)) {
			wrapper.in("id", idList);
		}

		if(StringUtils.isNotBlank(text)) { //多字段匹配查找
			text = text.trim();
			wrapper.like("username", text).or().like("fullname", text).or().like("ic_card", text);
		}
		wrapper.andNew();

		if(StringUtils.isNotBlank(username)) {
			wrapper.like("username", username);
		}

		if(role != null && role != -1) {
			wrapper.eq("role", role);
		}
		if(source != null && source != -1) {
			wrapper.eq("source", source);
		}
		if(status != null && status != -1) {
			wrapper.eq("status", status);
		}
		if(StringUtils.isNotBlank(icCard)) {
			wrapper.like("ic_card", icCard);
		}
		if(StringUtils.isNotBlank(fullname)) {
			wrapper.like("fullname", fullname);
		}
		if(sortField != null) {
			wrapper.orderBy(StringUtil.camelToUnderline(sortField), sortOrder.equals(SortCriteria.AES));
		} else {
			wrapper.orderBy("create_time", false);
		}

		wrapper.isNull("delete_time");

		Page<User> page = new Page<>(curPage, pageSize);
		return super.selectPage(page, wrapper);
	}


	/**
	 * 根据用户名查找用户
	 * @param username 用户名
	 * @return
	 */
	@Override
	public User getUserByName(String username) {
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.eq("username", username);
		wrapper.isNull("delete_time");
		return super.selectOne(wrapper);
	}

	/**
	 * 通过ic卡号获取用户
	 * @param icCard
	 * @return
	 */
	@Override
	public User getUserByIcCard(String icCard) {
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.eq("ic_card", icCard);
		wrapper.isNull("delete_time");
		return super.selectOne(wrapper);
	}

	/**
	 * @return
	 */
	@Override
//	@DataSource(DataSourceEnum.OUTER)
	public List<User> getOuterList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 重置用户密码
	 */
	@Override
	public String userPassUpdate(User user) {

		String msg = null;
		String oldPassword = user.getOldPassword();
		if(StringUtils.isNotBlank(oldPassword)) {
			User dbUser = this.getUserById(user.getId());
			if(!StringUtil.md5Password(oldPassword).equals(dbUser.getPassword())) {
				msg = "原密码输入错误！";
			}
		}

		if(StringUtils.isBlank(msg)) {
			user.setPassword(StringUtil.md5Password(user.getPassword()));
			super.updateById(user);
		}
		return msg;
	}

	/**
	 * 获取所有用户
	 */
	@Override
	public List<User> getAll() {
		EntityWrapper<User> wrapper = new EntityWrapper<>();
		wrapper.isNull("delete_time");
		return super.selectList(wrapper);
	}
}

