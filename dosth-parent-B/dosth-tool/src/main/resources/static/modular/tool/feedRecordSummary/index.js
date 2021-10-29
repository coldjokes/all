/**
 * 主柜领用查询
 */
var FeedRecordSummary = {
	layerIndex : -1,
	oTable : null,
	params: {},
	seItem : null,
};

/**
 * 重置
 */
FeedRecordSummary.resetSearch = function() {
	$("#beginTime").val("");
	$("#endTime").val("");
	$("#matInfo").val("");
	FeedRecordSummary.search();
}

/**
 * 查询列表详细
 */
FeedRecordSummary.search = function () {
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
	
	FeedRecordSummary.params = params;
	FeedRecordSummary.oTable.bootstrapTable('selectPage', 1);
	FeedRecordSummary.oTable.bootstrapTable('refresh');
};

/**
 * @description 刷新
 */
FeedRecordSummary.getList = function() {
	FeedRecordSummary.oTable = $("#managerTable").bootstrapTable({
		url : "/feedRecordSummary/list",
		cache: false,
		showColumns: true,
	    showRefresh: true,
		pagination: true,
		toolbar:btnExport,
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
				field : "unit",
				title : "补料单位",
				align : "center"
			},
			{
				field : "feedNum",
				title : "补料数量",
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
            const feedParams = FeedRecordSummary.params;
            return $.extend({}, defaultParams, feedParams);
        },
	});
};

/**
 * 导出
 */
FeedRecordSummary.exportRecordList = function() {
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
	window.location.href = Feng.ctxPath + "/feedRecordSummary/export/" + params;
};

$(function() {
	FeedRecordSummary.getList();
});