package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.quartz.QuartzScheduler;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.TimeTaskDetailService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.util.OpTip;

/**
 * 定时任务详情相关方法实现类
 * 
 * @author Weifeng.Li
 *
 */
@Service
@Transactional
public class TimeTaskDetailServiceImpl implements TimeTaskDetailService {

	@Autowired
	private TimeTaskDetailRepository timeTaskDetailRepository;
	@Autowired
	private UserService userSvc;
	@Autowired
	private QuartzScheduler quartzScheduler;

	@Override
	public void save(TimeTaskDetail entity) throws DoSthException {
		this.timeTaskDetailRepository.save(entity);
	}

	@Override
	public TimeTaskDetail get(Serializable id) throws DoSthException {
		return this.timeTaskDetailRepository.findOne(id);
	}

	@Override
	public TimeTaskDetail update(TimeTaskDetail entity) throws DoSthException {
		return this.timeTaskDetailRepository.saveAndFlush(entity);
	}

	@Override
	public void delete(TimeTaskDetail entity) throws DoSthException {
		this.timeTaskDetailRepository.delete(entity);
	}

	@Override
	public TimeTaskDetail getDetail() {
		TimeTaskDetail timeTaskDetail = null;
		List<TimeTaskDetail> list = this.timeTaskDetailRepository.findAll();
		if (CollectionUtils.isNotEmpty(list)) {
			timeTaskDetail = list.get(0);
		}
		return timeTaskDetail;
	}

	@Override
	public OpTip saveAndFlush(TimeTaskDetail timeTaskDetail) {
		boolean flag = false;
		OpTip tip = new OpTip(200, "操作成功");

		TimeTaskDetail temp = this.timeTaskDetailRepository.findOne(timeTaskDetail.getId());

		if (StringUtils.isNotBlank(timeTaskDetail.getAccountId())) {
			String userIds[] = timeTaskDetail.getAccountId().split(",");
			ViewUser user;
			String userName = "";
			for (int i = 0; i < userIds.length; i++) {
				user = this.userSvc.getViewUser(userIds[i]);
				if (user != null) {
					userName += user.getUserName() + ",";
				}
			}
			timeTaskDetail.setUserName(userName);

			if (temp.getExecutionTime().equals(timeTaskDetail.getExecutionTime())) {
				timeTaskDetail.setExecutionTime(temp.getExecutionTime());
			} else {
				try {
					flag = quartzScheduler.modifyJob(timeTaskDetail.getJobId(), timeTaskDetail.getJobGroup(),
							timeTaskDetail.getCronExpression());
					if (!flag) {
						tip.setCode(201);
						tip.setMessage("操作失败");
						return tip;
					}
				} catch (SchedulerException e) {
					e.printStackTrace();
				}
			}
			this.timeTaskDetailRepository.saveAndFlush(timeTaskDetail);
		}

		return tip;
	}

}
