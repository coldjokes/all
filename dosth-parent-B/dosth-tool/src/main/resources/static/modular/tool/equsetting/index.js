/**
 * 刀具柜管理
 */
var MgrEquSetting = {
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
MgrEquSetting.check = function() {
	var selected = $('#' + this.id).bootstrapTable('getSelections');
	if (selected.length == 0) {
		Feng.info("请先选中表格中的某一记录！");
		return false;
	} else {
		MgrEquSetting.seItem = selected[0];
		return true;
	}
};

/**
 * 查询
 */
MgrEquSetting.search = function() {
	tb1.bootstrapTable('refresh');
};

/**
 * 重置
 */
MgrEquSetting.resetSearch = function() {
	$("#name").val("");
	$("#cabinetType").val("");
	MgrEquSetting.search();
};

/**
 * 添加主柜
 */
MgrEquSetting.openAddMgr = function() {
	var index = layer.open({
		type: 2,
		title: '添加主柜',
		fix: false, //不固定
		maxmin: false,
		area:['100%','100%'],
		content: '/equsetting/addView'
	});
	this.layerIndex = index;
};

/**
 * 修改柜体状态
 */
MgrEquSetting.updateCabinetStatus = function(equSettingId, status) {
	var operation = function() {
		var ajax = new $ax(Feng.ctxPath + "/equsetting/update", function(data) {
			Feng.success("设置成功!");
			tb1.bootstrapTable('refresh');
		}, function(data) {
			Feng.error("设置失败!" + data.responseJSON.message + "!");
		});
		ajax.setData({ 'equSettingId': equSettingId, 'status': status });
		ajax.start();
	};
	Feng.confirm("是否变更当前状态?", operation);
};

/**
 * 主柜列表
 */
MgrEquSetting.getList = function() {
	var that = this;
	tb1 = $("#managerTable").bootstrapTable({
		url: "/equsetting/list",
		cache: false,
		toolbar: '#addMgr',
		showColumns: true,
		showRefresh: true,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		singleSelect: true,
		clickToSelect: true,
		detailView: true,
		sidePagination: "server",
		columns: [
			{
				checkbox: true,
				visible: false
			},
			{
				field: "equSettingName",
				title: "名称",
				align: "center"
			},
			{
				field: "cabinetType",
				title: "类型",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "KNIFE_CABINET_PLC") {
						return "PLC刀具柜";
					} else if (value == "KNIFE_CABINET_DETA") {
						return "行列式A刀具柜";
					} else if (value == "KNIFE_CABINET_DETB") {
						return "行列式B刀具柜";
					} else if (value == "KNIFE_CABINET_C") {
						return "C型柜";
					} else if (value == "SUB_CABINET") {
						return "副柜";
					} else if (value == "TEM_CABINET") {
						return "暂存柜";
					} else if (value == "STORE_CABINET") {
						return "储物柜";
					} else if (value == "VIRTUAL_WAREHOUSE") {
						return "虚拟仓";
					} else if (value == "RECOVERY_CABINET") {
						return "回收柜";
					} else if (value == "TROL_DRAWER") {
						return "可控柜";
					} else {
						console.log(value);
						return "-";
					}
				}
			},
			{
				field: "serialNo",
				title: "序列号",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
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
					if (row.status == 'DISABLE') {
						return '<i class="fa fa-toggle-off text-navy fa-2x" onclick="MgrEquSetting.updateCabinetStatus(\'' + row.id + '\', \'0\')"></i>';
					} else {
						return '<i class="fa fa-toggle-on text-navy fa-2x" onclick="MgrEquSetting.updateCabinetStatus(\'' + row.id + '\', \'1\')"></i>';
					}
				}
			},
			{
				field: "id",
				title: "操作",
				align: "center",
				width: "20%",
				formatter: function(value, row, index) {
					if (row.cabinetType == "VIRTUAL_WAREHOUSE") {
						return [
							`<button class='btn btn-xs btn btn-primary openAddBtn' type='button' data-id=${value}><i class='fa fa-plus'>添加</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-warning openEditSetup' type='button' data-id=${value}><i class='fa fa-pencil'>修改</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-black btn-outline openSetting' type='button' data-id=${value} data-type=${row.cabinetType}><i class='fa fa-gear'>设置</i></button>`,
						].join("");
					} else {
						return [
							`<button class='btn btn-xs btn btn-primary openAddBtn' type='button' data-id=${value}><i class='fa fa-plus'>添加</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-warning openEditSetup' type='button' data-id=${value}><i class='fa fa-pencil'>修改</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-success openSetupBtn' type='button' data-id=${value}><i class='fa fa-file-text'>参数</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-black btn-outline openSetting' type='button' data-id=${value} data-type=${row.cabinetType}><i class='fa fa-gear'>设置</i></button>`,
						].join("");
					}
				}
			}
		],
		onExpandRow: function(index, row, $detail) {
			MgrEquSetting.InitSubTable(index, row, $detail);
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
				cabinetType: $("#cabinetType").val()
			};
			return temp;
		},
		onLoadSuccess: function(res) {
			// 添加
			$(".openAddBtn").on("click", function(e) {
				var equSettingParentId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '添加子柜',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/equsetting/addInfoView/' + equSettingParentId
				});
				that.layerIndex = index;
			});
			// 修改
			$(".openEditSetup").on("click", function(e) {
				var equSettingId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '修改主柜',
					fix: false, //不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/equsetting/editView/' + equSettingId
				});
				that.layerIndex = index;
			});
			// 设置
			$(".openSetting").on("click", function() {
				var equSettingId = $(this).data("id");
				var cabinetType = $(this).data("type");
				var index = layer.open({
					type: 2,
					title: '设置',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/equsetting/setupInfoView/' + equSettingId + "/" + cabinetType
				});
				that.layerIndex = index;
			});
			// 参数
			$(".openSetupBtn").on("click", function(e) {
				var equSettingId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '参数',
					area: ['780px', '600px'], // 宽高
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/equsetting/setupView/' + equSettingId
				});
				that.layerIndex = index;
			});
		}
	});
};

/**
 * 子柜列表
 */
MgrEquSetting.InitSubTable = function(index, row, $detail) {
	var that = this;
	var parentid = row.MENU_ID;
	var cur_table = $detail.html('<table></table>').find('table');
	tb2 = $(cur_table).bootstrapTable({
		url: "/equsetting/sublist",
		queryParams: { strParentID: parentid },
		ajaxOptions: { strParentID: parentid },
		cache: false,
		pagination: true,
		pageNumber: 1,
		pageSize: 10,
		sidePagination: "server",
		columns: [
			{
				field: "equSettingName",
				title: "名称",
				align: "center"
			},
			{
				field: "cabinetType",
				title: "类型",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "KNIFE_CABINET_PLC") {
						return "PLC刀具柜";
					} else if (value == "KNIFE_CABINET_DETA") {
						return "行列式A刀具柜";
					} else if (value == "KNIFE_CABINET_DETB") {
						return "行列式B刀具柜";
					} else if (value == "KNIFE_CABINET_C_A") {
						return "C型柜-A柜";
					} else if (value == "KNIFE_CABINET_C_B") {
						return "C型柜-B柜";
					} else if (value == "SUB_CABINET") {
						return "副柜";
					} else if (value == "TEM_CABINET") {
						return "暂存柜";
					} else if (value == "STORE_CABINET") {
						return "储物柜";
					} else if (value == "VIRTUAL_WAREHOUSE") {
						return "虚拟仓";
					} else if (value == "RECOVERY_CABINET") {
						return "回收柜";
					} else if (value == "TROL_DRAWER") {
						return "可控柜";
					} else {
						return "-";
					}
				}
			},
			{
				field: "serialNo",
				title: "序列号",
				align: "center",
				formatter: function(value, row, index) {
					if (value == "") {
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
					if (row.cabinetType == "RECOVERY_CABINET" || row.cabinetType == "TROL_DRAWER") {
						return [
							`<button class='btn btn-xs btn btn-warning openEditInfoBtn' type='button' data-id=${value}><i class='fa fa-pencil'>修改</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-success openSonSetupBtn' type='button' data-id=${value}><i class='fa fa-file-text'>参数</i></button>`,
						].join("");
					} else if (row.cabinetType == "VIRTUAL_WAREHOUSE") {
						return [
							`<button class='btn btn-xs btn btn-warning openEditInfoBtn' type='button' data-id=${value}><i class='fa fa-pencil'>修改</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-black btn-outline openSonEditSetup' type='button' data-id=${value} data-type=${row.cabinetType}><i class='fa fa-gear'>设置</i></button>`,
						].join("");
					} else {
						return [
							`<button class='btn btn-xs btn btn-warning openEditInfoBtn' type='button' data-id=${value}><i class='fa fa-pencil'>修改</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-success openSonSetupBtn' type='button' data-id=${value}><i class='fa fa-file-text'>参数</i></button>`,
							"&nbsp;", "&nbsp;", "&nbsp;",
							`<button class='btn btn-xs btn btn-black btn-outline openSonEditSetup' type='button' data-id=${value} data-type=${row.cabinetType}><i class='fa fa-gear'>设置</i></button>`,
						].join("");
					}
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
				equSettingParentId: row.id,
			};
			return temp;
		},
		onLoadSuccess: function(res) {
			$(".openEditInfoBtn").on("click", function(e) {
				var equSettingId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '修改子柜',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/equsetting/editInfoView/' + equSettingId
				});
				that.layerIndex = index;
			});
			$(".openSonSetupBtn").on("click", function(e) {
				var equSettingId = $(this).data("id");
				var index = layer.open({
					type: 2,
					title: '参数',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/equsetting/setupView/' + equSettingId
				});
				that.layerIndex = index;
			});
			$(".openSonEditSetup").on("click", function(e) {
				var equSettingId = $(this).data("id");
				var cabinetType = $(this).data("type");
				var index = layer.open({
					type: 2,
					title: '设置',
					fix: false, // 不固定
					maxmin: false,
					area:['100%','100%'],
					content: '/equsetting/setupInfoView/' + equSettingId + "/" + cabinetType
				});
				that.layerIndex = index;
			});
		},
	});
};

$(function() {
	MgrEquSetting.getList();
});