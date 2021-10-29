package com.dosth.toolcabinet.service;

import java.util.List;

import com.dosth.dto.ExtraCabinet;

/**
 * @description 存储介质接口
 * @author guozhidong
 *
 */
public interface StorageMediumService {

	/**
	 * @description 领用
	 * @param batchCabinetList
	 */
	public void collarUse(List<ExtraCabinet> batchCabinetList);
}