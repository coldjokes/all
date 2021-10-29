package com.dosth.pojo;

public class Col {
	// 物理Id
	private String staId;
	// 物理列
	private Integer col;
	// 控制板列号
	private Integer boardColNo;
	// 数量
	private Integer num;

	public Col(String staId, Integer col, Integer boardColNo, Integer num) {
		this.staId = staId;
		this.boardColNo = boardColNo;
		this.col = col;
		this.num = num;
	}
	
	public String getStaId() {
		return staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public Integer getBoardColNo() {
		return this.boardColNo;
	}

	public void setBoardColNo(Integer boardColNo) {
		this.boardColNo = boardColNo;
	}

	public Integer getCol() {
		return this.col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}