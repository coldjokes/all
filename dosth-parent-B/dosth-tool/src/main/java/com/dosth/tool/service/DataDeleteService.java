package com.dosth.tool.service;

/**
 * @description 数据删除接口
 * @author Zhidong.Guo
 *
 */
public interface DataDeleteService {

	/**
	 * @description 清理物料
	 */
	public void clearMat();

	/**
	 * @description 清理设备
	 */
	public void clearEqu();

	/**
	 * @description 清理人员
	 */
	public void clearUser() throws Exception;

	/**
	 * @description 一键恢复出厂设置
	 */
	public void resetInit();
}