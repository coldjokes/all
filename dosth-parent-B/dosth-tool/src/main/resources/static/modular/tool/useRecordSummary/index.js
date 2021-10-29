/**
 * 主柜领用查询
 */
var UseRecordSummary = {
	layerIndex : -1,
	oTable : null,
	params: {},
	seItem : null,
};

/**
 * 重置
 */
UseRecordSummary.resetSearch = function() {
	$("#beginTime").val("");
	$("#endTime").val("");
	$("#matInfo").val("");
	UseRecordSummary.search();
}

/**
 * 查询列表详细
 */
UseRecordSummary.search = function () {
	if (Feng.compareDate("beginTime", "endTime")) {
		return;
	}
	var beginTime = $("#beginTime").val();
	if (beginTime == '') {
		beginTime = "1900-01-01";
	}
	var endTime = $("#endTime").val();
	if (endTime == '') {
		endTime = "9999-12-31";
	}
	
	var matInfo = $("#matInfo").val();
	
	var params = {
		beginTime : beginTime,
		endTime : endTime,
		matInfo : matInfo
	};
	
	UseRecordSummary.params = params;
	UseRecordSummary.oTable.bootstrapTable('selectPage', 1);
	UseRecordSummary.oTable.bootstrapTable('refresh');
	//UseRecordSummary.getList();
};

/**
 * @description 刷新
 */
UseRecordSummary.getList = function() {
//	console.log(1);
	UseRecordSummary.oTable = $("#managerTable").bootstrapTable({
		url : "/useRecordSummary/list",
		cache: false,
		showColumns: true,
		toolbar:'#btnExport',
	    showRefresh: true,
	    pagination: true,
	  	pageNumber: 1,
	  	pageSize: 10, 
	  	sidePagination: "server",
		columns : [
			{
				field : "barCode",
				title : "物料编号",
				align : "center"
			},
			{
				field : "matInfoName",
				title : "物料名称",
				align : "center"
			},
			{
				field : "spec",
				title : "物料型号",
				align : "center"
			},
			{
				field : "supplierName",
				title : "供应商",
				align : "center"
			},
			{
				field : "brand",
				title : "品牌",
				align : "center"
			},
			{
				field : "borrowUnit",
				title : "领取单位",
				align : "center"
			},
			{
				field : "borrowNum",
				title : "领取数量",
				align : "center"
			},
			{
				field : "price",
				title : "单价（元）",
				align : "center"
			},
			{
				field : "money",
				title : "金额（元）",
				align : "center"
			}
		],
		responseHandler : function(res) {
			return {
				  "total": res.totalElements,
				  "rows": res.content
			  };
		},
		queryParams : function (params) { //查询的参数
			const defaultParams = {  
					offset: params.offset, //页码
					limit: params.limit, //每页显示个数
	                sortField: params.sort,    //排序列名  
	                sortOrder: params.order, //正序、反序
	        };
			//页面参数
            const userParams = UseRecordSummary.params;
            return $.extend({}, defaultParams, userParams);
        },
	});
};

/**
 * 导出
 */
UseRecordSummary.exportRecordList = function() {
	var beginTime = $("#beginTime").val();
	if (beginTime == '') {
		beginTime = "1900-01-01";
	}
	var endTime = $("#endTime").val();
	if (endTime == '') {
		endTime = "9999-12-31";
	}
	var matInfo = $("#matInfo").val();
	var params = [beginTime, endTime, matInfo];
	window.location.href = Feng.ctxPath + "/useRecordSummary/export/" + params;
};

$(function() {
	UseRecordSummary.getList();
});