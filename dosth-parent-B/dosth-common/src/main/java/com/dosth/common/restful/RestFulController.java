package com.dosth.common.restful;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.dosth.common.controller.BaseController;

public class RestFulController extends BaseController{

	public <T> ApiResponse<T> success(String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpServletResponse.SC_OK);
        apiResponse.setMessage(message);
        return apiResponse;
    }
    
    public <T> ApiResponse<T> success(Integer total, List<T> results) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setTotal(total);
        response.setResults(results);
        return response;
    }

    public <T> ApiResponse<T> error(String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        apiResponse.setMessage(message);
        return apiResponse;
    }
    
    public <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    public <T> ApiResponse<T> error(Integer code, String message, List<T> results) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.setCode(code);
        response.setMessage(message);
        response.setResults(results);
        return response;
    }


}
