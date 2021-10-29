package com.dosth.tool.controller;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.tool.common.quartz.QuartzScheduler;

@RestController
@RequestMapping("/quartz")
public class QuartzController {

	@Autowired
	private QuartzScheduler quartzScheduler;

	@RequestMapping("/start")
	public void startQuartzJob() {
		try {
			quartzScheduler.startJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/info")
	public String getQuartzJob(String name, String group) {
		String info = null;
		try {
			info = quartzScheduler.getJobInfo(name, group);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return info;
	}

	@RequestMapping("/modify")
	public boolean modifyQuartzJob(String name, String group, String time) {
		boolean flag = true;
		try {
			flag = quartzScheduler.modifyJob(name, group, time);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return flag;
	}

	@RequestMapping(value = "/pause")
	public void pauseQuartzJob(String name, String group) {
		try {
			quartzScheduler.pauseJob(name, group);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/pauseAll")
	public void pauseAllQuartzJob() {
		try {
			quartzScheduler.pauseAllJob();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/delete")
	public void deleteJob(String name, String group) {
		try {
			quartzScheduler.deleteJob(name, group);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
