package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 每日限额
 * 
 * @author liweifneg
 *
 */
@Entity
@SuppressWarnings("serial")
public class DailyLimit extends BasePojo {

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "员工姓名", isForeign = true)
	private ViewUser user;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "限额物料", isForeign = true)
	private MatEquInfo matInfo;

	@PageTableTitle(value = "限额数量")
	@Column(columnDefinition = "int(11) COMMENT '限额数量'")
	private Integer limitNum;

	@PageTableTitle(value = "当前领取数量")
	@Column(columnDefinition = "int(11) COMMENT '当前领取数量'")
	private Integer curNum;

	@PageTableTitle(value = "未归还限额")
	@Column(columnDefinition = "int(11) COMMENT '未归还限额'")
	private Integer notReturnNum;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle("领用时间")
	@Column(columnDefinition = "datetime COMMENT '领用时间'")
	private Date opDate;

	public DailyLimit() {
		setOpDate(new Date());
	}

	public DailyLimit(Integer limitNum, Integer notReturnNum) {
		this.limitNum = limitNum;
		this.notReturnNum = notReturnNum;
	}

	public DailyLimit(String accountId, String matInfoId, Integer limitNum, Integer curNum, Integer notReturnNum) {
		setOpDate(new Date());
		this.accountId = accountId;
		this.matInfoId = matInfoId;
		this.limitNum = limitNum;
		this.curNum = curNum;
		this.notReturnNum = notReturnNum;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ViewUser getUser() {
		return user;
	}

	public void setUser(ViewUser user) {
		this.user = user;
	}

	public String getMatInfoId() {
		return matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public MatEquInfo getMatInfo() {
		return matInfo;
	}

	public void setMatInfo(MatEquInfo matInfo) {
		this.matInfo = matInfo;
	}

	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}

	public Integer getCurNum() {
		return curNum;
	}

	public void setCurNum(Integer curNum) {
		this.curNum = curNum;
	}

	public Integer getNotReturnNum() {
		return notReturnNum;
	}

	public void setNotReturnNum(Integer notReturnNum) {
		this.notReturnNum = notReturnNum;
	}

	public Date getOpDate() {
		return opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

}
