package com.dosth.tool.rpc;

/**
 * 
 * @description 移动端远程接口
 * @author guozhidong
 *
 */
public interface MobileRpcService {
	/**
	 * @description 添加电话预定
	 * @param equDetailStaId 刀具柜单元Id
	 * @param matInfoId 物料Id
	 * @param borrNum 借出数量
	 * @param accountId 预定帐户
	 */
	public void addPhoneBooking(String equDetailStaId, String matInfoId, Integer borrNum, String accountId);
	
	/**
	 * @description 领取电话预约
	 * @param cabinetId 柜子Id
	 * @param bookId 预约Id
	 * @param accountId 预约帐户
	 */
	public void removePhoneBooking(String cabinetId, String bookId, String accountId);
}