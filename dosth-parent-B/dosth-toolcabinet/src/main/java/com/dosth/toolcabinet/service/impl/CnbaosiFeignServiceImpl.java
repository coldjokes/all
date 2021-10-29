package com.dosth.toolcabinet.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cnbaosi.dto.tool.ApplyMatDetail;
import com.cnbaosi.dto.tool.ApplyVoucher;
import com.cnbaosi.dto.tool.FeignFeedingList;
import com.cnbaosi.dto.tool.FeignWarehouseFeed;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.dto.MatDetail;
import com.dosth.toolcabinet.service.CnbaosiFeignService;
import com.dosth.toolcabinet.service.ToolService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description 鲍斯对外接口实现
 * @author guozhidong
 *
 */
@Service
public class CnbaosiFeignServiceImpl implements CnbaosiFeignService {
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private CabinetConfig cabinetConfig;
	@Autowired
	private ToolService toolService;
	
	@Override
	public List<ApplyVoucher> getApplyVoucherList(String userName, String search) {
		String cabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(), SetupKey.Cabinet.MAIN_CABINET_ID);
		// 刀具柜仓库Id与第三方仓库标识对照
		String wareHouseAlias = this.toolService.getWareHouseAliasByCabinetId(cabinetId);
		List<ApplyVoucher> voucherList = new ArrayList<ApplyVoucher>();
		String url = String.format(this.cabinetConfig.getApplyVoucherUrl() 
				+ "?wareHouseAlias=%s&userName=%s&search=%s", wareHouseAlias.replaceAll("\"", ""), userName, search);
		ResponseEntity<String> entity =  this.restTemplate.getForEntity(url, String.class);
		JSONArray array = JSONArray.fromObject(entity.getBody());
		if (array.isEmpty()) {
			return voucherList;
		}

		ApplyVoucher voucher;
		ApplyMatDetail matDetail;
		MatDetail detail = null;
		String applyNo;
		JSONArray arrVoucher = JSONArray.fromObject(array);
		JSONArray arrMatDetail;
		JSONObject objVoucher;
		JSONObject objMatDetail;
		// 获取物料当前库存
		Map<String, Integer> matNumMap = this.toolService.getMatNumMapByCabinetId(cabinetId);
		for (int i = 0; i < arrVoucher.size(); i++) {
			objVoucher = JSONObject.fromObject(arrVoucher.get(i));
			applyNo = objVoucher.getString("applyNo");
			voucher = new ApplyVoucher();
			voucher.setApplyNo(applyNo);
			arrMatDetail = JSONArray.fromObject(objVoucher.get("detailList"));
			for (int j = 0; j < arrMatDetail.size(); j++) {
				objMatDetail = JSONObject.fromObject(arrMatDetail.get(j));
				detail = this.toolService.getMatDetailByBarCode(objMatDetail.getString("barCode"));
				matDetail = new ApplyMatDetail();
				matDetail.setBarCode(objMatDetail.getString("barCode"));
				matDetail.setMatName(objMatDetail.getString("matName"));
				matDetail.setSpec(objMatDetail.getString("spec"));
				matDetail.setBorrowNum(Float.valueOf(objMatDetail.getString("borrowNum")));
	            if (detail.getMatId() != null && !"".equals(detail.getMatId())) {
	            	matDetail.setMatId(detail.getMatId());
	            	matDetail.setBorrowType(detail.getBorrowTypeCode());
	            	matDetail.setRemainNum(matNumMap.get(detail.getMatId()) == null ? 0 : matNumMap.get(detail.getMatId()));
	            }
	            voucher.getDetailList().add(matDetail);
			}
			voucherList.add(voucher);
		}
		return voucherList;
	}

	@Override
	public void sendApplyVoucherResult(String applyVoucherResult) {
		List<ApplyVoucher> voucherList = this.toolService.getApplyVoucherResult(applyVoucherResult);
		JSONArray array = JSONArray.fromObject(voucherList);
		this.restTemplate.getForEntity(this.cabinetConfig.getApplyVoucherResultUrl() + "?result={json}", String.class, array.toString());
	}

	@Override
	public void syncFeedingList(String feedingListId) {
		List<FeignWarehouseFeed> warehouseInfos = this.toolService.warehouseFeedList(feedingListId);
		if(warehouseInfos != null && warehouseInfos.size() > 0) {
			for(FeignWarehouseFeed info : warehouseInfos) {
				JSONObject obj = JSONObject.fromObject(info);
				JSONObject result = this.restTemplate.postForObject(this.cabinetConfig.getWarehouseResultUrl(), obj, JSONObject.class);
				System.err.println(result);
				this.toolService.warehouseSave(info.getOrderNo(), result.getString("message"), result.getBoolean("success"));
			}
		}else {
			FeignFeedingList feedingList = this.toolService.syncFeedingList(feedingListId);
			JSONObject obj = JSONObject.fromObject(feedingList);
			String json = obj.toString();
			this.restTemplate.getForEntity(this.cabinetConfig.getSyncFeedingListUrl() + "?result={json}", JSONObject.class, json);
		}
	}
}