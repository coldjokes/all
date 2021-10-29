package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dosth.common.db.entity.BasePojo;

/**
 * 系统设置
 * 
 * @author ZhongYan.He
 *
 */
@Entity
@SuppressWarnings("serial")
public class SystemSetup extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '参数名'")
	private String setupKey;

	@Column(columnDefinition = "varchar(36) COMMENT '参数值'")
	private String setupValue;

	public SystemSetup() {
		super();
	}

	public SystemSetup(String setupKey, String setupValue) {
		super();
		this.setupKey = setupKey;
		this.setupValue = setupValue;
	}

	public String getSetupKey() {
		return setupKey;
	}

	public void setSetupKey(String setupKey) {
		this.setupKey = setupKey;
	}

	public String getSetupValue() {
		return setupValue;
	}

	public void setSetupValue(String setupValue) {
		this.setupValue = setupValue;
	}

}
