package com.dosth.tool.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.dto.OpTip;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.repository.CabinetSetupRepository;
import com.dosth.tool.repository.CartRepository;
import com.dosth.tool.repository.DailyLimitRepository;
import com.dosth.tool.repository.DataSyncStateRepository;
import com.dosth.tool.repository.EquDetailRepository;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.FeedingDetailRepository;
import com.dosth.tool.repository.FeedingListRepository;
import com.dosth.tool.repository.HardwareLogRepository;
import com.dosth.tool.repository.InventoryRepository;
import com.dosth.tool.repository.LockParamRepository;
import com.dosth.tool.repository.LowerFrameQueryRepository;
import com.dosth.tool.repository.ManufacturerCustomRepository;
import com.dosth.tool.repository.ManufacturerRepository;
import com.dosth.tool.repository.MatCategoryRepository;
import com.dosth.tool.repository.MatCategoryTreeRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.repository.MatReturnBackRepository;
import com.dosth.tool.repository.MatReturnDetailRepository;
import com.dosth.tool.repository.MatUseBillRepository;
import com.dosth.tool.repository.MatUseRecordRepository;
import com.dosth.tool.repository.NoticeMgrRepository;
import com.dosth.tool.repository.PhoneOrderMatDetailRepository;
import com.dosth.tool.repository.PhoneOrderMatRepository;
import com.dosth.tool.repository.PhoneOrderRepository;
import com.dosth.tool.repository.PhoneOrderStaRepository;
import com.dosth.tool.repository.StatementDetailRepository;
import com.dosth.tool.repository.StatementRepository;
import com.dosth.tool.repository.SubBoxAccountRefRepository;
import com.dosth.tool.repository.SubBoxRepository;
import com.dosth.tool.repository.SubCabinetBillRepository;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.repository.WarehouseFeedRepository;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.DataDeleteService;

/**
 * @description 数据删除接口实现
 * @author Zhidong.Guo
 *
 */
@Service
@Transactional
public class DataDeleteServiceImpl implements DataDeleteService {

	private static final Logger logger = LoggerFactory.getLogger(DataDeleteServiceImpl.class);

	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private DailyLimitRepository dailyLimitRepository;
	@Autowired
	private HardwareLogRepository hardwareLogRepository;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private FeedingDetailRepository feedingDetailRepository;
	@Autowired
	private FeedingListRepository feedingListRepository;
	@Autowired
	private WarehouseFeedRepository warehouseFeedRepository;
	@Autowired
	private InventoryRepository inventoryRepository;
	@Autowired
	private LowerFrameQueryRepository lowerFrameQueryRepository;
	@Autowired
	private ManufacturerCustomRepository manufacturerCustomRepository;
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	@Autowired
	private MatCategoryRepository matCategoryRepository;
	@Autowired
	private MatCategoryTreeRepository matCategoryTreeRepository;
	@Autowired
	private MatReturnDetailRepository matReturnDetailRepository;
	@Autowired
	private MatReturnBackRepository matReturnBackRepository;
	@Autowired
	private MatUseBillRepository matUseBillRepository;
	@Autowired
	private MatUseRecordRepository matUseRecordRepository;
	@Autowired
	private PhoneOrderStaRepository phoneOrderStaRepository;
	@Autowired
	private PhoneOrderMatDetailRepository phoneOrderMatDetailRepository;
	@Autowired
	private PhoneOrderMatRepository phoneOrderMatRepository;
	@Autowired
	private PhoneOrderRepository phoneOrderRepository;
	@Autowired
	private SubCabinetBillRepository subCabinetBillRepository;
	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;
	@Autowired
	private SubBoxAccountRefRepository subBoxAccountRefRepository;
	@Autowired
	private StatementDetailRepository statementDetailRepository;
	@Autowired
	private StatementRepository statementRepository;
	@Autowired
	private MatEquInfoRepository matEquInfoRepository;

	@Autowired
	private CabinetSetupRepository cabinetSetupRepository;
	@Autowired
	private LockParamRepository lockParamRepository;
	@Autowired
	private NoticeMgrRepository noticeMgrRepository;

	@Autowired
	private EquDetailRepository equDetailRepository;
	@Autowired
	private SubBoxRepository subBoxRepository;
	@Autowired
	private EquSettingRepository equSettingRepository;

	@Autowired
	private DataSyncStateRepository dataSyncStateRepository;

	@Autowired
	private AdminService adminService;
	
	@Autowired
	private TimeTaskDetailRepository timeTaskDetailRepository;

	@Override
	public void clearMat() {
		try {
			// 购物车
			logger.info("删除购物车");
			this.cartRepository.deleteAll();
			// 每日限额
			logger.info("删除每日限额");
			this.dailyLimitRepository.deleteAll();
			// 硬件日志
			logger.info("删除硬件日志");
			this.hardwareLogRepository.deleteAll();
			// 货道物料关联关系
			logger.info("删除货道物料关联关系");
			this.equDetailStaRepository.deleteByMat();
			// 补料明细
			logger.info("删除补料明细");
			this.feedingDetailRepository.deleteAll();
			// 补料清单
			logger.info("删除补料清单");
			this.feedingListRepository.deleteAll();
			// 入库单补料
			logger.info("删除入库单补料");
			this.warehouseFeedRepository.deleteAll();
			// 盘点
			logger.info("删除盘点");
			this.inventoryRepository.deleteAll();
			// 下架
			logger.info("删除下架");
			this.lowerFrameQueryRepository.deleteAll();
			// 领取关联
			logger.info("删除领取关联");
			this.matCategoryRepository.deleteAll();
			// 归还明细
			logger.info("删除归还明细");
			this.matReturnDetailRepository.deleteAll();
			// 归还记录
			logger.info("删除归还记录");
			this.matReturnBackRepository.deleteAll();
			// 领用流水
			logger.info("删除领用流水");
			this.matUseBillRepository.deleteAll();
			// 领用记录
			logger.info("删除领用记录");
			this.matUseRecordRepository.deleteAll();
			// 订单状态
			logger.info("删除订单状态");
			this.phoneOrderStaRepository.deleteAll();
			// 订单物料明细
			logger.info("删除订单物料明细");
			this.phoneOrderMatDetailRepository.deleteAll();
			// 订单物料详情
			logger.info("删除订单物料详情");
			this.phoneOrderMatRepository.deleteAll();
			// 订单
			logger.info("删除订单");
			this.phoneOrderRepository.deleteAll();
			// 暂存柜流水
			logger.info("删除暂存柜流水");
			this.subCabinetBillRepository.deleteAll();
			// 暂存柜明细
			logger.info("删除暂存柜明细");
			this.subCabinetDetailRepository.deleteAll();
			// 暂存柜关联账户
			logger.info("删除暂存柜关联账户");
			this.subBoxAccountRefRepository.deleteAll();
			// 核对明细
			logger.info("删除核对明细");
			this.statementDetailRepository.deleteAll();
			// 核对
			logger.info("删除核对");
			this.statementRepository.deleteAll();
			// 物料信息
			logger.info("删除物料信息");
			this.matEquInfoRepository.deleteAll();
			// 供应商客服
			logger.info("删除供应商客服");
			this.manufacturerCustomRepository.deleteByMat();
			// 供应商
			logger.info("删除供应商");
			this.manufacturerRepository.deleteByMat();
		} catch (Exception e) {
			logger.error("清理物料及相关信息失败:" + e.getMessage());
		}
	}

	@Override
	public void clearEqu() {
		try {
			// 设备配置项
			logger.info("设备配置项");
			this.cabinetSetupRepository.deleteAll();
			// 电磁锁配置参数
			logger.info("电磁锁配置参数");
			this.lockParamRepository.deleteAll();
			// 柜体通知设置
			logger.info("柜体通知设置");
			this.noticeMgrRepository.deleteAll();
			// 购物车
			logger.info("删除购物车");
			this.cartRepository.deleteAll();
			// 硬件日志
			logger.info("删除硬件日志");
			this.hardwareLogRepository.deleteAll();
			// 补料明细
			logger.info("删除补料明细");
			this.feedingDetailRepository.deleteAll();
			// 补料清单
			logger.info("删除补料清单");
			this.feedingListRepository.deleteAll();
			// 入库单补料
			logger.info("删除入库单补料");
			this.warehouseFeedRepository.deleteAll();
			// 盘点
			logger.info("删除盘点");
			this.inventoryRepository.deleteAll();
			// 下架
			logger.info("删除下架");
			this.lowerFrameQueryRepository.deleteAll();
			// 归还明细
			logger.info("删除归还明细");
			this.matReturnDetailRepository.deleteAll();
			// 归还记录
			logger.info("删除归还记录");
			this.matReturnBackRepository.deleteAll();
			// 领用流水
			logger.info("删除领用流水");
			this.matUseBillRepository.deleteAll();
			// 领用记录
			logger.info("删除领用记录");
			this.matUseRecordRepository.deleteAll();
			// 订单状态
			logger.info("删除订单状态");
			this.phoneOrderStaRepository.deleteAll();
			// 订单物料明细
			logger.info("删除订单物料明细");
			this.phoneOrderMatDetailRepository.deleteAll();
			// 订单物料详情
			logger.info("删除订单物料详情");
			this.phoneOrderMatRepository.deleteAll();
			// 订单
			logger.info("删除订单");
			this.phoneOrderRepository.deleteAll();
			// 暂存柜流水
			logger.info("删除暂存柜流水");
			this.subCabinetBillRepository.deleteAll();
			// 暂存柜明细
			logger.info("删除暂存柜明细");
			this.subCabinetDetailRepository.deleteAll();
			// 暂存柜关联账户
			logger.info("删除暂存柜关联账户");
			this.subBoxAccountRefRepository.deleteAll();
			// 核对明细
			logger.info("删除核对明细");
			this.statementDetailRepository.deleteAll();
			// 核对
			logger.info("删除核对");
			this.statementRepository.deleteAll();
			// 货道
			logger.info("删除货道");
			this.equDetailStaRepository.deleteAll();
			// 托盘
			logger.info("删除托盘");
			this.equDetailRepository.deleteAll();
			// 暂存柜
			logger.info("删除暂存柜");
			this.subBoxRepository.deleteAll();
			logger.info("数据同步状态");
			this.dataSyncStateRepository.deleteAll();
			// 柜体
			logger.info("删除柜体");
			this.deleteCabinet();
		} catch (Exception e) {
			logger.error("清理设备信息异常:" + e.getMessage());
		}
	}
	
	/**
	 * @description 删除柜体
	 */
	private void deleteCabinet() {
		List<EquSetting> cabinetPList = this.equSettingRepository.getMainCabinetList();
		for (EquSetting cabinetP : cabinetPList) {
			deleteChildCabinet(cabinetP);
		}
	}
	
	/**
	 * @description 删除附属柜体
	 * @param cabinetP 上级柜体
	 */
	private void deleteChildCabinet(EquSetting cabinetP) {
		List<EquSetting> cabinetCList = this.equSettingRepository.getEquSettingChildList(cabinetP.getId());
		if (cabinetCList != null && cabinetCList.size() > 0) {
			for (EquSetting cabinetC : cabinetCList) {
				deleteChildCabinet(cabinetC);
			}
		}
		this.equSettingRepository.delete(cabinetP);
	}

	@Override
	public void clearUser() throws Exception {
		try {
			// 购物车
			logger.info("删除购物车");
			this.cartRepository.deleteAll();
			// 每日限额
			logger.info("删除每日限额");
			this.dailyLimitRepository.deleteAll();
			// 硬件日志
			logger.info("删除硬件日志");
			this.hardwareLogRepository.deleteAll();
			// 补料明细
			logger.info("删除补料明细");
			this.feedingDetailRepository.deleteAll();
			// 补料清单
			logger.info("删除补料清单");
			this.feedingListRepository.deleteAll();
			// 入库单补料
			logger.info("删除入库单补料");
			this.warehouseFeedRepository.deleteAll();
			// 盘点
			logger.info("删除盘点");
			this.inventoryRepository.deleteAll();
			// 下架
			logger.info("删除下架");
			this.lowerFrameQueryRepository.deleteAll();
			// 归还明细
			logger.info("删除归还明细");
			this.matReturnDetailRepository.deleteAll();
			// 归还记录
			logger.info("删除归还记录");
			this.matReturnBackRepository.deleteAll();
			// 领用流水
			logger.info("删除领用流水");
			this.matUseBillRepository.deleteAll();
			// 领用记录
			logger.info("删除领用记录");
			this.matUseRecordRepository.deleteAll();
			// 订单状态
			logger.info("删除订单状态");
			this.phoneOrderStaRepository.deleteAll();
			// 订单物料明细
			logger.info("删除订单物料明细");
			this.phoneOrderMatDetailRepository.deleteAll();
			// 订单物料详情
			logger.info("删除订单物料详情");
			this.phoneOrderMatRepository.deleteAll();
			// 订单
			logger.info("删除订单");
			this.phoneOrderRepository.deleteAll();
			// 暂存柜流水
			logger.info("删除暂存柜流水");
			this.subCabinetBillRepository.deleteAll();
			// 暂存柜明细
			logger.info("删除暂存柜明细");
			this.subCabinetDetailRepository.deleteAll();
			// 暂存柜关联账户
			logger.info("删除暂存柜关联账户");
			this.subBoxAccountRefRepository.deleteAll();
			// 核对明细
			logger.info("删除核对明细");
			this.statementDetailRepository.deleteAll();
			// 核对
			logger.info("删除核对");
			this.statementRepository.deleteAll();
			// 设置柜体管理人员为管理员
			logger.info("设置柜体管理人员为管理员");
			this.equSettingRepository.deleteByUser();
			// 柜体信息
			logger.info("修改柜体管理员");
			OpTip tip = this.adminService.clearUser();
			if (tip.getCode() != 200) {
				throw new Exception("清理用户信息失败");
			}
			// 定时任务初始化
			logger.info("定时任务初始化");
			this.timeTaskDetailRepository.initTimeTaskDetail();
		} catch (Exception e) {
			logger.error("清除用户信息异常:" + e.getMessage());
		}
	}

	@Override
	public void resetInit() {
		try {
			clearMat();
			clearUser();
			// 领取类型
			logger.info("初始化领取类型");
			this.matCategoryTreeRepository.resetInit();
		} catch (Exception e) {
			logger.error("一键恢复异常:" + e.getMessage());
		}
	}
}