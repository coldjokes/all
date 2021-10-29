package com.cnbaosi.cabinet.entity.modal;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * 柜体（主板）
 * 
 * @author Yifeng Wang  
 */

public class Cabinet extends BaseModel<Cabinet> {

	private static final long serialVersionUID = 1L;

	private String name; //柜体名称
	private String computerId; //主机id
	private String computerName; //主机名称
	private String computerPort; //主机端口
	private int computerBaudRate; //主机波特率
	@TableField(exist = false)
	private List<CabinetRow> rows; //行列表
	
	private Integer sort; //序号

	private Date updateTime; //更新时间
	private Date deleteTime; //删除时间
	
	public Cabinet() {
		super();
	}
	public Cabinet(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComputerName() {
		return computerName;
	}
	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	public String getComputerId() {
		return computerId;
	}

	public void setComputerId(String computerId) {
		this.computerId = computerId;
	}


	public List<CabinetRow> getRows() {
		return rows;
	}

	public void setRows(List<CabinetRow> rows) {
		this.rows = rows;
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
	public String getComputerPort() {
		return computerPort;
	}
	public void setComputerPort(String computerPort) {
		this.computerPort = computerPort;
	}
	public int getComputerBaudRate() {
		return computerBaudRate;
	}
	public void setComputerBaudRate(int computerBaudRate) {
		this.computerBaudRate = computerBaudRate;
	}
}
