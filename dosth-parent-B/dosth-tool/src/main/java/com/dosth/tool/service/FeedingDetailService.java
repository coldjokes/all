package com.dosth.tool.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.common.servcie.BaseService;
import com.dosth.dto.Card;
import com.dosth.tool.entity.FeedingDetail;

/**
 * 
 * @description 补料清单明细Service
 * @author guozhidong
 *
 */
public interface FeedingDetailService extends BaseService<FeedingDetail> {
	/**
	 * @description 根据补料清单Id获取清单明细列表
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	public List<Card> getFeedingDetailListByListId(String feedingListId);

	/**
	 * @description 根据补料清单获取补料明细
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	public List<FeedingDetail> getFeedingDetailListByFeedingListId(String feedListId);

	/**
	 * @description 补料明细分页
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @param matInfo   物料信息
	 * @return
	 */
	public Page<FeedingDetail> getPage(int pageNo, int pageSize, String beginTime, String endTime, String matInfo,
			String feedingName, String cabinetId);

	/**
	 * @description 导出补料详情
	 * @param request
	 * @param response
	 * @param beginTime 起始时间
	 * @param endTime   截止时间
	 * @param matInfo   物料信息
	 */
	public void exportFeedingDetail(HttpServletRequest request, HttpServletResponse response, String beginTime,
			String endTime, String matInfo, String feedingName);

	/**
	 * 补料记录邮件发送
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param matInfo
	 * @param feedingName
	 */
	public void sendFeedRecord(String beginTime, String endTime, String matInfo, String feedingName);
}