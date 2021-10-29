package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.enums.SetupKey;
import com.dosth.tool.entity.CabinetSetup;
import com.dosth.tool.repository.CabinetSetupRepository;
import com.dosth.tool.service.CabinetSetupService;

/**
 * 刀具柜配置
 * 
 * @author WeiFeng.Li
 *
 */
@Service
public class CabinetSetupServiceImpl implements CabinetSetupService {

	@Autowired
	private CabinetSetupRepository cabinetSetupRepository;

	@Override
	public void save(CabinetSetup entity) throws DoSthException {
		this.cabinetSetupRepository.save(entity);
	}

	@Override
	public CabinetSetup get(Serializable id) throws DoSthException {
		return this.cabinetSetupRepository.findOne(id);
	}

	@Override
	public CabinetSetup update(CabinetSetup entity) throws DoSthException {
		return this.cabinetSetupRepository.saveAndFlush(entity);
	}

	@Override
	public void delete(CabinetSetup entity) throws DoSthException {
		this.cabinetSetupRepository.delete(entity);
	}

	@Override
	public Map<String, CabinetSetup> getCabinetSetupByEquSettingId(String equSettingId) {
		Criteria<CabinetSetup> criteria = new Criteria<>();
		if (equSettingId != null && !"".equals(equSettingId)) {
			criteria.add(Restrictions.eq("equSettingId", equSettingId, true));
		}
		List<CabinetSetup> list = this.cabinetSetupRepository.findAll(criteria);

		Map<String, CabinetSetup> map = list.stream()
				.collect(Collectors.toMap(CabinetSetup::getSetupKey, cabinetSetup -> cabinetSetup, (k1, k2) -> k1));
		return map;
	}

	@Override
	public List<CabinetSetup> getCabinetSetupBySerialNo(String serialNo) {
		return this.cabinetSetupRepository.getCabinetSetupBySerialNo(serialNo);
	}

	@Override
	public String getValueByCabinetIdAndKey(String cabinetId, String key) {
		List<String> valueList = this.cabinetSetupRepository.getValueByCabinetIdAndKey(cabinetId, key);
		return valueList != null && valueList.size() > 0 ? valueList.get(0) : null;
	}

	@Override
	public List<CabinetSetup> getStoreCabinetSetupByEquSettingId(String mainCabinetId) {
		List<CabinetSetup> setUpList = this.cabinetSetupRepository.getCabinetSetupListByMainCabinetId(mainCabinetId);
		// 由附属柜体参数过滤出储物柜串口和波特率
		return setUpList.stream().filter(setup -> setup.getSetupKey().equals(SetupKey.StoreCabinet.STORE_COM) 
				|| setup.getSetupKey().equals(SetupKey.StoreCabinet.STORE_BAUD)).collect(Collectors.toList());
	}
	
	/**
	 * 根据id查询switch的值
	 * */
	@Override
	public CabinetSetup getShareSwitch(String id) {
		Criteria<CabinetSetup> c = new Criteria<>();
		c.add(Restrictions.eq("setupKey", SetupKey.TemCabinet.TEM_SHARE_SWITCH.toString(), true));
		if(StringUtils.isNotBlank(id)) {
			c.add(Restrictions.eq("equSettingId", id, true));
		}
		return this.cabinetSetupRepository.findAll(c).get(0);
	 
	}
}