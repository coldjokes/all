package com.exam.serivce.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.exam.entity.modal.Setting;
import com.exam.mapper.SettingMapper;
import com.exam.serivce.SettingService;
import com.google.common.collect.Maps;

/**
 * 系统设置
 * 
 * @author Yifeng Wang
 */
@Service
public class SettingServiceImpl extends ServiceImpl<SettingMapper, Setting> implements SettingService {

	@Override
	public Map<String, Object> getAll() {
		EntityWrapper<Setting> wrapper = new EntityWrapper<>();
		List<Setting> list = super.selectList(wrapper);
		Map<String, Object> map = Maps.newHashMap();
		if(CollectionUtils.isNotEmpty(list)) {
			for(Setting setting : list) {
				map.put(setting.getName(), setting.getValue());
			}
		}
		return map;
	}

	@Override
	public Object getValue(String key) {
		return this.getAll().get(key);
	}

	@Override
	public boolean updateAll(List<Setting> list) {
		return super.updateAllColumnBatchById(list);
	}
	
}
