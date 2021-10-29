package com.dosth.tool.entity.mobile;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.entity.MatEquInfo;

/**
 * 
 * @description App购物车
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class PhoneCart extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;

	@Column(name = "MAT_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matId;
	@ManyToOne
	@JoinColumn(name = "MAT_ID", insertable = false, updatable = false)
	private MatEquInfo mat;

	@Column(columnDefinition = "int(11) COMMENT '选择数量'")
	private Integer num;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '操作时间'")
	private Date opDate;

	public PhoneCart() {
		setOpDate(new Date());
	}

	public PhoneCart(String accountId, String matId, Integer num) {
		setOpDate(new Date());
		this.accountId = accountId;
		this.matId = matId;
		this.num = num;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getMatId() {
		return this.matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public MatEquInfo getMat() {
		return this.mat;
	}

	public void setMat(MatEquInfo mat) {
		this.mat = mat;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
}