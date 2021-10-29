package com.dosth.tool.rpc.Impl;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.netty.dto.MsgType;
import com.dosth.netty.util.GlobalUserUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.rpc.MobileRpcService;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.toolcabinet.dto.PrintScan;

/**
 * 
 * @description 移动端远程接口
 * @author guozhidong
 *
 */
@Service
@Transactional
public class MobileRpcServiceImpl implements MobileRpcService {
	
	private static final Logger logger = LoggerFactory.getLogger(MobileRpcServiceImpl.class);

	@Autowired
	private EquDetailStaService equDetailStaService;
	
	@Override
	public void addPhoneBooking(String equDetailStaId, String matInfoId, Integer borrNum, String accountId) {
		EquDetailSta sta = this.equDetailStaService.get(equDetailStaId);
		if (sta != null) {
			GlobalUserUtil.writeMsg(sta.getEquDetail().getEquSettingId(), MsgType.PRINTSCAN, 
					new PrintScan("accountId:" + accountId + "<<" + equDetailStaId));
		}
	}

	@Override
	public void removePhoneBooking(String cabinetId, String bookId, String accountId) {
		GlobalUserUtil.writeMsg(cabinetId, MsgType.PRINTSCAN, new PrintScan(false, "accountId:" + accountId + ">>" + bookId));
		int count = new Random().nextInt(100) + 1;
		if (count % 3 == 0) {
			logger.info("条件成立,发送打印机暂停");
		}
	}
}