package com.dosth.tool.external.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

public class ExternalUtil{
	private static final Logger logger = LoggerFactory.getLogger(ExternalUtil.class);
	
    public static JSONObject ExternalPost(String params, String token, String url){
        // 使用RestTemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();
		
        // 设置请求header
        HttpHeaders headers = new HttpHeaders();
        if(token != null) {
        	headers.set("token",token);
        }
        headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        
        // 请求体,包括请求数据body和请求头 headers
		HttpEntity<String> httpEntity = new HttpEntity<String>(params, headers);
        JSONObject result = null;
        try {
            // 使用 exchange 发送请求,以String的类型接收返回的数据
            ResponseEntity<String> strbody = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
			// 解析返回的数据
            result = JSONObject.parseObject(strbody.getBody());
            logger.info("外部接口请求成功");
        }catch (Exception e){
        	logger.error("外部接口调用失败:" + e.getMessage());
        }
        return result;
    }
    
    public static String ExternalGet(String url){
        // 使用RestTemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();
		
        JSONObject result = null;
        try {
            // 使用 exchange 发送请求,以String的类型接收返回的数据
            ResponseEntity<String> strbody = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
			// 解析返回的数据
            result = JSONObject.parseObject(strbody.getBody());
            logger.info("外部接口请求成功");
        }catch (Exception e){
        	logger.error("外部接口调用失败:" + e.getMessage());
        }
        return result.getString("message");
    }
}