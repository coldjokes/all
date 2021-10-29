package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 归还详情
 * @author chenlei
 *
 */
@Entity
@SuppressWarnings("serial")
public class MatReturnDetail extends BasePojo {

	@Column(name = "MAT_RETURN_BACK_ID", columnDefinition = "varchar(36) COMMENT '归还ID'")
	private String matReturnBackId;
	@ManyToOne
	@PageTableTitle(value = "物料名称", isForeign = true)
	@JoinColumn(name = "MAT_RETURN_BACK_ID", insertable = false, updatable = false)
	private MatReturnBack matReturnBack;

	@Column(name = "RESTITUTION_TYPE_ID", columnDefinition = "varchar(36) COMMENT '归还ID'")
	private String restitutionTypeId;
	@OneToOne
	@PageTableTitle(value = "物料名称", isForeign = true)
	@JoinColumn(name = "RESTITUTION_TYPE_ID", insertable = false, updatable = false)
	private RestitutionType restitutionType;

	@PageTableTitle(value = "归还数量")
	@Column(columnDefinition = "int(36) COMMENT '归还数量'")
	private Integer num;

	@PageTableTitle(value = "归还时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '归还时间'")
	private Date opDate;

	public MatReturnDetail() {
		setOpDate(new Date());
	}

	public MatReturnDetail(String matReturnBackId, String restitutionTypeId, int num) {
		this.matReturnBackId = matReturnBackId;
		this.restitutionTypeId = restitutionTypeId;
		this.num = num;
		setOpDate(new Date());
	}

	public String getMatReturnBackId() {
		return matReturnBackId;
	}

	public void setMatReturnBackId(String matReturnBackId) {
		this.matReturnBackId = matReturnBackId;
	}

	public MatReturnBack getMatReturnBack() {
		return matReturnBack;
	}

	public void setMatReturnBack(MatReturnBack matReturnBack) {
		this.matReturnBack = matReturnBack;
	}

	public String getRestitutionTypeId() {
		return restitutionTypeId;
	}

	public void setRestitutionTypeId(String restitutionTypeId) {
		this.restitutionTypeId = restitutionTypeId;
	}

	public RestitutionType getRestitutionType() {
		return restitutionType;
	}

	public void setRestitutionType(RestitutionType restitutionType) {
		this.restitutionType = restitutionType;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Date getOpDate() {
		return opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

}