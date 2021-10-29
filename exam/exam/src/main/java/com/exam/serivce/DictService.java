package com.exam.serivce;

import java.util.List;
import java.util.Map;

import com.exam.entity.modal.Dict;

/**
 * 字典相关方法
 * 
 * @author Yifeng Wang  
 */

public interface DictService{

	/**
	 * 获取所有字典信息
	 * @return
	 */
	Map<String, List<Dict>> getDicts();
	
	/**
	 * 根据字典名获取字典
	 * @param ename
	 * @return
	 */
    List<Dict> getList(String ename);
    
    /**
     * 根据字典名、代码获取中文
     * @param ename
     * @param code
     * @return
     */
    String getText(String ename, Integer code);

    /**
     * 根据字典名、中文获取代码
     * @param ename
     * @param text
     * @return
     */
    Integer getCode(String ename, String text);
}

