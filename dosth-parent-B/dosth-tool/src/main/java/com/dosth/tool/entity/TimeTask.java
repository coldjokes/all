package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.OnOrOff;
import com.dosth.tool.common.state.TaskType;

/**
 * 定时任务
 * 
 * @author Weifeng.Li
 *
 */
@SuppressWarnings("serial")
@Entity
public class TimeTask extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '任务名称'")
	@PageTableTitle(value = "任务名称")
	private TaskType name;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(36) COMMENT '任务状态'")
	@PageTableTitle(value = "任务状态", isEnum = true)
	private OnOrOff status;

	public TaskType getName() {
		return name;
	}

	public void setName(TaskType name) {
		this.name = name;
	}

	public OnOrOff getStatus() {
		return status;
	}

	public void setStatus(OnOrOff status) {
		this.status = status;
	}

}
