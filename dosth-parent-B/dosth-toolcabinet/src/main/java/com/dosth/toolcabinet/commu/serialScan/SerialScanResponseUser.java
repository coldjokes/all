package com.dosth.toolcabinet.commu.serialScan;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.CompomentUtil;
import com.dosth.comm.audio.MP3Player;
import com.dosth.comm.softhand.SoftHandComm;
import com.dosth.comm.softhand.SoftHandCommMsg;
import com.dosth.constant.ScanCodeTypeEnum;
import com.dosth.toolcabinet.commu.scaner.ProcessScanerResponse;
import com.dosth.toolcabinet.commu.scaner.Scaner;
import com.dosth.toolcabinet.util.AppBorrowUtil;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

import net.sf.json.JSONObject;

public class SerialScanResponseUser implements ProcessScanerResponse {
	private static final Logger logger = LoggerFactory.getLogger(SerialScanResponseUser.class);
	private SerialScanCompoment scanCompoment;
	private volatile String cabinetId = "";
	private Scaner scanObj;
	private String ip;

	public SerialScanResponseUser(SerialScanCompoment scom) {
	    this.scanCompoment = scom;
	}

	public SerialScanResponseUser(SerialScanCompoment scom, Scaner scanObj, String cabinetId, String ip) {
		this.scanObj = scanObj;
	    this.scanCompoment = scom;
	    this.cabinetId = cabinetId;
	    this.ip = ip;
	}

	/**
	 * @description 处理扫描仪扫出的二维码（字符串）
	 * @param response 扫描仪扫出的二维码（字符串）
	 *   response format: 
	 *   cabinetId:层号,马达号-取出数目;层号,马达号-取出数目;...;
	 *   for example 999:2,3-1;2,5-2;3,6-7
	 */
	@Override
	public synchronized String processScanerResponse(String response) {
		logger.info("扫描到的二维码数据>>>>" + response + ",下一步验证扫描仪状态");
		response = response.trim();
		if (!scanObj.isKeepMonitoringMode()) {
			if(response.contains(ScanCodeTypeEnum.NFC.getCode())) {
				response = response.replace(ScanCodeTypeEnum.NFC.getCode(), "").trim();
				WebSocketUtil.sendMsgSingle(cabinetId, new WsMsg(WsMsgType.ICSWIPING, response));
			} else {
				if(response.contains(ScanCodeTypeEnum.APP.getCode())) { //app预约取料
					response = response.replace(ScanCodeTypeEnum.APP.getCode(), "");
					response = doAppLogic(response);
				} else { //给已打印条码赋值（常州）
					WebSocketUtil.sendMsgSingle(cabinetId, new WsMsg(WsMsgType.PRINTED_CODE_INFO, response));
				}
			}
		}
		return response;
	}
	
	private String doAppLogic(String response) {
		if (!scanCompoment.isNonReturning()) {
			logger.info("料斗取料中，请等待。。。");
			MP3Player.play("AUDIO_D2.mp3");
			return response;
		}
		try {
			String orderId = this.getAppOrderId(response);
			String userId = this.getUserId(response);
			
			Map<String, Map<String, Map<String, Integer>>> map = this.getJson2Map(response, cabinetId);
			
			if (map.size() < 1) {
				logger.warn("预约清单为空或柜子不匹配");
				MP3Player.play("AUDIO_E2.mp3");
				return response;
			}
			
			boolean isAppointmentCompleted = scanCompoment.isAppointmentCompletedByID(orderId);
 			if (isAppointmentCompleted) {
 				logger.warn("预约订单已经取出，不可重复领取");
 				MP3Player.play("AUDIO_E3.mp3");
 				return response;
 			}
			
			logger.info("验证成功，机柜开始取料。。。");
			MP3Player.play("AUDIO_E1.mp3");
			String orderStr = this.getAppointmentStr(map);
			logger.info("取料信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + orderStr);
			scanCompoment.send(new SoftHandCommMsg(SoftHandComm.SHIFT, orderStr));
			try {
				AppBorrowUtil.putReturnBackInfo(orderId);
				//向APP推送关门信息
				this.httpGet(ip, userId, cabinetId);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			logger.error("扫描信息异常:" + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * @description 获取APP订单Id
	 * @param response
	 * @return
	 */
	private String getAppOrderId(String response) {
		JSONObject obj = JSONObject.fromObject(response);
		Iterator<?> orderIds = obj.keys();
		while (orderIds.hasNext()) {
			String str = String.valueOf(orderIds.next());
			int  lastStr = str.lastIndexOf("_");
			return str.substring(0,lastStr);
		}
		return null;
	}
	
	/**
	 * @description 获取APP用户Id
	 * @param response
	 * @return
	 */
	private String getUserId(String response) {
		JSONObject obj = JSONObject.fromObject(response);
		Iterator<?> orderIds = obj.keys();
		while (orderIds.hasNext()) {
			String str = String.valueOf(orderIds.next());
			int  lastStr = str.lastIndexOf("_");
			return str.substring(lastStr+1);
		}
		return null;
	}
	
	/**
	 * @description http请求
	 * @param ip,userId
	 * @return
	 */
	private void httpGet(String ip,String userId, String cabinetID) { 

        //请求地址
        String path = "http://"+ip+":9060/api/msg?uid="+userId+"&m=closeDoor&d="+cabinetID;	
        
        try {
        	URL url = new URL(path);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestProperty("accept", "*/*");
	        conn.setRequestProperty("connection", "Keep-Alive");
	        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
	        conn.setDoOutput(true);
	        conn.setDoInput(true);
	        conn.setRequestMethod("GET");
	        conn.connect(); 
	        InputStream is = conn.getInputStream();
	        //构造一个字符流缓存
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        String str = "";
	        while ((str = br.readLine()) != null) {
	        	str=new String(str.getBytes(),"UTF-8");//解决中文乱码问题
	        	logger.info(str);
	        }
        //关闭流
        is.close();
        conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
	
//	/**
//	 * @description 转换行列数量对象到取料格式数据
//	 * @param map 行列数量对象
//	 * @return
//	 */
//	private String getAppointmentStr(Map<String, Map<String, Map<String, Integer>>> map) {
//		StringBuilder builder = new StringBuilder(CompomentUtil.Appointment_Cmd);
//		builder.append(":");
//		for (Entry<String, Map<String, Map<String, Integer>>> row : map.entrySet()) {
//			for (Entry<String, Map<String, Integer>> plc: row.getValue().entrySet()) {
//				for(Entry<String, Integer> col: plc.getValue().entrySet()) {
//					builder.append(row.getKey());
//					builder.append(";");
//					builder.append(plc.getKey());
//					builder.append(",");
//					builder.append(col.getKey());
//					builder.append("-");
//					builder.append(col.getValue());
//					builder.append(";");
//				}
//			}
//		}
//		System.err.println("****************************" + builder.toString());
//		return builder.toString();
//	}
	
	private String getAppointmentStr(Map<String, Map<String, Map<String, Integer>>> map) {
		String row = null;
		Map<String, Map<String, Integer>> ipParaMap;
		Map<String, Integer> colMap;
		StringBuilder builder = new StringBuilder();
		for (Entry<String, Map<String, Map<String, Integer>>> entry : map.entrySet()) {
			row = entry.getKey(); // 行号
			ipParaMap = entry.getValue();
			StringBuilder b = new StringBuilder();
			b.append(CompomentUtil.Appointment_Cmd);
			b.append(":");
			b.append("Shift,");
			b.append(row);
			b.append(";");
			for (Entry<String, Map<String, Integer>> rowMotor : ipParaMap.entrySet()) {
				b.append(rowMotor.getKey());
				b.append(",");
				colMap = rowMotor.getValue();
				int i = 0;
				for (Entry<String, Integer> col : colMap.entrySet()) {
					b.append(String.format("%s-%s", col.getKey(), col.getValue()));
					b.append(i == colMap.size() - 1 ? "" : ","); // 最后一个叠加空,其余的添加逗号
					i++;
				}
				b.append(";");
			}
			builder.append(b);
			builder.append("&&");
		}
		return builder.toString();
	}
	
	/**
	 * @description 根据json数据解析出当前柜子需要获取的取料信息
	 * @param response 手机订单json数据
	 * @param curCabinetId 当前柜子Id
	 * @return
	 */
	private Map<String, Map<String, Map<String, Integer>>> getJson2Map(String response, String curCabinetId) {
		Map<String, Map<String, Map<String, Integer>>> rowMap= new HashMap<>(); // 行标识
		Map<String, Map<String, Integer>> plcMap= null; //  plc板标识
		Map<String, Integer> colMap = null; // 列标识
		
		JSONObject orderObj = JSONObject.fromObject(response);
		JSONObject matObjs;
		JSONObject cabinetObjs;
		JSONObject rowObjs;
		JSONObject plcObjs;
		JSONObject colObjs;
		Iterator<?> orderKeys;
		Iterator<?> matKeys;
		Iterator<?> cabinetKeys;
		Iterator<?> rowKeys;
		Iterator<?> plcKeys;
		Iterator<?> colKeys;
		
		String cabinetId;
		String rowNo;
		String plcIp;
		String colNo;
		
		orderKeys = orderObj.keys();
		while (orderKeys.hasNext()) { // 遍历订单
			matObjs = JSONObject.fromObject(orderObj.get(orderKeys.next()));
			matKeys = matObjs.keys();
			while (matKeys.hasNext()) { // 遍历物料
				cabinetObjs = JSONObject.fromObject(matObjs.get(matKeys.next()));
				cabinetKeys = cabinetObjs.keys();
				while (cabinetKeys.hasNext()) { // 遍历柜子
					cabinetId = String.valueOf(cabinetKeys.next());
					// 忽略非当前柜子数据
					if (!cabinetId.equals(curCabinetId)) {
						String sonCabinetId = scanCompoment.getCabinetId(curCabinetId);
						sonCabinetId= sonCabinetId.replace("\"", "");
						if(!sonCabinetId.toString().equals(cabinetId)) {
							continue;
						}
					}
					rowObjs = JSONObject.fromObject(cabinetObjs.get(cabinetId));
					rowKeys = rowObjs.keys();
					while (rowKeys.hasNext()) { // 遍历行数据
						rowNo = String.valueOf(rowKeys.next());
						plcObjs = JSONObject.fromObject(rowObjs.get(rowNo));
						plcKeys = plcObjs.keys();
						plcMap = rowMap.get(rowNo);
						if (plcMap == null) {
							plcMap = new HashMap<>();
						}
						while(plcKeys.hasNext()) { // 遍历plc板数据
							plcIp = String.valueOf(plcKeys.next());
							colObjs = JSONObject.fromObject(plcObjs.get(plcIp));
							colKeys = colObjs.keys();
							colMap = plcMap.get(plcIp);
							if (colMap == null) {
								colMap = new HashMap<>();
							}
							while (colKeys.hasNext()) { // 遍历列数据
								colNo = String.valueOf(colKeys.next());
								colMap.put(colNo, colObjs.getInt(colNo));
							}
							plcMap.put(plcIp, colMap);
						}
						rowMap.put(rowNo, plcMap);
					}
				}
			}
		}
		return rowMap;
	}
}