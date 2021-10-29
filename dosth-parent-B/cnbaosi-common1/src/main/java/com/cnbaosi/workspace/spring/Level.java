package com.cnbaosi.workspace.spring;

import java.util.Map;

/**
 * @description 层
 * @author guozhidong
 *
 */
public class Level {
	// 行号
	private Integer rowNo;
	// 行高
	private Integer rowHeight;
	// 格子集合,按栈号分组, staId
	private Map<Byte, Map<String, Lattice>> latticeGroupMap;

	public Level() {
	}

	public Level(Integer rowNo, Integer rowHeight, Map<Byte, Map<String, Lattice>> latticeGroupMap) {
		this.rowNo = rowNo;
		this.rowHeight = rowHeight;
		this.latticeGroupMap = latticeGroupMap;
	}

	public Integer getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getRowHeight() {
		return this.rowHeight;
	}

	public void setRowHeight(Integer rowHeight) {
		this.rowHeight = rowHeight;
	}

	public Map<Byte, Map<String, Lattice>> getLatticeGroupMap() {
		return this.latticeGroupMap;
	}

	public void setLatticeGroupMap(Map<Byte, Map<String, Lattice>> latticeGroupMap) {
		this.latticeGroupMap = latticeGroupMap;
	}
}