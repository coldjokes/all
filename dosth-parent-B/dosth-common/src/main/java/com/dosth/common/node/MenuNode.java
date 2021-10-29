package com.dosth.common.node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * 菜单的节点
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MenuNode implements Comparable {

	/**
	 * 节点id
	 */
	private String id;

	/**
	 * 父节点
	 */
	private String parentId;

	/**
	 * 节点名称
	 */
	private String name;

	/**
	 * 按钮级别
	 */
	private Integer levels;

	/**
	 * 按钮级别
	 */
	private Boolean isMenu;

	/**
	 * 按钮的排序
	 */
	private Integer num;

	/**
	 * 节点的url
	 */
	private String url;

	/**
	 * 节点图标
	 */
	private String icon;

	/**
	 * 子节点的集合
	 */
	private List<MenuNode> children = new ArrayList<MenuNode>();

	/**
	 * 查询子节点时候的临时集合
	 */
	private List<MenuNode> linkedList = new ArrayList<MenuNode>();

	public MenuNode() {
		super();
	}

	public MenuNode(String id, String parentId) {
		super();
		this.id = id;
		this.parentId = parentId;
	}
	
	public MenuNode(String id, String parentId, String name, Integer levels, Boolean isMenu, Integer num, String url,
			String icon) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.levels = levels;
		this.isMenu = isMenu;
		this.num = num;
		this.url = url;
		this.icon = icon;
	}

	public Integer getLevels() {
		return levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static MenuNode createRoot() {
		return new MenuNode("0", "-1");
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MenuNode> getChildren() {
		return this.children;
	}

	public void setChildren(List<MenuNode> children) {
		this.children = children;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Boolean getIsMenu() {
		return this.isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	@Override
	public String toString() {
		return "MenuNode{" + "id=" + id + ", parentId=" + parentId + ", name='" + name + '\'' + ", levels=" + levels
				+ ", num=" + num + ", url='" + url + '\'' + ", icon='" + icon + '\'' + ", children=" + children
				+ ", linkedList=" + linkedList + '}';
	}

	@Override
	public int compareTo(Object o) {
		MenuNode menuNode = (MenuNode) o;
		Integer num = menuNode.getNum();
		if (num == null) {
			num = 0;
		}
		return this.num.compareTo(num);
	}

	/**
	 * 构建整个菜单树
	 *
	 * @author fengshuonan
	 */
	public void buildNodeTree(List<MenuNode> nodeList) {
		List<MenuNode> linkedList;
		for (MenuNode treeNode : nodeList) {
			linkedList = treeNode.findChildNodes(nodeList, treeNode.getId());
			if (linkedList.size() > 0) {
				treeNode.setChildren(linkedList);
			}
		}
	}

	/**
	 * 查询子节点的集合
	 */
	public List<MenuNode> findChildNodes(List<MenuNode> nodeList, String parentId) {
		if (nodeList == null && parentId == null)
			return null;
		MenuNode node;
		for (Iterator<MenuNode> iterator = nodeList.iterator(); iterator.hasNext();) {
			node = (MenuNode) iterator.next();
			// 根据传入的某个父节点ID,遍历该父节点的所有子节点
			if (!"0".equals(node.getParentId()) && parentId.equals(node.getParentId())) {
				recursionFn(nodeList, node, parentId);
			}
		}
		return linkedList;
	}

	/**
	 * 遍历一个节点的子节点
	 *
	 * @author fengshuonan
	 */
	public void recursionFn(List<MenuNode> nodeList, MenuNode node, String pId) {
		List<MenuNode> childList = getChildList(nodeList, node);// 得到子节点列表
		if (childList.size() > 0) {// 判断是否有子节点
			if (node.getParentId().equals(pId)) {
				linkedList.add(node);
			}
			Iterator<MenuNode> it = childList.iterator();
			MenuNode n;
			while (it.hasNext()) {
				n = (MenuNode) it.next();
				recursionFn(nodeList, n, pId);
			}
		} else {
			if (node.getParentId().equals(pId)) {
				linkedList.add(node);
			}
		}
	}

	/**
	 * 得到子节点列表
	 */
	private List<MenuNode> getChildList(List<MenuNode> list, MenuNode node) {
		List<MenuNode> nodeList = new ArrayList<MenuNode>();
		Iterator<MenuNode> it = list.iterator();
		MenuNode n;
		while (it.hasNext()) {
			n = (MenuNode) it.next();
			if (n.getParentId().equals(node.getId())) {
				nodeList.add(n);
			}
		}
		return nodeList;
	}

	/**
	 * 清除掉按钮级别的资源
	 */
	public static List<MenuNode> clearBtn(List<MenuNode> nodes) {
		ArrayList<MenuNode> noBtns = new ArrayList<MenuNode>();
		for (MenuNode node : nodes) {
			if (node.getIsMenu()) {
				noBtns.add(node);
			}
		}
		return noBtns;
	}

	/**
	 * 清除所有二级菜单
	 */
	public static List<MenuNode> clearLevelTwo(List<MenuNode> nodes) {
		ArrayList<MenuNode> results = new ArrayList<MenuNode>();
		Integer levels;
		for (MenuNode node : nodes) {
			levels = node.getLevels();
			if (levels.equals(1)) {
				results.add(node);
			}
		}
		return results;
	}

	/**
	 * 构建菜单列表
	 */
	public static List<MenuNode> buildTitle(List<MenuNode> nodes) {
		List<MenuNode> clearBtn = clearBtn(nodes);
		new MenuNode().buildNodeTree(clearBtn);
		List<MenuNode> menuNodes = clearLevelTwo(clearBtn);
		// 对菜单排序
		Collections.sort(menuNodes);
		// 对菜单的子菜单进行排序
		for (MenuNode menuNode : menuNodes) {
			if (menuNode.getChildren() != null && menuNode.getChildren().size() > 0) {
				Collections.sort(menuNode.getChildren());
			} else {
				menuNode.setChildren(new ArrayList<>());
			}
		}
		return menuNodes;
	}
}