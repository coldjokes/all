/**
 * 下架查询
 */
var LowerFrame = {
	id: "LowerFrameTable",	//表格id
	seItem: null,		//选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};


/**
 * 重置
 */
LowerFrame.resetSearch = function () {
	$("#beginTime").val("");
	$("#endTime").val("");
	$("#name").val("-1");
	LowerFrame.search();
}

/**
 * 导出
 */
LowerFrame.exportList = function () {
	var beginTime = $("#beginTime").val();
	if (beginTime == '') {
		beginTime = "1900-01-01";
	}
	var endTime = $("#endTime").val();
	if (endTime == '') {
		endTime = "9999-12-31";
	}
	var name = $("#name").val();
	if (name == '') {
		name = "-1";
	}
	window.location.href = Feng.ctxPath + "/lowerFrameQuery/export/" + beginTime + "/" + endTime + "/" + name;
};

/**
 * 查询日志列表
 */
LowerFrame.search = function () {
	if (Feng.compareDate("beginTime", "endTime")) {
		return;
	}
	tbl.bootstrapTable('refresh');
};

LowerFrame.list = function () {
	var that = this;
	tbl = $("#LowerFrameTable").bootstrapTable({
		url: "/lowerFrameQuery/list",
		cache: false,
		showColumns: true,
		showRefresh: true,
		clickToSelect: true,
		pagination: true,
		toolbar: '#btnExport',
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		columns: [
			{
				field: "equName",
				title: "刀具柜名称",
				align: "center"
			},
			{
				field: "position",
				title: "下架货位",
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
				field: "lowFrameNum",
				title: "下架数量",
				align: "center"
			},
			{
				field: "unit",
				title: "单位",
				align: "center"
			},
			{
				field: "ownerName",
				title: "使用人员",
				align: "center",
				formatter: function (value, row, index) {
					if (value == "") {
						return "-";
					}
					return value;
				}
			},
			{
				field: "userName",
				title: "操作人员",
				align: "center"
			},
			{
				field: "opDate",
				title: "操作时间",
				align: "center"
			},
		],
		responseHandler: function (res) {
			return {
				"total": res.totalElements,
				"rows": res.content
			};
		},
		queryParams: function (params) {
			var temp = {
				limit: params.limit,
				offset: params.offset,
				pageSize: this.pageSize,
				pageNumber: this.pageNumber,
				beginTime: $("#beginTime").val(),
				endTime: $("#endTime").val(),
				name: $("#name").val()
			};
			return temp;
		},
	});
}

$(function () {
	LowerFrame.list();
});

