package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.service.SubCabinetDetailService;

/**
 * 副柜明细Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class SubCabinetDetailServiceImpl implements SubCabinetDetailService {

	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;

	@Override
	public void save(SubCabinetDetail subCabinetDetail) throws DoSthException {
		this.subCabinetDetailRepository.save(subCabinetDetail);
	}

	@Override
	public SubCabinetDetail get(Serializable id) throws DoSthException {
		SubCabinetDetail subCabinetDetail = this.subCabinetDetailRepository.getOne(id);
		return subCabinetDetail;
	}

	@Override
	public SubCabinetDetail update(SubCabinetDetail subCabinetDetail) throws DoSthException {
		return this.subCabinetDetailRepository.saveAndFlush(subCabinetDetail);
	}

	@Override
	public void delete(SubCabinetDetail subCabinetDetail) throws DoSthException {
		this.subCabinetDetailRepository.delete(subCabinetDetail);
	}

	@Override
	public List<SubCabinetDetail> getSubDetailListBySubBoxId(String subBoxId) throws DoSthException {
		return this.subCabinetDetailRepository.getSubDetailListBySubBoxId(subBoxId);
	}

	@Override
	public List<SubCabinetDetail> getSubDetailList(String subBoxId, String matInfoId) {
		return this.subCabinetDetailRepository.getSubDetailList(subBoxId, matInfoId);
	}

	@Override
	public Map<String, Integer> getTotalQuantityGroupByMatId(String shareSwitch, String accountId)
			throws DoSthException {

		Map<String, Integer> map = new HashMap<>();
		List<SubCabinetDetail> list = new ArrayList<>();

		// 暂存柜共享开关
		if (TrueOrFalse.TRUE.toString().equals(shareSwitch)) {
			list = this.subCabinetDetailRepository.getAllSubCabinetDetailList();
		} else {
			list = this.subCabinetDetailRepository.getSubCabinetDetailList(accountId);
		}

		Map<String, List<SubCabinetDetail>> detailMap = list.stream()
				.collect(Collectors.groupingBy(SubCabinetDetail::getMatInfoId));
		detailMap.forEach((matId, detailList) -> {
			map.put(matId, detailList.stream().mapToInt(SubCabinetDetail::getNum).sum());
		});
		return map;
	}

	@Override
	public List<SubCabinetDetail> getSubDetailListByMatId(String matId) {
		return this.subCabinetDetailRepository.getSubDetailListByAccountIdAndMatId(matId);
	}

	@Override
	public List<SubCabinetDetail> getSubDetailListByAccountIdAndMatId(String accountId, String matId) {
		return this.subCabinetDetailRepository.getSubDetailListByAccountIdAndMatId(accountId, matId);
	}

	@Override
	public SubCabinetDetail outsubcabinet(String subboxId, String matInfoId) {
		List<SubCabinetDetail> list = this.subCabinetDetailRepository.getSubDetailListBySubboxIdAndMatId(subboxId,
				matInfoId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<MatEquInfo> getSubCabinetMatList(String accountId) {
		return this.subCabinetDetailRepository.getSubCabinetMatList(accountId);
	}

	@Override
	public List<SubCabinetDetail> findByAccountId(String accountId) {
		return this.subCabinetDetailRepository.getSubCabinetDetailList(accountId);
	}

	@Override
	public List<SubCabinetDetail> getSubCabinetDetailList() {
		return this.subCabinetDetailRepository.getAllMatInfos();
	}
}