package com.exam.entity.criteria;

import java.util.List;

/**
 *  用户查询条件
 * 
 * @author Yifeng Wang  
 */
public class UserCriteria{
	
	private String text; //用户名、姓名、编号、卡号
	private String username; //用户名
	private Integer role; //角色
	private Integer source; //数据来源
	private Integer status; //账户状态 1:启用 2:禁用
	private String icCard; //ic卡号
	private String fullname; //姓名

	private List<String> idList; //id集合
	private List<String> noList; //用户id集合
	private List<String> nameList; //用户名称集合

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}

	public List<String> getNoList() {
		return noList;
	}

	public void setNoList(List<String> noList) {
		this.noList = noList;
	}

	public List<String> getNameList() {
		return nameList;
	}

	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getRole() {
		return role;
	}
	public void setRole(Integer role) {
		this.role = role;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getIcCard() {
		return icCard;
	}
	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
}

