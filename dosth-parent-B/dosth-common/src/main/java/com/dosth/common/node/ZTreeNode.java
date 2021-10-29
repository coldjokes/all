package com.dosth.common.node;

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
	private String equType;// 物料类型
	private String path;// 节点路径
	private String icon; // 图标
	private Integer level; // 层级

	public ZTreeNode() {
	}

	public ZTreeNode(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public ZTreeNode(String id, String pId, String name) {
		this.id = id;
		this.pId = pId;
		this.name = name;
	}

	public ZTreeNode(String id, String pId, String name, String equType, String path) {
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.equType = equType;
		this.path = path;
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

	public String getEquType() {
		return equType;
	}

	public void setEquType(String equType) {
		this.equType = equType;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getLevel() {
		if (this.level == null) {
			this.level = 0;
		}
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pId == null) ? 0 : pId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZTreeNode other = (ZTreeNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pId == null) {
			if (other.pId != null)
				return false;
		} else if (!pId.equals(other.pId))
			return false;
		return true;
	}
}