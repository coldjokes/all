/**
 * 暂存柜明细查询
 */
var SubCabinetDetail = {
	equInfoInstance: null,
	id: "managerTable",// 表格id
	layerIndex: -1,
	oTable: null,
	seItem: null,
	treeNode: null,// 选中的节点
	tbl: null,
};

/**
 * 重置
 */
SubCabinetDetail.resetSearch = function() {
	$("#matInfo").val("");
	$("#rowNo").val("");
	$("#colNo").val("");
	SubCabinetDetail.search();
}

/**
 * 刷新详情信息
 */
SubCabinetDetail.search = function() {
	var cabinetId = $("#cabinetId").val();
	var params = {
		cabinetId: cabinetId
	};
	tbl.bootstrapTable('refresh');
};

/**
 * 暂存柜详情导出
 */
SubCabinetDetail.subcabinetDetailExport = function() {
	var cabinetId = $("#cabinetId").val();
	if (cabinetId != null && cabinetId != '') {
		window.location.href = Feng.ctxPath + "/stock/export/" + cabinetId;
	} else {
		return Feng.info("请选择刀具柜");
	}
};

/**
 * 点击储存设备树
 */
SubCabinetDetail.onClickEquInfo = function(e, treeId, treeNode) {
	SubCabinetDetail.treeNode = treeNode;
	$("#cabinetId").val(treeNode.id);
	var cabinetId = treeNode.id;
	var url = Feng.ctxPath + "/stock/init/" + cabinetId;
	var ajax = new $ax(url,
		function(data) {
			$("#rightContent").html(data);
		}, function(data) {
			Feng.error("设置失败," + data + "!");
		}, "html");
	ajax.start();
};

/**
 * 点击树形节点前判断
 */
SubCabinetDetail.onBeforeClick = function(treeId, treeNode, clickFlag) {
	if (treeNode.id.startWith("C_")) {
		return false;
	}
	return true;
};
SubCabinetDetail.list = function() {
	var that = this;
	tbl = $("#managerTable").bootstrapTable({
		url: "/stock/listMain",
		toolbar: "#exportExcel",//设置自定义工具栏容器ID，也可以是容器样式类名.toolbar
		cache: false,
		showColumns: true,
		showRefresh: true,
		clickToSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server", columns: [
			{
				field: "rowNo",
				title: "行号",
				align: "center"
			},
			{
				field: "colNo",
				title: "列号",
				align: "center"
			},
			{
				field: "matInfoName",
				title: "物料名称",
				align: "center",
				formatter: function(value, index, row) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "barCode",
				title: "物料编号",
				align: "center",
				formatter: function(value, index, row) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "spec",
				title: "物料型号",
				align: "center",
				formatter: function(value, index, row) {
					if (value == "") {
						return '-';
					}
					return value;
				}
			},
			{
				field: "curNum",
				title: "当前数量",
				align: "center"
			},
			{
				field: "packUnit",
				title: "包装单位",
				align: "center",
				formatter:function(value){
					if(value==""){
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
				field: "sumPrice",
				title: "金额",
				align: "center"
			},
			{
				field: "maxReserve",
				title: "最大存储",
				align: "center"
			},
			{
				field: "warnVal",
				title: "警告阀值",
				align: "center"
			},
			{
				field: "lastFeedTime",
				title: "最后上架时间",
				align: "center"
			},
			{
				field: "equSta",
				title: "状态",
				align: "center",
				formatter: function(value, index, row) {
					var a = "";
					if (value == "NONE") {
						var a = '<span style="color:#1ab394;">' + '正常' + '</span>';
					} else if (value == "ENABLE") {
						var a = '<span style="color:#FF0000;">' + '故障' + '</span>';
					}
					return a;
				}
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
				matInfo: $("#matInfo").val(),
				rowNo: $("#rowNo").val(),
				colNo: $("#colNo").val(),
			};
			return temp;
		},
	})
}

$(function() {
	SubCabinetDetail.list();
	
	var equInfoTree = new $ZTree("equInfoTree", "/tree/createCabinetTree");
	equInfoTree.bindBeforeClick(SubCabinetDetail.onBeforeClick);
	equInfoTree.bindOnClick(SubCabinetDetail.onClickEquInfo);
	equInfoTree.init();
	SubCabinetDetail.equInfoInstance = equInfoTree;
});