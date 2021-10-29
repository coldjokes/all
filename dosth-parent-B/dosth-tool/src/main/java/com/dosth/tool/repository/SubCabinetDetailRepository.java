package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubCabinetDetail;

/**
 * 
 * @description 副柜详情持久化层
 * @author guozhidong
 *
 */
public interface SubCabinetDetailRepository extends BaseRepository<SubCabinetDetail, String> {

	/**
	 * @description 由副柜格子获取指定物料的存储列表
	 * @param subBoxId  副柜格子Id
	 * @param matInfoId 物料Id
	 * @return
	 */
	@Query("from SubCabinetDetail where subBoxId = :subBoxId and matInfoId = :matInfoId")
	public List<SubCabinetDetail> getSubDetailList(String subBoxId, String matInfoId);

	/**
	 * 根据subBoxId查询副柜详情
	 * 
	 * @param subBoxId
	 * @return
	 */
	@Query("from SubCabinetDetail where subBoxId = :subBoxId")
	public List<SubCabinetDetail> getSubDetailListBySubBoxId(String subBoxId);

	/**
	 * @description 指定帐户暂存柜物料分组统计
	 * @param accountId 帐户Id
	 * @return
	 */
	@Query("select d from SubCabinetDetail d, SubBoxAccountRef r where d.subBoxId = r.subBoxId and r.accountId = :accountId")
	public List<SubCabinetDetail> getSubCabinetDetailList(String accountId);

	/**
	 * @description 全部暂存柜物料分组统计
	 * @param
	 * @return
	 */
	@Query("select d from SubCabinetDetail d, SubBoxAccountRef r where d.subBoxId = r.subBoxId")
	public List<SubCabinetDetail> getAllSubCabinetDetailList();

	/**
	 * @description 根据帐户Id和物料Id获取副柜详情
	 * @param accountId 帐户Id
	 * @param matId     物料Id
	 * @return
	 * @throws DoSthException
	 */
	@Query("select d from SubCabinetDetail d, SubBoxAccountRef r where d.subBoxId = r.subBoxId and r.accountId = :accountId and d.matInfoId = :matId")
	public List<SubCabinetDetail> getSubDetailListByAccountIdAndMatId(String accountId, String matId);

	/**
	 * @description 根据物料Id获取副柜详情
	 * @param matId 物料Id
	 * @return
	 * @throws DoSthException
	 */
	@Query("select d from SubCabinetDetail d, SubBoxAccountRef r where d.subBoxId = r.subBoxId and d.matInfoId = :matId")
	public List<SubCabinetDetail> getSubDetailListByAccountIdAndMatId(String matId);

	/**
	 * @description 根据暂存柜盒子和物料获取物料详情
	 * @param subboxId  暂存柜盒子Id
	 * @param matInfoId 物料信息Id
	 * @return
	 */
	@Query("from SubCabinetDetail d where d.subBoxId = :subboxId and d.matInfoId = :matInfoId")
	public List<SubCabinetDetail> getSubDetailListBySubboxIdAndMatId(String subboxId, String matInfoId);

	/**
	 * @description 指定帐户暂存柜物料信息
	 * @param accountId 帐户Id
	 * @return
	 */
	@Query("select d.matInfo from SubCabinetDetail d, SubBoxAccountRef r where d.subBoxId = r.subBoxId and r.accountId = :accountId")
	public List<MatEquInfo> getSubCabinetMatList(String accountId);

	/**
	 * @description 查询所有存放物料暂存柜列表
	 */
	@Query("from SubCabinetDetail d where d.matInfoId is not null")
	public List<SubCabinetDetail> getAllMatInfos();

	/**
	 * @description 查询暂存柜库存
	 */
	@Query("select sum(num) from SubCabinetDetail d where d.matInfoId = :matInfoId")
	public String getMatInfoNum(String matInfoId);
}