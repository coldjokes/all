package com.dosth.toolcabinet.service;

/**
 * @description 可控抽屉Service
 * @author Zhidong.Guo
 *
 */
public interface TrolDrawerService {
	/**
	 * @description 打开可控柜
	 * @param boardNo 栈号
	 * @param drawerNo 抽屉号
	 * @param openNum 打开数量
	 */
	public void openTrol(Byte boardNo, int drawerNo, int openNum);
}