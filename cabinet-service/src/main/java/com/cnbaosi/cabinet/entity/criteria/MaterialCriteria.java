package com.cnbaosi.cabinet.entity.criteria;

import java.util.List;

/**
 * 物料查询条件
 * 
 * @author Yifeng Wang  
 */
public class MaterialCriteria {

	private String text; //物料名称、编号或规格
	private Integer source; //物料来源
	private List<String> idList; //id集合
	private List<String> noList; //物料编号集合
	private List<String> nameList; //物料名称集合
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
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
	
}

