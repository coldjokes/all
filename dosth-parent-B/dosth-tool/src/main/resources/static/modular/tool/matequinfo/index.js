/**
 * 工具管理--物料/设备信息
 */
var MgrMatEquInfo = {
	id: "managerTable",//表格id
	seItem: null,		//选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};

/**
 * 检查是否选中
 */
MgrMatEquInfo.check = function() {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		MgrMatEquInfo.seItem = selected[0];
		return true;
	}
};

/**
 * 查询
 */
MgrMatEquInfo.search = function() {
	tbl.bootstrapTable('refresh');
};

/**
 * 重置
 */
MgrMatEquInfo.resetSearch = function() {
	$("#name").val("");
	$("#status").val("-1");
	MgrMatEquInfo.search();
};

/**
 * 导入
 */
MgrMatEquInfo.openUpload = function() {
	var index = layer.open({
		type: 2,
		title: '导入',
		fix: false, //不固定
		maxmin: false,
		area:['100%','100%'],
		content: '/importExcel/uploadView'
	});
	this.layerIndex = index;
};

/**
 * 导出
 */
MgrMatEquInfo.openExport = function() {
	var name = $("#name").val();
	if(name == ""){
		name = "-1";
	}
	var status = $("#status").val();
	if(status == ""){
		status = "-1";
	}
	window.location.href = Feng.ctxPath + "/matequinfo/exportExcel/" + name + "/" + status;
};

/**
 * 新增
 */
MgrMatEquInfo.openAddMgr = function() {
	var index = layer.open({
		type: 2,
		title: '添加物料/设备信息',
		fix: false, //不固定
		maxmin: false,
		area:['100%','100%'],
		content: '/matequinfo/addView'
	});
	this.layerIndex = index;
};

MgrMatEquInfo.list = function() {
	var that = this;
	tbl = $("#managerTable").bootstrapTable({
		url: "/matequinfo/list",
		toolbar: "#managerTableToolbar1",
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
				field: "barCode",
				title: "物料编号",
				align: "center"
			},
			{
				field: "matEquName",
				title: "物料名称",
				align: "center"
			},
			{
				field: "spec",
				title: "物料型号",
				align: "center"
			},
			{
				field: "manufacturerName",
				title: "供应商",
				align: "center",
			},
			{
				field: "brand",
				title: "品牌",
				align: "center",
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				},
			},
			{
				field: "num",
				title: "包装数量",
				align: "center"
			},
			{
				field: "packUnit",
				title: "包装单位",
				align: "center"
			},
			{
				field: "storePrice",
				title: "库存成本",
				align: "center"
			},
			{
				field: "lowerStockNum",
				title: "最低库存",
				align: "center"
			},
			{
				field: "useLife",
				title: "使用寿命",
				align: "center"
			},
			{
				field: "borrowType",
				title: "借出类型",
				align: "center",
				formatter: function(value, row, index) {
					return borrowTypeTools(row);
				}
			},
			{
				field: "status",
				title: "状态",
				align: "center",
				formatter: function(value, row, index) {
					return statusTools(row);
				}
			},
			{
				field: "id",
				title: "操作",
				align: "center",
				formatter: function(value, row, index) {
					return [
						`<button class='btn btn-xs btn btn-warning openEditBtn' type='button' data-id=${value}><i class='fa fa-edit'>修改</i></button>`,
						"&nbsp;"
					].join("");
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
				name: $("#name").val(),
				status: $("#status").val(),
			};
			return temp;
		},
		onLoadSuccess: function(res) {
			$(".openEditBtn").on("click", function(e) {
				var infoId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '修改物料/设备信息',
					fix: false,
					maxmin: false,
					area:['100%','100%'],
					content: '/matequinfo/editView/' + infoId
				});
				that.layerIndex = index;
			});
		},
	});
}

$(function() {
	MgrMatEquInfo.list();
});

/*显示借出类型 */
function borrowTypeTools(row) {
	if (row.borrowType == 'PACK') {
		return '盒';
	}else {
		return '支';
	}
}

/* 开启状态显示 */
function statusTools(row) {
	if (row.status == 'ENABLE') {
		return '<i class=\"fa fa-toggle-on  text-navy fa-2x\" onclick="disable(\'' + row.id + '\',\'' + row.status + '\')"></i> ';
	}else {
		return '<i class=\"fa fa-toggle-off text-navy fa-2x\" onclick="disable(\'' + row.id + '\',\'' + row.status + '\')"></i> ';
	}
}

/* 物料管理-启用停用 */
function disable(infoId,status) {
	var operation = function() {
		var ajax = new $ax("/matequinfo/update/" + infoId,
			function(data) {
				if (data.code == 200) {
					Feng.success("设置成功!");
					$("#managerTable").bootstrapTable('refresh');
				} else {
					Feng.error(data.message);
				}
			}, function(data) {
				Feng.error("设置失败!");
			});
		ajax.start();
	};
	status=='ENABLE'?Feng.confirm("是否停用当前物料?", operation):Feng.confirm("是否启用当前物料?", operation);
}