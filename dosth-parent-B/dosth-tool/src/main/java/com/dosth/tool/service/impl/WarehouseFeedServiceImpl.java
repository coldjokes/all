package com.dosth.tool.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnbaosi.dto.tool.ExternalWarehouseFeed;
import com.cnbaosi.dto.tool.FeignWarehouseFeed;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.WarehouseFeed;
import com.dosth.tool.external.util.ExternalUtil;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.FeedingDetailRepository;
import com.dosth.tool.repository.FeedingListRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.repository.WarehouseFeedRepository;
import com.dosth.tool.service.WarehouseFeedService;
import com.dosth.util.DateUtil;
import com.dosth.util.OpTip;

/**
 * 
 * @description 入库单补料
 * @author chenlei
 *
 */
@Service
@Transactional
public class WarehouseFeedServiceImpl implements WarehouseFeedService {
	
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private MatEquInfoRepository matEquInfoRepository;
	@Autowired
	private FeedingDetailRepository feedingDetailRepository;
	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private FeedingListRepository feedingListRepository;
	@Autowired
	private WarehouseFeedRepository warehouseFeedRepository;
	@Autowired
	private ToolProperties toolProperties;

	@Override
	public Page<ExternalWarehouseFeed> getFeedList(int pageNo, int pageSize, String cabinetId) throws DoSthException {
		LinkedMultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		EquSetting equSetting = this.equSettingRepository.findOne(cabinetId);
		paramMap.add("stockNos", equSetting.getWareHouseAlias());
		String param = JSON.toJSONString(paramMap);
		JSONObject result = ExternalUtil.ExternalPost(param, null, this.toolProperties.getWarehouseFeedUrl());
		if(result == null || result.isEmpty()) {
			return null;
		}
		List<ExternalWarehouseFeed> infos = JSON.parseArray((result.getString("result")),ExternalWarehouseFeed.class);
		Pageable pageable = new PageRequest(pageNo, pageSize);
		System.err.println(infos);
		return ListUtil.listConvertToPage(infos, pageable);
	}
	
	@Override
	public OpTip warehouseFeedList(String arrs, String equSettingId, String accountId, FeedType feedType)throws IOException {
		OpTip tip = new OpTip(200, "生成清单成功！");
		String[] arrsList = arrs.split(";");
		String[] feedInfo;
		MatEquInfo matEquInfo = null;
		List<FeedingDetail> feedingDetailList = null;
		
		// 待补物料验证
		for (String arrList : arrsList) {
			feedInfo = arrList.split(",");
			// 判断该物料是否存在
			List<MatEquInfo> matEquInfoList = this.matEquInfoRepository.selectByBarCode(feedInfo[0]);
			if(matEquInfoList == null || matEquInfoList.size() == 0) {
				tip.setCode(204);
				tip.setMessage("刀具柜不存在此物料："+ feedInfo[0]);
				return tip;
			}else {
				matEquInfo = matEquInfoList.get(0);
            }
			// 判断该物料是否存在等待上架的补料单
			feedingDetailList = this.feedingDetailRepository.getNoFinishFeedingDetailListByBarCode(feedInfo[0]);
			if (feedingDetailList != null && feedingDetailList.size() > 0) {
				tip.setCode(204);
				tip.setMessage("物料【" + matEquInfo.getBarCode() + "】存在未确认的补料单");
				return tip;
			}
			List<EquDetailSta> stas = this.equDetailStaRepository.getStaByBarCode(feedInfo[0], equSettingId);
			// 判断该物料能否拆包上架
            int extral = Integer.parseInt(feedInfo[1]) % matEquInfo.getNum();
            if(extral > 0) {
                tip.setCode(204);
                tip.setMessage("物料【" + feedInfo[0] + "】不能拆成整包，请确认包装数量");
                return tip;
            }
            // 判断该物料是否有足够空货位
			int tempNum = 0;
			for(EquDetailSta sta : stas) {
				tempNum += sta.getMaxReserve() - sta.getCurNum(); 
			}
			if(tempNum < Integer.parseInt(feedInfo[1])) {
				tip.setCode(204);
				tip.setMessage("物料【" + feedInfo[0] + "】空位不足，请上架新货道");
				return tip;
			}
		}
		
		// 生成补料清单
		FeedingList feedingList = new FeedingList(equSettingId, DateUtil.getAllTime(), accountId, YesOrNo.NO, feedType);
		feedingList = this.feedingListRepository.save(feedingList);
		for (String arrList : arrsList) {
			feedInfo = arrList.split(",");
			String barCode = feedInfo[0];// 物料编号
			int feedNum = Integer.parseInt(feedInfo[1]);// 待补数量
			String orderNo = feedInfo[2];// 入库单编号
			String stockNo = feedInfo[3];// 库房号
			
			matEquInfo = this.matEquInfoRepository.selectByBarCode(barCode).get(0);
			int feedPackNum = feedNum / matEquInfo.getNum();// 拆包数量
			List<EquDetailSta> stas = this.equDetailStaRepository.getStaByBarCode(barCode, equSettingId);
			// 生成入库单补料信息
			this.warehouseFeedRepository.save(new WarehouseFeed(orderNo, stockNo, feedingList.getId(), stas.get(0).getMatInfoId(), feedNum, YesOrNo.NO));
			for(EquDetailSta sta : stas) {
				int extraNum = sta.getMaxReserve() - sta.getCurNum();
				if(feedPackNum > 0) {
					if(extraNum < feedPackNum) {// 该物料已经上架，判断相同货道是否有足够空位
						this.feedingDetailRepository.save(new FeedingDetail(feedingList.getId(), sta.getId(), sta.getMatInfo().getId(), extraNum));
						feedPackNum = feedPackNum - extraNum;
					}else {
						this.feedingDetailRepository.save(new FeedingDetail(feedingList.getId(), sta.getId(), sta.getMatInfo().getId(), feedPackNum));
						break;
					}
				}
			}
		}
		return tip;
	}

	@Override
	public List<FeignWarehouseFeed> getWarehouseFeedList(String feedListId) throws IOException {
		List<FeignWarehouseFeed> feedList = new ArrayList<>();
		List<WarehouseFeed> infoList = this.warehouseFeedRepository.getWarehouseList(feedListId, YesOrNo.NO);
		if(infoList != null) {
			for(WarehouseFeed info : infoList) {
				FeignWarehouseFeed feed = new FeignWarehouseFeed();
				// 设置请求表单
				feed.setOrderNo(info.getFeedNo());//入库单号
				feed.setCompanyNo("");//公司编号
				feed.setMatNo(info.getMatInfo().getBarCode());//物料号
				feed.setFeedNum(info.getFeedNum());//补料数量
				feed.setStockNo(info.getStockNo());//刀具柜号
				feed.setFeedingListId(info.getFeedingListId());//单据号(刀具库的补料单号)
				feedList.add(feed);
			}
		}
		return feedList;
	}

	@Override
	public void warehouseSave(String feedNo, String message, Boolean result) throws IOException {
		List<WarehouseFeed> feedInfo = this.warehouseFeedRepository.getFeedInfo(feedNo, YesOrNo.NO);
		for(WarehouseFeed feed : feedInfo) {
			feed.setFeedTime(new Date());
			feed.setMessage(message);
			feed.setStatus(result ? YesOrNo.YES : YesOrNo.NO);
			this.warehouseFeedRepository.saveAndFlush(feed);
		}
	}
}