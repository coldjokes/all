package com.dosth.common.warpper.bootstraptable;

/**
 * bootstrap columns
 * 
 * @author guozhidong
 *
 */
public class Columns {
	/**
	 * json字段
	 */
	private String field;
	/**
	 * 表格标题
	 */
	private String title;
	/**
	 * 是否可见
	 */
	private Boolean isVisible;

	public Columns() {
		super();
	}

	public Columns(String field, String title, Boolean isVisible) {
		this.field = field;
		this.title = title;
		this.isVisible = isVisible;
	}

	public String getField() {
		return this.field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Boolean getIsVisible() {
		if (this.isVisible == null) {
			this.isVisible = true;
		}
		return this.isVisible;
	}

	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
	}
}