package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.tool.FeignFeedingDetail;
import com.cnbaosi.dto.tool.FeignFeedingList;
import com.cnbaosi.dto.tool.FeignMat;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.util.DateUtil;
import com.dosth.netty.dto.MsgType;
import com.dosth.netty.util.GlobalUserUtil;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.ExternalSetting;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.PostStatus;
import com.dosth.tool.external.entity.ExternalFeeding;
import com.dosth.tool.external.util.ExternalUtil;
import com.dosth.tool.repository.FeedingListRepository;
import com.dosth.tool.repository.PostStatusRepository;
import com.dosth.tool.repository.UserRepository;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.FeedingDetailService;
import com.dosth.tool.service.FeedingListService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.toolcabinet.dto.ExtractionMethod;
import com.dosth.toolcabinet.dto.UserInfo;

/**
 * 
 * @description 补料清单Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class FeedingListServiceImpl implements FeedingListService {

	@Autowired
	private FeedingListRepository feedingListRepository;
	@Autowired
	private PostStatusRepository postStatusRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FeedingDetailService feedingDetailService;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private EquSettingService equSettingService;
	
	@Override
	public void save(FeedingList feedingList) throws DoSthException {
		this.feedingListRepository.save(feedingList);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public FeedingList get(Serializable id) throws DoSthException {
		return this.feedingListRepository.findOne(id);
	}

	@Override
	public FeedingList update(FeedingList feedingList) throws DoSthException {
		return this.feedingListRepository.saveAndFlush(feedingList);
	}

	@Override
	public void delete(FeedingList feedingList) throws DoSthException {
		this.feedingListRepository.delete(feedingList);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ExtractionMethod> getWaitFinishFeedingList(String cabinetId, String feedingAccountId) {
		List<ExtractionMethod> list = new ArrayList<>();
		List<FeedingList> feedingListList = this.feedingListRepository.getFeedingList(cabinetId, feedingAccountId, YesOrNo.NO);
		feedingListList.forEach(feedingList -> {
			list.add(new ExtractionMethod(feedingList.getId(), feedingList.getFeedingName()));
		});
		return list;
	}

	@Override
	public FeedingList finishFeedingList(FeedingList feedingList) {
		feedingList.setIsFinish(YesOrNo.YES);
		Map<String, Integer> map;
		List<FeedingDetail> detailList = this.feedingDetailService.getFeedingDetailListByFeedingListId(feedingList.getId());
		EquDetailSta sta;
		// 推送最新数量到客户端
		for (FeedingDetail detail : detailList) {
			map = new HashMap<>();
			map.put(detail.getEquDetailSta().getId(), detail.getEquDetailSta().getCurNum() + detail.getFeedingNum());

			sta = detail.getEquDetailSta();
			// 更新库存数量
			sta.setCurNum(detail.getEquDetailSta().getCurNum() + detail.getFeedingNum());
			// 更新上架时间
			sta.setLastFeedTime(new Date());
			this.equDetailStaService.update(sta);
			
			GlobalUserUtil.writeMsg(detail.getEquDetailSta().getEquDetail().getEquSettingId(), MsgType.LATTICEVALUE, map);
		}
		return update(feedingList);
	}

	@Override
	public void feed(String feed, String cabinetId, String accountId, FeedType feedType) {
		FeedingList list = new FeedingList(cabinetId, DateUtil.getAllTime(), accountId, YesOrNo.YES, feedType);
		// 手动补料为即时补料,补料清单创建人与补料人为当前操作人
		list.setFeedingAccountId(accountId);
		list.setFeedingTime(new Date());
		save(list);
		// 分割传送补料信息,staId,num;staId,num;.....
		String[] feeds = feed.split(";");
		EquDetailSta sta = null;
		for (int i = 0; i < feeds.length; i++) {
			// 查询当前弹簧信息(当前存储物料)
			sta = this.equDetailStaService.get(feeds[i].split(",")[0]);
			this.feedingDetailService.save(new FeedingDetail(list.getId(), sta.getId(), sta.getMatInfoId(), Integer.valueOf(feeds[i].split(",")[1])));
			// 更新库存数量
			sta.setCurNum(sta.getCurNum() + Integer.valueOf(feeds[i].split(",")[1]));
			// 更新上架时间
			sta.setLastFeedTime(new Date());
			this.equDetailStaService.update(sta);
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<FeedingList> getWaitFeedingNum(String cabinetId) {
		List<FeedingList> feedingListList = this.feedingListRepository.getWaitFeedingNum(cabinetId, YesOrNo.NO);
		return feedingListList;
	}
	
	// 补料信息推送
	public void feedPost(String feedingListId, List<FeedingDetail> detailList, String[] feeds, String cabinetId, String accountId, ExternalSetting setting) {
		LinkedMultiValueMap<String, Object> feedMap = new LinkedMultiValueMap<>();
		LinkedMultiValueMap<String, Object> externalMap = new LinkedMultiValueMap<>();
		// 访问外部接口时间
		LocalDateTime dateTime = LocalDateTime.now();
		String time= dateTime.toString();
		
		int num = 0;
		if(detailList != null && detailList.size() != 0) {
			for (FeedingDetail detail : detailList) {
				// 补料信息
				ExternalFeeding feedingInfo = new ExternalFeeding(detail.getFeedingList().getFeedingAccountId(), detail.getFeedingList().getUser().getUserName(), 
						detail.getFeedingNum(), detail.getMatInfoId(), detail.getMatInfo().getMatEquName(), detail.getMatInfo().getSpec(),
						detail.getMatInfo().getBarCode(), detail.getMatInfo().getManufacturerId(), detail.getMatInfo().getManufacturer().getManufacturerName(),
						detail.getFeedingList().getCabinetId(), detail.getEquDetailSta().getEquDetail().getEquSetting().getEquSettingName(), time);
				externalMap.add("feedingReceives", feedingInfo);
				num++;
			}
			externalMap.set("outAllNum", Integer.toString(num));
		} else {
			for (; num < feeds.length; num++) {
				UserInfo userInfo = this.adminService.getUserInfo(accountId);
				EquDetailSta sta = this.equDetailStaService.get(feeds[num].split(",")[0]);
				// 补料信息
				ExternalFeeding feedingInfo = new ExternalFeeding(accountId, userInfo.getUserName(), Integer.parseInt(feeds[num].split(",")[1]), sta.getMatInfoId(), 
						sta.getMatInfo().getMatEquName(), sta.getMatInfo().getSpec(),sta.getMatInfo().getBarCode(), sta.getMatInfo().getManufacturerId(), 
						sta.getMatInfo().getManufacturer().getManufacturerName(),cabinetId, sta.getEquDetail().getEquSetting().getEquSettingName(), time);
				externalMap.add("feedingReceives", feedingInfo);
			}
			externalMap.set("outAllNum", Integer.toString(num + 1));
		}
		feedMap.set("feedingReceiveInfos", externalMap);
		String param = JSON.toJSONString(feedMap);
		
		String token = null;
		if(setting.getTokenMethod() != null) {
			token = ExternalUtil.ExternalGet(setting.getUrl() + setting.getMethod1());
		}
		JSONObject result = ExternalUtil.ExternalPost(param, token, setting.getUrl() + setting.getFeedMethod());
		
		// 请求不成功时记录
		YesOrNo yesOrNo = YesOrNo.YES;
		String message = null;
		if(result != null) {
			message = result.getString("massage");
			String code = result.getString("code");
			if(code == null || !code.equals("OK")) {
				yesOrNo = YesOrNo.NO;
			}
		}else {
			yesOrNo = YesOrNo.NO;
			message = "接口连接失败";
		}
		this.postStatusRepository.save(new PostStatus(true, null, null, setting.getUrl() + setting.getFeedMethod(),
				param, yesOrNo, message));
	}

	@Override
	public OpTip feedingList(List<FeignFeedingList> feedingInfos) {
		OpTip tip = new OpTip(200, "生成补料清单成功");
		
		// 按照刀具柜分别生成补料清单
		for(FeignFeedingList feedInfo : feedingInfos) {// 区分补料方式
			
			if(feedInfo.getFeedType().equals("MAT")) {// 按照物料-数量方式补货
				tip = feedByMatInfo(feedInfo);
				
				if(tip.getCode() != 200) {
					return tip;
				}
			}else if(feedInfo.getFeedType().equals("STA")){// 按照货道方式补货
				tip = feedByStaInfo(feedInfo);
				
				if(tip.getCode() != 200) {
					return tip;
				}
			}else {
				tip.setCode(201);
				tip.setMessage("请指定正确的补货方式");
				return tip;
			}
		}
		return tip;
	}

	@Override
	public FeignFeedingList getFeeding(String feedingListId) {
		FeedingList feedingList = this.feedingListRepository.getOne(feedingListId);
		FeignFeedingList feignFeedingList = new FeignFeedingList();
		feignFeedingList.setWareHouseAlias(feedingList.getEquSetting().getWareHouseAlias());
		List<FeedingDetail> detailList = this.feedingDetailService.getFeedingDetailListByFeedingListId(feedingListId);
		FeignMat feignMat;
		MatEquInfo info;
		for (FeedingDetail detail : detailList) {
			feignMat = new FeignMat();
			info = detail.getMatInfo();
			feignMat.setManufacturerName(info.getManufacturer().getManufacturerName()); // 供应商名称
			feignMat.setMatName(info.getMatEquName()); // 物料名称
			feignMat.setBarCode(info.getBarCode()); // 物料编号
			feignMat.setSpec(info.getSpec()); // 规格
			feignMat.setPackNum(info.getNum()); // 包装数量
			feignMat.setStotePrice(info.getStorePrice()); // 单价
			feignMat.setBrand(info.getBrand()); // 品牌
			feignMat.setBorrowType(info.getBorrowType().getMessage()); // 领用方式
			feignFeedingList.getDetailList().add(new FeignFeedingDetail(detail.getEquDetailSta().getId(),
					detail.getEquDetailSta().getEquDetail().getRowNo(), detail.getEquDetailSta().getColNo(), 
					feignMat, detail.getFeedingNum()));
		}
		return feignFeedingList;
	}
	
	/**
	 * @description 按照物料数量补货（外部接口用）
	 * @param FeignFeedingList
	 * @return
	 */
	public OpTip feedByMatInfo(FeignFeedingList feedInfo) {
		OpTip tip = new OpTip(200, "生成补料清单成功");
		
		Map<MatEquInfo, List<EquDetailSta>> matStaMap = this.equDetailStaService.getStaListGroupByMat(feedInfo.getWareHouseAlias());
		Map<String, Integer> barCodeWaitNumMap = new HashMap<>();
		List<EquDetailSta> staList;
		int maxNum;
		int curNum;
		for (Entry<MatEquInfo, List<EquDetailSta>> entryMatSta : matStaMap.entrySet()) {
			staList = entryMatSta.getValue();
			maxNum = staList.stream().mapToInt(EquDetailSta::getMaxReserve).sum();
			curNum = staList.stream().mapToInt(EquDetailSta::getCurNum).sum();
			barCodeWaitNumMap.put(entryMatSta.getKey().getBarCode(), maxNum - curNum);
		}
		
		StringBuilder message = new StringBuilder();
		for (FeignFeedingDetail feedMat : feedInfo.getDetailList()) {
			if (barCodeWaitNumMap.get(feedMat.getMat().getBarCode()) == null) {
				message.append("[").append(feedMat.getMat().getBarCode()).append("]未匹配到货道;");
				continue;
			}
			if (barCodeWaitNumMap.get(feedMat.getMat().getBarCode()) < feedMat.getFeedingNum()) {
				message.append("[").append(feedMat.getMat().getBarCode()).append("]超出最大补货量(").
				append(feedMat.getFeedingNum()).append("/").append(barCodeWaitNumMap.get(feedMat.getMat().getBarCode())).append(");");
			}
		}
		if (message.length() > 0)	 {
			tip = new OpTip(201, message.toString());
			return tip;
		}
		
		int supplyNum; // 补料数量
		
		FeedingList feedingList = new FeedingList();
		List<EquSetting> settingList = this.equSettingService.getCabinetListByWareHouseAlias(feedInfo.getWareHouseAlias());
		if (settingList != null && settingList.size() > 0) {
			feedingList.setCabinetId(settingList.get(0).getId());
		}
		feedingList.setFeedingName(DateUtil.getAllTime());
		feedingList.setCreateAccountId("101");
		feedingList.setCreateTime(new Date());
		feedingList.setIsFinish(YesOrNo.NO);
		feedingList.setFeedType(FeedType.API);
		feedingList = this.feedingListRepository.save(feedingList);
		
		MatEquInfo info;
		
		for (FeignFeedingDetail matInfo : feedInfo.getDetailList()) {
			supplyNum = matInfo.getFeedingNum();
			for (Entry<MatEquInfo, List<EquDetailSta>> entryMatSta : matStaMap.entrySet()) {
				info = entryMatSta.getKey();
				if (!matInfo.getMat().getBarCode().equals(info.getBarCode())) {
					continue;
				}
				
				staList = entryMatSta.getValue();
				for (EquDetailSta sta : staList) {
					if (supplyNum <= 0) {
						break;
					}
					if (sta.getMaxReserve() > sta.getCurNum()) {
						if (supplyNum > sta.getMaxReserve() - sta.getCurNum()) {
							this.feedingDetailService.save(new FeedingDetail(feedingList.getId(), sta.getId(), entryMatSta.getKey().getId(), sta.getMaxReserve() - sta.getCurNum()));
							supplyNum -= sta.getMaxReserve() - sta.getCurNum();
						} else {
							this.feedingDetailService.save(new FeedingDetail(feedingList.getId(), sta.getId(), entryMatSta.getKey().getId(), supplyNum));
							supplyNum = 0;
						}
					}
				}
				break;
			}
		}
		return tip;
	}
	
	/**
	 * @description 按照货道补货（外部接口用）
	 * @param FeignFeedingList
	 * @return
	 */
	public OpTip feedByStaInfo(FeignFeedingList feedInfo) {
		OpTip tip = new OpTip(200, "生成补料清单成功");
		
		for (FeignFeedingDetail detail : feedInfo.getDetailList()) {
            EquDetailSta sta = null;
            // 查询当前弹簧信息(当前存储物料)
            List<EquDetailSta> staList = this.equDetailStaService.getStaByRowCol(feedInfo.getWareHouseAlias(), detail.getRowNo(), detail.getColNo());
            // 物料CHECK
            if(staList == null || staList.size() == 0) {
                return new OpTip(201, feedInfo.getWareHouseAlias() + "：货道查询失败请确认");
            }
            
            sta = staList.get(0);
            if(!sta.getMatInfoId().equals(detail.getMat().getMatId())) {
            	if(!sta.getMatInfo().getBarCode().equals(detail.getMat().getBarCode())) {
            		return new OpTip(201, "F" + sta.getEquDetail().getRowNo() + "-"+sta.getColNo() + "：上架物料不符，请确认");
            	}
            }
            
            if(detail.getFeedingNum() > sta.getMaxReserve() - sta.getCurNum()) {
                return new OpTip(201, "F" + sta.getEquDetail().getRowNo() + "-"+sta.getColNo() + "：数量大于可补数量");
            }
        }
		
		List<EquSetting> settingList = this.equSettingService.getCabinetListByWareHouseAlias(feedInfo.getWareHouseAlias());
		List<ViewUser> user = this.userRepository.findUserByUserName(feedInfo.getUserName());
		FeedingList list = this.feedingListRepository.save(new FeedingList(settingList.get(0).getId(), DateUtil.getAllTime(), user.get(0).getAccountId(), YesOrNo.NO, FeedType.API));
		for (FeignFeedingDetail detail : feedInfo.getDetailList()) {
			List<EquDetailSta> staList = this.equDetailStaService.getStaByRowCol(feedInfo.getWareHouseAlias(), detail.getRowNo(), detail.getColNo());
			this.feedingDetailService.save(new FeedingDetail(list.getId(), staList.get(0).getId(), staList.get(0).getMatInfoId(), detail.getFeedingNum()));
		}
		return tip;
	}
}