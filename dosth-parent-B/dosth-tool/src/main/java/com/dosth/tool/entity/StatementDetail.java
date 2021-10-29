package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.db.entity.BasePojo;

/**
 * 核对明细
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class StatementDetail extends BasePojo {

	@Column(name = "STATEMENT_ID", columnDefinition = "varchar(36) COMMENT '核对主业务ID'")
	private String statementId;
	@ManyToOne
	@JoinColumn(name = "STATEMENT_ID", insertable = false, updatable = false)
	private Statement statement;

	@Column(name = "MAT_USE_RECORD_ID", columnDefinition = "varchar(36) COMMENT '领料记录ID'")
	private String matUseRecordId;
	@ManyToOne
	@JoinColumn(name = "MAT_USE_RECORD_ID", insertable = false, updatable = false)
	private MatUseRecord matUseRecord;

	public StatementDetail() {
	}

	public StatementDetail(String statementId, String matUseRecordId) {
		this.statementId = statementId;
		this.matUseRecordId = matUseRecordId;
	}

	public String getStatementId() {
		return this.statementId;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}

	public Statement getStatement() {
		return this.statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public String getMatUseRecordId() {
		return this.matUseRecordId;
	}

	public void setMatUseRecordId(String matUseRecordId) {
		this.matUseRecordId = matUseRecordId;
	}

	public MatUseRecord getMatUseRecord() {
		return this.matUseRecord;
	}

	public void setMatUseRecord(MatUseRecord matUseRecord) {
		this.matUseRecord = matUseRecord;
	}
}