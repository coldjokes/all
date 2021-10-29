/**
 * 盘点查询
 */
var Inventory = {
	id: "InventoryTable",	//表格id
	seItem: null,		//选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};

/**
 * 重置
 */
Inventory.resetSearch = function() {
	$("#beginTime").val("");
	$("#endTime").val("");
	$("#matInfo").val("");
	$("#equName").val("-1");
	Inventory.search();
}

/**
 * 查询列表
 */
Inventory.search = function() {
	if (Feng.compareDate("beginTime", "endTime")) {
		return;
	}
	tbl.bootstrapTable('refresh');
};

/**
 * 导出
 */
Inventory.exportList = function() {
	var beginTime = $("#beginTime").val();
	if (beginTime == '') {
		beginTime = "1900-01-01";
	}
	var endTime = $("#endTime").val();
	if (endTime == '') {
		endTime = "9999-12-31";
	}
	window.location.href = Feng.ctxPath + "/inventory/export/" + beginTime + "/" + endTime;
};

Inventory.list = function() {
	tbl = $("#InventoryTable").bootstrapTable({
		url: "/inventory/list",
		toolbar: "#btnExport",//设置自定义工具栏容器ID，也可以是容器样式类名.toolbar
		cache: false,
		showColumns: true,
		showRefresh: true,
		clickToSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		columns: [
			{
				field: "equName",
				title: "主柜信息",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "equSettingName",
				title: "暂存柜信息",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "matEquName",
				title: "物料名称",
				align: "center"
			},
			{
				field: "storageNum",
				title: "库存数量",
				align: "center"
			},
			{
				field: "inventoryNum",
				title: "实盘数量",
				align: "center"
			},
			{
				field: "inventoryStatus",
				title: "盘点状态",
				align: "center",
				formatter: function(value, index, row) {
					var a = "";
					if (value == "PING") {
						var a = '<span style="color:#f8ac59;">' + "正常" + '</span>';
					}
					else if (value == "SURPLUS") {
						var a = '<span style="color:#1ab394;">' + '盘盈' + '</span>';
					} else if (value == "LOSS") {
						var a = '<span style="color:#FF0000;">' + '盘亏' + '</span>';
					}
					return a;
				}
			},
			{
				field: "ownerName",
				title: "使用人员",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
						return '-';
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
				matInfo: $("#matInfo").val(),
				equName: $("#equName").val(),
			};
			return temp;
		},
	});
}

$(function() {
	Inventory.list();
});