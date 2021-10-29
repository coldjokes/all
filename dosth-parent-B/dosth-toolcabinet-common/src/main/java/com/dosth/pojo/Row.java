package com.dosth.pojo;

import java.util.concurrent.BlockingQueue;

public class Row {
	// 马达板Ip
	private String ip;
	// 马达板端口
	private int port;
	// 物理行
	private Integer row;
	// 控制板行号
	private Integer rowNo;
	// 层高
	private Integer levelHeight;
	// 同行所有列数据
	private BlockingQueue<Col> colQueue;

	public Row(Integer row, Integer rowNo, Integer levelHeight, BlockingQueue<Col> colQueue) {
		this.row = row;
		this.rowNo = rowNo;
		this.levelHeight = levelHeight;
		this.colQueue = colQueue;
	}

	public Row(String ip, int port, Integer row, Integer rowNo, Integer levelHeight, BlockingQueue<Col> colQueue) {
		this.ip = ip;
		this.port = port;
		this.rowNo = rowNo;
		this.row = row;
		this.levelHeight = levelHeight;
		this.colQueue = colQueue;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Integer getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getRow() {
		return this.row;
	}

	public void setRow(Integer row) {
		this.row = row;
	}

	public Integer getLevelHeight() {
		return this.levelHeight;
	}

	public void setLevelHeight(Integer levelHeight) {
		this.levelHeight = levelHeight;
	}

	public BlockingQueue<Col> getColQueue() {
		return this.colQueue;
	}

	public void setColQueue(BlockingQueue<Col> colQueue) {
		this.colQueue = colQueue;
	}
}