/**
 * 初始化插件相关设置
 */
$(function(){
	
	// bootstrap table
	initBootstrapTable();
	
	// jquery validate 验证规则
	initJqueryValidate();

	// daterangePicker 
	initDaterangePicker();
	
	// toastr
	initToastr();
});

function initBootstrapTable(){

    //初始化表格
	if (_.isUndefined($.fn.bootstrapTable)) {
        return;
	}
	$.extend(true, $.fn.bootstrapTable.defaults, {
	      cache: false, //是否使用缓存，默认为true
	      striped: true, //是否显示行间隔色
	      sortable: true, //是否启用排序
	      sortOrder: "asc", //排序方式
	      pagination: true, //是否显示分页（*）
	      pageNumber: 1, //初始化加载第一页，默认第一页,并记录
	      pageSize: 10, //每页的记录行数（*）
	      sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
	      columns: [],
	      responseHandler: function(res){
	    	  if(res.code == 200){
	    		  return {
	    			  "total": res.total,
	    			  "rows": res.results
	    		  };
	    	  }
	    	  return res;
	      },
    })
    
    // 自定义表格查询参数
	$.getTableDefaultQueryParams = function(params){
        var temp = {  
                curPage: (params.offset / params.limit) + 1, //页码
                pageSize: params.limit, //每页显示个数
                sortField: params.sort,	//排序列名  
                sortOrder: params.order, //正序、反序
        };
        return temp;
   }
   
   // 获取表格行号
   $.getTableRowNum = function(table, index){
       var options = table.bootstrapTable('getOptions');
       return options.pageSize * (options.pageNumber - 1) + index + 1;
   }
   // 刷新表格
   $.refreshTable = function(table){
	   table.bootstrapTable("selectPage", 1);
   }
   // 刷新表格（适用于不分页的情况）
   $.refreshTableManual = function(table){
	   table.bootstrapTable("selectPage", 1);
	   table.bootstrapTable("refresh");
   }
}

function initJqueryValidate(){
	// 字符验证       
	jQuery.validator.addMethod("stringCheck", function(value, element) {       
		return this.optional(element) || /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/.test(value);
	}, "只能包括中文字、英文字母、数字和下划线");   
	 
	// 中文字两个字节       
	jQuery.validator.addMethod("byteRangeLength", function(value, element, param) {       
	   var length = value.length;       
	   for(var i = 0; i < value.length; i++){       
	       if(value.charCodeAt(i) > 127){       
	       length++;       
	       }       
	   }       
	   return this.optional(element) || ( length >= param[0] && length <= param[1] );       
	}, "请确保输入的值在3-15个字节之间(一个中文字算2个字节)");   
	 
	// 身份证号码验证       
	jQuery.validator.addMethod("isIdCardNo", function(value, element) {       
		return this.optional(element) || isIdCardNo(value);       
	}, "请正确输入您的身份证号码");    
	    

	// 手机号码验证       
	jQuery.validator.addMethod("isMobile", function(value, element) {       
	   var length = value.length;   
	   var mobile = /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/;   
	   return this.optional(element) || (length == 11 && mobile.test(value));       
	}, "请正确填写您的手机号码");       

	// 电话号码验证       
	jQuery.validator.addMethod("isTel", function(value, element) {       
	   var tel = /^\d{3,4}-?\d{7,9}$/;    //电话号码格式010-12345678   
	   return this.optional(element) || (tel.test(value));       
	}, "请正确填写您的电话号码");   
	 
	// 联系电话(手机/电话皆可)验证   
	jQuery.validator.addMethod("isPhone", function(value,element) {   
	   var length = value.length;   
	   var mobile = /^(((13[0-9]{1})|(15[0-9]{1}))+\d{8})$/;   
	   var tel = /^\d{3,4}-?\d{7,9}$/;   
	   return this.optional(element) || (tel.test(value) || mobile.test(value));   
	}, "请正确填写您的联系电话");   
	    
	// 邮政编码验证       
	jQuery.validator.addMethod("isZipCode", function(value, element) {       
	   var tel = /^[0-9]{6}$/;       
	   return this.optional(element) || (tel.test(value));       
	}, "请正确填写您的邮政编码");    
	
	// 邮箱验证       
	jQuery.validator.addMethod("checkEmail", function(value, element) {       
	   var tel = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;       
	   return this.optional(element) || (tel.test(value));       
	}, "请正确填写您的邮箱");    
}

function initDaterangePicker(){

	$.fn.daterangepicker.defaultOptions = {
		"timePicker": true,
	    "timePicker24Hour" : true,//设置小时为24小时制 默认false
	    "showDropdowns": true, //当设置值为true的时候，允许年份和月份通过下拉框的形式选择 默认false
	    //"autoUpdateInput": false,
	    "startDate": startTime,
	    "endDate": endTime,
	    "ranges": {
	    	'今天': [moment().startOf('day'), moment()],
            '过去 7 天': [moment().subtract(6, 'days'), moment()],
            '过去 30 天': [moment().subtract(29, 'days'), moment()],
            '这个月': [moment().startOf('month'), moment().endOf('month')],
            '过去 3 个月': [moment().subtract(3, 'month'), moment()],
            '过去 6 个月': [moment().subtract(6, 'month'), moment()]
	    },
	    locale: {
	    	format: "YYYY-MM-DD HH:mm", 
	    	separator: " - ", 
	    	applyLabel: "确认", 
	    	cancelLabel: "清空", 
	    	fromLabel: "开始时间", 
	    	toLabel: "结束时间", 
	    	customRangeLabel: "自定义", 
	    	daysOfWeek: ["日","一","二","三","四","五","六"], 
	    	monthNames: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"] 
	    }
	}
}

function initToastr(){
	toastr.options = {
	  "closeButton": true,//显示关闭按钮
	  "debug": false,//启用debug
	  "newestOnTop": false,
	  "progressBar": false,
	  "positionClass": "toast-top-right",//弹出的位置
	  "preventDuplicates": false,
	  "onclick": null,
	  "showDuration": "300",//显示的时间
	  "hideDuration": "1000",//消失的时间
	  "timeOut": "5000",//停留的时间
	  "extendedTimeOut": "1000",//控制时间
	  "showEasing": "swing",//显示时的动画缓冲方式
	  "hideEasing": "linear",//消失时的动画缓冲方式
	  "showMethod": "fadeIn",//显示时的动画方式
	  "hideMethod": "fadeOut"//消失时的动画方式
	}
}
