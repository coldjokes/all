package com.cnbaosi.workspace.spring;

/**
 * @description 门
 * @author guozhidong
 *
 */
public class Door {
	// 当前介质名称
	private String mediumName;
	// 当前部分门的名称
	private String doorName;
	// 门高
	private Integer doorHeight;
	// 是否需要开门,默认已开
	private Boolean open = true;
	// 门是否关上,默认开启状态
	private Boolean closed = false;
	// 校验状态
	private Boolean check = false;

	public Door(String mediumName, String doorName, Integer doorHeight) {
		this.mediumName = mediumName;
		this.doorName = doorName;
		this.doorHeight = doorHeight;
	}

	public String getMediumName() {
		return this.mediumName;
	}

	public void setMediumName(String mediumName) {
		this.mediumName = mediumName;
	}

	public String getDoorName() {
		return this.doorName;
	}

	public void setDoorName(String doorName) {
		this.doorName = doorName;
	}

	public Integer getDoorHeight() {
		return this.doorHeight;
	}

	public void setDoorHeight(Integer doorHeight) {
		this.doorHeight = doorHeight;
	}

	public Boolean getOpen() {
		return this.open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getClosed() {
		return this.closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public Boolean getCheck() {
		return this.check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}
}