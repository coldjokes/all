package com.dosth.comm.node;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单的节点
 * 
 * @author chenlei
 *
 */
public class TypeNode {

	/**
	 * 节点id
	 */
	private String id;

	/**
	 * 父节点
	 */
	private String pId;

	/**
	 * 节点名称
	 */
	private String name;

	/**
	 * 子节点的集合
	 */
	private List<TypeNode> children = new ArrayList<TypeNode>();

	public TypeNode() {
		super();
	}

	public TypeNode(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public TypeNode(String id, String pId, String name) {
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return this.pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TypeNode> getChildren() {
		return this.children;
	}

	public void setChildren(List<TypeNode> children) {
		this.children = children;
	}
}