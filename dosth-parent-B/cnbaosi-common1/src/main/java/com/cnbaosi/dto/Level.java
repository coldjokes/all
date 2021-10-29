package com.cnbaosi.dto;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

/**
 * @description 层级
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class Level implements Serializable {
	// 栈号
	private int boardNo;
	// 行号
	private int rowNo;
	// 当前层高度
	private int height;	
	// 马达集合
	private BlockingQueue<Slave> slaveQueue;

	public Level(int rowNo, int height) {
		this.rowNo = rowNo;
		this.height = height;
	}

	public int getBoardNo() {
		return this.boardNo;
	}

	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}

	public int getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BlockingQueue<Slave> getSlaveQueue() {
		return this.slaveQueue;
	}

	public void setSlaveQueue(BlockingQueue<Slave> slaveQueue) {
		this.slaveQueue = slaveQueue;
	}
}