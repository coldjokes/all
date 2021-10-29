/**
 * 通知管理
 */
var NoticeMgr = {
	id: "managerTable",//表格id
	seItem: null,		//选中的条目
	oTable: null,
	layerIndex: -1,
	tbl: null
};

/**
 * 检查是否选中
 */
NoticeMgr.check = function () {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		NoticeMgr.seItem = selected[0];
		return true;
	}
};

/**
 * 查询
 */
NoticeMgr.search = function () {
	tbl.bootstrapTable('refresh');
};

/**
 * 重置
 */
NoticeMgr.resetSearch = function () {
	$("#equSettingName").val("-1");
	$("#noticeType").val("-1")
	NoticeMgr.search();
};

/**
 * 添加
 */
NoticeMgr.openAddMgr = function () {
	var index = layer.open({
		type: 2,
		title: '添加',
		area: ['780px', '600px'], //宽高
		shadeClose: true,
		fix: false, //不固定
		maxmin: true,
		content: '/noticeMgr/addView'
	});
	this.layerIndex = index;
};

/**
 * 删除
 */
NoticeMgr.delMgr = function () {
	if (this.check()) {
		var id = this.seItem.id;
		var operation = function () {
			var ajax = new $ax(Feng.ctxPath + "/noticeMgr/delete/" + id, function (data) {
				if (data.code == 200) {
					Feng.success(data.message);
					tbl.bootstrapTable('refresh');
				} else {
					Feng.info(data.message);
				}
			}, function (data) {
				Feng.error(data.responseJSON.message);
			});
			ajax.start();
		};
		Feng.confirm("是否变更当前状态?", operation);
	}
};

NoticeMgr.list = function () {
	var that = this;
	tbl = $("#managerTable").bootstrapTable({
		url: "/noticeMgr/list",
		cache: false,
		toolbar: '#changeMgr',
		showColumns: true,
		showRefresh: true,
		singleSelect: true,
		clickToSelect: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		columns: [
			{
				field: "equSettingName",
				title: "刀具柜名称",
				align: "center",
			},
			{
				field: "noticeType",
				title: "类型",
				align: "center",
				formatter: function (value, row, index) {
					if (value == 'PRINT') {
						return "打印机";
					} else if (value == 'RECOVERY') {
						return "回收仓";
					} else if (value == 'TEMCABINET') {
						return "暂存柜";
					} else if (value == 'STOCK') {
						return "库存";
					} else {
						return "-";
					}
					return value;
				}
			},
			{
				field: "num",
				title: "可用数量",
				align: "center",
				formatter: function (value, row, index) {
					if (value == 0 || value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "warnValue",
				title: "预警值",
				align: "center",
				formatter: function (value, row, index) {
					if (value == 0 || value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "userName",
				title: "收件人",
				align: "center",
				formatter: function (value, row, index) {
					if (value == 0 || value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				title: '开启状态',
				align: 'center',
				formatter: function (value, row, index) {
					return statusTools(row);
				}
			},
			{
				field: 'id',
				title: '操作',
				align: 'center',
				formatter: function (value, row, index) {
					return [
						`<button class='btn btn-xs btn btn-warning openEditBtn' type='button' data-id=${value}><i class='fa fa-edit'>修改</i></button>`,
						"&nbsp;"
					].join("");
				}
			}
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
				name: $("#equSettingName").val(),
				noticeType: $("#noticeType").val(),
			};
			return temp;
		},
		onLoadSuccess: function (res) {
			$(".openEditBtn").on("click", function (e) {
				var id = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '修改',
					area:['100%','100%'],
					fix: false, // 不固定
					maxmin: false,
					content: '/noticeMgr/editView/' + id
				});
				that.layerIndex = index;
			});
		}
	});
}

$(function () {
	NoticeMgr.list();
});

/* 开启状态显示 */
function statusTools(row) {
	if (row.status == 'OFF') {
		return '<i class=\"fa fa-toggle-off text-navy fa-2x\" onclick="enable(\'' + row.id + '\')"></i> ';
	} else {
		return '<i class=\"fa fa-toggle-on text-navy fa-2x\" onclick="disable(\'' + row.id + '\')"></i> ';
	}
}

/* 用户管理-停用 */
function disable(id) {
	var operation = function () {
		var ajax = new $ax("/noticeMgr/statusSet/" + id + "/1",
			function (data) {
				Feng.success("设置成功!");
				$("#managerTable").bootstrapTable('refresh');
			}, function (data) {
				Feng.error("设置失败!");
			});
		ajax.start();
	};
	Feng.confirm("是否停用当前通知?", operation);
}

/* 用户管理-启用 */
function enable(id) {
	var operation = function () {
		var ajax = new $ax("/noticeMgr/statusSet/" + id + "/0",
			function (data) {
				Feng.success("设置成功!");
				$("#managerTable").bootstrapTable('refresh');
			}, function (data) {
				Feng.error("设置失败!");
			});
		ajax.start();
	};
	Feng.confirm("是否启用当前通知?", operation);
}

