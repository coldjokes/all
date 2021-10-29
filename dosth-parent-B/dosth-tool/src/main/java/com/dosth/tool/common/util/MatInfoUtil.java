package com.dosth.tool.common.util;

import com.dosth.dto.MatInfo;
import com.dosth.tool.common.dto.SubBoxMatInfo;
import com.dosth.tool.entity.MatEquInfo;

/**
 * @description 物料信息工具类
 * @author guozhidong
 *
 */
public class MatInfoUtil {
	private MatInfoUtil() {}
	
	/**
	 * @description 创建暂存柜物料信息详情
	 * @param matInfo 暂存柜物料信息
	 * @return
	 */
	public static String createSubBoxMatInfo(SubBoxMatInfo matInfo) {
		if (matInfo == null) {
			return "";
		}
		StringBuffer matInfoBuffer = new StringBuffer(matInfo.getMatName());
		if (matInfo.getBarCode() != null && !"".equals(matInfo.getBarCode())) {
			matInfoBuffer.append("[");
			matInfoBuffer.append(matInfo.getBarCode());
			matInfoBuffer.append("]");
			if (matInfo.getSpec() != null && !"".equals(matInfo.getSpec())) {
				matInfoBuffer.append("[");
				matInfoBuffer.append(matInfo.getSpec());
				matInfoBuffer.append("]");
			}
		} 
		return matInfoBuffer.toString();
	}
	
	/**
	 * @description 封装物料POJO到物料传输对象
	 * @param matInfo 物料POJO
	 * @return
	 */
	public static MatInfo createMatInfo(MatEquInfo matInfo) {
		if (matInfo == null) {
			return null;
		}
		MatInfo info = new MatInfo();
		/** 物料Id */
		info.setMatId(matInfo.getId());
		/** 编号 */
		info.setBarCode(matInfo.getBarCode());
		/** 类型编码 */
//		info.setTypeCode(matInfo.get);
		/**包装单位*/
		info.setPackUnit(matInfo.getPackUnit());
		/** 物料名称 */
		info.setName(matInfo.getMatEquName());
		/** 品牌 */
		info.setBrand(matInfo.getBrand());
		/** 规格 */
		info.setSpec(matInfo.getSpec());
		/** 借出类型 */
		info.setBorrowType(matInfo.getBorrowType().name());
		/** 借出类型名称 */
		info.setBorrowTypeName(matInfo.getBorrowType().getMessage());
		/** 图片 */
		info.setIcon(matInfo.getIcon());
		/** 包装数量 */
		info.setPackNum(matInfo.getNum());
		/** 供应商 */
		info.setManufacturerName(matInfo.getManufacturer() != null ? matInfo.getManufacturer().getManufacturerName() : "");
		/** 备注 */
		info.setRemark(matInfo.getRemark());
		return info;
	}
}