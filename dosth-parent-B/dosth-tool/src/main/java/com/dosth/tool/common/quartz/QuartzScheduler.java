package com.dosth.tool.common.quartz;

import java.time.LocalDateTime;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.service.FeedRecordSummaryService;
import com.dosth.tool.service.FeedingDetailService;
import com.dosth.tool.service.InventoryService;
import com.dosth.tool.service.LowerFrameQueryService;
import com.dosth.tool.service.MainCabinetService;
import com.dosth.tool.service.MatReturnBackService;
import com.dosth.tool.service.MatUseRecordService;
import com.dosth.tool.service.TempCabinetService;
import com.dosth.tool.service.TimeTaskDetailService;
import com.dosth.tool.service.TimeTaskService;
import com.dosth.tool.service.UseRecordSummaryService;

/**
 * Quartz配置
 * 
 * @author Weifeng.Li
 *
 */
@Configuration
public class QuartzScheduler {

	public static final Logger logger = LoggerFactory.getLogger(QuartzScheduler.class);

	/**
	 * 任务调度
	 */
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private TimeTaskService timeTaskSvc;
	@Autowired
	private TimeTaskDetailService timeTaskDetailSvc;
	@Autowired
	private MatUseRecordService matUseRecordSvc;
	@Autowired
	private MatReturnBackService matReturnBackSvc;
	@Autowired
	private MainCabinetService mainCabinetSvc;
	@Autowired
	private TempCabinetService tempCabinetSvc;
	@Autowired
	private FeedingDetailService feedingDetailSvc;
	@Autowired
	private LowerFrameQueryService lowerFrameQuerySvc;
	@Autowired
	private UseRecordSummaryService useRecordSummarySvc;
	@Autowired
	private FeedRecordSummaryService feedRecordSummarySvc;
	@Autowired
	private InventoryService inventorySvc;
	@Autowired
	private EquSettingRepository equSettingRepository;

	private void startJob1(Scheduler scheduler) throws SchedulerException {

		TimeTaskDetail timeTaskDetail = timeTaskDetailSvc.getDetail();
		String name = timeTaskDetail.getJobId();
		String group = timeTaskDetail.getJobGroup();
		String cron = timeTaskDetail.getCronExpression();

		// 通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
		// JobDetail 是具体Job实例
		JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob.class).withIdentity(name, group).build();
		jobDetail.getJobDataMap().put("timeTaskSvc", timeTaskSvc);
		jobDetail.getJobDataMap().put("matUseRecordSvc", matUseRecordSvc);
		jobDetail.getJobDataMap().put("matReturnBackSvc", matReturnBackSvc);
		jobDetail.getJobDataMap().put("mainCabinetSvc", mainCabinetSvc);
		jobDetail.getJobDataMap().put("tempCabinetSvc", tempCabinetSvc);
		jobDetail.getJobDataMap().put("feedingDetailSvc", feedingDetailSvc);
		jobDetail.getJobDataMap().put("lowerFrameQuerySvc", lowerFrameQuerySvc);
		jobDetail.getJobDataMap().put("useRecordSummarySvc", useRecordSummarySvc);
		jobDetail.getJobDataMap().put("feedRecordSummarySvc", feedRecordSummarySvc);
		jobDetail.getJobDataMap().put("inventorySvc", inventorySvc);
		jobDetail.getJobDataMap().put("equSettingRepository", equSettingRepository);

		// 基于表达式构建触发器
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
		// CronTrigger表达式触发器 继承于Trigger
		// TriggerBuilder 用于构建触发器实例
		CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group)
				.withSchedule(cronScheduleBuilder).build();
		// 防止创建时存在数据问题 先移除，然后在执行创建操作
		if (scheduler.checkExists(JobKey.jobKey(name, group))) {
			scheduler.deleteJob(JobKey.jobKey(name, group));
		}

		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	/**
	 * 开始执行所有任务
	 * 
	 * @throws SchedulerException
	 */
	public void startJob() throws SchedulerException {
		startJob1(scheduler);
		scheduler.start();
	}

	/**
	 * 获取Job信息
	 * 
	 * @param name
	 * @param group
	 * @return
	 * @throws SchedulerException
	 */
	public String getJobInfo(String name, String group) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(name, group);
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
				scheduler.getTriggerState(triggerKey).name());
	}

	/**
	 * 修改某个任务的执行时间
	 * 
	 * @param name
	 * @param group
	 * @param time
	 * @return
	 * @throws SchedulerException
	 */
	public boolean modifyJob(String name, String group, String time) throws SchedulerException {

		logger.info("修改定时任务操作时间： " + LocalDateTime.now().toLocalTime());

		Date date = null;
		TriggerKey triggerKey = new TriggerKey(name, group);
		CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		String oldTime = cronTrigger.getCronExpression();
		if (!oldTime.equalsIgnoreCase(time)) {
			CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
					.withSchedule(cronScheduleBuilder).build();
			date = scheduler.rescheduleJob(triggerKey, trigger);
		}
		return date != null;
	}

	/**
	 * 暂停所有任务
	 * 
	 * @throws SchedulerException
	 */
	public void pauseAllJob() throws SchedulerException {
		scheduler.pauseAll();
	}

	/**
	 * 暂停某个任务
	 * 
	 * @param name
	 * @param group
	 * @throws SchedulerException
	 */
	public void pauseJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null)
			return;
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复所有任务
	 * 
	 * @throws SchedulerException
	 */
	public void resumeAllJob() throws SchedulerException {
		scheduler.resumeAll();
	}

	/**
	 * 恢复某个任务
	 * 
	 * @param name
	 * @param group
	 * @throws SchedulerException
	 */
	public void resumeJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null)
			return;
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 删除某个任务
	 * 
	 * @param name
	 * @param group
	 * @throws SchedulerException
	 */
	public void deleteJob(String name, String group) throws SchedulerException {
		JobKey jobKey = new JobKey(name, group);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		if (jobDetail == null)
			return;
		scheduler.deleteJob(jobKey);
	}
}
