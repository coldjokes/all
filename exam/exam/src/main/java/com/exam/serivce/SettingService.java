package com.exam.serivce;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.exam.entity.modal.Setting;

/**
 * 系统设置
 * 
 * @author Yifeng Wang
 */
public interface SettingService extends IService<Setting> {

	/**
	 * 获取全部配置
	 */
	Map<String, Object> getAll();
	
	/**
	 * 获取某个值
	 */
	Object getValue(String key);
	
	/**
	 * 更新全部
	 */
	boolean updateAll(List<Setting> list);
}
