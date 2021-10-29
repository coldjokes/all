package com.dosth.tool.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.criteria.EquSettingCriteria;
import com.dosth.dto.ExtraCabinet;
import com.dosth.tool.entity.EquSetting;
import com.dosth.toolcabinet.dto.CabinetInfo;

/**
 * 柜体管理Service
 * 
 * @author guozhidong
 *
 */
public interface EquSettingService extends BaseService<EquSetting> {
	/**
	 * 分页方法
	 * 
	 * @param pageNo   当前页码
	 * @param pageSize 每页大小
	 * @param name     设备
	 * @param status   是否启用状态
	 * @return
	 * @throws DoSthException
	 */
	public Page<EquSetting> getPage(int pageNo, int pageSize, String name, String cabinetType) throws DoSthException;

	/**
	 * 根据设备设置Id获取设置信息(设备详情)
	 * 
	 * @param equSettingId 设备设置Id
	 * @return
	 * @throws DoSthException
	 */
	public EquSetting getEquInfos(String equSettingId) throws DoSthException;

	/**
	 * 创建设备树
	 * 
	 * @return
	 * @throws DoSthException
	 */
	public List<ZTreeNode> treeEqu() throws DoSthException;

	/**
	 * 初始化存储设备树
	 * 
	 * @return
	 * @throws DoSthException
	 */
	public List<ZTreeNode> initEquSettingTree(String equSettingId) throws DoSthException;

	/**
	 * 根据修改刀具柜IP,端口
	 * 
	 * @param equSettingId
	 * @param ip
	 * @param port
	 * @throws DoSthException
	 */
	public EquSetting editCabinet(String equSettingId) throws DoSthException;

	/**
	 * 查询所有的刀具柜
	 * 
	 * @throws DoSthException
	 */
	public List<EquSetting> findAllCabinet(EquSettingCriteria equSettingCriteria);

	/**
	 * 获取子柜Id
	 * 
	 * @param cabinetId 柜子Id @return`
	 * @throws DoSthException
	 */
	public String getEquSettingListByEquInfoId(String cabinetId) throws DoSthException;

	public CabinetInfo getCabinetInfo(String cabinetId) throws DoSthException;

	/**
	 * @description 获取柜子列表信息
	 * @param serialNo 柜子序号
	 * @return
	 */
	public List<ExtraCabinet> getCabinetList(@RequestParam("serialNo") String serialNo);

	/**
	 * @description 根据序列号查询刀具柜信息
	 * @param serialNo
	 * @return
	 */
	public EquSetting getEquSettingBySerialNo(String serialNo);

	/**
	 * 分页方法
	 * 
	 * @param pageNo             当前页码
	 * @param pageSize           每页大小
	 * @param equSettingParentId 主柜id
	 * @return
	 * @throws DoSthException
	 */
	public Page<EquSetting> getsubPage(int pageNo, int pageSize, String equSettingParentId);

	/**
	 * @description 根据主柜Id获取柜体树形集合
	 * @param mainCabinetId 主柜Id
	 * @return
	 */
	public List<ExtraCabinet> getCabinetTreeList(String mainCabinetId);

	/**
	 * @description 获取柜子列表
	 * @return
	 */
	public List<EquSetting> getCabList();

	/**
	 * @description 根据仓库别名获取柜体列表
	 * @param wareHouseAlias 仓库别名
	 * @return
	 */
	public List<EquSetting> getCabinetListByWareHouseAlias(String wareHouseAlias);

	/**
	 * @description 创建设备+层级
	 * @return
	 */
	public List<ZTreeNode> treeCabDetail();
}