package com.cnbaosi.cabinet.serivce;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.modal.Setting;

/**
 * 系统设置
 * 
 * @author Yifeng Wang
 * @date 2021年9月12日
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
