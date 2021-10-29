package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description 归还详情
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class BorrowInfo extends BasePojo {

	@PageTableTitle(value = "刀具编号")
	@Column(columnDefinition = "varchar(255) COMMENT '物料编号'")
	private String knifeId;

	@PageTableTitle(value = "刀具名称")
	@Column(columnDefinition = "varchar(255) COMMENT '物料名称'")
	private String knifeName;

	@PageTableTitle(value = "归还人员")
	@Column(columnDefinition = "varchar(255) COMMENT '归还人员'")
	private String username;

	@ApiModelProperty(name = "userId", value = "用户ID")
	@Column(columnDefinition = "varchar(255) COMMENT '用户ID'")
	private String userId;

	@PageTableTitle(value = "归还详情")
	@Column(columnDefinition = "varchar(255) COMMENT '归还详情'")
	private String returnInfo;

	// 归还时间
	@PageTableTitle(value = "归还时间")
	@Column(columnDefinition = "varchar(255) COMMENT '归还时间'")
	private String time;

	// 借出数量
	@PageTableTitle(value = "借出数量")
	@Column(columnDefinition = "varchar(255) COMMENT '借出数量'")
	private String num;

	// 图片
	@PageTableTitle(value = "刀具图片")
	@Column(columnDefinition = "varchar(255) COMMENT '图片'")
	private String knifeImage;

	public BorrowInfo() {
	}

	public BorrowInfo(String knifeId, String knifeName, String username, String returnInfo, String time, String num,
			String knifeImage, String userId) {
		this.knifeId = knifeId;
		this.knifeName = knifeName;
		this.username = username;
		this.returnInfo = returnInfo;
		this.time = time;
		this.num = num;
		this.knifeImage = knifeImage;
		this.userId = userId;
	}

	public String getKnifeId() {
		return knifeId;
	}

	public void setKnifeId(String knifeId) {
		this.knifeId = knifeId;
	}

	public String getKnifeName() {
		return knifeName;
	}

	public void setKnifeName(String knifeName) {
		this.knifeName = knifeName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getReturnInfo() {
		return returnInfo;
	}

	public void setReturnInfo(String returnInfo) {
		this.returnInfo = returnInfo;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getKnifeImage() {
		return knifeImage;
	}

	public void setKnifeImage(String knifeImage) {
		this.knifeImage = knifeImage;
	}

}