package com.dosth.toolcabinet.commu.scaner;

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
import com.dosth.toolcabinet.commu.AppointmentScanCompoment;
import com.dosth.toolcabinet.util.AppBorrowUtil;
import com.dosth.util.OpTip;

import net.sf.json.JSONObject;

public class AppointmentScanerResponseUser implements ProcessScanerResponse {
	private static final Logger logger = LoggerFactory.getLogger(AppointmentScanerResponseUser.class);
	private Scaner scanObj;
	private AppointmentScanCompoment scanCompoment;
	private volatile String cabinetID = "";
	private String ip;

	public AppointmentScanerResponseUser(AppointmentScanCompoment scom, Scaner scanObj, String ip) {
		this.scanObj = scanObj;
	    this.scanCompoment = scom;
	    this.ip = ip;
	}

	/**
	 * @description 设置柜子ID
	 * @param cabinetID 柜子ID
	 */
	public synchronized void setCabinetIDInner(String cabinetID) {
		this.cabinetID = cabinetID;
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
		if (!scanObj.isKeepMonitoringMode()) {
			if (!scanCompoment.isNonReturning()) {
				logger.info("料斗取料中，请等待。。。");
				MP3Player.play("AUDIO_D2.mp3");
				return response;
			}
			try {
				String orderId = this.getAppOrderId(response);
				String userId = this.getUserId(response);
				Map<String, Map<String, Integer>> map = this.getJson2Map(response, this.cabinetID);
				
				if (map.size() < 1) {
					logger.warn("预约清单为空或柜子不匹配");
					MP3Player.play("AUDIO_E2.mp3");
					return response;
				}
				
				boolean isAppointmentCompleted = scanCompoment.isAppointmentCompletedByID(this.cabinetID);
	 			if (isAppointmentCompleted) {
	 				logger.warn("The appointment has been taken out");
	 				MP3Player.play("AUDIO_E3.mp3");
	 				return response;
	 			}
				
				// 设置手机订单完成状态
	 			OpTip opTip = scanCompoment.setAppointmentCompletedByID(this.cabinetID, true);
	 			int succCode = 200;
	 			if (opTip.getCode() != succCode) {
	 				logger.error("Submit setAppointmentCompletedByID() failed");
	 				return response;
	 			}
				
				logger.info("验证成功，机柜开始取料。。。");
				MP3Player.play("AUDIO_E1.mp3");
				String orderStr = this.getAppointmentStr(map);
				logger.info("取料信息>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + orderStr);
				scanCompoment.send(new SoftHandCommMsg(SoftHandComm.UNDEFINED, orderStr));
				try {
					AppBorrowUtil.putReturnBackInfo(orderId);
					//向APP推送关门信息
					this.httpGet(ip, userId, this.cabinetID);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				logger.error("扫描信息异常:" + e.getMessage());
			} finally {
				scanCompoment.stopMonitorScan();
			}
		} else {
			logger.warn("Enter ScanerResponseUser.returns_verify_mode");
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

	/**
	 * @description 转换行列数量对象到取料格式数据
	 * @param map 行列数量对象
	 * @return
	 */
	private String getAppointmentStr(Map<String, Map<String, Integer>> map) {
		StringBuilder builder = new StringBuilder(CompomentUtil.Appointment_Cmd);
		builder.append(":");
		for (Entry<String, Map<String, Integer>> row : map.entrySet()) {
			for (Entry<String, Integer> col: row.getValue().entrySet()) {
				builder.append(row.getKey());
				builder.append(",");
				builder.append(col.getKey());
				builder.append("-");
				builder.append(col.getValue());
				builder.append(";");
			}
		}
		logger.info("****************************" + builder.toString());
		return builder.toString();
	}
	
	/**
	 * @description 根据json数据解析出当前柜子需要获取的取料信息
	 * @param response 手机订单json数据
	 * @param curCabinetId 当前柜子Id
	 * @return
	 */
	private Map<String, Map<String, Integer>> getJson2Map(String response, String curCabinetId) {
		Map<String, Map<String, Integer>> rowMap = new HashMap<>(); // 行标识
		Map<String, Integer> colMap = null; // 列标识
		
		JSONObject orderObj = JSONObject.fromObject(response);
		JSONObject matObjs;
		JSONObject cabinetObjs;
		JSONObject rowObjs;
		JSONObject colObjs;
		Iterator<?> orderKeys;
		Iterator<?> matKeys;
		Iterator<?> cabinetKeys;
		Iterator<?> rowKeys;
		Iterator<?> colKeys;
		
		String cabinetId;
		String rowNo;
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
						continue;
					}
					rowObjs = JSONObject.fromObject(cabinetObjs.get(cabinetId));
					rowKeys = rowObjs.keys();
					while (rowKeys.hasNext()) { // 遍历行数据
						rowNo = String.valueOf(rowKeys.next());
						colObjs = JSONObject.fromObject(rowObjs.get(rowNo));
						colKeys = colObjs.keys();
						colMap = rowMap.get(rowNo);
						if (colMap == null) {
							colMap = new HashMap<>();
						}
						while (colKeys.hasNext()) { // 遍历列数据
							colNo = String.valueOf(colKeys.next());
							colMap.put(colNo, colObjs.getInt(colNo));
						}
						rowMap.put(rowNo, colMap);
					}
				}
			}
		}
		return rowMap;
	}
}