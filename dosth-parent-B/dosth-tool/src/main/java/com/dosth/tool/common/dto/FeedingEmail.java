package com.dosth.tool.common.dto;

import java.io.Serializable;

/**
 * 
 * @description 补料邮件对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeedingEmail implements Serializable {
//	private String cabinetId; // 柜子Id
	private String equDetailStaId; // 当前单元Id
	private String matId; // 物料Id
//	private Integer curNum; // 当前数量
//	private Integer maxNum; // 最大数量
//	private Integer outNum; // 需补充数量

	public FeedingEmail() {
	}

	public FeedingEmail(String equDetailStaId, String matId) {
		this.equDetailStaId = equDetailStaId;
		this.matId = matId;
	}

	public String getEquDetailStaId() {
		return this.equDetailStaId;
	}

	public void setEquDetailStaId(String equDetailStaId) {
		this.equDetailStaId = equDetailStaId;
	}

	public String getMatId() {
		return this.matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	@Override
	public String toString() {
		return "FeedingEmail [equDetailStaId=" + equDetailStaId + ", matId=" + matId + "]";
	}
}