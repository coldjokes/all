package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import com.dosth.common.db.entity.BasePojo;
import com.dosth.toolcabinet.enums.EnumBorrowType;

/**
 * 
 * @description 借出权限
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class BorrowPopedom extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '帐户ID'")
	private String accountId;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '物料借出权限'")
	private EnumBorrowType borrowPopedom;

	@Lob
	@Column(columnDefinition = "longtext COMMENT '权限'")
	private String popedoms;

	public BorrowPopedom() {
	}

	public BorrowPopedom(String accountId, EnumBorrowType borrowPopedom, String popedoms) {
		this.accountId = accountId;
		this.borrowPopedom = borrowPopedom;
		this.popedoms = popedoms;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public EnumBorrowType getBorrowPopedom() {
		return this.borrowPopedom;
	}

	public void setBorrowPopedom(EnumBorrowType borrowPopedom) {
		this.borrowPopedom = borrowPopedom;
	}

	public String getPopedoms() {
		return this.popedoms;
	}

	public void setPopedoms(String popedoms) {
		this.popedoms = popedoms;
	}
}