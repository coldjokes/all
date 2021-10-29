package com.dosth.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dosth.common.db.entity.BasePojo;

/**
 * 字典表
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class Dict extends BasePojo {

	@Column(columnDefinition = "int(11) COMMENT '排序'")
	private Integer num;

	@Column(columnDefinition = "varchar(255) COMMENT '父级ID'")
	private String pId;

	@Column(columnDefinition = "varchar(50) COMMENT '名称'")
	private String name;

	@Column(columnDefinition = "varchar(50) COMMENT '提示'")
	private String tips;

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getpId() {
		return this.pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTips() {
		return this.tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	@Override
	public String toString() {
		return "Dict{" + "id=" + id + ", num=" + num + ", pId=" + pId + ", name=" + name + ", tips=" + tips + "}";
	}
}