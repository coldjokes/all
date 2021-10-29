package com.dosth.tool.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.tool.FeignMat;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.dto.Lattice;
import com.dosth.tool.entity.MatEquInfo;

/**
 * @description 物料/设备信息Service
 * 
 * @author guozhidong
 *
 */
public interface MatEquInfoService extends BaseService<MatEquInfo> {
	/**
	 * @description 分页方法
	 * 
	 * @param pageNo   当前页码
	 * @param pageSize 每页大小
	 * @param name     名称
	 * @param status   是否启用状态
	 * @return
	 * @throws DoSthException
	 */
	public Page<MatEquInfo> getPage(int pageNo, int pageSize, String name, String status) throws DoSthException;

	/**
	 * @description 创建物料/设备Tree
	 * 
	 * @param params
	 * @return
	 * @throws DoSthException
	 */
	public List<ZTreeNode> tree(Map<String, String> params) throws DoSthException;

	/**
	 * @description 修改
	 * @param matEquInfo
	 * @return
	 * @throws DoSthException
	 */
	public MatEquInfo edit(MatEquInfo matEquInfo) throws DoSthException;

	/**
	 * @description 导出
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String exportExcel(HttpServletRequest request, HttpServletResponse response, String name, String status)
			throws IOException;

	/**
	 * @description 产品详情
	 * @param matId 物料Id
	 * @return
	 * @throws IOException
	 */
	public Lattice getPro(String matId, String latticeId) throws DoSthException;

	/**
	 * @description 获取物料剩余数量
	 * @param cabinetId 当前柜子Id
	 * @param matId     物料Id
	 * @return
	 */
	public List<MatEquInfo> findByMatId(String matId);

	/**
	 * @description 获取全部物料信息列表
	 * @param
	 * @return
	 */
	public Page<MatEquInfo> getAllMatInfo(int pageNo, int pageSize, String params);

	/**
	 * @description 获取物料列表
	 * @param matNameBarCodeSpec 物料名称编号型号
	 * @return
	 */
	public List<MatEquInfo> getMatInfoList(String matNameBarCodeSpec);

	/**
	 * @description 根据物料编号获取物料详情
	 * @param barCode 物料编号
	 * @return
	 */
	public MatEquInfo getMatDetailByBarCode(String barCode);

	/**
	 * @description 物料同步
	 * @param matList 物料列表
	 * @return
	 */
	public OpTip syncMat(List<FeignMat> matList);

	/**
	 * @description 更新物料状态 @param infoId 物料Id @throws
	 */
	public void updateStatus(String infoId) throws DoSthException;
}