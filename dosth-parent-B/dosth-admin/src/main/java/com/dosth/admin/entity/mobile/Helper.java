package com.dosth.admin.entity.mobile;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 
 * @description 帮助中心
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class Helper extends BasePojo {

	@PageTableTitle(value = "问题名称")
	@Column(columnDefinition = "varchar(50) COMMENT '问题名称'")
	private String questionName;

	@PageTableTitle(value = "答案")
	@Column(columnDefinition = "varchar(200) COMMENT '答案'")
	private String answer;

	public Helper() {
	}

	public Helper(String questionName, String answer) {
		this.questionName = questionName;
		this.answer = answer;
	}

	public String getQuestionName() {
		return this.questionName;
	}

	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}