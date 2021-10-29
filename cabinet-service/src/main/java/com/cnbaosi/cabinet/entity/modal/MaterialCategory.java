package com.cnbaosi.cabinet.entity.modal;

import java.util.Date;

/**
 * 类别
 * 
 * @author Yifeng Wang  
 */

public class MaterialCategory extends BaseModel<MaterialCategory> {
	
	private static final long serialVersionUID = 1L;
	
	private String text; //类别
	private String pId; //父类id
	private Date updateTime; //更新时间
	private Date deleteTime; //删除时间
	
	public MaterialCategory() {
		super();
	}
	public MaterialCategory(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getDeleteTime() {
		return deleteTime;
	}
	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
}

