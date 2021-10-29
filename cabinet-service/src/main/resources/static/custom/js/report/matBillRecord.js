$(function(){
	$("#matBillSearchForm [name='startTime']").val(startTime.valueOf()); 
	$("#matBillSearchForm [name='endTime']").val(endTime.valueOf()); 
	var matBillSearchForm = $("#matBillSearchForm");
	
	$(".operateType").append($.customOptions("operateType", true));
	
	//渲染设备列表
	$.getJSON(Api.url("cabinet"), {}, function(response){
		if(response.code == 200){
			var computerListHtml = "";
			for(let x of response.results){
				computerListHtml += `<option value=${x.id}>${x.name}</option>`;
			}
			$(".cabinetList").append(computerListHtml);
		}
	})
	
	var columns;
	if(SiteConfig.setting.materialCount == 'ON'){
		columns =  [
	   		 	{
	                field: "seq",
	                title: "序号",
	                width:"5%",
	                align: "center",
	                formatter: function(value, row, index){
	                	return $.getTableRowNum(tbl, index);
	                }
	            }, {
	                field: "materialName",
	                title: "物料编码",
	                align: "center"
	            }, {
	                field: "materialNo",
	                title: "刀片型号",
	                align: "center"
			    }, {
			    	field: "materialSpec",
			    	title: "图纸编号",
			    	align: "center"
			    }, {
			    	field: "userFullname",
			    	title: "操作人",
			    	align: "center"
			    }, {
			    	field: "operateName",
			    	title: "操作类型",
			    	align: "center"
			    }, {
			    	field: "amountStart",
			    	title: "操作前数量",
			    	width:"5%",
			    	align: "center"
			    }, {
			    	field: "amountDiff",
			    	title: "操作数量",
			    	width:"5%",
			    	align: "center",
			    	formatter:function(value, row, index){
			    		return Math.abs(value);
	               }
			    }, {
			    	field: "amountEnd",
			    	title: "操作后数量",
			    	width:"5%",
	               align: "center"
			    }, {
			    	field: "cabinetName",
			    	title: "设备名",
	               align: "center"
			    }, {
			    	field: "cellName",
			    	title: "方格名",
			    	align: "center"
			    }, {
			    	field: "createTime",
			    	title: "操作时间",
			    	align: "center",
	   				formatter:function(value, row, index){
		   				if(value){
		   					return moment(value).format("YYYY-MM-DD HH:mm:ss")
		   				}
	   				}
			    }
			]
	} else {
		columns =  [
   		 	{
                field: "seq",
                title: "序号",
                width:"5%",
                align: "center",
                formatter: function(value, row, index){
                	return $.getTableRowNum(tbl, index);
                }
            }, {
                field: "materialName",
                title: "物料编码",
                align: "center"
            }, {
                field: "materialNo",
                title: "刀片型号",
                align: "center"
		    }, {
		    	field: "materialSpec",
		    	title: "图纸编号",
		    	align: "center"
		    }, {
		    	field: "userFullname",
		    	title: "操作人",
		    	align: "center"
		    }, {
		    	field: "operateName",
		    	title: "操作类型",
		    	align: "center"
		    }, {
		    	field: "cabinetName",
		    	title: "设备名",
               align: "center"
		    }, {
		    	field: "cellName",
		    	title: "方格名",
		    	align: "center"
		    }, {
		    	field: "createTime",
		    	title: "操作时间",
		    	align: "center",
   				formatter:function(value, row, index){
	   				if(value){
	   					return moment(value).format("YYYY-MM-DD HH:mm:ss")
	   				}
   				}
		    }
		]
	}
	
    var tbl = $("#matBillRecordTbl").bootstrapTable({
    	url: Api.url("matBill"),
    	columns: columns,
		queryParams : function (params){
        	const defaultParams = $.getTableDefaultQueryParams(params);
        	const formData = $.getFormObject(matBillSearchForm);
        	return $.extend({}, defaultParams, formData);
        }
    });
    
    // 初始化日期选择插件
	$(".daterangePicker").daterangepicker({
		
	}).
	on('cancel.daterangepicker', function(ev, picker) {
		$('.daterangePicker').val("");
		$("#matBillSearchForm [type='hidden']").val(""); 
		
	}).
	on("apply.daterangepicker", function(ev, picker) { 
		$("#matBillSearchForm [name='startTime']").val(picker.startDate.valueOf()); 
		$("#matBillSearchForm [name='endTime']").val(picker.endDate.valueOf()); 
	});

    // 点击查询按钮
	$("#matBillSearchBtn").on("click", function(){
		$.refreshTable(tbl);
	})
	
	// 点击导出按钮
	$("#matBillExportBtn").on("click", function(){
		location.href = Api.url("matBillExport") + "?" + matBillSearchForm.serialize();
	})
    
 
})
  
