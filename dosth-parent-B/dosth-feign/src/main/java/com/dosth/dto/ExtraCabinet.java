package com.dosth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @description 扩展的柜体信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ExtraCabinet implements Serializable {
	// 柜子序列号
	private String serialNo;
	// 柜子Id
	private String cabinetId;
	// 被附属的柜子Id
	private String parentCabinetId;
	// 柜体名称
	private String cabinetName;
	// 柜子栈号
	private Integer boardNo = 0;
	// 门
	private String door;
	// 门高度
	private Integer doorHeight = 406;
	// 柜体类型
	private String cabinetType;
	// 最高层
	private Integer topLevel;
	// 最高层高
	private Integer topHeight;

	// 取料集合
	private List<Card> cardList;

	public ExtraCabinet() {
	}

	public ExtraCabinet(String serialNo, String cabinetId, String parentCabinetId, String cabinetName, Integer boardNo,
			String door, Integer doorHeight, String cabinetType) {
		super();
		this.serialNo = serialNo;
		this.cabinetId = cabinetId;
		this.parentCabinetId = parentCabinetId;
		this.cabinetName = cabinetName;
		this.boardNo = boardNo;
		this.door = door;
		this.doorHeight = doorHeight;
		this.cabinetType = cabinetType;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getCabinetId() {
		return this.cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public String getParentCabinetId() {
		return this.parentCabinetId;
	}

	public void setParentCabinetId(String parentCabinetId) {
		this.parentCabinetId = parentCabinetId;
	}

	public String getCabinetName() {
		if (this.cabinetName == null || "".equals(this.cabinetName)) {
			this.cabinetName = "未命名柜";
		}
		return this.cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

	public Integer getBoardNo() {
		if (this.boardNo == null) {
			this.boardNo = 0;
		}
		return this.boardNo;
	}

	public void setBoardNo(Integer boardNo) {
		this.boardNo = boardNo;
	}

	public String getDoor() {
		return this.door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public Integer getDoorHeight() {
		if (this.doorHeight == null) {
			this.doorHeight = 460;
		}
		return this.doorHeight;
	}

	public void setDoorHeight(Integer doorHeight) {
		this.doorHeight = doorHeight;
	}

	public String getCabinetType() {
		return this.cabinetType;
	}

	public void setCabinetType(String cabinetType) {
		this.cabinetType = cabinetType;
	}

	public Integer getTopLevel() {
		return this.topLevel;
	}

	public void setTopLevel(Integer topLevel) {
		this.topLevel = topLevel;
	}

	public Integer getTopHeight() {
		return this.topHeight;
	}

	public void setTopHeight(Integer topHeight) {
		this.topHeight = topHeight;
	}

	public List<Card> getCardList() {
		if (this.cardList == null) {
			this.cardList = new ArrayList<>();
		}
		return this.cardList;
	}

	public void setCardList(List<Card> cardList) {
		this.cardList = cardList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cabinetId == null) ? 0 : cabinetId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtraCabinet other = (ExtraCabinet) obj;
		if (cabinetId == null) {
			if (other.cabinetId != null)
				return false;
		} else if (!cabinetId.equals(other.cabinetId))
			return false;
		return true;
	}

	@Override
	@JsonValue
	public String toString() {
		return "ExtraCabinet [cabinetId=" + cabinetId + ", cabinetName=" + cabinetName + ", boardNo=" + boardNo
				+ ", doorHeight=" + doorHeight + "]";
	}
}