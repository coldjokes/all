package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @description 问题反馈
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "AppAdvice", description = "问题反馈")
public class AppAdvice implements Serializable {

	@ApiModelProperty(name = "accountId", value = "帐户Id")
	private String accountId;
	@ApiModelProperty(name = "adviceContent", value = "内容")
	private String adviceContent;
	@ApiModelProperty(name = "adviceMail", value = "邮箱")
	private String adviceMail;
	@ApiModelProperty(name = "adviceImages", value = "图片(多张)")
	private String adviceImages;
	@ApiModelProperty(name = "adviceContact", value = "其他联系方式")
	private String adviceContact;

	public AppAdvice() {
	}

	public AppAdvice(String accountId, String adviceContent, String adviceMail, String adviceImages,
			String adviceContact) {
		this.accountId = accountId;
		this.adviceContent = adviceContent;
		this.adviceMail = adviceMail;
		this.adviceImages = adviceImages;
		this.adviceContact = adviceContact;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAdviceContent() {
		return this.adviceContent;
	}

	public void setAdviceContent(String adviceContent) {
		this.adviceContent = adviceContent;
	}

	public String getAdviceMail() {
		return this.adviceMail;
	}

	public void setAdviceMail(String adviceMail) {
		this.adviceMail = adviceMail;
	}

	public String getAdviceImages() {
		return this.adviceImages;
	}

	public void setAdviceImages(String adviceImages) {
		this.adviceImages = adviceImages;
	}

	public String getAdviceContact() {
		return this.adviceContact;
	}

	public void setAdviceContact(String adviceContact) {
		this.adviceContact = adviceContact;
	}
}