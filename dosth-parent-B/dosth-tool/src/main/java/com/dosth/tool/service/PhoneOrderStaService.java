package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.mobile.PhoneOrder;
import com.dosth.tool.entity.mobile.PhoneOrderSta;

/**
 * 
 * @description 手机预约状态Service
 * @author guozhidong
 *
 */
public interface PhoneOrderStaService extends BaseService<PhoneOrderSta> {
	/**
	 * @description 根据订单编号获取订单状态
	 * @param orderId 订单编号
	 * @return
	 */
	public List<PhoneOrder> getFinishedOrderListByOrderId(String orderId);
}