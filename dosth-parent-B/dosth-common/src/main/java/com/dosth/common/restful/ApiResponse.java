package com.dosth.common.restful;

import java.util.List;

/**
 * Api results.
 */
public final class ApiResponse<T> {
	private int code;
	private int total;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int newTotal) {
		this.total = newTotal;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> data) {
		this.results = data;
	}

	public void setTotal(Long total2) {
		// TODO Auto-generated method stub
		
	}
}
