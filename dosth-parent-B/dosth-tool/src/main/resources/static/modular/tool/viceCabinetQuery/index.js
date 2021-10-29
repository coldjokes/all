/**
 * 副柜领用查询
 */
var ViceCabinetQuery = {
	id: "ViceCabinetQueryTable",	//表格id
	seItem: null,		//选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};


/**
 * 重置
 */
ViceCabinetQuery.resetSearch = function() {
	$("#beginTime").val("");
	$("#endTime").val("");
	$("#info").val("");
	$("#subBoxName").val("-1");
	$("#inOrOut").val("-1");
	ViceCabinetQuery.search();
}

/**
 * 查询日志列表
 */
ViceCabinetQuery.search = function() {
	if (Feng.compareDate("beginTime", "endTime")) {
		return;
	}
	tbl.bootstrapTable('refresh');
};

/**
 * 暂存柜领用记录导出    
 */
ViceCabinetQuery.infoExport = function() {
	var params = [$("#beginTime").val(), $("#endTime").val(), $("#info").val(), $("#subBoxName").val(), $("#inOrOut").val()];
	window.location.href = Feng.ctxPath + "/viceCabinetQuery/infoExport/" + params;

};
ViceCabinetQuery.list = function() {
	var that = this;
	tbl = $("#ViceCabinetQueryTable").bootstrapTable({
		url: "/viceCabinetQuery/list",
		cache: false,
		showColumns: true,
		showRefresh: true,
		clickToSelect: true,
		pagination: true,
		toolbar: '#infoExport',
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		columns: [
			{
				field: "subBoxName",
				title: "暂存柜名称",
				align: "center"
			},
			{
				field: "position",
				title: "货位",
				align: "center"
			},
			{
				field: "matInfoName",
				title: "物料名称",
				align: "center"
			},
			{
				field: "barCode",
				title: "物料编号",
				align: "center"
			},
			{
				field: "spec",
				title: "物料型号",
				align: "center"
			},
			{
				field: "num",
				title: "数量",
				align: "center"
			},
			{
				field: "borrowType",
				title: "单位",
				align: "center",
			},
			{
				field: "price",
				title: "单价（元）",
				align: "center"
			},
			{
				field: "tmpMoney",
				title: "金额（元）",
				align: "center"
			},
			{
				field: "userName",
				title: "领用人员",
				align: "center"
			},
			{
				field: "inOrOut",
				title: "暂存/取出",
				align: "center",
				formatter: function(value, index, row) {
					var a = "";
					if (value == "YES") {
						var a = '<span style="color:#1ab394;">' + '暂存' + '</span>';
					} else if(value == "NO"){
						var a = '<span style="color:#FF0000;">' + '取出' + '</span>';
					}
					return a;
				}
			},
			{
				field: "opDate",
				title: "操作时间",
				align: "center"
			},
		],
		responseHandler: function(res) {
			return {
				"total": res.totalElements,
				"rows": res.content
			};
		},
		queryParams: function(params) {
			var temp = {
				limit: params.limit,
				offset: params.offset,
				pageSize: this.pageSize,
				pageNumber: this.pageNumber,
				beginTime: $("#beginTime").val(),
				endTime: $("#endTime").val(),
				info: $("#info").val(),
				subBoxName: $("#subBoxName").val(),
				inOrOut: $("#inOrOut").val()
			};
			return temp;
		},
	})
}


$(function() {
	ViceCabinetQuery.list();
});

