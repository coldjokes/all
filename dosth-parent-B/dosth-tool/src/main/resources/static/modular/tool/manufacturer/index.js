/**
 * 供应商管理
 */
var MgrManufacturer = {
	id: "managerTable",
	seItem: null,
	oTable: null,
	layerIndex: -1,
	tbl: null,
	tb2: null
};

/**
 * 检查是否选中
 */
MgrManufacturer.check = function() {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		MgrManufacturer.seItem = selected[0];
		return true;
	}
};

/**
 * 查询
 */
MgrManufacturer.search = function() {
	tb1.bootstrapTable('refresh');
};

/**
 * 重置
 */
MgrManufacturer.resetSearch = function() {
	$("#name").val("");
	$("#status").val("-1");
	MgrManufacturer.search();
};

/**
 * 新增供应商
 */
MgrManufacturer.openAddMgr = function() {
	var index = layer.open({
		type: 2,
		title: '添加供应商',
		fix: false, //不固定
		maxmin: false,
		area:['100%','100%'],
		content: '/manufacturer/addView'
	});
	this.layerIndex = index;
};

/**
 * 供应商列表
 */
MgrManufacturer.list = function() {
	var that = this;
	tb1 = $("#managerTable").bootstrapTable({
		url: "/manufacturer/list",
		toolbar: "#addMgr",
		cache: false,
		showColumns: true,
		showRefresh: true,
		singleSelect: true,
		clickToSelect: true,
		detailView: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		columns: [
			{
				field: "manufacturerName",
				title: "供应商名称",
				align: "center",
			},
			{
				field: "manufacturerNo",
				title: "供应商编号",
				align: "center",
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "address",
				title: "供应商地址",
				align: "center",
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "phone",
				title: "联系电话",
				align: "center",
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "contact",
				title: "负责人",
				align: "center",
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "status",
				title: "状态",
				align: "center",
				formatter: function(value, row, index) {
					return statusTools(row, '1');
				}
			},
			{
				field: "remark",
				title: "备注",
				align: "center",
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "id",
				title: "操作",
				align: "center",
				width: "20%",
				formatter: function(value, row, index) {
					return [
						`<button class='btn btn-xs btn btn-primary openAddBtn' type='button' data-id=${value}><i class='fa fa-plus'>添加联系人</i></button>`,
						"&nbsp;",
						`<button class='btn btn-xs btn btn-warning openEditOutBtn' type='button' data-id=${value}><i class='fa fa-edit'>修改</i></button>`,
					].join("");
				}
			}
		],
		onExpandRow: function(index, row, $detail) {
			MgrManufacturer.InitSubTable(index, row, $detail);
		},
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
			$(".openEditOutBtn").on("click", function(e) {
				var manufacturerId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '修改供应商',
					fix: false,
					maxmin: false,
					area:['100%','100%'],
					content: '/manufacturer/editView/' + manufacturerId
				});
				that.layerIndex = index;
			});
			$(".openAddBtn").on("click", function(e) {
				var manufacturerId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '添加供应商详情',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/manufacturer/addInfoView/' + manufacturerId
				});
				that.layerIndex = index;
			});
		},
	});
};

/**
 * 联系人详情列表
 */
MgrManufacturer.InitSubTable = function(index, row, $detail) {
	var that = this;
	var parentid = row.MENU_ID;
	var cur_table = $detail.html('<table></table>').find('table');
	tb2 = $(cur_table).bootstrapTable({
		url: "/manufacturer/sublist",
		queryParams: { strParentID: parentid },
		ajaxOptions: { strParentID: parentid },
		cache: false,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		columns: [
			{
				field: 'contactName',
				title: '姓名',
				align: 'center',
			},
			{
				field: 'contactPhone',
				title: '电话',
				align: 'center',
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: 'mailAddress',
				title: '邮箱',
				align: 'center',
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "status",
				title: "状态",
				align: "center",
				formatter: function(value, row, index) {
					return statusTools(row, '2');
				}
			},
			{
				field: "remark",
				title: "备注",
				align: "center",
				formatter: function(value, row, index) {
					if (value == '') {
						return "-";
					}
					return value;
				}
			},
			{
				field: "id",
				title: "操作",
				align: "center",
				width: "10%",
				formatter: function(value, row, index) {
					return [
						`<button class='btn btn-xs btn btn-warning openEditBtn' type='button' data-id=${value}><i class='fa fa-edit'>修改</i></button>`,
						"&nbsp;",
						`<button class='btn btn-xs btn btn-black btn-outline  deleteBtn' type='button' data-id=${value}><i class='fa fa-trash'>删除</i></button>`
					].join("");
				}
			}
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
				manufacturerId: row.id,
			};
			return temp;
		},
		onLoadSuccess: function(res) {
			$(".openEditBtn").on("click", function(e) {
				var manufacturerCustomId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '修改供应商详情',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/manufacturer/editInfoView/' + manufacturerCustomId
				});
				that.layerIndex = index;
			});
			$(".deleteBtn").on("click", function(e) {
				var manufacturerCustomId = $(this).data("id");
				var operation = function() {
					var ajax = new $ax(Feng.ctxPath + "/manufacturer/deleteInfo/" + manufacturerCustomId, function(data) {
						Feng.success("删除成功!");
						tb2.bootstrapTable('refresh');
					}, function(data) {
						Feng.error("删除失败!" + data.responseJSON.message + "!");
					});
					ajax.start();
				};
				Feng.confirm("是否删除?", operation);
			});
		},
	});
};

/* 开启状态显示 */
function statusTools(row, type) {
	if (row.status == 'ENABLE') {
		return '<i class=\"fa fa-toggle-on  text-navy fa-2x\" onclick="enable(\'' + row.id + '\',\'' + type + '\')"></i> ';
	} else {
		return '<i class=\"fa fa-toggle-off text-navy fa-2x\" onclick="disable(\'' + row.id + '\',\'' + type + '\')"></i> ';
	}
}

/* 归还管理-启用 */
function disable(manufacturerId, type) {
	var operation = function() {
		var ajax = new $ax("/manufacturer/update/" + type + "/" + manufacturerId + "/1",
			function(data) {
				if (data.code == 200) {
					Feng.success(data.message);
					$("#managerTable").bootstrapTable('refresh');
				} else {
					Feng.info(data.message);
				}
			}, function(data) {
				Feng.error("设置失败!");
			});
		ajax.start();
	};
	Feng.confirm("是否启用当前" + (type == '1' ? "供应商" : "联系人") + "吗?", operation);
}

/*归还管理-停用 */
function enable(manufacturerId, type) {
	var operation = function() {
		var ajax = new $ax("/manufacturer/update/" + type + "/" + manufacturerId + "/0",
			function(data) {
				if (data.code == 200) {
					Feng.success(data.message);
					$("#managerTable").bootstrapTable('refresh');
				} else {
					Feng.info(data.message);
				}
			}, function(data) {
				Feng.error("设置失败!");
			});
		ajax.start();
	};
	Feng.confirm("是否停用当前" + (type == '1' ? "供应商" : "联系人") + "吗?", operation);
}

$(function() {
	MgrManufacturer.list();
});