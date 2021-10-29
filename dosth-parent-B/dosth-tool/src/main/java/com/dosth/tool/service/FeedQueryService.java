package com.dosth.tool.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.FeedingList;

public interface FeedQueryService {
	
	/**
	 * 获取分页数据
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param beginTime
	 *            起始日期
	 * @param endTime
	 *            截止日期
	 * @return
	 * @throws BusinessException
	 */
	public Page<FeedingList> getPage(int pageNo, int pageSize, String beginTime, String endTime)
			throws DoSthException;
	
	/**
	 * @description 导出Excel
	 * 
	 * @param feedingListId 补料清单Id
	 * @throws DoSthException
	 */
	public String exportExcel(HttpServletRequest request, HttpServletResponse response, String feedingListId) throws IOException;
	
}
