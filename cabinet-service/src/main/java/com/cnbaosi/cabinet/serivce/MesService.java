package com.cnbaosi.cabinet.serivce;

/**
 * mes相关方法
 * 
 * @author Yifeng Wang
 */
public interface MesService {

	/**
	 * 根据工单号获取物料名称
	 */
	String getMaterialNamesByCode(String code);
}
