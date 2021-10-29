package com.exam.entity.modal.vo;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 级联树
 * @author Administrator
 *
 */
public class TreeNodes{
    private String id; //当前id
    private String pId; //父id
    private String text; //名称

	private String type;
    
    private String parentId; //父id(for app)
    private String name; //名称（for app）
    
    private List<TreeNodes> children = Lists.newArrayList();
    
    
    public TreeNodes() {
		super();
	}

	public TreeNodes(String id, String pId, String text) {
		super();
		this.id = id;
		this.pId = pId;
		this.text = text;
		
		this.parentId = pId;
		this.name = text;
	}

	public TreeNodes(String id, String pId, String text,String type) {
		super();
		this.id = id;
		this.pId = pId;
		this.text = text;
		this.type = type;

		this.parentId = pId;
		this.name = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<TreeNodes> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNodes> children) {
		this.children = children;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
