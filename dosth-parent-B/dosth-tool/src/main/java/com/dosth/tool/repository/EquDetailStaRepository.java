package com.dosth.tool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.MatEquInfo;

/**
 * 
 * @description 设备详情状态持久化层
 * @author guozhidong
 *
 */
public interface EquDetailStaRepository extends BaseRepository<EquDetailSta, String> {
	/**
	 * @description 根据设备设置Id获取设备详情列表
	 * @param equSettingId 设备设置Id
	 * @return
	 */
	@Query("from EquDetailSta i where i.equDetail.equSettingId = :equSettingId")
	public List<EquDetailSta> getEquDetailStaListBySettingId(String equSettingId);

	/**
	 * @description 根据设备设置Id获取设备子级级联详情列表
	 * @param equSettingId 设备设置Id
	 * @return
	 */
	@Query("from EquDetailSta i where i.equDetail.equSettingId = :equSettingId or i.equDetail.equSetting.equSettingParentId = :equSettingId and i.equDetail.equSetting.cabinetType <> 'VIRTUAL_WAREHOUSE'")
	public List<EquDetailSta> getEquDetailStaTreeListBySettingId(String equSettingId);

	/**
	 * @description 根据设备设置Id获取设备详情列表(已设置物料)
	 * @param cabinetId 设备设置Id
	 * @return
	 */
	@Query("select i from EquDetailSta i left join i.equDetail.equSetting s where s.cabinetType <> 'VIRTUAL_WAREHOUSE' and s.cabinetType <> 'TROL_DRAWER' and i.matInfoId is not null and (i.equDetail.equSettingId = :cabinetId or s.equSettingParentId = :cabinetId)")
	public List<EquDetailSta> getEquDetailStaListNotNullMatBySettingId(String cabinetId);

	/**
	 * @description 根据详情Id获取详情列表
	 * @param equDetailId 设备详情Id
	 * @return
	 */
	@Query("from EquDetailSta i where i.equDetailId = :equDetailId order by i.colNo")
	public List<EquDetailSta> getStaListByDetailId(String equDetailId);

	/**
	 * @description 根据柜子Id获取所设立所有存放物料集合
	 * @param cabinetId 柜子Id
	 * @param status    柜子启用状态
	 * @return
	 */
	@Query("select s.matInfo from EquDetailSta s where s.equDetail.equSettingId = :cabinetId and s.status = :status")
	public List<MatEquInfo> getMatInfoListByCabinetId(String cabinetId, UsingStatus status);

	/**
	 * 根据物料Id查询所有主柜
	 * 
	 * @param matInfoId 物料Id
	 * @return
	 */
	@Query("from EquDetailSta s where s.matInfoId = ?1")
	public List<EquDetailSta> getCabinetListByEquInfoId(String matInfoId);
	
	/**
	 * 根据物料Id查询&柜子Id查询库存信息
	 * 
	 * @param matInfoId 物料Id
	 * @return
	 */
	@Query("from EquDetailSta s where s.matInfoId =:matInfoId and s.equDetail.equSettingId =:equSettingId")
	public List<EquDetailSta> getStocks(String equSettingId, String matInfoId);

	/**
	 * 根据物料Id查询格子详情
	 * 
	 * @param matInfoId 物料Id
	 * @return
	 */
	@Query("from EquDetailSta s where s.matInfoId = ?1")
	public EquDetailSta getStaByMatId(String matInfoId);
	
	/**
	 * 根据物料查询格子详情
	 * 
	 * @param barCode 物料编号
	 * @return
	 */
	@Query("from EquDetailSta s where s.matInfo.barCode =:barCode and s.equDetail.equSettingId =:cabinetId")
	public List<EquDetailSta> getStaByBarCode(String barCode, String cabinetId);


	/**
	 * 根据格子Id查询格子详情
	 * 
	 * @param matInfoId 物料Id
	 * @return
	 */
	@Query("from EquDetailSta s where s.id =:equDetailStaId")
	public EquDetailSta getStaByStaId(String equDetailStaId);

	/**
	 * @description 指定物料在刀具柜分布信息
	 * @param equSettingId 刀具柜Id
	 * @param matInfoId    物料Id
	 * @return
	 */
	@Query("select s from EquDetailSta s left join s.equDetail d where (d.equSetting.cabinetType = 'VIRTUAL_WAREHOUSE' and d.equSetting.equSettingParentId is null) or d.equSettingId =:equSettingId or d.equSetting.equSettingParentId = :equSettingId and s.matInfoId =:matInfoId")
	public List<EquDetailSta> getStaByCart(String equSettingId, String matInfoId);
	
	/**
	 * @description 根据柜子Id获取设备详情列表(副柜)
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@Query("from EquDetailSta i where (i.equDetail.equSetting.cabinetType = 'VIRTUAL_WAREHOUSE' and  i.equDetail.equSetting.equSettingParentId is null) or i.equDetail.equSettingId = :cabinetId or i.equDetail.equSetting.equSettingParentId = :cabinetId")
	public List<EquDetailSta> getEquDetailStaListByCabinetId(String cabinetId);

	/**
	 * @description 根据柜子Id获取空闲格子
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@Query("from EquDetailSta sta where (sta.matInfoId is null or sta.matInfoId = '') and sta.equDetail.equSettingId = :cabinetId")
	public List<EquDetailSta> getEmptyNumByCabinetId(String cabinetId);

	/**
	 * @description 物料库存查询
	 * @return
	 */
	@Query("select sum(i.curNum) from EquDetailSta i where i.matInfoId = :staId and i.equDetail.equSettingId = :cabinetId or i.equDetail.equSetting.equSettingParentId = :cabinetId and i.equDetail.equSetting.cabinetType <> 'VIRTUAL_WAREHOUSE'")
	public int curNumCheck(String staId, String cabinetId);

	/**
	 * @description 根据柜子Id获取柜子详情
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@Query("from EquDetailSta sta where sta.equDetail.equSettingId = :cabinetId")
	public List<EquDetailSta> getStaListByCabinetId(String cabinetId);

	/**
	 * @description 根据仓库别名获取货道列表
	 * @param wareHouseAlias 仓库别名
	 * @return
	 */
	@Query("from EquDetailSta s where s.equDetail.equSetting.wareHouseAlias = :wareHouseAlias")
	public List<EquDetailSta> getStaListByWareHouseAlias(String wareHouseAlias);
	
	/**
	 * @description 根据行列号获取货道信息
	 * @param wareHouseAlias 仓库别名
	 * @param rowNo
	 * @param colNo
	 * @return
	 */
	@Query("from EquDetailSta s where s.equDetail.equSetting.wareHouseAlias = :wareHouseAlias and s.equDetail.rowNo = :rowNo and s.colNo = :colNo")
	public List<EquDetailSta> getStaByRowCol(String wareHouseAlias, Integer rowNo, Integer colNo);
	
	/**
	 * @description 清除物料相关数据
	 */
	@Modifying
	@Transactional
	@Query("update from EquDetailSta set curNum = 0, matInfoId = null")
	public void deleteByMat();
	
	/**
	 * @description 根据柜体Id删除货道
	 * @param equSettingId 柜体Id
	 */
	@Modifying
	@Transactional
	@Query("delete from EquDetailSta s where exists(select 1 from s.equDetail d where d.equSettingId = :equSettingId)")
	public void deleteEquDetailStaByCabinetId(String equSettingId);

	/**
	 * @description 根据物料Id获取物料在用货道
	 * @param infoId 物料Id
	 */
	@Query("from EquDetailSta sta where sta.matInfoId = :infoId")
	public List<EquDetailSta> findEquDetailStaListByMatInfoId(String infoId);
}