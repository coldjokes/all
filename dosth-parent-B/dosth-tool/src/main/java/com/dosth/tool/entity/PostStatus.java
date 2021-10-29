package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 对外接口推送状态
 * 
 * @author chenlei
 *
 */
@Entity
@SuppressWarnings("serial")
public class PostStatus extends BasePojo {

	@Column(columnDefinition = "bit COMMENT '是否补料'")
	private Boolean feedFlg;

	@Column(columnDefinition = "bit COMMENT '是否领用'")
	private Boolean borrowFlg;

	@Column(columnDefinition = "bit COMMENT '是否归还'")
	private Boolean returnBackFlg;

	@Column(columnDefinition = "varchar(100) COMMENT '访问URL'")
	private String url;

	@Lob
	@Column(columnDefinition = "longtext COMMENT '访问参数'")
	private String params;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	private YesOrNo status;

	@Column(columnDefinition = "varchar(100) COMMENT '返回信息'")
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle("推送时间")
	@Column(columnDefinition = "datetime COMMENT '推送时间'")
	private Date opDate;

	public PostStatus(Boolean feedFlg, Boolean borrowFlg, Boolean returnBackFlg, String url, String params,
			YesOrNo status, String message) {
		this.feedFlg = feedFlg;
		this.borrowFlg = borrowFlg;
		this.returnBackFlg = returnBackFlg;
		this.url = url;
		this.params = params;
		this.status = status;
		this.message = message;
		setOpDate(new Date());
	}

	public Boolean getFeedFlg() {
		return feedFlg;
	}

	public void setFeedFlg(Boolean feedFlg) {
		this.feedFlg = feedFlg;
	}

	public Boolean getBorrowFlg() {
		return borrowFlg;
	}

	public void setBorrowFlg(Boolean borrowFlg) {
		this.borrowFlg = borrowFlg;
	}

	public Boolean getReturnBackFlg() {
		return returnBackFlg;
	}

	public void setReturnBackFlg(Boolean returnBackFlg) {
		this.returnBackFlg = returnBackFlg;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public YesOrNo getStatus() {
		return status;
	}

	public void setStatus(YesOrNo status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getOpDate() {
		return opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}

}