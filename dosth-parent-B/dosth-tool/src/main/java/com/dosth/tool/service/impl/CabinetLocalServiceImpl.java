package com.dosth.tool.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.service.CabinetLocalService;
import com.dosth.tool.service.MatUseBillService;

/**
 * @description 柜子本地服务接口实现
 * @author guozhidong
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CabinetLocalServiceImpl implements CabinetLocalService {

	@Autowired
	private EquDetailStaRepository equDetailStaRepository;

	@Autowired
	private MatUseBillService matUseBillService;

	@Override
	public Map<MatEquInfo, Integer> getTotalQuantityGroupByMatEquInfo(String cabinetId) {
		List<EquDetailSta> staList = this.equDetailStaRepository.getEquDetailStaListNotNullMatBySettingId(cabinetId);
		return staList.stream()
				.filter(sta -> sta.getMatInfo() != null && sta.getStatus() != null
						&& UsingStatus.ENABLE.equals(sta.getStatus()) && sta.getStatus() != null
						&& UsingStatus.ENABLE.equals(sta.getStatus()) && sta.getCurNum() > 0)
				.collect(Collectors.groupingBy(EquDetailSta::getMatInfo,
						Collectors.summingInt(EquDetailSta::getCurNum)));
	}

	@Override
	public Map<String, Integer> getBorrCountGroupByMatId(String accountId) {
		List<MatUseBill> billList = this.matUseBillService.getUnReturnList(accountId);
		return billList.stream().collect(
				Collectors.groupingBy(MatUseBill::getMatInfoId, Collectors.summingInt(MatUseBill::getBorrowNum)));
	}
}