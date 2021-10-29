package com.dosth.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modbus电路板
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class Card implements Serializable {
	// id
	private String detailId;
	// ip
	private String host;
	// 端口
	private String port;
	// 层级索引位
	private int rowNo;
	// 门
	private String door;
	// 槽位集合
	private List<Lattice> latticeList;
	// 层高
	private Integer levelHeight;

	public Card() {
	}

	public Card(String detailId, String host, String port, int rowNo, Integer levelHeight) {
		super();
		this.detailId = detailId;
		this.host = host;
		this.port = port;
		this.rowNo = rowNo;
		this.levelHeight = levelHeight;
	}

	public String getDetailId() {
		return this.detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public String getDoor() {
		return this.door;
	}

	public void setDoor(String door) {
		this.door = door;
	}

	public List<Lattice> getLatticeList() {
		if (this.latticeList == null) {
			this.latticeList = new ArrayList<>();
		}
		return this.latticeList;
	}

	public void setLatticeList(List<Lattice> latticeList) {
		this.latticeList = latticeList;
	}

	public Integer getLevelHeight() {
		return this.levelHeight;
	}

	public void setLevelHeight(Integer levelHeight) {
		this.levelHeight = levelHeight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + rowNo;
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
		Card other = (Card) obj;
		if (rowNo != other.rowNo)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Card [host=" + host + ", port=" + port + ", rowNo=" + rowNo + ", door=" + door + ", levelHeight="
				+ levelHeight + "]";
	}
}