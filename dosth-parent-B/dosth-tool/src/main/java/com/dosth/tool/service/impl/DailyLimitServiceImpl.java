package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.app.dto.FeignUser;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.state.BorrowType;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.entity.BorrowPopedom;
import com.dosth.tool.entity.DailyLimit;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.repository.BorrowPopedomRepository;
import com.dosth.tool.repository.DailyLimitRepository;
import com.dosth.tool.repository.MatCategoryRepository;
import com.dosth.tool.repository.MatCategoryTreeRepository;
import com.dosth.tool.rpc.CabinetRpcService;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.DailyLimitService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.tool.service.SubCabinetDetailService;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.SubMatDetail;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.util.OpTip;

/**
 * @description 每日限额Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class DailyLimitServiceImpl implements DailyLimitService {

	@Autowired
	private AdminService adminService;
	@Autowired
	private CabinetRpcService cabinetRpcService;
	@Autowired
	private DailyLimitRepository dailyLimitRepository;
	@Autowired
	private BorrowPopedomRepository borrowPopedomRepository;
	@Autowired
	private MatCategoryRepository matCategoryRepository;
	@Autowired
	private MatCategoryTreeRepository matCategoryTreeRepository;
	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private SubCabinetDetailService subCabinetDetailService;

	@Override
	public void save(DailyLimit dailyLimit) throws DoSthException {
		this.dailyLimitRepository.save(dailyLimit);
	}

	@Override
	public DailyLimit get(Serializable id) throws DoSthException {
		DailyLimit dailyLimit = this.dailyLimitRepository.findOne(id);
		return dailyLimit;
	}

	@Override
	public DailyLimit update(DailyLimit dailyLimit) throws DoSthException {
		return this.dailyLimitRepository.saveAndFlush(dailyLimit);
	}

	@Override
	public void delete(DailyLimit dailyLimit) throws DoSthException {
		this.dailyLimitRepository.delete(dailyLimit);
	}

	@Override
	public void deleteByAccountId(String accountId) throws DoSthException {
		this.dailyLimitRepository.delete(this.dailyLimitRepository.findAllByAccountId(accountId));
	}

	@Override
	public List<DailyLimit> findAllByAccountId(String accountId) throws DoSthException {
		List<DailyLimit> limitList = this.dailyLimitRepository.findAllByAccountId(accountId);
		return limitList;
	}

	@Override
	public OpTip dataSyncByAccountId(String accountId) throws DoSthException {
		OpTip tip = new OpTip(200, "查询成功");
		// 获取已存在的限额信息
		List<String> matIds = this.dailyLimitRepository.findMatIdByAccountId(accountId);
		// 获取用户绑定的权限信息
		List<BorrowPopedom> popedomList = this.borrowPopedomRepository.getPopedomList(accountId);
		StringBuilder buildChild = new StringBuilder();
		// 根据权限信息获取用户绑定的所有物料类型
		for (BorrowPopedom popedom : popedomList) {
			String[] popedomArr = popedom.getPopedoms().split(",");
			for (String arr : popedomArr) {
				if (arr.equals("MATTYPE")) {// 类型
					buildChild.append("1" + ",");
				} else if (arr.equals("REQREF")) {// 设备
					buildChild.append("2" + ",");
				} else if (arr.equals("PROCREF")) {// 工序
					buildChild.append("3" + ",");
				} else if (arr.equals("PARTS")) {// 零件
					buildChild.append("4" + ",");
				} else if (arr.equals("CUSTOM")) {// 自定义
					buildChild.append("5" + ",");
				} else {// 子节点
					buildChild.append(arr + ",");
				}
				// 获取子节点(节点的父节点path中包含该节点Id,即等于属于该节点的子节点)
				List<MatCategoryTree> treeList = this.matCategoryTreeRepository.findPathNode(arr, UsingStatus.ENABLE);
				for (MatCategoryTree tree : treeList) {
					buildChild.append(tree.getId() + ",");
				}
			}
		}
		// 根据用户绑定的物料类型获取对应的物料
		List<String> matIdList = matCategoryRepository.getMatIds(Arrays.asList(buildChild.toString().split(",")));
		// 判断限额表中物料是否存在于绑定物料中
		for (String id : matIds) {
			if (!matIdList.contains(id)) {
				// 不存在的场合同步删除
				this.dailyLimitRepository.delDailyLimitByMatId(accountId, id);
			}
		}
		// 判断绑定物料中是否存在于限额表中
		for (String id : matIdList) {
			if (matIds.contains(id)) {
				// 存在的场合跳过
				continue;
			}
			// 不存在的场合同步增加
			this.dailyLimitRepository.save(new DailyLimit(accountId, id, 0, 0, 0));
		}
		return tip;
	}

	@Override
	public OpTip getDailyLimit(String accountId, String matId, Integer borrowNum, UserInfo userInfo) {
		Integer unreturnNum = 0; // 未归还总数量
		Integer curBorrowNum = 0; // 当前领用总数量
		String startTime = userInfo.getStartTime();
		String endTime = userInfo.getEndTime();
		String opDate;// 最后操作时间
		OpTip tip = new OpTip(200, "请稍等。。。");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		String curTime = df.format(new Date());// 当前时间
		String curDate = df2.format(new Date());// 当前日期

		// 获取限额列表
		List<DailyLimit> limitList = this.dailyLimitRepository.findAllByAccountId(accountId);

		// 获取未归还列表
		List<SubMatDetail> unreturnlist = this.cabinetRpcService.getUnReturnList(accountId);

		for (DailyLimit limit : limitList) {
			// 获取最后操作时间
			opDate = df2.format(limit.getOpDate());
			try {
				// 夜班
				if (df.parse(startTime).getTime() > df.parse(endTime).getTime()) {
					// 判断是否在领取时间内
					if (df.parse(curTime).getTime() > df.parse(endTime).getTime()
							&& df.parse(curTime).getTime() < df.parse(startTime).getTime()) {
						tip = new OpTip(201, "未在领取时间内！");
						return tip;
						// 重置当前领取数量
					} else {
						if (df2.parse(curDate).getTime() > df2.parse(opDate).getTime()
								&& df.parse(curTime).getTime() > df.parse(startTime).getTime()) {
							limit.setCurNum(0);
							limit.setOpDate(new Date());
							this.dailyLimitRepository.saveAndFlush(limit);
						}
					}
					// 白班
				} else if (df.parse(startTime).getTime() < df.parse(endTime).getTime()) {
					if (df.parse(curTime).getTime() < df.parse(startTime).getTime()
							|| df.parse(curTime).getTime() > df.parse(endTime).getTime()) {
						tip = new OpTip(201, "未在领取时间内！");
						return tip;
						// 重置当前领取数量
					} else {
						if (df2.parse(curDate).getTime() > df2.parse(opDate).getTime()
								&& (df.parse(curTime).getTime() > df.parse(startTime).getTime()
										&& df.parse(curTime).getTime() < df.parse(endTime).getTime())) {
							limit.setCurNum(0);
							limit.setOpDate(new Date());
							this.dailyLimitRepository.saveAndFlush(limit);
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		// 计算当前领取总数
		if (limitList != null && limitList.size() > 0) {
			for (DailyLimit dailylimit : limitList) {
				curBorrowNum += dailylimit.getCurNum();
			}
		}

		// 计算当前未归还总数
		if (unreturnlist != null && unreturnlist.size() > 0) {
			for (SubMatDetail unreturn : unreturnlist) {
				unreturnNum += unreturn.getBorrowNum();
			}
		}

		// 判断领取限额总数
		if (userInfo.getLimitSumNum() != null && userInfo.getLimitSumNum() != 0) {
			if (curBorrowNum + (borrowNum == 0 ? 1 : borrowNum) > userInfo.getLimitSumNum()) {
				tip = new OpTip(201, "超出领取限额总数！");
				return tip;
			}
		}

		// 判断未归还限额总数
		if (userInfo.getNotReturnLimitNum() != null && userInfo.getNotReturnLimitNum() != 0) {
			if (borrowNum + unreturnNum > userInfo.getNotReturnLimitNum()) {
				tip = new OpTip(201, "超出未归还限额总数！");
				return tip;
			}
		}

		if (limitList != null && limitList.size() > 0) {
			for (DailyLimit limit : limitList) {
				// 限额判断
				if (matId.equals(limit.getMatInfoId())) {
					// 借出数量大于未归还数量时，直接返回
					if (limit.getNotReturnNum() != null && limit.getNotReturnNum() != 0) {
						if (borrowNum > limit.getNotReturnNum()) {
							tip = new OpTip(201, limit.getMatInfo().getMatEquName() + " " + limit.getMatInfo().getSpec()
									+ " " + "超出未归还数量！");
							return tip;
						}
					}

					// 计算此物料未归还总数
					int tempNum = 0;
					for (SubMatDetail unreturn : unreturnlist) {
						if (matId.equals(unreturn.getMatId())) {
							tempNum += unreturn.getBorrowNum();
						}
					}
					// 判断未归还限额
					if (limit.getNotReturnNum() != null && limit.getNotReturnNum() != 0) {
						if (tempNum + borrowNum > limit.getNotReturnNum()) {
							tip = new OpTip(201, limit.getMatInfo().getMatEquName() + " " + limit.getMatInfo().getSpec()
									+ " " + "超出未归还数量！");
							return tip;
						}
					}
					// 判断领取限额
					if (limit.getLimitNum() != null && limit.getLimitNum() != 0) {
						if (limit.getCurNum() + (borrowNum == 0 ? 1 : borrowNum) > limit.getLimitNum()) {
							tip = new OpTip(201, limit.getMatInfo().getMatEquName() + " " + limit.getMatInfo().getSpec()
									+ " " + "已达领取上限!");
							return tip;
						}
					}
				}
			}
		}
		return tip;
	}

	@Override
	public OpTip getDailyLimitByCart(String shareSwitch, String accountId, Map<String, CartInfo> limitMap,
			String startTime, String endTime, Integer limitSumNum, Integer notReturnLimitNum) {
		Integer unreturnNum = 0; // 未归还总数量
		Integer curBorrowNum = 0; // 当前领用总数量
		Integer allBorrowNum = 0; // 购物车领取总数
		String opDate;// 最后操作时间
		String matId;// 物料id
		Integer borrowNum = 0; // 取料数量
		double tmpBorrowNum; // 临时存储取料数量
		String borrowType = null;// 借出类型（盒/支）
		OpTip tip = new OpTip(200, "请稍等。。。");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		String curTime = df.format(new Date());// 当前时间
		String curDate = df2.format(new Date());// 当前日期

		// 判断暂存柜是否有物料，并且判断是否够领取
		Integer subBoxNum = 0;// 暂存柜数量
		SubCabinetDetail subCabinetDetail = null;// 暂存柜详情
		List<SubCabinetDetail> subCabinetDetailList = null;// 暂存柜列表

		Map<String, Integer> map = this.subCabinetDetailService.getTotalQuantityGroupByMatId(shareSwitch, accountId);

		for (Map.Entry<String, CartInfo> entry : limitMap.entrySet()) {
			MatEquInfo info = this.matEquInfoService.get(entry.getKey());

			if (TrueOrFalse.TRUE.toString().equals(shareSwitch)) {
				subCabinetDetailList = this.subCabinetDetailService.getSubDetailListByMatId(entry.getKey());
			} else {
				subCabinetDetailList = this.subCabinetDetailService.getSubDetailListByAccountIdAndMatId(accountId,
						entry.getKey());
			}

			if (String.valueOf(BorrowType.METER).equals(entry.getValue().getBorrowType())) {
				subBoxNum = map.get(entry.getKey()) == null ? 0 : map.get(entry.getKey());
				if (subBoxNum >= entry.getValue().getNum()) {
					if (subCabinetDetailList != null && subCabinetDetailList.size() > 0) {
						subCabinetDetail = subCabinetDetailList.get(0);
						tip = new OpTip(201, "暂存柜有【" + info.getMatEquName() + " " + info.getSpec() + "】"
								+ subCabinetDetail.getNum() + "支可使用！");
						return tip;
					}
				}
			} else {
				subBoxNum = map.get(entry.getKey()) == null ? 0 : map.get(entry.getKey());
				if (subBoxNum >= entry.getValue().getNum()) {
					if (subCabinetDetailList != null && subCabinetDetailList.size() > 0) {
						subCabinetDetail = subCabinetDetailList.get(0);
						tip = new OpTip(201, "暂存柜有【" + info.getMatEquName() + " " + info.getSpec() + "】"
								+ subCabinetDetail.getNum() + "盒可使用！");
						return tip;
					}
				}
			}
		}

		// 获取限额列表
		List<DailyLimit> limitList = this.dailyLimitRepository.findAllByAccountId(accountId);

		// 获取未归还列表
		List<SubMatDetail> unreturnlist = this.cabinetRpcService.getUnReturnList(accountId);

		for (DailyLimit limit : limitList) {
			opDate = df2.format(limit.getOpDate());
			// 判断是否在领取时间内
			try {
				// 夜班
				if (df.parse(startTime).getTime() > df.parse(endTime).getTime()) {
					// 判断是否在领取时间内
					if (df.parse(curTime).getTime() > df.parse(endTime).getTime()
							&& df.parse(curTime).getTime() < df.parse(startTime).getTime()) {
						tip = new OpTip(201, "未在领取时间内！");
						return tip;
					} else {
						// 重置当前领取数量
						if (df2.parse(curDate).getTime() > df2.parse(opDate).getTime()
								&& df.parse(curTime).getTime() > df.parse(startTime).getTime()) {
							limit.setCurNum(0);
							limit.setOpDate(new Date());
							this.dailyLimitRepository.saveAndFlush(limit);
						}
					}
					// 白板
				} else if (df.parse(startTime).getTime() < df.parse(endTime).getTime()) {
					// 判断是否在领取时间内
					if (df.parse(curTime).getTime() < df.parse(startTime).getTime()
							|| df.parse(curTime).getTime() > df.parse(endTime).getTime()) {
						tip = new OpTip(201, "未在领取时间内！");
						return tip;
					} else {
						// 重置当前领取数量
						if (df2.parse(curDate).getTime() > df2.parse(opDate).getTime()
								&& (df.parse(curTime).getTime() > df.parse(startTime).getTime()
										&& df.parse(curTime).getTime() < df.parse(endTime).getTime())) {
							limit.setCurNum(0);
							limit.setOpDate(new Date());
							this.dailyLimitRepository.saveAndFlush(limit);
						}
					}
				}
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}

		// 计算当前领取总数
		if (limitList != null && limitList.size() > 0) {
			for (DailyLimit dailylimit : limitList) {
				curBorrowNum += dailylimit.getCurNum();
			}
		}

		// 计算当前未归还总数
		if (unreturnlist != null && unreturnlist.size() > 0) {
			for (SubMatDetail unreturn : unreturnlist) {
				unreturnNum += unreturn.getBorrowNum();
			}
		}

		// 购物车领取总数
		for (Map.Entry<String, CartInfo> entry : limitMap.entrySet()) {
			matId = entry.getKey();
			// 获取当前物料信息
			MatEquInfo matInfo = this.matEquInfoService.get(matId);

			// 根据借出类型计算取料盒数（按支领取,不满1盒按1盒领取）
			borrowType = entry.getValue().getBorrowType();
			if (borrowType.equals(BorrowType.METER.toString())) {
				tmpBorrowNum = new BigDecimal((float) entry.getValue().getNum() / matInfo.getNum())
						.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				borrowNum = (int) Math.ceil(tmpBorrowNum);
			} else if (borrowType.equals(BorrowType.PACK.toString())) {
				borrowNum = entry.getValue().getNum();
			}

			allBorrowNum += borrowNum;
		}

		// 判断领取限额总数
		if (limitSumNum != null && limitSumNum != 0) {
			if (curBorrowNum + allBorrowNum > limitSumNum) {
				tip = new OpTip(201, "超出领取限额！");
				return tip;
			}
		}

		// 判断未归还限额总数
		if (notReturnLimitNum != null && notReturnLimitNum != 0) {
			if (allBorrowNum + unreturnNum > notReturnLimitNum) {
				tip = new OpTip(201, "超出未归还限额总数！");
				return tip;
			}
		}
		if (limitList != null && limitList.size() > 0) {
			for (Map.Entry<String, CartInfo> entry : limitMap.entrySet()) {
				matId = entry.getKey();
				// 获取当前物料信息
				MatEquInfo matInfo = this.matEquInfoService.get(matId);

				// 根据借出类型计算取料盒数（按支领取,不满1盒按1盒领取）
				borrowType = entry.getValue().getBorrowType();
				if (borrowType.equals(BorrowType.METER.toString())) {
					tmpBorrowNum = new BigDecimal((float) entry.getValue().getNum() / matInfo.getNum())
							.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					borrowNum = (int) Math.ceil(tmpBorrowNum);
				} else if (borrowType.equals(BorrowType.PACK.toString())) {
					borrowNum = entry.getValue().getNum();
				}

				for (DailyLimit limit : limitList) {

					// 计算此物料未归还总数
					int tempNum = 0;
					for (SubMatDetail unreturn : unreturnlist) {
						// 判断未归还限额
						if (matId.equals(unreturn.getMatId())) {
							tempNum += unreturn.getBorrowNum();
						}
					}

					// 判断是否有限额
					if (matId.equals(limit.getMatInfoId())) {
						// 判断未归还数量限额
						if (limit.getNotReturnNum() != null && limit.getNotReturnNum() != 0) {
							// 领取数量大于限额数量直接返回
							if (borrowNum > limit.getNotReturnNum()) {
								tip = new OpTip(201, limit.getMatInfo().getMatEquName() + " "
										+ limit.getMatInfo().getSpec() + " " + "超出未归还数量！");
								return tip;
							}
							// 领取数量+历史未归还数量大于限额数量直接返回
							if (tempNum + borrowNum > limit.getNotReturnNum()) {
								tip = new OpTip(201, limit.getMatInfo().getMatEquName() + " "
										+ limit.getMatInfo().getSpec() + " " + "超出未归还数量！");
								return tip;
							}
						}
						// 判断领取上限数量限额
						if (limit.getLimitNum() != null && limit.getLimitNum() != 0) {
							if (limit.getCurNum() + borrowNum > limit.getLimitNum()) {
								tip = new OpTip(201, limit.getMatInfo().getMatEquName() + " "
										+ limit.getMatInfo().getSpec() + " " + "已达领取上限!");
								return tip;
							}
						}
					}
				}
			}
		}
		return tip;
	}

	@Override
	public OpTip saveDailyLimit(Map<String, DailyLimit> limitMap, FeignUser feignUser) {
		OpTip tip = new OpTip(200, "保存成功");
		DailyLimit dailyLimit;
		UserInfo userInfo = this.adminService.getUserByAccountId(feignUser);
		if (userInfo != null) {
			if (limitMap != null && limitMap.size() > 0) {
				for (Entry<String, DailyLimit> entry : limitMap.entrySet()) {
					dailyLimit = this.dailyLimitRepository.findOne(entry.getKey());
					dailyLimit.setLimitNum(entry.getValue().getLimitNum());
					dailyLimit.setNotReturnNum(entry.getValue().getNotReturnNum());
					this.update(dailyLimit);
				}
			} else {
				tip = new OpTip(200, "保存成功（未绑定领用权限）！");
			}
		} else {
			tip = new OpTip(200, "不存在该人员！");
		}
		return tip;
	}

	@Override
	public void delDailyLimit(String accountId) {
		this.dailyLimitRepository.delDailyLimit(accountId);
	}

}
