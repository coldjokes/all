/**
 * 自定义ajax相关方法
 */
$(function(){

	var timeout = 10000;
	var contentTypeJson = "application/json; charset=utf-8";
	var contentTypeText = "text/plain; charset=utf-8";

    /*提交json数据的ajax-post请求*/
    $.postJSON = function(url, data, callback, async){
    	callAjax(url, JSON.stringify(data), callback, "post", async);
    };
    /*删除数据的ajax-delete请求*/
    $.deleteJSON = function(url, data, callback, async){
    	callAjax(url, JSON.stringify(data), callback, "delete", async);
    };
    /*修改数据的ajax-put请求*/
    $.putJSON = function(url, data, callback, async){
    	callAjax(url, JSON.stringify(data), callback, "put", async);
    };
	
	/*获取数据ajax-get请求*/
    $.getJSON = function (url, data, callback, async){
    	callAjax(url, data, callback, "get", async);
    };
    
    /*导入文件*/
    $.importFile = function (url, inputId, callback){
    	var formData = new FormData();
		const files = document.getElementById(inputId).files[0];
		formData.append('file', files);
		$.ajax({
			url: url,
			type: 'post',
			data: formData,
			contentType: 'multipart/form-data',
			processData: false, // 告诉jQuery不要去处理发送的数据
			contentType: false, // 告诉jQuery不要去设置Content-Type请求头
			success: function(response) {
				if(callback){
            		callback(response);
            	}
			},
			error: function(response) {
				toastr.error("上传失败，服务器内部错误！")
			}
		});
    };
    
    var callAjax = function(url, data, callback, method, async){
    	if(typeof async == "undefined"){
    		async = true;
    	}
    	$.ajax({
            url: url,
            type: method,
            contentType: contentTypeJson,
            async: async,
            timeout: timeout,
            data: data,
            dataType: "json",
            success: function(response){
            	if(callback){
            		callback(response);
            	}
            },
            error: function(XMLHttpRequest, textStatus, errorThrown){
            	console.log("服务器未正常响应，请重试");
            	console.log("请求路径：" + url);
            	/*
            	 	XMLHttpRequest.readyState:
            		0 	（未初始化）还没有调用send()方法 
            		1 	（载入）已调用send()方法，正在发送请求 ，服务器连接已建立
            		2 	（载入完成）send()方法执行完成，已经接收到全部响应内容 ，请求已接收
            		3 	（交互）正在解析响应内容 ， 请求处理中
            		4 	（完成）响应内容解析完成，可以在客户端调用了，请求已完成，且响应已就绪
            	*/
            	console.log("当前状态：" + XMLHttpRequest.readyState);
            	console.log("HTTP状态码：" + XMLHttpRequest.status);
            	/*
        		  	textStatus:
        			timeout		超时
        			error		错误
        			abort		中止 
        			parsererror 解析错误
        			空值
        		*/
            	console.log("返回的状态：" + textStatus);
            	console.log("错误信息：" + errorThrown);
            }
        });
    }
});