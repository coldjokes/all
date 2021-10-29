package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.state.ReceiveType;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.vo.ViewUser;

public class MainCabinetQueryPageWarpper extends PageWarpper<MatUseRecord> {

	public MainCabinetQueryPageWarpper(Page<MatUseRecord> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof MatEquInfo) {
			map.put(key, ((MatEquInfo) obj).getMatEquName());
		} else if (obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
		} else if (obj instanceof EquDetailSta) {
			map.put(key, ((EquDetailSta) obj).getEquDetail().getEquSetting().getEquSettingName() + "("
					+ ((EquDetailSta) obj).getEquDetail().getRowNo() + "-" + ((EquDetailSta) obj).getColNo() + ")");
		} else if (obj instanceof ReceiveType) {
			map.put(key, ((ReceiveType) obj).getMessage());
		} else if (obj instanceof MatCategoryTree) {
			map.put(key, ((MatCategoryTree) obj).getfName());
		}
	}
}