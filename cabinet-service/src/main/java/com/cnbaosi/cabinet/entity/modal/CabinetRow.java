package com.cnbaosi.cabinet.entity.modal;

import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 行
 * 
 * @author Yifeng Wang  
 */

public class CabinetRow extends BaseModel<CabinetRow> {

	private static final long serialVersionUID = 1L;
	
	private String cabinetId; //柜子id
	private String name; //行名称
	@TableField(exist = false)
	private List<CabinetCell> cells; //每行格口
	private Integer sort; //序号
	
	public String getCabinetId() {
		return cabinetId;
	}
	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}
	public List<CabinetCell> getCells() {
		return cells;
	}
	public void setCells(List<CabinetCell> cells) {
		this.cells = cells;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}

