package com.dosth.tool.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.YesOrNo;
import com.dosth.common.exception.DoSthException;
import com.dosth.dto.BorrowNotice;
import com.dosth.dto.TrolDrawerNotice;
import com.dosth.netty.dto.MsgType;
import com.dosth.netty.util.GlobalUserUtil;
import com.dosth.tool.common.state.BorrowType;
import com.dosth.tool.common.state.HardwareInfoType;
import com.dosth.tool.entity.DailyLimit;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.HardwareLog;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.repository.DailyLimitRepository;
import com.dosth.tool.repository.HardwareLogRepository;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.MatUseBillService;
import com.dosth.tool.service.MatUseRecordService;
import com.dosth.tool.service.ToolPullService;

/**
 * 工具提取Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class ToolPullServiceImpl implements ToolPullService {

	private static final Logger logger = LoggerFactory.getLogger(ToolPullServiceImpl.class);

	@Autowired
	private HardwareLogRepository hardwareLogRepository;

	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private MatUseRecordService matUseRecordService;
	@Autowired
	private MatUseBillService matUseBillService;
	@Autowired
	private DailyLimitRepository dailyLimitRepository;

	@Override
	public void toolPullBill(MatUseRecord record, EquDetailSta equDetailSta, Integer borrowNum, String accountId,
			String receiveType, String receiveInfo, Boolean isSyncStockNum) throws DoSthException {
		// 非同步库存更新借出记录实取数量
		if (isSyncStockNum != null && !isSyncStockNum) {
			MatEquInfo info = equDetailSta.getMatInfo();
			// 更新借出实领数量
			if (BorrowType.PACK.equals(info.getBorrowType())) {
				record.setRealNum(record.getRealNum() + borrowNum);
                record.setRealLife(record.getUseLife() + info.getUseLife());
				record.setMoney(record.getMoney() + (float) (Math.round(info.getStorePrice() * 1 * 10000)) / 10000);
			} else { 
				record.setRealNum(record.getRealNum() + info.getNum() * borrowNum);
                record.setRealLife(record.getUseLife() +  info.getNum() * info.getUseLife());
				record.setMoney(
						record.getMoney() + (float) (Math.round(info.getStorePrice() * info.getNum() * 10000)) / 10000);
			}
			this.matUseRecordService.update(record);
		}

		equDetailSta.setCurNum(equDetailSta.getCurNum() - borrowNum);

		// 推送最新数量到客户端
		Map<String, Integer> map = new HashMap<>();
		map.put(equDetailSta.getId(), equDetailSta.getCurNum());
		GlobalUserUtil.writeMsg(equDetailSta.getEquDetail().getEquSettingId(), MsgType.LATTICEVALUE, map);
		this.equDetailStaService.update(equDetailSta);

		// 更新最新数量到领用限额
		List<DailyLimit> limitList = this.dailyLimitRepository.findByMatId(accountId, equDetailSta.getMatInfoId());
		if (limitList != null && limitList.size() > 0) {
			DailyLimit limit = limitList.get(0);
			limit.setCurNum(limit.getCurNum() + borrowNum);
			limit.setOpDate(new Date());
			this.dailyLimitRepository.saveAndFlush(limit);
		}

		MatEquInfo info;
		Integer billNum;
		
		// 领用流水更新
		for (int num = 0; num < borrowNum; num++) {
			info = equDetailSta.getMatInfo();
			billNum = 1;
			if (BorrowType.METER.equals(info.getBorrowType())) {
				billNum = info.getNum();
			}
			this.matUseBillService.save(new MatUseBill(record.getId(), equDetailSta.getId(), info.getId(),
					info.getBarCode(), info.getMatEquName(), info.getSpec(), info.getManufacturer().getManufacturerName(),
					info.getBrand(), info.getStorePrice(), billNum, accountId));
		}

		// 当库存到达库存阈值时，发送邮件给管理员
//		if (equDetailSta.getCurNum() <= equDetailSta.getWarnVal()) {
//			try {
//				FeedingEmailUtil
//						.putFeedingEmailInfo(new FeedingEmail(equDetailSta.getId(), equDetailSta.getMatInfoId()));
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public void notice(BorrowNotice notice) throws DoSthException {
		try {
			MatUseRecord record = this.matUseRecordService.get(notice.getRecordId());
			EquDetailSta sta = this.equDetailStaService.get(notice.getStaId());
			HardwareLog log = new HardwareLog(HardwareInfoType.MOTOR, new StringBuilder("F")
					.append(sta.getEquDetail().getRowNo()).append("-").append(sta.getColNo()).toString(),
					record.getAccountId());
			if (notice.getFlag() != null && notice.getFlag()) { // 马达成功
				// 更新库存信息
				sta.setCurNum(sta.getCurNum() - 1);
				this.equDetailStaService.update(sta);

				// 物料信息
				MatEquInfo info = sta.getMatInfo();

				// 更新借出实领数量
				if (BorrowType.PACK.equals(info.getBorrowType())) {
					record.setRealNum(record.getRealNum() + 1);
                    record.setRealLife(record.getRealLife() + info.getUseLife());
					record.setMoney(record.getMoney() + (float) (Math.round(info.getStorePrice() * 1 * 10000)) / 10000);
				} else {
					record.setRealNum(record.getRealNum() + info.getNum());
					record.setRealLife(record.getRealLife() +info.getNum() * info.getUseLife());
					record.setMoney(record.getMoney()
							+ (float) (Math.round(info.getStorePrice() * info.getNum() * 10000)) / 10000);
				}
				this.matUseRecordService.update(record);

				Integer billNum = 1;
				
				if (BorrowType.METER.equals(info.getBorrowType())) {
					billNum = info.getNum();
				}
				// 添加领取流水
				this.matUseBillService.save(new MatUseBill(record.getId(), sta.getId(), info.getId(),
						info.getBarCode(), info.getMatEquName(), info.getSpec(), info.getManufacturer().getManufacturerName(),
						info.getBrand(), info.getStorePrice(), billNum, record.getAccountId()));

				// 更新最新数量到领用限额
				List<DailyLimit> limitList = this.dailyLimitRepository.findByMatId(record.getAccountId(),
						sta.getMatInfoId());
				if (limitList != null && limitList.size() > 0) {
					DailyLimit limit = limitList.get(0);
					limit.setCurNum(limit.getCurNum() + 1);
					limit.setOpDate(new Date());
					this.dailyLimitRepository.saveAndFlush(limit);
				}
				log.setIsSucc(YesOrNo.YES);
			} else { // 马达失败
				log.setIsSucc(YesOrNo.NO);
			}

			// 硬件运行日志更新
			this.hardwareLogRepository.save(log);

			// 当库存到达库存阈值时，发送邮件给管理员
//			if (sta.getCurNum() <= sta.getWarnVal()) {
//				try {
//					FeedingEmailUtil.putFeedingEmailInfo(new FeedingEmail(sta.getId(), sta.getMatInfoId()));
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("扣库存异常数据:" + notice.toString());
			throw new DoSthException(201, "扣库存异常数据:" + notice.toString(), null);
		}
	}

	@Override
	public void noticeTrolDrawerNotice(TrolDrawerNotice notice) {
		try {
			MatUseRecord record = this.matUseRecordService.get(notice.getRecordId());
			EquDetailSta sta = this.equDetailStaService.get(notice.getStaId());
			int realNum = notice.getRealNum() / sta.getInterval();
			// 更新库存信息
			sta.setCurNum(sta.getCurNum() - realNum);
			this.equDetailStaService.update(sta);

			// 物料信息
			MatEquInfo info = sta.getMatInfo();

			// 更新借出实领数量
			if (BorrowType.PACK.equals(info.getBorrowType())) {
				record.setRealNum(record.getRealNum() + realNum);
                record.setRealLife(record.getRealLife() + info.getUseLife());
				record.setMoney(record.getMoney() + (float) (Math.round(info.getStorePrice() * realNum * 10000)) / 10000);
			} else {
				record.setRealNum(record.getRealNum() + info.getNum() * realNum);
				record.setRealLife(record.getRealLife() + info.getNum() * realNum * info.getUseLife());
				record.setMoney(record.getMoney()
						+ (float) (Math.round(info.getStorePrice() * realNum * info.getNum() * 10000)) / 10000);
			}
			this.matUseRecordService.update(record);

			Integer billNum = 1;
			
			if (BorrowType.METER.equals(info.getBorrowType())) {
				billNum = info.getNum();
			}
			for (int i = 0; i < realNum; i++) {
				// 添加领取流水
				this.matUseBillService.save(new MatUseBill(record.getId(), sta.getId(), info.getId(),
						info.getBarCode(), info.getMatEquName(), info.getSpec(), info.getManufacturer().getManufacturerName(),
						info.getBrand(), info.getStorePrice(), billNum, record.getAccountId()));
			}

			// 更新最新数量到领用限额
			List<DailyLimit> limitList = this.dailyLimitRepository.findByMatId(record.getAccountId(),
					sta.getMatInfoId());
			if (limitList != null && limitList.size() > 0) {
				DailyLimit limit = limitList.get(0);
				limit.setCurNum(limit.getCurNum() + realNum);
				limit.setOpDate(new Date());
				this.dailyLimitRepository.saveAndFlush(limit);
			}

			HardwareLog log = new HardwareLog(HardwareInfoType.MOTOR, new StringBuilder("F")
					.append(sta.getEquDetail().getRowNo()).append("-").append(sta.getColNo()).toString(), record.getAccountId());
			log.setIsSucc(notice.getAmount() == notice.getRealNum() ? YesOrNo.YES : YesOrNo.NO);
			// 硬件运行日志更新
			this.hardwareLogRepository.save(log);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("扣库存异常数据:" + notice.toString());
			throw new DoSthException(201, "扣库存异常数据:" + notice.toString(), null);
		}
	}
}