$(function(){
	
	$(".logType").append($.customOptions("logType", true));
	$("#logSearchForm [name='startTime']").val(startTime.valueOf()); 
	$("#logSearchForm [name='endTime']").val(endTime.valueOf()); 
	
	//渲染表格
    var tbl = $("#serviceLogTbl").bootstrapTable({
      url: Api.url("log"),
      columns: [
    	    {
    		   field: "seq",
    		   title: "序号",
    		   width:"5%",
    		   align: "center",
    		   formatter: function(value, row, index){
    			   return $.getTableRowNum(tbl, index);
    		   }
          	}, {
                field: "username",
                title: "用户名",
                align: "center"
            }, {
		    	field: "logType",
		    	title: "日志类型",
                align: "center",
                formatter: function(value, row, index){
                	return Dict.getText("logType", value);
                }
		    }, {
                field: "business",
                title: "操作描述",
                align: "center",
            }, {
                field: "ipAddress",
                title: "IP地址",
                align: "center",
            },{
                field: "createTime",
                title: "记录时间",
                sortable : true,
                align: "center",
                formatter:function(value, row, index){
                	return moment(value).format(DATE_FMT)
                }
            }
        ],
        queryParams : function (params) { //查询的参数
        	const defaultParams = $.getTableDefaultQueryParams(params);
        	const formData = $.getFormObject($("#logSearchForm"));
        	return $.extend({}, defaultParams, formData);
        }
    });

    // 点击查询按钮
	$("#logListSearchBtn").on("click", function(){
		$.refreshTable(tbl);
	})
	
	// 初始化日期选择插件
	$(".daterangePicker").daterangepicker({
		
	}).
	on('cancel.daterangepicker', function(ev, picker) {
		$('.daterangePicker').val("");
		$("#logSearchForm [type='hidden']").val(""); 
		
	}).
	on("apply.daterangepicker", function(ev, picker) { 
		$("#logSearchForm [name='startTime']").val(picker.startDate.valueOf()); 
		$("#logSearchForm [name='endTime']").val(picker.endDate.valueOf()); 
	});

	
})

