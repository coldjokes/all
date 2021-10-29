package com.dosth.tool.common.quartz;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.enums.CabinetType;
import com.dosth.tool.common.state.OnOrOff;
import com.dosth.tool.common.state.TaskType;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.TimeTask;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.service.FeedRecordSummaryService;
import com.dosth.tool.service.FeedingDetailService;
import com.dosth.tool.service.InventoryService;
import com.dosth.tool.service.LowerFrameQueryService;
import com.dosth.tool.service.MainCabinetService;
import com.dosth.tool.service.MatReturnBackService;
import com.dosth.tool.service.MatUseRecordService;
import com.dosth.tool.service.TempCabinetService;
import com.dosth.tool.service.TimeTaskService;
import com.dosth.tool.service.UseRecordSummaryService;

/**
 * 定时任务
 * 
 * @author Weifeng.Li
 *
 */
public class SchedulerQuartzJob implements Job {

	public static final Logger logger = LoggerFactory.getLogger(SchedulerQuartzJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		logger.info("定时任务执行： " + LocalDateTime.now().toLocalTime());

		TimeTaskService timeTaskSvc = (TimeTaskService) context.getJobDetail().getJobDataMap().get("timeTaskSvc");
		List<TimeTask> timeTaskList = timeTaskSvc.getListByStatus(OnOrOff.ON);
		try {
			if (CollectionUtils.isNotEmpty(timeTaskList)) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
				String beginTime = dateFormat.format(calendar.getTime()) + " 00:00:00";
				String endTime = dateFormat.format(calendar.getTime()) + " 23:59:59";

				for (TimeTask timeTask : timeTaskList) {

					if (timeTask.getName().equals(TaskType.USE_RECORD)) {
						MatUseRecordService matUseRecordSvc = (MatUseRecordService) context.getJobDetail()
								.getJobDataMap().get("matUseRecordSvc");
						matUseRecordSvc.sendUseRecord(null, beginTime, endTime, null, null, null);
					}

					if (timeTask.getName().equals(TaskType.RETURN_RECORD)) {
						MatReturnBackService matReturnBackSvc = (MatReturnBackService) context.getJobDetail()
								.getJobDataMap().get("matReturnBackSvc");
						matReturnBackSvc.sendReturnRecord(beginTime, endTime, null, null, null, null);
					}

					if (timeTask.getName().equals(TaskType.STOCK_RECORD)) {
						EquSettingRepository equSettingRepository = (EquSettingRepository) context.getJobDetail()
								.getJobDataMap().get("equSettingRepository");
						List<EquSetting> equSettingList = equSettingRepository.findAll();
						if (CollectionUtils.isNotEmpty(equSettingList)) {
							for (EquSetting equSetting : equSettingList) {
								if (equSetting.getCabinetType().equals(CabinetType.SUB_CABINET)
										|| equSetting.getCabinetType().equals(CabinetType.VIRTUAL_WAREHOUSE)
										|| equSetting.getCabinetType().equals(CabinetType.STORE_CABINET)
										|| equSetting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_A)
										|| equSetting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_B)
										|| equSetting.getEquSettingParentId() == null) {
									MainCabinetService mainCabinetSvc = (MainCabinetService) context.getJobDetail()
											.getJobDataMap().get("mainCabinetSvc");
									mainCabinetSvc.sendStockRecord(equSetting.getId());
								} else {
									TempCabinetService tempCabinetSvc = (TempCabinetService) context.getJobDetail()
											.getJobDataMap().get("tempCabinetSvc");
									tempCabinetSvc.sendStockRecord(equSetting.getId());
								}
							}
						}
					}

					if (timeTask.getName().equals(TaskType.FEED_RECORD)) {
						FeedingDetailService feedingDetailSvc = (FeedingDetailService) context.getJobDetail()
								.getJobDataMap().get("feedingDetailSvc");
						feedingDetailSvc.sendFeedRecord(beginTime, endTime, null, null);
					}

					if (timeTask.getName().equals(TaskType.LOWER_RECORD)) {
						LowerFrameQueryService lowerFrameQuerySvc = (LowerFrameQueryService) context.getJobDetail()
								.getJobDataMap().get("lowerFrameQuerySvc");
						lowerFrameQuerySvc.sendLowerRecord(beginTime, endTime, null);
					}

					if (timeTask.getName().equals(TaskType.USE_SUMMARY)) {
						UseRecordSummaryService useRecordSummarySvc = (UseRecordSummaryService) context.getJobDetail()
								.getJobDataMap().get("useRecordSummarySvc");
						useRecordSummarySvc.sendUseSummary(beginTime, endTime, null);
					}

					if (timeTask.getName().equals(TaskType.FEED_SUMMARY)) {
						FeedRecordSummaryService feedRecordSummarySvc = (FeedRecordSummaryService) context
								.getJobDetail().getJobDataMap().get("feedRecordSummarySvc");
						feedRecordSummarySvc.sendFeedSummary(beginTime, endTime, null);
					}

					if (timeTask.getName().equals(TaskType.INVENTORY_RECORD)) {
						InventoryService inventorySvc = (InventoryService) context.getJobDetail().getJobDataMap()
								.get("inventorySvc");
						inventorySvc.sendInventoryRecord(beginTime, endTime);
					}
				}
			}
		} catch (Exception e) {
			logger.error("任务执行异常  - ：", e.getMessage());
		}
	}

}
