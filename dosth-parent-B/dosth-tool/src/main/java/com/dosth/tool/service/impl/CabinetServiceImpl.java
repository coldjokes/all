package com.dosth.tool.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.node.ZTreeNode;
import com.dosth.enums.CabinetType;
import com.dosth.tool.common.state.StorageType;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.service.CabinetService;

/**
 * @description 柜体Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class CabinetServiceImpl implements CabinetService {

	@Autowired
	private EquSettingRepository equSettingRepository;

	@Override
	public List<ZTreeNode> createCabinetTree() {
		List<ZTreeNode> tree = new ArrayList<>();

		// 绑定主柜数据
		Criteria<EquSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		criteria.add(Restrictions.ne("cabinetType", CabinetType.RECOVERY_CABINET, true));
		List<EquSetting> settingList = this.equSettingRepository.findAll(criteria);
		ZTreeNode node = null;
		// 绑定暂存柜数据
		if (settingList != null && settingList.size() > 0) {
			for (EquSetting setting : settingList) {
				if (setting.getEquSettingParentId() != null && !"".equals(setting.getEquSettingParentId())) {
					if (setting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_A)
							|| setting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_B)
							|| setting.getEquSettingParent().getCabinetType().equals(CabinetType.KNIFE_CABINET_C)) {
						node = new ZTreeNode(setting.getId(), "C_" + setting.getEquSettingParentId(),
								setting.getEquSettingName());
						node.setLevel(1);
						node.setIcon("/static/img/one.png");
					} else {
						node = new ZTreeNode(setting.getId(), setting.getEquSettingParentId(),
								setting.getEquSettingName());
						node.setLevel(0);
						node.setIcon("/static/img/two.png");
					}
				} else {
					if (setting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C)) {
						node=new ZTreeNode("C_" + setting.getId(), setting.getId(), setting.getEquSettingName());
						node.setLevel(0);
						node.setIcon("/static/img/two.png");
					} else {
						node=new ZTreeNode(setting.getId(), setting.getId(), setting.getEquSettingName());
//						tree.add(new ZTreeNode(setting.getId(), setting.getId(), setting.getEquSettingName()));
						node.setLevel(1);
						node.setIcon("/static/img/one.png");
					}
				}
				tree.add(node);
			}
		}
		return tree;
	}

	@Override
	public List<ZTreeNode> createMainCabinetTree() {
		List<ZTreeNode> tree = new ArrayList<>();
		// 绑定主柜数据
		Criteria<EquSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		List<EquSetting> settingList = this.equSettingRepository.findAll(criteria);
		for (EquSetting setting : settingList) {
			if (setting.getEquSettingParentId() != null && !"".equals(setting.getEquSettingParentId())) {
				tree.add(new ZTreeNode(setting.getId(), "E_" + setting.getEquSettingParentId(),
						setting.getEquSettingName()));
			} else {
				tree.add(new ZTreeNode("E_" + setting.getId(), StorageType.MAIN.name(), setting.getEquSettingName()));
				tree.add(new ZTreeNode(setting.getId(), "E_" + setting.getId(), setting.getEquSettingName()));
			}
		}
		return tree;
	}
}