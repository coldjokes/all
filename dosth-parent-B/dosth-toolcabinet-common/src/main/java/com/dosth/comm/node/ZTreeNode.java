package com.dosth.comm.node;

/**
 * jquery ztree 插件的节点
 * 
 * @author guozhidong
 *
 */
public class ZTreeNode {

	private String id; // 节点id
	private String pId;// 父节点id
	private String name;// 节点名称
	private Boolean isOpen;// 是否打开节点
	private Boolean checked;// 是否被选中

	public ZTreeNode() {
	}

	public ZTreeNode(String id, String pId, String name) {
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public ZTreeNode(String id, String pId, String name, Boolean isOpen) {
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.isOpen = isOpen;
	}

	public ZTreeNode(String id, String pId, String name, Boolean isOpen, Boolean checked) {
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.isOpen = isOpen;
		this.checked = checked;
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

	public Boolean getIsOpen() {
		if (this.isOpen == null) {
			this.isOpen = false;
		}
		return this.isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Boolean getChecked() {
		if (this.checked == null) {
			this.checked = false;
		}
		return this.checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public static ZTreeNode createParent(String rootName) {
		ZTreeNode zTreeNode = new ZTreeNode();
		zTreeNode.setChecked(true);
		zTreeNode.setId("0");
		zTreeNode.setName(rootName);
		zTreeNode.setIsOpen(true);
		zTreeNode.setpId("0");
		return zTreeNode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ZTreeNode)) {
			return false;
		}
		ZTreeNode other = (ZTreeNode) obj;
		if (this.id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!this.id.equals(other.id)) {
			return false;
		}
		return true;
	}
}