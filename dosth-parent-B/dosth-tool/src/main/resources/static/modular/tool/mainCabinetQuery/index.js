/**
 * 主柜领用查询
 */
var MainCabinetQuery = {
	id: "MainCabinetQueryTable", // 表格id
	seItem: null, // 选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};

/**
 * 重置
 */
MainCabinetQuery.resetSearch = function() {
	$("#beginTime").val("");
	$("#endTime").val("");
	$("#receiveType").val("-1");
	$("#isReturnBack").val("-1");
	$("#equSettingName").val("-1");
	$("#searchCondition").val("");
	MainCabinetQuery.search();
}

/**
 * 查询日志列表
 */
MainCabinetQuery.search = function() {
	if (Feng.compareDate("beginTime", "endTime")) {
		return;
	}
	var params = {
		beginTime: $("#beginTime").val(),
		endTime: $("#endTime").val(),
		receiveType: $("#receiveType").val(),
		isReturnBack: $("#isReturnBack").val(),
		equSettingName: $("#equSettingName").val(),
		searchCondition: $("#searchCondition").val()
	};
	tbl.bootstrapTable('refresh');
};

/**
 * 导出
 */
MainCabinetQuery.exportBorrowList = function() {
	var beginTime = $("#beginTime").val();
	if (beginTime == '') {
		beginTime = "1900-01-01";
	}
	var endTime = $("#endTime").val();
	if (endTime == '') {
		endTime = "9999-12-31";
	}

	var receiveType = $("#receiveType").val();
	if (receiveType == '') {
		receiveType = "-1";
	}

	var isReturnBack = $("#isReturnBack").val();
	if (isReturnBack == '') {
		isReturnBack = "-1";
	}

	var equSettingName = $("#equSettingName").val();
	if (equSettingName == '') {
		equSettingName = "-1";
	}

	var searchCondition = $("#searchCondition").val();
	if (searchCondition == '') {
		searchCondition = "-1";
	}

	window.location.href = Feng.ctxPath + "/mainCabinetQuery/export/" + receiveType + "/"
		+ beginTime + "/" + endTime + "/" + isReturnBack + "/" + equSettingName + "/" + searchCondition;
};

MainCabinetQuery.list = function() {
	var that = this;
	tbl = $("#MainCabinetQueryTable").bootstrapTable({
		url: "/mainCabinetQuery/list",
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
				field: "brand",
				title: "品牌（供应商）",
				align: "center"
			},
			{
				field: "borrowOrigin",
				title: "领取来源",
				align: "center"
			},
			{
				field: "borrowNum",
				title: "领取数量",
				align: "center"
			},
			{
				field: "realNum",
				title: "实领数量",
				align: "center"
			},
			{
				field: "borrowType",
				title: "领取单位",
				align: "center"
			},
			{
				field: "packNum",
				title: "包装数量",
				align: "center"
			},
			{
				field: "packUnit",
				title: "包装单位",
				align: "center"
			},
			{
				field: "receiveType",
				title: "类型",
				align: "center",
				formatter: function(value, index, row) {
					if (value == "GRID") {
						return "全部";
					} else if (value == "MATTYPE") {
						return "类型";
					} else if (value == "REQREF") {
						return "设备";
					} else if (value == "PROCREF") {
						return "工序";
					} else if (value == "PARTS") {
						return "零件";
					} else if (value == "CUSTOM") {
						return "自定义";
					} else if (value == "GETNEWONE") {
						return "以旧换新";
					} else if (value == "APP") {
						return "APP预约";
					} else if (value == "APPLYVOUCHER") {
						return "申请单";
					}
					return value;
				}
			},
			{
				field: "receiveInfo1",
				title: "领用类型1",
				align: "center"
			},
			{
				field: "receiveInfo2",
				title: "领用类型2",
				align: "center",
				formatter: function(value, index, row) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "receiveInfo3",
				title: "领用类型3",
				align: "center",
				formatter: function(value, index, row) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "price",
				title: "单价",
				align: "center"
			},
			{
				field: "money",
				title: "金额",
				align: "center"
			},
			{
				field: "useLife",
				title: "使用寿命",
				align: "center"
			},
			{
				field: "deptName",
				title: "部门",
				align: "center"
			},
			{
				field: "userName",
				title: "领用人员",
				align: "center"
			},
			{
				field: "returnBackNum",
				title: "已归还数量",
				align: "center"
			},
			{
				field: "isReturnBack",
				title: "是否归还",
				align: "center",
				formatter: function(value, row, index) {
					var a = "";
					if (value == "未归还") {
						var a = '<span style="color:#FF0000;">' + value + '</span>';
					} else if (value == "已归还") {
						var a = '<span style="color:#1ab394">' + value + '</span>';
					}
					return a;
				},
			},
			{
				field: "realLife",
				title: "制造产量",
				align: "center"
			},
			{
				field: "opDate",
				title: "领用时间",
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
				receiveType: $("#receiveType").val(),
				isReturnBack: $("#isReturnBack").val(),
				equSettingName: $("#equSettingName").val(),
				searchCondition: $("#searchCondition").val()
			};
			return temp;
		},
	});
}

$(function() {
	MainCabinetQuery.list();
});