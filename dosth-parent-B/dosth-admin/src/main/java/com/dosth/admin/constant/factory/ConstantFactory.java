package com.dosth.admin.constant.factory;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.Dept;
import com.dosth.admin.entity.Dict;
import com.dosth.admin.entity.Menu;
import com.dosth.admin.entity.Notice;
import com.dosth.admin.entity.Roles;
import com.dosth.admin.entity.SystemInfo;
import com.dosth.admin.entity.User;

import com.dosth.admin.repository.AccountRepository;
import com.dosth.admin.repository.AccountRoleRepository;
import com.dosth.admin.repository.DeptRepository;
import com.dosth.admin.repository.DictRepository;
import com.dosth.admin.repository.MenuRepository;
import com.dosth.admin.repository.NoticeRepository;
import com.dosth.admin.repository.RoleRepository;
import com.dosth.admin.repository.SystemInfoRepository;
import com.dosth.admin.repository.UserRepository;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.support.StrKit;
import com.dosth.common.util.Convert;
import com.dosth.common.util.SpringContextHolder;
import com.dosth.common.util.ToolUtil;

/**
 * 常量的生产工厂
 * 
 * @author guozhidong
 *
 */
@Component
@DependsOn("springContextHolder")
public class ConstantFactory implements IConstantFactory {

	private AccountRepository accountRepository = SpringContextHolder.getBean(AccountRepository.class);
	private UserRepository userRepository = SpringContextHolder.getBean(UserRepository.class);
	private RoleRepository roleRepository = SpringContextHolder.getBean(RoleRepository.class);
	private DeptRepository deptRepository = SpringContextHolder.getBean(DeptRepository.class);
	private DictRepository dictRepository = SpringContextHolder.getBean(DictRepository.class);
	private MenuRepository menuRepository = SpringContextHolder.getBean(MenuRepository.class);
	private NoticeRepository noticeRepository = SpringContextHolder.getBean(NoticeRepository.class);
	private SystemInfoRepository systemInfoRepository = SpringContextHolder.getBean(SystemInfoRepository.class);
	private AccountRoleRepository accountRoleRepository = SpringContextHolder.getBean(AccountRoleRepository.class);

	public static IConstantFactory me() {
		return SpringContextHolder.getBean("constantFactory");
	}
	
	@Override
	public String getLoginNameById(String userId) {
		Account account = this.accountRepository.getOne(userId);
		if (account != null) {
			return account.getLoginName();
		}
		return "--";
	}

	@Override
	public String getUserAccountById(String userId) {
		User user = this.userRepository.findUserByAccountId(userId);
		if (user != null && user.getAccount() != null) {
			return user.getAccount().getLoginName();
		}
		return "--";
	}

	@Override
	public String getRoleName(String accountId) {
		List<String> roleIdList = this.accountRoleRepository.getRoleIdListByAccountId(accountId);
		StringBuilder sb = new StringBuilder();
		Roles roleObj = null;
		for (String roleId : roleIdList) {
			roleObj = this.roleRepository.getOne(roleId);
			if (ToolUtil.isNotEmpty(roleObj) && ToolUtil.isNotEmpty(roleObj.getName())) {
				sb.append(roleObj.getName()).append(",");
			}
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}

	@Override
	public String getSingleRoleName(String roleId) {
		if (roleId == null || "0".equals(roleId)) {
			return "--";
		}
		Roles role = this.roleRepository.getOne(roleId);
		if (ToolUtil.isNotEmpty(role) && ToolUtil.isNotEmpty(role.getName())) {
			return role.getName();
		}
		return "";
	}

	@Override
	public String getSingleRoleTip(String roleId) {
		if (roleId == null || "0".equals(roleId)) {
			return "--";
		}
		Roles role = this.roleRepository.getOne(roleId);
		if (ToolUtil.isNotEmpty(role) && ToolUtil.isNotEmpty(role.getTips())) {
			return role.getTips();
		}
		return "";
	}

	@Override
	public String getDeptName(String deptId) {
		Dept dept = this.deptRepository.findOne(deptId);
		if (ToolUtil.isNotEmpty(dept) && ToolUtil.isNotEmpty(dept.getDeptName())) {
			return dept.getDeptName();
		}
		return "";
	}
	
	@Override
	public String getSystemName(String id) {
		SystemInfo systemInfo = this.systemInfoRepository.getOne(id);
		if (ToolUtil.isNotEmpty(systemInfo) && ToolUtil.isNotEmpty(systemInfo.getSystemName())) {
			return systemInfo.getSystemName();
		}
		return "";
	}

	@Override
	public String getMenuNames(String menuIds) {
		Long[] ids = Convert.toLongArray(true, menuIds);
		StringBuilder sb = new StringBuilder();
		Menu menu = null;
		for (Long id : ids) {
			menu = this.menuRepository.getOne(id);
			if (ToolUtil.isNotEmpty(menu) && ToolUtil.isNotEmpty(menu.getName())) {
				sb.append(menu.getName()).append(",");
			}
		}
		return StrKit.removeSuffix(sb.toString(), ",");
	}

	@Override
	public String getMenuName(String menuId) {
		if (ToolUtil.isEmpty(menuId)) {
			return "";
		} else {
			Menu menu = this.menuRepository.getOne(menuId);
			if (menu == null) {
				return "";
			}
			return menu.getName();
		}
	}

	@Override
	public String getMenuNameByCode(String code) {
		if (ToolUtil.isEmpty(code)) {
			return "";
		} else {
			List<Menu> menuList = this.menuRepository.findMenuListByCode(code);
			if (ToolUtil.isEmpty(menuList)) {
				return "";
			}
			return menuList.get(0).getName();
		}
	}

	@Override
	public String getDictName(String dictId) {
		if (ToolUtil.isEmpty(dictId)) {
			return "";
		} else {
			Dict dict = this.dictRepository.getOne(dictId);
			if (dict == null) {
				return "";
			}
			return dict.getName();
		}
	}

	@Override
	public String getNoticeTitle(String dictId) {
		if (ToolUtil.isEmpty(dictId)) {
			return "";
		} else {
			Notice notice = this.noticeRepository.getOne(dictId);
			if (notice == null) {
				return "";
			}
			return notice.getTitle();
		}
	}

	@Override
	public String getDictsByName(String name, Integer val) {
		List<Dict> dictList = this.dictRepository.findDictListByName(name);
		if (ToolUtil.isEmpty(dictList)) {
			return "";
		} else {
			for (Dict dict : dictList) {
				if (dict.getNum() != null && val.equals(dict.getNum())) {
					return dict.getName();
				}
			}
			return "";
		}
	}

	@Override
	public List<Dict> findInDict(String id) {
		if (ToolUtil.isEmpty(id)) {
			return null;
		} else {
			List<Dict> dicts = this.dictRepository.findListByPid(id);
			if (dicts == null || dicts.size() == 0) {
				return null;
			}
			return dicts;
		}
	}

	@Override
	public String getCacheObject(String para) {
		return LogObjectHolder.me().get().toString();
	}

	@Override
	public List<String> getSubDeptId(String deptId) {
		List<Dept> depts = this.deptRepository.findDeptListByDeptId(deptId);
        List<String> deptids = new ArrayList<>();
        if(depts != null && depts.size() > 0){
            for (Dept dept : depts) {
                deptids.add(dept.getId());
            }
        }
        return deptids;
	}

	@Override
	public List<String> getParentDeptIds(String deptId) {
		Dept dept = this.deptRepository.getOne(deptId);
        String pids = dept.getFullIds();
        String[] split = pids.split(",");
        List<String> parentDeptIds = new ArrayList<>();
        for (String s : split) {
            parentDeptIds.add(StrKit.removeSuffix(StrKit.removePrefix(s, "["), "]"));
        }
        return parentDeptIds;
	}

}