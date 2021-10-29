package com.dosth.admin.rpc;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dosth.comm.node.ZTreeNode;
import com.dosth.dto.ExtraBoxNum;
import com.dosth.statistics.dto.MonthCost;

/**
 * 
 * @description 工具项目远程接口
 * @author guozhidong
 *
 */
@FeignClient("service-tool")
public interface ToolService {

	/**
	 * @description 删除每日限额
	 * @return
	 */
	@RequestMapping("/feign/delDailyLimit")
	public void delDailyLimit(@RequestParam("accountId") String accountId);

	/**
	 * @description 近三年成本趋势
	 * @return
	 */
	@RequestMapping("/feign/getThrYearPriceSumGroupByMonth")
	public Map<String, List<MonthCost>> getThrYearPriceSumGroupByMonth();

	/**
	 * @description 当前月物料数量分布
	 * @return
	 */
	@RequestMapping("/feign/getCurMonthGroupByMat")
	public Map<String, Integer> getCurMonthGroupByMat();

	/**
	 * @description 三年物料类型分布
	 * @return
	 */
	@RequestMapping("/feign/getThrYCntGroupByMatType")
	public Map<String, Map<String, Integer>> getThrYCntGroupByMatType();
	
	/**
	 * @description 按部门统计领取数量分组
	 * @return
	 */
	@RequestMapping("/feign/getBorrowNumGroupByDept")
	public Map<String, Integer> getBorrowNumGroupByDept();

	/**
	 * @description 初始化借出权限树
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@RequestMapping("/feign/initBorrowPopedomTree")
	public List<ZTreeNode> initBorrowPopedomTree(@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 绑定借出权限
	 * @param accountId 帐户Id
	 * @param popedoms  借出权限
	 * @return
	 */
	@RequestMapping("/feign/bindBorrowPopedoms")
	public Boolean bindBorrowPopedoms(@RequestParam("accountId") String accountId,
			@RequestParam("popedoms") String popedoms);

	/**
	 * @description 修改暂存柜数量
	 * @param user 用户信息
	 * @return
	 */
	@RequestMapping("/feign/updateExtraBoxNum")
	public void updateExtraBoxNum(@RequestParam("accountId") String accountId,
			@RequestParam("extraBoxNum") String extraBoxNum);

	/**
	 * @description 查询暂存柜数量
	 * @param accountId 账户id
	 * @return
	 */
	@RequestMapping("/feign/getExtraBoxNum")
	public ExtraBoxNum getExtraBoxNum(@RequestParam("accountId") String accountId);

	/**
	 * @description 删除暂存柜数量
	 * @param accountId 账户id
	 * @return
	 */
	@RequestMapping("/feign/delExtraBoxNum")
	public void delExtraBoxNum(@RequestParam("accountId") String accountId);

	/**
	 * @description 删除通知管理收件人
	 * @param accountId 账户id
	 * @return
	 */
	@RequestMapping("/feign/delNoticeMgr")
	public void delNoticeMgr(@RequestParam("accountId") String accountId);
}