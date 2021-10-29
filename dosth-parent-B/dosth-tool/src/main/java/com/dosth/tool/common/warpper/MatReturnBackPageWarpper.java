package com.dosth.tool.common.warpper;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.warpper.PageWarpper;
import com.dosth.tool.common.state.AuditStatus;
import com.dosth.tool.common.state.VerifyMode;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.MatReturnBack;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.tool.vo.ViewUser;

public class MatReturnBackPageWarpper extends PageWarpper<MatReturnBack> {

	public MatReturnBackPageWarpper(Page<MatReturnBack> page) {
		super(page);
	}

	@Override
	protected void warpTheMap(Map<String, Object> map, String key, Object obj) {
		if(obj instanceof ViewUser) {
			map.put(key, ViewUserUtil.createViewUserName((ViewUser)obj));
		}else if(obj instanceof MatUseBill) {
			map.put(key, ((MatUseBill) obj).getMatEquName());
		}else if(obj instanceof RestitutionType) {
			map.put(key, ((RestitutionType) obj).getReturnBackType().getDesc());
		}else if(obj instanceof VerifyMode) {
			map.put(key, ((VerifyMode) obj).getMessage());
		}else if(obj instanceof AuditStatus) {
			map.put(key, ((AuditStatus) obj).getMessage());
		}
	}
}