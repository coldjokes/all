package com.dosth.toolcabinet.service;

import com.cnbaosi.exception.BscException;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.util.OpTip;

/**
 * @description 归还Service
 * @author guozhidong
 *
 */
public interface ReturnBackService {

	/**
	 * @description 归还物料
	 * @param printInfo 打印信息
	 * @return 
	 * @throws BscException
	 */
	public OpTip returnBack(ReturnBackPrintInfo printInfo, String cabinetId, Boolean printFlag) throws BscException;
}