package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.constant.YesOrNo;
import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.SubCabinetBill;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 暂存柜领用记录分页封装对象
 * @author guozhidong
 *
 */
public class ViceCabinetQueryPageWarpper extends PageWarpper<SubCabinetBill> {

	public ViceCabinetQueryPageWarpper(Page<SubCabinetBill> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof MatEquInfo) {
			MatEquInfo matInfo = (MatEquInfo) obj;
			map.put(key, matInfo.getMatEquName());
		} else if (obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
		} else if (obj instanceof SubBox) {
			SubBox box = (SubBox) obj;
			map.put(key, box.getEquSetting().getEquSettingName() + "(" + box.getRowNo() + "-" + box.getColNo() + ")");
		} else if (obj instanceof YesOrNo) {
			if (YesOrNo.valueOf(String.valueOf(obj)).equals(YesOrNo.YES)) {
				map.put(key, "暂存");
			} else {
				map.put(key, "取出");
			}
		}
	}
}