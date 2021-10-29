package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.tool.FeignSupplier;
import com.cnbaosi.dto.tool.FeignSupporter;
import com.dosth.common.base.tips.SuccessTip;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.entity.Manufacturer;
import com.dosth.tool.entity.ManufacturerCustom;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.repository.ManufacturerCustomRepository;
import com.dosth.tool.repository.ManufacturerRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.service.ManufacturerService;

/**
 * 供货商Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class ManufacturerServiceImpl implements ManufacturerService {

	private static final Logger logger = LoggerFactory.getLogger(ManufacturerServiceImpl.class);

	@Autowired
	private ManufacturerRepository manufacturerRepository;
	@Autowired
	private ManufacturerCustomRepository manufacturerCustomRepository;
	@Autowired
	private MatEquInfoRepository matEquInfoRepository;

	@Override
	public void save(Manufacturer manufacturer) throws DoSthException {
		this.manufacturerRepository.save(manufacturer);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Manufacturer get(Serializable id) throws DoSthException {
		Manufacturer manufacturer = this.manufacturerRepository.findOne(id);
		return manufacturer;
	}

	@Override
	public Manufacturer update(Manufacturer manufacturer) throws DoSthException {
		return this.manufacturerRepository.saveAndFlush(manufacturer);
	}

	@Override
	public void delete(Manufacturer manufacturer) throws DoSthException {
		manufacturer.setStatus(UsingStatus.DISABLE);
		update(manufacturer);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<Manufacturer> getPage(int pageNo, int pageSize, String name, String status) throws DoSthException {
		Criteria<Manufacturer> criteria = new Criteria<Manufacturer>();
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.or(Restrictions.like("manufacturerName", name.trim(), true),
					Restrictions.like("contact", name.trim(), true), Restrictions.like("phone", name.trim(), true)));
		}
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			criteria.add(Restrictions.eq("status", UsingStatus.valueOf(status), true));
		}

		List<Manufacturer> list = this.manufacturerRepository.findAll(criteria);
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(list, pageable);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ZTreeNode> tree(Map<String, String> params) throws DoSthException {
		Criteria<Manufacturer> criteria = new Criteria<Manufacturer>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		List<Manufacturer> list = this.manufacturerRepository.findAll(criteria);
		List<ZTreeNode> treeNodeList = new ArrayList<>();
		ZTreeNode node;
		for (Manufacturer manufacturer : list) {
			node = new ZTreeNode(String.valueOf(manufacturer.getId()), String.valueOf(manufacturer.getId()),
					manufacturer.getManufacturerName());
			node.setLevel(0);
			node.setIcon("/static/img/comp.png");
			treeNodeList.add(node);
		}
		return treeNodeList;

	}

	@Override
	public OpTip syncSupplier(List<FeignSupplier> supplierList) {
		OpTip tip = new OpTip(200, "供应商同步成功");
		try {
			List<Manufacturer> manufacturerList;
			Manufacturer manufacturer;
			List<ManufacturerCustom> customList;
			ManufacturerCustom custom;
			for (FeignSupplier supplier : supplierList) {
				if (supplier.getSerialNo() != null && !"".equals(supplier.getSerialNo())) {
					manufacturerList = this.manufacturerRepository.getManufacturerByNo(supplier.getSerialNo());
				} else {
					manufacturerList = this.manufacturerRepository.getManufacturerByName(supplier.getName());
				}
				if (manufacturerList != null && manufacturerList.size() > 0) {
					manufacturer = manufacturerList.get(0);
				} else {
					manufacturer = new Manufacturer();
					manufacturer.setStatus(UsingStatus.ENABLE);
				}
				manufacturer.setManufacturerName(supplier.getName());
				manufacturer.setManufacturerNo(supplier.getSerialNo());
				manufacturer.setAddress(supplier.getAddress());
				manufacturer.setContact(supplier.getContact());
				manufacturer.setPhone(supplier.getPhone());
				manufacturer = this.manufacturerRepository.saveAndFlush(manufacturer);
				if (supplier.getSupporterList() != null && supplier.getSupporterList().size() > 0) {
					for (FeignSupporter supporter : supplier.getSupporterList()) {
						customList = this.manufacturerCustomRepository.getCustomListByCustomName(manufacturer.getId(),
								supporter.getContactName());
						if (customList != null && customList.size() > 0) {
							custom = customList.get(0);
						} else {
							custom = new ManufacturerCustom();
							custom.setStatus(UsingStatus.ENABLE);
						}
						custom.setManufacturerId(manufacturer.getId());
						custom.setContactName(supporter.getContactName());
						custom.setContactPhone(supporter.getContactPhone());
						custom.setMailAddress(supporter.getMailAddress());
						this.manufacturerCustomRepository.saveAndFlush(custom);
					}
				}
			}
		} catch (Exception e) {
			logger.error("供应商同步失败:" + e.getMessage());
			tip.setCode(201);
			tip.setMessage("供应商同步失败");
		}
		return tip;
	}

	@Override
	public Tip updateStatus(String type, String manufacturerId, String status) {
		Tip tip = new SuccessTip();
		if (type != null && "1".equals(type)) {
			if (status != null && "0".equals(status)) { // 停用添加物料绑定验证
				List<MatEquInfo> infoList = this.matEquInfoRepository.findAll();
				infoList = infoList.stream()
						.filter(info -> info.getManufacturerId() != null && manufacturerId.equals(info.getManufacturerId()))
						.collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(infoList)) {
					tip.setCode(201);
					tip.setMessage("当前供应商正在使用中,不可禁用!");
					return tip;
				}
			}
			Manufacturer manufacturer = this.manufacturerRepository.getOne(manufacturerId);
			manufacturer.setStatus(status != null && "1".equals(status) ? UsingStatus.ENABLE : UsingStatus.DISABLE);
			this.manufacturerRepository.saveAndFlush(manufacturer);
			
			// 禁用供应商下所有联系人
			List<ManufacturerCustom> customList = this.manufacturerCustomRepository.getCustomListByManufacturerId(manufacturerId, UsingStatus.ENABLE);
			if(CollectionUtils.isNotEmpty(customList)) {
				for(ManufacturerCustom custom : customList) {
					custom.setStatus(status != null && "1".equals(status) ? UsingStatus.ENABLE : UsingStatus.DISABLE);
					this.manufacturerCustomRepository.saveAndFlush(custom);
					
				}
			}
		} else {
			ManufacturerCustom custom = this.manufacturerCustomRepository.getOne(manufacturerId);
			custom.setStatus(status != null && "1".equals(status) ? UsingStatus.ENABLE : UsingStatus.DISABLE);
			this.manufacturerCustomRepository.saveAndFlush(custom);
		}
		return tip;
	}
}