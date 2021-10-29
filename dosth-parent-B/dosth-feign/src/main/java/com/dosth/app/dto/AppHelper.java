package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description App帮助中心
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "AppHelper", description = "帮助中心")
public class AppHelper implements Serializable {
	@ApiModelProperty(name = "questionName", value = "问题名称")
	private String questionName;
	@ApiModelProperty(name = "answer", value = "答案")
	private String answer;

	public AppHelper() {
	}

	public AppHelper(String questionName, String answer) {
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