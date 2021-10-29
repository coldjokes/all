package com.dosth.admin.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.db.entity.BasePojo;

/**
 * 通知表
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class Notice extends BasePojo {

	@Column(columnDefinition = "varchar(50) COMMENT '标题'")
	private String title;

	@Column(columnDefinition = "int(11) COMMENT '类型'")
	private Integer type;

	@Column(columnDefinition = "varchar(200) COMMENT '内容'")
	private String content;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '创建时间'")
	private Date createTime;

	@Column(columnDefinition = "bigint(20) COMMENT '创建人'")
	private Long creater;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		if (this.createTime == null) {
			this.createTime = new Date();
		}
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreater() {
		return this.creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	@Override
	public String toString() {
		return "Notice{" + "id=" + id + ", title=" + title + ", type=" + type + ", content=" + content + ", createTime="
				+ createTime + ", creater=" + creater + "}";
	}
}