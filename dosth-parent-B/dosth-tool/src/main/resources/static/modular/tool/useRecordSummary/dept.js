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
	
	
	var params = {
		beginTime : beginTime,
		endTime : endTime
	};
	
	UseRecordSummary.params = params;
	UseRecordSummary.oTable.bootstrapTable('selectPage', 1);
	UseRecordSummary.oTable.bootstrapTable('refresh');
};

/**
 * @description 刷新
 */
UseRecordSummary.getList = function() {
	UseRecordSummary.oTable = $("#managerTable").bootstrapTable({
		url : "/useRecordSummary/listDept",
		cache: false,
		showColumns: true,
	    showRefresh: true,
		pagination: true,
		toolbar:'#btnExport',
	  	pageNumber: 1,
	  	pageSize: 10, 
	  	sidePagination: "server",
		columns : [
			{
				field : "categoryName",
				title : "领取部门",
				align : "center"
			},
			{
				field : "borrowNumSummary",
				title : "领取数量",
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
	var params = [beginTime, endTime];
	window.location.href = Feng.ctxPath + "/useRecordSummary/exportDept/" + params;
};

$(function() {
	UseRecordSummary.getList();
});