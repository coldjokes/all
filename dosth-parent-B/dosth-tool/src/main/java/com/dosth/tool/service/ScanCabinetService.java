package com.dosth.tool.service;

import java.util.List;

import com.dosth.tool.entity.BorrowInfo;
import com.dosth.util.OpTip;

/**
 * 
 * @description 归还审核（PC端扫码枪）请求Service
 * @author chenlei
 *
 */
public interface ScanCabinetService {
	
	/**
	 * @description 归还信息审核
	 * @param code 二维码
	 */
	public List<BorrowInfo> recycleInfo(String code);
	
	/**
	 * @description 审核不通过
	 * @param  code 二维码信息
     * @param  num 确定数量
     * @param  content 备注
	 */
	public OpTip recycleReject(String code, int num, String contect, String accountId);
	
	/**
	 * @description 审核通过
	 * @param code 条形码
	 */
	public OpTip recyclePass(String code, String accountId);
}