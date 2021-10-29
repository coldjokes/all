package com.dosth.tool.common.util;

import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.SubBox;

/**
 * @description 柜子信息工具类
 * @author guozhidong
 *
 */
public final class CabinetInfoUtil {
	private CabinetInfoUtil() {
	}

	/**
	 * @description 根据EquDetailSta创建格子信息
	 * @param sta EquDetailSta对象
	 * @return XXX柜-X面(行号--列号)
	 */
	public static String createCabinetInfoByEquDetailSta(EquDetailSta sta) {
		if (sta == null) {
			return "未知弹簧位置";
		}
		StringBuilder builder = new StringBuilder();
		EquSetting setting = sta.getEquDetail().getEquSetting();
		builder.append(createCabinetInfoByEquSetting(setting));
		builder.append("(");
		builder.append(sta.getEquDetail().getRowNo());
		builder.append("--");
		builder.append(sta.getColNo());
		builder.append(")");
		return builder.toString();
	}

	/**
	 * @description 根据EquSetting创建柜子信息
	 * @param setting EquSetting 对象
	 * @return XXX柜-X面或XXX柜
	 */
	public static String createCabinetInfoByEquSetting(EquSetting setting) {
		if (setting == null) {
			return "未知设备";
		}
		StringBuilder builder = new StringBuilder();
		if (setting.getEquSettingParentId() != null && !"".equals(setting.getEquSettingParentId())) {
			builder.append(setting.getEquSettingParent().getEquSettingName());
		} else {
			builder.append(setting.getEquSettingName());
		}
		if (setting.getEquSettingName() != null && !"".equals(setting.getEquSettingName())) {
			builder.append("-");
			builder.append(setting.getEquSettingName());
		}
		return builder.toString();
	}

	/**
	 * @description 根据subBox创建暂存柜盒子信息
	 * @param subBox SubBox对象
	 * @return
	 */
	public static String createSubBoxInfoBySubBox(SubBox subBox) {
		if (subBox == null) {
			return "未定义暂存柜盒子";
		}
		StringBuilder builder = new StringBuilder();
		builder.append(subBox.getEquSetting().getEquSettingName());
		builder.append("(");
		builder.append(subBox.getRowNo());
		builder.append("--");
		builder.append(subBox.getColNo());
		builder.append(")");
		return builder.toString();
	}
}