package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.db.entity.BasePojo;

/**
 * 
 * @description 副柜详情
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class SubCabinetDetail extends BasePojo {

	@Column(name = "SUB_BOX_ID", columnDefinition = "varchar(36) COMMENT '暂存柜货位ID'")
	private String subBoxId;
	@ManyToOne
	@JoinColumn(name = "SUB_BOX_ID", insertable = false, updatable = false)
	private SubBox subBox;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	private MatEquInfo matInfo;

	@Column(columnDefinition = "int(11) COMMENT '数量'")
	private Integer num;

	public SubCabinetDetail() {
	}

	public SubCabinetDetail(String subBoxId, String matInfoId, Integer num) {
		this.subBoxId = subBoxId;
		this.matInfoId = matInfoId;
		this.num = num;
	}

	public String getSubBoxId() {
		return this.subBoxId;
	}

	public void setSubBoxId(String subBoxId) {
		this.subBoxId = subBoxId;
	}

	public SubBox getSubBox() {
		return this.subBox;
	}

	public void setSubBox(SubBox subBox) {
		this.subBox = subBox;
	}

	public String getMatInfoId() {
		return this.matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public MatEquInfo getMatInfo() {
		return this.matInfo;
	}

	public void setMatInfo(MatEquInfo matInfo) {
		this.matInfo = matInfo;
	}

	public Integer getNum() {
		if (this.num == null) {
			this.num = 0;
		}
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Float getPrice() {
		return this.matInfo.getStorePrice();
	}
}