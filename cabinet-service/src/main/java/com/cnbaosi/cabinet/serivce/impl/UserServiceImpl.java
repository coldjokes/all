package com.cnbaosi.cabinet.serivce.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.AppConsts;
import com.cnbaosi.cabinet.entity.criteria.DeptMapCriteria;
import com.cnbaosi.cabinet.entity.criteria.UserCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.SortCriteria;
import com.cnbaosi.cabinet.entity.enums.RoleEnum;
import com.cnbaosi.cabinet.entity.enums.SourceEnum;
import com.cnbaosi.cabinet.entity.enums.StatusEnum;
import com.cnbaosi.cabinet.entity.modal.DeptMap;
import com.cnbaosi.cabinet.entity.modal.FaceFileBean;
import com.cnbaosi.cabinet.entity.modal.User;
import com.cnbaosi.cabinet.mapper.UserMapper;
import com.cnbaosi.cabinet.serivce.DeptMapService;
import com.cnbaosi.cabinet.serivce.FaceEngineService;
import com.cnbaosi.cabinet.serivce.ShiroService;
import com.cnbaosi.cabinet.serivce.UserService;
import com.cnbaosi.cabinet.util.DateTimeUtil;
import com.cnbaosi.cabinet.util.ExcelUtil;
import com.cnbaosi.cabinet.util.StringUtil;
import com.google.common.collect.Lists;

/**
 *  用户方法实现类
 *
 * @author Yifeng Wang  
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

	@Autowired
	private ShiroService shiroSvc;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private DeptMapService deptMapSvc;
	@Autowired
	private FaceEngineService faceEngineSvc;

	/**
	 * 新增用户
	 * @param user 用户实体
	 */
	@Override
	public boolean addUser(User user) {

		//移除之前已绑ic卡号
		unBindIcCard(user.getIcCard());

		user.setPassword(StringUtil.md5Password(user.getPassword()));
		user.setSource(SourceEnum.SYSTEM.getCode());

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
		String password = StringUtil.md5Password("000000");
		Integer role = RoleEnum.STAFF.getCode();
		Integer source = SourceEnum.FILE.getCode();
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
					user.setIcCard(icCard);
					user.setPassword(password);
					user.setRole(role);
					user.setSource(source);
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
		String password = user.getPassword();
		String icCard = user.getIcCard();

		//密码加密
//		if(StringUtils.isNotBlank(password)) {
//			user.setPassword(StringUtil.md5Password(password));
//		}

		//移除其他绑定该卡的用户
		unBindIcCard(icCard);

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
		//只能看到比自己权限低的用户
		User user = shiroSvc.getUser();
		Integer currentUserRole = user.getRole();
		wrapper.ge("role", currentUserRole);

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
	 * 根据部门查询用户列表
	 * @param deptId
	 * @return
	 */
	@Override
	public List<User> getUserByDept(String deptId) {
		UserCriteria matCriteria = new UserCriteria();
		PageCriteria pageCriteria = new PageCriteria();
		SortCriteria sortCriteria = new SortCriteria();

		if(deptId != null && !"#".equals(deptId) && !"-1".equals(deptId)) {
			DeptMapCriteria deptMapCriteria = new DeptMapCriteria();
			deptMapCriteria.setDeptId(deptId);
			List<DeptMap> mapList = deptMapSvc.getMapList(deptMapCriteria);
			if(CollectionUtils.isNotEmpty(mapList)) {
				List<String> userIdList = mapList.stream().map(x -> x.getUserId()).collect(Collectors.toList());
				matCriteria.setIdList(userIdList);
			} else {
				return Lists.newArrayList();
			}
		}

		Page<User> materialList = this.getUsers(matCriteria, pageCriteria, sortCriteria);
		return materialList.getRecords();
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
	 * 移除已绑定该ic卡的用户
	 * @param icCard ic卡号
	 * @return
	 */
	@Override
	public boolean unBindIcCard(String icCard) {
		if(StringUtils.isNotBlank(icCard)) {
			User bindedUser = new User();
			bindedUser.setIcCard("");
			EntityWrapper<User> wrapper = new EntityWrapper<>();
			wrapper.eq("ic_card", icCard);
			return super.update(bindedUser, wrapper);
		}
		return false;
	}

	/**
	 * TODO 对接外部数据源
	 * @return
	 */
	@Override
//	@DataSource(DataSourceEnum.OUTER)
	public List<User> getOuterList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取邮箱列表
	 */
	@Override
	public List<String> getEmailList() {
		return userMapper.getEmailList();
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
	 * 人脸注册
	 */
	@Override
	public boolean faceRegister(FaceFileBean faceBean) {
		try {

			byte[] decode = Base64.decode(base64Process(faceBean.getFile()));
			ImageInfo imageInfo = ImageFactory.getRGBData(decode);

			// 人脸特征获取
			byte[] faceFeature;
			faceFeature = faceEngineSvc.extractFaceFeature(imageInfo);
			if (faceFeature == null) {
				return false;
			}

			// 清除已有此人脸特征的用户信息
			try {
				List<User> userFaceInfoList = faceEngineSvc.compareFaceFeature(faceFeature);
				if (CollectionUtils.isNotEmpty(userFaceInfoList)) {
					for (User faceUserInfo : userFaceInfoList) {
						User userInfo = this.getUserById(faceUserInfo.getId());
						userInfo.setFaceFeature(null);
						super.updateById(userInfo);
					}
				}
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			// 根据账户增加人脸特征
			User user = this.getUserById(faceBean.getUserId());
			user.setFaceFeature(faceFeature);
			super.updateById(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 人脸登陆
	 */
	@Override
	public User faceLogin(FaceFileBean faceBean) {
		User user = null;
		try {
			byte[] decode = Base64.decode(base64Process(faceBean.getFile()));
			ImageInfo imageInfo = ImageFactory.getRGBData(decode);
			// 人脸特征获取
			byte[] bytes = faceEngineSvc.extractFaceFeature(imageInfo);
			if (bytes != null) {
				// 人脸比对，获取比对结果
				List<User> userFaceInfoList = faceEngineSvc.compareFaceFeature(bytes);
				if (CollectionUtils.isNotEmpty(userFaceInfoList)) {
					user = this.getUserById(userFaceInfoList.get(0).getId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	private String base64Process(String base64Str) {
		if (!StringUtils.isEmpty(base64Str)) {
			String photoBase64 = base64Str.substring(0, 30).toLowerCase();
			int indexOf = photoBase64.indexOf("base64,");
			if (indexOf > 0) {
				base64Str = base64Str.substring(indexOf + 7);
			}

			return base64Str;
		} else {
			return "";
		}
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

