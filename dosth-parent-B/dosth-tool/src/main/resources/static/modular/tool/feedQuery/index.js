/**
 * 补料查询
 */
var FeedQuery = {
	id: "FeedQueryTable", // 表格id
	seItem: null, // 选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};

/**
 * 检查是否选中
 */
FeedQuery.check = function() {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		FeedQuery.seItem = selected[0];
		return true;
	}
};

/**
 * 重置
 */
FeedQuery.resetSearch = function() {
	$("#beginTime").val("");
	$("#endTime").val("");
	$("#matInfo").val("");
	$("#feedingName").val("");
	$("#cabinetId").val("-1");
	FeedQuery.search();
}

/**
 * 查询日志列表
 */
FeedQuery.search = function() {
	if (Feng.compareDate("beginTime", "endTime")) {
		return;
	}
	tbl.bootstrapTable('refresh');
};

/**
 * 查看明细
 */
FeedQuery.openDetail = function() {
	if (this.check()) {
		var index = layer.open({
			type: 2,
			title: '补料清单明细',
			area: ['800px', '600px'], // 宽高
			fix: false, // 不固定
			maxmin: true,
			content: '/feedQuery/feedDetailView/' + this.seItem.id
		});
		this.layerIndex = index;
	}
};

/**
 * 导出清单
 */
FeedQuery.exportDetail = function() {
	var beginTime = $("#beginTime").val();
	if (beginTime == '') {
		beginTime = "1900-01-01";
	}
	var endTime = $("#endTime").val();
	if (endTime == '') {
		endTime = "9999-12-31";
	}
	var matInfo = $("#matInfo").val();
	if (matInfo == '') {
		matInfo = "-1";
	}
	var feedingName = $("#feedingName").val();
	if (feedingName == '') {
		feedingName = "-1";
	}
	window.location.href = Feng.ctxPath + "/feedQuery/exportFeedingDetail/"
		+ beginTime + "/" + endTime + "/" + matInfo + "/" + feedingName;
};

FeedQuery.list = function() {
	var that = this;
	tbl = $("#FeedQueryTable").bootstrapTable({
		url: "/feedQuery/list",
		toolbar: "#exportDetail",//设置自定义工具栏容器ID，也可以是容器样式类名.toolbar
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
				field: "cabinetName",
				title: "刀具柜名称",
				align: "center"
			},
			{
				field: "feedingName",
				title: "补料清单编号",
				align: "center"
			},
			{
				field: "feedType",
				title: "补料类型",
				align: "center"
			},
			{
				field: "position",
				title: "补料货位",
				align: "center"
			},
			{
				field: "matName",
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
				field: "feedingNum",
				title: "补充数量",
				align: "center"
			},
			{
				field: "feedingAccount",
				title: "补料人员",
				align: "center",
				formatter: function(value) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "feedingTime",
				title: "补料时间",
				align: "center",
				formatter: function(value) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "isFinish",
				title: "是否完成",
				align: "center",
				formatter: function(value, row, index) {
					var a = "";
					if (value == "否") {
						var a = '<span style="color:#FF0000;">' + value + '</span>';
					} else if (value == "是") {
						var a = '<span style="color:#1ab394">' + value + '</span>';
					}
					return a;
				},
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
				feedingName: $("#feedingName").val(),
				cabinetId: $("#cabinetId").val(),
			};
			return temp;
		},
	});
}

$(function() {
	FeedQuery.list();
});