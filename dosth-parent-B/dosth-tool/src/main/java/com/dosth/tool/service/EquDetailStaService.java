package com.dosth.tool.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cnbaosi.dto.ApiFeignResponse;
import com.cnbaosi.dto.tool.FeignCabinet;
import com.cnbaosi.dto.tool.FeignStock;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.dto.StockTip;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.mobile.PhoneOrderMatDetail;
import com.dosth.tool.entity.vo.EquDetailStaVo;
import com.dosth.toolcabinet.dto.FeedDetailSta;
import com.dosth.util.OpTip;

/**
 * @description 设备详情Service
 * 
 * @author guozhidong
 *
 */
public interface EquDetailStaService extends BaseService<EquDetailSta> {
	/**
	 * @description 设置物料存储设置
	 * 
	 * @param equDetailSta 设备详情状态
	 * @param accountId    当前操作账号Id
	 * @return
	 * @throws DoSthException
	 */
	public OpTip storage(EquDetailSta equDetailSta, String accountId) throws DoSthException;

	/**
	 * 补料清单
	 * 
	 * @param response
	 * @param arrs
	 * @return
	 * @throws IOException
	 */
	public OpTip feedList(String arrs, String equSettingId, String accountId, FeedType feedType) throws IOException;

	/**
	 * @description 根据柜子Id获取柜子设置详情列表
	 * @param cabinetId 柜子Id, 参数为空，则查询全部柜子
	 * @return
	 * @throws DoSthException
	 */
	public List<EquDetailSta> getEquDetailStaListByCabinetId(String cabinetId) throws DoSthException;

	/**
	 * @description 根据柜子Id获取柜子设置详情列表
	 * @param cabinetId 柜子Id, 参数为空，则查询全部柜子
	 * @return
	 * @throws DoSthException
	 */
	public List<EquDetailSta> getEquDetailStaTreeListByCabinetId(String cabinetId) throws DoSthException;

	/**
	 * @description 获取已预约物料详情
	 * @param
	 * @return
	 * @throws DoSthException
	 */
	public List<PhoneOrderMatDetail> getPhoneOrderMatDetail() throws DoSthException;

	/**
	 * @description 获取刀具柜单元格数量信息
	 * @param cabinetId 刀具柜Id
	 * @return
	 * @throws DoSthException
	 */
	public Map<String, Integer> getLatticeValueMap(String cabinetId) throws DoSthException;

	/**
	 * @description 根据设备详情Id获取设备详情状态列表
	 * @param equDetailId 设备详情Id
	 * @return
	 * @throws DoSthException
	 */
	public List<EquDetailSta> getStaListByDetailId(String equDetailId) throws DoSthException;

	/**
	 * @description 根据柜子Id获取所设立所有存放物料集合
	 * @param cabinetId 柜子Id
	 * @return
	 * @throws DoSthException
	 */
	public List<MatEquInfo> getMatInfoListByCabinetId(String cabinetId) throws DoSthException;

	/**
	 * @description 根据柜子Id获取库存列表
	 * 
	 * @param cabinetId 柜子Id
	 * @param query     查询范围条件
	 * @param search    查询过滤条件
	 * @return
	 */
	public List<FeedDetailSta> getFeedDetailStaList(String cabinetId, String query, String search);

	/**
	 * @description 物料上架
	 * @param equDetailStaId
	 * @param matInfoId
	 * @return
	 */
	public OpTip upFrame(String equDetailStaId, String matInfoId, String accountId);

	/**
	 * @description 物料批量上架
	 * @param accountId
	 * @param cabinetId
	 * @param array
	 * @return
	 */
	public OpTip upFrameAll(String accountId, String cabinetId, List<String> list);

	/**
	 * @description 柜子故障报备
	 * @param staId 柜子索引号
	 * @return
	 */
	public Boolean equStaProblem(String staId);

	/**
	 * @description 故障状态恢复
	 * @param cabinetId 柜子Id
	 * @return
	 */
	public Boolean resetProblem(String cabinetId);

	/**
	 * @description 当前库存检查
	 * @param billId
	 * @return
	 */
	public OpTip curNumCheck(String staId, int num, String cabinetId);

	/**
	 * @description 根据柜子Id获取柜子详情
	 * @param cabinetId 柜子Id
	 * @return
	 */
	public List<EquDetailSta> getStaListByCabinetId(String cabinetId);

	/**
	 * @description 物料上架
	 * @param matIds 物料Ids
	 * @param staIds 货位Ids
	 * @return
	 */
	public OpTip matIn(String matIds, String staIds);

	/**
	 * @description 物料下架
	 * @param staIds    货位Ids
	 * @param cabinetId 刀具柜Id
	 */
	public OpTip matOut(String staIds, String accountId, String cabinetId);

	/**
	 * @description 获取库存
	 * @param mainCabinetId 主柜Id
	 */
	public List<StockTip> getStockTip(String mainCabinetId);

	/**
	 * @description 根据柜子Id获取物料余量
	 * @param cabinetId 柜子Id
	 * @return
	 */
	public Map<String, Integer> getMatNumMapByCabinetId(String cabinetId);

	/**
	 * @description 获取库存详情
	 * @param wareHouseAlias 仓库别称
	 * @return
	 */
	public ApiFeignResponse<FeignStock> getStockDetail(String wareHouseAlias);

	/**
	 * @description 获取库存详情
	 * @param
	 * @return
	 */
	public ApiFeignResponse<FeignCabinet> getStaStock();

	/**
	 * @description 通过仓库别名获取按物料分组货道列表
	 * @param wareHouseAlias 仓库别名
	 * @return
	 */
	public Map<MatEquInfo, List<EquDetailSta>> getStaListGroupByMat(String wareHouseAlias);

	/**
	 * @description 通过行列号获取货道信息
	 * @param rowNo
	 * @param colNo
	 * @return
	 */
	public List<EquDetailSta> getStaByRowCol(String wareHouseAlias, Integer rowNo, Integer colNo);

	/**
	 * @description 导出最新的补料单
	 * @param request
	 * @param response
	 */
	public void exportLastFeedingList(HttpServletRequest request, HttpServletResponse response);

	/**
	 * @description 根据柜子Id或托盘Id获取货道列表
	 * @param cabinetId             柜子Id
	 * @param staMatNameBarCodeSpec 货道物料名称编号型号
	 * @return
	 */
	public List<EquDetailSta> getStaListByCabinetIdOrDetailId(String cabinetId, String staMatNameBarCodeSpec);

	/**
	 * @description 获取库位信息
	 * @param equSettingId       柜体Id或托盘Id
	 * @param matNameBarCodeSpec 物料名称、编号、规格
	 * @return
	 */
	public List<EquDetailStaVo> getEquInfos(String equSettingId, String matNameBarCodeSpec);

	/**
	 * @description 根据物料Id获取物料在用货道
	 * @param infoId 物料Id
	 * @return
	 */
	public List<EquDetailSta> findEquDetailStaListByMatInfoId(String infoId);
}