package com.exam.serivce.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.exam.entity.enums.BaseEnum;
import com.exam.entity.enums.LogTypeEnum;
import com.exam.entity.enums.RoleEnum;
import com.exam.entity.enums.StatusEnum;
import com.exam.entity.modal.Dict;
import com.exam.serivce.DictService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 字典方法实现类
 * 
 * @author Yifeng Wang  
 */
@Service
public class DictServiceImpl implements DictService{

	public static final Map<String, BaseEnum[]> ENUM_MAP = new HashMap<String, BaseEnum[]>() {
		private static final long serialVersionUID = 1L;
		{
			put("logType", LogTypeEnum.values());
			put("role", RoleEnum.values());
			put("status", StatusEnum.values());
		}
	};
	
	@Override
	public Map<String, List<Dict>> getDicts() {
		Map<String, List<Dict>> result = Maps.newHashMap();
		for(Entry<String, BaseEnum[]> entry : ENUM_MAP.entrySet()) {
			String ename = entry.getKey();
			BaseEnum[] enumArr = entry.getValue();
			List<Dict> dictList = Lists.newArrayList();
			for(BaseEnum be : enumArr) {
				Dict dict = new Dict();
				dict.setCode(be.getCode());
				dict.setText(be.getText());
				dictList.add(dict);
			}
			result.put(ename, dictList);
		}
		return result;
	}
	
    @Override
    public List<Dict> getList(String ename) {
    	Map<String, List<Dict>> dictMap = getDicts();
    	return dictMap.get(ename);
    }
	
    @Override
    public String getText(String ename, Integer code) {
        for (Dict d : this.getList(ename)) {
            if (d.getCode().equals(code)) {
                return d.getText();
            }
        }
        return null;
    }

    @Override
    public Integer getCode(String ename, String text) {
    	for (Dict d : this.getList(ename)) {
    		if (d.getText().equals(text)) {
    			return d.getCode();
    		}
    	}
    	return null;
    }
}

