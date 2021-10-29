$(function(){
	//渲染表格
    var tbl = $("#matRemindListTbl").bootstrapTable({
      url: Api.url("materialRemind"),
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
                field: "name",
                title: "物料编码",
                align: "center"
            }, {
                field: "no",
                title: "刀片型号",
                align: "center",
		    }, {
                field: "spec",
                title: "图纸编号",
                align: "center",
            }, {
		    	field: "remark",
		    	title: "备注",
                align: "center"
		    }, {
		    	field: "createdUserFullname",
		    	title: "添加人",
                align: "center"
		    }, {
                field: "createTime",
                title: "添加日期",
                sortable : true,
                align: "center",
                formatter:function(value, row, index){
                	return moment(value).format(DATE_FMT)
                }
            }
        ],
        queryParams : function (params) { //查询的参数
        	const defaultParams = $.getTableDefaultQueryParams(params);
        	const formData = $.getFormObject($("#matRemindSearchForm"));
        	return $.extend({}, defaultParams, formData);
        }
    });
    
    // 点击查询按钮
    $("#matRemindListSearchBtn").on("click", function(){
		$.refreshTable(tbl);
	})
	
})