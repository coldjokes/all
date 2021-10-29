package com.dosth.toolcabinet.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @description 借出物料
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class BorrRow implements Serializable {
	/** 行号 */
	private Integer rowNo;
	/** 物料格子信息 */
	private List<MatLatticeInfo> list;

	public BorrRow(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getRowNo() {
		return this.rowNo;
	}

	public List<MatLatticeInfo> getList() {
		return this.list;
	}

	public void setList(List<MatLatticeInfo> list) {
		this.list = list;
	}
}