package com.exam.entity.modal;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;

/**
 * 系统设置实体类
 * 
 * @author Yifeng Wang
 */

public class Setting extends Model<Setting> {
	
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String name; //键
	
	private Object value; //值
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	protected Serializable pkVal() {
		return this.name;
	}

}
