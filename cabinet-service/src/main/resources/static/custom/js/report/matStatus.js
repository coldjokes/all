$(function(){
	
	//物料状态
	var matStockSearchForm = $("#matStockSearchForm");
	$(".matStatus").append($.customOptions("materialStatus", true));
	
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
	
	var materialCountStatus = SiteConfig.setting.materialCount == 'ON'
		
	var tbl =  $("#matStatusTbl").bootstrapTable({
      url:	Api.url("matStock"),
      striped: true,
      pagination: true,
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
		    	field: "totalAmount",
		    	title: "库存",
                align: "center",
                formatter: function(value, row, index){
                	if(materialCountStatus){
                		return value;
                	} else {
                		if(value > 0){
                			return "在库";
                		} else {
                			return "不在库";
                		}
                	}
                }
		    }, {
                field: "stockDetailList",
                title: "存储位置",
                width:"15%",
                align: "center",
                formatter:function(value, row, index){
                	let totalAmount = row.totalAmount;
                	if(totalAmount && totalAmount > 0){
                		
                		let content = "";
                		for(let i = 0; i < value.length; i ++){
                			let stockDetail = value[i];
                			content = content + stockDetail.cabinetName + ":" + stockDetail.cellName + ":" + stockDetail.amount;
                			if(i < (value.length-1)){
                				content += "<br/>";
                			}
                		}
                		return content
                	}
                }
            },
        ],
        queryParams : function (params){
        	const defaultParams = $.getTableDefaultQueryParams(params);
        	const formData = $.getFormObject(matStockSearchForm);
        	return $.extend({}, defaultParams, formData);
        },
        onLoadSuccess: function () {
        	   
        },
    });
	
	// 点击查询按钮
	$("#matStockSearchBtn").on("click", function(){
		$.refreshTable(tbl);
	})
	
	// 点击导出按钮
	$("#matStockExportBtn").on("click", function(){
		location.href = Api.url("matStockExport") + "?" + matStockSearchForm.serialize();
	})
})
  
