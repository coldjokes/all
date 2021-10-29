package com.cnbaosi.cabinet.entity.modal;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 主机
 * 
 * @author Yifeng Wang  
 */

public class Computer extends BaseModel<Computer> {
	
	private static final long serialVersionUID = 1L;
	
	private String name; //主机名称
	private String identifyCode; //识别码
	private String cellPrefix; //格口前缀
	
	@TableField(exist = false)
	private List<Cabinet> cabinetList;// 柜体列表
	
	private Integer sort; //序号
	
	private Date updateTime; //更新时间
	private Date deleteTime; //删除时间
	
	public Computer() {
		super();
	}
	public Computer(String id) {
		this.id = id;
	}
	public String getIdentifyCode() {
		return identifyCode;
	}

	public void setIdentifyCode(String identifyCode) {
		this.identifyCode = identifyCode;
	}

	public List<Cabinet> getCabinetList() {
		return cabinetList;
	}

	public void setCabinetList(List<Cabinet> cabinetList) {
		this.cabinetList = cabinetList;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getCellPrefix() {
		return cellPrefix;
	}
	public void setCellPrefix(String cellPrefix) {
		this.cellPrefix = cellPrefix;
	}
}

