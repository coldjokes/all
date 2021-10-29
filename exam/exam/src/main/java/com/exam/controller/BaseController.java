package com.exam.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.exam.entity.RestFulResponse;

/**
 *  controller基类
 * 
 * @author Yifeng Wang  
 */
public class BaseController {
	
	public <T> RestFulResponse<T> success(String message) {
		return success(0, null, message);
    }
	
	public <T> RestFulResponse<T> success(long total, List<T> results) {
		return success(total, results, null);
	}
	
	public <T> RestFulResponse<T> success(long total, List<T> results, String message) {
		RestFulResponse<T> apiResponse = new RestFulResponse<>();
		apiResponse.setCode(HttpServletResponse.SC_OK);
		apiResponse.setTotal(total);
		apiResponse.setResults(results);
		apiResponse.setMessage(message);
		return apiResponse;
	}
    
    public <T> RestFulResponse<T> error(String message) {
    	return error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message, null);
    }
    
    public <T> RestFulResponse<T> error(Integer code, String message) {
    	return error(code, message, null);
    }

    public <T> RestFulResponse<T> error(Integer code, String message, List<T> results) {
        RestFulResponse<T> response = new RestFulResponse<T>();
        response.setCode(code);
        response.setMessage(message);
        response.setResults(results);
        return response;
    }
    
    /**
     * 	操作结果
     */
    public <T> RestFulResponse<T> actionResult(Boolean result){
    	return result ? success("操作成功") : error("操作失败");
    }
}

