package com.dosth.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Modbus电路板
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class AppCard implements Serializable {
	// 层级索引位
	private String floorName;
	// 槽位集合
	private List<AppKnifes> knives;

	public AppCard() {
	}

	public AppCard(String floorName) {
		this.floorName = floorName;
	}

	public String getFloorName() {
		return this.floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public List<AppKnifes> getKnives() {
		if (this.knives == null) {
			this.knives = new ArrayList<>();
		}
		return this.knives;
	}

	public void setKnives(List<AppKnifes> knives) {
		this.knives = knives;
	}
}