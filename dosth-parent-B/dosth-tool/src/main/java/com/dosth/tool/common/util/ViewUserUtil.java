package com.dosth.tool.common.util;

import com.dosth.tool.vo.ViewUser;

/**
 * 视图用户工具类
 * 
 * @author guozhidong
 *
 */
public final class ViewUserUtil {

	private ViewUserUtil() {
	}

	/**
	 * 根据用户视图创建人员名称
	 * 
	 * @param viewUser
	 * @return
	 */
	public static String createViewUserName(ViewUser viewUser) {
		if (viewUser == null) {
			return "";
		}
//		StringBuffer viewUserName = new StringBuffer(viewUser.getUserName());
//		if (viewUser.getIcCard() != null && !"".equals(viewUser.getIcCard())) {
//			viewUserName.append("[");
//			viewUserName.append(viewUser.getIcCard());
//			viewUserName.append("]");
//		}
//		return viewUserName.toString();
		return viewUser.getUserName();
	}

}