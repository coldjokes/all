package com.dosth.toolcabinet.service;

/**
 * 取料Service
 * 
 * @author guozhidong
 *
 */
public interface BorrowMatService {
	/**
     * @description 借出物料
     * @param matBillId
     * @param accountId
     */
    public void borrowMat(String matBillId, String accountId) throws Exception;
}