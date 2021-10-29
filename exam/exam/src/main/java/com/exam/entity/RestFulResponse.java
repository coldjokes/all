package com.exam.entity;

import java.util.List;

/**
 *  所有api返回结果
 * 
 * @author Yifeng Wang  
 */
public final class RestFulResponse<T> {
	
	private int code;
	private long total;
	private List<T> results;
	private String message = "";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int newCode) {
		this.code = newCode;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long newTotal) {
		this.total = newTotal;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> data) {
		this.results = data;
	}

}
