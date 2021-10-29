package com.cnbaosi.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 服务端响应结果
 * @author guozhidong
 *
 * @param <T>
 */
@SuppressWarnings("serial")
public class ApiFeignResponse<T> extends OpTip {
	private List<T> resultList;

	public List<T> getResultList() {
		if (this.resultList == null) {
			this.resultList = new ArrayList<>();
		}
		return this.resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
}