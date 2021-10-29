package com.dosth.tool;

import org.springframework.boot.test.context.SpringBootTest;

import com.alibaba.fastjson.JSONObject;
import com.dosth.common.constant.YesOrNo;
import com.dosth.tool.external.util.ExternalUtil;

@SpringBootTest
public class ExternalTest {
	
	public static void main(String[] args) {
		
		String param = "{ \"consumingReceiveInfos\": [ { \"consumingReceives\": [ { \"accountId\": \"012\", \"accountName\": \"张三\", \"equId\": \"333323\", \"equName\": \"32r34r43\", \"matBarcode\": \"TG010001\", \"matId\": \"1578c9b4-4384-4204-b193-58151da440b5\", \"matName\": \"40米大长刀\", \"matSpec\": \"40m\", \"opDate\": \"2019-12-24T06:31:03.345Z\", \"outBillNum\": \"10\", \"partVersion\": \"V1.1\", \"returnDate\": \"2019-12-24T06:31:03.345Z\",\"houseName\": \"生产库房\", \"supplyCode\": \"SU00001\", \"supplyFullName\": \"张家港鼎力机械有限公司\", \"supplyID\": \"e14779eff7e0492cabaf919ecc578522\" } ], \"outAllNum\": \"10\" } ] }";
		// GET请求
//		String result = ExternalUtil.ExternalGet("http://47.112.20.176:4001/invmng/tokenInfo/getToken");
		
		// POST请求
		JSONObject result = ExternalUtil.ExternalPost(param, "WspJ91XCSi3UtTfjs3+fNuhicU6aWodCXZMaGgZVy06p3BvNhJL5dBNsTSFSvm0kCZitdRGBiXtjuqIEiXynpk6QLH2gUcFfy11DGmugVn+cp7uNnOOP4IZsJvZghEvtGg6lrH4FTxkAh4kjhVztw==", "http://47.112.20.176:4001/invmng/outwarehouse");
		// PUT请求
		// DELETE请求
		System.err.println(result);
		YesOrNo yesOrNo = YesOrNo.YES;
		if(result != null) {
			String status = result.getString("code");
			if(status == null || !status.equals("OK")) {
				yesOrNo = YesOrNo.NO;
			}
		}else {
			yesOrNo = YesOrNo.NO;
		}
		System.err.println(yesOrNo);
	}
}
