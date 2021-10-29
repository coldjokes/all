package com.dosth.test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.dosth.pojo.Col;
import com.dosth.pojo.Row;

public class Data {

	public static BlockingQueue<Row> rowQueue = new LinkedBlockingQueue<>();
	
	static {
		c();
	}
	
	static void c() {
		try {
			Col col = new Col(null, 1, 1, 1);
			BlockingQueue<Col> colQueue = new LinkedBlockingQueue<>();
			colQueue.put(col);
			Row row = new Row(3, 3, 410, colQueue);
			rowQueue.put(row);
			col = new Col(null,9, 8, 1);
			colQueue = new LinkedBlockingQueue<>();
			colQueue.put(col);
			col = new Col(null,2, 2, 1);
			colQueue.put(col);
			row = new Row(2, 2, 235, colQueue);
			rowQueue.put(row);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}