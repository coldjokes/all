package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.dto.SubBoxMatInfo;
import com.dosth.tool.common.util.MatInfoUtil;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.Manufacturer;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.Statement;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 结算分页封装
 * @author guozhidong
 *
 */
public class StatementPageWarpper extends PageWarpper<Statement> {

	public StatementPageWarpper(Page<Statement> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if (obj instanceof MatEquInfo) {
			MatEquInfo matInfo = (MatEquInfo) obj;
			map.put(key, MatInfoUtil.createSubBoxMatInfo(new SubBoxMatInfo(matInfo.getId(), matInfo.getMatEquName(), matInfo.getBarCode(), matInfo.getSpec())));
		} else if (obj instanceof Manufacturer) {
			map.put(key, ((Manufacturer) obj).getManufacturerName());
		} else if (obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser) obj));
		}
	}
}