package com.dosth.tool.common.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Quartz注入
 * 
 * @author Weifeng.Li
 *
 */
@Configuration
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private QuartzScheduler quartzScheduler;

	/**
	 * 初始启动quartz
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			quartzScheduler.startJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始注入scheduler
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	@Bean
	public Scheduler scheduler() throws SchedulerException {
		SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
		return schedulerFactoryBean.getScheduler();
	}
}
